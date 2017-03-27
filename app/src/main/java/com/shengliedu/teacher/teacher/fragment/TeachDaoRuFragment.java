package com.shengliedu.teacher.teacher.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.shengliedu.teacher.teacher.BaseActivity;
import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.adapter.TeachDaoruAdapter;
import com.shengliedu.teacher.teacher.bean.Beike;
import com.shengliedu.teacher.teacher.fragment.homeworkdetail.BKHomeworkDetailActivity;
import com.shengliedu.teacher.teacher.showclass.FlashHtmlActivity;
import com.shengliedu.teacher.teacher.showclass.PicActivity;
import com.shengliedu.teacher.teacher.showclass.SynNetTextBookActivity;
import com.shengliedu.teacher.teacher.showclass.SynSeletQuestionAnalyticalActivity;
import com.shengliedu.teacher.teacher.showclass.SynZiYuanActivity;
import com.shengliedu.teacher.teacher.util.CallOtherOpeanFile;
import com.shengliedu.teacher.teacher.util.ZZKTHttpDownLoad;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class TeachDaoRuFragment extends Fragment {
	private View fView;
	private BaseActivity activity;
	private ListView listView;
	private List<Beike> guide;
	private TeachDaoruAdapter adapter;

	public TeachDaoRuFragment(){

	}
	@SuppressLint("ValidFragment")
	public TeachDaoRuFragment(BaseActivity activity, List<Beike> guide) {
		super();
		this.activity = activity;
		this.guide = guide;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		fView = inflater.inflate(R.layout.fragment_teach_daoru, container,
				false);
		initView();
		return fView;
	}
	private Handler handlerReq=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
				String link= (String) msg.obj;
				activity.showRoundProcessDialogCancel();
				CallOtherOpeanFile callOtherOpeanFile = new CallOtherOpeanFile();
				callOtherOpeanFile
						.openFile(
								activity,new File(
										Environment
												.getExternalStorageDirectory()
												.getAbsolutePath()
												+ "/zzkt/teacher/"
												+ link.substring(link
												.lastIndexOf("/") + 1)));
			}else if (msg.what==2){
				activity.showRoundProcessDialogCancel();
			}
		}
	};
	@SuppressLint("NewApi")
	private void initView() {
		// TODO Auto-generated method stub
		listView = (ListView) fView
				.findViewById(R.id.list_teach);
		if (guide != null) {
			adapter = new TeachDaoruAdapter(activity, guide);
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
					Intent intent = null;
					Bundle bundle = null;
					int content_type = guide.get(i).content_type;
					int id = guide.get(i).content_id;
					int subType = guide.get(i).subType;
//					String title = guide.get(arg2).content.get(arg3).title;
//					int id = guide.get(arg2).content.get(arg3).id;
//					int subType = guide.get(arg2).content.get(arg3).subType;
//					String questionType = guide.get(arg2).content
//							.get(arg3).questionType + "";
//					String questionContentType = guide.get(arg2).content
//							.get(arg3).questionContentType + "";
					int school = guide.get(i).school;
					int from = guide.get(i).from;
					int book = guide.get(i).book;
					String ip = guide.get(i).schoolIp;
					String name = guide.get(i).name;
					final String link = guide.get(i).link;
//					final String lineCount = guide.get(arg2).content
//							.get(arg3).lineCount;
					switch (content_type) {
						case 0:// 手动添加文本：{content_type = 0，name}

							break;
						case 1:// 素材：{content_type =
							// 1，name，link，id，school,subType}，subType说明：1素材，2试卷，4连接，5文本，6百宝箱
							intent = new Intent();
							bundle = new Bundle();
							bundle.putString("plan_info", name);
							bundle.putInt("content_type", content_type);
							if (subType == 1) {
								if (!TextUtils.isEmpty(link)) {
									if (link.toLowerCase().endsWith(".ppt")
											|| link.toLowerCase().endsWith(
											".pptx")
											|| link.toLowerCase().endsWith(
											".doc")
											|| link.toLowerCase().endsWith(
											".docx")
											|| link.toLowerCase().endsWith(
											".xls")
											|| link.toLowerCase().endsWith(
											".xlsx")
											|| link.toLowerCase().endsWith(
											".txt")) {
										Log.v("TAG", ip
												+ "file/download?id=" + id);
										Log.v("TAG",
												Environment
														.getExternalStorageDirectory()
														.getAbsolutePath()
														+ "/zzkt/teacher/"
														+ link.substring(link
														.lastIndexOf("/") + 1));
										File file = new File(
												Environment
														.getExternalStorageDirectory()
														.getAbsolutePath()
														+ "/zzkt/teacher/"
														+ link.substring(link
														.lastIndexOf("/") + 1));
										if (!file.exists()) {
											activity.showRoundProcessDialog(activity);
											ZZKTHttpDownLoad zzktHttpDownLoad = new ZZKTHttpDownLoad();
											zzktHttpDownLoad
													.downloads(
															"http://"+ip+"/"
																	+ "file/download?id="
																	+ id,
															Environment
																	.getExternalStorageDirectory()
																	.getAbsolutePath()
																	+ "/zzkt/teacher/"
																	+ link.substring(link
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
																	message.obj=link;
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
												ip);
										intent.putExtras(bundle);
										intent.setClass(activity,
												FlashHtmlActivity.class);
										startActivity(intent);
									} else if (link.endsWith(".PNG")
											|| link.endsWith(".png")
											|| link.endsWith(".jpg")
											|| link.endsWith(".JPG")) {
										bundle.putString("html", link);
										bundle.putString("content_host",
												"http://"+ip+"/");
										intent.putExtras(bundle);
										intent.setClass(activity,
												PicActivity.class);
										startActivity(intent);
									} else if (link.endsWith(".mp4")
											|| link.endsWith(".mp3")
											|| link.endsWith(".rmvb")
											|| link.endsWith(".avi")
											|| link.endsWith(".wmv")
											|| link.endsWith(".3gp")) {
										String mimeType = getMIMEType(ip
												+ link);
										Intent mediaIntent = new Intent(
												Intent.ACTION_VIEW);
										mediaIntent
												.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
										Log.v("TAG", "url="
												+ (ip + link)
												+ "mimeType=" + mimeType);
										mediaIntent.setDataAndType(
												Uri.parse("http://"+ip+"/"
														+ link), mimeType);
										startActivity(mediaIntent);
									}
								}
							} else if (subType == 4) {
								bundle.putString("html", link);
								bundle.putString("content_host",
										"http://"+ip+"/");
								intent.putExtras(bundle);
								intent.setClass(activity,
										FlashHtmlActivity.class);
								startActivity(intent);
							} else if (subType == 5) {
								bundle.putString("explanation", link);
								intent.putExtras(bundle);
								intent.setClass(
										activity,
										SynSeletQuestionAnalyticalActivity.class);
								startActivity(intent);
							} else if (subType == 6) {
								String mimeType = getMIMEType("http://"+ip+"/"
										+ link);
								Intent mediaIntent = new Intent(
										Intent.ACTION_VIEW);
								mediaIntent
										.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								Log.v("TAG", "url=" + ("http://"+ip+"/" + link)
										+ "mimeType=" + mimeType);
								mediaIntent.setDataAndType(
										Uri.parse("http://"+ip+"/" + link),
										mimeType);
								startActivity(mediaIntent);
							} else {

							}

							break;
						case 2:// 习题：{content_type =
							// 2，name，id，school，questionType，from},from说明：1表示本地，2表示云端
							// 向后台请求相关数据
							intent = new Intent();
							intent.putExtra("schoolIp","http://"+ip+"/");
							intent.putExtra("content_id",id);
							intent.setClass(getActivity(),
									BKHomeworkDetailActivity.class);
							startActivity(intent);
							break;
						case 8:// 电子课本：{content_type = 8，name，id，book}
							intent = new Intent();
							bundle = new Bundle();
							bundle.putInt("id", id);
							bundle.putInt("book", book);
							bundle.putString("name", name);
							bundle.putInt("content_type", content_type);
							bundle.putString("content_host", "http://"+ip+"/");
							intent.putExtras(bundle);
							intent.setClass(activity,
									SynNetTextBookActivity.class);
							startActivity(intent);
							break;
						case 9:// 教辅资料：{content_type = 9，name，id，book}
						case 10:// 示范资源：{content_type = 10，name，id，book}
							intent = new Intent();
							bundle = new Bundle();
							bundle.putInt("id", id);
							bundle.putInt("book", book);
							bundle.putString("name", name);
							bundle.putInt("content_type", content_type);
							bundle.putString("content_host", "http://"+ip+"/");
							intent.putExtras(bundle);
							intent.setClass(activity,
									SynZiYuanActivity.class);
							startActivity(intent);
							break;

						default:
							break;
					}
				}
			});
		}
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
			{ ".mp3", "audio/mpeg" },
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
