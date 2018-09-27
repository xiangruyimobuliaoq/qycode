package com.nst.qycode.ui;

import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.nst.qycode.BaseActivity;
import com.nst.qycode.BaseAppActivity;
import com.nst.qycode.R;
import com.nst.qycode.model.BindCardReceive;
import com.nst.qycode.model.HistoryReceive;
import com.nst.qycode.model.HistorySend;
import com.nst.qycode.model.RoomInfo;
import com.nst.qycode.util.ConsUtil;
import com.nst.qycode.util.DateUtil;
import com.nst.qycode.util.SocketUtil;
import com.nst.qycode.view.HistoryWindow;
import com.nst.qycode.view.Layout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.w3c.dom.Text;

import java.util.List;

import butterknife.BindView;

/**
 * 创建者     彭龙
 * 创建时间   2018/8/9 上午9:44
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
@Layout(layoutId = R.layout.activity_history)
public class HistoryActivity extends BaseAppActivity {
    @BindView(R.id.tv_toolbar_title)
    protected TextView title;
    @BindView(R.id.starttime)
    protected TextView starttime;
    @BindView(R.id.endtime)
    protected TextView endtime;
    @BindView(R.id.list)
    protected RecyclerView list;
    private HistoryWindow mWindow;
    private String currentType;
    private RoomListAdapter mRoomListAdapter;

    @Override
    protected void init() {
        setTitle("点击选择");
        mRoomListAdapter = new RoomListAdapter(null);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        list.setLayoutManager(layoutManager);
        list.setAdapter(mRoomListAdapter);
        mRoomListAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        list.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWindow = new HistoryWindow(HistoryActivity.this, new HistoryWindow.HistoryCallback() {

                    @Override
                    public void onHistorySubmit(int checkedRadioButtonIndex, String checkedRadioButtonText) {
                        if (null != mWindow && mWindow.isShowing()) {
                            mWindow.dismiss();
                            title.setText(checkedRadioButtonText);
                            switch (checkedRadioButtonIndex) {
                                case 0:
                                    currentType = "DepositHistory";
                                    break;
                                case 1:
                                    currentType = "WithdrawHistory";
                                    break;
                                case 2:
                                    currentType = "RedPacketSendHistory";
                                    break;
                                case 3:
                                    currentType = "RedPacketGrabHistory";
                                    break;
                            }
                            refreshData();
                        }
                    }
                });
                mWindow.showPopupWindow(title);
            }
        });
        starttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateUtil.setDate(HistoryActivity.this, DateUtil.YMD, new DateUtil.Callback() {
                    @Override
                    public void callback(String result) {
                        starttime.setText(result);
                        refreshData();
                    }
                });
            }
        });
        endtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateUtil.setDate(HistoryActivity.this, DateUtil.YMD, new DateUtil.Callback() {
                    @Override
                    public void callback(String result) {
                        endtime.setText(result);
                        refreshData();
                    }
                });
            }
        });
    }

    private void refreshData() {
        String begin = starttime.getText().toString().trim();
        String end = endtime.getText().toString().trim();
        if (TextUtils.isEmpty(begin)) {
            return;
        }
        if (TextUtils.isEmpty(end)) {
            return;
        }
        if (TextUtils.isEmpty(currentType)) {
            return;
        }
        showDialog("处理中");
        HistorySend send = new HistorySend();
        send.ClientId = ConsUtil.getID();
        send.FuncName = currentType;
        send.UserName = ConsUtil.getUsername();
        send.HistoryModel.BeginDate = begin;
        send.HistoryModel.EndDate = end;
        SocketUtil.sendMsg(new Gson().toJson(send));
    }

    class RoomListAdapter extends BaseMultiItemQuickAdapter<HistoryReceive.Data, BaseViewHolder> {
        public RoomListAdapter(@Nullable List data) {
            super(data);
            addItemType(HistoryReceive.Data.cunkuan, R.layout.item_kuan);
            addItemType(HistoryReceive.Data.qukuan, R.layout.item_kuan);
            addItemType(HistoryReceive.Data.fahongbao, R.layout.item_hongbao);
            addItemType(HistoryReceive.Data.qianghongbao, R.layout.item_hongbao);
        }

        @Override
        protected void convert(BaseViewHolder helper, HistoryReceive.Data item) {
            switch (helper.getItemViewType()) {
                case HistoryReceive.Data.cunkuan:
                    helper.setText(R.id.type, "存款金额")
                            .setText(R.id.time, item.TranDate)
                            .setText(R.id.amount, ConsUtil.doubleToString(item.Amount))
                            .setText(R.id.status, item.Status);
                    break;
                case HistoryReceive.Data.qukuan:
                    helper.setText(R.id.type, "取款金额")
                            .setText(R.id.time, item.TranDate)
                            .setText(R.id.amount, ConsUtil.doubleToString(item.Amount))
                            .setText(R.id.status, item.Status);
                    break;
                case HistoryReceive.Data.fahongbao:
                    helper
                            .setText(R.id.name, "发包房间: " + item.RoomName)
                            .setText(R.id.type, "房间类型: " + item.RoomType)
                            .setText(R.id.money, "发包金额: " + ConsUtil.doubleToString(item.TotalMoney))
                            .setText(R.id.num, "雷号: " + item.LandmineNo)
                            .setText(R.id.time, "发包时间: " + item.CreateDate);
                    break;
                case HistoryReceive.Data.qianghongbao:
                    helper
                            .setText(R.id.name, "抢包房间: " + item.RoomName)
                            .setText(R.id.type, "房间类型: " + item.RoomType)
                            .setText(R.id.money, "抢到金额: " + ConsUtil.doubleToString(item.TotalMoney))
                            .setText(R.id.num, "雷号: " + item.LandmineNo)
                            .setText(R.id.time, "抢包时间: " + item.CreateDate);
                    break;
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHistoryReceive(HistoryReceive ev) {
        toast(ev.Message);
        dismissDialog();
        if (ev.Status == 1) {
            switch (ev.FuncName) {
                case "RedPacketSendHistory":
                    for (HistoryReceive.Data data : ev.RedPacketHistoryInfo
                            ) {
                        data.itemType = HistoryReceive.Data.fahongbao;
                    }
                    mRoomListAdapter.replaceData(ev.RedPacketHistoryInfo);
                    break;
                case "RedPacketGrabHistory":
                    for (HistoryReceive.Data data : ev.RedPacketHistoryInfo
                            ) {
                        data.itemType = HistoryReceive.Data.qianghongbao;
                    }
                    mRoomListAdapter.replaceData(ev.RedPacketHistoryInfo);
                    break;
                case "DepositHistory":
                    for (HistoryReceive.Data data : ev.TranHistoryInfo
                            ) {
                        data.itemType = HistoryReceive.Data.cunkuan;
                    }
                    mRoomListAdapter.replaceData(ev.TranHistoryInfo);
                    break;
                case "WithdrawHistory":
                    for (HistoryReceive.Data data : ev.TranHistoryInfo
                            ) {
                        data.itemType = HistoryReceive.Data.qukuan;
                    }
                    mRoomListAdapter.replaceData(ev.TranHistoryInfo);
                    break;
            }
        } else {
            mRoomListAdapter.getData().clear();
            mRoomListAdapter.notifyDataSetChanged();
        }
    }
}
