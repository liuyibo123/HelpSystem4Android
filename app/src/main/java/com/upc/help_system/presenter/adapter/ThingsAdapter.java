package com.upc.help_system.presenter.adapter;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.upc.help_system.Constant;
import com.upc.help_system.R;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;

public class ThingsAdapter extends BaseAdapter {
	private Context context;
	List<JSONObject> jsons;
	private LayoutInflater inflater;

	public ThingsAdapter(Context _context, List<JSONObject> jsons) {
		this.jsons = jsons;
		this.context = _context;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return jsons.size();
	}

	@Override
	public JSONObject getItem(int position) {
		// TODO Auto-generated method stub
		return jsons.get(position);
	}
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_fragment_post, parent, false);
		}
		ViewHolder holder = (ViewHolder) convertView.getTag();
		if (holder == null) {
			holder = new ViewHolder();
			holder.ll_pl = (RelativeLayout) convertView
					.findViewById(R.id.ll_pl);
			convertView.setTag(holder);
		}
		final JSONObject userJson = getItem(position);
		String imageStr = userJson.getString("imageStr");
		String content = userJson.getString("content");
		String location = userJson.getString("location");
		// String location = userJson.getString("location");
		String nick = userJson.getJSONObject("userInfo").getString("nick");
		String avatar = userJson.getJSONObject("userInfo").getString("avatar");
		Glide.with(context).load(avatar)
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.placeholder(R.drawable.ic_boy_48)
				.into(holder.iv_avatar);
		holder.tv_content.setText(content);
		holder.tv_name.setText(nick);
		if (!imageStr.equals("0") && imageStr != null) {
			String[] images = imageStr.split("split");
			int imNumb = images.length;
			holder.iv_image1.setVisibility(View.VISIBLE);
			Glide.with(context).load(Constant.URL_SOCIAL_PHOTO + images[0])
					.diskCacheStrategy(DiskCacheStrategy.ALL)
					.placeholder(R.drawable.add).centerCrop()
					.into(holder.iv_image1);
			holder.iv_image1.setOnClickListener(new ImageListener(images, 0));

			Log.e("imNumb--->>", String.valueOf(imNumb));
			// 四张图的时间情况比较特殊
			if (imNumb == 2) {
				holder.iv_image2.setVisibility(View.VISIBLE);
				// holder.iv_image2.setImageURI(Uri
				// .parse(Constant.URL_SOCIAL_PHOTO + images[1]));
				Glide.with(context).load(Constant.URL_SOCIAL_PHOTO + images[1])
						.diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop()
						.placeholder(R.drawable.add)
						.into(holder.iv_image2);

				holder.iv_image2
						.setOnClickListener(new ImageListener(images, 1));
				holder.iv_image3.setVisibility(View.GONE);

			} else if (imNumb > 2) {
				holder.iv_image2.setVisibility(View.VISIBLE);
				Glide.with(context).load(Constant.URL_SOCIAL_PHOTO + images[1])
						.diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop()
						.placeholder(R.drawable.ease_blue_add)
						.into(holder.iv_image2);
				holder.iv_image2
						.setOnClickListener(new ImageListener(images, 1));

				holder.iv_image3.setVisibility(View.VISIBLE);
				Glide.with(context).load(Constant.URL_SOCIAL_PHOTO + images[2])
						.centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL)
						.placeholder(R.drawable.ease_blue_add)
						.into(holder.iv_image3);
				holder.iv_image3
						.setOnClickListener(new ImageListener(images, 2));
			} else {
				holder.iv_image2.setVisibility(View.GONE);

				holder.iv_image3.setVisibility(View.GONE);

			}

		}
		if (!TextUtils.isEmpty(location)) {

			holder.tv_address.setText(location);
		}
		// TODO Auto-generated method stub
		return convertView;
	}

	public static class ViewHolder {
		@BindView(R.id.iv_avatar)
		ImageView iv_avatar;
		@BindView(R.id.iv_image1)
		ImageView iv_image1;
		@BindView(R.id.iv_image2)
		ImageView iv_image2;
		@BindView(R.id.iv_image3)
		ImageView iv_image3;
		@BindView(R.id.tv_name)
		TextView tv_name;
		@BindView(R.id.tv_distance)
		TextView tv_distance;
		@BindView(R.id.tv_content)
		TextView tv_content;
		@BindView(R.id.tv_address)
		TextView tv_address;
		RelativeLayout ll_pl;
	}

	class ImageListener implements OnClickListener {
		String[] images;
		int page;

		public ImageListener(String[] images, int page) {
			this.images = images;
			this.page = page;
		}

		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
	//		intent.setClass(context, BigImageActivity.class);
			intent.putExtra("images", images);
			intent.putExtra("page", page);
			context.startActivity(intent);
		}
	}

	/**
	 * 将给定图片维持宽高比缩放后，截取正中间的正方形部分。
	 * 
	 * @param bitmap
	 *            原图
	 * @param edgeLength
	 *            希望得到的正方形部分的边长
	 * @return 缩放截取正中部分后的位图。
	 */
	public static Bitmap centerSquareScaleBitmap(Bitmap bitmap, int edgeLength) {
		if (null == bitmap || edgeLength <= 0) {
			return null;
		}

		Bitmap result = bitmap;
		int widthOrg = bitmap.getWidth();
		int heightOrg = bitmap.getHeight();

		if (widthOrg > edgeLength && heightOrg > edgeLength) {
			// 压缩到一个最小长度是edgeLength的bitmap
			int longerEdge = (int) (edgeLength * Math.max(widthOrg, heightOrg) / Math
					.min(widthOrg, heightOrg));
			int scaledWidth = widthOrg > heightOrg ? longerEdge : edgeLength;
			int scaledHeight = widthOrg > heightOrg ? edgeLength : longerEdge;
			Bitmap scaledBitmap;

			try {
				scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth,
						scaledHeight, true);
			} catch (Exception e) {
				return null;
			}

			// 从图中截取正中间的正方形部分。
			int xTopLeft = (scaledWidth - edgeLength) / 2;
			int yTopLeft = (scaledHeight - edgeLength) / 2;

			try {
				result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft,
						edgeLength, edgeLength);
				scaledBitmap.recycle();
			} catch (Exception e) {
				return null;
			}
		}
		return result;
	}

}
