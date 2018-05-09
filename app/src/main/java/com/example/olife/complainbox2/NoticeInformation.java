package com.example.olife.complainbox2;

import java.io.Serializable;

/**
 * Created by olife on 4/6/2018.
 */
@SuppressWarnings("serial")
public class NoticeInformation implements Serializable{


    /*
    *     private static final String TAG_NOTICE_ID = "noticeID";
    private static final String TAG_NOTICE_TITLE = "noticeTitle";
    private static final String TAG_NOTICE_FILE_NAME = "noticeFileName";
    private static final String TAG_DATE = "data";
    * */

    private String notice_id,notice_title,notice_file_name,notice_date;


    public NoticeInformation(String notice_id, String notice_title, String notice_file_name, String notice_date) {
        this.notice_id = notice_id;
        this.notice_title = notice_title;
        this.notice_file_name = notice_file_name;
        this.notice_date = notice_date;
    }

    public String getNotice_id() {
        return notice_id;
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
