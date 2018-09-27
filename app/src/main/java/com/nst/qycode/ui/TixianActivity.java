package com.nst.qycode.ui;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.nst.qycode.BaseActivity;
import com.nst.qycode.R;
import com.nst.qycode.model.GetMemberCardReceive;
import com.nst.qycode.model.GetMemberCardSend;
import com.nst.qycode.model.MemberWithdrawReceive;
import com.nst.qycode.model.MemberWithdrawSend;
import com.nst.qycode.util.ConsUtil;
import com.nst.qycode.util.SocketUtil;
import com.nst.qycode.view.Layout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 创建者     彭龙
 * 创建时间   2018/7/18 下午2:31
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
@Layout(layoutId = R.layout.activity_tixian)
public class TixianActivity extends BaseActivity {
    @BindView(R.id.fanhui)
    protected ImageView fanhui;
    @BindView(R.id.username)
    protected TextView username;
    @BindView(R.id.zhenshixingming)
    protected EditText zhenshixingming;
    @BindView(R.id.kaihuhangmingcheng)
    protected TextView kaihuhangmingcheng;
    @BindView(R.id.tikuanjine)
    protected EditText tikuanjine;
    @BindView(R.id.yinhangkahao)
    protected TextView yinhangkahao;
    @BindView(R.id.tikuanmima)
    protected EditText tikuanmima;
    @BindView(R.id.submit)
    protected ImageButton submit;

    @BindView(R.id.select)
    protected TextView select;

    private String mZhenshixingming;
    private String mKaihuhangmingcheng;
    private String mTikuanjine;
    private String mTikuanmima;
    private String mYinhangkahao;

    @Override
    protected void init() {
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        username.setText(ConsUtil.getUsername());
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mZhenshixingming = zhenshixingming.getText().toString().trim();
                mKaihuhangmingcheng = kaihuhangmingcheng.getText().toString().trim();
                mTikuanjine = tikuanjine.getText().toString().trim();
                mTikuanmima = tikuanmima.getText().toString().trim();
                mYinhangkahao = yinhangkahao.getText().toString().trim();
                checkData();
                showDialog("处理中");
                MemberWithdrawSend send = new MemberWithdrawSend();
                send.ClientId = ConsUtil.getID();
                send.UserName = ConsUtil.getUsername();
                send.FuncName = ConsUtil.MEMBERWITHDRAW;
                send.MemberWithdrawModel.AccountNumber = mYinhangkahao;
                send.MemberWithdrawModel.BankName = mKaihuhangmingcheng;
                send.MemberWithdrawModel.TrueName = mZhenshixingming;
                send.MemberWithdrawModel.WithdrawPass = mTikuanmima;
                send.MemberWithdrawModel.WithdrawMoney = Double.parseDouble(mTikuanjine);
                SocketUtil.sendMsg(new Gson().toJson(send));
            }
        });

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog("处理中");
                GetMemberCard();
            }
        });
    }

    private void GetMemberCard() {
        GetMemberCardSend send = new GetMemberCardSend();
        send.ClientId = ConsUtil.getID();
        send.UserName = ConsUtil.getUsername();
        send.FuncName = ConsUtil.GETMEMBERCARD;
        SocketUtil.sendMsg(new Gson().toJson(send));
    }

    private void checkData() {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMemberWithdrawReceive(MemberWithdrawReceive ev) {
        toast(ev.Message);
        if (ev.Status == 1) {
            dismissDialog();
            finish();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMemberCardReceive(GetMemberCardReceive ev) {
        toast(ev.Message);
        if (ev.Status == 1) {
            List<String> list = new ArrayList<>();
            for (GetMemberCardReceive.MemberCardInfo info : ev.MemberCardInfo
                    ) {
                list.add(info.AccountNumber + "   " + info.BankName);
            }
            dismissDialog();
            if (list.size() == 0) {
                new MaterialDialog.Builder(this).title("提示")
                        .content("您还未绑定银行卡,是否前去绑定?")
                        .positiveText("确定前去")
                        .negativeText("稍后再说")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                overlay(BindCardActivity.class);
                            }
                        }).show();
            } else {
                showList(list);
            }
        }
    }

    private void showList(List<String> list) {
        new MaterialDialog.Builder(this)
                .title("请选择您的转账卡号")
                .items(list)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        if (TextUtils.isEmpty(text)) {
                            return true;
                        }
                        String[] split = text.toString().split("   ");
                        yinhangkahao.setText(split[0]);
                        kaihuhangmingcheng.setText(split[1]);
                        return true;
                    }
                })
                .positiveText("确定")
                .show();
    }
}
