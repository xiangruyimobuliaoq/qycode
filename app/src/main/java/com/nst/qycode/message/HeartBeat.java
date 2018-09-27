package com.nst.qycode.message;

import com.penglong.android.libsocket.sdk.bean.IPulseSendable;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

/**
 * 创建者     彭龙
 * 创建时间   2018/7/23 下午5:19
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class HeartBeat implements IPulseSendable {
    private String heartBeatPacket = "HeartBeatPacket";

    @Override
    public byte[] parse() {
        byte[] body = heartBeatPacket.getBytes(Charset.defaultCharset());
        ByteBuffer bb = ByteBuffer.allocate(4 + body.length);
        bb.order(ByteOrder.BIG_ENDIAN);
        bb.putInt(body.length);
        bb.put(body);
        return bb.array();
    }
}
