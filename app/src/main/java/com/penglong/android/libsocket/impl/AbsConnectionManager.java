package com.penglong.android.libsocket.impl;

import android.content.BroadcastReceiver;
import android.content.Context;

import com.penglong.android.libsocket.impl.abilities.IConnectionSwitchListener;
import com.penglong.android.libsocket.sdk.ConnectionInfo;
import com.penglong.android.libsocket.sdk.connection.IConnectionManager;
import com.penglong.android.libsocket.sdk.connection.interfacies.ISocketActionListener;

import java.io.Serializable;


/**
 * Created by penglong on 2017/5/17.
 */

public abstract class AbsConnectionManager implements IConnectionManager {
    /**
     * 上下文
     */
    protected Context mContext;
    /**
     * 连接信息
     */
    protected ConnectionInfo mConnectionInfo;
    /**
     * 连接信息switch监听器
     */
    private IConnectionSwitchListener mConnectionSwitchListener;
    /**
     * 状态机
     */
    protected ActionDispatcher mActionDispatcher;

    public AbsConnectionManager(Context context, ConnectionInfo info) {
        mContext = context;
        mConnectionInfo = info;
        mActionDispatcher = new ActionDispatcher(mContext, info);
    }

    public void registerReceiver(BroadcastReceiver broadcastReceiver, String... action) {
        mActionDispatcher.registerReceiver(broadcastReceiver, action);
    }

    public void registerReceiver(final ISocketActionListener socketResponseHandler) {
        mActionDispatcher.registerReceiver(socketResponseHandler);
    }


    public void unRegisterReceiver(BroadcastReceiver broadcastReceiver) {
        mActionDispatcher.unRegisterReceiver(broadcastReceiver);
    }

    public void unRegisterReceiver(ISocketActionListener socketResponseHandler) {
        mActionDispatcher.unRegisterReceiver(socketResponseHandler);
    }

    protected void sendBroadcast(String action, Serializable serializable) {
        mActionDispatcher.sendBroadcast(action, serializable);
    }

    protected void sendBroadcast(String action) {
        mActionDispatcher.sendBroadcast(action);
    }

    @Override
    public ConnectionInfo getConnectionInfo() {
        if (mConnectionInfo != null) {
            return mConnectionInfo.clone();
        }
        return null;
    }

    @Override
    public void switchConnectionInfo(ConnectionInfo info) {
        if (info != null) {
            ConnectionInfo tempOldInfo = mConnectionInfo;
            mConnectionInfo = info.clone();
            if (mConnectionSwitchListener != null) {
                mConnectionSwitchListener.onSwitchConnectionInfo(this, tempOldInfo, mConnectionInfo);
            }
        }
    }

    protected void setOnConnectionSwitchListener(IConnectionSwitchListener listener) {
        mConnectionSwitchListener = listener;
    }
}
