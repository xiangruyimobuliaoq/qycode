package com.nst.qycode.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.nst.qycode.BaseAppActivity;
import com.nst.qycode.R;
import com.nst.qycode.model.CloseRoomReceive;
import com.nst.qycode.model.CloseRoomSend;
import com.nst.qycode.model.ContinueGameReceive;
import com.nst.qycode.model.FaceReceive;
import com.nst.qycode.model.FaceSend;
import com.nst.qycode.model.OpenRedPacketReceive;
import com.nst.qycode.model.OpenRedPacketSend;
import com.nst.qycode.model.RedPacketInfo;
import com.nst.qycode.model.RedPacketListReceive;
import com.nst.qycode.model.RedPacketListSend;
import com.nst.qycode.model.RedPacketReceive;
import com.nst.qycode.model.RedPacketSend;
import com.nst.qycode.model.RoomInfo;
import com.nst.qycode.model.RoomMemberReceive;
import com.nst.qycode.model.TuichuRoomReceive;
import com.nst.qycode.model.TuichuRoomSend;
import com.nst.qycode.util.ConsUtil;
import com.nst.qycode.util.SocketUtil;
import com.nst.qycode.view.EmojWindow;
import com.nst.qycode.view.Layout;
import com.nst.qycode.view.OpenRedPacketWindow;
import com.nst.qycode.view.SendRedPacketWindow;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 创建者     彭龙
 * 创建时间   2018/7/10 下午2:40
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
@Layout(layoutId = R.layout.activity_room)
public class RoomActivity extends BaseAppActivity {
    @BindView(R.id.menu)
    protected ImageView menu;
    @BindView(R.id.redpacketlist)
    protected RecyclerView redpacketlist;
    @BindView(R.id.sendredpacket)
    protected ImageButton sendredpacket;
    @BindView(R.id.exit)
    protected ImageButton exit;
    @BindView(R.id.biaoqing)
    protected ImageButton biaoqing;
    private RoomInfo mData;
    private RedPacketAdapter redpacketadapter;
    private List<RedPacketInfo> mList = new ArrayList<>();
    private SendRedPacketWindow mWindow;
    private OpenRedPacketWindow mOpenRedPacketWindow;
    private RedPacketInfo mCurrentPacket;
    private EmojWindow mEmojWindow;

