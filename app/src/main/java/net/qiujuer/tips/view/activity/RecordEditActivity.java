package net.qiujuer.tips.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import net.qiujuer.tips.R;
import net.qiujuer.tips.factory.presenter.RecordEditPresenter;
import net.qiujuer.tips.factory.util.TipsCalender;
import net.qiujuer.tips.factory.view.RecordEditView;

import java.util.UUID;

public class RecordEditActivity extends RecordAddActivity implements RecordEditView {
    private UUID mId;
    private RecordEditPresenter mPresenter;

    public static void actionStart(Context context, UUID id) {
        Intent intent = new Intent(context, RecordEditActivity.class);
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
        mPresenter = new RecordEditPresenter(this);
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
    public UUID getId() {
        return mId;
    }

    @Override
    public void setType(int type) {
        if (type == 1)
            mType.check(R.id.edit_radio_type_birthday);
        else if (type == 2)
            mType.check(R.id.edit_radio_type_memorial);
        else //if (type == 3)
            mType.check(R.id.edit_radio_type_future);
    }

    @Override
    public void setBrief(String title) {
        if (title != null)
            mBrief.setText(title);
    }

    @Override
    public void setDate(TipsCalender date) {
        if (date.getIsLunar())
            mTimeType.check(R.id.edit_radio_time_type_lunar);
        else
            mTimeType.check(R.id.edit_radio_time_type_solar);
        mTime.setTag(date);
        mTime.setText(date.toDate());
    }

    @Override
    public void setColor(int color) {
        mColor.setTag(color);
        mColor.setBackgroundColor(color);
    }

    @Override
    public void setContactName(String name) {
        mTxtContact.setText(name);
    }

    @Override
    public void setStatus(long status) {
        super.setStatus(status);
    }
}
