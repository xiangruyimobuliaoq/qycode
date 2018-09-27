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
public class ZhuanzhangWindow extends BasePopupWindow {
    private final LinearLayout pop_anima;
    private final TextView zongzhanghu;
    private final TextView current;
    private final EditText change;
    private final ImageButton submit;
    private final TextView text;
    BaseActivity context;
    ZhuanzhangCallback callback;

    public interface ZhuanzhangCallback {
        void onZhuanzhang(double money, Type type);
    }

    public enum Type {
        saoleizhuanru, saoleizhuanchu, jielongzhuanru, jielongzhuanchu, niuniuzhuanru, niuniuzhuanchu
    }

    public ZhuanzhangWindow(final BaseActivity context, final Type type, String zong, String curr, final ZhuanzhangCallback callback) {
        super(context);
        this.callback = callback;
        this.context = context;
        pop_anima = (LinearLayout) findViewById(R.id.pop_anima);
        zongzhanghu = (TextView) findViewById(R.id.zongzhanghu);
        zongzhanghu.setText(zong);
        current = (TextView) findViewById(R.id.current);
        current.setText(curr);
        text = (TextView) findViewById(R.id.text);
        change = (EditText) findViewById(R.id.change);
        InputUtil.setMoneyFilter(change);
        submit = (ImageButton) findViewById(R.id.submit);
        int resID = -1;
        switch (type.name()) {
            case "saoleizhuanru":
                resID = R.mipmap.saolei_zhuanru;
                text.setText("转入金额:");
                break;
            case "saoleizhuanchu":
                resID = R.mipmap.saolei_zhuanchu;
                text.setText("转出金额:");
                break;
            case "jielongzhuanru":
                resID = R.mipmap.jielong_zhuanru;
                text.setText("转入金额:");
                break;
            case "jielongzhuanchu":
                resID = R.mipmap.jielong_zhuanchu;
                text.setText("转出金额:");
                break;
            case "niuniuzhuanru":
                resID = R.mipmap.niuniu_zhuanru;
                text.setText("转入金额:");
                break;
            case "niuniuzhuanchu":
                resID = R.mipmap.niuniu_zhuanchu;
                text.setText("转出金额:");
                break;
        }
        pop_anima.setBackgroundResource(resID);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String trim = change.getText().toString().trim();
                if (TextUtils.isEmpty(trim)) {
                    trim = "0";
                }
                double money = Double.parseDouble(trim);
                if (money == 0) {
                    context.toast("请输入金额");
                }
                if (callback != null) {
                    callback.onZhuanzhang(money, type);
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
        return createPopupById(R.layout.window_zhuanzhang);
    }

    @Override
    public View initAnimaView() {
        return findViewById(R.id.pop_anima);
    }
}
