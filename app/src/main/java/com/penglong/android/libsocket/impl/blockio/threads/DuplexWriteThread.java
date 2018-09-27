package com.penglong.android.libsocket.impl.blockio.threads;

import android.content.Context;

import com.penglong.android.libsocket.impl.LoopThread;
import com.penglong.android.libsocket.impl.abilities.IWriter;
import com.penglong.android.libsocket.impl.exceptions.ManuallyDisconnectException;
import com.penglong.android.libsocket.sdk.connection.abilities.IStateSender;
import com.penglong.android.libsocket.sdk.connection.interfacies.IAction;
import com.penglong.android.libsocket.utils.SL;

import java.io.IOException;

/**
 * Created by penglong on 2017/5/17.
 */

public class DuplexWriteThread extends LoopThread {
    private IStateSender mStateSender;

    private IWriter mWriter;

    public DuplexWriteThread(Context context, IWriter writer,
                             IStateSender stateSender) {
        super(context, "duplex_write_thread");
        this.mStateSender = stateSender;
        this.mWriter = writer;
    }

    @Override
    protected void beforeLoop() {
        mStateSender.sendBroadcast(IAction.ACTION_WRITE_THREAD_START);
    }

    @Override
    protected void runInLoopThread() throws IOException {
        mWriter.write();
    }

    @Override
    public synchronized void shutdown(Exception e) {
        mWriter.close();
        super.shutdown(e);
    }

    @Override
    protected void loopFinish(Exception e) {
        e = e instanceof ManuallyDisconnectException ? null : e;
        if (e != null) {
            SL.e("duplex write error,thread is dead with exception:" + e.getMessage());
        }
        mStateSender.sendBroadcast(IAction.ACTION_WRITE_THREAD_SHUTDOWN, e);
    }
}
