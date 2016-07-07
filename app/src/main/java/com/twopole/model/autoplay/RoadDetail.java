package com.twopole.model.autoplay;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Administrator on 2016-06-18.
 */
@DatabaseTable(tableName = "tb_road_detail")
public class RoadDetail {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = "imei")
    private String imei;
    @DatabaseField(columnName = "road_id")
    private int road_id;
    @DatabaseField(columnName = "road_project_id")//项目id
    private int road_project_id;
    @DatabaseField(columnName = "road_project_name")//项目名
    private String road_project_name;
    @DatabaseField(columnName = "road_param_status")
    private String road_param_status;
    @DatabaseField(columnName = "is_speaker")
    private int is_speaker;
    @DatabaseField(columnName = "is_default")
    private boolean is_default;

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

    public int getRoad_id() {
        return road_id;
    }

    public void setRoad_id(int road_id) {
        this.road_id = road_id;
    }

    public int getRoad_project_id() {
        return road_project_id;
    }

    public void setRoad_project_id(int road_project_id) {
        this.road_project_id = road_project_id;
    }

    public String getRoad_project_name() {
        return road_project_name;
    }

    public void setRoad_project_name(String road_project_name) {
        this.road_project_name = road_project_name;
    }

    public String getRoad_param_status() {
        return road_param_status;
    }

    public void setRoad_param_status(String road_param_status) {
        this.road_param_status = road_param_status;
    }

    public boolean is_default() {
        return is_default;
    }

    public void setIs_default(boolean is_default) {
        this.is_default = is_default;
    }

    public int is_speaker() {
        return is_speaker;
    }

    public void setIs_speaker(int is_speaker) {
        this.is_speaker = is_speaker;
    }
}
