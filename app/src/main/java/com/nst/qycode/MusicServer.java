package com.nst.qycode;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

/**
 * 创建者     彭龙
 * 创建时间   2018/7/26 下午8:58
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class MusicServer extends Service {
    private MediaPlayer mediaPlayer;

    private Player mPlayer = new Player();

    public class Player extends Binder {
        MusicServer getService() {
            return MusicServer.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mPlayer;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.music);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        } else {
            mediaPlayer.start();
        }
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
    }
}