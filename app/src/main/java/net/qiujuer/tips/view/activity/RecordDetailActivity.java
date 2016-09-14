package net.qiujuer.tips.view.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import net.qiujuer.tips.R;
import net.qiujuer.tips.common.drawable.AnimJagDrawable;
import net.qiujuer.tips.common.widget.SimpleDateView;
import net.qiujuer.tips.factory.model.db.RecordModel;
import net.qiujuer.tips.factory.presenter.RecordDetailPresenter;
import net.qiujuer.tips.factory.util.TipsCalender;
import net.qiujuer.tips.factory.view.RecordDetailView;
import net.qiujuer.tips.view.util.AnimationListener;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.UUID;

import static java.util.Calendar.DATE;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.DAY_OF_WEEK;

public class RecordDetailActivity extends BlurActivity implements RecordDetailView, OnClickListener {
    private static final String SUFFIX = "DAY";

    private UUID mId;
    private RecordDetailPresenter mPresenter;
    private TextView mBrief;
    private TextView mYearMonth;
    private View mOperation;
    private View mTop;
    private SimpleDateView mSimpleDate;
    private TextView mLunar;
    private TextView mDate;

    public static void actionStart(Context context, UUID id) {
        Intent intent = new Intent(context, RecordDetailActivity.class);
        if (id != null)
            intent.putExtra("Id", id.toString());
        context.startActivity(intent);
    }

    @Override
    protected void onInitToolBar() {
        // NULL
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_detail;
    }

