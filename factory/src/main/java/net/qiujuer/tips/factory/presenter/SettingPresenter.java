package net.qiujuer.tips.factory.presenter;

import net.qiujuer.tips.factory.cache.Cache;
import net.qiujuer.tips.factory.model.xml.SettingModel;
import net.qiujuer.tips.factory.view.SettingView;


public class SettingPresenter {
    private SettingModel mModel;
    private SettingView mView;

    public SettingPresenter(SettingView view) {
        mView = view;
        mModel = new SettingModel();
    }

    public void refresh() {

        mView.setLeadTime(mModel.getLeadTime());
        mView.setColor(new int[]{mModel.getStartColor(), mModel.getEndColor()});

    }

    public void save() {
        int[] colors = mView.getColor();
        int leadTime = mView.getLeadTime();

        mModel.setStartColor(colors[0]);
        mModel.setEndColor(colors[1]);
        mModel.setLeadTime(leadTime);
        mModel.save();

        Cache.getInstance().refreshDesktop(leadTime);
    }
}
