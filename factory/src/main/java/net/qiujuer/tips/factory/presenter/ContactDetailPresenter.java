package net.qiujuer.tips.factory.presenter;

import net.qiujuer.tips.factory.model.db.ContactModel;
import net.qiujuer.tips.factory.view.ContactDetailView;


public class ContactDetailPresenter {
    private ContactDetailView mView;
    private ContactModel mModel;

    public ContactDetailPresenter(ContactDetailView view) {
        mView = view;
    }

    public boolean refresh() {
        mModel = ContactModel.get(mView.getId());
        if (mModel != null) {
            mView.setNameStr(mModel.getName());
            mView.setPhoneNumber(mModel.getPhone());
            mView.setQQ(mModel.getQQNumber());
            mView.setSex(mModel.getSex());
            mView.setRelation(mModel.getRelation());
            mView.setColor(mModel.getColor());
            return true;
        }
        return false;
    }

}
