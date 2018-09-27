package com.nst.qycode.view;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.TextView;

import com.nst.qycode.BaseActivity;
import com.nst.qycode.R;
import com.nst.qycode.model.RedPacketInfo;

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
public class ZhaleiWindow extends BasePopupWindow {
    BaseActivity context;


    public ZhaleiWindow(final BaseActivity context, int resId) {
        super(context);
        this.context = context;
        View view = findViewById(R.id.pop_anima);
        view.setBackgroundResource(resId);
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
        return createPopupById(R.layout.window_zhalei);
    }

    @Override
    public View initAnimaView() {
        return findViewById(R.id.pop_anima);
    }
}
