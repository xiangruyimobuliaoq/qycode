package com.penglong.android.libsocket.impl.abilities;

import com.penglong.android.libsocket.sdk.OkSocketOptions;
import com.penglong.android.libsocket.sdk.bean.ISendable;

/**
 * Created by penglong on 2017/5/16.
 */

public interface IWriter {
    boolean write() throws RuntimeException;

    void setOption(OkSocketOptions option);

    void offer(ISendable sendable);

    void close();

}
