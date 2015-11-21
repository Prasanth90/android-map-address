package com.example.prasanthaj.map;
import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity {

    private GoogleMap mMap;

    protected static String STATIC_MAP_API_ENDPOINT = "http://maps.google.com/maps/api/staticmap?center=" +"37.4223662" +"," + "-122.0839445"+"&zoom=18&size=400x200&sensor=true";


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        // Enable MyLocation Layer of Google Map
        mMap.setMyLocationEnabled(true);

        double latitude = 12.978590;
        double longitude = 80.213008;

        STATIC_MAP_API_ENDPOINT = "http://maps.google.com/maps/api/staticmap?center=" +Double.toString(latitude) +"," +Double.toString(longitude)+"&zoom=16&size=400x200&sensor=true";

        GotoMyLocation();

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                MarkerOptions marker = new MarkerOptions()
                        .position(new LatLng(point.latitude, point.longitude))
                        .title("Use this Location");
                mMap.addMarker(marker);
                System.out.println(point.latitude + "---" + point.longitude);
                UpdateAddress(point.latitude, point.longitude);
            }
        });

        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                GotoMyLocation();
                return true;
            }
        });


/*
        AsyncTask<Void, Void, Bitmap> setImageFromUrl = new AsyncTask<Void, Void, Bitmap>(){
            @Override
            protected Bitmap doInBackground(Void... params) {
                Bitmap bmp = null;
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet request = new HttpGet(STATIC_MAP_API_ENDPOINT);

                InputStream in = null;
                try {
                    in = httpclient.execute(request).getEntity().getContent();
                    bmp = BitmapFactory.decodeStream(in);
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return bmp;
            }
            protected void onPostExecute(Bitmap bmp) {
                if (bmp!=null) {
                    final ImageView iv = (ImageView) findViewById(R.id.img);
                    iv.setImageBitmap(bmp);
                }
            }
        };
        setImageFromUrl.execute();*/

    }


    private void GotoMyLocation(){


        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Create a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Get the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        Location myLocation = locationManager.getLastKnownLocation(provider);

        // set map type
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Get latitude of the current location
        double latitude = myLocation.getLatitude();

        // Get longitude of the current location
        double longitude = myLocation.getLongitude();

        STATIC_MAP_API_ENDPOINT = "http://maps.google.com/maps/api/staticmap?center=" +Double.toString(latitude) +"," +Double.toString(longitude)+"&zoom=16&size=400x200&sensor=true";

        GotoLocation(latitude,longitude);

        UpdateAddress(latitude, longitude);
    }


    private void GotoLocation(double latitude,double longitude)
    {
        // Create a LatLng object for the current location
        LatLng latLng = new LatLng(latitude, longitude);
        // Show the current location in Google Map
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)      // Sets the center of the map to LatLng (refer to previous snippet)
                .zoom(18)            // Sets the zoom
                .bearing(0)          // Sets the orientation of the camera to east
                .tilt(40)            // Sets the tilt of the camera to 30 degrees
                .build();            // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.addMarker(new MarkerOptions().position(latLng).title("Your Delivery address").snippet("Consider yourself located"));
    }


    private void UpdateAddress(double latitude, double longitude)
    {
        try {
            Geocoder geocoder;
            List<Address> addresses;

            geocoder = new Geocoder(this, Locale.getDefault());

            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            TextView addressBox = (TextView) findViewById(R.id.address_item_flat);
            TextView cityBox = (TextView) findViewById(R.id.address_item_address);

            int lines = addresses.get(0).getMaxAddressLineIndex();
            String address = "";
            for (int i = 0; i <= lines; i++) {
                address = address + "\n" + addresses.get(0).getAddressLine(i) + ",";
            }

            String city = addresses.get(0).getLocality();
            addressBox.setText(address);
            cityBox.setText(city);
        }
        catch (IOException e)
        {

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
