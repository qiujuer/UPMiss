package net.qiujuer.tips.view.fragment;


import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import net.qiujuer.genius.res.Resource;
import net.qiujuer.tips.R;
import net.qiujuer.tips.factory.presenter.UserPresenter;
import net.qiujuer.tips.factory.util.TipsCalender;
import net.qiujuer.tips.factory.view.UserView;
import net.qiujuer.tips.view.activity.AccountActivity;
import net.qiujuer.tips.view.activity.BaseActivity;
import net.qiujuer.tips.view.util.ColorSelector;
import net.qiujuer.tips.view.util.DateManager;

import java.util.Calendar;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment implements View.OnClickListener, UserView, CompoundButton.OnCheckedChangeListener {
    private UserPresenter mPresenter;

    private ImageView mCode;
    private TextView mAccount, mBirthday;
    private EditText mName;
    private RadioGroup mSex;
    private CheckBox mDateWay;

    private View mColor, mSave;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_user, container, false);

        mCode = (ImageView) root.findViewById(R.id.img_user_code);
        mColor = root.findViewById(R.id.btn_user_color);
        mSave = root.findViewById(R.id.btn_save);
        mAccount = (TextView) root.findViewById(R.id.txt_user_account);
        mBirthday = (TextView) root.findViewById(R.id.txt_user_birthday);
        mName = (EditText) root.findViewById(R.id.edit_user_name);
        mSex = (RadioGroup) root.findViewById(R.id.radio_user_sex);
        mDateWay = (CheckBox) root.findViewById(R.id.check_user_date_way);

        onInitValues();

        // Presenter
        mPresenter = new UserPresenter(this);
        mPresenter.init();

        mDateWay.setOnCheckedChangeListener(this);
        mColor.setOnClickListener(this);
        mSave.setOnClickListener(this);
        mBirthday.setOnClickListener(this);

        return root;
    }

    protected void onInitValues() {
        // Check
        setSex(1);

        // Color
        int color = Resource.Color.COLORS[new Random().nextInt(20) + 1];
        setColor(color);

        // Time
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        long date = year * 100000;
        date += month * 1000;
        date += day * 10;
        date += 1;

        TipsCalender tipsCalender = new TipsCalender(date);
        setBirthday(tipsCalender);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        final BaseActivity activity = (BaseActivity) getActivity();
        if (id == R.id.btn_user_color) {
            final ColorSelector selector = new ColorSelector(activity.getLayoutInflater(), getColor());
            AlertDialog dialog = activity.showDialog(activity, R.string.title_select_color,
                    selector.getView(), null,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setColor(selector.getColor());
                        }
                    });
            dialog.show();
        } else if (id == R.id.txt_user_birthday) {
            final DateManager selector = new DateManager(activity.getLayoutInflater(), getBirthday());

            AlertDialog dialog = activity.showDialog(activity, R.string.title_select_date,
                    selector.getView(), null,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setBirthday(selector.getDate());
                        }
                    });
            dialog.show();
        } else if (id == R.id.btn_save) {
            setEnable(false);
            mPresenter.save();
        }
    }

    @Override
    public String getName() {
        return mName.getText().toString();
    }

    @Override
    public TipsCalender getBirthday() {
        return (TipsCalender) mBirthday.getTag();
    }

    @Override
    public int getSex() {
        int id = mSex.getCheckedRadioButtonId();
        if (id == R.id.radio_user_sex_man)
            return 1;
        else if (id == R.id.radio_user_sex_woman)
            return 2;
        return 0;
    }

    @Override
    public int getColor() {
        return (int) mColor.getTag();
    }


    @Override
    public void setAccount(String account) {
        mAccount.setText(account);
    }

    @Override
    public void setName(String title) {
        mName.setText(title);
    }

    @Override
    public void setBirthday(TipsCalender date) {
        mDateWay.setChecked(date.getIsLunar());
        if (date.getIsLunar()) {
            mDateWay.setText(getResources().getText(R.string.txt_date_lunar));
        } else {
            mDateWay.setText(getResources().getText(R.string.txt_date_sun));
        }

        mBirthday.setTag(date);
        mBirthday.setText(date.toDate());
    }

    @Override
    public void setSex(int sex) {
        if (sex == 1)
            mSex.check(R.id.radio_user_sex_man);
        else if (sex == 2)
            mSex.check(R.id.radio_user_sex_woman);
    }

    @Override
    public void setColor(int color) {
        mColor.setTag(color);
        mColor.setBackgroundColor(color);
    }

    @Override
    public void setQRCode(Bitmap bitmap) {
        mCode.setImageBitmap(bitmap);
    }

    @Override
    public void setStatus(long status) {
        setEnable(true);
        BaseActivity activity = (BaseActivity) getActivity();
        if (status == STATUS_OK) {
            Toast.makeText(activity, R.string.txt_user_status_ok, Toast.LENGTH_SHORT).show();
        } else if (status == STATUS_ERROR_ACCOUNT) {
            Toast.makeText(activity, R.string.txt_user_status_account, Toast.LENGTH_SHORT).show();
            activity.startActivity(new Intent(activity, AccountActivity.class));
            activity.finish();
        } else if (status == STATUS_ERROR_NAME) {
            Toast.makeText(activity, R.string.txt_user_status_name, Toast.LENGTH_SHORT).show();
        } else if (status == STATUS_ERROR_SYNC) {
            Toast.makeText(activity, R.string.txt_user_status_sync, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        TipsCalender date = (TipsCalender) mBirthday.getTag();
        date.setLunar(isChecked);
        int y = date.getYear();
        int m = date.getMonth();
        int d = date.getDay();
        int newDays = TipsCalender.coercionDay(y, m, d, isChecked);
        if (date.getDay() > newDays) {
            date.setDay(newDays);
        }

        setBirthday(date);
    }

    private void setEnable(boolean isEnable) {
        mSave.setEnabled(isEnable);
        mName.setEnabled(isEnable);
        mSex.setEnabled(isEnable);
        mDateWay.setEnabled(isEnable);
        mBirthday.setEnabled(isEnable);
        mColor.setEnabled(isEnable);
    }
}
