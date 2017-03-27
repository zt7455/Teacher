package com.shengliedu.teacher.teacher.chat.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.chat.activitys.FriendActivity;
import com.shengliedu.teacher.teacher.chat.activitys.MapActivity;
import com.shengliedu.teacher.teacher.chat.activitys.ShowPicActivitiy;
import com.shengliedu.teacher.teacher.chat.constant.Constants;
import com.shengliedu.teacher.teacher.chat.constant.ImgConfig;
import com.shengliedu.teacher.teacher.chat.constant.MyApplication;
import com.shengliedu.teacher.teacher.chat.model.ChatItem;
import com.shengliedu.teacher.teacher.chat.model.User;
import com.shengliedu.teacher.teacher.chat.util.DateUtil;
import com.shengliedu.teacher.teacher.chat.util.FileUtil;
import com.shengliedu.teacher.teacher.chat.util.ImageUtil;
import com.shengliedu.teacher.teacher.chat.util.StringUtil;
import com.shengliedu.teacher.teacher.chat.util.Tool;
import com.shengliedu.teacher.teacher.chat.view.expression.ExpressionUtil;
import com.shengliedu.teacher.teacher.chat.view.gifView.GifView;
import com.shengliedu.teacher.teacher.chat.view.gifView.GifView.GifImageType;
import com.shengliedu.teacher.teacher.chat.xmpp.XmppConnection;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

public class ChatAdapter extends ArrayAdapter<ChatItem> {
	private Activity cxt;
	private static int[] resTo = { R.mipmap.voiceto0, R.mipmap.voiceto1,
			R.mipmap.voiceto2, R.mipmap.voiceto3 };
	private static int[] resFrom = { R.mipmap.voicefrom0,
			R.mipmap.voicefrom1, R.mipmap.voicefrom2, R.mipmap.voicefrom3 };
	private String username = null;
	private String chaticon;

	public static interface MsgType {
		int MSG_OUT = 0;
		int MSG_IN = 1;
	}

	public ChatAdapter(Activity context, String username) {
		super(context, 0);
		this.cxt = context;
		this.username = username;
	}

