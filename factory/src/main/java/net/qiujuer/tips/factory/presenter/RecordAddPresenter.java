package net.qiujuer.tips.factory.presenter;

import net.qiujuer.tips.factory.cache.Cache;
import net.qiujuer.tips.factory.model.db.RecordModel;
import net.qiujuer.tips.factory.view.RecordAddView;

import java.util.UUID;


public class RecordAddPresenter {
    private RecordAddView mView;

    public RecordAddPresenter(RecordAddView view) {
        mView = view;
    }

    public void create() {
        if (mView.getBrief() == null || mView.getBrief().length() <= 0) {
            mView.setStatus(-1);
        } else {
            RecordModel model = new RecordModel();
            model.setMark(UUID.randomUUID());
            model.setType(mView.getType());
            model.setBrief(mView.getBrief());
            model.setColor(mView.getColor());
            model.setDate(mView.getDate());
            model.setContact(mView.getContact());
            try {
                model.save();
                Cache.getInstance().add(model);
                mView.setStatus(model.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
