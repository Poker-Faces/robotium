package com.sunnymum.utils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * 建立socket，发送内容
 * 
 * @author gds_lucky
 * 
 */
public class Send {

	/**
	 * 
	 * @param ip 发送邮件机器方的ip地址
	 * @param sort 端口，与监控端保持一致
	 * @param content 发送的内容
	 */
	public static void sendContent(String ip, int sort, String content) {
		try {
			Socket socket = new Socket(ip, sort);
			DataOutputStream dos = new DataOutputStream(
					socket.getOutputStream());
			dos.writeUTF(content);
			dos.flush();
			dos.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
