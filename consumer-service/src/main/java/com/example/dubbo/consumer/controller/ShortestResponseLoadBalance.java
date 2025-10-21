package com.example.dubbo.consumer.controller;

import org.apache.dubbo.rpc.cluster.loadbalance.AbstractLoadBalance;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.threadpool.manager.FrameworkExecutorRepository;
import org.apache.dubbo.common.utils.ConcurrentHashMapUtils;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.RpcStatus;
import org.apache.dubbo.rpc.cluster.Constants;
import org.apache.dubbo.rpc.model.ApplicationModel;
import org.apache.dubbo.rpc.model.ScopeModelAware;
import org.apache.dubbo.rpc.support.RpcUtils;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class ShortestResponseLoadBalance extends AbstractLoadBalance implements ScopeModelAware {

    public static final String NAME = "shortestresponse";

    private int slidePeriod = 30_000;

    private final ConcurrentMap<RpcStatus, SlideWindowData> methodMap = new ConcurrentHashMap<>();

    private final AtomicBoolean onResetSlideWindow = new AtomicBoolean(false);

    private volatile long lastUpdateTime = System.currentTimeMillis();

    private ExecutorService executorService;

    private List<Long> responseTimes = new CopyOnWriteArrayList<>(); // 存储窗口内的响应时间

    @Override
    public void setApplicationModel(ApplicationModel applicationModel) {
        slidePeriod = applicationModel
                .modelEnvironment()
                .getConfiguration()
                .getInt(Constants.SHORTEST_RESPONSE_SLIDE_PERIOD, 30_000);
        executorService = applicationModel
                .getFrameworkModel()
                .getBeanFactory()
                .getBean(FrameworkExecutorRepository.class)
                .getSharedExecutor();
    }

    protected static class SlideWindowData {

        private long succeededOffset;
        private long succeededElapsedOffset;
        private final RpcStatus rpcStatus;

        public SlideWindowData(RpcStatus rpcStatus) {
            this.rpcStatus = rpcStatus;
            this.succeededOffset = 0;
            this.succeededElapsedOffset = 0;
        }

        public void reset() {
            this.succeededOffset = rpcStatus.getSucceeded();
            this.succeededElapsedOffset = rpcStatus.getSucceededElapsed();
        }

        private long getSucceededAverageElapsed() {
            long succeed = this.rpcStatus.getSucceeded() - this.succeededOffset;
            if (succeed == 0) {
                return 0;
            }
            return (this.rpcStatus.getSucceededElapsed() - this.succeededElapsedOffset) / succeed;
        }

        public long getEstimateResponse() {
            int active = this.rpcStatus.getActive() + 1;
            return getSucceededAverageElapsed() * active;
        }
    }

    @Override
    protected <T> Invoker<T> doSelect(List<Invoker<T>> invokers, URL url, Invocation invocation) {
        // Number of invokers
        int length = invokers.size();
        // Estimated shortest response time of all invokers
        long shortestResponse = Long.MAX_VALUE;
        // The number of invokers having the same estimated shortest response time
        int shortestCount = 0;
        // The index of invokers having the same estimated shortest response time
        int[] shortestIndexes = new int[length];
        // the weight of every invokers
        int[] weights = new int[length];
        // The sum of the warmup weights of all the shortest response  invokers
        int totalWeight = 0;
        // The weight of the first shortest response invokers
        int firstWeight = 0;
        // Every shortest response invoker has the same weight value?
        boolean sameWeight = true;

        // Filter out all the shortest response invokers
        for (int i = 0; i < length; i++) {
            Invoker<T> invoker = invokers.get(i);
            RpcStatus rpcStatus = RpcStatus.getStatus(invoker.getUrl(), RpcUtils.getMethodName(invocation));
            SlideWindowData slideWindowData =
                    ConcurrentHashMapUtils.computeIfAbsent(methodMap, rpcStatus, SlideWindowData::new);

            // Calculate the estimated response time from the product of active connections and succeeded average
            // elapsed time.
            long estimateResponse = slideWindowData.getEstimateResponse();
            int afterWarmup = getWeight(invoker, invocation);
            weights[i] = afterWarmup;
            // Same as LeastActiveLoadBalance
            if (estimateResponse < shortestResponse) {
                shortestResponse = estimateResponse;
                shortestCount = 1;
                shortestIndexes[0] = i;
                totalWeight = afterWarmup;
                firstWeight = afterWarmup;
                sameWeight = true;
            } else if (estimateResponse == shortestResponse) {
                shortestIndexes[shortestCount++] = i;
                totalWeight += afterWarmup;
                if (sameWeight && i > 0 && afterWarmup != firstWeight) {
                    sameWeight = false;
                }
            }
        }

        if (System.currentTimeMillis() - lastUpdateTime > slidePeriod
                && onResetSlideWindow.compareAndSet(false, true)) {
            // reset slideWindowData in async way
            executorService.execute(() -> {
                methodMap.values().forEach(SlideWindowData::reset);
                lastUpdateTime = System.currentTimeMillis();
                onResetSlideWindow.set(false);
            });
        }

        if (shortestCount == 1) {
            return invokers.get(shortestIndexes[0]);
        }
        if (!sameWeight && totalWeight > 0) {
            int offsetWeight = ThreadLocalRandom.current().nextInt(totalWeight);
            for (int i = 0; i < shortestCount; i++) {
                int shortestIndex = shortestIndexes[i];
                offsetWeight -= weights[shortestIndex];
                if (offsetWeight < 0) {
                    return invokers.get(shortestIndex);
                }
            }
        }
        return invokers.get(shortestIndexes[ThreadLocalRandom.current().nextInt(shortestCount)]);
    }

    public void addResponseTime(long elapsed) {
        responseTimes.add(elapsed);
        // 超过窗口内最大样本数时移除最早的（避免内存膨胀）
        if (responseTimes.size() > MAX_SAMPLES) {
            responseTimes.remove(0);
        }
    }

    // 计算90%分位值
    private long get90thPercentile() {
        if (responseTimes.isEmpty()) return 0;
        Collections.sort(responseTimes);
        int index = (int) (responseTimes.size() * 0.9);
        return responseTimes.get(index);
    }

    // 预估响应时间改用分位值
    public long getEstimateResponse() {
        int active = this.rpcStatus.getActive() + 1;
        return get90thPercentile() * active;
    }
}
