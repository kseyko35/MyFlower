package com.denes.myflower;

import java.util.Date;

/**
 * Created by seyfi on 17.3.2018.
 */

public class PostFlower extends FlowerPostId {

    String flower_name,image_url,type,user_id,date,image_thumb;
    Date timestamp;



    public PostFlower(){}

    public String getFlower_name() {
        return flower_name;
    }

    public void setFlower_name(String flower_name) {
        this.flower_name = flower_name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getType() {
        return type;
    }

    public PostFlower(String flower_name, String image_url, String type,String image_thumb, String user_id, String date,Date timestamp) {
        this.flower_name = flower_name;
        this.image_url = image_url;
        this.type = type;
        this.user_id = user_id;
        this.date = date;
        this.timestamp=timestamp;
        this.image_thumb=image_thumb;
    }
    public String getImage_thumb() {
        return image_thumb;
    }

    public void setImage_thumb(String image_thumb) {
        this.image_thumb = image_thumb;
    }

    public void setType(String type) {
        this.type = type;

    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

}
