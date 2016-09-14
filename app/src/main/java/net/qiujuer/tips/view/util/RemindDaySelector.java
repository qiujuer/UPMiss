package net.qiujuer.tips.view.util;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;

import net.qiujuer.tips.R;

public class RemindDaySelector implements View.OnClickListener {

    View mRoot;
    int mRemindDay;
    RadioButton mRadioButton;

    public RemindDaySelector(LayoutInflater inflater, int time) {
        mRemindDay = time;
        mRoot = inflater.inflate(R.layout.dialog_remind_day, null);

        initRadioButton(R.id.dialog_remind_days_one);
        initRadioButton(R.id.dialog_remind_days_two);
        initRadioButton(R.id.dialog_remind_days_three);
        initRadioButton(R.id.dialog_remind_days_four);
        initRadioButton(R.id.dialog_remind_days_five);
        initRadioButton(R.id.dialog_remind_days_six);
        initRadioButton(R.id.dialog_remind_days_seven);
        initRadioButton(R.id.dialog_remind_days_eight);
        initRadioButton(R.id.dialog_remind_days_nine);
        initRadioButton(R.id.dialog_remind_days_ten);

    }

    private void initRadioButton(int id) {
        RadioButton view = (RadioButton) mRoot.findViewById(id);
        view.setOnClickListener(this);
        final int days = Integer.parseInt(view.getText().toString());
        if (days == mRemindDay) {
            onClick(view);
        }
    }

    public View getView() {
        return mRoot;
    }

    public int getRemindDay() {
        return mRemindDay;
    }

    @Override
    public void onClick(View v) {
        if (mRadioButton != null) {
            mRadioButton.setChecked(false);
        }
        mRadioButton = (RadioButton) v;
        mRadioButton.setChecked(true);

        mRemindDay = Integer.parseInt(mRadioButton.getText().toString());
    }
}
