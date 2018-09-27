package com.nst.qycode.model;

/**
 * 创建者     彭龙
 * 创建时间   2018/7/26 下午3:09
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class OpenRedPacketSend extends DataSend {

    public SendRedPacket SendRedPacketModel = new SendRedPacket();

    public class SendRedPacket {
        public String RedPacketNo;
        public String RoomId;
    }
}
