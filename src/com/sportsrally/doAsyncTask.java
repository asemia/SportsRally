package com.sportsrally;

import java.net.URL;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class doAsyncTask extends AsyncTask<URL, Integer, String> {

    private Context mContext;
    private ProgressDialog mDialog;

    public doAsyncTask(Context mContext) {
     this.mContext = mContext;
    }

    protected void onPreExecute() {
     
        mDialog = new ProgressDialog(mContext);
        mDialog.setMessage("Loading...");
        mDialog.setCancelable(false);
        mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mDialog.show();
    }
   
    protected String doInBackground(URL... urls) {
        // TODO Auto-generated method stub
     int progress =0;
     final MyValues myapp = (MyValues) mContext.getApplicationContext();
     while(progress<=100){
    	 try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO 自動產生的 catch 區塊
			e.printStackTrace();
		}
    
         progress=myapp.progress;
         publishProgress(Integer.valueOf(progress));
         
     }
     
        return "Loading compleded.";
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        // TODO Auto-generated method stub
        mDialog.setProgress(progress[0]);
    }

    protected void onPostExecute(String result) {
     if(result.equals("Loading compleded.")){
      mDialog.dismiss();  
     }
    }

}