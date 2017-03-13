package com.waibao.team.tuyou.dto;

import java.util.Date;

/**
 * Created by Delete_exe on 2016/6/9.
 */
public class VoteDto {
    private String id;
    private String content;        //投票内容
    private String title;        //标题
    private String total;        //选项统计
    private int count;        //投票的目前人数
    private Date postDate;    //投票发起时间
    private String group_id;    //发起投票的团

    //发起投票人
    private String uid;        //发起投票人的id
    private String nickname;    //发起投票人的名称
    private String imgUrl;        //发起投票人的头像

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }
}
