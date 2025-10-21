package com.example.dubbo.consumer.controller;

import com.example.protobuf.UserProto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;

// Java实体类（与Protobuf结构对应）
public class JsonVsProtobufTest {
    public static void main(String[] args) throws JsonProcessingException {
        // 1. 创建测试数据（包含多种数据类型，模拟真实业务对象）
        Map<String, String> metadata = new HashMap<>();
        metadata.put("department", "engineering");
        metadata.put("position", "senior developer");
        metadata.put("hireYear", "2020");

        User javaUser = new User(
                "张三",
                30,
                "zhangsan@example.com",
                true,
                12500.75,
                new int[]{101, 102, 103, 104},
                metadata
        );

        // 2. 转换为Protobuf对象
        UserProto.User.Builder protoBuilder = UserProto.User.newBuilder();
        protoBuilder.setName(javaUser.getName());
        protoBuilder.setAge(javaUser.getAge());
        protoBuilder.setEmail(javaUser.getEmail());
        protoBuilder.setActive(javaUser.isActive());
        protoBuilder.setSalary(javaUser.getSalary());
        for (int role : javaUser.getRoles()) {
            protoBuilder.addRoles(role);
        }
        protoBuilder.putAllMetadata(javaUser.getMetadata());
        UserProto.User protoUser = protoBuilder.build();

        // 3. JSON序列化（使用Jackson）
        ObjectMapper objectMapper = new ObjectMapper();
        byte[] jsonBytes = objectMapper.writeValueAsBytes(javaUser);

        // 4. Protobuf序列化
        byte[] protobufBytes = protoUser.toByteArray();

        // 5. 输出大小对比
        System.out.println("=== 序列化大小对比 ===");
        System.out.println("JSON 大小: " + jsonBytes.length + " 字节");
        System.out.println("Protobuf 大小: " + protobufBytes.length + " 字节");
        System.out.println("差异: " + (jsonBytes.length - protobufBytes.length) + " 字节");
        System.out.println("Protobuf 比 JSON 小 " +
                String.format("%.2f", (1 - (double)protobufBytes.length/jsonBytes.length) * 100) + "%");

        // 6. （可选）打印内容片段（验证数据一致性）
        System.out.println("\n=== 内容片段 ===");
        System.out.println("JSON: " + new String(jsonBytes).substring(0, 50) + "...");
        System.out.println("Protobuf: [二进制数据，长度=" + protobufBytes.length + "]");
    }
}
