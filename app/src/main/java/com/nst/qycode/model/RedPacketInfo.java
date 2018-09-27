package com.nst.qycode.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

/**
 * 创建者     彭龙
 * 创建时间   2018/8/11 下午1:08
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class RedPacketInfo implements Serializable, MultiItemEntity {
    public static final int redpacket = 1;
    public static final int emoj = 2;
    public int itemType = redpacket;
    public String RoomId;
    public int LandmineNo;
    public double TotalMoney;
    public String Creator;
    public int HeaderImg;
    public int FaceImg;
    public String RedPacketNo;

    @Override
    public int getItemType() {
        return itemType;
    }
}
