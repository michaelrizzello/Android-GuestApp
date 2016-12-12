package com.mikerizzello.guestapp.managers;

import com.mikerizzello.guestapp.models.Order;

/**
 * Created by michaelrizzello on 2016-12-12.
 */
public class DataManager {
    private static DataManager ourInstance = new DataManager();

    public static DataManager getInstance() {
        return ourInstance;
    }

    private Order currentOrder;

    private DataManager() {
    }

    public Order getCurrentOrder() {
        return currentOrder;
    }

    public void setCurrentOrder(Order currentOrder) {
        this.currentOrder = currentOrder;
    }

}
