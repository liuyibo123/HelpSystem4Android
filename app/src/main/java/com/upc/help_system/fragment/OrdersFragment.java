package com.upc.help_system.fragment;

import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.SupportMapFragment;
import com.baidu.mapapi.model.LatLng;
import com.upc.help_system.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class OrdersFragment extends Fragment {
    private MapView mMapView = null;
    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient;
    private LocationManager mlocationManager;
    private String provider;
    private BDLocation bdLocation;
    private MybdLocationListener mybdLocationListener;
    private boolean isFirstLoc = true;
    private double mLatitude;
    private double mLongitude;
    private Context context;


    public BDLocationListener mbdLocationListener = new MybdLocationListener();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getActivity().getApplicationContext());
        View view = inflater.inflate(R.layout.fragment_orders, container, false);
        ViewHolder holder = new ViewHolder(view);
        mMapView = holder.bmapview;
        mBaiduMap = mMapView.getMap();
        //开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        //设定初始化地图时的比例，当前位500m
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.zoomTo(15.0f);
        mBaiduMap.setMapStatus(mapStatusUpdate);
        //使用LocationManager判断可以使用的定位方式

        mlocationManager=(LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        List<String> providerList= mlocationManager.getProviders(true);

        if(providerList.contains(LocationManager.GPS_PROVIDER)){
            provider=LocationManager.GPS_PROVIDER;

        }else if(providerList.contains(LocationManager.NETWORK_PROVIDER)){
            provider=LocationManager.NETWORK_PROVIDER;
        } else{
            Toast.makeText(getContext(), "No location Provider to use", Toast.LENGTH_SHORT).show();
        }
        //定位初始化
        initLocation();
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.recycler)
        RecyclerView recycler;
        @BindView(R.id.bmapview)
        MapView bmapview;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        //停止定位
        mBaiduMap.setMyLocationEnabled(false);
        mLocationClient.stop();
    }
    @Override
    public void onResume() {
        super.onResume();
        //在Fragment执行onResume时执行mMapView.onResume()实现地图生命周期的管理
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        // 退出时销毁定位
        if (mLocationClient != null) {
            mLocationClient.stop();
        }
        super.onDestroy();
    }

    private void initLocation() {
        //创建LocationClient实例
        mLocationClient = new LocationClient(getContext());
        mbdLocationListener = new MybdLocationListener();
        //注册监听函数,定位成功后回调
        mLocationClient.registerLocationListener(mbdLocationListener);
        //初始化
        setLocationOption();
        mLocationClient.start();
    }
    public void setLocationOption() {
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);//设置使用gps
        option.setCoorType("bd09ll");//默认gcj02，设置返回的定位结果坐标系
        option.setIsNeedAddress(true);//设置需要地址信息
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//设置高精度
        //默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        int span = 5000;
        option.setScanSpan(span);

        option.setLocationNotify(true);//设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//设置需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);//使用设置
    }
    private class MybdLocationListener implements BDLocationListener {
        @Override
        //接收异步返回的定位结果，参数是BDLocation 类型
        public void onReceiveLocation(BDLocation bdLocation) {
            //创建LocationData类
            MyLocationData myLocationData = new MyLocationData.Builder()
                    .accuracy(bdLocation.getRadius())
                    .latitude(bdLocation.getLatitude())
                    .longitude(bdLocation.getLongitude())
                    .build();
            //在BaiduMap中设置
            mBaiduMap.setMyLocationData(myLocationData);
            mLatitude = bdLocation.getLatitude();
            mLongitude = bdLocation.getLongitude();

            if (isFirstLoc) {
                LatLng latLng = new LatLng(mLatitude, mLongitude);
                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
                mBaiduMap.animateMapStatus(msu);
                isFirstLoc = false;
        //        Toast.makeText(context, bdLocation.getAddrStr(), Toast.LENGTH_SHORT).show();
             /*   Toast.makeText(
                        context,
                        "地址是：" + bdLocation.getAddrStr() + "\n"
                                + "定位精度是：" + bdLocation.getRadius() + "\n"
                        , Toast.LENGTH_LONG).show();
                Log.e("Address", String.valueOf(bdLocation.getCity()));
                Log.e("District", String.valueOf(bdLocation.getAddrStr()));
                Log.e("Location", String.valueOf(bdLocation));
            */
            }
        }
    }
}

/*
        List<Poi> list = bdLocation.getPoiList();    // POI数据
        if (list != null) {
            sb.append("\npoilist size = : ");
            sb.append(list.size());
            for (Poi p : list) {
                sb.append("\npoi= : ");
                sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
            }
        }

        Log.i("BaiduLocationApi", sb.toString());
    }



    //接收异步返回的POI查询结果，参数也是BDLocation类型
    public void  onReceivePoi(BDLocation poiLocation){
        if(poiLocation==null){
            return;
        }
        StringBuffer sb = new StringBuffer(256);
        sb.append("Poi time : ");   //POi查询时间
        sb.append(poiLocation.getTime());

        sb.append("\nerror code : ");//错误码
        sb.append(poiLocation.getLocType());

        sb.append("\nlatitude : ");
        sb.append(poiLocation.getLatitude());

        sb.append("\nlontitude : ");
        sb.append(poiLocation.getLongitude());

        sb.append("\nradius : ");
        sb.append(poiLocation.getRadius());

        if (poiLocation.getLocType() == BDLocation.TypeNetWorkLocation){
            sb.append("\naddr : ");
            sb.append(poiLocation.getAddrStr());
        }

        Log.i("BaiduLocationApiPoi", sb.toString());

    }
    */