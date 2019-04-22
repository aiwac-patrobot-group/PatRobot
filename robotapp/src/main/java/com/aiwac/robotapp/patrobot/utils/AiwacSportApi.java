package com.aiwac.robotapp.patrobot.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.print.PrinterId;
import android.util.Log;

import com.aiwac.robotapp.commonlibrary.utils.LogUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;


// 使用说明
// 第一步 AiwacSportApi aiwacSportApi = new AiwacSportApi();
// 第二步  接口使用
//		1.运动模块的接口 使用portApi.aiwacSportType(type);  其中type 不同的位会有不同的含义，具体看 aiwacSportType 的逻辑
//			注意：下发的运动指令需要在1.5s 内反复下发，否则底层会自动停止运动
//		2.投食接口 public void aiwacFeedPetType(int type) 	0 :关闭投食   1: 开启投食
//			注意：开启和关闭操作必须  成对出现；开啦就要记得关
//		3.超声波避障
//			1）先传APP 处理handler     public void setAiwacHaLMsgHandler(Handler aiwacAndroidHandler)
//				以后上传的数据都需要使用这个接口， ms.what :100 表 超声波探测的数据， ms.obj: 1,018   前方18CM 有障碍，具体看接口EXCEL
//			2）再    public void aiwacUltrasoundDetectionType(int type)  0 :关闭超声波   1: 开启超声波
//				注意：超声波模块的避障信息 会  在1S 左右上传一次




public class AiwacSportApi {

	private  boolean flag=false;
	private  Socket socketA33 = null;
	private  OutputStream os ;
	private  BufferedReader br;  //read from socket
	private  String content  = null ;

	private  String A33Ip = "127.0.0.1";
	private  int socketPort = 8989;  // A33 C的端口是8989
	private  String readContent ; //Socket read String
	private  int stateNetFlag = 0; // 0:没连上，  1：link ok
	private boolean LinkStatus = true;  // 当前连接状态,

	private Handler  writeSockethandle; // socket write

	private  Handler APPGetHaLHandle ;
	public int APPGetHaLHandleFlag = 0; // 0: 未传入；1: 赋值OK

	private boolean logable=true;

