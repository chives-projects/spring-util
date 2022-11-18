package com.csc.spring.demo.po;

import org.springframework.core.Ordered;

/**
 * @description:
 * @Author :  csc
 * @create: 2022/11/18
*/
public class User implements Ordered {
    private String username = "liming";
    private String password;
    private Job job;
    private int order;

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("User:" + username + " finalize");
    }

    @Override
    public String toString() {
        return this.getUsername();
    }

    @Override
    public int getOrder() {
        return order;
    }
}
