package com.shengliedu.teacher.teacher.showclass;

import java.util.List;

import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.shengliedu.teacher.teacher.BaseActivity;
import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.util.HtmlImage;

public class SynSeletQuestionAnalyticalActivity extends BaseActivity {
	private TextView analytical_content;
	private String analytical;
	private String plan_info;
	private LinearLayout fsinglecLin;
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		analytical = (String) getIntent().getExtras().get("explanation");
		plan_info = (String) getIntent().getExtras().get("plan_info");
		setBack();
		if (!TextUtils.isEmpty(plan_info)) {
			showTitle(plan_info);
		}else {
			showTitle("查看解析");
		}
		analytical_content = (TextView) findViewById(R.id.analytical_content);
		fsinglecLin = (LinearLayout) findViewById(R.id.fsinglecLin);
		analytical_content.setText(Html.fromHtml(HtmlImage
				.deleteSrc(analytical)));
		List<String> imgList = HtmlImage.getImgSrc(analytical);
		ImageLoader utils = ImageLoader.getInstance();
		if (imgList.size() > 0) {
			for (int j = 0; j < imgList.size(); j++) {
				final String url = imgList.get(j);
				ImageView imageView = new ImageView(SynSeletQuestionAnalyticalActivity.this);
				imageView.setScaleType(ScaleType.FIT_CENTER);
				imageView.setLayoutParams(new LayoutParams(
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
				utils.displayImage(url, imageView);
				imageView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated
						// method stub
						HtmlImage htmlImage = new HtmlImage();
						htmlImage.showDialog(SynSeletQuestionAnalyticalActivity.this, url);
					}
				});
				fsinglecLin.addView(imageView);
			}
		}
	}

	@Override
	public void getDatas() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_syn_analytical;
	}

}
