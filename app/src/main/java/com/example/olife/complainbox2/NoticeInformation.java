package com.example.olife.complainbox2;

import java.io.Serializable;

/**
 * Created by olife on 4/6/2018.
 */
@SuppressWarnings("serial")
public class NoticeInformation implements Serializable{


    private String notice_title,notice_file_name,notice_date;


    public NoticeInformation( String notice_title, String notice_file_name, String notice_date) {

        this.notice_title = notice_title;
        this.notice_file_name = notice_file_name;
        this.notice_date = notice_date;
    }


    public String getNotice_title() {
        return notice_title;
    }

    public String getNotice_file_name() {
        return notice_file_name;
    }

    public String getNotice_date() {
        return notice_date;
    }

}
