package com.waibao.team.tuyou.event;

import java.util.ArrayList;

/**
 * Created by Azusa on 2016/3/19.
 */
public class AddPhotoEvent {
    private ArrayList<String> imgurl;

    public AddPhotoEvent(ArrayList<String> imgurl) {
        this.imgurl = imgurl;
    }

    public ArrayList<String> getImgurl() {
        return imgurl;
    }
}
