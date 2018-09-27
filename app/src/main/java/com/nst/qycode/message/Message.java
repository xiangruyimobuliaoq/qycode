package com.nst.qycode.message;

import android.util.Log;

import com.google.gson.JsonObject;
import com.nst.qycode.util.SpUtil;
import com.nst.qycode.util.ConsUtil;
import com.penglong.android.libsocket.sdk.bean.ISendable;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

/**
 * 创建者     彭龙
 * 创建时间   2018/7/17 上午11:15
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class Message implements ISendable {
    String data;


    public Message(String msg) {
        data = msg;
    }

    @Override
    public byte[] parse() {
        byte[] body = data.getBytes(Charset.defaultCharset());
        ByteBuffer bb = ByteBuffer.allocate(4 + body.length);
        bb.order(ByteOrder.BIG_ENDIAN);
        bb.putInt(body.length);
        bb.put(body);
        return bb.array();
    }
}
