package com.nst.qycode.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.nst.qycode.BaseActivity;
import com.nst.qycode.R;
import com.nst.qycode.model.CreateRoomReceive;
import com.nst.qycode.model.CreateRoomSend;
import com.nst.qycode.model.DataSend;
import com.nst.qycode.model.RoomInfo;
import com.nst.qycode.model.RoomListReceive;
import com.nst.qycode.model.RoomMemberReceive;
import com.nst.qycode.model.RoomMemberSend;
import com.nst.qycode.util.ConsUtil;
import com.nst.qycode.util.SocketUtil;
import com.nst.qycode.view.CreateRoomWindow;
import com.nst.qycode.view.JoinRoomWindow;
import com.nst.qycode.view.Layout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;

/**
 * 创建者     彭龙
 * 创建时间   2018/7/10 上午11:28
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
@Layout(layoutId = R.layout.activity_roomlist)
public class RoomListActivity extends BaseActivity implements CreateRoomWindow.CreateRoomCallback {
    @BindView(R.id.roomlist)
    protected RecyclerView roomlist;
    @BindView(R.id.cancel)
    protected ImageView cancel;
    @BindView(R.id.createRoom)
    protected Button createRoom;
    @BindView(R.id.fastStart)
    protected Button fastStart;
    private RoomListAdapter mRoomListAdapter;
    private List<RoomInfo> mRooms;
    private CreateRoomWindow mWindow;
    private RoomInfo mCurrentRoom;
    private JoinRoomWindow mJoinRoomWindow;

    @Override
    protected void init() {
        getRoomList();
        initView();
    }

    private void getRoomList() {
        showDialog("处理中");
        DataSend send = new DataSend();
        send.ClientId = ConsUtil.getID();
        send.FuncName = ConsUtil.ROOMLIST;
        send.UserName = ConsUtil.getUsername();
        SocketUtil.sendMsg(new Gson().toJson(send));
    }

    private void initView() {
        mRoomListAdapter = new RoomListAdapter(R.layout.item_roomlist, null);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        roomlist.setLayoutManager(layoutManager);
        roomlist.setAdapter(mRoomListAdapter);
        mRoomListAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        View footer = getLayoutInflater().inflate(R.layout.foot_roomlist, null);
        mRoomListAdapter.addFooterView(footer);
        mRoomListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mCurrentRoom = mRooms.get(position);
                if (mCurrentRoom.IsPrivate) {
                    mJoinRoomWindow = new JoinRoomWindow(RoomListActivity.this, new JoinRoomWindow.JoinRoomCallback() {
                        @Override
                        public void onCreateRoom(String pass) {
                            if (pass.equals(mCurrentRoom.RoomPass)) {
                                joinRoom(mCurrentRoom);
                            } else {
                                toast("密码输入错误");
                            }
                        }
                    });
                    mJoinRoomWindow.showPopupWindow();
                } else
                    joinRoom(mCurrentRoom);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        createRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initCreateRoomWindow();
            }
        });
    }

    private void joinRoom(RoomInfo roomInfo) {
        showDialog("处理中");
        RoomMemberSend send = new RoomMemberSend();
        send.ClientId = ConsUtil.getID();
        send.FuncName = ConsUtil.ROOMMEMBER;
        send.UserName = ConsUtil.getUsername();
        send.RoomMemberModel.RoomId = roomInfo.RoomId;
        SocketUtil.sendMsg(new Gson().toJson(send));
    }

    private void initCreateRoomWindow() {
        if (null == mWindow) {
            mWindow = new CreateRoomWindow(this, this);
        }
        if (mWindow.isShowing()) {
            mWindow.dismiss();
        }
        mWindow.showPopupWindow();
    }

    @Override
    public void onCreateRoom(String currentState, String name, String pass, boolean isPrivate) {
        showDialog("处理中");
        CreateRoomSend createRoomSend = new CreateRoomSend();
        createRoomSend.ClientId = ConsUtil.getID();
        createRoomSend.FuncName = ConsUtil.CREATEROOM;
        createRoomSend.UserName = ConsUtil.getUsername();
        createRoomSend.RoomModel.RoomName = name;
        createRoomSend.RoomModel.RoomType = currentState;
        createRoomSend.RoomModel.IsPrivate = isPrivate;
        createRoomSend.RoomModel.RoomPass = pass;
        SocketUtil.sendMsg(new Gson().toJson(createRoomSend));
    }

    class RoomListAdapter extends BaseQuickAdapter<RoomInfo, BaseViewHolder> {
        public RoomListAdapter(int layoutResId, @Nullable List<RoomInfo> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, RoomInfo item) {
            helper.setText(R.id.RoomName, item.RoomName)
                    .setText(R.id.RoomType, item.RoomType)
                    .setText(R.id.status, item.RoomStatus)
                    .setText(R.id.creater, item.Creator)
                    .setVisible(R.id.lock, item.IsPrivate);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onRoomListReceive(RoomListReceive ev) {
        dismissDialog();
        if (ev.Status == 1) {
            mRooms = ev.RoomsInfo;
            mRoomListAdapter.replaceData(mRooms);
//            RoomListReceive stickyEvent = EventBus.getDefault().getStickyEvent(RoomListReceive.class);
//            if (stickyEvent != null)
            EventBus.getDefault().removeStickyEvent(ev);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCreateRoomReceive(CreateRoomReceive ev) {
        toast(ev.Message);
        dismissDialog();
        if (ev.Status == 1) {
            if (null != mWindow && mWindow.isShowing()) {
                mWindow.dismiss();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRoomMemberReceive(RoomMemberReceive ev) {
        toast(ev.Message);
        dismissDialog();
        if (ev.Status == 1 && ev.OperMember.equals(ConsUtil.getUsername())) {
            if (mJoinRoomWindow != null && mJoinRoomWindow.isShowing()) {
                mJoinRoomWindow.dismiss();
            }
            Bundle bundle = new Bundle();
            bundle.putSerializable(ConsUtil.ROOMINFO, mCurrentRoom);
            overlay(RoomActivity.class, bundle);
        }
    }
}
