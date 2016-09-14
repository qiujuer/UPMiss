package net.qiujuer.tips.factory.presenter;


import net.qiujuer.tips.factory.cache.Cache;
import net.qiujuer.tips.factory.model.db.ModelStatus;
import net.qiujuer.tips.factory.model.db.RecordModel;
import net.qiujuer.tips.factory.view.RecordEditView;

public class RecordEditPresenter {
    private RecordEditView mView;
    private RecordModel mModel;

    public RecordEditPresenter(RecordEditView view) {
        mView = view;
    }

    public boolean refresh() {
        mModel = RecordModel.get(mView.getId());
        if (mModel != null) {
            mView.setType(mModel.getType());
            mView.setBrief(mModel.getBrief());
            mView.setDate(mModel.getDateCalender());
            mView.setColor(mModel.getColor());
            if (mModel.getContact() != null) {
                mView.setContactName(mModel.getContact().getName());
            }
            return true;
        }
        return false;
    }

    public void save() {
        if (mView.getBrief() == null || mView.getBrief().length() <= 0) {
            mView.setStatus(-1);
        } else {
            mModel.setType(mView.getType());
            mModel.setBrief(mView.getBrief());
            mModel.setColor(mView.getColor());
            mModel.setDate(mView.getDate());
            mModel.setContact(mView.getContact());

            if (!mModel.isAdd()) {
                mModel.setStatus(ModelStatus.STATUS_EDIT);
            }

            try {
                mModel.save();
                Cache.getInstance().edit(mModel);
                mView.setStatus(mModel.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
