package net.qiujuer.tips.factory.presenter;

import net.qiujuer.tips.factory.cache.Cache;
import net.qiujuer.tips.factory.model.db.ContactModel;
import net.qiujuer.tips.factory.model.db.ModelStatus;
import net.qiujuer.tips.factory.view.ContactEditView;

public class ContactEditPresenter {
    private ContactEditView mView;
    private ContactModel mModel;

    public ContactEditPresenter(ContactEditView view) {
        mView = view;
    }

    public boolean refresh() {
        mModel = ContactModel.get(mView.getId());
        if (mModel != null) {
            mView.setNameStr(mModel.getName());
            mView.setPhoneNumber(mModel.getPhone());
            mView.setQQ(mModel.getQQNumber());
            mView.setGender(mModel.getSex());
            mView.setRelation(mModel.getRelation());
            return true;
        }
        return false;
    }

    public boolean save() {
        if (mView.getNameStr() == null || mView.getNameStr().length() <= 0) {
            mView.setStatus(-2);
        } else if (mView.getPhoneNumber() == null || mView.getPhoneNumber().length() <= 0) {
            mView.setStatus(-3);
        } else {
            mModel.setName(mView.getNameStr());
            mModel.setPhone(mView.getPhoneNumber());
            mModel.setQQNumber(mView.getQQ());
            mModel.setSex(mView.getGender());
            mModel.setRelation(mView.getRelation());

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
        return true;
    }
}
