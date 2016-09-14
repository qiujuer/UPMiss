package net.qiujuer.tips.factory.presenter;


import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import net.qiujuer.tips.factory.R;
import net.qiujuer.tips.factory.model.api.AccountRegisterModel;
import net.qiujuer.tips.factory.model.api.AccountRspModel;
import net.qiujuer.tips.factory.model.api.RspModel;
import net.qiujuer.tips.factory.model.xml.AccountPreference;
import net.qiujuer.tips.factory.util.http.HttpJsonObjectRequest;
import net.qiujuer.tips.factory.util.http.HttpKit;
import net.qiujuer.tips.factory.view.RegisterView;

import org.json.JSONObject;

public class AccountRegisterPresenterAccount extends AccountLoginPresenter {
    private RegisterView mView;
    private AccountRegisterModel mModel = new AccountRegisterModel();

    public AccountRegisterPresenterAccount(RegisterView view) {
        super(view);
        mView = view;
    }

    protected boolean verify(AccountRegisterModel model) {
        if (!super.verify(model))
            return false;

        if (!mView.getConfirmPassword().equals(model.getPassword())) {
            setStatus(R.string.status_account_password_not_confirm);
            return false;
        }

        model.setConfirmPassword(mView.getConfirmPassword());

        return true;
    }

    public void register() {
        if (mRunning)
            return;

        if (!verify(mModel))
            return;

        setStatus(R.string.status_account_register_running);

        JSONObject json = mModel.toJson();
        register(HttpKit.getAccountRegisterUrl(), json);
    }

    private void register(final String url, final JSONObject json) {
        mRunning = true;
        HttpJsonObjectRequest jsonRequestObject = new HttpJsonObjectRequest(Request.Method.POST, url, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        RspModel<AccountRspModel> model = RspModel.fromJson(response,
                                AccountRspModel.getRspType());
                        if (model == null) {
                            callbackError();
                        } else {
                            callback(model);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callbackError();
                    }
                }).setOnFinishListener(new HttpJsonObjectRequest.OnFinishListener() {
            @Override
            public void onFinish() {
                mRunning = false;
            }
        });
        AppPresenter.getRequestQueue().add(jsonRequestObject);
    }

}
