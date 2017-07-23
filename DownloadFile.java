package com.cWww.homework;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
/**
 * ���������õ��߳�
 * @author Administrator
 *
 */
public class DownloadFile extends Thread{
	//���ص�ַ
	private String urlStr;
	//��ʼ�����ص�
	private long startPoint;
	//���������ص�
	private long endPoint;
	//���ص����ص�Ŀ¼
	private File file;
	
	public DownloadFile(String urlStr, long startPoint, long endPoint, File file) {
		super();
		this.urlStr = urlStr;
		this.startPoint = startPoint;
		this.endPoint = endPoint;
		this.file = file;
	}

	@Override
	public void run() {
		BufferedInputStream bis = null;
		RandomAccessFile raf = null;
		RandomAccessFile raf_read = null;
		RandomAccessFile raf_write = null;
		long seekPoint = 0;
		//ÿһ���������Դ����Լ��İ����ļ�
		File f = new File(file,this.getName()+".txt");
		try {
			URL url = new URL(urlStr);
			HttpURLConnection http = (HttpURLConnection)url.openConnection();
			int code = http.getResponseCode();
			if(code == HttpURLConnection.HTTP_OK){
				//������Ӧ10s
				http.setReadTimeout(10000);
				//����Ҫ�ȴ���write����Ȼ�����read�������ļ�������쳣
				raf_write = new RandomAccessFile(f, "rw");
				raf_read = new RandomAccessFile(f, "r");
				raf_read.seek(0);
				try {
					seekPoint = raf_read.readLong(); //���ڶ��ļ��е�Ѱַλ�ý��ж�ȡ
				} catch(EOFException e){
					seekPoint = startPoint;
				}
				InputStream is = http.getInputStream();
				//����������������������а�װ��
				bis = new BufferedInputStream(is);
				raf = new RandomAccessFile(new File(file,urlStr.substring(urlStr.lastIndexOf("/"))),"rw");
				//���������������Ѱַ��
				bis.skip(seekPoint);
				raf.seek(seekPoint);
				byte[] b = new byte[1024];
				int len = 0;
				System.out.println(Thread.currentThread().getName()+"��ʼ����");
				while((len = bis.read(b)) != -1 && startPoint <= endPoint){
					startPoint += len;
					raf.write(b,0,len);
					raf_write.seek(0);
					raf_write.writeLong(startPoint);//��ʵ�ʿ������ȷ�������ļ���
					try {
						//���㿴���ļ��Ŀ������ȣ���ʵû��ʲôʵ�ʵ��ô���
						sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				System.out.println(Thread.currentThread().getName()+"��ɿ���");
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}  catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				if(raf!=null){raf.close();}
				if(bis!=null){bis.close();}
				if(raf_read!=null){raf_read.close();}
				if(raf_write!=null){raf_write.close();}
				f.delete();//�����ر������Ժ�Դ������ļ�����ɾ��
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
