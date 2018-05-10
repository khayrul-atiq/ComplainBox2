package com.example.olife.complainbox2;

public class ProblemInformation {

    private String problemDescription,problemCategory,problemSubmissionDate,problemStatus;

    public ProblemInformation(String problemDescription, String problemCategory, String problemSubmissionDate, String problemStatus) {
        this.problemDescription = problemDescription;
        this.problemCategory = problemCategory;
        this.problemSubmissionDate = problemSubmissionDate;
        this.problemStatus = problemStatus;
    }

    public String getProblemDescription() {
        return problemDescription;
    }

    public String getProblemCategory() {
        return problemCategory;
    }

    public String getProblemSubmissionDate() {
        return problemSubmissionDate;
    }

    public String getProblemStatus() {
        return problemStatus;
    }
}
