package net.qiujuer.tips.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import net.qiujuer.tips.R;
import net.qiujuer.tips.factory.presenter.ContactEditPresenter;
import net.qiujuer.tips.factory.view.ContactEditView;

import java.util.UUID;

public class ContactEditActivity extends ContactAddActivity implements ContactEditView {
    private UUID mId;
    private ContactEditPresenter mPresenter;


    public static void actionStart(Context context, UUID id) {
        Intent intent = new Intent(context, ContactEditActivity.class);
        if (id != null)
            intent.putExtra("Id", id.toString());
        context.startActivity(intent);
    }

    @Override
    protected void onInit(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (intent != null) {
            mId = UUID.fromString(intent.getStringExtra("Id"));
        }
        super.onInit(savedInstanceState);
    }

    @Override
    protected void onInitPresenter() {
        mPresenter = new ContactEditPresenter(this);
        // Refresh data
        if (!mPresenter.refresh())
            finish();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_save) {
            //Save
            mPresenter.save();
        } else {
            super.onClick(v);
        }
    }

    @Override
    public void setNameStr(String name) {
        mEdtTxtName.setText(name);
    }

    @Override
    public void setPhoneNumber(String phoneNumber) {
        mEdtTxtPhone.setText(phoneNumber);
    }

    @Override
    public void setQQ(String qq) {
        mEdtTxtQQ.setText(qq);
    }

    @Override
    public void setGender(int isMan) {
        mRdoBtnGender.setTag(isMan);
        if (isMan == 1) {
            mRdoBtnGender.check(R.id.contacts_radio_gender_man);
        } else if (isMan == 0) {
            mRdoBtnGender.check(R.id.contacts_radio_gender_woman);
        }
    }

    @Override
    public UUID getId() {
        return mId;
    }

    @Override
    public void setStatus(long status) {
        super.setStatus(status);
    }
}
