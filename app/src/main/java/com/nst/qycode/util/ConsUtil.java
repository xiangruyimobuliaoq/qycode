package com.nst.qycode.util;

import java.text.DecimalFormat;

/**
 * 创建者     彭龙
 * 创建时间   2018/7/20 下午2:51
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class ConsUtil {
    public static final String FUNCNAME = "FuncName";
    public static final String CREATEROOM = "CreateRoom";
    public static final String USERNAME = "username";
    public static final String MEMBERACCOUNT = "MemberAccount";
    public static final String GETBALANCE = "GetBalance";
    public static final String BANKCARD = "BankCard";
    public static final String MEMBERWITHDRAW = "MemberWithdraw";
    public static final String ROOMMEMBER = "RoomMember";
    public static final String ROOMINFO = "roomInfo";
    public static final String SENDREDPACKET = "SendRedPacket";
    public static final String MEMBERTRANSFER = "MemberTransfer";
    public static final String ROOMLIST = "RoomList";
    public static final String MEMBERDEPOSIT = "MemberDeposit";
    public static final String MEMBERLIST = "MemberList";
    public static final String QUITROOM = "QuitRoom";
    public static final String REDPACKETLIST = "RedPacketList";
    public static final String CLOSEROOM = "CloseRoom";
    public static final String OPENREDPACKET = "OpenRedPacket";
    public static final String REDPACKETDETAIL = "RedPacketDetail";
    public static final String GETSYSTEMCARD = "GetSystemCard";
    public static final String GETMEMBERCARD = "GetMemberCard";
    public static final String CONTINUEGAME = "ContinueGame";
    public static final String IMG = "img";
    public static final String LOGOUT = "Logout";
    private static final String ISLOGIN = "islogin";
    public static final String SENDFACE = "SendFace";
    public static final String RELINK = "ReLink";
    public static final String GETNOTIFY = "GetNotify";
    public static final String ALIPAYDEPOSIT = "AlipayDeposit";
    public static String USERINFO = "userinfo";
    public static String CLIENTID = "clientid";


    public static final String LOGIN = "Login";
    public static final String REGISTER = "Register";


    public static String getID() {
        return SpUtil.getString(ConsUtil.USERINFO, ConsUtil.CLIENTID, null);
    }

    public static void cleanID() {
        SpUtil.putString(ConsUtil.USERINFO, ConsUtil.CLIENTID, null);
    }

    public static void setLogin(boolean islogin) {
        SpUtil.putBoolean(ConsUtil.USERINFO, ConsUtil.ISLOGIN, islogin);
    }

    public static boolean getLogin() {
        return SpUtil.getBoolean(ConsUtil.USERINFO, ConsUtil.ISLOGIN, false);
    }

    public static int getIMG() {
        return SpUtil.getInt(ConsUtil.USERINFO, ConsUtil.IMG, 0);
    }

    public static String getUsername() {
        return SpUtil.getString(ConsUtil.USERINFO, ConsUtil.USERNAME, null);
    }

    public static void cleanUsername() {
        SpUtil.getString(ConsUtil.USERINFO, ConsUtil.USERNAME, null);
    }

    public static String doubleToString(double num){
        //使用0.00不足位补0，#.##仅保留有效位
        return new DecimalFormat("0.00").format(num);
    }
}
