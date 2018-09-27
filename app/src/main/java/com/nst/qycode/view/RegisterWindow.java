package com.nst.qycode.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;

import com.nst.qycode.R;
import com.nst.qycode.util.InputUtil;
import com.nst.qycode.util.ToastHelper;

import razerdp.basepopup.BasePopupWindow;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

/**
 * 创建者     彭龙
 * 创建时间   2018/7/11 下午2:02
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class RegisterWindow extends BasePopupWindow {

    private final EditText mPhonenum;
    private final EditText mUsername;
    private final EditText mRealname;
    private final EditText mPassword;
    private final EditText mPassword2;
    private final Button submit;
    private final EditText AgentCode;
    Context context;

    RegisterCallback callback;

    public interface RegisterCallback {
        void onRegisterSubmit(String phonenum, String username, String realname, String password, String agentcode);
    }

    public RegisterWindow(final Context context, final RegisterCallback callback) {
        super(context);
        this.context = context;
        this.callback = callback;
        mPhonenum = (EditText) findViewById(R.id.phonenum);
        mUsername = (EditText) findViewById(R.id.username);
        mRealname = (EditText) findViewById(R.id.realname);
        mPassword = (EditText) findViewById(R.id.password);
        mPassword2 = (EditText) findViewById(R.id.password2);
        AgentCode = (EditText) findViewById(R.id.AgentCode);
        submit = (Button) findViewById(R.id.submit);
        InputUtil.setEtFilter(mPhonenum);
        InputUtil.setEtFilter(mUsername);
        InputUtil.setEtFilter(mRealname);
        InputUtil.setEtFilter(mPassword);
        InputUtil.setEtFilter(mPassword2);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phonenum = mPhonenum.getText().toString().trim();
                String username = mUsername.getText().toString().trim();
                String realname = mRealname.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String password2 = mPassword2.getText().toString().trim();
                String agentcode = AgentCode.getText().toString().trim();
                if (checkInfo(phonenum, username, realname, password, password2)) {
                    if (null != callback)
                        callback.onRegisterSubmit(phonenum, username, realname, password, agentcode);
                }
            }
        });
    }

    private boolean checkInfo(String phonenum, String username, String realname, String password, String password2) {
        if (TextUtils.isEmpty(phonenum) || phonenum.length() != 11) {
            ToastHelper.showToast("手机号码不正确", context);
            return false;
        }
        if (TextUtils.isEmpty(username) || username.length() < 6) {
            ToastHelper.showToast("用户名不能小于6位", context);
            return false;
        }
        if (TextUtils.isEmpty(realname)) {
            ToastHelper.showToast("姓名不能为空", context);
            return false;
        }
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            ToastHelper.showToast("密码不能小于6位", context);
            return false;
        }
        if (!password.equals(password2)) {
            ToastHelper.showToast("两次密码输入不同", context);
            return false;
        }
        return true;
    }

    @Override
    protected Animation initShowAnimation() {
        AnimationSet set = new AnimationSet(false);
        Animation scaleAnimation = new ScaleAnimation(0, 1, 0, 1, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(200);
        set.addAnimation(getDefaultAlphaAnimation());
        set.addAnimation(scaleAnimation);
        return set;
    }

    @Override
    public View getClickToDismissView() {
        return findViewById(R.id.cancel);
    }

    @Override
    public View onCreatePopupView() {
        return createPopupById(R.layout.window_regist);
    }

    @Override
    public View initAnimaView() {
        return findViewById(R.id.pop_anima);
    }
}
