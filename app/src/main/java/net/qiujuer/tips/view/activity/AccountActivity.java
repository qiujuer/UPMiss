package net.qiujuer.tips.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import net.qiujuer.genius.ui.widget.Loading;
import net.qiujuer.tips.R;
import net.qiujuer.tips.factory.presenter.AccountLoginPresenter;
import net.qiujuer.tips.factory.presenter.AccountRegisterPresenterAccount;
import net.qiujuer.tips.factory.view.LoginView;
import net.qiujuer.tips.factory.view.RegisterView;

public class AccountActivity extends BaseActivity implements View.OnClickListener, LoginView, RegisterView {
    private ViewAnimator mViewAnimator;
    private Button mBtnSubmit;
    private Button mBtnChange;

    private EditText mEditLoginEmail;
    private EditText mEditLoginPassword;

    private EditText mEditRegisterEmail;
    private EditText mEditRegisterPassword;
    private EditText mEditRegisterPasswordConfirm;

    private TextView mTxtTitle;
    private TextView mTxtWhether;

    private AccountLoginPresenter mAccountLoginPresenter;
    private AccountRegisterPresenterAccount mRegisterPresenter;

    private Loading mLoading;


    public static void actionStart(Context context) {
        Intent intent = new Intent(context, AccountActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        mTxtWhether = (TextView) findViewById(R.id.account_txt_whether);
        mTxtTitle = (TextView) findViewById(R.id.account_txt_title);
        mViewAnimator = (ViewAnimator) findViewById(R.id.account_output);
        mEditLoginEmail = (EditText) findViewById(R.id.account_edit_login_email);
        mEditLoginPassword = (EditText) findViewById(R.id.account_edit_login_password);
        mEditRegisterPasswordConfirm = (EditText) findViewById(R.id.account_edit_register_password_confirm);
        mEditRegisterPassword = (EditText) findViewById(R.id.account_edit_register_password);
        mEditRegisterEmail = (EditText) findViewById(R.id.account_edit_register_email);
        mBtnSubmit = (Button) findViewById(R.id.account_btn_submit);
        mBtnChange = (Button) findViewById(R.id.account_btn_change);
        mLoading = (Loading) findViewById(R.id.loading);

        mViewAnimator.setInAnimation(AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.anim_in_slide_right));
        mViewAnimator.setOutAnimation(AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.anim_out_slide_left));

        mBtnSubmit.setOnClickListener(this);
        mBtnChange.setOnClickListener(this);

        mRegisterPresenter = new AccountRegisterPresenterAccount(this);
        mAccountLoginPresenter = mRegisterPresenter;
    }

    private boolean isLogin() {
        View view = mViewAnimator.getCurrentView();
        return view.getId() == R.id.account_lay_login;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.account_btn_submit:
                if (isLogin()) {
                    mAccountLoginPresenter.login();
                } else {
                    mRegisterPresenter.register();
                }
                break;
            case R.id.account_btn_change: {
                mViewAnimator.showNext();
                if (isLogin()) {
                    mTxtWhether.setText(getResources().getString(R.string.txt_not_have_account));
                    mTxtTitle.setText(getResources().getString(R.string.txt_title_login));
                    mBtnChange.setText(getResources().getString(R.string.txt_not_have_account_btn));
                } else {
                    mTxtWhether.setText(getResources().getString(R.string.txt_have_account));
                    mTxtTitle.setText(getResources().getString(R.string.txt_title_join));
                    mBtnChange.setText(getResources().getString(R.string.txt_have_account_btn));
                }
            }
            break;
            default:
                break;
        }
    }

    @Override
    public String getEmail() {
        if (isLogin())
            return mEditLoginEmail.getText().toString();
        else
            return mEditRegisterEmail.getText().toString();
    }

    @Override
    public String getPassword() {
        if (isLogin())
            return mEditLoginPassword.getText().toString();
        else
            return mEditRegisterPassword.getText().toString();
    }

    @Override
    public String getConfirmPassword() {
        return mEditRegisterPasswordConfirm.getText().toString();
    }

    @Override
    public void setStatus(@StringRes int res) {
        if (res == R.string.status_account_login_running
                || res == R.string.status_account_register_running) {
            mBtnChange.setEnabled(false);
            mBtnSubmit.setEnabled(false);
            mBtnSubmit.setText("");
            mLoading.start();
        } else if (res == R.string.status_account_bind_succeed) {
            finish();
            Toast.makeText(this, res, Toast.LENGTH_SHORT).show();
        } else {
            mBtnChange.setEnabled(true);
            mBtnSubmit.setEnabled(true);
            mBtnSubmit.setText(R.string.txt_go);
            mLoading.stop();
            Toast.makeText(this, res, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setEmail(String email) {
        mEditLoginEmail.setText(email);
        mEditRegisterEmail.setText(email);
    }

    @Override
    public void onlyChangePassword() {
        mEditLoginEmail.setEnabled(false);
        mBtnChange.setVisibility(View.GONE);
        mTxtWhether.setVisibility(View.GONE);
    }
}
