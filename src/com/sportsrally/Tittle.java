package com.sportsrally;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class Tittle extends Activity {
	TextView hellotxv;
	ImageView tittle;
	Timer timer = new Timer();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tittle);
		tittle = (ImageView)findViewById(R.id.tittle);
		hellotxv = (TextView)findViewById(R.id.hellotxv);
		timer.schedule(task, 7000);
		timer.schedule(task2, 5000);
		
		AlphaAnimation aa = new AlphaAnimation(0.1f, 1.0f);
		aa.setDuration(6000);
		aa.setRepeatCount(0);
		tittle.startAnimation(aa);
	}
	
	
	
	
	Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				Intent intent = new Intent();
				intent.setClass(Tittle.this, Menu.class);
				startActivity(intent);
				break;
			case 2:
				hellotxv.setVisibility(View.VISIBLE);
				break;
			}
			super.handleMessage(msg);
		}

	};
	TimerTask task = new TimerTask() {

		public void run() {
			Message message = new Message();
			message.what = 1;
			handler.sendMessage(message);
		}

	};
	TimerTask task2 = new TimerTask() {

		public void run() {
			Message message = new Message();
			message.what = 2;
			handler.sendMessage(message);
		}

	};

		
	public void entering(View v){
		Intent i = new Intent(Tittle.this,Menu.class);
		startActivity(i);
	}
	
}
