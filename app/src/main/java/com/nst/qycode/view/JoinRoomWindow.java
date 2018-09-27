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
public class JoinRoomWindow extends BasePopupWindow {

    private final ImageButton createRoom;
    private final BaseActivity context;
    private final EditText roompass;

    public JoinRoomWindow(final BaseActivity context, final JoinRoomCallback callback) {
        super(context);
        this.callback = callback;
        this.context = context;
        roompass = (EditText) findViewById(R.id.roompass);
        createRoom = (ImageButton) findViewById(R.id.createRoom);


        createRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass = roompass.getText().toString().trim();
                if (TextUtils.isEmpty(pass)) {
                    ToastHelper.showToast("请输入房密码", context);
                    return;
                }
                if (null != callback) {
                    callback.onCreateRoom(pass);
                }
            }
        });
    }

    JoinRoomCallback callback;

    public interface JoinRoomCallback {
        void onCreateRoom(String currentState);
    }

    @Override
    public View onCreatePopupView() {
        return createPopupById(R.layout.window_joinroom);
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
