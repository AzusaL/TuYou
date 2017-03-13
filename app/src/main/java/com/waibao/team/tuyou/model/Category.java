package com.waibao.team.tuyou.model;

public class Category {
	
	private String id;
	private String type;	//类型(国内，国外)
	private String second_type;	//刺激探险，休闲娱乐
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSecond_type() {
		return second_type;
	}
	public void setSecond_type(String second_type) {
		this.second_type = second_type;
	}
}
