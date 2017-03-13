package com.waibao.team.tuyou.dto;

public class FriendDto {
	private String id;		//好友的id
	private String imgUrl;		//好友的头像地址
	private String nickname;	//好友的姓名
	private String loginName;	//好友的登陆用户名
	private String fid;			//t_friend的主键
	private int type;			//0为未读，1代表已为拒绝，2为好友

	public String getFid() {
		return fid;
	}
	public void setFid(String fid) {
		this.fid = fid;
	}
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
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	
}
