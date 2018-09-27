package com.nst.qycode.ui;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.nst.qycode.BaseActivity;
import com.nst.qycode.R;
import com.nst.qycode.model.AlipayDepositReceive;
import com.nst.qycode.model.AlipayDepositSend;
import com.nst.qycode.model.GetBalanceReceive;
import com.nst.qycode.model.GetMemberCardReceive;
import com.nst.qycode.model.GetMemberCardSend;
import com.nst.qycode.model.GetSystemCardReceive;
import com.nst.qycode.model.GetSystemCardSend;
import com.nst.qycode.model.MemberDepositReceive;
import com.nst.qycode.model.MemberDepositSend;
import com.nst.qycode.model.MemberWithdrawReceive;
import com.nst.qycode.model.MemberWithdrawSend;
import com.nst.qycode.util.ConsUtil;
import com.nst.qycode.util.NumberUtil;
import com.nst.qycode.util.SocketUtil;
import com.nst.qycode.util.UIUtil;
import com.nst.qycode.view.AlipayWindow;
import com.nst.qycode.view.Layout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
@Layout(layoutId = R.layout.activity_chongzhi)
public class ChongzhiActivity extends BaseActivity {
    @BindView(R.id.fanhui)
    protected ImageView fanhui;
    @BindView(R.id.BankName)
    protected TextView BankName;
    @BindView(R.id.AccountName)
    protected TextView AccountName;
    @BindView(R.id.AccountNumber)
    protected TextView AccountNumber;
    @BindView(R.id.select)
    protected TextView select;
    @BindView(R.id.huikuanjine)
    protected EditText huikuanjine;
    @BindView(R.id.yinhangkahao)
    protected TextView yinhangkahao;
    @BindView(R.id.submit)
    protected ImageButton submit;
    @BindView(R.id.rg)
    protected RadioGroup rg;
    @BindView(R.id.cardpart)
    protected LinearLayout cardpart;

    private String mHuikuanjine;
    private String mYinhangkahao;
    private String type = "银行卡";
    private GetSystemCardReceive.SystemCardInfo mInfo;
    private double mMoney;

    @Override
    protected void init() {
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.yinhangka:
                        cardpart.setVisibility(View.VISIBLE);
                        type = "银行卡";
                        break;
                    case R.id.zhifubao:
                        cardpart.setVisibility(View.GONE);
                        type = "支付宝";
                        break;
                }
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHuikuanjine = huikuanjine.getText().toString().trim();
                if (TextUtils.isEmpty(mHuikuanjine)) {
                    toast("请输入存款金额");
                    return;
                }
                showDialog("处理中");
                if (type.equals("银行卡")) {
                    mYinhangkahao = yinhangkahao.getText().toString().trim();
                    checkData();
                    MemberDepositSend send = new MemberDepositSend();
                    send.ClientId = ConsUtil.getID();
                    send.UserName = ConsUtil.getUsername();
                    send.FuncName = ConsUtil.MEMBERDEPOSIT;
                    send.MemberDepositModel.DepositAccount = mYinhangkahao;
                    send.MemberDepositModel.DepositMoney = TextUtils.isEmpty(mHuikuanjine) ? 0 : Double.parseDouble(mHuikuanjine);
                    send.MemberDepositModel.SystemCardInfo = mInfo;
                    SocketUtil.sendMsg(new Gson().toJson(send));
                } else {
                    mMoney = Double.parseDouble(ConsUtil.doubleToString(Double.parseDouble(mHuikuanjine) + Math.random()));
                    AlipayDepositSend send = new AlipayDepositSend();
                    send.ClientId = ConsUtil.getID();
                    send.UserName = ConsUtil.getUsername();
                    send.FuncName = ConsUtil.ALIPAYDEPOSIT;
                    send.MemberDepositModel.DepositMoney = mMoney;
                    SocketUtil.sendMsg(new Gson().toJson(send));
                }
            }
        });
        getSystemCard();
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

    private void getSystemCard() {
        GetSystemCardSend send = new GetSystemCardSend();
        send.ClientId = ConsUtil.getID();
        send.UserName = ConsUtil.getUsername();
        send.FuncName = ConsUtil.GETSYSTEMCARD;
        SocketUtil.sendMsg(new Gson().toJson(send));
    }

    private void checkData() {
        if (null == mInfo) {
            toast("读取系统银行卡信息失败,请重试...");
            return;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAlipayDepositReceive(AlipayDepositReceive ev) {
        toast(ev.Message);
        if (ev.Status == 1) {
            dismissDialog();
            Bitmap bitmap = UIUtil.base64ToBitmap(ev.AccountImage);
            new AlipayWindow(this, mMoney, bitmap).showPopupWindow();

//            finish();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMemberDepositReceive(MemberDepositReceive ev) {
        toast(ev.Message);
        if (ev.Status == 1) {
            dismissDialog();
            finish();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetSystemCardReceive(GetSystemCardReceive ev) {
        toast(ev.Message);
        if (ev.Status == 1) {
            mInfo = ev.SystemCardInfo;
            BankName.setText("系统开户行名称: " + ev.SystemCardInfo.BankName);
            AccountName.setText("系统收款人姓名: " + ev.SystemCardInfo.AccountName);
            AccountNumber.setText("系统收款银行卡号: " + ev.SystemCardInfo.AccountNumber);
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
                        return true;
                    }
                })
                .positiveText("确定")
                .show();
    }
}
