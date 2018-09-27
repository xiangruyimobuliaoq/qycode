package com.nst.qycode.ui.fragment;

import android.widget.TextView;

import com.nst.qycode.BaseFragment;
import com.nst.qycode.R;
import com.nst.qycode.view.Layout;

import butterknife.BindView;

/**
 * 创建者     彭龙
 * 创建时间   2018/7/10 上午9:51
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */


@Layout(layoutId = R.layout.fragment_wanfa)
public class WanfaFragment extends BaseFragment {
    @BindView(R.id.wanfa)
    protected TextView wanfa;

    @Override
    protected void init() {
        String text = "游戏规则:\n" + "发包者：选择发包金额　数量  雷号该红包中雷后，系统自动返还金额到个人账户，未取完剩余红包金额，将在五分钟内自动返还到账户。\n" + "\n" + "抢包者：房间内所显示红包都可抢，中雷后系统自动扣除个人账户金额\n" + "\n" + "游戏说明：选择发包埋雷，数字1-9为雷号，抢包者扫雷时，若尾数为该红包埋雷数字时，视为中雷，\n（例如：发包者埋雷数为6抢包者扫雷金额为8.16尾数为6时，视为中雷）";
        wanfa.setText(text);
    }
}