    @Override
    protected void init() {
        mData = (RoomInfo) getIntent().getSerializableExtra(ConsUtil.ROOMINFO);
        ContinueGameReceive roomInfo = (ContinueGameReceive) getIntent().getSerializableExtra(ConsUtil.CONTINUEGAME);
        if (null != roomInfo) {
            mData = roomInfo.RoomsInfo.get(0);
        }
        getRedPacketList();
        if (ConsUtil.getUsername().equals(mData.Creator)) {
            exit.setImageResource(R.mipmap.jiesan);
            exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new MaterialDialog.Builder(RoomActivity.this)
                            .title("确定要关闭本房间吗?")
                            .positiveText("确定")
                            .negativeText("取消").onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            showDialog("处理中");
                            CloseRoomSend send = new CloseRoomSend();
                            send.ClientId = ConsUtil.getID();
                            send.FuncName = ConsUtil.CLOSEROOM;
                            send.UserName = ConsUtil.getUsername();
                            send.RoomMemberModel.RoomId = mData.RoomId;
                            SocketUtil.sendMsg(new Gson().toJson(send));
                        }
                    }).show();
                }
            });
        } else {
            exit.setImageResource(R.mipmap.tuichu);
            exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new MaterialDialog.Builder(RoomActivity.this)
                            .title("确定要退出游戏吗?")
                            .positiveText("确定")
                            .negativeText("取消").onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            showDialog("处理中");
                            TuichuRoomSend send = new TuichuRoomSend();
                            send.ClientId = ConsUtil.getID();
                            send.FuncName = ConsUtil.QUITROOM;
                            send.UserName = ConsUtil.getUsername();
                            send.RoomMemberModel.RoomId = mData.RoomId;
                            SocketUtil.sendMsg(new Gson().toJson(send));
                        }
                    }).show();
                }
            });
        }
        setTitle(mData.RoomName);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(ConsUtil.ROOMINFO, mData);
                overlay(MemberActivity.class, bundle);
            }
        });
        sendredpacket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendredpacket();
            }
        });
        redpacketadapter = new RedPacketAdapter(null);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        redpacketlist.setLayoutManager(layoutManager);
        redpacketlist.setAdapter(redpacketadapter);
        biaoqing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmojWindow = new EmojWindow(RoomActivity.this, new EmojWindow.EmojCallback() {
                    @Override
                    public void onHistorySubmit(int checkedRadioButtonIndex, String checkedRadioButtonText) {
                        sendFace(checkedRadioButtonIndex + 1);
                        mEmojWindow.dismiss();
                    }
                });
                mEmojWindow.showPopupWindow();
            }
        });
    }

    private void sendFace(int i) {
        FaceSend send = new FaceSend();
        send.ClientId = ConsUtil.getID();
        send.FuncName = ConsUtil.SENDFACE;
        send.UserName = ConsUtil.getUsername();
        send.RoomMemberModel.RoomId = mData.RoomId;
        send.RoomMemberModel.FaceImg = i;
        SocketUtil.sendMsg(new Gson().toJson(send));
    }

    private void getRedPacketList() {
        showDialog("处理中");
        RedPacketListSend send = new RedPacketListSend();
        send.ClientId = ConsUtil.getID();
        send.FuncName = ConsUtil.REDPACKETLIST;
        send.UserName = ConsUtil.getUsername();
        send.RoomMemberModel.RoomId = mData.RoomId;
        SocketUtil.sendMsg(new Gson().toJson(send));
    }

    private void sendredpacket() {
        mWindow = new SendRedPacketWindow(this, new SendRedPacketWindow.SendRedPacketCallback() {
            @Override
            public void onZhuanzhang(double money, int number) {
                showDialog("处理中");
                RedPacketSend send = new RedPacketSend();
                send.ClientId = ConsUtil.getID();
                send.FuncName = ConsUtil.SENDREDPACKET;
                send.UserName = ConsUtil.getUsername();
                send.SendRedPacketModel.RoomId = mData.RoomId;
                send.SendRedPacketModel.TotalMoney = money;
                send.SendRedPacketModel.LandmineNo = number;
                SocketUtil.sendMsg(new Gson().toJson(send));
            }
        });
        mWindow.showPopupWindow();
    }

    class RedPacketAdapter extends BaseMultiItemQuickAdapter<RedPacketInfo, BaseViewHolder> {

        public RedPacketAdapter(@Nullable List<RedPacketInfo> data) {
            super(data);
            addItemType(RedPacketInfo.redpacket, R.layout.item_redpacketlist);
            addItemType(RedPacketInfo.emoj, R.layout.item_emoj);
        }

        @Override
        protected void convert(BaseViewHolder helper, final RedPacketInfo item) {
            CircleImageView img = helper.getView(R.id.img);
            img.setImageResource(getResources().getIdentifier("img" + item.HeaderImg, "mipmap", getApplicationInfo().packageName));
            helper.setText(R.id.name, item.Creator);
            switch (helper.getItemViewType()) {
                case RedPacketInfo.redpacket:
                    helper.setText(R.id.info, "金额: " + ConsUtil.doubleToString(item.TotalMoney) + "金币/雷号: " + item.LandmineNo);
                    final LinearLayout view = helper.getView(R.id.bg);
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            view.setBackgroundResource(R.mipmap.hongbaodakai);
                            mOpenRedPacketWindow = new OpenRedPacketWindow(RoomActivity.this, item, new OpenRedPacketWindow.OpenRedPacketCallback() {
                                @Override
                                public void onZhuanzhang(RedPacketInfo item) {
                                    openRedPacket(item);
                                }
                            });
                            mOpenRedPacketWindow.showPopupWindow();
                        }
                    });
                    break;
                case RedPacketInfo.emoj:
                    ImageView emoj = helper.getView(R.id.emoj);
                    emoj.setImageResource(getResources().getIdentifier("emoj" + item.FaceImg, "mipmap", getApplicationInfo().packageName));
                    break;
            }
        }
    }

    private void openRedPacket(RedPacketInfo item) {
        showDialog("处理中");
        OpenRedPacketSend send = new OpenRedPacketSend();
        send.ClientId = ConsUtil.getID();
        send.FuncName = ConsUtil.OPENREDPACKET;
        send.UserName = ConsUtil.getUsername();
        send.SendRedPacketModel.RedPacketNo = item.RedPacketNo;
        send.SendRedPacketModel.RoomId = item.RoomId;
        mCurrentPacket = item;
        SocketUtil.sendMsg(new Gson().toJson(send));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRoomMemberReceive(RoomMemberReceive ev) {
        toast(ev.Message);
        if (ev.Status == 1) {
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRTuichuRoomReceive(TuichuRoomReceive ev) {
        toast(ev.Message);
        dismissDialog();
        if (ev.Status == 1 && ev.OperMember.equals(ConsUtil.getUsername()) && ev.RoomMemberInfo.RoomId.equals(mData.RoomId)) {
            finish();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCloseRoomReceive(CloseRoomReceive ev) {
        toast(ev.Message);
        dismissDialog();
        if (ev.Status == 1 && ev.OperMember.equals(ConsUtil.getUsername())) {
            finish();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onRedPacketReceive(RedPacketReceive ev) {
        dismissDialog();
        if (ev.Status == 1) {
            if (null != mWindow && mWindow.isShowing() && ev.RedPacketList.get(0).Creator.equals(ConsUtil.getUsername())) {
                mWindow.dismiss();
                toast(ev.Message);
            }
            if (ev.RoomMemberInfo.RoomId.equals(mData.RoomId)) {
                boolean needAdd = true;
                for (RedPacketInfo info :
                        mList) {
                    if (info.itemType == RedPacketInfo.emoj) {
                        continue;
                    }
                    if (info.RedPacketNo.equals(ev.RedPacketList.get(0).RedPacketNo)) {
                        needAdd = false;
                    }
                }
                if (needAdd) {
                    if (ev.RedPacketList.get(0).RoomId.equals(mData.RoomId)) {
                        mList.add(ev.RedPacketList.get(0));
                    }
                }
                redpacketadapter.replaceData(mList);
                if (mList.size() > 0)
                    redpacketlist.scrollToPosition(mList.size() - 1);
            }
        } else {
            toast(ev.Message);
        }
        EventBus.getDefault().removeStickyEvent(ev);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRedPacketListReceive(RedPacketListReceive ev) {
        dismissDialog();
        if (ev.Status == 1) {
            if (null == ev.RedPacketList) {
            } else {
                mList = ev.RedPacketList;
            }
            redpacketadapter.replaceData(mList);
            if (mList.size() > 0)
                redpacketlist.scrollToPosition(mList.size() - 1);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onOpenRedPacketReceive(OpenRedPacketReceive ev) {
        dismissDialog();
        toast(ev.Message);
        if ((ev.Status == 1 || ev.Status == 4) && ev.OperMember.equals(ConsUtil.getUsername())) {
            if (null != mOpenRedPacketWindow && mOpenRedPacketWindow.isShowing()) {
                mOpenRedPacketWindow.dismiss();
                Bundle bundle = new Bundle();
                ev.mCurrentPacket = mCurrentPacket;
                bundle.putSerializable(ConsUtil.REDPACKETDETAIL, ev);
                overlay(RedPacketDetailActivity.class, bundle);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFaceReceive(FaceReceive ev) {
        dismissDialog();
        toast(ev.Message);
        if (ev.Status == 1) {
            int img = ev.RoomMemberInfo.Members.get(ev.OperMember);
            int emoj = ev.RoomMemberInfo.FaceImg;
            RedPacketInfo info = new RedPacketInfo();
            info.HeaderImg = img;
            info.FaceImg = emoj;
            info.itemType = RedPacketInfo.emoj;
            info.Creator = ev.OperMember;
            mList.add(info);
            redpacketadapter.replaceData(mList);
            if (mList.size() > 0)
                redpacketlist.scrollToPosition(mList.size() - 1);
        }
    }
}
