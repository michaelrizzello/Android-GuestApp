package com.mikerizzello.guestapp.models;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by michaelrizzello on 2016-12-12.
 */

public class OrderLocation {

    private LatLng orderLocation;

    public OrderLocation(JSONObject jsonObject)
    {
        try
        {
            if (!jsonObject.isNull("latitude") && !jsonObject.isNull("longitude"))
            {
                double lat = Double.parseDouble(jsonObject.getString("latitude"));
                double lng = Double.parseDouble(jsonObject.getString("longitude"));

                this.orderLocation = new LatLng(lat, lng);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    public LatLng getOrderLocation() {
        return orderLocation;
    }

}
