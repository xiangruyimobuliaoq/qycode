package com.nst.qycode;

import com.nst.qycode.ui.LoginActivity;
import com.nst.qycode.ui.MainActivity;
import com.nst.qycode.util.ConsUtil;
import com.nst.qycode.view.Layout;

/**
 * 创建者     彭龙
 * 创建时间   2018/8/10 上午9:17
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
@Layout(layoutId = R.layout.activity_root)
public class RootActivity extends LaunchActivity {
    @Override
    protected void init() {
        if (ConsUtil.getLogin()) {
            forward(MainActivity.class);
        } else {
            forward(LoginActivity.class);
        }
    }
}
