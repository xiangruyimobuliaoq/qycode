package com.nst.qycode.model;

import java.io.Serializable;
import java.util.List;

/**
 * 创建者     彭龙
 * 创建时间   2018/7/26 下午5:54
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class OpenRedPacketReceive extends DataReceive {

    public List<RedPacketResultInfo> RedPacketResultInfo;
    public RedPacketInfo mCurrentPacket;

    public static class RedPacketResultInfo implements Serializable {
        /**
         * RoomId : 20180725162416
         * RedPacketNo : 201807251624162687
         * Member : pl8236448
         * RedPacketMoney : 3.0
         * IsLandmine : false
         * BackMoney : 0.0
         * TotalMoney : 50.0
         * Creator : pl8236448
         */
        public String RoomId;
        public String RedPacketNo;
        public String Member;
        public double RedPacketMoney;
        public boolean IsLandmine;
        public double BackMoney;
        public double TotalMoney;
        public String Creator;
    }
}
