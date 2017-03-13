package com.waibao.team.tuyou.dto;

import java.util.Date;

public class CollectionDto {
	
	private String id;		//团或游记的id
	private String imgUrl;	//团或游记的照片
	private String uid;		//用户的id
	private Date postDate;	//发布的时间
	private String uImgUrl;	//用户的头像
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public Date getPostDate() {
		return postDate;
	}
	public void setPostDate(Date postDate) {
		this.postDate = postDate;
	}
	public String getuImgUrl() {
		return uImgUrl;
	}
	public void setuImgUrl(String uImgUrl) {
		this.uImgUrl = uImgUrl;
	}
}
