package com.nst.qycode;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.nst.qycode.util.SocketUtil;
import com.nst.qycode.util.UIUtil;
import com.penglong.android.libsocket.sdk.OkSocket;
import com.pgyersdk.crash.PgyCrashManager;
import com.pgyersdk.crash.PgyerCrashObservable;
import com.pgyersdk.crash.PgyerObserver;

import java.util.List;

/**
 * 创建者     彭龙
 * 创建时间   2018/6/14 下午3:35
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class App extends Application {

    private static boolean isAppInForeground;
    private static int started;
    private static int stopped;

    private ScreenListener mListener;
    private Intent mIntent;

    @Override
    public void onCreate() {
        super.onCreate();
        UIUtil.init(this);
        OkSocket.initialize(this, true);
        SocketUtil.initSocket();
        PgyCrashManager.register(getApplicationContext());  // 弃用
        PgyCrashManager.register();
        PgyerCrashObservable.get().attach(new PgyerObserver() {
            @Override
            public void receivedCrash(Thread thread, Throwable throwable) {

            }
        });
        mIntent = new Intent();
        mIntent.setAction("com.angel.Android.MUSIC");
        mIntent.setPackage(getPackageName());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(mIntent);
        } else {
            startService(mIntent);
        }
        mListener = new ScreenListener(this);
        mListener.begin(new ScreenListener.ScreenStateListener() {
            @Override
            public void onScreenOn() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(mIntent);
                } else {
                    startService(mIntent);
                }
            }

            @Override
            public void onScreenOff() {
                stopService(mIntent);
            }

            @Override
            public void onUserPresent() {

            }
        });

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
                if (isAppShowFromBackground()) {
                    isAppInForeground = true;
                }
                ++started;
                if (isAppInForeground()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(mIntent);
                    } else {
                        startService(mIntent);
                    }
                }
            }

            @Override
            public void onActivityResumed(Activity activity) {
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
                ++stopped;
                if (isAppInBackground()) {
                    isAppInForeground = false;
                }
                if (isAppInBackground()) {
                    stopService(mIntent);
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }

            private boolean isAppShowFromBackground() {
                return started == stopped;
            }

            //外部调用
            public boolean isAppInForeground() {
                return isAppInForeground;
            }

            public boolean isAppInBackground() {
                return started == stopped;
            }
        });

    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }
}