package com.nst.qycode.view;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.nst.qycode.BaseActivity;
import com.nst.qycode.R;
import com.nst.qycode.model.RedPacketInfo;
import com.nst.qycode.model.RedPacketReceive;
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
public class OpenRedPacketWindow extends BasePopupWindow {
    private final ImageButton submit;
    private final TextView name;
    private final TextView info;
    private final TextView leihao;
    BaseActivity context;
    OpenRedPacketCallback callback;

    public interface OpenRedPacketCallback {

        void onZhuanzhang(RedPacketInfo item);
    }


    public OpenRedPacketWindow(final BaseActivity context, final RedPacketInfo item, final OpenRedPacketCallback callback) {
        super(context);
        this.callback = callback;
        this.context = context;
        name = (TextView) findViewById(R.id.name);
        info = (TextView) findViewById(R.id.info);
        leihao = (TextView) findViewById(R.id.leihao);
        name.setText(item.Creator);
        info.setText("红包金额: " + item.TotalMoney);
        leihao.setText("雷号: " + item.LandmineNo);
        submit = (ImageButton) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.onZhuanzhang(item);
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
        return createPopupById(R.layout.window_openredpacket);
    }

    @Override
    public View initAnimaView() {
        return findViewById(R.id.pop_anima);
    }
}
