package com.nst.qycode.view;

import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.nst.qycode.BaseActivity;
import com.nst.qycode.R;
import com.nst.qycode.util.InputUtil;
import com.nst.qycode.util.ToastHelper;

import razerdp.basepopup.BasePopupWindow;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

/**
 * 创建者     彭龙
 * 创建时间   2018/7/20 上午9:43
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class CreateRoomWindow extends BasePopupWindow {

    private static final String STATE_DANBEI = "单倍";
    private static final String STATE_SHUANGBEI = "双倍";
    private final EditText roomname;
    private final ImageButton createRoom;
    private final BaseActivity context;
    private final RadioGroup pwd;
    private final RadioGroup type;
    private final LinearLayout pwdpart;
    private final EditText roompass;
    private String currentState = STATE_DANBEI;
    private boolean isPrivate;

    public CreateRoomWindow(final BaseActivity context, final CreateRoomCallback callback) {
        super(context);
        this.callback = callback;
        this.context = context;
        pwd = (RadioGroup) findViewById(R.id.rg_pwd);
        type = (RadioGroup) findViewById(R.id.rg_type);
        roomname = (EditText) findViewById(R.id.roomname);
        roompass = (EditText) findViewById(R.id.roompass);
        createRoom = (ImageButton) findViewById(R.id.createRoom);
        pwdpart = (LinearLayout) findViewById(R.id.pwdpart);
        InputUtil.setEtFilter(roomname);
        pwd.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.putong:
                        isPrivate = false;
                        pwdpart.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.mima:
                        pwdpart.setVisibility(View.VISIBLE);
                        isPrivate = true;
                        break;
                }
            }
        });

        type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.danbei:
                        currentState = STATE_DANBEI;
                        break;
                    case R.id.shuangbei:
                        currentState = STATE_SHUANGBEI;
                        break;
                }
            }
        });

        createRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = roomname.getText().toString().trim();
                String pass = roompass.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    ToastHelper.showToast("请输入房间名", context);
                    return;
                }
                if (isPrivate) {
                    if (TextUtils.isEmpty(pass)) {
                    ToastHelper.showToast("请输入房密码", context);
                    return;
                    }
                }
                if (null != callback) {
                    callback.onCreateRoom(currentState, name,pass,isPrivate);
                }
            }
        });
    }

    CreateRoomCallback callback;

    public interface CreateRoomCallback {
        void onCreateRoom(String currentState, String type, String name, boolean isPrivate);
    }

    @Override
    public View onCreatePopupView() {
        return createPopupById(R.layout.window_createroom);
    }

    @Override
    public View initAnimaView() {
        return findViewById(R.id.pop_anima);
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
        return findViewById(R.id.fanhui);
    }
}
