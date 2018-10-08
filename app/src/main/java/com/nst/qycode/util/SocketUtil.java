package com.nst.qycode.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.nst.qycode.event.SocketConnectionEvent;
import com.nst.qycode.message.HeartBeat;
import com.nst.qycode.message.Message;
import com.nst.qycode.model.AlipayDepositReceive;
import com.nst.qycode.model.BindCardReceive;
import com.nst.qycode.model.CloseRoomReceive;
import com.nst.qycode.model.CloseRoomSend;
import com.nst.qycode.model.ContinueGameReceive;
import com.nst.qycode.model.CreateRoomReceive;
import com.nst.qycode.model.DataReceive;
import com.nst.qycode.model.DataSend;
import com.nst.qycode.model.FaceReceive;
import com.nst.qycode.model.FaceSend;
import com.nst.qycode.model.GetBalanceReceive;
import com.nst.qycode.model.GetMemberCardReceive;
import com.nst.qycode.model.GetNotifyReceive;
import com.nst.qycode.model.GetSystemCardReceive;
import com.nst.qycode.model.HistoryReceive;
import com.nst.qycode.model.LoginReceive;
import com.nst.qycode.model.LogoutReceive;
import com.nst.qycode.model.MemberAccountReceive;
import com.nst.qycode.model.MemberDepositReceive;
import com.nst.qycode.model.MemberListReceive;
import com.nst.qycode.model.MemberTransferReceive;
import com.nst.qycode.model.MemberWithdrawReceive;
import com.nst.qycode.model.OpenRedPacketReceive;
import com.nst.qycode.model.RedPacketListReceive;
import com.nst.qycode.model.RedPacketReceive;
import com.nst.qycode.model.RegisterReceive;
import com.nst.qycode.model.RoomListReceive;
import com.nst.qycode.model.RoomMemberReceive;
import com.nst.qycode.model.TuichuRoomReceive;
import com.penglong.android.libsocket.impl.PulseManager;
import com.penglong.android.libsocket.sdk.ConnectionInfo;
import com.penglong.android.libsocket.sdk.OkSocket;
import com.penglong.android.libsocket.sdk.SocketActionAdapter;
import com.penglong.android.libsocket.sdk.bean.ISendable;
import com.penglong.android.libsocket.sdk.bean.OriginalData;
import com.penglong.android.libsocket.sdk.connection.IConnectionManager;

import org.greenrobot.eventbus.EventBus;


