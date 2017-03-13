package com.waibao.team.tuyou.dto;

/**
 * Created by Azusa on 2016/6/11.
 */
public class Label {
    private String name;
    private boolean isCheck;
    private int id;

    public Label(String name, boolean isCheck, int id) {
        this.name = name;
        this.isCheck = isCheck;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
