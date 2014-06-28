package com.sportsrally;


import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class compete extends Activity {
	ImageButton start,end;
	ImageView playera,playerb;
	AnimationDrawable ad,ae;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自動產生的方法 Stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.compete);
		start = (ImageButton)findViewById(R.id.start);
		end = (ImageButton)findViewById(R.id.end);
		playera = (ImageView)findViewById(R.id.playera);
		playerb	= (ImageView)findViewById(R.id.playerb);
		
		start.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自動產生的方法 Stub
				playera.setBackgroundResource(R.anim.anim_playera);
				playerb.setBackgroundResource(R.anim.anim_playerb);
				ad = (AnimationDrawable) playera.getDrawable();
				ae =(AnimationDrawable) playerb.getDrawable();
				ad.start();
				ae.start();
			}
		});
	end.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO 自動產生的方法 Stub
			ad.stop();
			ae.stop();
		}
	});
	}

}
