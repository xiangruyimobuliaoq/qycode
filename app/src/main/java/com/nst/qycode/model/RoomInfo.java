package com.nst.qycode.model;

import java.io.Serializable;

/**
 * 创建者     彭龙
 * 创建时间   2018/7/10 下午12:15
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class RoomInfo implements Serializable{

    public String RoomId;
    public String RoomName;
    public String RoomType;
    public String Creator;
    public String RoomStatus;
    public String RoomPass;
    public boolean IsPrivate;
}
