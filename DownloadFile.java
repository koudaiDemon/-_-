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
 * 用于下载用的线程
 * @author Administrator
 *
 */
public class DownloadFile extends Thread{
	//下载地址
	private String urlStr;
	//开始的下载点
	private long startPoint;
	//结束的下载点
	private long endPoint;
	//下载到本地的目录
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
		//每一个流都可以创建自己的伴随文件
		File f = new File(file,this.getName()+".txt");
		try {
			URL url = new URL(urlStr);
			HttpURLConnection http = (HttpURLConnection)url.openConnection();
			int code = http.getResponseCode();
			if(code == HttpURLConnection.HTTP_OK){
				//网络响应10s
				http.setReadTimeout(10000);
				//必须要先创建write，不然后面的read读不到文件会出现异常
				raf_write = new RandomAccessFile(f, "rw");
				raf_read = new RandomAccessFile(f, "r");
				raf_read.seek(0);
				try {
					seekPoint = raf_read.readLong(); //用于对文件中的寻址位置进行读取
				} catch(EOFException e){
					seekPoint = startPoint;
				}
				InputStream is = http.getInputStream();
				//创建输入输出流（对流进行包装）
				bis = new BufferedInputStream(is);
				raf = new RandomAccessFile(new File(file,urlStr.substring(urlStr.lastIndexOf("/"))),"rw");
				//对于输入输出流的寻址！
				bis.skip(seekPoint);
				raf.seek(seekPoint);
				byte[] b = new byte[1024];
				int len = 0;
				System.out.println(Thread.currentThread().getName()+"开始拷贝");
				while((len = bis.read(b)) != -1 && startPoint <= endPoint){
					startPoint += len;
					raf.write(b,0,len);
					raf_write.seek(0);
					raf_write.writeLong(startPoint);//将实际拷贝长度放入伴随文件中
					try {
						//方便看到文件的拷贝进度（其实没有什么实际的用处）
						sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				System.out.println(Thread.currentThread().getName()+"完成拷贝");
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
				f.delete();//当最后关闭了流以后对创建的文件进行删除
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
