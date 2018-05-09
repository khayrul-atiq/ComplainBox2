package com.example.olife.complainbox2;

/**
 * Created by olife on 4/6/2018.
 */

public class InstituteInformation {




    private String instituteName,instituteLocation,latitude,longitude,phone;


    public InstituteInformation(String instituteName, String instituteLocation, String latitude, String longitude, String phone) {
        this.instituteName = instituteName;
        this.instituteLocation = instituteLocation;
        this.latitude = latitude;
        this.longitude = longitude;
        this.phone = phone;
    }

    public String getInstituteName() {
        return instituteName;
    }

    public String getInstituteLocation() {
        return instituteLocation;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getPhone() {
        return phone;
    }
}
