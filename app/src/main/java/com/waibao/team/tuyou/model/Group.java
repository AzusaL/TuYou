package com.waibao.team.tuyou.model;

import java.util.Date;

public class Group {
	private String id;
	private String name;	//组名
	private Date launch_date;	//团发起日期
	private Date begin_date;	//团开始日期
	private Date finsh_date;	//团完成日期
	private byte state;			//0未开始，1准备开始，2进行中，3已结束
	private String age;		//年龄范围
	private int current_people;	//目前人数
	private String imgUrl;		//团图片
	private String description;		//团描述
	private int likes;			//点赞
	private String way;		//路径
	private byte sexType;		//性别要求(0为男，1为女，2为不限)
	private Category category;		//团属于的类别
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Date getLaunch_date() {
		return launch_date;
	}
	public void setLaunch_date(Date launch_date) {
		this.launch_date = launch_date;
	}
	public Date getBegin_date() {
		return begin_date;
	}
	public void setBegin_date(Date begin_date) {
		this.begin_date = begin_date;
	}
	public Date getFinsh_date() {
		return finsh_date;
	}
	public void setFinsh_date(Date finsh_date) {
		this.finsh_date = finsh_date;
	}
	public byte getState() {
		return state;
	}
	public void setState(byte state) {
		this.state = state;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public int getLikes() {
		return likes;
	}
	public void setLikes(int likes) {
		this.likes = likes;
	}
	public String getWay() {
		return way;
	}
	public void setWay(String way) {
		this.way = way;
	}
	public int getCurrent_people() {
		return current_people;
	}
	public void setCurrent_people(int current_people) {
		this.current_people = current_people;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public byte getSexType() {
		return sexType;
	}
	public void setSexType(byte sexType) {
		this.sexType = sexType;
	}
}
