package com.mycompany.grocerystore.Models;

public class OrderUserDetail {
    String orderId;
    String consumer;
    String userId;
    String date, time;
    String phoneNumber;
    int total;


    public OrderUserDetail(String orderId, String consumer, String userId, String date, String time,
                           int total, String phoneNumber) {
        this.orderId = orderId;
        this.consumer = consumer;
        this.userId = userId;
        this.date = date;
        this.time = time;
        this.phoneNumber = phoneNumber;
        this.total = total;
    }

    public OrderUserDetail() {
    }

    public String getUserId() {
        return userId;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getConsumer() {
        return consumer;
    }

    public String getOrderId() {
        return orderId;
    }

    public int getTotal() {
        return total;
    }

}
