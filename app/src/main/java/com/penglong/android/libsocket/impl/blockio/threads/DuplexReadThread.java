package com.penglong.android.libsocket.impl.blockio.threads;

import android.content.Context;

import com.penglong.android.libsocket.impl.LoopThread;
import com.penglong.android.libsocket.impl.abilities.IReader;
import com.penglong.android.libsocket.impl.exceptions.ManuallyDisconnectException;
import com.penglong.android.libsocket.sdk.connection.abilities.IStateSender;
import com.penglong.android.libsocket.sdk.connection.interfacies.IAction;
import com.penglong.android.libsocket.utils.SL;

import java.io.IOException;

/**
 * Created by penglong on 2017/5/17.
 */

public class DuplexReadThread extends LoopThread {
    private IStateSender mStateSender;

    private IReader mReader;

    public DuplexReadThread(Context context, IReader reader, IStateSender stateSender) {
        super(context, "duplex_read_thread");
        this.mStateSender = stateSender;
        this.mReader = reader;
    }

    @Override
    protected void beforeLoop() {
        mStateSender.sendBroadcast(IAction.ACTION_READ_THREAD_START);
    }

    @Override
    protected void runInLoopThread() throws IOException {
        mReader.read();
    }

    @Override
    public synchronized void shutdown(Exception e) {
        mReader.close();
        super.shutdown(e);
    }

    @Override
    protected void loopFinish(Exception e) {
        e = e instanceof ManuallyDisconnectException ? null : e;
        if (e != null) {
            SL.e("duplex read error,thread is dead with exception:" + e.getMessage());
        }
        mStateSender.sendBroadcast(IAction.ACTION_READ_THREAD_SHUTDOWN, e);
    }
}
