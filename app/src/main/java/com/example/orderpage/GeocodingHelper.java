package com.example.orderpage;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

public class GeocodingHelper {

    public static LatLng getLocationFromAddress(String address, Geocoder geocoder) {
        LatLng location = null;

        try {
            List<Address> addresses = geocoder.getFromLocationName(address, 1);
            if (addresses != null && addresses.size() > 0) {
                Address firstAddress = addresses.get(0);
                double latitude = firstAddress.getLatitude();
                double longitude = firstAddress.getLongitude();
                location = new LatLng(latitude, longitude);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return location;
    }
}
