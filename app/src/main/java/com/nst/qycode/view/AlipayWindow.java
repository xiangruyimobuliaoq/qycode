package com.nst.qycode.view;

import android.graphics.Bitmap;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.nst.qycode.BaseActivity;
import com.nst.qycode.R;

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
public class AlipayWindow extends BasePopupWindow {
    private final double money;
    private final Bitmap bitmap;
    BaseActivity context;


    public AlipayWindow(final BaseActivity context, double money, Bitmap bitmap) {
        super(context);
        this.context = context;
        this.money = money;
        this.bitmap = bitmap;

        TextView mon = (TextView) findViewById(R.id.money);
        ImageView iv = (ImageView) findViewById(R.id.iv);
        mon.setText(money + "");
        iv.setImageBitmap(bitmap);

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
        return createPopupById(R.layout.window_alipay);
    }

    @Override
    public View initAnimaView() {
        return findViewById(R.id.pop_anima);
    }
}