/**
 * 创建者     彭龙
 * 创建时间   2018/7/13 下午3:48
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class SocketUtil {
    private static Gson gson = new Gson();
    private static IConnectionManager socketManger;

    public static void initSocket() {
//        ConnectionInfo info = new ConnectionInfo("13.70.6.71", 1080);
        ConnectionInfo info = new ConnectionInfo("27.19.196.161", 1080);
        socketManger = OkSocket.open(info);
        socketManger.registerReceiver(new SocketActionAdapter() {
            @Override
            public void onSocketConnectionSuccess(Context context, ConnectionInfo info, String action) {
                EventBus.getDefault().postSticky(new SocketConnectionEvent(1));
                if (null != socketManger) {
                    PulseManager pulseManager = socketManger.getPulseManager();
                    pulseManager.setPulseSendable(new HeartBeat()).pulse();
                } else {
                    initSocket();
                }
                if (TextUtils.isEmpty(ConsUtil.getID()) || TextUtils.isEmpty(ConsUtil.getUsername())) {
                    return;
                }
                if (ConsUtil.getLogin()) {
                    DataSend send = new DataSend();
                    send.ClientId = ConsUtil.getID();
                    send.FuncName = ConsUtil.RELINK;
                    send.UserName = ConsUtil.getUsername();
                    sendMsg(gson.toJson(send));
                }
            }

            @Override
            public void onSocketConnectionFailed(Context context, ConnectionInfo info, String action, Exception e) {
                super.onSocketConnectionFailed(context, info, action, e);
                EventBus.getDefault().postSticky(new SocketConnectionEvent(0));
            }

            @Override
            public void onSocketReadResponse(Context context, ConnectionInfo info, String action, OriginalData data) {
                super.onSocketReadResponse(context, info, action, data);
                String s = new String(data.getBodyBytes());
                Log.e("recive", action + "   " + s);
                if ("HeartBeatPacketAccess".equals(s) && null != socketManger) {
                    socketManger.getPulseManager().feed();
                    return;
                }
                DataReceive baseData = gson.fromJson(s, DataReceive.class);
                if (baseData.Status != 3) {
                    switch (baseData.FuncName) {
                        case "Login":
                            Login(s);
                            break;
                        case "Register":
                            Register(s);
                            break;
                        case "RoomList":
                            RoomList(s);
                            break;
                        case "CreateRoom":
                            CreateRoom(s);
                            break;
                        case "MemberAccount":
                            MemberAccount(s);
                            break;
                        case "GetBalance":
                            GetBalance(s);
                            break;
                        case "BankCard":
                            BankCard(s);
                            break;
                        case "MemberWithdraw":
                            MemberWithdraw(s);
                            break;
                        case "RoomMember":
                            RoomMember(s);
                            break;
                        case "SendRedPacket":
                            SendRedPacket(s);
                            break;
                        case "MemberTransfer":
                            MemberTransfer(s);
                            break;
                        case "MemberDeposit":
                            MemberDeposit(s);
                            break;
                        case "MemberList":
                            MemberList(s);
                            break;
                        case "QuitRoom":
                            QuitRoom(s);
                            break;
                        case "CloseRoom":
                            CloseRoom(s);
                            break;
                        case "RedPacketList":
                            RedPacketList(s);
                            break;
                        case "RedPacketResult":
                            RedPacketResult(s);
                            break;
                        case "OpenRedPacket":
                            RedPacketResult(s);
                            break;
                        case "GetSystemCard":
                            GetSystemCard(s);
                            break;
                        case "GetMemberCard":
                            GetMemberCard(s);
                            break;
                        case "GetNotify":
                            GetNotify(s);
                            break;
                        case "ContinueGame":
                            ContinueGame(s);
                            break;
                        case "RedPacketSendHistory":
                        case "RedPacketGrabHistory":
                        case "DepositHistory":
                        case "WithdrawHistory":
                            History(s);
                            break;
                        case "Logout":
                            Logout(s);
                            break;
                        case "SendFace":
                            SendFace(s);
                            break;
                        case "AlipayDeposit":
                            AlipayDeposit(s);
                            break;
                    }
                } else {//权限异常
                    SocketConnectionEvent event = new SocketConnectionEvent(-1);
                    EventBus.getDefault().post(event);
                }
            }

            @Override
            public void onSocketWriteResponse(Context context, ConnectionInfo info, String action, ISendable data) {
                super.onSocketWriteResponse(context, info, action, data);
                Log.e("send", action + "   " + new String(data.parse()));
            }
        });
        socketManger.connect();
    }

    private static void AlipayDeposit(String s) {
        AlipayDepositReceive receive = gson.fromJson(s, AlipayDepositReceive.class);
        EventBus.getDefault().post(receive);
    }

    private static void SendFace(String s) {
        FaceReceive receive = gson.fromJson(s, FaceReceive.class);
        EventBus.getDefault().post(receive);
    }

    private static void Logout(String s) {
        LogoutReceive receive = gson.fromJson(s, LogoutReceive.class);
        EventBus.getDefault().post(receive);
    }

    private static void History(String s) {
        HistoryReceive receive = gson.fromJson(s, HistoryReceive.class);
        EventBus.getDefault().post(receive);
    }

    private static void ContinueGame(String s) {
        ContinueGameReceive receive = gson.fromJson(s, ContinueGameReceive.class);
        EventBus.getDefault().postSticky(receive);
    }

    private static void GetNotify(String s) {
        GetNotifyReceive receive = gson.fromJson(s, GetNotifyReceive.class);
        EventBus.getDefault().postSticky(receive);
    }

    private static void GetMemberCard(String s) {
        GetMemberCardReceive receive = gson.fromJson(s, GetMemberCardReceive.class);
        EventBus.getDefault().post(receive);
    }

    private static void GetSystemCard(String s) {
        GetSystemCardReceive receive = gson.fromJson(s, GetSystemCardReceive.class);
        EventBus.getDefault().post(receive);
    }

    private static void RedPacketResult(String s) {
        OpenRedPacketReceive receive = gson.fromJson(s, OpenRedPacketReceive.class);
        EventBus.getDefault().post(receive);
    }

    private static void CloseRoom(String s) {
        CloseRoomReceive receive = gson.fromJson(s, CloseRoomReceive.class);
        EventBus.getDefault().post(receive);
    }

    private static void RedPacketList(String s) {
        RedPacketListReceive receive = gson.fromJson(s, RedPacketListReceive.class);
        EventBus.getDefault().post(receive);
    }

    private static void QuitRoom(String s) {
        TuichuRoomReceive receive = gson.fromJson(s, TuichuRoomReceive.class);
        EventBus.getDefault().post(receive);
    }

    private static void MemberList(String s) {
        MemberListReceive receive = gson.fromJson(s, MemberListReceive.class);
        EventBus.getDefault().post(receive);
    }

    private static void MemberDeposit(String s) {
        MemberDepositReceive receive = gson.fromJson(s, MemberDepositReceive.class);
        EventBus.getDefault().post(receive);
    }

    private static void MemberTransfer(String s) {
        MemberTransferReceive receive = gson.fromJson(s, MemberTransferReceive.class);
        EventBus.getDefault().post(receive);
    }

    private static void SendRedPacket(String s) {
        RedPacketReceive receive = gson.fromJson(s, RedPacketReceive.class);
        EventBus.getDefault().postSticky(receive);
    }

    private static void RoomMember(String s) {
        RoomMemberReceive receive = gson.fromJson(s, RoomMemberReceive.class);
        EventBus.getDefault().post(receive);
    }

    private static void MemberWithdraw(String s) {
        MemberWithdrawReceive receive = gson.fromJson(s, MemberWithdrawReceive.class);
        EventBus.getDefault().post(receive);
    }

    private static void BankCard(String s) {
        BindCardReceive receive = gson.fromJson(s, BindCardReceive.class);
        EventBus.getDefault().post(receive);
    }

    private static void GetBalance(String s) {
        GetBalanceReceive receive = gson.fromJson(s, GetBalanceReceive.class);
        EventBus.getDefault().post(receive);
    }

    private static void MemberAccount(String s) {
        MemberAccountReceive memberAccountReceive = gson.fromJson(s, MemberAccountReceive.class);
        EventBus.getDefault().post(memberAccountReceive);
    }

    private static void CreateRoom(String s) {
        CreateRoomReceive createRoomReceive = gson.fromJson(s, CreateRoomReceive.class);
        EventBus.getDefault().post(createRoomReceive);
    }

    private static void RoomList(String s) {
        RoomListReceive roomlistreceive = gson.fromJson(s, RoomListReceive.class);
        EventBus.getDefault().postSticky(roomlistreceive);
    }

    private static void Register(String s) {
        RegisterReceive registerReceive = gson.fromJson(s, RegisterReceive.class);
        EventBus.getDefault().post(registerReceive);
    }

    private static void Login(String s) {
        LoginReceive login = gson.fromJson(s, LoginReceive.class);
        if (login.Status == 1) {
            SpUtil.putString(ConsUtil.USERINFO, ConsUtil.CLIENTID, login.UserInfo.ClientId);
            SpUtil.putInt(ConsUtil.USERINFO, ConsUtil.IMG, login.UserInfo.HeaderImg);
        }
        EventBus.getDefault().post(login);
    }

    public static void sendMsg(String msg) {
        if (null != socketManger && socketManger.isConnect()) {
            socketManger.send(new Message(msg));
        }
    }
}
