package edu.bbte.idde.keim2152.spring.tasks;

import lombok.Data;

import java.util.Date;

@Data
public class Statistics {
    private String fullName;
    private Double sum;
    private Double avg;
    private Date createdAt;

    public Statistics(String fullName, Double sum, Double avg) {
        this.avg = avg;
        this.sum = sum;
        this.fullName = fullName;
        this.createdAt = new Date();
    }
}
