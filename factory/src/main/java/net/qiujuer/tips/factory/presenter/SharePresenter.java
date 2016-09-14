package net.qiujuer.tips.factory.presenter;


import net.qiujuer.tips.factory.model.Model;
import net.qiujuer.tips.open.Share;
import net.qiujuer.tips.factory.view.ShareView;

import java.util.UUID;

public class SharePresenter {
    private ShareView mView;
    private String mTransaction;

    public SharePresenter(ShareView view) {
        mView = view;
        Share.init(Model.getApplication());
        mTransaction = UUID.randomUUID().toString();
    }

    public void share(int id) {
        Share.share(mTransaction, mView.getActivity(), id, mView.getShareTitle(), mView.getShareSummary(), mView.getBitmap());
    }
}
