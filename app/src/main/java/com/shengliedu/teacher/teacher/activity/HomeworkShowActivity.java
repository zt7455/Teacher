package com.shengliedu.teacher.teacher.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.shengliedu.teacher.teacher.BaseActivity;
import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.adapter.MyAdapter;
import com.shengliedu.teacher.teacher.bean.Homeworkshow;
import com.shengliedu.teacher.teacher.bean.HomeworkshowHistory;
import com.shengliedu.teacher.teacher.bean.HomeworkshowStudent;
import com.shengliedu.teacher.teacher.bean.ImageHistoryInfo;
import com.shengliedu.teacher.teacher.bean.JsonControl;
import com.shengliedu.teacher.teacher.bean.JsonControlData;
import com.shengliedu.teacher.teacher.chat.constant.Constants;
import com.shengliedu.teacher.teacher.chat.constant.MyApplication;
import com.shengliedu.teacher.teacher.chat.model.ChatItem;
import com.shengliedu.teacher.teacher.chat.util.XmppLoadThread;
import com.shengliedu.teacher.teacher.chat.xmpp.XmppConnection;
import com.shengliedu.teacher.teacher.util.Config1;
import com.shengliedu.teacher.teacher.util.FileUtils;
import com.shengliedu.teacher.teacher.util.ResultCallback;
import com.shengliedu.teacher.teacher.util.SharedPreferenceTool;
import com.shengliedu.teacher.teacher.util.UploadUtil;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class HomeworkShowActivity extends BaseActivity {
	private List<HomeworkshowStudent> imageInfolist = new ArrayList<HomeworkshowStudent>();
	private List<Homeworkshow> homeworkshows = new ArrayList<Homeworkshow>();
//	private HomeworkInfo info;
	private MyAdapter adapter;
	private ListView recyclerView;
	public static final int TAKE_PICTURE = 0;
	public Uri photoUri;
	public int userId;
	public int activityId;
	public int activity;
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		setBack();
		recyclerView = getView(R.id.recyclerView);
		adapter = new MyAdapter(homeworkshows, this);
		recyclerView.setAdapter(adapter);
		requestData();
	}

	@Override
	public void getDatas() {
		// TODO Auto-generated method stub

	}
	private Handler handlerReq=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
				JSONArray array = JSON.parseArray((String) msg.obj);
				if (array != null && array.size() > 0) {
					activity = array.getJSONObject(0).getInteger("id");
//					activityStudent?activity=100&type=4&studentImageShow=1
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("activity", activity);
					map.put("type", 4);
					map.put("studentImageShow", 1);
					doGet(Config1.getInstance().IP + "activityStudent?", map,
							new ResultCallback() {
								@Override
								public void onFailure(Call call, IOException e) {
									handlerReq.sendEmptyMessage(4);
								}

								@Override
								public void onResponse(Call call, Response response, String json) {
									Message message = Message.obtain();
									message.what = 3;
									message.obj = json;
									handlerReq.sendMessage(message);
								}
							});
				}else {
					homeworkshows.clear();
					Homeworkshow homeworkshow = new Homeworkshow();
					homeworkshow.type = 1;
					homeworkshows.add(homeworkshow);
					getLocalHistoryHomework();
							adapter.notifyDataSetChanged();
				}

			} else if (msg.what == 2) {
				homeworkshows.clear();
				Homeworkshow homeworkshow = new Homeworkshow();
				homeworkshow.type = 1;
				homeworkshows.add(homeworkshow);
				getLocalHistoryHomework();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						adapter.notifyDataSetChanged();
					}
				});
			} else if (msg.what == 3) {
//				JSONObject object = JSON.parseObject((String) msg.obj);
//				if (object.getInteger("message") == 1000) {
//				info = JSON.parseObject(
//						(String) msg.obj, HomeworkInfo.class);
				List<HomeworkshowStudent> studentArr = JSON.parseArray(
						(String) msg.obj, HomeworkshowStudent.class);
				homeworkshows.clear();
				if (studentArr != null && studentArr.size()>0) {
					Homeworkshow homeworkshow = new Homeworkshow();
					homeworkshow.type = 2;
					homeworkshow.students = studentArr;
					homeworkshow.activity = activity;
					homeworkshows.add(homeworkshow);
				}else {
					Homeworkshow homeworkshow = new Homeworkshow();
					homeworkshow.type = 1;
					homeworkshows.add(homeworkshow);
				}
				getLocalHistoryHomework();
				adapter.notifyDataSetChanged();

//			}else {
//					homeworkshows.clear();
//					Homeworkshow homeworkshow = new Homeworkshow();
//					homeworkshow.type = 1;
//					homeworkshows.add(homeworkshow);
//					getLocalHistoryHomework();
//					runOnUiThread(new Runnable() {
//						@Override
//						public void run() {
//							adapter.notifyDataSetChanged();
//						}
//					});
//				}
//			}
			} else if (msg.what == 4) {

			} else if (msg.what == 5) {
				toast("上传成功！");
				requestData();
				copyHomework();
				setResult(601);
				if (MyApplication.chatLogin) {
					JsonControl jsonControl = new JsonControl();
					jsonControl.date = new Date().getTime();
					jsonControl.type = "HomeworkShow";
					jsonControl.data = new JsonControlData();
					jsonControl.data.studentid = userId;
					jsonControl.data.link = (String) msg.obj;
					jsonControl.data.key = 0;
					try {
						XmppConnection.getInstance().setRecevier(MyApplication.userInfo.name + "slDispatchServerUser", ChatItem.CHAT);
						Log.v("TAG", "j=" + JSON.toJSONString(jsonControl));
						XmppConnection.getInstance().sendMsg(JSON.toJSONString(jsonControl), ChatItem.CHAT);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					loginAccount(MyApplication.userInfo.name, SharedPreferenceTool
							.getPassword(HomeworkShowActivity.this), (String) (msg.obj));
				}
			} else if (msg.what == 6) {

			}
		}
	};
	public void requestData() {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user", MyApplication.userInfo.id);
		map.put("date", 	MyApplication.userInfo.currentDate);
		doGet(Config1.getInstance().IP+"activity?", map,
				new ResultCallback() {
					@Override
					public void onFailure(Call call, IOException e) {
						handlerReq.sendEmptyMessage(2);
					}

					@Override
					public void onResponse(Call call, Response response,String json)  {
						Message message=Message.obtain();
						message.what=1;
						message.obj=json;
						handlerReq.sendMessage(message);
					}
				});
	}
	private void getLocalHistoryHomework() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		String file0 = Environment.getExternalStorageDirectory().getPath()
				+ "/zzkt/homeworkshow/";
		File fileDirHomework = new File(file0);
		if (fileDirHomework.exists()) {
			boolean exitT = false;
			File files[] = fileDirHomework.listFiles();
			Homeworkshow homeworkshowT = new Homeworkshow();
			homeworkshowT.type = 4;
			homeworkshows.add(homeworkshowT);
			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				Log.v("TAG", "fileName=" + file.getName());
				if ((MyApplication.userInfo.id + "").equals(file.getName()
						.split("_")[0])
						&& !str.equals(file.getName().split("_")[1])) {
					Homeworkshow homeworkshow = new Homeworkshow();
					homeworkshow.type = 3;
					HomeworkshowHistory homeworkshowHistory = new HomeworkshowHistory();
					homeworkshowHistory.describe = file.getName().split("_")[1];
					List<ImageHistoryInfo> imageHistoryInfos = new ArrayList<ImageHistoryInfo>();
					File imgFiles[] = file.listFiles();
					if (imgFiles.length > 0) {
						exitT = true;
					}
					for (int j = 0; j < imgFiles.length; j++) {
						Log.v("TAG", "fileName2=" + imgFiles[j].getName());
						ImageHistoryInfo imageHistoryInfo = new ImageHistoryInfo();
						imageHistoryInfo.path = imgFiles[j].getAbsolutePath();
						imageHistoryInfo.name = imgFiles[j].getName();
						imageHistoryInfo.filename = file.getName().split("_")[1];
						imageHistoryInfos.add(imageHistoryInfo);
					}
					homeworkshowHistory.imageHistoryInfos = imageHistoryInfos;
					homeworkshow.homeworkshowHistory = homeworkshowHistory;
					homeworkshows.add(homeworkshow);
				}
			}
			if (!exitT) {
				for (int i = 0; i < homeworkshows.size(); i++) {
					if (homeworkshows.get(i).type == 4) {
						homeworkshows.remove(i);
					}
				}
			}
		}

	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_homework_show3;
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		if (arg0==0 && arg1==RESULT_OK) {
			Log.v("TAG", "照片返回"+photoUri);
			if (photoUri!=null&&!TextUtils.isEmpty(photoUri.getPath())) {
				new Thread() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						super.run();
						if (photoUri!=null&&!TextUtils.isEmpty(photoUri.toString())) {
							Log.v("TAG", photoUri.toString());
							String a = "";
							if (photoUri.getPath().endsWith(".jpg")
									|| photoUri.getPath().endsWith(".JPG")
									|| photoUri.getPath().endsWith(".JPEG")
									|| photoUri.getPath().endsWith(".jpeg")
									|| photoUri.getPath().endsWith(".png")) {
								File file = new File(photoUri.getPath());
								Log.v("TAG", photoUri.getPath().toString());
								handler.sendEmptyMessage(10);
								UploadUtil u = new UploadUtil();
								a = u.uploadFile(file, Config1.getInstance()
										.FILEUPLOAD(), activityId + "", userId + "");
								Log.v("TAG", "a=" + a);
								handler.sendEmptyMessage(11);
							}
							try {
								org.json.JSONObject json = new org.json.JSONObject(
										a);
								Message message = Message.obtain();
								message.obj = json.getString("link");
								message.what = 0;
								handler.sendMessage(message);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}else {
							handler.sendEmptyMessage(12);
						}
					}
				}.start();
			}
			
		}else if (arg0==500 && arg1==201) {
			requestData();
		}
	}
	Handler handler = new Handler() {
		public void handleMessage(final Message msg) {
			if (msg.what == 0) {
				final String showImage=(String) msg.obj;
//				{type: 7, data: 'cloudfile/9?isLink=true&link=data/user/2/res/3952/图片.jpg'},  // 图片
				String answer="[{\"type\":7,\"data\":\""+showImage+"\"}]";
				Map<String,Object> map=new HashMap<>();
				map.put("activity", activityId + "");
				map.put("student", userId + "");
				map.put("type",4);
				map.put("content",0);
				map.put("answer", answer);
				Log.v("TAG", "activityId:" + activityId + ",userId:" + userId
						+ ",answer:" + msg.obj);
				HomeworkShowActivity.this.doPost(Config1.getInstance().IP+"activityStudent", map,
						new ResultCallback() {

							@Override
							public void onFailure(Call call, IOException e) {
								handlerReq.sendEmptyMessage(6);
							}

							@Override
							public void onResponse(Call call, Response response,String json) {
								Message message=Message.obtain();
								message.what=5;
								message.obj=showImage;
								handlerReq.sendMessage(message);
							}
						});
			} else if (msg.what == 10) {
				showRoundProcessDialog(HomeworkShowActivity.this);
			} else if (msg.what == 11) {
				showRoundProcessDialogCancel();
			} else if (msg.what == 12) {
				toast("图片为空");
			}
		}
	};
	private void copyHomework(){
		String file0 = Environment
				.getExternalStorageDirectory().getPath() + "/zzkt/homeworkshow/";
		File fileDirHomework = new File(file0);
		if (!fileDirHomework.exists()) {
			fileDirHomework.mkdirs();
		}
		SimpleDateFormat formatter    =   new    SimpleDateFormat    ("yyyy-MM-dd");
		Date    curDate    =   new    Date(System.currentTimeMillis());//获取当前时间
		String    str    =    formatter.format(curDate);
		String file1 = Environment
				.getExternalStorageDirectory().getPath() + "/zzkt/homeworkshow/"+MyApplication.userInfo.id+"_"+str+"/";
		File fileDirHomeworkDate = new File(file1);
		if (!fileDirHomeworkDate.exists()) {
			fileDirHomeworkDate.mkdirs();
		}
		if (photoUri!=null){
			String filesave = Environment
					.getExternalStorageDirectory().getPath() + "/zzkt/homeworkshow/"+MyApplication.userInfo.id+"_"+str+"/"+photoUri.getPath().substring(photoUri.getPath().lastIndexOf("/")+1)+"";
			FileUtils.copyFile(photoUri.getPath(),filesave);
		}
	}
	private void loginAccount(final String userName, final String password,final String msg) {
		new XmppLoadThread(this) {

			@Override
			protected Object load() {
				Log.v("TAG", "userName="+userName);
				Log.v("TAG", "password="+password);
				boolean isSuccess = XmppConnection.getInstance().login(
						userName, password);
				Log.v("TAG", "isSuccess="+isSuccess);
				if (isSuccess) {
					Constants.USER_NAME = userName;
					Constants.PWD = password;
				}
				return isSuccess;
			}

			@Override
			protected void result(Object o) {
				boolean isSuccess = (Boolean) o;
				MyApplication.chatLogin=isSuccess;
				if (isSuccess) {
					JsonControl jsonControl=new JsonControl();
					jsonControl.date=new Date().getTime();
					jsonControl.type="HomeworkShow";
					jsonControl.data=new JsonControlData();
					jsonControl.data.studentid=userId;
					jsonControl.data.link=msg;
					jsonControl.data.key=0;
					try {
						XmppConnection.getInstance().setRecevier(MyApplication.userInfo.name+"slDispatchServerUser", ChatItem.CHAT);
						XmppConnection.getInstance().sendMsg(JSON.toJSONString(jsonControl), ChatItem.CHAT);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					toast("服务器连接失败");
				}
			}

		};
	}
}
