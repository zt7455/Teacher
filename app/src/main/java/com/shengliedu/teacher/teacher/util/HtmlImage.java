package com.shengliedu.teacher.teacher.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.text.Html.ImageGetter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.shengliedu.teacher.teacher.R;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlImage {
	 public static ImageGetter imgGetter = new ImageGetter() {
	        public Drawable getDrawable(String source) {  
	            Log.i("RG", "source---?>>>" + source);  
	            Drawable drawable = null;  
	            URL url;  
	            try {  
	                url = new URL(source);  
	                Log.i("RG", "url---?>>>" + url);  
	                drawable = Drawable.createFromStream(url.openStream(), ""); // 获取网路图片  
	            } catch (Exception e) {  
	                e.printStackTrace();  
	                return null;  
	            }  
	            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),  
	                    drawable.getIntrinsicHeight());  
	            Log.i("RG", "url---?>>>" + url);  
	            return drawable;  
	        }  
	    };  
	    
	    public static List<String> getImgSrc(String content){
	         
	        List<String> list = new ArrayList<String>();
	        //目前img标签标示有3种表达式
	        //<img alt="" src="1.jpg"/>   <img alt="" src="1.jpg"></img>     <img alt="" src="1.jpg">
	        //开始匹配content中的<img />标签
	        Pattern p_img = Pattern.compile("<(img|IMG)(.*?)(/>|></img>|>)");
	        Matcher m_img = p_img.matcher(content);
	        boolean result_img = m_img.find();
	        if (result_img) {
	            while (result_img) {
	                //获取到匹配的<img />标签中的内容
	                String str_img = m_img.group(2);
	                 
	                //开始匹配<img />标签中的src
	                Pattern p_src = Pattern.compile("(src|SRC)=(\"|\')(.*?)(\"|\')");
	                Matcher m_src = p_src.matcher(str_img);
	                if (m_src.find()) {
	                    String str_src = m_src.group(3);
	                    list.add(str_src);
	                }
	                //结束匹配<img />标签中的src
	                 
	                //匹配content中是否存在下一个<img />标签，有则继续以上步骤匹配<img />标签中的src
	                result_img = m_img.find();
	            }
	        }
	        return list;
	    }
	    public static String deleteSrc(String content){
	    	
	    	List<String> list = new ArrayList<String>();
	    	//目前img标签标示有3种表达式
	    	//<img alt="" src="1.jpg"/>   <img alt="" src="1.jpg"></img>     <img alt="" src="1.jpg">
	    	//开始匹配content中的<img />标签
	    	Pattern p_img = Pattern.compile("<(img|IMG)(.*?)(/>|></img>|>)");
	    	Matcher m_img = p_img.matcher(content);
	    	boolean result_img = m_img.find();
	    	if (result_img) {
	    		while (result_img) {
	    			//获取到匹配的<img />标签中的内容
	    			String str_img = m_img.group(0);
	    			Log.v("TAG", "aaa="+str_img);
	    			content=content.replace(str_img, "");
	    			//开始匹配<img />标签中的src
//	    			Pattern p_src = Pattern.compile("(src|SRC)=(\"|\')(.*?)(\"|\')");
//	    			Matcher m_src = p_src.matcher(str_img);
//	    			if (m_src.find()) {
//	    				String str_src = m_src.group(3);
//	    				list.add(str_src);
//	    			}
	    			//结束匹配<img />标签中的src
	    			
	    			//匹配content中是否存在下一个<img />标签，有则继续以上步骤匹配<img />标签中的src
	    			result_img = m_img.find();
	    		}
	    	}
	    	return content;
	    }
	    
		private Dialog d;

		@SuppressLint("InflateParams")
		public void showDialog(Activity context,final String path1) {
			// TODO Auto-generated method stub
			d = null;
			if (null == d) {
				View v = LayoutInflater.from(context).inflate(
						R.layout.see_photo1, null);
				d = new Dialog(context, R.style.dialogss);
				d.addContentView(v, new LayoutParams(context
						.getWindowManager().getDefaultDisplay().getWidth(),
						context.getWindowManager()
								.getDefaultDisplay().getHeight()));
				ImageView iv = (ImageView) v.findViewById(R.id.see_photoview);
				Button btn_delete_photo = (Button) v
						.findViewById(R.id.btn_delete_photo);
				ImageLoader imageLoader=ImageLoader.getInstance();
				imageLoader.displayImage(path1, iv);
//				BitmapUtils utils = new BitmapUtils(context);
//				utils.display(iv, path1);
				d.show();
				btn_delete_photo.setVisibility(View.INVISIBLE);
				iv.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						d.dismiss();
					}
				});
			}

		}
		
}