    @Override
    protected void onInit(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (intent != null) {
            String id = intent.getStringExtra("Id");
            mId = UUID.fromString(id);
        }
        // Init presenter
        mPresenter = new RecordDetailPresenter(this);

        // Init View
        mYearMonth = (TextView) findViewById(R.id.txt_year_month);
        mBrief = (TextView) findViewById(R.id.txt_brief);
        mOperation = findViewById(R.id.lay_scroll_operation);
        mSimpleDate = (SimpleDateView) findViewById(R.id.simple_date);
        mLunar = (TextView) findViewById(R.id.txt_lunar);
        mDate = (TextView) findViewById(R.id.txt_date);
        mTop = findViewById(R.id.lay_top);

        mDate.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Hero.otf"));

        AnimJagDrawable drawable = new AnimJagDrawable();
        drawable.setFluCount(new Rect(0, 0, 0, 36));

        mTop.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mTop.setBackgroundDrawable(drawable);

        findViewById(R.id.container).setOnClickListener(this);
        findViewById(R.id.detail_btn_share).setOnClickListener(this);
        findViewById(R.id.detail_btn_save).setOnClickListener(this);
        findViewById(R.id.detail_btn_edit).setOnClickListener(this);
        findViewById(R.id.detail_btn_delete).setOnClickListener(this);

        mDate.postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation anim = AnimationUtils.loadAnimation(RecordDetailActivity.this,
                        R.anim.anim_record_detail_content_in);
                anim.setAnimationListener(new AnimationListener() {
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        super.onAnimationEnd(animation);
                        mDate.setVisibility(View.VISIBLE);
                    }
                });
                mDate.clearAnimation();
                mDate.startAnimation(anim);
            }
        }, 64);
    }

    private void refresh() {
        // Refresh
        if (!mPresenter.refresh())
            finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.detail_btn_share:
                hideOperation(new Runnable() {
                    @Override
                    public void run() {
                        setBlurSrcAsync();
                        ShareActivity.actionStart(RecordDetailActivity.this, mBrief.getText().toString(), getSummary());
                    }
                });
                break;
            case R.id.detail_btn_save:
                hideOperation(new Runnable() {
                    @Override
                    public void run() {
                        mPresenter.saveScreen();
                    }
                });
                break;
            case R.id.detail_btn_edit:
                hideOperation(new Runnable() {
                    @Override
                    public void run() {
                        RecordEditActivity.actionStart(RecordDetailActivity.this, mId);
                        setBlur(RecordDetailActivity.this);
                    }
                });
                break;
            case R.id.detail_btn_delete:
                hideOperation(new Runnable() {
                    @Override
                    public void run() {
                        showDialog(RecordDetailActivity.this, R.string.txt_status_delete_notes, null, null, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mPresenter.delete();
                            }
                        }).show();
                    }
                });
                break;
            case R.id.container:
            default:
                shOperation();
                break;
        }
    }

    @Override
    public UUID getId() {
        return mId;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void setType(int type) {
        mDate.setTag(type);
        if (type == 1) {
            mDate.setBackgroundDrawable(getResources().getDrawable(R.mipmap.ic_badge_birthday));
        } else if (type == 2) {
            mDate.setBackgroundDrawable(getResources().getDrawable(R.mipmap.ic_badge_memorial));
        } else {
            mDate.setBackgroundDrawable(getResources().getDrawable(R.mipmap.ic_badge_future));
        }
    }

    @Override
    public void setBrief(String brief) {
        mBrief.setText(brief);
    }

    @Override
    public void setTime(TipsCalender date) {
        int[] days = new int[7];

        Calendar calendar;
        if ((int) mDate.getTag() == RecordModel.TYPE_FUTURE) {
            calendar = date.getCalender();
            mLunar.setText(date.toLunarString());
            mDate.setText(getTopText(date.distanceNow()));
            mYearMonth.setText(date.toSunYMString());
        } else {
            calendar = date.getNextCalender();
            mLunar.setText(date.toNextLunarString());
            mDate.setText(getTopText(date.distanceNowSpecial()));
            mYearMonth.setText(date.toNextSunYMString());
        }

        int week = calendar.get(DAY_OF_WEEK) - 1;

        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, week);
        for (int i = 0; i < 7; i++) {
            if (i == 0) {
                calendar.add(DATE, -week);
            }
            days[i] = calendar.get(DAY_OF_MONTH);
            calendar.add(DATE, 1);
        }
        mSimpleDate.setDate(days, week);
    }

    private String getSummary() {
        String str = getResources().getString(R.string.txt_o_share_summary);
        return String.format(str, mYearMonth.getText().toString() + mSimpleDate.getSelectDay(),
                mLunar.getText().toString(),
                mDate.getText().toString().replace('\n', ' '));
    }

    private CharSequence getTopText(int occupancy) {
        SpannableStringBuilder span = new SpannableStringBuilder();

        int lenFx = SUFFIX.length();

        if (occupancy < 0) {
            occupancy = -occupancy;
            span.append(String.valueOf(occupancy));
            span.append('\n');
            span.append('-');
            lenFx += 2;
        } else {
            span.append(String.valueOf(occupancy));
            span.append('\n');
            lenFx += 1;
        }
        span.append(SUFFIX);

        final Resources resources = getResources();
        final int len = span.length();
        lenFx = len - lenFx;

        span.setSpan(new AbsoluteSizeSpan(resources.getDimensionPixelSize(R.dimen.font_14)),
                lenFx, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new ForegroundColorSpan(resources.getColor(R.color.grey_500)),
                lenFx, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return span;
    }

    @Override
    public void setColor(int color) {
        AnimJagDrawable drawable = (AnimJagDrawable) mTop.getBackground();
        if (color != drawable.getColor()) {
            drawable.setColorUnInvalidate(color);
            drawable.setAlpha(164);
            drawable.startAnim();
        }
    }

    @Override
    public void setStatus(@StringRes int statusId) {
        Toast.makeText(this, statusId, Toast.LENGTH_SHORT).show();
        if (statusId == R.string.status_opt_delete_ok) {
            finish();
        }
    }

    private class OperationAnimationListener extends AnimationListener {
        @Override
        public void onAnimationEnd(Animation animation) {
            super.onAnimationEnd(animation);
            animation.setAnimationListener(null);
        }
    }

    private void shOperation() {
        if (mOperation.getAnimation() != null)
            return;

        if (mOperation.getVisibility() == View.VISIBLE) {
            Animation anim = AnimationUtils.loadAnimation(this,
                    R.anim.anim_out_slide_alpha_bottom);
            mOperation.clearAnimation();
            anim.setAnimationListener(new OperationAnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    super.onAnimationEnd(animation);
                    mOperation.setVisibility(View.INVISIBLE);
                }
            });
            mOperation.startAnimation(anim);
        } else {
            Animation anim = AnimationUtils.loadAnimation(this,
                    R.anim.anim_in_slide_alpha_bottom);
            mOperation.clearAnimation();
            anim.setAnimationListener(new OperationAnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    super.onAnimationEnd(animation);
                    mOperation.setVisibility(View.VISIBLE);
                }
            });
            mOperation.startAnimation(anim);
        }
    }

    private void hideOperation(final Runnable runnable) {
        if (mOperation.getVisibility() == View.VISIBLE) {
            Animation anim = AnimationUtils.loadAnimation(this,
                    R.anim.anim_out_slide_alpha_bottom);
            mOperation.clearAnimation();
            anim.setAnimationListener(new OperationAnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    super.onAnimationEnd(animation);
                    mOperation.setVisibility(View.INVISIBLE);
                    if (runnable != null)
                        runnable.run();
                }
            });
            mOperation.startAnimation(anim);
        } else {
            if (runnable != null)
                runnable.run();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
