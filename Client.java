package com.cWww.homework;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * �ͻ��ˣ����ڲ���
 * @author Administrator
 *
 */
public class Client {

	public static void main(String[] args) throws IOException {
		
		String urlStr = "http://192.168.46.254:8888/easyBuy/images/plmm.jpg";
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		long length = conn.getContentLengthLong();
		long size = length/3;
		File file = new File("C:\\Documents and Settings\\Administrator\\����\\java");
		/*
		 * ����4���̣߳�����3��Ϊ�����̣߳�����һ��Ϊ��ؽ����߳�
		 */
		Thread t1 = new DownloadFile(urlStr,0,size,file);
		t1.setName("�߳�1111");
		Thread t2 = new DownloadFile(urlStr,size,size*2,file);
		t2.setName("�߳�2222");
		Thread t3 = new DownloadFile(urlStr,size*2,size*3,file);
		t3.setName("�߳�3333");
		DownloadProgress dlp = new DownloadProgress(length,urlStr,file);
		Thread t4 = new Thread(dlp,"�ػ��߳�");
		t4.setDaemon(true);
		t1.start();
		t2.start();
		t3.start();
		t4.start();
	}
	
}
