package com.upc.help_system.view.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseBaiduMapActivity;
import com.upc.help_system.utils.MyGson;
import com.upc.help_system.utils.MyLoction;
import com.upc.help_system.utils.PermissionUtil;
import com.upc.help_system.utils.network.ConConfig;
import com.upc.help_system.utils.network.RequestService;
import com.upc.help_system.utils.widgetutil.SnackbarUtil;
import com.upc.help_system.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.upc.help_system.utils.network.MyRetrofit.requestService;


/**
 * Created by Administrator on 2017/5/31.
 */

public class DetailActivity extends Activity {

    float tip;
    String pub_person;
    String phone_number;
    String content;
    String pub_loc;
    MyLoction loc;
    BaiduMap baiduMap;
    int id;
    String username;
    String accept_person;
    @BindView(R.id.imageView)
    MapView imageView;
    @BindView(R.id.back_btn)
    ImageButton backBtn;
    @BindView(R.id.pub_tv)
    TextView pubTv;
    @BindView(R.id.phone_tv)
    TextView phoneTv;
    @BindView(R.id.call)
    ImageButton call;
    @BindView(R.id.tip_tv)
    TextView tipTv;
    @BindView(R.id.content_tv)
    TextView contentTv;
    @BindView(R.id.contact)
    Button contact;
    @BindView(R.id.accept)
    Button accept;
    ConstraintLayout constraintlayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_details);
        ButterKnife.bind(this);
        constraintlayout = (ConstraintLayout) findViewById(R.id.constraint_layout);
        Intent intent = getIntent();
        float tip = intent.getFloatExtra("tip", 0);
        pub_person = intent.getStringExtra("pubperson");
        phone_number = intent.getStringExtra("phonenumber");
        content = intent.getStringExtra("content");
        id = intent.getIntExtra("id", 0);
        accept_person = intent.getStringExtra("acceptperson");
        phoneTv.setText(phone_number);
        pub_loc = intent.getStringExtra("location");
        loc = MyGson.fromJson(pub_loc,MyLoction.class);
        tipTv.setText(String.valueOf(tip));
        pubTv.setText(pub_person);
        contentTv.setText(content);
        SharedPreferences sharedpreferenrce = getSharedPreferences("user", MODE_PRIVATE);
        username = sharedpreferenrce.getString("name", "");
        initBaiduMap();
    }
    private void initBaiduMap() {
        baiduMap = imageView.getMap();
        double latitude = loc.getLatitude();
        double longtitude = loc.getLongitude();
        Log.d("DetailActivity", "latitude:" + latitude);
        Log.d("DetailActivity", "longtitude:" + longtitude);
        LatLng p = new LatLng(latitude,longtitude);
        imageView = new MapView(this,new BaiduMapOptions().mapStatus(new MapStatus.Builder().target(p).build()));
        baiduMap.setMyLocationEnabled(true);
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(100)
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(90.0f)
                .latitude(latitude)
                .longitude(longtitude).build();
        float f = baiduMap.getMaxZoomLevel();//19.0 最小比例尺
        //float m = mBaiduMap.getMinZoomLevel();//3.0 最大比例尺
        baiduMap.setMyLocationData(locData);
        LatLng ll = new LatLng(latitude,longtitude);
        showMap(latitude,longtitude,loc.getAddr());
    }
    private void showMap(double latitude, double longtitude, String address) {
        LatLng llA = new LatLng(latitude, longtitude);
        CoordinateConverter converter= new CoordinateConverter();
        converter.coord(llA);
        converter.from(CoordinateConverter.CoordType.COMMON);
        LatLng convertLatLng = converter.convert();
        OverlayOptions ooA = new MarkerOptions().position(convertLatLng).icon(BitmapDescriptorFactory
                .fromResource(com.hyphenate.easeui.R.drawable.ease_icon_marka))
                .zIndex(4).draggable(true);
        baiduMap.addOverlay(ooA);
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(convertLatLng, 17.0f);
        baiduMap.animateMapStatus(u);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        imageView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
     imageView.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        imageView.onPause();
    }

    @OnClick({R.id.back_btn, R.id.call, R.id.contact, R.id.accept})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                this.finish();
                break;
            case R.id.call:
                    if(PermissionUtil.checkPermission(DetailActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE,"需要请求打电话权限")==1)
                    {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone_number));
                        startActivity(intent);
                     }
                break;
            case R.id.contact:
                if(pub_person.equals(username)){
                    Snackbar.make(constraintlayout, "不能和自己聊天", Snackbar.LENGTH_LONG).show();
                    break;
                }
                Intent intent = new Intent(DetailActivity.this,ChatActivity.class);
                intent.putExtra(EaseConstant.EXTRA_USER_ID,pub_person);
                startActivity(intent);
                break;
            case R.id.accept:
                if (username == null || username.equals("")) {
                    Snackbar.make(constraintlayout, "接收订单前需要登录", Snackbar.LENGTH_LONG).show();
                }else{
                    if (pub_person.equals(username)) {
                        Snackbar.make(constraintlayout, "不能接收自己的订单", Snackbar.LENGTH_LONG).show();
                    } else if (accept_person!= null&&!accept_person.equals("")) {
                        Snackbar.make(constraintlayout, "该订单已经被接收", Snackbar.LENGTH_LONG).show();
                    } else {
                        acceptOrder();
                    }
                }

                break;
        }
    }

    private void acceptOrder() {
        Call<String> call = requestService.acceptOrder(id, username);
        Log.d("DetailActivity", "id:" + id);
        Log.d("DetailActivity", username);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                switch (response.body()) {
                    case "1":
                        Snackbar snackbar = SnackbarUtil.ShortSnackbar(constraintlayout, "接单成功", SnackbarUtil.Confirm).setActionTextColor(Color.RED).setAction("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DetailActivity.this.finish();
                            }
                        });
                        snackbar.show();
                        break;
                    default:
                        Snackbar snackbar1 = SnackbarUtil.ShortSnackbar(constraintlayout, "接单失败", SnackbarUtil.Alert).setActionTextColor(Color.RED).setAction("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DetailActivity.this.finish();
                            }
                        });
                        snackbar1.show();
                        break;

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Snackbar snackbar = SnackbarUtil.ShortSnackbar(constraintlayout, "网络原因失败", SnackbarUtil.Alert).setActionTextColor(Color.RED).setAction("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DetailActivity.this.finish();
                    }
                });
                snackbar.show();
            }
        });
    }
}
