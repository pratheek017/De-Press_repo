package com.pratapps.www.de_press;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private boolean mEnableAnalysis;
    ActivityManager mActivityManager;
    Thread mAnalysisRunnable;
    Handler mHandler;
    List<ActivityManager.RunningAppProcessInfo> mProcessInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume(){
        super.onResume();

//        mActivityManager = (ActivityManager) getBaseContext().getSystemService(Context.ACTIVITY_SERVICE);
        mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//        mActivityManager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);

        mHandler = new Handler();
        mEnableAnalysis = true;
        mAnalysisRunnable = new Thread(new CollectAppTimeRunnable());
        mAnalysisRunnable.start();
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onStop(){
        super.onStop();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

        mEnableAnalysis = false;
        mAnalysisRunnable.interrupt();
    }


    private class CollectAppTimeRunnable implements Runnable{

        @Override
        public void run() {
            while (mEnableAnalysis){

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mProcessInfos = mActivityManager.getRunningAppProcesses();

                        String packages = "";
                        for (final ActivityManager.RunningAppProcessInfo processInfo: mProcessInfos) {
                            packages = processInfo.processName + "    ";
                        }
                        mProcessInfos.clear();
                        Toast.makeText(getBaseContext(), packages, Toast.LENGTH_SHORT).show();
                    }
                });

                try {
                    Thread.sleep(5000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
