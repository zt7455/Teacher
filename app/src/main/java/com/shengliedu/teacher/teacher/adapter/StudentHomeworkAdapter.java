package com.shengliedu.teacher.teacher.adapter;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.activity.OneStudentHomeworkActivity;
import com.shengliedu.teacher.teacher.bean.courseware_content;
import com.shengliedu.teacher.teacher.bean.Normal;
import com.shengliedu.teacher.teacher.chat.constant.MyApplication;
import com.shengliedu.teacher.teacher.util.Config1;
import com.shengliedu.teacher.teacher.util.HandlerMessageObj;
import com.shengliedu.teacher.teacher.util.HttpImage;
import com.shengliedu.teacher.teacher.util.ResultCallback;
import com.shengliedu.teacher.teacher.view.NoScrollGridView;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import it.sephiroth.android.library.picasso.Picasso;
import okhttp3.Call;
import okhttp3.Response;

import static com.shengliedu.teacher.teacher.R.id.one_student_homework_item_submit;
import static com.shengliedu.teacher.teacher.R.id.pj_rg;
import static com.shengliedu.teacher.teacher.R.id.tupian;

public class StudentHomeworkAdapter extends BaseAdapter {
	private OneStudentHomeworkActivity context;
	private List<courseware_content> persons;
	private LayoutInflater inflater;
	private static boolean playState = false; // 播放状态
	private int userId;
	public StudentHomeworkAdapter(OneStudentHomeworkActivity context,
								  List<courseware_content> persons, int userId) {
		this.context = context;
		this.persons = persons;
		this.inflater = LayoutInflater.from(context);
		this.userId = userId;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return persons.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return persons.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	private RecordHolder recordHolder;

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView != null && convertView.getTag() != null && convertView.getTag() instanceof VideoHolder) {
			((VideoHolder) convertView.getTag()).jcVideoPlayer.release();
		}
		Log.v("Position","position======"+position);
		Log.v("Position","getItemViewType(position)======"+getItemViewType(position));
		if (getItemViewType(position) == 1) {
			TextViewHolder textViewHolder;
			if (convertView != null && convertView.getTag() != null && convertView.getTag() instanceof TextViewHolder) {
				textViewHolder = (TextViewHolder) convertView.getTag();
			} else {
				textViewHolder = new TextViewHolder();
				LayoutInflater mInflater = LayoutInflater.from(context);
				convertView = mInflater.inflate(R.layout.one_student_homework_awser_text, null);
				textViewHolder.one_student_homework_item_awser = (TextView) convertView.findViewById(R.id.one_student_homework_item_awser);
				textViewHolder.pj_rg = (RadioGroup) convertView.findViewById(pj_rg);
				textViewHolder.one_student_homework_item_submit = (LinearLayout) convertView.findViewById(one_student_homework_item_submit);
				textViewHolder.you = (RadioButton) convertView.findViewById(R.id.you);
				textViewHolder.liang = (RadioButton) convertView.findViewById(R.id.liang);
				textViewHolder.zhong = (RadioButton) convertView.findViewById(R.id.zhong);
				textViewHolder.cha = (RadioButton) convertView.findViewById(R.id.cha);
				convertView.setTag(textViewHolder);
			}
			JSONObject object=JSON.parseObject(persons.get(position).teaching);
			if (persons.get(position).content_type == 2) {
				textViewHolder.pj_rg.setVisibility(View.GONE);
				String awser = "";
				List<Normal> studentAnswer= JSON.parseArray(persons.get(position).answer,Normal.class);

				if (!MyApplication.listIsEmpty(studentAnswer) && studentAnswer.size()>0) {
					textViewHolder.one_student_homework_item_awser
							.setText(Html.fromHtml(object.getString("name"))+"\n"+studentAnswer.get(0).data+(persons.get(position).wrong==0?"（正确）":"（错误）"));
				} else {
					textViewHolder.one_student_homework_item_awser
							.setText(Html.fromHtml(object.getString("name"))+"\n"+"未回答");
				}
			}else if (persons.get(position).content_type == 1 && persons.get(position).submitType == 1){
				List<Normal> studentAnswer= JSON.parseArray(persons.get(position).answer,Normal.class);
				if (!MyApplication
						.listIsEmpty(studentAnswer) && studentAnswer.size()>0) {
					textViewHolder.one_student_homework_item_submit.setVisibility(View.VISIBLE);
					textViewHolder.one_student_homework_item_awser.setText(object.getString("name")+"\n"+
							studentAnswer.get(0).data);
					setPJ(textViewHolder.pj_rg,textViewHolder.you,textViewHolder.liang,textViewHolder.zhong,textViewHolder.cha,persons.get(position).evaluate,persons.get(position).content);
				}else {
					textViewHolder.one_student_homework_item_awser.setText(object.getString("name")+"(未完成)");
					textViewHolder.one_student_homework_item_submit.setVisibility(View.GONE);
				}
			}
		} else if (getItemViewType(position) == 2) {
			PhotoHolder photoHolder;
			if (convertView != null && convertView.getTag() != null && convertView.getTag() instanceof PhotoHolder) {
				photoHolder = (PhotoHolder) convertView.getTag();
			} else {
				photoHolder = new PhotoHolder();
				LayoutInflater mInflater = LayoutInflater.from(context);
				convertView = mInflater.inflate(R.layout.one_student_homework_awser_photo, null);
				photoHolder.tupian = (NoScrollGridView) convertView.findViewById(tupian);
				photoHolder.one_student_homework_item_submit = (LinearLayout) convertView.findViewById(one_student_homework_item_submit);
				photoHolder.is_over = (TextView) convertView.findViewById(R.id.is_over);
				photoHolder.pj_rg = (RadioGroup) convertView.findViewById(pj_rg);
				photoHolder.you = (RadioButton) convertView.findViewById(R.id.you);
				photoHolder.liang = (RadioButton) convertView.findViewById(R.id.liang);
				photoHolder.zhong = (RadioButton) convertView.findViewById(R.id.zhong);
				photoHolder.cha = (RadioButton) convertView.findViewById(R.id.cha);
				convertView.setTag(photoHolder);
			}
			List<Normal> studentAnswer= JSON.parseArray(persons.get(position).answer,Normal.class);
			JSONObject object=JSON.parseObject(persons.get(position).teaching);
			if (!MyApplication
					.listIsEmpty(studentAnswer)  && studentAnswer.size()>0) {
				photoHolder.is_over.setText(object.getString("name")+"(已完成)");
				photoHolder.one_student_homework_item_submit.setVisibility(View.VISIBLE);
				photoHolder.tupian.setVisibility(View.VISIBLE);
				HomeworkShowImageAdapter adapter = new HomeworkShowImageAdapter(
						context, studentAnswer,persons.get(position).content);
				photoHolder.tupian.setAdapter(adapter);
				setPJ(photoHolder.pj_rg,photoHolder.you,photoHolder.liang,photoHolder.zhong,photoHolder.cha,persons.get(position).evaluate,persons.get(position).content);
			}else {
				photoHolder.is_over.setText(object.getString("name")+"(未完成)");
				photoHolder.tupian.setVisibility(View.GONE);
				photoHolder.one_student_homework_item_submit.setVisibility(View.GONE);
			}

		}else if (getItemViewType(position) == 3) {
			if (convertView != null && convertView.getTag() != null && convertView.getTag() instanceof RecordHolder) {
				recordHolder = (RecordHolder) convertView.getTag();
			} else {
				recordHolder = new RecordHolder();
				LayoutInflater mInflater = LayoutInflater.from(context);
				convertView = mInflater.inflate(R.layout.one_student_homework_awser_record, null);
				recordHolder.luyin = (ImageButton) convertView.findViewById(R.id.luyin);
				recordHolder.one_student_homework_item_submit = (LinearLayout) convertView.findViewById(one_student_homework_item_submit);
				recordHolder.is_over = (TextView) convertView.findViewById(R.id.is_over);
				recordHolder.pj_rg = (RadioGroup) convertView.findViewById(pj_rg);
				recordHolder.you = (RadioButton) convertView.findViewById(R.id.you);
				recordHolder.liang = (RadioButton) convertView.findViewById(R.id.liang);
				recordHolder.zhong = (RadioButton) convertView.findViewById(R.id.zhong);
				recordHolder.cha = (RadioButton) convertView.findViewById(R.id.cha);
				convertView.setTag(recordHolder);
			}
			final List<Normal> studentAnswer= JSON.parseArray(persons.get(position).answer,Normal.class);
			JSONObject object=JSON.parseObject(persons.get(position).teaching);
			if (!MyApplication
					.listIsEmpty(studentAnswer)&& studentAnswer.size()>0) {
				recordHolder.one_student_homework_item_submit.setVisibility(View.VISIBLE);
				recordHolder.is_over.setText(object.getString("name")+"(已完成)");
				recordHolder.luyin.setVisibility(View.VISIBLE);
				recordHolder.luyin.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (!playState) {
							mediaPlayer = new MediaPlayer();
							try {
								// 模拟器里播放传url，真机播放传getAmrPath()
								// mediaPlayer.setDataSource(url);
								mediaPlayer.setDataSource(Config1.IP
										+ studentAnswer
										.get(0).data);
								mediaPlayer.prepare();
								mediaPlayer.start();
								recordHolder.luyin
										.setImageResource(R.mipmap.luyin_pause);
								playState = true;
								// 设置播放结束时监听
								mediaPlayer
										.setOnCompletionListener(new OnCompletionListener() {

											@Override
											public void onCompletion(
													MediaPlayer mp) {
												if (playState) {
													recordHolder.luyin
															.setImageResource(R.mipmap.luyin_play);
													playState = false;
												}
											}
										});
							} catch (IllegalArgumentException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IllegalStateException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						} else {
							if (mediaPlayer.isPlaying()) {
								mediaPlayer.stop();

								playState = false;
							} else {
								playState = false;
							}
							recordHolder.luyin
									.setImageResource(R.mipmap.luyin_play);
						}

					}
				});
				setPJ(recordHolder.pj_rg,recordHolder.you,recordHolder.liang,recordHolder.zhong,recordHolder.cha,persons.get(position).evaluate,persons.get(position).content);
			}else {
				recordHolder.one_student_homework_item_submit.setVisibility(View.GONE);
				recordHolder.is_over.setText(object.getString("name")+"(未完成)");
			}
		}else if (getItemViewType(position) == 4) {
			final VideoHolder videoHolder;
			if (convertView != null && convertView.getTag() != null && convertView.getTag() instanceof VideoHolder) {
				videoHolder = (VideoHolder) convertView.getTag();
			} else {
				videoHolder = new VideoHolder();
				LayoutInflater mInflater = LayoutInflater.from(context);
				convertView = mInflater.inflate(R.layout.one_student_homework_awser_vedio, null);
				videoHolder.jcVideoPlayer = (JCVideoPlayerStandard) convertView.findViewById(R.id.videoplayer);
				videoHolder.one_student_homework_item_submit = (LinearLayout) convertView.findViewById(one_student_homework_item_submit);
				videoHolder.is_over = (TextView) convertView.findViewById(R.id.is_over);
				videoHolder.pj_rg = (RadioGroup) convertView.findViewById(pj_rg);
				videoHolder.you = (RadioButton) convertView.findViewById(R.id.you);
				videoHolder.liang = (RadioButton) convertView.findViewById(R.id.liang);
				videoHolder.zhong = (RadioButton) convertView.findViewById(R.id.zhong);
				videoHolder.cha = (RadioButton) convertView.findViewById(R.id.cha);
				convertView.setTag(videoHolder);
			}
			final List<Normal> studentAnswer= JSON.parseArray(persons.get(position).answer,Normal.class);
			JSONObject object=JSON.parseObject(persons.get(position).teaching);
			if (!MyApplication
					.listIsEmpty(studentAnswer)&& studentAnswer.size()>0) {
				videoHolder.one_student_homework_item_submit.setVisibility(View.VISIBLE);
				videoHolder.is_over.setText(object.getString("name")+"(已完成)");
				final String videoPath = Config1.IP
						+ studentAnswer.get(0).data;
				new Thread(){
					@Override
					public void run() {
						super.run();
						Bitmap bitmap=createVideoThumbnail(videoPath,480,320);
						HandlerMessageObj handlerMessageObj=new HandlerMessageObj();
						handlerMessageObj.setLink(videoPath);
						handlerMessageObj.setJcVideoPlayer(videoHolder.jcVideoPlayer);
						handlerMessageObj.setBitmap(bitmap);
						Message message=Message.obtain();
						message.obj=handlerMessageObj;
						message.what=3;
						handlerReq.sendMessage(message);
					}
				}.start();

				setPJ(videoHolder.pj_rg,videoHolder.you,videoHolder.liang,videoHolder.zhong,videoHolder.cha,persons.get(position).evaluate,persons.get(position).content);
			}else {
				videoHolder.one_student_homework_item_submit.setVisibility(View.GONE);
				videoHolder.is_over.setText(object.getString("name")+"(未完成)");
			}

		}
		return convertView;
	}

	public  MediaPlayer mediaPlayer;
	public void destroy() {
		// TODO Auto-generated method stub
		if (mediaPlayer!=null) {
			if (mediaPlayer.isPlaying()) {
				mediaPlayer.stop();
				playState = false;
				mediaPlayer=null;
			}
		}
	}
	private Handler handlerReq=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
