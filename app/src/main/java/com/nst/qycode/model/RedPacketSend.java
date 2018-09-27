package com.nst.qycode.model;

/**
 * 创建者     彭龙
 * 创建时间   2018/7/25 下午6:46
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class RedPacketSend extends DataSend {

    public SendRedPacket SendRedPacketModel = new SendRedPacket();

    public class SendRedPacket {
        public String RoomId;
        public int LandmineNo;
        public double TotalMoney;
    }
}
