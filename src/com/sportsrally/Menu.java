package com.sportsrally;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class Menu extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);
		Button m01 = (Button) findViewById(R.id.m01);
		Button m02 = (Button) findViewById(R.id.m02);
		Button m03 = (Button) findViewById(R.id.m03);
		Button m04 = (Button) findViewById(R.id.m04);
		Button m05 = (Button) findViewById(R.id.m05);
		m01.getBackground().setAlpha(50);
		OnClickListener listener = new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				int id = v.getId();
				switch (id)
				{
				case R.id.m01:
				{
					Intent i = new Intent(Menu.this,MainActivity.class);
					startActivity(i);
					break;
				}
				case R.id.m02:
				{
					Intent i = new Intent(Menu.this,compete.class);
					startActivity(i);
					break;
				}
				case R.id.m03:
				{
					Intent i = new Intent(Menu.this,Config.class);
					startActivity(i);
					break;
				}
				case R.id.m04:
				{
					/*Intent i = new Intent(Menu.this,OldTrackActivity.class);
					startActivity(i);
					break;*/
				}
				case R.id.m05:
				{
					Intent homeIntent = new Intent(Intent.ACTION_MAIN);
					homeIntent.addCategory( Intent.CATEGORY_HOME );
					homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(homeIntent);
				}
				}
			}
		};
		
		m01.setOnClickListener(listener);
		m02.setOnClickListener(listener);
		m03.setOnClickListener(listener);
		m04.setOnClickListener(listener);
		m05.setOnClickListener(listener);
	}

	@Override
	protected void onDestroy() {
		// TODO 自動產生的方法 Stub

		super.onDestroy();
		
	}
		
	}

	
