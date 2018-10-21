package com.aimheadshot.buynsell.Model;

/**
 * Created by vipul on 28/9/18.
 */

public class ExchangeItemInformation {

    private String image;
    private String item_name;
    private String item_price;
    private String item_desc;
    private String seller_number;

    public ExchangeItemInformation() {
    }

    public ExchangeItemInformation(String image, String item_name, String item_price, String item_desc, String seller_number) {
        this.image = image;
        this.item_name = item_name;
        this.item_price = item_price;
        this.item_desc = item_desc;
        this.seller_number = seller_number;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_price() {
        return item_price;
    }

    public void setItem_price(String item_price) {
        this.item_price = item_price;
    }

    public String getItem_desc() {
        return item_desc;
    }

    public void setItem_desc(String item_desc) {
        this.item_desc = item_desc;
    }

    public String getSeller_number() {
        return seller_number;
    }

    public void setSeller_number(String seller_number) {
        this.seller_number = seller_number;
    }
}