	@Override
	public int getItemViewType(int position) {
		ChatItem nowMsg = (ChatItem) getItem(position);
		if (nowMsg.inOrOut == 1) {
			return MsgType.MSG_OUT;
		} else {
			return MsgType.MSG_IN;
		}
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		final ChatItem item = (ChatItem) getItem(position);
		final ViewHolder viewHolder;
		int msgType = getItemViewType(position);
		if (convertView == null) {
			if (msgType == MsgType.MSG_OUT) {
				convertView = LayoutInflater.from(cxt).inflate(
						R.layout.row_chat_mine, null);
			} else {
				convertView = LayoutInflater.from(cxt).inflate(
						R.layout.row_chat, null);
			}
			viewHolder = new ViewHolder();
			viewHolder.timeView = (TextView) convertView
					.findViewById(R.id.timeView);
			viewHolder.msgView = (TextView) convertView
					.findViewById(R.id.msgView);
			viewHolder.head = (ImageView) convertView
					.findViewById(R.id.headImg);
			viewHolder.img = (ImageView) convertView.findViewById(R.id.imgView);
			viewHolder.gifImg = (ImageView) convertView
					.findViewById(R.id.gifImgView);
			viewHolder.voice = (ImageView) convertView
					.findViewById(R.id.voiceView);
			viewHolder.soundDuration = (TextView) convertView
					.findViewById(R.id.soundView);
			viewHolder.gif = (GifView) convertView.findViewById(R.id.gifView);
			viewHolder.nameView = (TextView) convertView
					.findViewById(R.id.nameView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
			viewHolder.timeView.setVisibility(View.VISIBLE);
			viewHolder.msgView.setVisibility(View.VISIBLE);
			viewHolder.head.setVisibility(View.VISIBLE);
			viewHolder.img.setVisibility(View.GONE);
			viewHolder.gifImg.setVisibility(View.GONE);
			viewHolder.voice.setVisibility(View.GONE);
			viewHolder.soundDuration.setVisibility(View.GONE);
			viewHolder.gif.setVisibility(View.GONE);
			viewHolder.nameView.setVisibility(View.GONE);
		}

		// head
		if (item.inOrOut == 0) { // 接收
			if (item.chatType == ChatItem.CHAT) {
				User user = new User(XmppConnection.getInstance().getUserInfo(
						username));
				chaticon = user.chaticon;
				if (chaticon != null) {
					user.showHead(cxt, viewHolder.head);
				}
			} else {
				ImgConfig.showHeadImg(cxt, item.username, viewHolder.head,null);
			}

			viewHolder.head.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(cxt, FriendActivity.class);
					intent.putExtra("username", item.username);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					cxt.startActivity(intent);
				}
			});

		} else {
			ImgConfig.showHeadImg(cxt, Constants.USER_NAME, viewHolder.head,null);
		}

		// 同一分钟内则不显示相同时间了
		ChatItem lastMsg = null;
		if (position != 0)
			lastMsg = (ChatItem) getItem(position - 1);
		if (lastMsg != null
				&& lastMsg.sendDate.substring(0, lastMsg.sendDate.length() - 3)
						.equals(item.sendDate.substring(0,
								lastMsg.sendDate.length() - 3))) {
			viewHolder.timeView.setVisibility(View.GONE);
		} else {
			Log.v("TAG", "item=" + item.sendDate);
			viewHolder.timeView.setText(DateUtil
					.getRecentTimeCN_yyyy_MM_dd_HH_mm(item.sendDate));
		}

		// text
		// 适配图片、声音等文件
		if (item.msg != null && item.msg.contains(Constants.PATH)) {
			String path = item.msg;
			File file = new File(path);

			if (file.exists() && file.length() != 0) { // isExist
				viewHolder.msgView.setVisibility(View.GONE);
				int type = FileUtil.getType(path);

				if (type == FileUtil.IMG) { // isImg
					showImg(viewHolder.img, path);
				} else if (type == FileUtil.SOUND) { // isSound
					playSound(path, viewHolder.soundDuration, viewHolder.voice,
							item.inOrOut == 1);
				}
			} else {
				viewHolder.msgView.setText("加载中...");
			}
		} else if (item.msg != null && item.msg.contains("[/g0")) { // isGif
			playGif(viewHolder.gif, viewHolder.msgView, viewHolder.gifImg,
					item.msg, position);
		} else if (item.msg != null && item.msg.contains("[/f0")) { // 适配表情
			viewHolder.msgView.setText(ExpressionUtil.getText(cxt,
					StringUtil.Unicode2GBK(item.msg)));
		} else if (item.msg != null && item.msg.contains("[/a0")) { // 适配地图
			viewHolder.msgView.setVisibility(View.GONE);
			showMap(viewHolder.img, item.msg);
		} else {
			viewHolder.msgView.setText(item.msg);
		}

		if (item.chatType == ChatItem.GROUP_CHAT && item.inOrOut == 0) {
			viewHolder.nameView.setVisibility(View.VISIBLE);
			viewHolder.nameView.setText(item.username);
		} else {
			viewHolder.nameView.setVisibility(View.GONE);
		}

		// 内容复制
		viewHolder.msgView.setOnLongClickListener(new OnLongClickListener() {
			@SuppressLint("NewApi")
			@Override
			public boolean onLongClick(View v) {
				TextView msgView = (TextView) v;
				ClipboardManager cm = (ClipboardManager) cxt
						.getSystemService(Context.CLIPBOARD_SERVICE);
				// 将文本数据复制到剪贴板
				cm.setText(msgView.getText());
				Vibrator vib = (Vibrator) cxt
						.getSystemService(Context.VIBRATOR_SERVICE); // 震动提醒
				vib.vibrate(100);
				Tool.initToast(cxt, "复制成功");
				return false;
			}
		});
		return convertView;
	}

	private void showImg(ImageView img, final String path) {
		img.setVisibility(View.VISIBLE);
		img.setImageBitmap(ImageUtil.createImageThumbnail(path, 200 * 200));
		img.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("picPath", path);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setClass(cxt, ShowPicActivitiy.class);
				cxt.startActivity(intent);
			}
		});

		// //内容复制
		img.setOnLongClickListener(new OnLongClickListener() {
			@SuppressLint("NewApi")
			@Override
			public boolean onLongClick(View v) {
				// FileUtil.changeFile(path,
				// Constants.IMG_PATH+"/"+FileUtil.getFileName(path));
				Tool.initToast(cxt, "图片已保存至本地" + path);
				MyApplication.getInstance().sendBroadcast(
						new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri
								.parse("file://" + path)));
				return false;
			}
		});
	}

	private void showMap(ImageView img, String msg) {
		img.setVisibility(View.VISIBLE);
		img.setImageResource(R.mipmap.map);
		String[] adrs = msg.split(",");
		final double lat = Double.valueOf(adrs[1]);
		final double lon = Double.valueOf(adrs[2]);

		img.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("lat", lat);
				intent.putExtra("lon", lon);
				intent.setClass(cxt, MapActivity.class);
				cxt.startActivity(intent);
			}
		});
	}

	/**
	 * play gif
	 * 
	 * @param gif
	 * @param msg
	 */
	private void playGif(GifView gif, TextView msgView, ImageView img,
			String msg, int position) {
		msgView.setVisibility(View.GONE);
		try {
			Field field = R.drawable.class.getDeclaredField(msg.substring(2,
					msg.indexOf("]")));
			int resId = Integer.parseInt(field.get(null).toString());
			if (getCount() - 1 - position < 3) { // 只显示三个动态
				gif.setVisibility(View.VISIBLE);
				gif.setGifImageType(GifImageType.COVER);
				gif.setGifImage(resId);
			} else {
				img.setVisibility(View.VISIBLE);
				img.setBackgroundResource(resId);
			}
		} catch (NoSuchFieldException e) {
			msgView.setVisibility(View.VISIBLE);
			msgView.setText(ExpressionUtil.getText(cxt,
					StringUtil.Unicode2GBK(msg)));
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * play voice
	 * 
	 * @param file
	 * @param soundDuration
	 * @param voice
	 * @param isOut
	 */
	public MediaPlayer mping = new MediaPlayer();

	private void playSound(String file, TextView soundDuration,
			final ImageView voice, final boolean isOut) {
		final MediaPlayer mp = new MediaPlayer();
		voice.setVisibility(View.VISIBLE);
		soundDuration.setVisibility(View.VISIBLE);
		try {
			mp.setDataSource(file);
			mp.prepare();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		soundDuration.setText("" + mp.getDuration() / 1000 + "\"");
		voice.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mp.isPlaying())
					mp.stop();
				else {
					mp.start();
					mping = mp;
					new CountDownTimer(mp.getDuration(), 500) {
						int i = 0;

						@Override
						public void onTick(long millisUntilFinished) {
							if (i <= mp.getDuration() / 1000) {
								if (isOut)
									voice.setImageResource(resTo[i]);
								else
									voice.setImageResource(resFrom[i]);
								i++;
								if (i > 3)
									i = 0;
							}
						}

						@Override
						public void onFinish() {
							if (isOut)
								voice.setImageResource(resTo[0]);
							else
								voice.setImageResource(resFrom[0]);
						}
					}.start();
				}
			}
		});
	}

	static class ViewHolder {
		TextView timeView, msgView, soundDuration, nameView;
		ImageView head, img, gifImg, voice;
		GifView gif;
	}

}