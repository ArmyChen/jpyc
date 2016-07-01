package com.twopole.model.subject3;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Administrator on 2016/6/13.
 */
@DatabaseTable(tableName = "tb_deduction_item")
public class DeductionItem {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = "imei")
    private String imei;
    @DatabaseField(columnName = "item_name")
    private String item_name;
    @DatabaseField(columnName = "item_reason")
    private String item_reason;
    @DatabaseField(columnName = "item_category_code")
    private String item_category_code;
    @DatabaseField(columnName = "item_score")
    private String item_score;

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

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_category_code() {
        return item_category_code;
    }

    public void setItem_category_code(String item_category_code) {
        this.item_category_code = item_category_code;
    }

    public String getItem_reason() {
        return item_reason;
    }

    public void setItem_reason(String item_reason) {
        this.item_reason = item_reason;
    }

    public String getItem_score() {
        return item_score;
    }

    public void setItem_score(String item_score) {
        this.item_score = item_score;
    }
}
