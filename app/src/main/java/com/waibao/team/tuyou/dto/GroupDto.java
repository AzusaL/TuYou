package com.waibao.team.tuyou.dto;


public class GroupDto {
	private String id;        //组id
	private String age;        //年龄段
	private int current_people;    //目前人数
	private String imgUrl;        //团图片
	private String description;        //团描述
	private int collectionCount;            //收藏人数
	private String way;        //路径
	private String wayTime;        //路径时间
	private String sexType;        //性别要求(2为男，1为女，0为不限)
	private float km;            //团所需的公里数
	private long talkId;        //群聊id
	private double lat;        //团的纬度
	private double lng;        //团的经度
	private int credit;        //团的信誉度需求的级别
	private int status;        //0为未开始，1以进行中，2为已结束

	private long flag;        //是否被收藏(0为未，1为被收藏)
	private String uid;    //用户对象
	private String uImgUrl;    //用户图片
	private String uDescription;    //用户个性签名
	private float uKm;            //用户的总共路程
	private int uTimes;        //用户的次数
	private String uNickname;    //用户名


	public GroupDto() {

	}


	public GroupDto(String id, String age, int current_people, String imgUrl, String description, int collectionCount,
					String way, String wayTime, String sexType, float km, long talkId, double lat, double lng, int credit,
					String uid, String uImgUrl, String uDescription, float uKm, int uTimes, String uNickname) {
		super();
		this.id = id;
		this.age = age;
		this.current_people = current_people;
		this.imgUrl = imgUrl;
		this.description = description;
		this.collectionCount = collectionCount;
		this.way = way;
		this.wayTime = wayTime;
		this.sexType = sexType;
		this.km = km;
		this.talkId = talkId;
		this.lat = lat;
		this.lng = lng;
		this.credit = credit;
		this.uid = uid;
		this.uImgUrl = uImgUrl;
		this.uDescription = uDescription;
		this.uKm = uKm;
		this.uTimes = uTimes;
		this.uNickname = uNickname;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getFlag() {
		return flag;
	}

	public void setFlag(long flag) {
		this.flag = flag;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getKm() {
		return km;
	}

	public void setKm(float km) {
		this.km = km;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public int getCollectionCount() {
		return collectionCount;
	}

	public void setCollectionCount(int collectionCount) {
		this.collectionCount = collectionCount;
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

	public String getSexType() {
		return sexType;
	}

	public void setSexType(String sexType) {
		this.sexType = sexType;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getWayTime() {
		return wayTime;
	}

	public void setWayTime(String wayTime) {
		this.wayTime = wayTime;
	}

	//用户
	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUImgUrl() {
		return uImgUrl;
	}

	public void setUImgUrl(String uImgUrl) {
		this.uImgUrl = uImgUrl;
	}

	public long getTalkId() {
		return talkId;
	}

	public void setTalkId(long talkId) {
		this.talkId = talkId;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public int getCredit() {
		return credit;
	}

	public void setCredit(int credit) {
		this.credit = credit;
	}

	public float getUKm() {
		return uKm;
	}

	public void setUKm(float uKm) {
		this.uKm = uKm;
	}

	public int getUTimes() {
		return uTimes;
	}

	public void setUTimes(int uTimes) {
		this.uTimes = uTimes;
	}

	public String getUNickname() {
		return uNickname;
	}

	public void setUNickname(String uNickname) {
		this.uNickname = uNickname;
	}


	public String getUDescription() {
		return uDescription;
	}


	public void setUDescription(String uDescription) {
		this.uDescription = uDescription;
	}


}
