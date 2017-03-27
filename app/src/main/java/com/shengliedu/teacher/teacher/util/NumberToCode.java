package com.shengliedu.teacher.teacher.util;

public class NumberToCode {
	public static String numberToCode(int i) {
		String str = "";
		switch (i) {
		case 0:
			str = "A";
			break;
		case 1:
			str = "B";
			break;
		case 2:
			str = "C";
			break;
		case 3:
			str = "D";
			break;
		case 4:
			str = "E";
			break;
		case 5:
			str = "F";
			break;
		case 6:
			str = "G";
			break;
		case 7:
			str = "H";
			break;
		case 8:
			str = "I";
			break;
		case 9:
			str = "J";
			break;
		default:
			break;
		}
		return str;
	}

	public static int codeToNumber(String str) {
		int i = -1;
		if ("A".equalsIgnoreCase(str)) {
			i = 0;
		} else if ("B".equalsIgnoreCase(str)) {
			i = 1;
		} else if ("C".equalsIgnoreCase(str)) {
			i = 2;
		} else if ("D".equalsIgnoreCase(str)) {
			i = 3;
		} else if ("E".equalsIgnoreCase(str)) {
			i = 4;
		} else if ("F".equalsIgnoreCase(str)) {
			i = 5;
		} else if ("G".equalsIgnoreCase(str)) {
			i = 6;
		} else if ("H".equalsIgnoreCase(str)) {
			i = 7;
		} else if ("I".equalsIgnoreCase(str)) {
			i = 8;
		} else if ("J".equalsIgnoreCase(str)) {
			i = 9;
		}
		return i;
	}
}
