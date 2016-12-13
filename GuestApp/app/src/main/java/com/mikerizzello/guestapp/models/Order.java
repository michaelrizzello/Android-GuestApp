package com.mikerizzello.guestapp.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by michaelrizzello on 2016-12-12.
 */

public class Order {

    private int orderID;

    private OrderLocation orderLocation;

    public Order()
    {
        this.orderID = 0;
    }
    public Order(int orderID)
    {
        this.orderID = orderID;
    }

    public Order(JSONObject jsonObject)
    {
        try
        {
            if (!jsonObject.isNull("orderId"))
            {
                this.orderID = Integer.parseInt(jsonObject.getString("orderId"));
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    public int getOrderID() {
        return orderID;
    }

    public OrderLocation getOrderLocation() {
        return orderLocation;
    }

    public void setOrderLocation(OrderLocation orderLocation) {
        this.orderLocation = orderLocation;
    }

}
