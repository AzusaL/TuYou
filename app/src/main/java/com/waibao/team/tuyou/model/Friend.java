package com.waibao.team.tuyou.model;

public class Friend {
	
	private String id;
	private User own;		//用户
	private User other;		//用户
	private int type;	//0为接受方未同意，1代表已为拒绝，2为好友
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public int getType() {
		return type;
	}
	
	public User getOwn() {
		return own;
	}
	public void setOwn(User own) {
		this.own = own;
	}
	public User getOther() {
		return other;
	}
	public void setOther(User other) {
		this.other = other;
	}
	public void setType(int type) {
		this.type = type;
	}
	
}
