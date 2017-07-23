package com.cWww.homework;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;

/**
 * 监控线程（守护线程）用于监控文件的下载
 * @author Administrator
 *
 */
public class DownloadProgress implements Runnable{

	//总共需要下载的大小
	private long totalLen;
	//下载的路径(用于获取文件名称)
	private String urlStr;
	//当前下载位置
	private File file;
	
	public DownloadProgress(long totalLen, String urlStr, File file) {
		super();
		this.totalLen = totalLen;
		this.urlStr = urlStr;
		this.file = file;
	}

	@Override
	public void run() {
		File f = new File(file,urlStr.substring(urlStr.lastIndexOf("/")));
		NumberFormat nf = NumberFormat.getPercentInstance();
		long currentLen = 0;
		while(currentLen<totalLen){
			currentLen = f.length();
			BigDecimal current = BigDecimal.valueOf(currentLen);
			BigDecimal total = BigDecimal.valueOf(totalLen);
			//利用BigDecimal中的方法对相除的结果进行格式化
			double temp = current.divide(total,10,RoundingMode.HALF_DOWN).doubleValue();
			System.out.println("当前拷贝进度:"+nf.format(temp));
			try {
				Thread.sleep(300);//用于让监控进度变慢一点
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("拷贝完成！！");
	}

}
