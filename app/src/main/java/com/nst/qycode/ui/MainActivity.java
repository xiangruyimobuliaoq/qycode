package com.nst.qycode.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.nst.qycode.BaseActivity;
import com.nst.qycode.BaseFragment;
import com.nst.qycode.R;
import com.nst.qycode.model.ContinueGameReceive;
import com.nst.qycode.model.DataSend;
import com.nst.qycode.model.GetBalanceReceive;
import com.nst.qycode.model.GetBalanceSend;
import com.nst.qycode.model.GetNotifyReceive;
import com.nst.qycode.model.LogoutReceive;
import com.nst.qycode.ui.fragment.DatingFragment;
import com.nst.qycode.ui.fragment.ShezhiFragment;
import com.nst.qycode.ui.fragment.WanfaFragment;
import com.nst.qycode.util.ConsUtil;
import com.nst.qycode.util.SocketUtil;
import com.nst.qycode.view.Layout;
import com.nst.qycode.view.NoScrollViewPager;
import com.pgyersdk.update.DownloadFileListener;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;
import com.pgyersdk.update.javabean.AppBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

@Layout(layoutId = R.layout.activity_main)
public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {
    @BindView(R.id.rg_home)
    protected RadioGroup mPageSelector;
    @BindView(R.id.viewpager_home)
    protected NoScrollViewPager mFragmentContainer;
    @BindView(R.id.fab)
    protected ImageButton fab;

    private HomeFragmentAdapter mAdapter;
    private Map<Integer, BaseFragment> mCache = new HashMap<>();

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            moveTaskToBack(false);
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void init() {
//        getBalance();
        getNotify();
        initEvent();
        requestPermissions();
        update();
    }

    private void update() {
        new PgyUpdateManager.Builder()
                .setForced(false)                //设置是否强制更新,非自定义回调更新接口此方法有用
                .setUserCanRetry(true)         //失败后是否提示重新下载，非自定义下载 apk 回调此方法有用
                .setDeleteHistroyApk(false)     // 检查更新前是否删除本地历史 Apk
                .setUpdateManagerListener(new UpdateManagerListener() {
                    @Override
                    public void onNoUpdateAvailable() {
                        //没有更新是回调此方法
                        Log.d("pgyer", "there is no new version");
                    }

                    @Override
                    public void onUpdateAvailable(AppBean appBean) {
                        //没有更新是回调此方法
                        Log.d("pgyer", "there is new version can update"
                                + "new versionCode is " + appBean.getVersionCode());
                        //调用以下方法，DownloadFileListener 才有效；如果完全使用自己的下载方法，不需要设置DownloadFileListener
                        PgyUpdateManager.downLoadApk(appBean.getDownloadURL());
                    }

                    @Override
                    public void checkUpdateFailed(Exception e) {
                        //更新检测失败回调
                        Log.e("pgyer", "check update failed ", e);
                    }
                })
                //注意 ：下载方法调用 PgyUpdateManager.downLoadApk(appBean.getDownloadURL()); 此回调才有效
                .setDownloadFileListener(new DownloadFileListener() {   // 使用蒲公英提供的下载方法，这个接口才有效。
                    @Override
                    public void downloadFailed() {
                        //下载失败
                        Log.e("pgyer", "download apk failed");
                    }

                    @Override
                    public void downloadSuccessful(Uri uri) {
                        Log.e("pgyer", "download apk success");
                        PgyUpdateManager.installApk(uri);  // 使用蒲公英提供的安装方法提示用户 安装apk
                    }

                    @Override
                    public void onProgressUpdate(Integer... integers) {
                        Log.e("pgyer", "update download apk progress" + integers.toString());
                    }
                })
                .register();
    }

    private void getNotify() {
        DataSend send = new DataSend();
        send.ClientId = ConsUtil.getID();
        send.FuncName = ConsUtil.GETNOTIFY;
        send.UserName = ConsUtil.getUsername();
        SocketUtil.sendMsg(new Gson().toJson(send));
    }

    private void initEvent() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overlay(ChartActivity.class);
            }
        });
        mPageSelector.setOnCheckedChangeListener(this);
        mAdapter = new HomeFragmentAdapter(getSupportFragmentManager());
        mFragmentContainer.setAdapter(mAdapter);
        mFragmentContainer.setOnPageChangeListener(this);
        mFragmentContainer.setCurrentItem(1);
        mFragmentContainer.setOffscreenPageLimit(2);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            case R.id.wanfa:
                mFragmentContainer.setCurrentItem(0, false);
                break;
            case R.id.dating:
                mFragmentContainer.setCurrentItem(1, false);
                break;
            case R.id.geren:
                mFragmentContainer.setCurrentItem(2, false);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        int pageId = 0;
        switch (position) {
            case 0:
                pageId = R.id.wanfa;
                break;
            case 1:
                pageId = R.id.dating;
                break;
            case 2:
                pageId = R.id.geren;
                break;
        }
        mPageSelector.check(pageId);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public Fragment getFragment(int position) {
        //加上缓存功能,优先取缓存
        BaseFragment fragment = mCache.get(position);
        if (fragment != null) {
            return fragment;
        }
        switch (position) {
            case 0:
                fragment = new WanfaFragment();
                break;
            case 1:
                fragment = new DatingFragment();
                break;
            case 2:
                fragment = new ShezhiFragment();
                break;
        }
        mCache.put(position, fragment);
        return fragment;
    }


    class HomeFragmentAdapter extends FragmentStatePagerAdapter {

        public HomeFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            return getFragment(position);
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getBalance();
    }

    private void getBalance() {
        GetBalanceSend getBalanceSend = new GetBalanceSend();
        getBalanceSend.ClientId = ConsUtil.getID();
        getBalanceSend.UserName = ConsUtil.getUsername();
        getBalanceSend.FuncName = ConsUtil.GETBALANCE;
        SocketUtil.sendMsg(new Gson().toJson(getBalanceSend));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetBalanceReceive(GetBalanceReceive ev) {
        if (ev.Status == 1) {
//            getNotify();
            ShezhiFragment ShezhiFragment = (com.nst.qycode.ui.fragment.ShezhiFragment) mCache.get(2);
            if (null != ShezhiFragment) {
                ShezhiFragment.onGetBalanceReceive(ev);
            }
            DatingFragment DatingFragment = (DatingFragment) mCache.get(1);
            if (null != DatingFragment) {
                DatingFragment.onGetBalanceReceive(ev);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onGetNotifyReceive(GetNotifyReceive ev) {
        if (ev.Status == 1) {
            DatingFragment DatingFragment = (DatingFragment) mCache.get(1);
            if (null != DatingFragment) {
                DatingFragment.onGetNotifyReceive(ev);
//                DatingFragment stickyEvent = EventBus.getDefault().getStickyEvent(DatingFragment.class);
//                if (stickyEvent != null)
                EventBus.getDefault().removeStickyEvent(ev);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onContinueGameReceive(final ContinueGameReceive ev) {
        if (ev.Status == 1) {
            new MaterialDialog.Builder(this)
                    .title("提示")
                    .content("您当前还有未完成的游戏,是否进入房间继续?")
                    .positiveText("现在进入")
                    .negativeText("稍后再说")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(ConsUtil.CONTINUEGAME, ev);
                            overlay(RoomActivity.class, bundle);
                        }
                    }).show();
//            ContinueGameReceive stickyEvent = EventBus.getDefault().getStickyEvent(ContinueGameReceive.class);
//            if (stickyEvent != null)
            EventBus.getDefault().removeStickyEvent(ev);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogoutReceive(LogoutReceive ev) {
        dismissDialog();
        toast(ev.Message);
        if (ev.Status == 1) {
            ConsUtil.cleanID();
            ConsUtil.cleanUsername();
            ConsUtil.setLogin(false);
            forward(LoginActivity.class);
        }
    }
}
