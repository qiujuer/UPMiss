package net.qiujuer.tips.common.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import net.qiujuer.tips.common.R;

public class RelationView extends LinearLayout implements View.OnClickListener {
    private RadioGroup mRdoGroupGender;
    private RadioButton mLastRdoBtn;

    private RadioButton mFirstRdoBtn;
    private RadioButton mTwoRdoBtn;
    private RadioButton mThreeRdoBtn;
    private RadioButton mFourRdoBtn;
    private RadioButton mFiveRdoBtn;
    private RadioButton mSixRdoBtn;

    private int mGender;
    private int mClickRelation;
    private String[] mRelations;
    private int mInitViewsTxtMark;

    public RelationView(Context context) {
        super(context);
    }

    public RelationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.relation_view, this);
        mRdoGroupGender = (RadioGroup) findViewById(R.id.relation_radioGroup_gender);
        mRelations = getResources().getStringArray(R.array.array_contacts_relations);
        mRdoGroupGender.setVisibility(GONE);
        setChangeListener();
        onInitViewsTxt();
    }

    public RelationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RelationView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void setChangeListener() {
        mRdoGroupGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.relation_radioGroup_gender_man) {
                    mGender = 1;
                    onInitMan();
                } else if (checkedId == R.id.relation_radioGroup_gender_woman) {
                    mGender = 0;
                    onInitWoman();
                }
            }
        });
    }

    /**
     * 刷新前6个关系
     */
    private void onInitWoman() {
        mFirstRdoBtn.setText(mRelations[1]);
        mTwoRdoBtn.setText(mRelations[2]);
        mThreeRdoBtn.setText(mRelations[3]);
        mFourRdoBtn.setText(mRelations[4]);
        mFiveRdoBtn.setText(mRelations[5]);
        mSixRdoBtn.setText(mRelations[6]);
    }

    private void onInitMan() {
        mFirstRdoBtn.setText(mRelations[7]);
        mTwoRdoBtn.setText(mRelations[8]);
        mThreeRdoBtn.setText(mRelations[9]);
        mFourRdoBtn.setText(mRelations[10]);
        mFiveRdoBtn.setText(mRelations[11]);
        mSixRdoBtn.setText(mRelations[12]);
    }

    /**
     * 初始化文本内容
     */
    private void onInitViewsTxt() {
        mInitViewsTxtMark = 7;
        mFirstRdoBtn = onInitView(R.id.relation_rdoBtn_man_and_wife);
        mTwoRdoBtn = onInitView(R.id.relation_rdoBtn_younger_brother_and_sister);
        mThreeRdoBtn = onInitView(R.id.relation_rdoBtn_boyfriend_and_girlfriend);
        mFourRdoBtn = onInitView(R.id.relation_rdoBtn_parents);

        mFiveRdoBtn = onInitView(R.id.relation_rdoBtn_children);
        mSixRdoBtn = onInitView(R.id.relation_rdoBtn_elder_brother_and_sister);

        onInitView(R.id.relation_rdoBtn_sworn_followers);
        onInitView(R.id.relation_rdoBtn_friend);

        onInitView(R.id.relation_rdoBtn_schoolmate);
        onInitView(R.id.relation_rdoBtn_elder);
        onInitView(R.id.relation_rdoBtn_contemporaries);
        onInitView(R.id.relation_rdoBtn_junior);

        onInitView(R.id.relation_rdoBtn_teacher);
        onInitView(R.id.relation_rdoBtn_colleague);
        onInitView(R.id.relation_rdoBtn_client);
        onInitView(R.id.relation_rdoBtn_leader);
    }

    /**
     * 初始化选择项
     */
    private void onInitCheckedView() {
        selectView(R.id.relation_rdoBtn_man_and_wife);
        selectView(R.id.relation_rdoBtn_younger_brother_and_sister);
        selectView(R.id.relation_rdoBtn_boyfriend_and_girlfriend);
        selectView(R.id.relation_rdoBtn_parents);

        selectView(R.id.relation_rdoBtn_children);
        selectView(R.id.relation_rdoBtn_elder_brother_and_sister);
        selectView(R.id.relation_rdoBtn_sworn_followers);
        selectView(R.id.relation_rdoBtn_friend);

        selectView(R.id.relation_rdoBtn_schoolmate);
        selectView(R.id.relation_rdoBtn_elder);
        selectView(R.id.relation_rdoBtn_contemporaries);
        selectView(R.id.relation_rdoBtn_junior);

        selectView(R.id.relation_rdoBtn_teacher);
        selectView(R.id.relation_rdoBtn_colleague);
        selectView(R.id.relation_rdoBtn_client);
        selectView(R.id.relation_rdoBtn_leader);
    }

    private RadioButton onInitView(int id) {
        RadioButton view = (RadioButton) findViewById(id);
        view.setOnClickListener(this);
        view.setText(mRelations[mInitViewsTxtMark]);
        mInitViewsTxtMark++;
        return view;
    }

    private void selectView(int id) {
        RadioButton view = (RadioButton) findViewById(id);

        final int mkRelation = Integer.parseInt((String) view.getTag());
        if (mkRelation == mClickRelation) {
            onClick(view);
        }
    }

    @Override
    public void onClick(View v) {
        if (mLastRdoBtn != null) {
            mLastRdoBtn.setChecked(false);
        }
        mLastRdoBtn = (RadioButton) v;
        mLastRdoBtn.setChecked(true);
        mClickRelation = Integer.parseInt((String) mLastRdoBtn.getTag());
    }

    public void setRelation(int relation) {
        mClickRelation = relation;
        onInitCheckedView();
    }

    public int getRelation() {
        return mClickRelation;
    }

    public int getGender() {
        return mGender;
    }

    public void setGender(int gender) {
        this.mGender = gender;
        if (mGender == 1) {
            mRdoGroupGender.check(R.id.relation_radioGroup_gender_man);
            onInitMan();
        } else if (mGender == 0) {
            mRdoGroupGender.check(R.id.relation_radioGroup_gender_woman);
            onInitWoman();
        }
    }
}
