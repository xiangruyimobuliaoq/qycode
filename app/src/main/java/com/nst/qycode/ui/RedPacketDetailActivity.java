package com.nst.qycode.ui;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.nst.qycode.BaseActivity;
import com.nst.qycode.R;
import com.nst.qycode.model.OpenRedPacketReceive;
import com.nst.qycode.util.ConsUtil;
import com.nst.qycode.view.Layout;
import com.nst.qycode.view.ZhaleiWindow;

import java.util.List;

import butterknife.BindView;

/**
 * 创建者     彭龙
 * 创建时间   2018/7/26 下午6:26
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
@Layout(layoutId = R.layout.activity_redpacketdetail)
public class RedPacketDetailActivity extends BaseActivity {

    @BindView(R.id.back)
    protected ImageView back;
    @BindView(R.id.leihao)
    protected TextView leihao;
    @BindView(R.id.text2)
    protected TextView text2;
    @BindView(R.id.name)
    protected TextView name;
    @BindView(R.id.list)
    protected RecyclerView list;
    private OpenRedPacketReceive mData;
    private PacketListAdapter mRoomListAdapter;

    @Override
    protected void init() {
        mData = (OpenRedPacketReceive) getIntent().getSerializableExtra(ConsUtil.REDPACKETDETAIL);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        leihao.setText("本期雷号：" + mData.mCurrentPacket.LandmineNo + "");
        name.setText(mData.mCurrentPacket.Creator);
        text2.setText("已领取" + mData.RedPacketResultInfo.size() + "/10个，共" + ConsUtil.doubleToString(mData.mCurrentPacket.TotalMoney) + "金币");
        mRoomListAdapter = new PacketListAdapter(R.layout.item_detaillist, mData.RedPacketResultInfo);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        list.setLayoutManager(layoutManager);
        list.setAdapter(mRoomListAdapter);
        mRoomListAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        if (mData.Status == 1) {
            for (OpenRedPacketReceive.RedPacketResultInfo info : mData.RedPacketResultInfo
                    ) {
                if (info.Member.equals(ConsUtil.getUsername())) {
                    if (info.IsLandmine) {
                        new ZhaleiWindow(this, R.mipmap.zhonglei).showPopupWindow();
                    } else {
                        new ZhaleiWindow(this, R.mipmap.meizhonglei).showPopupWindow();
                    }
                }
            }
        }
    }

    class PacketListAdapter extends BaseQuickAdapter<OpenRedPacketReceive.RedPacketResultInfo, BaseViewHolder> {
        public PacketListAdapter(int layoutResId, @Nullable List<OpenRedPacketReceive.RedPacketResultInfo> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, OpenRedPacketReceive.RedPacketResultInfo item) {
            helper.setText(R.id.name, item.Member)
                    .setText(R.id.money, ConsUtil.doubleToString(item.RedPacketMoney));
            ImageView view = helper.getView(R.id.lei);
            view.setVisibility(item.IsLandmine ? View.VISIBLE : View.INVISIBLE);
        }
    }
}
