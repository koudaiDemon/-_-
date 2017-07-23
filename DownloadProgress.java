package com.cWww.homework;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;

/**
 * ����̣߳��ػ��̣߳����ڼ���ļ�������
 * @author Administrator
 *
 */
public class DownloadProgress implements Runnable{

	//�ܹ���Ҫ���صĴ�С
	private long totalLen;
	//���ص�·��(���ڻ�ȡ�ļ�����)
	private String urlStr;
	//��ǰ����λ��
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
			//����BigDecimal�еķ���������Ľ�����и�ʽ��
			double temp = current.divide(total,10,RoundingMode.HALF_DOWN).doubleValue();
			System.out.println("��ǰ��������:"+nf.format(temp));
			try {
				Thread.sleep(300);//�����ü�ؽ��ȱ���һ��
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("������ɣ���");
	}

}
