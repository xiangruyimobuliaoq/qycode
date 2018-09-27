package com.nst.qycode.view;

import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nst.qycode.BaseActivity;
import com.nst.qycode.R;
import com.nst.qycode.util.InputUtil;

import razerdp.basepopup.BasePopupWindow;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

/**
 * 创建者     彭龙
 * 创建时间   2018/7/25 下午7:11
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class SendRedPacketWindow extends BasePopupWindow {
    private final EditText change;
    private final EditText leihao;
    private final ImageButton submit;
    BaseActivity context;
    SendRedPacketCallback callback;

    public interface SendRedPacketCallback {
        void onZhuanzhang(double money, int number);
    }


    public SendRedPacketWindow(final BaseActivity context, final SendRedPacketCallback callback) {
        super(context);
        this.callback = callback;
        this.context = context;
        change = (EditText) findViewById(R.id.change);
        leihao = (EditText) findViewById(R.id.leihao);
        InputUtil.setMoneyFilter(change);
        submit = (ImageButton) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String trim = change.getText().toString().trim();
                String trim1 = leihao.getText().toString().trim();
                if (TextUtils.isEmpty(trim)) {
                    context.toast("请输入金额");
                    return;
                }
                if (TextUtils.isEmpty(trim1)) {
                    context.toast("请输入雷号");
                    return;
                }
                if (trim1.equals("0")) {
                    context.toast("雷号不能为0");
                    return;
                }
                if (callback != null) {
                    double money = Double.parseDouble(trim);
                    callback.onZhuanzhang(money, Integer.parseInt(trim1));
                }
            }
        });
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
        return createPopupById(R.layout.window_sendredpacket);
    }

    @Override
    public View initAnimaView() {
        return findViewById(R.id.pop_anima);
    }
}
