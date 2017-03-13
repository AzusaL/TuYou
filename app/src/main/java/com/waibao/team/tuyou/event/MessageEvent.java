package com.waibao.team.tuyou.event;

/**
 * Created by Delete_exe on 2016/6/3.
 */
public class MessageEvent {
    private boolean isMsgReceive;

    public MessageEvent(boolean isMsgReceive) {
        this.isMsgReceive = isMsgReceive;
    }

    public boolean isMsgReceive() {
        return this.isMsgReceive;
    }
}
