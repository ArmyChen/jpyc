package com.twopole.model.subject3;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Administrator on 2016/6/13.
 */
@DatabaseTable(tableName = "tb_deduction_category")
public class DeductionCategory {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = "imei")
    private String imei;
    @DatabaseField(columnName = "category_code")
    private String category_code;
    @DatabaseField(columnName = "category_name")
    private String category_name;

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

    public String getCategory_code() {
        return category_code;
    }

    public void setCategory_code(String category_code) {
        this.category_code = category_code;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }
}
