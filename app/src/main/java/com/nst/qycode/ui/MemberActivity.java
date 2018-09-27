package com.nst.qycode.ui;

import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.nst.qycode.BaseActivity;
import com.nst.qycode.R;
import com.nst.qycode.model.MemberListReceive;
import com.nst.qycode.model.MemberListSend;
import com.nst.qycode.model.RoomInfo;
import com.nst.qycode.model.RoomMemberReceive;
import com.nst.qycode.util.ConsUtil;
import com.nst.qycode.util.SocketUtil;
import com.nst.qycode.view.Layout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 创建者     彭龙
 * 创建时间   2018/7/21 上午8:45
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
@Layout(layoutId = R.layout.activity_menu)
public class MemberActivity extends BaseActivity {
    @BindView(R.id.back)
    protected ImageView back;
    @BindView(R.id.memberList)
    protected RecyclerView memberlist;
    private Map<String, Integer> mRooms;
    private MemberListAdapter mMemberListAdapter;
    private RoomInfo mData;

    @Override
    protected void init() {
        mData = (RoomInfo) getIntent().getSerializableExtra(ConsUtil.ROOMINFO);
        getMemberList();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mMemberListAdapter = new MemberListAdapter(R.layout.item_memberlist, null);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        memberlist.setLayoutManager(layoutManager);
        memberlist.setAdapter(mMemberListAdapter);
        View footer = getLayoutInflater().inflate(R.layout.foot_roomlist, null);
        mMemberListAdapter.addFooterView(footer);
        mMemberListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            }
        });
    }

    private void getMemberList() {
        showDialog("处理中");
        MemberListSend send = new MemberListSend();
        send.ClientId = ConsUtil.getID();
        send.UserName = ConsUtil.getUsername();
        send.FuncName = ConsUtil.MEMBERLIST;
        send.RoomMemberModel.RoomId = mData.RoomId;
        SocketUtil.sendMsg(new Gson().toJson(send));

    }

    class MemberListAdapter extends BaseQuickAdapter<Map.Entry<String, Integer>, BaseViewHolder> {

        public MemberListAdapter(int layoutResId, @Nullable List<Map.Entry<String, Integer>> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, Map.Entry<String, Integer> item) {
            helper.setText(R.id.name, item.getKey());
            CircleImageView view = helper.getView(R.id.img);
            view.setImageResource(getResources().getIdentifier("img" + ConsUtil.getIMG(), "mipmap", getApplicationInfo().packageName));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRoomMemberReceive(RoomMemberReceive ev) {
        toast(ev.Message);
        if (ev.Status == 1) {
            mRooms = ev.RoomMemberInfo.Members;
            refresh();
        }
    }

    private void refresh() {
        ArrayList<Map.Entry<String, Integer>> list = new ArrayList<>();
        for (Map.Entry<String, Integer> entry :
                mRooms.entrySet()) {
            list.add(entry);
        }
        mMemberListAdapter.replaceData(list);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMemberListReceive(MemberListReceive ev) {
        toast(ev.Message);
        dismissDialog();
        if (ev.Status == 1) {
            mRooms = ev.RoomMemberInfo.Members;
            refresh();
        }
    }
}
