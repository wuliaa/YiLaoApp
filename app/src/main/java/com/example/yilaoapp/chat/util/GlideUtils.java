package com.example.yilaoapp.chat.util;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.yilaoapp.R;


public class GlideUtils {

 	public static void loadChatImage(final Context mContext, String imgUrl, final ImageView imageView) {

		Glide.with(mContext)
				.load(imgUrl) // 图片地址
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.placeholder(R.drawable.wait)
				.error(R.drawable.error)
				.centerCrop()
				.into(imageView);
 	}

}
