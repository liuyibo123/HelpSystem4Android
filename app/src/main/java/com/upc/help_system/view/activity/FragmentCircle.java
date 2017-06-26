package com.upc.help_system.view.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyphenate.easeui.widget.EaseTitleBar;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
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

public class FragmentCircle extends Fragment {
	private List<JSONObject> jsonArray = new ArrayList<JSONObject>();
	protected boolean isConflict;
	protected boolean hidden;
	private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
	private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
	private final OkHttpClient client = new OkHttpClient();
	private int page = 1;
	private JSONArray jsonArray_Cache;
	private ThingsAdapter adapter;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_circle, container, false);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (savedInstanceState != null
				&& savedInstanceState.getBoolean("isConflict", false))
			return;
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
		EaseTitleBar titleBar = (EaseTitleBar) getView().findViewById(
				R.id.titleBar);
		titleBar.setRightImageResource(R.drawable.icon_camera);
		titleBar.setRightLayoutClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showPhotoDialog();
			}

		});
		JSONArray jsonArray_temp = ACache.get(getActivity()).getAsJSONArray(
				"things");
		if (jsonArray_temp != null) {
			jsonArray_Cache = jsonArray_temp;
			JSONArray2List(jsonArray_temp, jsonArray);
			try {
				page = Integer.parseInt(ACache.get(getActivity()).getAsString(
						"things"));
			} catch (RuntimeException ex) {
				page = 1;
			}

		}

		adapter = new ThingsAdapter(getActivity(), jsonArray);
		getData(1);

	}

	private void getData(final int page) {
		RequestBody formBody = new FormEncodingBuilder()
		.add("num", String.valueOf(page))
		.build();
		Request request = new Request.Builder().url(Constant.URL_GET_SOCIAL)
				.post(formBody)
				.build();
		client.newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(Request arg0, IOException arg1) {
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(getActivity(), "连不上服务器",
								Toast.LENGTH_SHORT).show();
					}
				});
			}
			@Override
			public void onResponse(Response arg0) throws IOException {
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
					}

				});
				if (arg0.isSuccessful()) {
					String result = arg0.body().string();
					System.out.println("result--->" + result);
					final JSONObject json = JSONObject.parseObject(result);
					if (json!=null&&json.getInteger("code") == 1000) {
						JSONArray jsonArray_temp = json
								.getJSONArray("things");
                        if(jsonArray_temp!=null&&jsonArray_temp.size()!=0){
                        	
                        	if (page == 1) {
    							jsonArray.clear();
    							JSONArray2List(jsonArray_temp, jsonArray);
    							jsonArray_Cache = jsonArray_temp;
    						} else {

    							JSONArray2List(jsonArray_temp, jsonArray);
    							jsonArray_Cache.addAll(jsonArray_temp);
    						}

    						getActivity().runOnUiThread(new Runnable() {

    							@Override
    							public void run() {
    								// TODO Auto-generated method stub

    								adapter.notifyDataSetChanged();
    							}

    						});

    						ACache.get(getActivity())
    								.put("things", jsonArray_Cache);
    						ACache.get(getActivity()).put("page", page + "");
                        }
						
					} else {
						 
						getActivity().runOnUiThread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								Toast.makeText(getActivity(), "访问失败",
										Toast.LENGTH_SHORT).show();
							}

						});
					}

				} else {

					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							Toast.makeText(getActivity(), "访问失败",
									Toast.LENGTH_SHORT).show();
						}

					});
					throw new IOException("Unexpected code " + arg0);
				}
			}
		});
	}

	private String imageName;
	private void showPhotoDialog() {
		final AlertDialog dlg = new AlertDialog.Builder(getActivity()).create();
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
					Cursor cursor = getActivity().getContentResolver().query(
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

			intent.setClass(getActivity(), SocialPublishActivity.class);
			startActivity(intent);
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	private void JSONArray2List(JSONArray jsonArray, List<JSONObject> lists) {
		;
		for (int i = 0; i < jsonArray.size(); i++) {

			lists.add(jsonArray.getJSONObject(i));
		}

	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		this.hidden = hidden;
		if (!hidden && !isConflict) {
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (!hidden) {
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (isConflict) {
			outState.putBoolean("isConflict", true);
		}
	}
}
