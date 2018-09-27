package com.nst.qycode.view;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.RadioGroup;

import com.nst.qycode.BaseActivity;
import com.nst.qycode.R;

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
public class EmojWindow extends BasePopupWindow {

    BaseActivity context;

    EmojCallback callback;
    private final FlowRadioGroup mRg;

    public interface EmojCallback {
        void onHistorySubmit(int checkedRadioButtonIndex, String checkedRadioButtonText);
    }


    public EmojWindow(final BaseActivity context, final EmojCallback callback) {
        super(context);
        this.context = context;
        this.callback = callback;
        mRg = (FlowRadioGroup) findViewById(R.id.rg);
        mRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (null != callback) {
                    callback.onHistorySubmit(mRg.getCheckedRadioButtonIndex(), mRg.getCheckedRadioButtonText());
                }
            }
        });
    }

    @Override
    protected Animation initShowAnimation() {
        AnimationSet set = new AnimationSet(false);
        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, RELATIVE_TO_SELF, 0
                , RELATIVE_TO_SELF, 1, RELATIVE_TO_SELF, 0);
        translateAnimation.setDuration(200);
        set.addAnimation(translateAnimation);
        return set;
    }

    @Override
    protected Animation initExitAnimation() {
        AnimationSet set = new AnimationSet(false);
        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, RELATIVE_TO_SELF, 0
                , RELATIVE_TO_SELF, 0, RELATIVE_TO_SELF, 1);
        translateAnimation.setDuration(200);
        set.addAnimation(translateAnimation);
        return set;
    }

    @Override
    public View getClickToDismissView() {
        return findViewById(R.id.bg);
    }

    @Override
    public View onCreatePopupView() {
        return createPopupById(R.layout.window_emoj);
    }

    @Override
    public View initAnimaView() {
        return findViewById(R.id.pop_anima);
    }
}
