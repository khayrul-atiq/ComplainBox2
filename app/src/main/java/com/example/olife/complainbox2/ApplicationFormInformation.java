package com.example.olife.complainbox2;

public class ApplicationFormInformation {

    private String applicationFormTitle,applicationFormDescription,applicationFormFileName,applicationFormType,applicationFormUploadedDate;

    private boolean isHeader;

    public ApplicationFormInformation(String applicationFormTitle, String applicationFormDescription, String applicationFormFileName, String applicationFormType, String applicationFormUploadedDate) {
        this.applicationFormTitle = applicationFormTitle;
        this.applicationFormDescription = applicationFormDescription;
        this.applicationFormFileName = applicationFormFileName;
        this.applicationFormType = applicationFormType;
        this.applicationFormUploadedDate = applicationFormUploadedDate;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public ApplicationFormInformation(boolean isHeader, String applicationFormType){
        this.isHeader = isHeader;
        this.applicationFormType = applicationFormType;

    }

    public String getApplicationFormTitle() {
        return applicationFormTitle;
    }

    public String getApplicationFormDescription() {
        return applicationFormDescription;
    }

    public String getApplicationFormFileName() {
        return applicationFormFileName;
    }

    public String getApplicationFormType() {
        return applicationFormType;
    }

    public String getApplicationFormUploadedDate() {
        return applicationFormUploadedDate;
    }
}
