/**
 * 
 */
package com.shengliedu.teacher.teacher.chat.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.shengliedu.teacher.teacher.util.Config1;

import org.jivesoftware.smackx.packet.VCard;

/**
 * @author zt10
 * 
 */
public class User {
	public String nickname;
	public String username;
	public String truename;
	public String email;
	public String headimg;
	public String intro;
	public String mobile;
	public String sex;
	public String adr;
	public String chaticon;
	public VCard vCard;
	public Bitmap bitmap;
	public double lat = 0.0;
	public double lon = 0.0;

	public User() {
		super();
	}

	public User(final VCard vCard) {
		if (vCard != null) {
			nickname = vCard.getField("nickname");
			email = vCard.getField("email");
			intro = vCard.getField("signature");
			sex = vCard.getField("sex");
			mobile = vCard.getField("tel");
			adr = vCard.getField("hometown");
			chaticon = vCard.getField("chaticon");
			String latAndlon = vCard.getField("latAndlon");
			if (latAndlon != null && !latAndlon.equals("")) {
				String[] latAndLons = latAndlon.split(",");
				lat = Double.valueOf(latAndLons[0]);
				lon = Double.valueOf(latAndLons[1]);
			}
			this.vCard = vCard;
		}
	}

	public void showHead(Context context,ImageView imageView) {
		if (chaticon != null) {
			ImageLoader imageLoader=ImageLoader.getInstance();
			imageLoader.displayImage( Config1.IP
					+ vCard.getField("chaticon")
					.replace("\\/", "/"),imageView);
		}
	}
}
