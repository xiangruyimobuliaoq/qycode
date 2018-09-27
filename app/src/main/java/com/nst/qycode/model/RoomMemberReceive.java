package com.nst.qycode.model;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 创建者     彭龙
 * 创建时间   2018/7/25 下午4:32
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class RoomMemberReceive extends DataReceive implements Serializable {


    public RoomMemberInfo RoomMemberInfo = new RoomMemberInfo();
    public RoomInfo mCurrentRoom = new RoomInfo();

    public static class RoomMemberInfo implements Serializable {
        public String RoomId;
        public Map<String, Integer> Members = new HashMap<>();
    }


}
