package com.waibao.team.tuyou.dto;

import java.util.Date;

public class JournalDto {

    private String id;            //日记id
    private String content;        //日记内容
    private String title;        //日记标题
    private String imgUrl;        //日记图片
    private int zan;            //赞
    private int collectionCount;    //收藏次数
    private Date postDate;        //发布日记时间
    private int commentCount;    //评论条数
    private String uid;            //用户id
    private String uimgUrl;        //用户的照片
    private String nickname;    //用户名
    private String description;    //用户个性签名
    private String group_id;    //团id
    private String group_way;        //团行程
    private byte flag;            //是否被收藏

    public JournalDto() {
    }

    public JournalDto(String content, String title, String group_id, String group_way) {
        this.content = content;
        this.title = title;
        this.group_id = group_id;
        this.group_way = group_way;
    }

    public int getCollectionCount() {
        return collectionCount;
    }

    public void setCollectionCount(int collectionCount) {
        this.collectionCount = collectionCount;
    }

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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getZan() {
        return zan;
    }

    public void setZan(int zan) {
        this.zan = zan;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUimgUrl() {
        return uimgUrl;
    }

    public void setUimgUrl(String uimgUrl) {
        this.uimgUrl = uimgUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public byte getFlag() {
        return flag;
    }

    public void setFlag(byte flag) {
        this.flag = flag;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getGroup_way() {
        return group_way;
    }

    public void setGroup_way(String group_way) {
        this.group_way = group_way;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
}
