package com.shengliedu.teacher.teacher.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shengliedu.teacher.teacher.R;

/**
 * 通用头部
 * 
 * @author zhangtao
 * 
 */
public class CustomTitle extends RelativeLayout {

	public TextView textView_left;
	public TextView textView_right;
	public TextView textView_right_l;
	public ImageView imageView_left;
	public ImageView imageView_right;
	private TextView textView_title;
	public LinearLayout moon_layout;
	private TextView moon_year;

	public CustomTitle(Context context) {
		super(context);
		initUi(context);
	}

	public CustomTitle(Context context, AttributeSet attrs) {
		super(context, attrs);
		initUi(context);
	}

	private void initUi(Context context) {
		LayoutInflater.from(context).inflate(R.layout.custom_title, this, true);
		textView_left = (TextView) findViewById(R.id.user_name1);
		textView_right = (TextView) findViewById(R.id.user_age);
		textView_right_l = (TextView) findViewById(R.id.right_publish);
		imageView_left = (ImageView) findViewById(R.id.user_logo_image);
		imageView_right = (ImageView) findViewById(R.id.textView_right_image);
		textView_title = (TextView) findViewById(R.id.user_ju_logo_time);
		moon_layout=(LinearLayout)findViewById(R.id.moon_layout);
		moon_year=(TextView) findViewById(R.id.moon_year);
		// Typeface typeFace =
		// Typeface.createFromAsset(context.getAssets(),"fonts/wawati.ttf");
		// textView_left.setTypeface(typeFace);
		// textView_right.setTypeface(typeFace);
		// textView_title.setTypeface(typeFace);
	}
	public void setMoonTile(String text){
		moon_layout.setVisibility(View.VISIBLE);
		moon_year.setText(text);
	}
	public void setRightText(String text, OnClickListener listener) {
		textView_right.setVisibility(View.VISIBLE);
		textView_right.setText(text);
		textView_right.setOnClickListener(listener);
	}

	public void setRightText2(String text, OnClickListener listener) {
		textView_right_l.setVisibility(View.VISIBLE);
		textView_right_l.setText(text);
		textView_right_l.setOnClickListener(listener);
	}

	public void setLeftText(String text, OnClickListener listener) {
		textView_left.setVisibility(View.VISIBLE);
		textView_left.setText(text);
		textView_left.setOnClickListener(listener);
	}

	public void setLeftImage(int res, OnClickListener listener) {
		imageView_left.setVisibility(View.VISIBLE);
		imageView_left.setImageResource(res);
		imageView_left.setOnClickListener(listener);
	}

	public void setRightImage(int res, OnClickListener listener) {
		imageView_right.setVisibility(View.VISIBLE);
		imageView_right.setImageResource(res);
		imageView_right.setOnClickListener(listener);
	}

	public void setTitle(String text) {
		textView_title.setVisibility(View.VISIBLE);
		textView_title.setText(text);
	}
}
