package com.sportsrally;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

public class Config extends Activity {
	EditText du,sspd,hspd;
	TextView TextView01;
	RadioGroup rg01;
	Button bt01,bt02;
	SeekBar sb1;
	
	int tmp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.config);
		TextView01 = (TextView)findViewById(R.id.TextView01);
		rg01 = (RadioGroup)findViewById(R.id.rg01);
		sspd = (EditText) findViewById(R.id.sspd);
		bt01 = (Button) findViewById(R.id.bt01);
		bt02 = (Button) findViewById(R.id.bt02);
		sb1 = (SeekBar)findViewById(R.id.sb1);
		
		sb1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO 自動產生的方法 Stub
				if (sb1.getId()==R.id.sb1){
					double p=(tmp/100.0)*(Double.parseDouble(sspd.getText().toString()));
					TextView01.setText("Lower:"+p);
				}
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO 自動產生的方法 Stub
				if (sb1.getId()==R.id.sb1){
					TextView01.setText("Done");
				}
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO 自動產生的方法 Stub
				if (sb1.getId()==R.id.sb1){
				TextView01.setText("Entering:"+progress);
				tmp=progress;
			}
			}
		});
		bt02.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	
	}
	
}
