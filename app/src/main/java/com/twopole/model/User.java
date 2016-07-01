package com.twopole.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "tb_user")
public class User
{
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = "name")
    private String name;
    @DatabaseField(columnName = "desc")
    private String desc;
    @DatabaseField(columnName = "imei")
    private String imei;
    @DatabaseField(columnName = "first_install")
    private boolean first_install;
    @DatabaseField(columnName = "native_number")
    private String native_number;


    public User()
    {
    }

    public User(String name, String desc)
    {
        this.name = name;
        this.desc = desc;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDesc()
    {
        return desc;
    }

    public void setDesc(String desc)
    {
        this.desc = desc;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getNative_number() {
        return native_number;
    }

    public void setNative_number(String native_number) {
        this.native_number = native_number;
    }

    public boolean isFirst_install() {
        return first_install;
    }

    public void setFirst_install(boolean first_install) {
        this.first_install = first_install;
    }
}