//				Log.v("ssssssssssss", (String) msg.obj+"");
				context.setBackSetResult(context.result);
				context.toast("评价成功");
			}else if (msg.what==2){

			}else if (msg.what==3){
				HandlerMessageObj handlerMessageObj= (HandlerMessageObj) msg.obj;
				JCVideoPlayerStandard jcVideoPlayerStandard=handlerMessageObj.getJcVideoPlayer();
				String videoPath=handlerMessageObj.getLink();
				Bitmap bitmap=handlerMessageObj.getBitmap();
				jcVideoPlayerStandard.setUp(videoPath, JCVideoPlayer.SCREEN_LAYOUT_LIST,
						"视频作业");
				if(bitmap!=null){
					File file=new File(HttpImage.SDPATH+"sl.jpeg");
					HttpImage.saveBitmap(bitmap,file);
					Picasso.with(context)
							.load(file)
							.into(jcVideoPlayerStandard.thumbImageView);
				}
			}
		}
	};
	private void setPJ(RadioGroup radioGroup, RadioButton radioButton1,
					   RadioButton radioButton2, RadioButton radioButton3,
					   RadioButton radioButton4, int quality, final int id) {
		radioGroup.setVisibility(View.VISIBLE);
		switch (quality) {
			case 1:
				radioButton1.setChecked(true);
				break;
			case 2:
				radioButton2.setChecked(true);
				break;
			case 3:
				radioButton3.setChecked(true);
				break;
			case 4:
				radioButton4.setChecked(true);
				break;

			default:
				break;
		}
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				int quality = 0;
				switch (arg1) {
					case R.id.you:
						quality = 1;
						break;
					case R.id.liang:
						quality = 2;
						break;
					case R.id.zhong:
						quality = 3;
						break;
					case R.id.cha:
						quality = 4;
						break;

					default:
						break;
				}
				Map<String,Object> map=new HashMap<String, Object>();
				map.put("id",
						id);
				map.put("evaluate", quality);
				Log.e("aaaaaaaa", "id="+id
						+",evaluate="+quality);
				context.doPost(Config1.getInstance().IP+"activityStudent",
						map,
						new ResultCallback() {
							@Override
							public void onFailure(Call call, IOException e) {
								handlerReq.sendEmptyMessage(2);
							}

							@Override
							public void onResponse(Call call, Response response,String json)  {
								if (response.code()==200){
									Message message=Message.obtain();
									message.what=1;
									message.obj=json;
									handlerReq.sendMessage(message);
								}
							}
						});
			}
		});
	}
	class TextViewHolder {
		TextView one_student_homework_item_awser,is_over;
		RadioGroup pj_rg;
		RadioButton you, liang, zhong, cha;
		LinearLayout one_student_homework_item_submit;
	}
	class PhotoHolder {
		LinearLayout one_student_homework_item_submit;
		TextView is_over;
		NoScrollGridView tupian;
		RadioGroup pj_rg;
		RadioButton you, liang, zhong, cha;
	}
	class RecordHolder {
		LinearLayout one_student_homework_item_submit;
		TextView is_over;
		ImageButton luyin;
		RadioGroup pj_rg;
		RadioButton you, liang, zhong, cha;
	}
	class VideoHolder {
		LinearLayout one_student_homework_item_submit;
		TextView is_over;
		JCVideoPlayerStandard jcVideoPlayer;
		RadioGroup pj_rg;
		RadioButton you, liang, zhong, cha;
	}

	@Override
	public int getItemViewType(int position) {
		if (persons.get(position).content_type==2){
			return 1;
		}else if (persons.get(position).content_type==1){
			return persons.get(position).submitType;
		}
		return 0;
	}
	@Override
	public int getViewTypeCount() {
		return 5;
	}

	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		super.notifyDataSetChanged();
	}

	private Bitmap createVideoThumbnail(String url, int width, int height) {
		Bitmap bitmap = null;
		MediaMetadataRetriever retriever = new MediaMetadataRetriever();
		int kind = MediaStore.Video.Thumbnails.MINI_KIND;
		try {
			if (Build.VERSION.SDK_INT >= 14) {
				retriever.setDataSource(url, new HashMap<String, String>());
			} else {
				retriever.setDataSource(url);
			}
			bitmap = retriever.getFrameAtTime();
		} catch (IllegalArgumentException ex) {
			// Assume this is a corrupt video file
		} catch (RuntimeException ex) {
			// Assume this is a corrupt video file.
		} finally {
			try {
				retriever.release();
			} catch (RuntimeException ex) {
				// Ignore failures while cleaning up.
			}
		}
		if (kind == MediaStore.Images.Thumbnails.MICRO_KIND && bitmap != null) {
			bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
					ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		}
		return bitmap;
	}
}
