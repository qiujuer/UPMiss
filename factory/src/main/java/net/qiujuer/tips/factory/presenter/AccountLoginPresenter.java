package net.qiujuer.tips.factory.presenter;


import android.support.annotation.StringRes;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;
import net.qiujuer.tips.factory.R;
import net.qiujuer.tips.factory.model.api.AccountLoginModel;
import net.qiujuer.tips.factory.model.api.AccountRspModel;
import net.qiujuer.tips.factory.model.api.PhoneBindModel;
import net.qiujuer.tips.factory.model.api.PhoneBindRspModel;
import net.qiujuer.tips.factory.model.api.RspModel;
import net.qiujuer.tips.factory.model.xml.AccountPreference;
import net.qiujuer.tips.factory.util.http.HttpJsonObjectRequest;
import net.qiujuer.tips.factory.util.http.HttpKit;
import net.qiujuer.tips.factory.view.LoginView;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccountLoginPresenter {
    private LoginView mView;
    private AccountLoginModel mModel = new AccountLoginModel();
    protected boolean mRunning;

    public AccountLoginPresenter(LoginView view) {
        mView = view;
        // check email
        AccountPreference accountPreference = AccountPreference.getInstance();
        if (!TextUtils.isEmpty(accountPreference.getEmail())) {
            mView.setEmail(accountPreference.getEmail());
        }
        // check re login
        if(AccountPreference.isLogin()){
            mView.onlyChangePassword();
        }
    }

    protected boolean verify(AccountLoginModel model) {
        if (TextUtils.isEmpty(mView.getEmail())) {
            setStatus(R.string.status_account_email_null);
            return false;
        }

        if (!isEmail(mView.getEmail())) {
            setStatus(R.string.status_account_email_incorrect);
            return false;
        }

        if (TextUtils.isEmpty(mView.getPassword())
                || mView.getPassword().length() < 6
                || mView.getPassword().length() > 18) {
            setStatus(R.string.status_account_password_incorrect);
            return false;
        }

        model.setEmail(mView.getEmail());
        model.setPassword(mView.getPassword());

        // This password is explicit
        model.setExplicit(true);

        return true;
    }

    private boolean isEmail(String str) {
        Pattern pattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
        Matcher isNum = pattern.matcher(str);
        return (isNum.matches());
    }

    public void login() {
        if (mRunning)
            return;

        if (!verify(mModel))
            return;

        setStatus(R.string.status_account_login_running);

        JSONObject json = mModel.toJson();
        login(HttpKit.getAccountLoginUrl(), json);
    }

    private void login(final String url, final JSONObject json) {
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

    protected void setStatus(@StringRes final int status) {
        final LoginView view = mView;
        if (view == null)
            return;
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.setStatus(status);
            }
        });
    }

    protected void callback(RspModel<AccountRspModel> model) {
        if (model.isOk()) {
            bindPhone(model.getResult());
            return;
        }
        setStatus(model.getStatusStringRes());
    }

    protected void onBindSucceed() {
        setStatus(R.string.status_account_bind_succeed);
    }

    protected void onBindFailed(RspModel<PhoneBindRspModel> model) {
        setStatus(R.string.status_account_bind_failed);
    }

    protected void callbackBind(RspModel<PhoneBindRspModel> model) {
        if (model != null && model.isOk()) {
            PhoneBindRspModel bindRspModel = model.getResult();
            AccountPreference.updatePhoneToken(bindRspModel.getPhoneToken());
            onBindSucceed();
        } else {
            onBindFailed(model);
        }
    }

    protected void callbackError() {
        setStatus(R.string.status_network_error);
    }

    protected void bindPhone(AccountRspModel model) {
        // Save before bind
        AccountPreference.save(model);

        setStatus(R.string.status_account_bind_running);
        mRunning = true;

        final JSONObject json = new PhoneBindModel().toJson();
        HttpJsonObjectRequest jsonRequestObject = new HttpJsonObjectRequest(Request.Method.POST,
                HttpKit.getPhoneBindUrl(),
                json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        RspModel<PhoneBindRspModel> model = RspModel.fromJson(response,
                                PhoneBindRspModel.getRspType());
                        callbackBind(model);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        onBindFailed(null);
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
