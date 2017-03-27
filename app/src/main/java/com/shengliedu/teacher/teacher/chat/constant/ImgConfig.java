package com.shengliedu.teacher.teacher.chat.constant;

import android.app.Activity;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.chat.util.ImgHandler;
import com.shengliedu.teacher.teacher.chat.xmpp.XmppConnection;
import com.shengliedu.teacher.teacher.util.Config1;

import org.jivesoftware.smackx.packet.VCard;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ImgConfig extends ImageLoader {
	private static DisplayImageOptions options_circle;
	private static AnimateFirstDisplayListener animateFirstDisplayListener = new AnimateFirstDisplayListener();
	private static Map<String, Bitmap> bMap = new HashMap<String, Bitmap>();

	/**
	 * @param url
	 *            服务器的文件名
	 * @param imageView
	 *            圆行 R for Round
	 */
	public static void showImg(String url, ImageView imageView) {
		ImageLoader.getInstance().displayImage(url, imageView, options_circle,
				animateFirstDisplayListener);
	}

	public static void showHeadImg(final Activity context,
			final String username, final ImageView imageView,final TextView textView) {
		// ImageLoader.getInstance().displayImage("http://121.52.216.138:9090/plugins/xinxy/apps/userinfo/getuserheadimagetojpg?userName="+username,
		// imageView, options_circle, animateFirstDisplayListener);
		if (username == null || imageView == null) {
			return;
		}
		if (bMap == null) {
			bMap = new HashMap<String, Bitmap>();
		}

		// 由于懒，上传头像用了avatar，imageloader用不了..
		imageView.setImageDrawable(ImgHandler
				.ToCircularBig(R.mipmap.default_icon));
		if (!bMap.containsKey(username)) {
			VCard vCard = XmppConnection.getInstance().getUserInfo(username);
			if (vCard != null) {
				final String chaticon = vCard.getField("chaticon");
				final String nickname = vCard.getField("nickname");
				if (chaticon != null) {
					Config1.imageLoader.displayImage( Config1.IP
										+ chaticon.replace("\\/", "/").replace("&", "&amp"),imageView);
				}
				if (textView!=null) {
					if (!TextUtils.isEmpty(nickname)) {
						textView.setText(nickname);
					}
				}
			} else {
				imageView.setImageDrawable(ImgHandler
						.ToCircularBig(R.mipmap.default_icon));
			}
		}
	}


	/**
	 * 初始化图片读取方式
	 */
	public static void initImageLoader() {

		options_circle = new DisplayImageOptions.Builder()
				.showImageOnLoading(
						ImgHandler.ToCircularBig(R.mipmap.default_icon))
				.showImageForEmptyUri(
						ImgHandler.ToCircularBig(R.mipmap.default_icon))
				.showImageOnFail(
						ImgHandler.ToCircularBig(R.mipmap.default_icon))
				.cacheInMemory(true).cacheOnDisc(false)
				.considerExifParams(true)
				.displayer(new RoundedBitmapDisplayer(1000))
				// 暂未研究出圆形，故使用一个大值
				.resetViewBeforeLoading(false)
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED).build();

		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				MyApplication.getInstance())
				.threadPriority(Thread.NORM_PRIORITY)
				// .denyCacheImageMultipleSizesInMemory() // 不同大小图片只有一个缓存，默认多个
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				// 设置图片下载和显示的工作队列排序
				// .writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

	/**
	 * @author Administrator 监听读取完图片
	 */
	private static class AnimateFirstDisplayListener extends
			SimpleImageLoadingListener {
		// 放到内存
		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}

}