	private Handler analysisHandler  = new Handler(){
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if(msg.what == 100)
			{
				String analysisContent = msg.obj.toString();
				if(logable)
					Log.i("A33Socket", "analysisContent:"+analysisContent+"++");
				try {
					if (analysisContent.substring(0,1).equals("1") == true) // 避障信息
					{
						Log.i("A33Socket", "ffff:"+analysisContent+"++"+APPGetHaLHandleFlag);
						if (APPGetHaLHandleFlag==1) // 已经getAPP 的处理handler
						{
							Log.i("A33Socket", "aaaaaa:"+analysisContent+"++");
							Message ms = new Message();
							ms.what = 100;
							ms.obj = analysisContent.substring(2);
							APPGetHaLHandle.sendMessage(ms);
							if(logable)
								Log.i("A33Socket", "准备发往APP进行处理"+ms.obj.toString()+"++");
						}
					}
				} catch (Exception e) {
					//Log.i("A33Socket", "准备发往APP进行处理"+ms.obj.toString()+"++");
				}

			}
		}
	};

	public AiwacSportApi()
	{	if(logable)
 			Log.i("A33Socket", "启动  link  ，并检测");
		this.startAiwacSport();
		this.linsentingLinkStatus();
		this.writeSocketFromA33();
		this.readSocketFromA33();
	}

	// 传入应用程序的handler ，有消息就提醒app
	public void setAiwacHaLMsgHandler(Handler aiwacAndroidHandler)
	{
		LogUtil.d("qqqqq");
		this.APPGetHaLHandle = aiwacAndroidHandler;
		APPGetHaLHandleFlag = 1;
	}


	// 检测当前连接状态 ，否并 重连
	private void linsentingLinkStatus()
	{
		new Thread()
		{
			public void run()
			{
				try {
					sleep(1000);
					if(logable)
						Log.i("A33Socket", "开始检测  连接情况");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				while (true)
				{

					try {
						sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					try{
						socketA33.sendUrgentData(0xFF);
					}catch(Exception ex){
						//连接断开
						if(logable)
							Log.i("A33Socket", "检测到A33Socket连接已断开，尝试重连ing");
						LinkStatus = false;
						stateNetFlag = 0;
					}
					if (LinkStatus == false)   //启动重连
					{
						try {
							//socketA33.close();
							socketA33 = null;
							socketA33 = new Socket(A33Ip, socketPort);
							if (socketA33 == null)
							{
								if(logable)
									Log.i("A33Socket", " 启动重连 link error!");
								continue;
							}
							else
							{
								if(logable)
									Log.i("A33Socket","启动重连 link ok!");
								LinkStatus = true ;
							}
							os = socketA33.getOutputStream();
							br = new BufferedReader(new InputStreamReader(socketA33.getInputStream(), "utf-8"));
							stateNetFlag = 1;

						} catch (UnknownHostException e) {
							e.printStackTrace();
							stateNetFlag = 0;
						} catch (IOException e) {
							e.printStackTrace();
							stateNetFlag = 0;
						}

					}
					else
					{
						if(logable)
							Log.i("A33Socket", " 连接健在!");
					}

				}

			}
		}.start();
	}
	private void readSocketFromA33()
	{
		new Thread()
		{
			public void run() {

				while(stateNetFlag == 0)  //等待网络连接.
					;

				BufferedReader brA33 ;

				// 读取A33 C 服务发来的数据，注意数据流需要有 /r  或 /n 结尾
				while(true)
				{
					try {
						while(stateNetFlag == 0)  //等待网络连接.
							;
						if(logable)
							Log.i("A33Socket", "检测接受情况");
						if((readContent = br.readLine())!=null)
						{
							Message ms = new Message();
							ms.what = 100;
							ms.obj = readContent;
							analysisHandler.sendMessage(ms);
							if(logable)
								Log.i("A33Socket", "收到A33发来的数据"+readContent+"++");
						}

					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						stateNetFlag = 0;// 马上设置网络情况
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						stateNetFlag = 0; // 马上设置网络情况
					}
				}
			}
		}.start();

	}


	private void writeSocketFromA33()
	{
		new Thread()
		{
			public void run() {

				Looper.prepare();
				writeSockethandle = new Handler(){
					public void handleMessage(Message msg) {
						// TODO Auto-generated method stub
						if(msg.what == 333)
						{
							if (stateNetFlag == 1) // 判断为连接状态再发送
							{
								try {
									if(logable)
										Log.i("A33Socket", "发送前："+ msg.obj.toString());
									os.write((msg.obj.toString()).getBytes("utf-8"));
									os.flush();
									if(logable)
										Log.i("A33Socket", "发送后："+ msg.obj.toString());
								} catch (UnsupportedEncodingException e) {
									if(logable)
										Log.i("A33Socket", "发送失败");
									stateNetFlag = 0; // 马上设置网络情况
								} catch (IOException e) {
									if(logable)
										Log.i("A33Socket", "发送失败");
									stateNetFlag = 0; // 马上设置网络情况
								}
							}

						}
					}
				};
				Looper.loop();

			}
		}.start();

	}

	private boolean startAiwacSport()
	{

		new Thread()
		{
			public void run()
			{
//				A33Ip = "127.0.0.1";
				if(logable)
					Log.i("A33Socket", "enter run()!");
				try {
					socketA33 = null;
					socketA33 = new Socket(A33Ip, socketPort);

					if (socketA33 == null)
					{
						if(logable)
							Log.i("A33Socket", "link error!");
					}
					else
					{
						if(logable)
							Log.i("A33Socket","link ok!");
						flag=true;
					}

					os = socketA33.getOutputStream();
					br = new BufferedReader(new InputStreamReader(socketA33.getInputStream(), "utf-8"));
					stateNetFlag = 1;  // 网络设置ok

				} catch (UnknownHostException e) {
					e.printStackTrace();
					stateNetFlag = 0;
				} catch (IOException e) {
					e.printStackTrace();
					stateNetFlag = 0;
				}
			}
		}.start();

		return  flag;
	}


	private void SportClass(String sportCode)
	{
		Message ms = new Message();
		ms.what = 333;

		content = sportCode;
		if(logable)
			Log.i("A33Socket", "sportCode "+content);
		ms.obj = content;
		writeSockethandle.sendMessage(ms);
	}

	public void aiwacSportType(int type)
	{
		if ((type > 11)||(type == 3) || (type == 7))  // 入参检查
		{
			if(logable)
				Log.i("A33Socket", "aiwacSportType 参数输入错误 type："+type);
			return;
		}

		int sportTemp = 0;
		if(logable)
			Log.i("A33Socket", "type :"+type);

		//ÔË¶¯Ê¶±ð
		sportTemp = type & 15;
		if(logable)
			Log.i("A33Socket", "sportTemp:"+sportTemp);
		switch (sportTemp)
		{
			case 1:
				this.SportClass("#1a.");  // 前
				if(logable)
					Log.i("A33Socket", "SportClass:"+"a");
				break;
			case 2:
				this.SportClass("#1b.");  // 后
				if(logable)
					Log.i("A33Socket", "SportClass:"+"b");
				break;
			case 4:
				this.SportClass("#1c.");  // 左
				if(logable)
					Log.i("A33Socketg", "SportClass:"+"c");
				break;
			case 8:
				this.SportClass("#1d.");  //右
				if(logable)
					Log.i("A33Socket", "SportClass:"+"d");
				break;
			case 9:
				this.SportClass("#1e.");  // 前+右
				if(logable)
					Log.i("A33Socket", "SportClass:"+"e");
				break;
			case 5:
				this.SportClass("#1f."); // 前+左
				if(logable)
					Log.i("A33Socket", "SportClass:"+"f");
				break;
			case 10:
				this.SportClass("#1g."); //后+右
				if(logable)
					Log.i("A33Socket", "SportClass:"+"g");
				break;
			case 6:
				this.SportClass("#1h."); //后+左
				if(logable)
					Log.i("A33Socket", "h");
				break;
			case 0:
				this.SportClass("#1i."); // 停止
				if(logable)
					Log.i("A33Socket", "SportClass:"+"i");
				break;
			default:
				break;
		}




	}

	public void testAiwacStringSend(String testString)
	{
		Message ms = new Message();
		ms.what = 333;
		String content1  = null ;
		content1 = testString;
		if(logable)
			Log.i("A33Socket", "testString"+content1+"++");
		ms.obj = content1;
		writeSockethandle.sendMessage(ms);
	}


	// 投食的开关
	public void aiwacFeedPetType(int type)
	{
		String content1 ;
		if (type > 1)
		{
			if(logable)
				Log.i("A33Socket", "aiwacFeedPetType "+"错误输入 type:"+type);
			return ;
		}

		Message ms = new Message();
		ms.what = 333;

		if (type == 0)
		{
			content1 = "#4b."; // 关闭投食
		}
		else
		{
			content1 = "#4a."; // 开启投食
		}
		if(logable)
			Log.i("A33Socket", "aiwacFeedPetType "+content1+"++");
		ms.obj = content1;
		writeSockethandle.sendMessage(ms);
	}

	// 超声波探测开关
	public void aiwacUltrasoundDetectionType(int type)
	{
		String content1 ;
		if (type > 1)
		{
			if(logable)
				Log.i("A33Socket", "aiwacUltrasoundDetectionType "+"错误输入  type:"+type);
			return ;
		}

		Message ms = new Message();
		ms.what = 333;

		if (type == 0)
		{
			content1 = "#2b."; // 关闭超声波探测
		}
		else
		{
			content1 = "#2a."; // 开启超声波探测
		}
		if(logable)
			Log.i("A33Socket", "aiwacUltrasoundDetectionType "+content1+"++");
		ms.obj = content1;
		writeSockethandle.sendMessage(ms);
	}

}
