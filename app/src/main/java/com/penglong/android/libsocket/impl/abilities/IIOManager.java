package com.penglong.android.libsocket.impl.abilities;

import com.penglong.android.libsocket.sdk.OkSocketOptions;
import com.penglong.android.libsocket.sdk.bean.ISendable;

/**
 * Created by penglong on 2017/5/16.
 */

public interface IIOManager {
    void resolve();

    void setOkOptions(OkSocketOptions options);

    void send(ISendable sendable);

    void close();

    void close(Exception e);

}
