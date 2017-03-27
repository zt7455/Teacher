package com.shengliedu.teacher.teacher.adapter;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shengliedu.teacher.teacher.BaseActivity;
import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.activity.ConsoleActivity;
import com.shengliedu.teacher.teacher.bean.BeanHomework2;
import com.shengliedu.teacher.teacher.bean.Beike;
import com.shengliedu.teacher.teacher.fragment.homeworkdetail.BKHomeworkDetailActivity;
import com.shengliedu.teacher.teacher.showclass.FlashHtmlActivity;
import com.shengliedu.teacher.teacher.showclass.PicActivity;
import com.shengliedu.teacher.teacher.showclass.SynNetTextBookActivity;
import com.shengliedu.teacher.teacher.showclass.SynSeletQuestionAnalyticalActivity;
import com.shengliedu.teacher.teacher.showclass.SynZiYuanActivity;
import com.shengliedu.teacher.teacher.util.CallOtherOpeanFile;
import com.shengliedu.teacher.teacher.util.Config1;
import com.shengliedu.teacher.teacher.util.ResultCallback;
import com.shengliedu.teacher.teacher.util.ZZKTHttpDownLoad;
import com.shengliedu.teacher.teacher.view.NoScrollListview;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ClassPlanJCAdapter extends BaseExpandableListAdapter {

	private List<Beike> group;
	private LayoutInflater layoutInflater;
	BaseActivity activity;

	public ClassPlanJCAdapter(BaseActivity context, List<Beike> group) {
		super();
		this.group = group;
		this.activity = context;
		layoutInflater = LayoutInflater.from(context);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object getChild(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return group.get(arg0).catalogs.get(arg1);
	}

	@Override
	public long getChildId(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return arg1;
	}
	private Handler handlerReq=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
				String fileLink= (String) msg.obj;
				activity.showRoundProcessDialogCancel();
				CallOtherOpeanFile callOtherOpeanFile = new CallOtherOpeanFile();
				callOtherOpeanFile
						.openFile(
								activity,new File(
										Environment
												.getExternalStorageDirectory()
												.getAbsolutePath()
												+ "/zzkt/teacher/"
												+ fileLink
												.substring(fileLink
														.lastIndexOf("/") + 1)));
			}else if (msg.what==2){
				activity.showRoundProcessDialogCancel();
			}else if (msg.what==3){
				TextView textView= (TextView) msg.obj;
				textView.setText("已完成");
			}else if (msg.what==4){
			}
		}
	};
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = layoutInflater.inflate(
					R.layout.teach_process_child_item, parent, false);
			viewHolder.homework_finish_item_title = (TextView) convertView
					.findViewById(R.id.homework_finish_item_title);
			// viewHolder.homework_finish_item_finish = (TextView) convertView
			// .findViewById(R.id.homework_finish_item_finish);
			viewHolder.homework_finish_item_list = (NoScrollListview) convertView
					.findViewById(R.id.homework_finish_item_list);
			convertView.setTag(viewHolder);
			viewHolder.homework_finish_item_luping = (TextView) convertView
					.findViewById(R.id.homework_finish_item_luping);
			convertView.setTag(viewHolder);
			viewHolder.homework_finish_item_part = (TextView) convertView
					.findViewById(R.id.homework_finish_item_part);
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.homework_finish_item_title
				.setText(group.get(groupPosition).catalogs.get(childPosition).name);
		final List<BeanHomework2> content = group.get(groupPosition).catalogs
				.get(childPosition).contents;
		if (group.get(groupPosition).catalogs.get(childPosition).video == 1) {
			viewHolder.homework_finish_item_luping.setVisibility(View.VISIBLE);
			viewHolder.homework_finish_item_luping.setText("录屏");
		} else {
			viewHolder.homework_finish_item_luping
					.setVisibility(View.INVISIBLE);
		}
		String part = "";
		switch (group.get(groupPosition).catalogs.get(childPosition).part) {
		case 1:
			part = "课前";
			break;
		case 2:
			part = "课中";
			break;
		case 3:
			part = "课后";
			break;

		default:
			break;
		}
		viewHolder.homework_finish_item_part.setText(part);

		HomworkListAdapter adapter = new HomworkListAdapter(activity, content);
		viewHolder.homework_finish_item_list.setAdapter(adapter);
		viewHolder.homework_finish_item_list
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(final AdapterView<?> arg0, View arg1,
											int arg2, long arg3) {
						// TODO Auto-generated method stub
//						Log.v("TAG", "ss");
						Intent intent = null;
						Bundle bundle = null;
						JSONObject jsonObject= JSON.parseObject(content.get(arg2).teaching);
						int content_type = content.get(arg2).content_type;
						String title = content.get(arg2).title;
						int id = content.get(arg2).content_id;
						int subType =0;
						String questionType =  "";
						String questionContentType = "";
						int school = content.get(arg2).school;
						int from = content.get(arg2).from;
						int book = 0;
						String content_host = "http://"+content.get(arg2).schoolIp+"/";
						String name = "";
						String link = "";
						String content = "";
						String lineCount = "";
						if (jsonObject!=null){
							if (jsonObject.containsKey("subType")){
								subType = jsonObject.getInteger("subType");
							}
							if (jsonObject.containsKey("questionType")){
								questionType =jsonObject.getString("questionType");
							}
							if (jsonObject.containsKey("questionContentType")){
								questionContentType = jsonObject.getString("questionContentType");
							}
							if (jsonObject.containsKey("bookId")){
								book = jsonObject.getInteger("bookId");
							}
							if (jsonObject.containsKey("name")){
								name = jsonObject.getString("name");
							}
							if (jsonObject.containsKey("link")){
								link = jsonObject.getString("link");
							}if (jsonObject.containsKey("content")){
								content = jsonObject.getString("content");
							}
							if (jsonObject.containsKey("lineCount")){
								lineCount = jsonObject.getString("lineCount");
							}

						}
						switch (content_type) {
							case 0:// 手动添加文本：{content_type = 0，name}
								break;
							case 1:// 素材：{content_type =
								// 1，name，link，id，school,subType}，subType说明：1素材，2试卷，4连接，5文本，6百宝箱
								intent = new Intent();
								bundle = new Bundle();
								bundle.putString("plan_info", name);
								bundle.putInt("content_type", content_type);
								final String fileLink = jsonObject.getString("file");
								if (subType == 1) {
									if (!TextUtils.isEmpty(fileLink)) {
										if (fileLink.toLowerCase().endsWith(".ppt")
												|| fileLink.toLowerCase().endsWith(
												".pptx")
												|| fileLink.toLowerCase().endsWith(
												".doc")
												|| fileLink.toLowerCase().endsWith(
												".docx")
												|| fileLink.toLowerCase().endsWith(
												".xls")
												|| fileLink.toLowerCase().endsWith(
												".xlsx")
												|| fileLink.toLowerCase().endsWith(
												".txt")) {
											Log.v("TAG", content_host
													+ "file/download?id=" + id);
											Log.v("TAG",
													Environment
															.getExternalStorageDirectory()
															.getAbsolutePath()
															+ "/zzkt/teacher/"
															+ fileLink
															.substring(fileLink
																	.lastIndexOf("/") + 1));
											File file = new File(Environment
													.getExternalStorageDirectory()
													.getAbsolutePath()
													+ "/zzkt/teacher/"
													+ fileLink.substring(fileLink
													.lastIndexOf("/") + 1));
											if (!file.exists()) {
												activity.showRoundProcessDialog(activity);
												ZZKTHttpDownLoad zzktHttpDownLoad=new ZZKTHttpDownLoad();
												zzktHttpDownLoad.downloads(
														content_host
																+ "file/download?id="
																+ id,
														Environment
																.getExternalStorageDirectory()
																.getAbsolutePath()
																+ "/zzkt/teacher/"
																+ fileLink
																.substring(fileLink
																		.lastIndexOf("/") + 1),
														new Callback() {
															@Override
															public void onFailure(Call call, IOException e) {
																handlerReq.sendEmptyMessage(2);
															}

															@Override
															public void onResponse(Call call, Response response) throws IOException {
																Message message=Message.obtain();
																message.what=1;
																message.obj=fileLink;
																handlerReq.sendMessage(message);
															}
														});
											} else {
												CallOtherOpeanFile callOtherOpeanFile = new CallOtherOpeanFile();
												callOtherOpeanFile.openFile(
														activity, file);
											}
										} else if (link.endsWith(".html")
												|| link.endsWith(".htm")
												|| link.endsWith(".swf")) {
											bundle.putString("html", link);
											bundle.putString("content_host",
													content_host);
											intent.putExtras(bundle);
											intent.setClass(activity,
													FlashHtmlActivity.class);
											activity.startActivity(intent);
										} else if (link.endsWith(".PNG")
												|| link.endsWith(".png")
												|| link.endsWith(".jpg")
												|| link.endsWith(".JPG")) {
											bundle.putString("html", link);
											bundle.putString("content_host",
													content_host);
											intent.putExtras(bundle);
											intent.setClass(activity,
													PicActivity.class);
											activity.startActivity(intent);
										} else if (link.endsWith(".mp4")
												|| link.endsWith(".mp3")
												|| link.endsWith(".rmvb")
												|| link.endsWith(".avi")
												|| link.endsWith(".wmv")
												|| link.endsWith(".3gp")) {
											try{
											String url = content_host + link;
											String mimeType = getMIMEType(url);
											Intent mediaIntent = new Intent(
													Intent.ACTION_VIEW);
											mediaIntent
													.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
											Log.v("TAG", "url=" + url + "mimeType="
													+ mimeType);
											mediaIntent.setDataAndType(
													Uri.parse(url), mimeType);
											activity.startActivity(mediaIntent);
										}catch (ActivityNotFoundException e) {
											// TODO: handle exception
											Toast.makeText(activity, "sorry附件不能打开，请下载相关软件！", Toast.LENGTH_SHORT).show();
										}
											// DownLoadOpen downLoadOpen = new
											// DownLoadOpen(
											// activity);
											// downLoadOpen.downOpen(content_host
											// + "file/download?id=" +
											// content_id, link,
											// size);
											// bundle.putString("html", link);
											// intent.putExtras(bundle);
											// intent.setClass(activity,
											// MediaPlayerActivity.class);
											// activity.startActivity(intent);
										}
									}
								} else if (subType == 4) {
									bundle.putString("html", link);
									bundle.putString("content_host", content_host);
									intent.putExtras(bundle);
									intent.setClass(activity,
											FlashHtmlActivity.class);
									activity.startActivity(intent);
								} else if (subType == 5) {
									if (!TextUtils.isEmpty(link)) {
										bundle.putString("explanation", link);
										intent.putExtras(bundle);
										intent.setClass(
												activity,
												SynSeletQuestionAnalyticalActivity.class);
										activity.startActivity(intent);
									}else if (!TextUtils.isEmpty(content)) {
										bundle.putString("explanation", content);
										intent.putExtras(bundle);
										intent.setClass(
												activity,
												SynSeletQuestionAnalyticalActivity.class);
										activity.startActivity(intent);
									}
								} else if (subType == 6) {
									try{
									String mimeType = getMIMEType(content_host
											+ link);
									Intent mediaIntent = new Intent(
											Intent.ACTION_VIEW);
									mediaIntent
											.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
									Log.v("TAG", "url=" + (content_host + link)
											+ "mimeType=" + mimeType);
									mediaIntent.setDataAndType(
											Uri.parse(content_host + link),
											mimeType);
									activity.startActivity(mediaIntent);
								}catch (ActivityNotFoundException e) {
								// TODO: handle exception
								Toast.makeText(activity, "sorry附件不能打开，请下载相关软件！", Toast.LENGTH_SHORT).show();
							}
								} else {

								}

								break;
							case 2:// 习题：{content_type =
								// 2，name，id，school，questionType，from},from说明：1表示本地，2表示云端
								// 向后台请求相关数据
								intent = new Intent();
								intent.putExtra("schoolIp",content_host);
								intent.putExtra("content_id",id);
								intent.setClass(activity,
										BKHomeworkDetailActivity.class);
								activity.startActivity(intent);
								break;
							case 8:// 电子课本：{content_type = 8，name，id，book}
								intent = new Intent();
								bundle = new Bundle();
								bundle.putInt("id", id);
								bundle.putInt("book", book);
								bundle.putString("name", name);
								bundle.putInt("content_type", content_type);
								bundle.putString("content_host", content_host);
								intent.putExtras(bundle);
								intent.setClass(activity,
										SynNetTextBookActivity.class);
								activity.startActivity(intent);
								break;
							case 9:// 教辅资料：{content_type = 9，name，id，book}
							case 10:// 示范资源：{content_type = 10，name，id，book}
								intent = new Intent();
								bundle = new Bundle();
								bundle.putInt("id", id);
								bundle.putInt("book", book);
								bundle.putString("name", name);
								bundle.putInt("content_type", content_type);
								bundle.putString("content_host", content_host);
								intent.putExtras(bundle);
								intent.setClass(activity, SynZiYuanActivity.class);
								activity.startActivity(intent);
								break;

							default:
								break;
						}
					}
				});
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return group.get(groupPosition).catalogs.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return group.get(groupPosition).name;
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return group.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public View getGroupView(final int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = layoutInflater.inflate(
					R.layout.teach_process_jc_group_item, parent, false);
			viewHolder.teach_process_item_title = (TextView) convertView
					.findViewById(R.id.teach_process_item_title);
			viewHolder.teach_process_item_jc = (TextView) convertView
					.findViewById(R.id.teach_process_item_jc);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.teach_process_item_title.setText("第"
				+ group.get(groupPosition).order + "课时");
		if (ConsoleActivity.roleJiaowu) {
			viewHolder.teach_process_item_jc.setVisibility(View.VISIBLE);
			if (group.get(groupPosition).finish == 0) {
				viewHolder.teach_process_item_jc.setText("未完成");
				viewHolder.teach_process_item_jc
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View arg0) {
//								teachOutlineHour/100?finish=1
								// TODO Auto-generated method stub
								HashMap<String, Object> map = new HashMap<String, Object>();
								map.put("finish", "1");
								activity.doPut(Config1.getInstance().IP+"teachOutlineHour/"+group.get(groupPosition).id+"?", map,
										new ResultCallback() {
											@Override
											public void onFailure(Call call, IOException e) {
handlerReq.sendEmptyMessage(4);
											}

											@Override
											public void onResponse(Call call, Response response,String json)  {
												// stub
												group.get(groupPosition).finish=1;

												viewHolder.teach_process_item_jc
														.setOnClickListener(null);
												Message message=Message.obtain();
												message.what=3;
												message.obj=viewHolder.teach_process_item_jc;
												handlerReq.sendMessage(message);
											}
										});
							}
						});
			} else {
				viewHolder.teach_process_item_jc.setText("已完成");
			}

		} else {
			viewHolder.teach_process_item_jc.setVisibility(View.GONE);
		}
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

	static class ViewHolder {
		public TextView teach_process_item_title;
		public TextView teach_process_item_jc;
		public TextView homework_finish_item_title;
		public TextView homework_finish_item_luping;
		public TextView homework_finish_item_part;
		// public TextView homework_finish_item_finish;
		public NoScrollListview homework_finish_item_list;
	}

	public String getMIMEType(String url) {

		String type = "*/*";
		// 获取后缀名前的分隔符"."在fName中的位置。
		int dotIndex = url.lastIndexOf(".");
		if (dotIndex < 0) {
			return type;
		}
		/* 获取文件的后缀名 */
		String end = url.substring(dotIndex, url.length()).toLowerCase();
		Log.v("TAG", "end" + end);
		if (end == "")
			return type;
		// 在MIME和文件类型的匹配表中找到对应的MIME类型。
		for (int i = 0; i < MIME_MapTable.length; i++) {

			if (end.equalsIgnoreCase(MIME_MapTable[i][0]))
				type = MIME_MapTable[i][1];
		}
		return type;
	}

	// 可以自己随意添加
	private String[][] MIME_MapTable = {
			// {后缀名，MIME类型}
			{ ".3gp", "video/3gpp" },
			{ ".apk", "application/vnd.android.package-archive" },
			{ ".asf", "video/x-ms-asf" },
			{ ".avi", "video/x-msvideo" },
			{ ".bin", "application/octet-stream" },
			{ ".bmp", "image/bmp" },
			{ ".c", "text/plain" },
			{ ".class", "application/octet-stream" },
			{ ".conf", "text/plain" },
			{ ".cpp", "text/plain" },
			{ ".doc", "application/msword" },
			{ ".docx",
					"application/vnd.openxmlformats-officedocument.wordprocessingml.document" },
			{ ".xls", "application/vnd.ms-excel" },
			{ ".xlsx",
					"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" },
			{ ".exe", "application/octet-stream" },
			{ ".gif", "image/gif" },
			{ ".gtar", "application/x-gtar" },
			{ ".gz", "application/x-gzip" },
			{ ".h", "text/plain" },
			{ ".htm", "text/html" },
			{ ".html", "text/html" },
			{ ".jar", "application/java-archive" },
			{ ".java", "text/plain" },
			{ ".jpeg", "image/jpeg" },
			{ ".jpg", "image/jpeg" },
			{ ".js", "application/x-javascript" },
			{ ".log", "text/plain" },
			{ ".m3u", "audio/x-mpegurl" },
			{ ".m4a", "audio/mp4a-latm" },
			{ ".m4b", "audio/mp4a-latm" },
			{ ".m4p", "audio/mp4a-latm" },
			{ ".m4u", "video/vnd.mpegurl" },
			{ ".m4v", "video/x-m4v" },
			{ ".mov", "video/quicktime" },
			{ ".mp2", "audio/x-mpeg" },
			{ ".mp3", "audio/x-mpeg" },
			{ ".mp4", "video/mp4" },
			{ ".mpc", "application/vnd.mpohun.certificate" },
			{ ".mpe", "video/mpeg" },
			{ ".mpeg", "video/mpeg" },
			{ ".mpg", "video/mpeg" },
			{ ".mpg4", "video/mp4" },
			{ ".mpga", "audio/mpeg" },
			{ ".msg", "application/vnd.ms-outlook" },
			{ ".ogg", "audio/ogg" },
			{ ".pdf", "application/pdf" },
			{ ".png", "image/png" },
			{ ".pps", "application/vnd.ms-powerpoint" },
			{ ".ppt", "application/vnd.ms-powerpoint" },
			{ ".pptx",
					"application/vnd.openxmlformats-officedocument.presentationml.presentation" },
			{ ".prop", "text/plain" }, { ".rc", "text/plain" },
			{ ".rmvb", "audio/x-pn-realaudio" }, { ".rtf", "application/rtf" },
			{ ".sh", "text/plain" }, { ".tar", "application/x-tar" },
			{ ".tgz", "application/x-compressed" }, { ".txt", "text/plain" },
			{ ".wav", "audio/x-wav" }, { ".wma", "audio/x-ms-wma" },
			{ ".wmv", "audio/x-ms-wmv" },
			{ ".wps", "application/vnd.ms-works" }, { ".xml", "text/plain" },
			{ ".z", "application/x-compress" },
			{ ".zip", "application/x-zip-compressed" }, { "", "*/*" } };
}
