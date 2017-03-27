package com.shengliedu.teacher.teacher.util;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MobileUtils {

	public static boolean isMobileNO(String mobiles) {
		boolean flag = false;
		try {
			Pattern p = Pattern.compile("^(13|14|15|17|18)\\d{9}$");
			Matcher m = p.matcher(mobiles);
			flag = m.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}
	
}
