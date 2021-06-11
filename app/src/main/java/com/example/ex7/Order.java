package com.example.ex7;


public class Order {

    public String orderId;
    public String customer_name;
    public int pickles;
    public boolean hummus;
    public boolean tahini;
    public String comment;
    public int status;

    public Order()
    {}
    public Order(String id, String customer, int picklesNum, boolean isHummus, boolean isTahini, String comment, int state) {
        this.orderId = id;
        this.customer_name = customer;
        this.pickles = picklesNum;
        this.hummus = isHummus;
        this.tahini = isTahini;
        this.comment = comment;
        this.status = state;
    }

}
