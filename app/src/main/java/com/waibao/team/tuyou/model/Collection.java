package com.waibao.team.tuyou.model;

import java.util.Date;


public class Collection {
	
	private String id;
	private String foreign_id;	//用于存放团id或者日记id
	private User user;	//收藏者的id
	private byte type;	//0为收藏日记，1为收藏团信息
	private Date collectionDate;	//收藏时间
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getForeign_id() {
		return foreign_id;
	}
	public void setForeign_id(String foreign_id) {
		this.foreign_id = foreign_id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public byte getType() {
		return type;
	}
	public void setType(byte type) {
		this.type = type;
	}
	public Date getCollectionDate() {
		return collectionDate;
	}
	public void setCollectionDate(Date collectionDate) {
		this.collectionDate = collectionDate;
	}
}
