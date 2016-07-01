package com.twopole.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Administrator on 2016-06-08.
 */
@DatabaseTable(tableName = "tb_settings")
public class Settings {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = "imei")
    private String imei;
    @DatabaseField(columnName = "subject3_light_timespan")
    private String subject3_light_timespan;


    public Settings()
    {
    }

    public Settings(String imei, String subject3_light_timespan)
    {
        this.imei = imei;
        this.subject3_light_timespan = subject3_light_timespan;
    }

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

    public String getSubject3_light_timespan() {
        return subject3_light_timespan;
    }

    public void setSubject3_light_timespan(String subject3_light_timespan) {
        this.subject3_light_timespan = subject3_light_timespan;
    }

}
