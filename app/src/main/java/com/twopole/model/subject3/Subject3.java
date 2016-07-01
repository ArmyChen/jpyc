package com.twopole.model.subject3;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Administrator on 2016/6/13.
 */
@DatabaseTable(tableName = "tb_subject3")
public class Subject3 {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = "imei")
    private String imei;
    @DatabaseField(columnName = "subject3_command")
    private String subject3_command;
    @DatabaseField(columnName = "subject3_command_short")
    private String subject3_command_short;
    @DatabaseField(columnName = "subject3_command_title")
    private String subject3_command_title;
    @DatabaseField(columnName = "subject3_command_order")
    private int subject3_command_order;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getSubject3_command() {
        return subject3_command;
    }

    public void setSubject3_command(String subject3_command) {
        this.subject3_command = subject3_command;
    }

    public String getSubject3_command_short() {
        return subject3_command_short;
    }

    public void setSubject3_command_short(String subject3_command_short) {
        this.subject3_command_short = subject3_command_short;
    }

    public String getSubject3_command_title() {
        return subject3_command_title;
    }

    public void setSubject3_command_title(String subject3_command_title) {
        this.subject3_command_title = subject3_command_title;
    }

    public int getSubject3_command_order() {
        return subject3_command_order;
    }

    public void setSubject3_command_order(int subject3_command_order) {
        this.subject3_command_order = subject3_command_order;
    }
}
