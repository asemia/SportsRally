package com.sportsrally;

import java.util.Calendar;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class TimerService extends Service {

	private static UIHandler handler;
	public static final String ACTION_PLAY = "com.sportsrally.action.PLAY";
	public static final String ACTION_STOP = "com.sportsrally.action.STOP";
	Context context = this;

	enum State {
		Stopped, Running
	};

	static State mState = State.Stopped;

	private Boolean isRun = true;

	private static ServiceThread serviceThread;

	// NotificationManager mNotificationManager;
	Notification mNotification = null;
	final int NOTIFICATION_ID = 1;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO 自動產生的方法 Stub

		String action = intent.getAction();
		if (action.equals(ACTION_PLAY)) {

			isRun = true;
			startTimer();
		} else if (action.equals(ACTION_STOP)) {
			stopTimer();
		}

		return super.onStartCommand(intent, flags, startId);
	}

	private void stopTimer() {
		// stop thread
		mState = State.Stopped;
		isRun = false;
		stopForeground(true);
	}

	private void startTimer() {

		mState = State.Running;
		serviceThread = new ServiceThread(handler);
		// new Thread(new ServiceThread(handler)).start();
		new Thread(serviceThread).start();

		setUpAsForeground("SportsRally Recording ...");
	}

	public static State getTimerState() {
		return mState;
	}

	public static void registerHandler(Handler uiHandler) {
		handler = (UIHandler) uiHandler;
	}

	public static UIHandler getUIHandler() {
		return handler;
	}

	public static void resetServiceThreadHandler() {
		serviceThread.threadHandler = handler;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO 自動產生的方法 Stub
		return null;
	}

	@Override
	public void onDestroy() {
		// TODO 自動產生的方法 Stub
		super.onDestroy();
	}

	public class ServiceThread implements Runnable {

		private UIHandler threadHandler;

		public ServiceThread(UIHandler handler) {
			super();
			this.threadHandler = handler;
		}

		@Override
		public void run() {

			while (isRun) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				final MyValues myapp = (MyValues) context
						.getApplicationContext();
				long time = (myapp.spentSeconds + (System.currentTimeMillis() - myapp.startTime)) / 1000;

				String s = String.format("%d", time);

				Message msg = this.threadHandler.obtainMessage();

				msg.getData().putString("spentTime", s);
				threadHandler.sendMessage(msg);
				Log.v("TEST", "still runing");
			}

			{
				final MyValues myapp = (MyValues) context
						.getApplicationContext();
				myapp.endTime = System.currentTimeMillis();
				myapp.spentSeconds = myapp.spentSeconds + myapp.endTime
						- myapp.startTime;
				long time = (myapp.spentSeconds) / 1000;
				String s = String.format("%d", time);
			}
		}

	}

	@SuppressWarnings("deprecation")
	void setUpAsForeground(String text) {
		PendingIntent pi = PendingIntent.getActivity(getApplicationContext(),
				0, new Intent(getApplicationContext(), MainActivity.class),
				PendingIntent.FLAG_UPDATE_CURRENT);
		mNotification = new Notification();
		mNotification.tickerText = text;
		mNotification.icon = R.drawable.ic_launcher;
		mNotification.flags |= Notification.FLAG_ONGOING_EVENT;
		mNotification.setLatestEventInfo(getApplicationContext(), "Timer",
				text, pi);
		startForeground(NOTIFICATION_ID, mNotification);
	}

}
