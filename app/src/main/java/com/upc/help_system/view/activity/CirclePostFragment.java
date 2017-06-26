package com.upc.help_system.view.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyphenate.easeui.widget.EaseTitleBar;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.RequestBody;
import com.upc.help_system.Constant;
import com.upc.help_system.R;
import com.upc.help_system.presenter.adapter.ThingsAdapter;
import com.upc.help_system.utils.ACache;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;

public class CirclePostFragment extends BaseActivity{
	
	private List<JSONObject> jsonArray = new ArrayList<JSONObject>();
	protected boolean isConflict;
	protected boolean hidden;
	private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
	private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
	private final OkHttpClient client = new OkHttpClient();
	private int page = 1;
	private JSONArray jsonArray_Cache;
	private ThingsAdapter adapter;
	private ListView actualListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_circle);
		initView();
		initFile();
	}
	@SuppressLint("SdCardPath")
	public void initFile() {
		File dir = new File("/sdcard/bizchat");

		if (!dir.exists()) {
			dir.mkdirs();
		}
	}
	private void initView() {
		EaseTitleBar titleBar = (EaseTitleBar) findViewById(R.id.titleBar);
		titleBar.setRightImageResource(R.drawable.icon_camera);
		titleBar.setRightLayoutClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showPhotoDialog();
			}

		});
		JSONArray jsonArray_temp = ACache.get(CirclePostFragment.this).getAsJSONArray("things");
		if (jsonArray_temp != null) {
			jsonArray_Cache = jsonArray_temp;
			JSONArray2List(jsonArray_temp, jsonArray);
			try {
				page = Integer.parseInt(ACache.get(CirclePostFragment.this).getAsString(
						"things"));
			} catch (RuntimeException ex) {
				page = 1;
			}
		}
		adapter = new ThingsAdapter(CirclePostFragment.this, jsonArray);
		actualListView.setAdapter(adapter);
		actualListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
				 JSONObject json = adapter.getItem(position - 1);
//				 startActivity(new Intent(CirclePostFragment.this, ThingDetailActivity.class).putExtra("thingsInfo",json.toJSONString()));
			}

		});
		getData(1);
	}

	private void getData(final int page) {
		okhttp3.RequestBody formBody =new FormBody.Builder()
                       .add("num", String.valueOf(page))
                       .build();
		Request request = new Request.Builder().url(Constant.URL_GET_SOCIAL)
				.post(formBody)
				.build();

		client.newCall(request).enqueue(new Callback() {

			@Override
			public void onFailure(okhttp3.Call call, IOException e) {

			}

			@Override
			public void onResponse(okhttp3.Call call, Response response) throws IOException {
			}
		});
	}

	private String imageName;

	private void showPhotoDialog() {
		final AlertDialog dlg = new AlertDialog.Builder(CirclePostFragment.this).create();
		dlg.show();
		Window window = dlg.getWindow();
		window.setContentView(R.layout.dialog_alert);
		TextView tv_paizhao = (TextView) window.findViewById(R.id.tv_content1);
		tv_paizhao.setText("拍照");
		tv_paizhao.setOnClickListener(new View.OnClickListener() {
			@SuppressLint("SdCardPath")
			public void onClick(View v) {

				imageName = getNowTime() + ".jpg";
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				// 指定调用相机拍照后照片的储存路径
				intent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(new File("/sdcard/bizchat/", imageName)));
				startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
				dlg.cancel();
			}
		});
		TextView tv_xiangce = (TextView) window.findViewById(R.id.tv_content2);
		tv_xiangce.setText("相册");
		tv_xiangce.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				getNowTime();
				imageName = getNowTime() + ".jpg";
				Intent intent = new Intent(Intent.ACTION_PICK, null);
				intent.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
				dlg.cancel();
			}
		});
	}

	@SuppressLint("SimpleDateFormat")
	private String getNowTime() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmssSS");
		return dateFormat.format(date);
	}
	@SuppressLint("SdCardPath")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			String path = null;
			switch (requestCode) {
			case PHOTO_REQUEST_TAKEPHOTO:
				path = "/sdcard/bizchat/" + imageName;
				break;
			case PHOTO_REQUEST_GALLERY:
				if (data != null) {
					Uri imageFilePath = data.getData();
					String[] proj = { MediaStore.Images.Media.DATA };
					Cursor cursor = CirclePostFragment.this.getContentResolver().query(
							imageFilePath, proj, null, null, null);
					int column_index = cursor
							.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					cursor.moveToFirst();
					// 获取图片真实地址
					path = cursor.getString(column_index);
					System.out.println(path);
				}
				break;
			}
			Intent intent = new Intent();
			intent.putExtra("imagePath", path);
			intent.setClass(CirclePostFragment.this, SocialPublishActivity.class);
			startActivity(intent);
			super.onActivityResult(requestCode, resultCode, data);
		}
	}
	private void JSONArray2List(JSONArray jsonArray, List<JSONObject> lists) {
		for (int i = 0; i < jsonArray.size(); i++) {
			lists.add(jsonArray.getJSONObject(i));
		}
	}

}
