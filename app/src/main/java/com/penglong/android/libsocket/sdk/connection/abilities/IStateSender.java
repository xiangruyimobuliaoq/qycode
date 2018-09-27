package com.penglong.android.libsocket.sdk.connection.abilities;

import java.io.Serializable;

/**
 * Created by penglong on 2017/5/17.
 */

public interface IStateSender {

    void sendBroadcast(String action, Serializable serializable);

    void sendBroadcast(String action);
}
