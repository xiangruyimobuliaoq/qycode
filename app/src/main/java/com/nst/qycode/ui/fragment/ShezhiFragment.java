package com.nst.qycode.ui.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nst.qycode.BaseFragment;
import com.nst.qycode.R;
import com.nst.qycode.model.BindCardReceive;
import com.nst.qycode.model.GameInfo;
import com.nst.qycode.model.GetBalanceReceive;
import com.nst.qycode.model.GetBalanceSend;
import com.nst.qycode.model.LogoutReceive;
import com.nst.qycode.model.LogoutSend;
import com.nst.qycode.ui.BindCardActivity;
import com.nst.qycode.ui.ChongzhiActivity;
import com.nst.qycode.ui.HistoryActivity;
import com.nst.qycode.ui.LoginActivity;
import com.nst.qycode.ui.TixianActivity;
import com.nst.qycode.ui.ZhanghuActivity;
import com.nst.qycode.ui.ZhuanzhangActivity;
import com.nst.qycode.util.ConsUtil;
import com.nst.qycode.util.SocketUtil;
import com.nst.qycode.view.Layout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
@Layout(layoutId = R.layout.fragment_shezhi)
public class ShezhiFragment extends BaseFragment {
    @BindView(R.id.zhanghuguanli)
    protected ImageView zhanghuguanli;
    @BindView(R.id.bindcard)
    protected ImageView bindcard;
    @BindView(R.id.zhuanzhang)
    protected ImageView zhuanzhang;
    @BindView(R.id.cunkuan)
    protected ImageView cunkuan;
    @BindView(R.id.qukuan)
    protected ImageView qukuan;
    @BindView(R.id.history)
    protected ImageView history;
    @BindView(R.id.tuichu)
    protected ImageView tuichu;
    @BindView(R.id.touxiangkuang)
    protected CircleImageView touxiangkuang;

    @BindView(R.id.total)
    protected TextView total;
    @BindView(R.id.main)
    protected TextView main;
    @BindView(R.id.saolei)
    protected TextView saolei;
    @BindView(R.id.niuniu)
    protected TextView niuniu;
    @BindView(R.id.jielong)
    protected TextView jielong;

    @Override
    protected void init() {
        touxiangkuang.setImageResource(getResources().getIdentifier("img" + ConsUtil.getIMG(), "mipmap", getActivity().getApplicationInfo().packageName));
        initListener();
    }

    private void initListener() {
        zhanghuguanli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overlay(ZhanghuActivity.class);
            }
        });
        bindcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overlay(BindCardActivity.class);
            }
        });
        zhuanzhang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overlay(ZhuanzhangActivity.class);
            }
        });
        cunkuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overlay(ChongzhiActivity.class);

            }
        });
        qukuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overlay(TixianActivity.class);
            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overlay(HistoryActivity.class);
            }
        });
        tuichu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    private void logout() {
        showDialog("处理中");
        LogoutSend send = new LogoutSend();
        send.ClientId = ConsUtil.getID();
        send.UserName = ConsUtil.getUsername();
        send.FuncName = ConsUtil.LOGOUT;
        SocketUtil.sendMsg(new Gson().toJson(send));
    }

    public void onGetBalanceReceive(GetBalanceReceive ev) {
        if (ev.Status == 1) {
            BigDecimal totalB = new BigDecimal(0);
            for (GameInfo info : ev.GameInfo) {
                switch (info.GID) {
                    case 1:
                        main.setText(info.Balance + "");
                        break;
                    case 2:
                        saolei.setText(info.Balance + "");
                        break;
                    case 3:
                        jielong.setText(info.Balance + "");
                        break;
                }
                totalB = totalB.add(new BigDecimal(info.Balance));
            }
            total.setText(totalB.setScale(2, RoundingMode.HALF_UP).doubleValue() + "");
        }
    }
}
