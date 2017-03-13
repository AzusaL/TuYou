package com.waibao.team.tuyou.event;

/**
 * Created by Azusa on 2016/6/6.
 */
public class AddPathWayEvent {
    private int what;
    private int position;

    public AddPathWayEvent(int what, int position) {
        this.what = what;
        this.position = position;
    }

    public int getWhat() {
        return what;
    }

    public int getPosition() {
        return position;
    }
}
