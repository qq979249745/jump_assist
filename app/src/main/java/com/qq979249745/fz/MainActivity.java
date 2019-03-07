package com.qq979249745.fz;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;

public class MainActivity extends Activity 
{
	Button bt;
	Intent i;
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		
        setContentView(R.layout.main);
		this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		
		bt=(Button) findViewById(R.id.bt);
		bt.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				if(i==null){
					i=new Intent(MainActivity.this,MyService.class);
					
					startService(i);
					bt.setText("关闭辅助");
				}else{
					stopService(i);
					i=null;
					bt.setText("打开辅助");
				}
			}
		});
		
    }
}
