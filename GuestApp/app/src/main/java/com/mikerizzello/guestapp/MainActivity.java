package com.mikerizzello.guestapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mikerizzello.guestapp.api.APIManager;
import com.mikerizzello.guestapp.managers.DataManager;
import com.mikerizzello.guestapp.managers.LocationManager;
import com.mikerizzello.guestapp.models.Order;
import com.mikerizzello.guestapp.models.OrderLocation;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private SupportMapFragment mMapFragment;
    private TextView orderIdLabel;
    private GoogleMap googleMap;

    private Marker marker;

    private Handler handler = new Handler();
    private Runnable locationPoll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        if (mMapFragment == null) {
            mMapFragment = SupportMapFragment.newInstance();
            mMapFragment.getMapAsync(this);
        }

        Button createOrderButton = (Button)findViewById(R.id.create_order);

        this.orderIdLabel = (TextView) findViewById(R.id.order_id_label);

        createOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createOrder();
            }
        });

        createPoll();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.


            return;
        }

        LatLng userPosition = new LatLng(LocationManager.getInstance().getGpsTracker().getLatitude(), LocationManager.getInstance().getGpsTracker().getLongitude());

        this.googleMap = googleMap;
        this.googleMap.setMyLocationEnabled(true);
        this.showOrderLocation(userPosition);
    }

    private void createOrder()
    {
        APIManager.getInstance().createOrder(new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, org.json.JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                if (response.length() > 0)
                {
                    DataManager.getInstance().setCurrentOrder(new Order(response));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, java.lang.String responseString, java.lang.Throwable throwable) {

                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, java.lang.Throwable throwable, org.json.JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

            }
        });
    }

    private void trackOrder()
    {
        APIManager.getInstance().trackOrder(DataManager.getInstance().getCurrentOrder().getOrderID(), new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, org.json.JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                if (response.length() > 0)
                {
                    DataManager.getInstance().getCurrentOrder().setOrderLocation(new OrderLocation(response));
                    LatLng location = DataManager.getInstance().getCurrentOrder().getOrderLocation().getOrderLocation();

                    if (marker == null)
                    {
                        drawMarker(location);
                    }
                    else {
                        marker.setPosition(location);
                    }

                    showOrderLocation(location);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, java.lang.String responseString, java.lang.Throwable throwable) {

                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, java.lang.Throwable throwable, org.json.JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

            }
        });
    }

    private void drawMarker(LatLng location)
    {
        MarkerOptions options = new MarkerOptions();
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.taxi_other));
        options.position(location);
        this.marker = this.googleMap.addMarker(options);
    }

    private void showOrderLocation(LatLng location)
    {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(location, 16);
        this.googleMap.animateCamera(cameraUpdate);
    }

    public void createPoll()
    {
        locationPoll = new Runnable() {
            @Override
            public void run() {

                if (DataManager.getInstance().getCurrentOrder() != null) {
                    trackOrder();
                }

                handler.postDelayed(locationPoll, 5000);

            }
        };

        locationPoll.run();

    }
}
