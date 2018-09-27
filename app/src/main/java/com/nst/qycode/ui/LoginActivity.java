package com.nst.qycode.ui;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.nst.qycode.BaseActivity;
import com.nst.qycode.R;
import com.nst.qycode.model.LoginReceive;
import com.nst.qycode.model.LoginSend;
import com.nst.qycode.model.RegisterReceive;
import com.nst.qycode.model.RegisterSend;
import com.nst.qycode.util.ConsUtil;
import com.nst.qycode.util.InputUtil;
import com.nst.qycode.util.NumberUtil;
import com.nst.qycode.util.SocketUtil;
import com.nst.qycode.util.SpUtil;
import com.nst.qycode.view.Layout;
import com.nst.qycode.view.RegisterWindow;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Random;

import butterknife.BindView;

@Layout(layoutId = R.layout.activity_login)
public class LoginActivity extends BaseActivity implements RegisterWindow.RegisterCallback {

    @BindView(R.id.login)
    protected ImageButton login;
    @BindView(R.id.regist)
    protected ImageButton regist;
    @BindView(R.id.username)
    protected EditText username;
    @BindView(R.id.password)
    protected EditText password;
    @BindView(R.id.forgotpwd)
    protected ImageView forgotpwd;
    private RegisterWindow mWindow;
    private String mUsername;
    private String mPassword;

    @Override
    protected void init() {
        InputUtil.setEtFilter(username);
        InputUtil.setEtFilter(password);
        username.setText(ConsUtil.getUsername());
        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initRegistWindow();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUsername = username.getText().toString().trim();
                mPassword = password.getText().toString().trim();
                if (!checkData()) {
                    return;
                }
                LoginSend loginSend = new LoginSend();
                loginSend.FuncName = ConsUtil.LOGIN;
                loginSend.UserName = mUsername;
                loginSend.LoginModel.Password = mPassword;
                showDialog("处理中");
                SocketUtil.sendMsg(new Gson().toJson(loginSend));
            }
        });
        forgotpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NumberUtil.splitRedPacket(50, 10);
            }
        });
    }

    private boolean checkData() {
        if (TextUtils.isEmpty(mUsername) || TextUtils.isEmpty(mPassword)) {
            toast("用户名和密码不能为空");
            return false;
        }
        return true;
    }

    private void initRegistWindow() {
        if (null == mWindow) {
            mWindow = new RegisterWindow(this, this);
        }
        if (mWindow.isShowing()) {
            mWindow.dismiss();
        }
        mWindow.showPopupWindow();
    }

    @Override
    public void onRegisterSubmit(String phonenum, String username, String realname, String password, String agentcode) {
        RegisterSend registerSend = new RegisterSend();
        registerSend.FuncName = ConsUtil.REGISTER;
        registerSend.UserName = username;
        registerSend.RegisterModel.Password = password;
        registerSend.RegisterModel.Tel = phonenum;
        registerSend.RegisterModel.TrueName = realname;
        registerSend.RegisterModel.AgentCode = agentcode;
        registerSend.RegisterModel.HeaderImg = new Random().nextInt(5);
        showDialog("处理中");
        SocketUtil.sendMsg(new Gson().toJson(registerSend));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginEvent(LoginReceive ev) {
        toast(ev.Message);
        dismissDialog();
        if (ev.Status == 1) {
            SpUtil.putString(ConsUtil.USERINFO, ConsUtil.USERNAME, mUsername);
            ConsUtil.setLogin(true);
            forward(MainActivity.class);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRegisterEvent(RegisterReceive ev) {
        toast(ev.Message);
        dismissDialog();
        if (ev.Status == 1) {
            if (null != mWindow && mWindow.isShowing()) {
                mWindow.dismiss();
            }
        }
    }
}
