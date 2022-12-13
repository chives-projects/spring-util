package com.csc.spring.demo.po;

import org.springframework.core.Ordered;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @description:
 * @Author :  csc
 * @create: 2022/11/18
*/
public class User implements Ordered {
    private String username = "liming";
    private String password;
    @Valid
    @NotEmpty
    private List<Job> jobList;
    private int order;

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

    public List<Job> getJobList() {
        return jobList;
    }

    public void setJobList(List<Job> jobList) {
        this.jobList = jobList;
    }

    public void setOrder(int order) {
        this.order = order;
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
