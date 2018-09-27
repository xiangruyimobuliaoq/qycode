package com.nst.qycode.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 创建者     彭龙
 * 创建时间   2018/8/9 下午7:03
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class HistoryReceive extends DataReceive {


    public List<Data> TranHistoryInfo;
    public List<Data> RedPacketHistoryInfo;

    public static class Data implements MultiItemEntity {
        public static final int cunkuan = 1;
        public static final int qukuan = 2;
        public static final int fahongbao = 3;
        public static final int qianghongbao = 4;
        public int itemType;

        /**
         * RoomName : 心中有党
         * RoomType : 单倍
         * TotalMoney : 2.08
         * LandmineNo : 2
         * CreateDate : 2018-08-09T10:09:44.377
         */
        public String TranDate;
        public double Amount;
        public String Status;
        public String RoomName;
        public String RoomType;
        public double TotalMoney;
        public int LandmineNo;
        public String CreateDate;

        @Override
        public int getItemType() {
            return itemType;
        }
    }
}
