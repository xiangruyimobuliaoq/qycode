package com.penglong.android.libsocket.impl.abilities;

import com.penglong.android.libsocket.sdk.ConnectionInfo;
import com.penglong.android.libsocket.sdk.connection.IConnectionManager;

/**
 * Created by penglong on 2017/6/30.
 */

public interface IConnectionSwitchListener {
    void onSwitchConnectionInfo(IConnectionManager manager, ConnectionInfo oldInfo, ConnectionInfo newInfo);
}
