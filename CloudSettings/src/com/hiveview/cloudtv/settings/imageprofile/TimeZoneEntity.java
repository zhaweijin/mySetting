package com.hiveview.cloudtv.settings.imageprofile;

/**
 * Created by liulifeng on 16/6/7.
 */

public class TimeZoneEntity {

    private String id;

    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TimeZoneEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
