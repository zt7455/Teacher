package com.shengliedu.teacher.teacher;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.shengliedu.teacher.teacher.chat.activitys.LoginActivity;
import com.shengliedu.teacher.teacher.chat.constant.MyApplication;
import com.shengliedu.teacher.teacher.util.DrawerToast;
import com.shengliedu.teacher.teacher.util.LoadingDialog;
import com.shengliedu.teacher.teacher.util.ResultCallback;
import com.shengliedu.teacher.teacher.view.CustomTitle;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 
 zhangtao
 * 
 * @date 2014-9-25
 */
public abstract class BaseActivity extends FragmentActivity implements
		BaseInterface {

	private static final String TAG = "BaseActivity";
	public ProgressDialog progressDialog;
	// public static ScreenManager screenManager = ScreenManager
	// .getScreenManager();
	//
	private LoadingDialog mDialog;

	public static int screenHigh;
	public static int screenwidth;
	public static float scale;

	public BaseActivity context = this;
	public CustomTitle title;
	public LocalActivityManager manager;
	public DrawerToast mToast;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setFullScreen(false);
		setContentView(setLayout());
		loadTitle();

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenwidth = dm.widthPixels;
		screenHigh = dm.heightPixels;
		scale = getResources().getDisplayMetrics().density;
		manager = new LocalActivityManager(this, true);
		manager.dispatchCreate(savedInstanceState);
		// login_params = JPushInterface.getRegistrationID(this);
		// Log.i(TAG, "-->"+login_params);
		initViews();
		getDatas();
		mToast = DrawerToast.getInstance(getApplicationContext());
	}

	/**
	 * @param px
	 * @return dp
	 */
	public int Px2Dp(float px) {
		return (int) (px / scale + 0.5f);
	}

	/**
	 * 获得View
	 * 
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public final <E extends View> E getView(int id) {
		try {

			return (E) findViewById(id);

		} catch (Exception e) {

			e.printStackTrace();

			return null;

		}
	}

	/**
	 * 设置布局
	 * 
	 * @return
	 */
	public abstract int setLayout();

	/**
	 * true 隐藏actionbar
	 * 
	 * @param fullScreen
	 */
	public void setFullScreen(boolean fullScreen) {
		if (fullScreen) {
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		} else {
			requestWindowFeature(Window.FEATURE_NO_TITLE);
		}

	}

	private OkHttpClient okHttpClient = new OkHttpClient();

	/**
	 * put请求 map为body
	 *
	 * @param url
	 * @param map
	 * @param callback
	 */
	public void doPut(String url, Map<String, Object> map,final ResultCallback callback) {

		// FormBody.Builder builder = new FormBody.Builder();
		// FormBody body=new FormBody.Builder().add("key", "value").build();

		/**
		 * 创建请求的参数body
		 */
		FormBody.Builder builder = new FormBody.Builder();

		/**
		 * 遍历key
		 */

		Log.v("TAG", "url="+url);
		if (null != map) {
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				System.out.println("Key = " + entry.getKey() + ", Value = "
						+ entry.getValue());
				builder.add(entry.getKey(), entry.getValue().toString());

			}
		}

		RequestBody body = builder.build();

		Request request = new Request.Builder().url(url).put(body).build();
		okHttpClient.newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call call, Response response)
					throws IOException {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						showRoundProcessDialogCancel();
					}
				});

				callback.onResponse(call, response,response.body().string());
			}

			@Override
			public void onFailure(Call call, IOException arg1) {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						showRoundProcessDialogCancel();
					}
				});
				callback.onFailure(call,arg1);
			}
		});
	}
	/**
	 * post请求 map为body
	 *
	 * @param url
	 * @param map
	 * @param callback
	 */
	public void doPost(String url, Map<String, Object> map,final ResultCallback callback) {

		// FormBody.Builder builder = new FormBody.Builder();
		// FormBody body=new FormBody.Builder().add("key", "value").build();

		/**
		 * 创建请求的参数body
		 */
		FormBody.Builder builder = new FormBody.Builder();

		/**
		 * 遍历key
		 */

		Log.v("TAG", "url="+url);
		if (null != map) {
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				System.out.println("Key = " + entry.getKey() + ", Value = "
						+ entry.getValue());
				builder.add(entry.getKey(), entry.getValue().toString());

			}
		}

		RequestBody body = builder.build();

		Request request = new Request.Builder().url(url).post(body).build();
		okHttpClient.newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call call, Response response)
					throws IOException {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						showRoundProcessDialogCancel();
					}
				});

				callback.onResponse(call, response,response.body().string());
			}

			@Override
			public void onFailure(Call call, IOException arg1) {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						showRoundProcessDialogCancel();
					}
				});
				callback.onFailure(call,arg1);
			}
		});
	}
	/**
	 * delete请求 map为body
	 *
	 * @param url
	 * @param map
	 * @param callback
	 */
	public void doDelete(String url, Map<String, Object> map,final ResultCallback callback) {

		// FormBody.Builder builder = new FormBody.Builder();
		// FormBody body=new FormBody.Builder().add("key", "value").build();

		/**
		 * 创建请求的参数body
		 */
		FormBody.Builder builder = new FormBody.Builder();

		/**
		 * 遍历key
		 */

		Log.v("TAG", "url="+url);
		if (null != map) {
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				System.out.println("Key = " + entry.getKey() + ", Value = "
						+ entry.getValue());
				builder.add(entry.getKey(), entry.getValue().toString());

			}
		}

		RequestBody body = builder.build();

		Request request = new Request.Builder().url(url).delete(body).build();
		okHttpClient.newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call call, Response response)
					throws IOException {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						showRoundProcessDialogCancel();
					}
				});
				if (response.code()==200) {
					callback.onResponse(call, response, response.body().string());
				}
			}

			@Override
			public void onFailure(Call call, IOException arg1) {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						showRoundProcessDialogCancel();
					}
				});
				callback.onFailure(call,arg1);
			}
		});
	}

	/**
	 * Get请求
	 * 
	 * @param url
	 * @params  params
	 * @params callBack
	 */
	public void doGet(String url, Map<String, ?> map,
			final ResultCallback callback) {
		StringBuilder sb=new StringBuilder();
		Set<String> set=map.keySet();
		 Iterator<String> iterator=set.iterator();
		 while (iterator.hasNext()) {
			 String key=iterator.next();
			 sb.append(key+"=");
			 sb.append(map.get(key)+"&");
		}
		String value=sb.toString();
		if (value.length()>0) {
			value=value.substring(0,value.lastIndexOf("&"));
		}
		url=url+value;
		Log.v("TAG", "url="+url);
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				showRoundProcessDialog(context);
			}
		});
		Request.Builder requestBuilder = new Request.Builder().url(url);
		final Request request = requestBuilder.build();
		Call mcall = okHttpClient.newCall(request);
		mcall.enqueue(new Callback() {

			@Override
			public void onResponse(Call call, Response response)
					throws IOException {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						showRoundProcessDialogCancel();
					}
				});
				if (response.code()==401){
					mToast.show(response.body().string());
				}else {
					callback.onResponse(call,response,response.body().string());
				}

			}

			@Override
			public void onFailure(Call call, IOException arg1) {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						showRoundProcessDialogCancel();
					}
				});
				callback.onFailure(call,arg1);
			}
		});
	}

	public void startActivity(Class<? extends Activity> clazz) {
		startActivity(new Intent(this, clazz));
	}

	/**
	 * 方法描述：设置标题
	 * 
	 * @param text
	 */
	public void showTitle(String text) {
		title.setVisibility(View.VISIBLE);
		title.setTitle(text);
	}

	/**
	 * 加载标题
	 */
	private void loadTitle() {
		title = getView(R.id.ctitle);
	}

	public void setLeftText(String text, OnClickListener listener) {
		title.textView_left.setVisibility(View.VISIBLE);
		title.textView_left.setText(text);
		title.textView_left.setOnClickListener(listener);
	}

	public void setRightText(String text, OnClickListener listener) {
		title.textView_right.setVisibility(View.VISIBLE);
		title.textView_right.setText(text);
		title.textView_right.setOnClickListener(listener);
	}
	public void setRightText(String text) {
		title.textView_right.setVisibility(View.VISIBLE);
		title.textView_right.setText(text);
	}

	public void setRightText_l(String text, OnClickListener listener) {
		title.textView_right_l.setVisibility(View.VISIBLE);
		title.textView_right_l.setText(text);
		title.textView_right_l.setOnClickListener(listener);
	}

	public void setLeftImage(int res, OnClickListener listener) {
		title.imageView_left.setVisibility(View.VISIBLE);
		title.imageView_left.setImageResource(res);
		title.imageView_left.setOnClickListener(listener);
	}

	public void setRightImage(int res, OnClickListener listener) {
		title.imageView_right.setVisibility(View.VISIBLE);
		title.imageView_right.setImageResource(res);
		title.imageView_right.setOnClickListener(listener);
	}

	public void showMoonTitle(String text) {
		title.setVisibility(View.VISIBLE);
		title.setMoonTile(text);
	}
	public void hideTitle() {
		title.setVisibility(View.GONE);
	}
	/**
	 * 设置左上角返回键
	 */
	public void setBack() {
		title.imageView_left.setVisibility(View.VISIBLE);
		title.imageView_left.setImageResource(R.mipmap.icon_back_small_white);
		title.imageView_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finishActivityByAniamtion();
			}
		});
	}

	/**
	 * 设置左上角返回键
	 */
	public void setBackSetResult(final int result) {
		title.imageView_left.setVisibility(View.VISIBLE);
		title.imageView_left.setImageResource(R.mipmap.icon_back_small_white);
		title.imageView_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setResult(result);
				finishActivityByAniamtion();
			}
		});
	}
	/**
	 * 设置左上角返回键
	 */
	public void setBack2(OnClickListener onclicklistener) {
		title.imageView_left.setVisibility(View.VISIBLE);
		title.imageView_left.setImageResource(R.mipmap.icon_back_small_white);
		title.imageView_left.setOnClickListener(onclicklistener);
	}

	/**
	 * 隐藏键盘
	 */
	public void hideInputMethod() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive() && getCurrentFocus() != null) {
			imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	/**
	 * 
	 * 
	 * @param mContext
	 */
	public void showRoundProcessDialog(Context mContext) {
		if (mDialog == null) {
			synchronized (LoadingDialog.class) {
				if (mDialog == null) {
					mDialog = LoadingDialog.createDialog(context);
				}
			}
		}
		mDialog.show();
	}

	/**
	 * 取消加载转圈
	 */
	public void showRoundProcessDialogCancel() {
		if (mDialog != null) {
			mDialog.dismiss();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finishActivityByAniamtion();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			// if (getCurrentFocus() != null
			// && getCurrentFocus().getWindowToken() != null) {
			// return App.manager.hideSoftInputFromWindow(getCurrentFocus()
			// .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			// }
		}
		return super.onTouchEvent(event);
	}

	/**
	 * 结束activity带动画
	 */
	public void finishActivityByAniamtion() {
		this.finish();
		overridePendingTransition(R.anim.a_my_out_zoom1, R.anim.a_my_out_zoom2);
	}

	/**
	 * 传参启动activity
	 * 
	 * @param intent
	 */
	public void startActivityByAniamtion(Intent intent) {
		startActivity(intent);
		overridePendingTransition(R.anim.a_my_in_scale_min_max1,
				R.anim.a_my_in_alpha_action);
	}

	/**
	 * 跳到已启动的activity，而不会新启动activity
	 */
	public void startExitsActivityByAniamtion(Intent intent) {
		intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
		overridePendingTransition(R.anim.a_my_in_scale_min_max1,
				R.anim.a_my_in_alpha_action);
	}

	/**
	 * 不传参启动activity
	 * 
	 * @param c
	 */
	public void startActivityByAniamtion(Class<?> c) {
		Intent intent = new Intent(this, c);
		startActivity(intent);
		overridePendingTransition(R.anim.a_my_in_scale_min_max1,
				R.anim.a_my_in_alpha_action);
	}

	/**
	 * 启动带动画带回调的activity
	 * 
	 * @param intent
	 * @param requestCode
	 */
	public void startActivityForResultByAniamtion(Intent intent, int requestCode) {
		startActivityForResult(intent, requestCode);
		overridePendingTransition(R.anim.a_my_in_scale_min_max1,
				R.anim.a_my_in_alpha_action);
	}

	/**
	 * @param resId
	 */
	private Toast toast;

	public void toast(final String msg) {
		// ToastUtil.toast(this, msg);
//		mToast.show(msg);
		if ("MI 5s".equals(android.os.Build.MODEL) || "vivo X6".equals(android.os.Build.MODEL)) {
			mToast.show(msg);
		} else {
			new Thread() {
				public void run() {
					Looper.prepare();
					if (toast != null) {
						toast.cancel();
					}
					toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
					toast.show();
					Looper.loop();
				}

				;
			}.start();
		}
	}

	public void toastCancel() {
		try {
			if (toast != null) {
				toast.cancel();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		manager.dispatchResume();
		if (MyApplication.userInfo==null) {
			Intent intent = new Intent(this,
					LoginActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			finish();
		}
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		manager.dispatchStop();
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		manager.dispatchPause(true);
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		manager.dispatchDestroy(true);
	}
}
