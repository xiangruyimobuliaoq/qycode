package com.nst.qycode.ui.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nst.qycode.BaseFragment;
import com.nst.qycode.R;
import com.nst.qycode.model.GameInfo;
import com.nst.qycode.model.GetBalanceReceive;
import com.nst.qycode.model.GetBalanceSend;
import com.nst.qycode.model.GetNotifyReceive;
import com.nst.qycode.ui.RoomListActivity;
import com.nst.qycode.util.ConsUtil;
import com.nst.qycode.util.SocketUtil;
import com.nst.qycode.view.Layout;

import java.math.BigDecimal;
import java.math.RoundingMode;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 创建者     彭龙
 * 创建时间   2018/7/10 上午9:51
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
@Layout(layoutId = R.layout.fragment_dating)
public class DatingFragment extends BaseFragment {
    @BindView(R.id.saolei)
    protected ImageView saolei;
    @BindView(R.id.jielong)
    protected ImageView jielong;
    @BindView(R.id.niuniu)
    protected ImageView niuniu;
    @BindView(R.id.refresh)
    protected ImageView refresh;
    @BindView(R.id.username)
    protected TextView username;
    @BindView(R.id.total)
    protected TextView total;
    @BindView(R.id.notify)
    protected TextView notify;
    @BindView(R.id.img)
    protected CircleImageView img;

    @Override
    protected void init() {
        img.setImageResource(getResources().getIdentifier("img" + ConsUtil.getIMG(), "mipmap", getActivity().getApplicationInfo().packageName));
        username.setText(ConsUtil.getUsername());
        saolei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overlay(RoomListActivity.class);
            }
        });
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBalance();
            }
        });
        jielong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("即将推出");
            }
        });
        niuniu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("即将推出");
            }
        });
    }

    private void getBalance() {
        showDialog("处理中");
        GetBalanceSend getBalanceSend = new GetBalanceSend();
        getBalanceSend.ClientId = ConsUtil.getID();
        getBalanceSend.UserName = ConsUtil.getUsername();
        getBalanceSend.FuncName = ConsUtil.GETBALANCE;
        SocketUtil.sendMsg(new Gson().toJson(getBalanceSend));
    }

    public void onGetBalanceReceive(GetBalanceReceive ev) {
        dismissDialog();
        if (ev.Status == 1) {
            BigDecimal totalB = new BigDecimal(0);
            for (GameInfo info : ev.GameInfo) {
                totalB = totalB.add(new BigDecimal(info.Balance));
            }
            total.setText(totalB.setScale(2, RoundingMode.HALF_UP).doubleValue() + "");
        }
    }

    public void onGetNotifyReceive(GetNotifyReceive ev) {
        String data = "";
        for (String s : ev.NotifyInfo) {
            data += s + "                                ";
        }
        notify.setText(data);
    }
}
