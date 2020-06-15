package com.example.preventionapp;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.CircleOverlay;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class CrimeMap_Rape extends AppCompatActivity implements Overlay.OnClickListener, OnMapReadyCallback, NaverMap.OnCameraChangeListener, NaverMap.OnCameraIdleListener {

    private static final int ACCESS_LOCATION_PERMISSION_REQUEST_CODE=100;
    private FusedLocationSource locationSource;
    private NaverMap naverMap;
    private List<Marker> markerList=new ArrayList<>();
    private InfoWindow infoWindow;
    private boolean isCameraAnimated=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_map);

        MapFragment mapFragment=(MapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {

        this.naverMap = naverMap;
        FusedLocationSource locationSource=new FusedLocationSource(this,100);
        naverMap.setLocationSource(locationSource);
        UiSettings uiSettings=naverMap.getUiSettings();
        uiSettings.setLocationButtonEnabled(true);

        LatLng mapCenter=naverMap.getCameraPosition().target;
        CameraPosition cameraPosition=new CameraPosition(mapCenter,12.5);
        naverMap.setCameraPosition(cameraPosition);
        getjson();

        infoWindow=new InfoWindow();

        infoWindow.setAdapter(new InfoWindow.DefaultViewAdapter(this) {
            @NonNull
            @Override
            protected View getContentView(@NonNull InfoWindow infoWindow) {
                Marker marker =infoWindow.getMarker();
                Crime crime=(Crime) marker.getTag();
                View view=View.inflate(CrimeMap_Rape.this, R.layout.view_info_window_murder,null);
                ((TextView) view.findViewById(R.id.name)).setText(crime.getName());
                ((TextView) view.findViewById(R.id.murder)).setText("강간: "+crime.getRape());

                return view;
            }
        });

    }



    private void getjson(){

        AssetManager assetManager=getAssets();

        try {
            InputStream is= assetManager.open("jsons/crime.json");
            InputStreamReader isr= new InputStreamReader(is);
            BufferedReader reader= new BufferedReader(isr);

            StringBuffer buffer= new StringBuffer();
            String line= reader.readLine();
            while (line!=null){
                buffer.append(line+"\n");
                line=reader.readLine();
            }

            String jsonData= buffer.toString();

            JSONArray jsonArray= new JSONArray(jsonData);

            resetMarkserList();
            System.out.println(jsonArray.length());

            markerset(jsonArray);


        } catch (IOException e) {e.printStackTrace();}
        catch (JSONException e) {e.printStackTrace(); }


    }

    @Override
    public void onCameraChange(int reason, boolean animated) {
        isCameraAnimated=animated;
    }

    @Override
    public void onCameraIdle() {


    }

    private void markerset(JSONArray jsonArray){

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jo = jsonArray.getJSONObject(i);
                Crime crimedata = new Crime();

                crimedata.setLat(jo.getDouble("latitude"));
                crimedata.setLng(jo.getDouble("longtitude"));
                crimedata.setName(jo.getString("id"));
                crimedata.setMurder(jo.getString("murder"));
                crimedata.setRobbery(jo.getString("robbery"));
                crimedata.setRape(jo.getString("rape"));
                crimedata.setLarceny(jo.getString("larceny"));
                crimedata.setViolence(jo.getString("violence"));


                Marker marker = new Marker();
                CircleOverlay circle=new CircleOverlay();
                marker.setTag(crimedata);
                marker.setPosition(new LatLng(crimedata.getLat(), crimedata.getLng()));
                marker.setIcon(OverlayImage.fromResource(R.drawable.marker));
                circle.setCenter(new LatLng(crimedata.getLat(),crimedata.getLng()));

                if(Integer.parseInt(crimedata.getRape())<50){
                    circle.setRadius(800);
                    circle.setColor(0x8016AA52);
                }
                if(Integer.parseInt(crimedata.getRape())>=50&&Integer.parseInt(crimedata.getRape())<200){
                    circle.setRadius(1200);
                    circle.setColor(0x80FEE134);
                }
                if(Integer.parseInt(crimedata.getRape())>=200&&Integer.parseInt(crimedata.getRape())<500){
                    circle.setRadius(1600);
                    circle.setColor(0x80ED9149);
                }
                if(Integer.parseInt(crimedata.getRape())>500){
                    circle.setRadius(2000);
                    circle.setColor(0x80F15B5B);
                }

                circle.setMap(naverMap);
                marker.setMap(naverMap);
                marker.setOnClickListener(this);
                markerList.add(marker);


            }

        }catch(JSONException e){e.printStackTrace();}

    }
    private void resetMarkserList(){
        if(markerList!=null&&markerList.size()>0){
            for(Marker marker : markerList){
                marker.setMap(null);
            }
            markerList.clear();
        }
    }


    @Override
    public boolean onClick(@NonNull Overlay overlay) {

        Marker marker=(Marker) overlay;
        infoWindow.open(marker);
        return false;
    }
}