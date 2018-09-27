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
@Layout(layoutId = R.layout.activity_zhanghu)
public class ZhanghuActivity extends BaseActivity {
    @BindView(R.id.fanhui)
    protected ImageView fanhui;
    @BindView(R.id.username)
    protected TextView username;
    @BindView(R.id.phonenum)
    protected EditText phonenum;
    @BindView(R.id.pwd)
    protected EditText pwd;
    @BindView(R.id.accountpwd)
    protected EditText accountpwd;
    @BindView(R.id.submit)
    protected ImageButton submit;
    private String mPhone;
    private String mPassword;
    private String mAccountpassword;

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
                mAccountpassword = accountpwd.getText().toString().trim();
                checkData();
                showDialog("处理中");
                MemberAccountSend send = new MemberAccountSend();
                send.ClientId = ConsUtil.getID();
                send.UserName = ConsUtil.getUsername();
                send.FuncName = ConsUtil.MEMBERACCOUNT;
                send.MemberAccountModel.Password = mPassword;
                send.MemberAccountModel.Tel = mPhone;
                send.MemberAccountModel.WithdrawPass = mAccountpassword;
                SocketUtil.sendMsg(new Gson().toJson(send));
            }
        });
    }

    private void checkData() {
        if (TextUtils.isEmpty(mPhone) && TextUtils.isEmpty(mPassword) && TextUtils.isEmpty(mAccountpassword)) {
            toast("请输入要修改的内容");
            return;
        }
        if (!TextUtils.isEmpty(mPhone) && mPhone.length() != 11) {
            toast("请输入正确的手机号码");
            return;
        }
        if (!TextUtils.isEmpty(mPassword) && mPassword.length() < 6) {
            toast("密码长度不能小于6位");
            return;
        }
        if (!TextUtils.isEmpty(mAccountpassword) && mAccountpassword.length() < 6) {
            toast("取款密码长度不能小于6位");
            return;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMemberAccountReceive(MemberAccountReceive ev) {
        toast(ev.Message);
        if (ev.Status == 1) {
            dismissDialog();
            finish();
        }
    }
}
