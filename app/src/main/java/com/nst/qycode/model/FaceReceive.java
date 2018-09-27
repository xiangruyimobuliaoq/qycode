package com.nst.qycode.model;

import java.util.Map;

/**
 * 创建者     彭龙
 * 创建时间   2018/8/11 上午11:34
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class FaceReceive extends DataReceive {
    public RoomMemberInfo RoomMemberInfo;

    public static class RoomMemberInfo {
        public String RoomId;
        public Map<String, Integer> Members;
        public int FaceImg;
    }
}
