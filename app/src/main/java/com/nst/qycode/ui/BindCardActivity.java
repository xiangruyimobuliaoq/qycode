package com.nst.qycode.ui;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nst.qycode.BaseActivity;
import com.nst.qycode.R;
import com.nst.qycode.model.BindCardReceive;
import com.nst.qycode.model.BindCardSend;
import com.nst.qycode.model.MemberAccountReceive;
import com.nst.qycode.model.MemberAccountSend;
import com.nst.qycode.util.ConsUtil;
import com.nst.qycode.util.SocketUtil;
import com.nst.qycode.view.Layout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
@Layout(layoutId = R.layout.activity_bindcard)
public class BindCardActivity extends BaseActivity {
    @BindView(R.id.fanhui)
    protected ImageView fanhui;
    @BindView(R.id.username)
    protected TextView username;
    @BindView(R.id.phonenum)
    protected EditText phonenum;
    @BindView(R.id.pwd)
    protected EditText pwd;
    @BindView(R.id.submit)
    protected ImageButton submit;
    private String mPhone;
    private String mPassword;

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
                mPhone = phonenum.getText().toString().trim();
                mPassword = pwd.getText().toString().trim();
                checkData();
                showDialog("处理中");
                BindCardSend send = new BindCardSend();
                send.ClientId = ConsUtil.getID();
                send.UserName = ConsUtil.getUsername();
                send.FuncName = ConsUtil.BANKCARD;
                send.BankCardModel.AccountNumber = mPassword;
                send.BankCardModel.BankName = mPhone;
                SocketUtil.sendMsg(new Gson().toJson(send));
            }
        });
    }

    private void checkData() {
        if (TextUtils.isEmpty(mPhone) && TextUtils.isEmpty(mPassword)) {
            toast("请输入要修改的内容");
            return;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBindCardReceive(BindCardReceive ev) {
        toast(ev.Message);
        if (ev.Status == 1) {
            dismissDialog();
            finish();
        }
    }
}
