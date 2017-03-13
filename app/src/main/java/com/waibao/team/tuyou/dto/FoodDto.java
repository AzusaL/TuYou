package com.waibao.team.tuyou.dto;

/**
 * Created by Azusa on 2016/6/11.
 */
public class FoodDto {

    /**
     * name : 昌茂酒家
     * navigation : 上海餐厅>>崇明县>>本帮江浙菜>>农家菜>>昌茂酒家
     * city : 上海
     * area : 崇明县
     * address : 崇明县北沿公路1647弄732号(近长江公路)
     * phone : 021-59668272
     * latitude : 31.67416
     * longitude : 121.5385
     * stars : 3.0
     * avg_price : 61
     * photos : http://i3.dpfile.com/pc/f07e5f553e60d11dccd1a75e461d42c1/36504707_m.jpg
     * tags : 农家菜
     * all_remarks : 16
     * very_good_remarks : 1
     * good_remarks : 2
     * common_remarks : 1
     * bad_remarks : 0
     * very_bad_remarks : 0
     * product_rating : 6.3
     * environment_rating : 6.1
     * service_rating : 6.6
     * recommended_products :
     * recommended_dishes : 红烧山羊肉,大闸蟹,呛虾,香辣螺蛳,白菜鹅肉,年糕白蟹,红烧猪脚,老母鸡汤,干烧胖头鱼,红烧野生黄鳝,白斩鸡
     * nearby_shops :
     */

    private String name;  //店名
    private String navigation;   //类导航
    private String city;    //	所属城市
    private String area;       //	所属区域
    private String address;   //	详细地址
    private String phone;      //	联系电话
    private String latitude;     //坐标纬度
    private String longitude;     //	坐标经度
    private String stars;          //	星级，总五星
    private String avg_price;       //	人均消费
    private String photos;       //	图片URL
    private String tags;        //	标签
    private String all_remarks;       //	总点评数,
    private String very_good_remarks;      //非常好
    private String good_remarks;        //	好评
    private String common_remarks;          //普通
    private String bad_remarks;        //	差
    private String very_bad_remarks;          //	非常差
    private String product_rating;         //	产品评分
    private String environment_rating;       //	环境评分
    private String service_rating;            //服务评分
    private String recommended_products;        //推荐产品
    private String recommended_dishes;             //推荐菜色
    private String nearby_shops;             //周边美食

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNavigation() {
        return navigation;
    }

    public void setNavigation(String navigation) {
        this.navigation = navigation;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getStars() {
        return stars;
    }

    public void setStars(String stars) {
        this.stars = stars;
    }

    public String getAvg_price() {
        return avg_price;
    }

    public void setAvg_price(String avg_price) {
        this.avg_price = avg_price;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getAll_remarks() {
        return all_remarks;
    }

    public void setAll_remarks(String all_remarks) {
        this.all_remarks = all_remarks;
    }

    public String getVery_good_remarks() {
        return very_good_remarks;
    }

    public void setVery_good_remarks(String very_good_remarks) {
        this.very_good_remarks = very_good_remarks;
    }

    public String getGood_remarks() {
        return good_remarks;
    }

    public void setGood_remarks(String good_remarks) {
        this.good_remarks = good_remarks;
    }

    public String getCommon_remarks() {
        return common_remarks;
    }

    public void setCommon_remarks(String common_remarks) {
        this.common_remarks = common_remarks;
    }

    public String getBad_remarks() {
        return bad_remarks;
    }

    public void setBad_remarks(String bad_remarks) {
        this.bad_remarks = bad_remarks;
    }

    public String getVery_bad_remarks() {
        return very_bad_remarks;
    }

    public void setVery_bad_remarks(String very_bad_remarks) {
        this.very_bad_remarks = very_bad_remarks;
    }

    public String getProduct_rating() {
        return product_rating;
    }

    public void setProduct_rating(String product_rating) {
        this.product_rating = product_rating;
    }

    public String getEnvironment_rating() {
        return environment_rating;
    }

    public void setEnvironment_rating(String environment_rating) {
        this.environment_rating = environment_rating;
    }

    public String getService_rating() {
        return service_rating;
    }

    public void setService_rating(String service_rating) {
        this.service_rating = service_rating;
    }

    public String getRecommended_products() {
        return recommended_products;
    }

    public void setRecommended_products(String recommended_products) {
        this.recommended_products = recommended_products;
    }

    public String getRecommended_dishes() {
        return recommended_dishes;
    }

    public void setRecommended_dishes(String recommended_dishes) {
        this.recommended_dishes = recommended_dishes;
    }

    public String getNearby_shops() {
        return nearby_shops;
    }

    public void setNearby_shops(String nearby_shops) {
        this.nearby_shops = nearby_shops;
    }
}
