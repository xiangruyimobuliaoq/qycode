package com.nst.qycode.ui;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nst.qycode.BaseActivity;
import com.nst.qycode.R;
import com.nst.qycode.model.GameInfo;
import com.nst.qycode.model.GetBalanceReceive;
import com.nst.qycode.model.GetBalanceSend;
import com.nst.qycode.model.MemberTransferReceive;
import com.nst.qycode.model.MemberTransferSend;
import com.nst.qycode.util.ConsUtil;
import com.nst.qycode.util.SocketUtil;
import com.nst.qycode.view.Layout;
import com.nst.qycode.view.ZhuanzhangWindow;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.math.RoundingMode;

import butterknife.BindView;

/**
 * 创建者     彭龙
 * 创建时间   2018/7/25 下午3:28
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
@Layout(layoutId = R.layout.activity_zhuanzhang)
public class ZhuanzhangActivity extends BaseActivity {
    @BindView(R.id.fanhui)
    protected ImageView fanhui;
    @BindView(R.id.zongzhanghu)
    protected TextView zongzhanghu;
    @BindView(R.id.zongjine)
    protected TextView zongjine;
    @BindView(R.id.saolei)
    protected TextView saolei;
    @BindView(R.id.jielong)
    protected TextView jielong;
    @BindView(R.id.niuniu)
    protected TextView niuniu;
    @BindView(R.id.saolei_zhuanru)
    protected RelativeLayout saolei_zhuanru;
    @BindView(R.id.saolei_zhuanchu)
    protected RelativeLayout saolei_zhuanchu;
    @BindView(R.id.jielong_zhuanru)
    protected RelativeLayout jielong_zhuanru;
    @BindView(R.id.jielong_zhuanchu)
    protected RelativeLayout jielong_zhuanchu;
    @BindView(R.id.niuniu_zhuanru)
    protected RelativeLayout niuniu_zhuanru;
    @BindView(R.id.niuniu_zhuanchu)
    protected RelativeLayout niuniu_zhuanchu;
    private ZhuanzhangWindow mWindow;

    @Override
    protected void init() {
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getBalance();
        initListener();
    }

    private void initListener() {
        saolei_zhuanru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWindow(ZhuanzhangWindow.Type.saoleizhuanru, saolei.getText().toString().trim());
            }
        });
        saolei_zhuanchu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWindow(ZhuanzhangWindow.Type.saoleizhuanchu, saolei.getText().toString().trim());

            }
        });
        jielong_zhuanru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWindow(ZhuanzhangWindow.Type.jielongzhuanru, jielong.getText().toString().trim());

            }
        });
        jielong_zhuanchu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWindow(ZhuanzhangWindow.Type.jielongzhuanchu, jielong.getText().toString().trim());

            }
        });
        niuniu_zhuanru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        niuniu_zhuanchu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void showWindow(ZhuanzhangWindow.Type type, String current) {
        mWindow = new ZhuanzhangWindow(this, type, zongzhanghu.getText().toString().trim(), current, new ZhuanzhangWindow.ZhuanzhangCallback() {
            @Override
            public void onZhuanzhang(double money, ZhuanzhangWindow.Type type) {
                showDialog("处理中");
                MemberTransferSend send = new MemberTransferSend();
                send.ClientId = ConsUtil.getID();
                send.UserName = ConsUtil.getUsername();
                send.FuncName = ConsUtil.MEMBERTRANSFER;
                send.MemberTransferModel.TransferMoney = money;
                switch (type.name()) {
                    case "saoleizhuanru":
                        send.MemberTransferModel.FromGame = 1;
                        send.MemberTransferModel.ToGame = 2;
                        break;
                    case "saoleizhuanchu":
                        send.MemberTransferModel.FromGame = 2;
                        send.MemberTransferModel.ToGame = 1;
                        break;
                    case "jielongzhuanru":
                        send.MemberTransferModel.FromGame = 1;
                        send.MemberTransferModel.ToGame = 3;
                        break;
                    case "jielongzhuanchu":
                        send.MemberTransferModel.FromGame = 3;
                        send.MemberTransferModel.ToGame = 1;
                        break;
                    case "niuniuzhuanru":
                        break;
                    case "niuniuzhuanchu":
                        break;
                }
                SocketUtil.sendMsg(new Gson().toJson(send));
            }
        });
        mWindow.showPopupWindow();
    }

    private void getBalance() {
        GetBalanceSend getBalanceSend = new GetBalanceSend();
        getBalanceSend.ClientId = ConsUtil.getID();
        getBalanceSend.UserName = ConsUtil.getUsername();
        getBalanceSend.FuncName = ConsUtil.GETBALANCE;
        SocketUtil.sendMsg(new Gson().toJson(getBalanceSend));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetBalanceReceive(GetBalanceReceive ev) {
        if (ev.Status == 1) {
            BigDecimal totalB = new BigDecimal(0);
            for (GameInfo info : ev.GameInfo) {
                switch (info.GID) {
                    case 1:
                        zongzhanghu.setText(info.Balance + "");
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
            zongjine.setText(totalB.setScale(2, RoundingMode.HALF_UP).doubleValue() + "");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMemberTransferReceive(MemberTransferReceive ev) {
        dismissDialog();
        if (ev.Status == 1) {
            getBalance();
            if (null != mWindow && mWindow.isShowing()) {
                mWindow.dismiss();
            }
        }
    }
}
