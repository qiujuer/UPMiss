package net.qiujuer.tips.factory.presenter;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;
import net.qiujuer.tips.factory.R;
import net.qiujuer.tips.factory.service.IMissServiceInterface;
import net.qiujuer.tips.factory.service.MissBinder;
import net.qiujuer.tips.factory.service.MissBinderInterface;
import net.qiujuer.tips.factory.service.MissService;
import net.qiujuer.tips.factory.view.SyncView;

public class SyncPresenter extends BroadcastReceiver {
    private SyncView mView;

    public SyncPresenter(SyncView view) {
        mView = view;
        try {
            IntentFilter filter = new IntentFilter();
            filter.addAction(MissService.ACTION_MISS_SYNC);
            mView.getContext().registerReceiver(this, filter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void destroy() {
        if (mView != null) {
            try {
                mView.getContext().unregisterReceiver(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
            mView = null;
        }
    }

    private void setEnd(final int status) {
        final SyncView view = mView;
        if (view == null)
            return;

        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                try {
                    view.syncStop(status);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void sync() {
        SyncView view = mView;
        if (view == null)
            return;

        if (!AppPresenter.isHaveNetwork()) {
            view.syncStop(R.string.status_network_unlink);
            return;
        }

        if (!AppPresenter.isLogin()) {
            view.syncStop(R.string.status_account_login_need);
            return;
        }

        view.syncStart();

        MissBinderInterface serviceInterface = AppPresenter.getService();
        if (serviceInterface == null)
            return;

        try {
            serviceInterface.sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (MissService.ACTION_MISS_SYNC.equals(action)) {
            int status = intent.getIntExtra(MissService.ACTION_MISS_SYNC_VALUE_STATUS, 0);
            setEnd(status);
        }
    }
}
