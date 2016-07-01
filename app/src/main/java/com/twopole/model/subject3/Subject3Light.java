package com.twopole.model.subject3;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Administrator on 2016-06-08.
 */
@DatabaseTable(tableName = "tb_subject3_light_settings")
public class Subject3Light {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = "imei")
    private String imei;
    @DatabaseField(columnName = "voice")
    private String voice;

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

    public String getVoice() {
        return voice;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }
}
