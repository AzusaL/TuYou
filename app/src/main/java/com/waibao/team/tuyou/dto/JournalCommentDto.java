package com.waibao.team.tuyou.dto;

import java.util.Date;

public class JournalCommentDto {

    private String uid;        //用户的id
    private String imgUrl;    //用户的头像
    private String nickname;    //用户伪名
    private Date commentDate;        //用户的评论时间
    private String content;        //评论内容

    public JournalCommentDto() {
    }

    public JournalCommentDto(String uid, String imgUrl, String nickname, Date commentDate, String content) {
        super();
        this.uid = uid;
        this.imgUrl = imgUrl;
        this.nickname = nickname;
        this.commentDate = commentDate;
        this.content = content;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Date getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(Date commentDate) {
        this.commentDate = commentDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
