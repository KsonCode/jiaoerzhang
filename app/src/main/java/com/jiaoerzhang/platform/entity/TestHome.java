package com.jiaoerzhang.platform.entity;import com.jiaoerzhang.platform.lib_net.network.BaseResponse;public class TestHome extends BaseResponse {    private String title;    private int itemType;    private String imgUrl;    public String getTitle() {        return title;    }    public void setTitle(String title) {        this.title = title;    }    public int getItemType() {        return itemType;    }    public void setItemType(int itemType) {        this.itemType = itemType;    }    public String getImgUrl() {        return imgUrl;    }    public void setImgUrl(String imgUrl) {        this.imgUrl = imgUrl;    }}