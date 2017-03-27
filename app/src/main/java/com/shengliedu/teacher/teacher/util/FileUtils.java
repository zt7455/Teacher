package com.shengliedu.teacher.teacher.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {

	public static String SDPATH = Environment.getExternalStorageDirectory()
			+ "/formats/";
	public static String SDPATH1 = Environment.getExternalStorageDirectory()
			+ "/myimages/";

	public static void saveBitmap(Bitmap bm, String picName) {
		Log.e("", "保存图片");
		try {
			if (!isFileExist("")) {
				File tempf = createSDDir("");
			}
			File f = new File(SDPATH, picName + ".JPEG");
			if (f.exists()) {
				f.delete();
			}
			FileOutputStream out = new FileOutputStream(f);
			bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.flush();
			out.close();
			Log.e("", "已经保存");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static File createSDDir(String dirName) throws IOException {
		File dir = new File(SDPATH + dirName);
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {

			System.out.println("createSDDir:" + dir.getAbsolutePath());
			System.out.println("createSDDir:" + dir.mkdir());
		}
		return dir;
	}

	public static boolean isFileExist(String fileName) {
		File file = new File(SDPATH + fileName);
		file.isFile();
		return file.exists();
	}

	public static void delFile(String fileName) {
		File file = new File(SDPATH + fileName);
		if (file.isFile()) {
			file.delete();
		}
		file.exists();
	}

	public static void deleteDir(String path) {
		File dir = new File(path);
		if (dir == null || !dir.exists() || !dir.isDirectory())
			return;

		for (File file : dir.listFiles()) {
			if (file.isFile())
				file.delete(); // 删除所有文件
			else if (file.isDirectory())
				deleteDir(path); // 递规的方式删除文件夹
		}
		dir.delete();// 删除目录本身
	}

	public static boolean fileIsExists(String path) {
		try {
			File f = new File(path);
			if (!f.exists()) {
				return false;
			}
		} catch (Exception e) {

			return false;
		}
		return true;
	}

	public static String getPath(Activity context, Uri uri) {
		Log.v("TAG", "Scheme:" + uri.getScheme());
		if ("content".equalsIgnoreCase(uri.getScheme())) {
			String[] projection = { MediaStore.Images.Media.DATA };
			Cursor cursor = null;

			try {
				cursor = context.getContentResolver().query(uri, projection,
						null, null, null);
				Log.v("TAG", "cursor:" + (cursor == null));
				int column_index = cursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				Log.v("TAG", "column_index:" + column_index);
				cursor.moveToFirst();
				// if (cursor.moveToFirst()) {
				String a = cursor.getString(column_index);
				Log.v("TAG", "a:" + a);
				return a;
				// }
			} catch (Exception e) {
				// Eat it
				e.printStackTrace();
			}
		}

		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}
	
	/**
	 * 复制单个文件
	 * @param oldPath String 原文件路径 如：c:/fqf.txt
	 * @param newPath String 复制后路径 如：f:/fqf.txt
	 * @return boolean
	 */
	public static void copyFile(String oldPath, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { //文件存在时
				InputStream inStream = new FileInputStream(oldPath); //读入原文件
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				int length;
				while ( (byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; //字节数 文件大小
					System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		}
		catch (Exception e) {
			System.out.println("复制单个文件操作出错");
			e.printStackTrace();

		}

	}

	/**
	 * 复制整个文件夹内容
	 * @param oldPath String 原文件路径 如：c:/fqf
	 * @param newPath String 复制后路径 如：f:/fqf/ff
	 * @return boolean
	 */
	public static void copyFolder(String oldPath, String newPath) {

		try {
			(new File(newPath)).mkdirs(); //如果文件夹不存在 则建立新文件夹
			File a=new File(oldPath);
			String[] file=a.list();
			File temp=null;
			for (int i = 0; i < file.length; i++) {
				if(oldPath.endsWith(File.separator)){
					temp=new File(oldPath+file[i]);
				}
				else{
					temp=new File(oldPath+File.separator+file[i]);
				}

				if(temp.isFile()){
					FileInputStream input = new FileInputStream(temp);
					FileOutputStream output = new FileOutputStream(newPath + "/" +
							(temp.getName()).toString());
					byte[] b = new byte[1024 * 5];
					int len;
					while ( (len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
				}
				if(temp.isDirectory()){//如果是子文件夹
					copyFolder(oldPath+"/"+file[i],newPath+"/"+file[i]);
				}
			}
		}
		catch (Exception e) {
			System.out.println("复制整个文件夹内容操作出错");
			e.printStackTrace();

		}

	}
	/** 
	 * 根据Uri获取图片绝对路径，解决Android4.4以上版本Uri转换 
	 * @params activity
	 * @params imageUri
	 * @author yaoxing 
	 * @date 2014-10-12 
	 */  
	@TargetApi(19)  
	public static String getImageAbsolutePath(Activity context, Uri imageUri) {  
	    if (context == null || imageUri == null)  
	        return null;  
	    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {  
	        if (isExternalStorageDocument(imageUri)) {  
	            String docId = DocumentsContract.getDocumentId(imageUri);  
	            String[] split = docId.split(":");  
	            String type = split[0];  
	            if ("primary".equalsIgnoreCase(type)) {  
	                return Environment.getExternalStorageDirectory() + "/" + split[1];  
	            }  
	        } else if (isDownloadsDocument(imageUri)) {  
	            String id = DocumentsContract.getDocumentId(imageUri);  
	            Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));  
	            return getDataColumn(context, contentUri, null, null);  
	        } else if (isMediaDocument(imageUri)) {  
	            String docId = DocumentsContract.getDocumentId(imageUri);  
	            String[] split = docId.split(":");  
	            String type = split[0];  
	            Uri contentUri = null;  
	            if ("image".equals(type)) {  
	                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;  
	            } else if ("video".equals(type)) {  
	                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;  
	            } else if ("audio".equals(type)) {  
	                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;  
	            }  
	            String selection = MediaStore.Images.Media._ID + "=?";  
	            String[] selectionArgs = new String[] { split[1] };  
	            return getDataColumn(context, contentUri, selection, selectionArgs);  
	        }  
	    } // MediaStore (and general)  
	    else if ("content".equalsIgnoreCase(imageUri.getScheme())) {  
	        // Return the remote address  
	        if (isGooglePhotosUri(imageUri))  
	            return imageUri.getLastPathSegment();  
	        return getDataColumn(context, imageUri, null, null);  
	    }  
	    // File  
	    else if ("file".equalsIgnoreCase(imageUri.getScheme())) {  
	        return imageUri.getPath();  
	    }  
	    return null;  
	}  
	  
	public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {  
	    Cursor cursor = null;  
	    String column = MediaStore.Images.Media.DATA;  
	    String[] projection = { column };  
	    try {  
	        cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);  
	        if (cursor != null && cursor.moveToFirst()) {  
	            int index = cursor.getColumnIndexOrThrow(column);  
	            return cursor.getString(index);  
	        }  
	    } finally {  
	        if (cursor != null)  
	            cursor.close();  
	    }  
	    return null;  
	}  
	  
	/** 
	 * @param uri The Uri to check. 
	 * @return Whether the Uri authority is ExternalStorageProvider. 
	 */  
	public static boolean isExternalStorageDocument(Uri uri) {  
	    return "com.android.externalstorage.documents".equals(uri.getAuthority());  
	}  
	  
	/** 
	 * @param uri The Uri to check. 
	 * @return Whether the Uri authority is DownloadsProvider. 
	 */  
	public static boolean isDownloadsDocument(Uri uri) {  
	    return "com.android.providers.downloads.documents".equals(uri.getAuthority());  
	}  
	  
	/** 
	 * @param uri The Uri to check. 
	 * @return Whether the Uri authority is MediaProvider. 
	 */  
	public static boolean isMediaDocument(Uri uri) {  
	    return "com.android.providers.media.documents".equals(uri.getAuthority());  
	}  
	  
	/** 
	 * @param uri The Uri to check. 
	 * @return Whether the Uri authority is Google Photos. 
	 */  
	public static boolean isGooglePhotosUri(Uri uri) {  
	    return "com.google.android.apps.photos.content".equals(uri.getAuthority());  
	}  
}
