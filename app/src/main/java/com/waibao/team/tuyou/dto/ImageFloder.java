package com.waibao.team.tuyou.dto;

/**
 * Created by Azusa on 2016/3/9.
 */
public class ImageFloder {

    private String dir; //图片的文件夹路径
    private String firstImagePath; //第一张图片的路径
    private String name; //文件夹的名称
    private int count; //图片的数量
    private boolean isSelected;  //文件夹是否被选中

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
        int lastIndexOf = this.dir.lastIndexOf("/");
        this.name = this.dir.substring(lastIndexOf);
    }

    public String getFirstImagePath() {
        return firstImagePath;
    }

    public void setFirstImagePath(String firstImagePath) {
        this.firstImagePath = firstImagePath;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
