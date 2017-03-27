package com.shengliedu.teacher.teacher.util;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import android.app.ProgressDialog;
import android.util.Log;

public class UploadUtil {
	private static final String TAG = "uploadFile";

	private static final int TIME_OUT = 10 * 60 * 1000; // 超时时间

	private static final String CHARSET = "utf-8"; // 设置编码

	/**
	 * 
	 * 上传文件到服务器
	 * 
	 * @param file
	 *            需要上传的文件
	 * 
	 * @param RequestURL
	 *            请求的rul
	 * 
	 * @return 返回响应的内容
	 */

	public String uploadFile(File file, String RequestURL, String activityId,
			String userId) {

		int res = 0;

		String result = "";

		String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成

		String PREFIX = "--", LINE_END = "\r\n";

		String CONTENT_TYPE = "multipart/form-data"; // 内容类型

		try {

			URL url = new URL(RequestURL);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setReadTimeout(TIME_OUT);

			conn.setConnectTimeout(TIME_OUT);

			conn.setDoInput(true); // 允许输入流

			conn.setDoOutput(true); // 允许输出流

			conn.setUseCaches(false); // 不允许使用缓存

			conn.setRequestMethod("POST"); // 请求方式

			conn.setRequestProperty("Charset", CHARSET); // 设置编码

			conn.setRequestProperty("connection", "keep-alive");

			conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="
					+ BOUNDARY);

			if (file != null) {

				/**
				 * 
				 * 当文件不为空时执行上传
				 */

				DataOutputStream dos = new DataOutputStream(
						conn.getOutputStream());

				StringBuffer sb = new StringBuffer();

				sb.append(PREFIX);

				sb.append(BOUNDARY);

				sb.append(LINE_END);

				/**
				 * 
				 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
				 * 
				 * filename是文件的名字，包含后缀名
				 */
				String content = PREFIX + BOUNDARY + LINE_END
						+ "Content-Disposition: form-data; name=\"savePath\";"
						+ LINE_END + LINE_END + "activity/" + activityId + "/"
						+ userId + LINE_END;

				sb.append("Content-Disposition: form-data; name=\"file\"; filename=\""
						+ file.getName() + "\"" + LINE_END);

				sb.append("Content-Type: application/octet-stream; charset="
						+ CHARSET + LINE_END);

				sb.append(LINE_END);

				dos.write(content.getBytes());
				dos.write(sb.toString().getBytes());

				Log.e("%%%%%%%%%%%%%%%%%%", RequestURL);
				Log.e("******************", content);
				Log.e("##################", sb.toString());
				Log.e("-------------", "activityId=" + activityId + "userId="
						+ userId);
				if (HttpImage.isImage3(file)) {
					HttpImage.compressPicture(file.getAbsolutePath(),
							file.getAbsolutePath());
				}
				InputStream is = new FileInputStream(file);

				byte[] bytes = new byte[1024];

				int len = 0;

				while ((len = is.read(bytes)) != -1) {
					dos.write(bytes, 0, len);
				}
				is.close();
				dos.write(LINE_END.getBytes());

				byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)
						.getBytes();

				dos.write(end_data);
				dos.flush();

				/**
				 * 
				 * 获取响应码 200=成功 当响应成功，获取响应的流
				 */

				res = conn.getResponseCode();
				// Log.e(TAG, "response code:" + res);

				if (res == 200) {
					Log.e(TAG, "request success");
					InputStream input = conn.getInputStream();
					StringBuffer sb1 = new StringBuffer();
					int ss;
					while ((ss = input.read()) != -1) {
						sb1.append((char) ss);
					}
					result = sb1.toString();
					Log.e(TAG, "result : " + result);
				} else {
					Log.e(TAG, "request error");
				}

			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 
	 * 上传文件到服务器
	 * 
	 * @param file
	 *            需要上传的文件
	 * 
	 * @param RequestURL
	 *            请求的rul
	 * 
	 * @return 返回响应的内容
	 */

	public String uploadFile2(File file, String RequestURL, String userId,
			String typeId, ProgressDialog pd) {

		int res = 0;

		String result = "";

		String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成

		String PREFIX = "--", LINE_END = "\r\n";

		String CONTENT_TYPE = "multipart/form-data"; // 内容类型

		try {

			URL url = new URL(RequestURL);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setReadTimeout(TIME_OUT);

			conn.setConnectTimeout(TIME_OUT);

			conn.setDoInput(true); // 允许输入流

			conn.setDoOutput(true); // 允许输出流

			conn.setUseCaches(false); // 不允许使用缓存

			conn.setRequestMethod("POST"); // 请求方式

			conn.setRequestProperty("Charset", CHARSET); // 设置编码

			conn.setRequestProperty("connection", "keep-alive");

			// if (file != null) {
			// conn.setChunkedStreamingMode(Integer.parseInt(file.length()+""));
			// }

			conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="
					+ BOUNDARY);

			if (file != null) {

				/**
				 * 
				 * 当文件不为空时执行上传
				 */

				DataOutputStream dos = new DataOutputStream(
						conn.getOutputStream());
				StringBuffer sb = new StringBuffer();

				sb.append(PREFIX);

				sb.append(BOUNDARY);

				sb.append(LINE_END);

				/**
				 * 
				 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
				 * 
				 * filename是文件的名字，包含后缀名
				 */

				String content = PREFIX + BOUNDARY + LINE_END
						+ "Content-Disposition: form-data; name=\"savePath\";"
						+ LINE_END + LINE_END + "personalFile/" + userId + "/"
						+ typeId + LINE_END;

				String content1 = PREFIX
						+ BOUNDARY
						+ LINE_END
						+ "Content-Disposition: form-data; name=\"personalFile\";"
						+ LINE_END + LINE_END + "1" + LINE_END;
				sb.append("Content-Disposition: form-data; name=\"file\"; filename=\""
						+ file.getName() + "\"" + LINE_END);

				sb.append("Content-Type: application/octet-stream; charset="
						+ CHARSET + LINE_END);

				sb.append(LINE_END);

				dos.write(content.getBytes("UTF-8"));
				dos.write(content1.getBytes("UTF-8"));
				dos.write(sb.toString().getBytes("UTF-8"));
				Log.e("%%%%%%%%%%%%%%%%%%", RequestURL);
				Log.e("******************", content);
				Log.e("__________________", content1);
				Log.e("##################", sb.toString());
				Log.e("-------------", "userId=" + userId + "typeId=" + typeId);
				if (HttpImage.isImage3(file)) {
					HttpImage.compressPicture(file.getAbsolutePath(),
							file.getAbsolutePath());
				}
				InputStream is = new FileInputStream(file);

				byte[] bytes = new byte[1024];
				int len = 0;
				int total = 0;
				while ((len = is.read(bytes)) != -1) {
					dos.write(bytes, 0, len);
					total += len;
					if (pd != null) {
						pd.setProgress(total);
					}
				}
				is.close();
				dos.write(LINE_END.getBytes());

				byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)
						.getBytes();

				dos.write(end_data);
				dos.flush();

				/**
				 * 
				 * 获取响应码 200=成功 当响应成功，获取响应的流
				 */

				res = conn.getResponseCode();
				// Log.e(TAG, "response code:" + res);
				Log.e(TAG, "ResponseCode：" + res);
				if (res == 200) {
					Log.e(TAG, "request success");
					InputStream input = conn.getInputStream();
					StringBuffer sb1 = new StringBuffer();
					int ss;
					while ((ss = input.read()) != -1) {
						sb1.append((char) ss);
					}
					result = sb1.toString();
					result = UnicodeConverter.unicode2String(result);
					Log.e(TAG, "result : " + result);
				} else {
					Log.e(TAG, "request error");
				}

			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 
	 * 上传文件到服务器
	 * 
	 * @param file
	 *            需要上传的文件
	 * 
	 * @param RequestURL
	 *            请求的rul
	 * 
	 * @return 返回响应的内容
	 */

	public String uploadHead(File file, String RequestURL) {

		int res = 0;

		String result = "";

		String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成

		String PREFIX = "--", LINE_END = "\r\n";

		String CONTENT_TYPE = "multipart/form-data"; // 内容类型

		try {

			URL url = new URL(RequestURL);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setReadTimeout(TIME_OUT);

			conn.setConnectTimeout(TIME_OUT);

			conn.setDoInput(true); // 允许输入流

			conn.setDoOutput(true); // 允许输出流

			conn.setUseCaches(false); // 不允许使用缓存

			conn.setRequestMethod("POST"); // 请求方式

			conn.setRequestProperty("Charset", CHARSET); // 设置编码

			conn.setRequestProperty("connection", "keep-alive");

			conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="
					+ BOUNDARY);

			if (file != null) {

				/**
				 * 
				 * 当文件不为空时执行上传
				 */

				DataOutputStream dos = new DataOutputStream(
						conn.getOutputStream());

				StringBuffer sb = new StringBuffer();

				sb.append(PREFIX);

				sb.append(BOUNDARY);

				sb.append(LINE_END);

				/**
				 * 
				 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
				 * 
				 * filename是文件的名字，包含后缀名
				 */
				String content = PREFIX + BOUNDARY + LINE_END
						+ "Content-Disposition: form-data; name=\"savePath\";"
						+ LINE_END + LINE_END + "user/images" + LINE_END;

				sb.append("Content-Disposition: form-data; name=\"file\"; filename=\""
						+ file.getName() + "\"" + LINE_END);

				sb.append("Content-Type: application/octet-stream; charset="
						+ CHARSET + LINE_END);

				sb.append(LINE_END);

				dos.write(content.getBytes());
				dos.write(sb.toString().getBytes());

				Log.e("%%%%%%%%%%%%%%%%%%", RequestURL);
				Log.e("******************", content);
				Log.e("##################", sb.toString());
				if (HttpImage.isImage3(file)) {
					HttpImage.compressPicture(file.getAbsolutePath(),
							file.getAbsolutePath());
				}
				InputStream is = new FileInputStream(file);

				byte[] bytes = new byte[1024];

				int len = 0;

				while ((len = is.read(bytes)) != -1) {

					dos.write(bytes, 0, len);

				}
				is.close();
				dos.write(LINE_END.getBytes());

				byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)
						.getBytes();

				dos.write(end_data);
				dos.flush();

				/**
				 * 
				 * 获取响应码 200=成功 当响应成功，获取响应的流
				 */

				res = conn.getResponseCode();
				// Log.e(TAG, "response code:" + res);

				if (res == 200) {
					// Log.e(TAG, "request success");
					InputStream input = conn.getInputStream();
					StringBuffer sb1 = new StringBuffer();
					int ss;
					while ((ss = input.read()) != -1) {
						sb1.append((char) ss);
					}
					result = sb1.toString();
					// Log.e(TAG, "result : " + result);
				} else {
					// Log.e(TAG, "request error");
				}

			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

}