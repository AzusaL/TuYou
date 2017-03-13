package com.waibao.team.tuyou.event;

/**
 * Created by Azusa on 2016/6/13.
 */
public class SearchEvent {
    private String key;

    public SearchEvent(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
