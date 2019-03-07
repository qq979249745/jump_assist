package com.qq979249745.fz;
import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.view.WindowManager.*;
import android.widget.*;
import java.io.*;

import java.lang.Process;

public class MyService extends Service
{
	private WindowManager.LayoutParams 布局参数对象;
	private WindowManager wm;
	private TextView 文本;
	private ImageView iv;
	private Canvas canvas;
	private Paint p;
	private Bitmap bm;
	private Process process;
	
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		布局参数对象=new WindowManager.LayoutParams();
		wm=(WindowManager) getApplication().getSystemService(getApplication().WINDOW_SERVICE);
		
		//设置窗口类型
		布局参数对象.type=LayoutParams.TYPE_PHONE;
		//布局参数对象.gravity=Gravity.LEFT|Gravity.BOTTOM;
	
		布局参数对象.width=LayoutParams.WRAP_CONTENT;
		布局参数对象.height=LayoutParams.WRAP_CONTENT;
		布局参数对象.format=PixelFormat.RGBA_8888;
		布局参数对象.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		文本=new TextView(getApplicationContext());
		文本.setText("点我");
		文本.setTextSize(20);
		文本.setTextColor(Color.BLUE);
		
		bm=Bitmap.createBitmap(wm.getDefaultDisplay().getWidth(),wm.getDefaultDisplay().getHeight(),Bitmap.Config.ARGB_8888);
		canvas=new Canvas(bm);
		p=new Paint();
		p.setColor(Color.RED);
		p.setStrokeWidth(5.0f);
		p.setTextSize(60);
		p.setTextAlign(Paint.Align.CENTER);
		
		iv=new ImageView(getApplicationContext());
		
		文本.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				文本.setText(" ");
				布局参数对象.width=LayoutParams.MATCH_PARENT;
				布局参数对象.height=LayoutParams.MATCH_PARENT;
				canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
				wm.addView(iv,布局参数对象);
				
				
			}
		});

		iv.setOnTouchListener(new OnTouchListener(){
				float x,y;
				double 距离;
				@Override
				public boolean onTouch(View p1, MotionEvent p2)
				{
					switch(p2.getAction()){
						case MotionEvent.ACTION_DOWN:
							
							x=p2.getX();
							y=p2.getY();
						
							//canvas.drawText("("+x+","+y+")",x,y-250,p);
							
							break;
						case MotionEvent.ACTION_MOVE:
							canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
							canvas.drawText("("+x+","+y+")",x,y-250,p);
							canvas.drawLine(x,y,p2.getX(),p2.getY(),p);
							canvas.drawText("("+p2.getX()+","+p2.getY()+")",p2.getX(),p2.getY()-250,p);
							//startx=p2.getX();
							//starty=p2.getY();
							
							iv.setImageBitmap(bm);
							
							break;
						case MotionEvent.ACTION_UP:
							距离=Math.sqrt(Math.pow(x-p2.getX(),2)+Math.pow(y-p2.getY(),2));
							布局参数对象.width=LayoutParams.WRAP_CONTENT;
							布局参数对象.height=LayoutParams.WRAP_CONTENT;
							wm.removeView(iv);
							
							Toast.makeText(getApplicationContext(),"距离为"+距离,Toast.LENGTH_SHORT).show();
							int 长按时间=(int)(距离*1.4);
							文本.setText("点我");
							runShell("input swipe 123 456 123 456 "+长按时间);
							//runShell("input tap 540 1660");
//							new Handler().postDelayed(new Runnable(){
//									@Override
//									public void run()
//									{
//										// TODO: Implement this method
//										布局参数对象.width=LayoutParams.MATCH_PARENT;
//										布局参数对象.height=LayoutParams.MATCH_PARENT;
//										canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
//										wm.addView(iv,布局参数对象);
//										
//									}
//							},长按时间);
							
							break;
					}

					
					return false;
				}
			});
		文本.setOnTouchListener(new OnTouchListener(){

				float 按下的x,按下的y;
				int x,y;
				@Override
				public boolean onTouch(View p1, MotionEvent event)
				{
					switch(event.getAction()){
						case MotionEvent.ACTION_DOWN:
							按下的x=event.getRawX();
							按下的y=event.getRawY();
							x=布局参数对象.x;
							y=布局参数对象.y;
						break;
						case MotionEvent.ACTION_MOVE:
							布局参数对象.x=x+(int)(event.getRawX()-按下的x);
							布局参数对象.y=y+(int)(event.getRawY()-按下的y);
							//文本.setText(event.getRawX()+"\n"+event.getRawY());
							wm.updateViewLayout(文本,布局参数对象);
						break;
						
					}
					return false;
				}
			});
		wm.addView(文本,布局参数对象);
		
	}
	
	@Override
	public IBinder onBind(Intent p1)
	{
		// TODO: Implement this method
		return null;
	}

	@Override
	public void onDestroy()
	{
		// TODO: Implement this method
		super.onDestroy();
		wm.removeView(文本);
	}
	
	
	public void runShell(String 指令){
		try{
				// 获取输出流  
			process = Runtime.getRuntime().exec("su");
				OutputStream outputStream = process.getOutputStream();  
				DataOutputStream dataOutputStream = new DataOutputStream(  
					outputStream);  
				dataOutputStream.writeBytes(指令);  
				dataOutputStream.flush();  
				dataOutputStream.close();  
				outputStream.close();
		}catch (IOException e){
		}
	}
}
