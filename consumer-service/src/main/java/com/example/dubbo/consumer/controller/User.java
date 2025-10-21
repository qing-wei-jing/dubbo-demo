package com.example.dubbo.consumer.controller;

import java.util.Map;

class User {
    private String name;
    private int age;
    private String email;
    private boolean active;
    private double salary;
    private int[] roles;
    private Map<String, String> metadata;

    // 构造函数、getter和setter
    public User(String name, int age, String email, boolean active, double salary,
                int[] roles, Map<String, String> metadata) {
        this.name = name;
        this.age = age;
        this.email = email;
        this.active = active;
        this.salary = salary;
        this.roles = roles;
        this.metadata = metadata;
    }

    // getter和setter（Protobuf不需要这些，但JSON序列化需要）
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getEmail() { return email; }
    public boolean isActive() { return active; }
    public double getSalary() { return salary; }
    public int[] getRoles() { return roles; }
    public Map<String, String> getMetadata() { return metadata; }
}