package com.upc.help_system.view.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.NotificationCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.util.NetUtils;
import com.upc.help_system.R;
import com.upc.help_system.presenter.MainPresenter;
import com.upc.help_system.presenter.MainPresenterImpl;
import com.upc.help_system.utils.SharedPreferenceUtil;
import com.upc.help_system.utils.widgetutil.SnackbarUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.smssdk.EventHandler;



public class MainActivity extends FragmentActivity {

    private final int SDK_PERMISSION_REQUEST = 127;
    @BindView(R.id.order)
    RadioButton order;
    @BindView(R.id.help)
    RadioButton help;
    @BindView(R.id.community)
    RadioButton community;
    @BindView(R.id.pulic)
    RadioButton pulic;
    private String permissionInfo;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.navigation_view)
    NavigationView navigationView;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.fragment)
    public FrameLayout frameLayout;
    private EaseUI easeUI;
    View headerView;
    MainPresenter presenter;
    EventHandler eventHandler;
    Button register;
    Button login;
    ImageButton img_btn;
    TextView nickname;
    String username = null;
    String sex;
    String nickname_string;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        easeUI = EaseUI.getInstance();
        headerView = navigationView.getHeaderView(0);
        register = (Button) headerView.findViewById(R.id.register);
        login = (Button) headerView.findViewById(R.id.login);
        img_btn = (ImageButton) headerView.findViewById(R.id.head_button);
        nickname = (TextView) headerView.findViewById(R.id.nickname);
        init();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (username == null || username.equals("")) {
                    Snackbar snackbar = SnackbarUtil.ShortSnackbar(drawerLayout, "发帖需要先登录", SnackbarUtil.Info).setActionTextColor(Color.RED).setAction("去登录", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    });
                    snackbar.show();
                } else {
                    startActivity(new Intent(MainActivity.this, PubActivity.class));
                }
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_item_signout:
                        String name = SharedPreferenceUtil.getString("user","name");
                        if (name!=null&&!name.equals("")){
                            SharedPreferences.Editor editor = getSharedPreferences("user",MODE_PRIVATE).edit();
                            editor.clear().commit();
                            clearString();
                            EMClient.getInstance().logout(true);
                            Toast.makeText(MainActivity.this, "注销成功", Toast.LENGTH_SHORT).show();
                            onResume();
                        }
                        else{
                            Toast.makeText(MainActivity.this, "未登录", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.navigation_item_change_password:

                        username = SharedPreferenceUtil.getString("user","name");
                        if (username == null || username.equals("")) {
                            Toast.makeText(MainActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        startActivity(new Intent(MainActivity.this, ChangPasswordActivity.class));
                        break;
                    case R.id.navigation_item_blog:
                        startActivity(new Intent(MainActivity.this,ConversitionActivity.class));
                        break;
                    case R.id.navigation_item_about:
                        Toast.makeText(MainActivity.this, "正在开发", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
        registerBroadcastReceiver();

    }
    private void registerBroadcastReceiver() {
        EMMessageListener messageListener = new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                // notify new message
                int i=1;
                for (EMMessage message : messages) {
                    Intent intent = new Intent(MainActivity.this,ChatActivity.class);
                    intent.putExtra(EaseConstant.EXTRA_USER_ID,message.getUserName());
                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
                  easeUI.getNotifier().onNewMesg(messages);
                    NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext());
                    mBuilder.setContentTitle(message.getUserName())//设置通知栏标题
                            .setContentText(message.getBody().toString().substring(4))//设置通知栏点击意图
                            .setContentIntent(pendingIntent)
//  .setNumber(number) //设置通知集合的数量
                            .setTicker("新消息") //通知首次出现在通知栏，带上升动画效果的
                            .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                            .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
                            .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                            .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                            .setSmallIcon(R.drawable.help);//设置通知小ICON
                    Notification notification = mBuilder.build();
                    mNotificationManager.notify(i,notification);
                    i++;
                }

            }
            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
            }
            @Override
            public void onMessageRead(List<EMMessage> messages) {
            }
            @Override
            public void onMessageDelivered(List<EMMessage> message) {
            }
            @Override
            public void onMessageChanged(EMMessage message, Object change) {}
        };
        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }

    private void clearString() {
        this.username = "";
        sex = "";
        nickname_string = "";
    }

    private void init() {
        presenter = new MainPresenterImpl(this);
        getPersimmions();
        EMClient.getInstance().addConnectionListener(new MyConnectionListener());
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        Stetho.initializeWithDefaults(this);
        presenter.showOrders();
        help.toggle();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username != null && !username.equals("")) {
                    Toast.makeText(MainActivity.this, "需要更换新的账号请先注销登陆", Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(i);
                }
            }
        });
        img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username!=null&&!username.equals("")){
                    startActivity(new Intent(MainActivity.this, UpdateUserActivity.class));
                }else {
                    Toast.makeText(MainActivity.this, "未登陆", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @TargetApi(23)
    private void getPersimmions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
            }
            if (addPermission(permissions, Manifest.permission.READ_PHONE_STATE)) {
                permissionInfo += "Manifest.permission.READ_PHONE_STATE Deny \n";
            }
            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
            }
        }
    }

    @TargetApi(23)
    private boolean addPermission(ArrayList<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) { // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
            if (shouldShowRequestPermissionRationale(permission)) {
                return true;
            } else {
                permissionsList.add(permission);
                return false;
            }

        } else {
            return true;
        }
    }
    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // TODO Auto-generated method stub
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @OnClick({R.id.order, R.id.help, R.id.community, R.id.pulic})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.order:
                if (username != null && !username.equals("")) {
                    presenter.showMyOrders();
                } else {
                    Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
                    order.toggle();
                    help.toggle();
                }
                break;
            case R.id.help:
                presenter.showOrders();
                break;
            case R.id.community:
                presenter.showSocial();
                break;
            case R.id.pulic:
                break;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        nickname_string = sharedPreferences.getString("nickname", "");
        sex = sharedPreferences.getString("sex", "");
        username = sharedPreferences.getString("name", "");
        if (nickname_string == null || nickname_string.equals("")) {
            nickname.setText("未登录");
        } else {
            nickname.setText(nickname_string);

                if (sex.equals("男")) {
                    Resources resources = getApplicationContext().getResources();
                    Drawable drawable = resources.getDrawable(R.drawable.ic_boy_128);
                    img_btn.setBackground(drawable);
                }else if (sex.equals("女")) {
                    Resources resources = getApplicationContext().getResources();
                    Drawable drawable = resources.getDrawable(R.drawable.ic_girl_48);
                    img_btn.setBackground(drawable);
                }
            }




    }

    private class MyConnectionListener implements EMConnectionListener {
        @Override
        public void onConnected() {
        }
        @Override
        public void onDisconnected(final int error) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(error == EMError.USER_REMOVED){
                        Toast.makeText(MainActivity.this, "账号已经被删除", Toast.LENGTH_SHORT).show();
                    }else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {

                    } else {
                        if (NetUtils.hasNetwork(MainActivity.this)){

                        }
                        else {
                            Toast.makeText(MainActivity.this, "当前网络不可用，请检查网络设置", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }
}



