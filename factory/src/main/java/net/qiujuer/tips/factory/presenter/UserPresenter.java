package net.qiujuer.tips.factory.presenter;


import net.qiujuer.tips.factory.model.xml.UserModel;
import net.qiujuer.tips.factory.view.UserView;

public class UserPresenter implements Presenter {
    private UserView mView;

    public UserPresenter(UserView view) {
        mView = view;
    }

    public void init() {
        if (mView != null) {
            UserView view = mView;
            if (!AppPresenter.isLogin()) {
                view.setStatus(UserView.STATUS_ERROR_ACCOUNT);
                return;
            }

            //AccountPreference accountPreference = new AccountPreference();
            //view.setAccount(accountPreference.getName() + "\n" + accountPreference.getEmail());

            //String str = QRcodePresenter.MARK_FLOW + accountPreference.getId();
            //Bitmap bitmap = BitmapUtil.create2DCoderBitmap(str, 200, 200);
            //view.setQRCode(bitmap);

            UserModel model = new UserModel();
            view.setColor(model.getColor());
            view.setBirthday(model.getBirthday());
            view.setName(model.getName());
            view.setSex(model.getSex());
        }
    }

    public void save() {
        if (mView != null) {
            UserView view = mView;
            String name = view.getName();
            if (name.length() <= 0) {
                view.setStatus(UserView.STATUS_ERROR_NAME);
                return;
            }
            UserModel model = new UserModel();
            model.setName(name);
            model.setBirthday(view.getBirthday());
            model.setSex(view.getSex());
            model.setColor(view.getColor());
            model.save();
            view.setStatus(UserView.STATUS_OK);
        }
    }

    private void sync() {

    }

    private void setSaveStatus() {
        if (mView != null) {

        }
    }

    @Override
    public void destroy() {
        mView = null;
    }
}
