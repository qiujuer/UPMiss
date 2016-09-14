package net.qiujuer.tips.factory.presenter;

import net.qiujuer.tips.factory.cache.Cache;
import net.qiujuer.tips.factory.model.db.ContactModel;
import net.qiujuer.tips.factory.view.ContactAddView;

import java.util.UUID;

public class ContactAddPresenter {
    private ContactAddView mView;

    public ContactAddPresenter(ContactAddView view) {
        mView = view;
    }

    public void create() {
        if (mView.getNameStr() == null || mView.getNameStr().length() <= 0) {
            mView.setStatus(-2);
        } else if (mView.getPhoneNumber() == null || mView.getPhoneNumber().length() <= 0) {
            mView.setStatus(-3);
        } else {
            ContactModel model = new ContactModel();
            model.setMark(UUID.randomUUID());
            model.setName(mView.getNameStr());
            model.setPhone(mView.getPhoneNumber());
            model.setQQNumber(mView.getQQ());
            model.setSex(mView.getGender());
            model.setRelation(mView.getRelation());
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
