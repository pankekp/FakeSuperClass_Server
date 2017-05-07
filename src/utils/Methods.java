package utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import pojo.Header;

/**
 * @author Zhao Zhixu
 */
public class Methods {
	public static String get(Header header, String url) throws Exception {
		URL u = new URL(url);
		HttpURLConnection con = (HttpURLConnection) u.openConnection();
		con.setRequestMethod("GET");
		if (header.getAccept() != null)
			con.setRequestProperty("Accept", header.getAccept());
		if (header.getCookie() != null)
			con.setRequestProperty("Cookie", header.getCookie());
		if (header.getAccept_Encoding() != null)
			con.setRequestProperty("Accept-Encoding", header.getAccept_Encoding());
		if (header.getAccept_Language() != null)
			con.setRequestProperty("Accept-Language", header.getAccept_Language());
		if (header.getCache_Control() != null)
			con.setRequestProperty("Cache-Control", header.getCache_Control());
		if (header.getConnection() != null)
			con.setRequestProperty("Connection", header.getConnection());
		if (header.getContent_Length() != null)
			con.setRequestProperty("Content-Length", header.getContent_Length());
		if (header.getContent_Type() != null)
			con.setRequestProperty("Content-Type", header.getContent_Type());
		if (header.getHost() != null)
			con.setRequestProperty("Host", header.getHost());
		if (header.getOrigin() != null)
			con.setRequestProperty("Origin", header.getOrigin());
		if (header.getReferer() != null)
			con.setRequestProperty("Referer", header.getReferer());
		if (header.getUpgrade_Insecure_Requests() != null)
			con.setRequestProperty("Upgrade-Insecure-Requests", header.getUpgrade_Insecure_Requests());
		if (header.getUser_Agent() != null)
			con.setRequestProperty("User-Agent", header.getUser_Agent());
		con.setDoInput(true);
		con.setDoOutput(true);

		BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), Charset.forName("GBK")));
		String resStr = "";
		String line = "";
		while ((line = reader.readLine()) != null) {
			resStr += line;
		}
		reader.close();

		resStr = new String(resStr.getBytes("utf-8"));
		return resStr;
	}

	public static String post(Header header, String url, String params) throws Exception {
		URL u = new URL(url);
		HttpURLConnection con = (HttpURLConnection) u.openConnection();
		con.setRequestMethod("POST");
		if (header.getAccept() != null)
			con.setRequestProperty("Accept", header.getAccept());
		if (header.getCookie() != null)
			con.setRequestProperty("Cookie", header.getCookie());
		if (header.getAccept_Encoding() != null)
			con.setRequestProperty("Accept-Encoding", header.getAccept_Encoding());
		if (header.getAccept_Language() != null)
			con.setRequestProperty("Accept-Language", header.getAccept_Language());
		if (header.getCache_Control() != null)
			con.setRequestProperty("Cache-Control", header.getCache_Control());
		if (header.getConnection() != null)
			con.setRequestProperty("Connection", header.getConnection());
		if (header.getContent_Length() != null)
			con.setRequestProperty("Content-Length", header.getContent_Length());
		if (header.getContent_Type() != null)
			con.setRequestProperty("Content-Type", header.getContent_Type());
		if (header.getHost() != null)
			con.setRequestProperty("Host", header.getHost());
		if (header.getOrigin() != null)
			con.setRequestProperty("Origin", header.getOrigin());
		if (header.getReferer() != null)
			con.setRequestProperty("Referer", header.getReferer());
		if (header.getUpgrade_Insecure_Requests() != null)
			con.setRequestProperty("Upgrade-Insecure-Requests", header.getUpgrade_Insecure_Requests());
		if (header.getUser_Agent() != null)
			con.setRequestProperty("User-Agent", header.getUser_Agent());
		con.setDoInput(true);
		con.setDoOutput(true);

		OutputStream out = con.getOutputStream();
		out.write(params.getBytes());
		out.close();

		BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), Charset.forName("GBK")));
		String resStr = "";
		String line = "";
		while ((line = reader.readLine()) != null) {
			resStr += line;
		}
		reader.close();

		resStr = new String(resStr.getBytes("utf-8"));
		return resStr;
	}
}
