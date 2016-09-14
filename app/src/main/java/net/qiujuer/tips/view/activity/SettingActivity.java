package net.qiujuer.tips.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.qiujuer.tips.R;
import net.qiujuer.tips.factory.presenter.SettingPresenter;
import net.qiujuer.tips.factory.view.SettingView;
import net.qiujuer.tips.view.util.ColorSelector;
import net.qiujuer.tips.view.util.RemindDaySelector;

public class SettingActivity extends BlurActivity implements SettingView,
        Toolbar.OnMenuItemClickListener, View.OnClickListener {

    private TextView mTxtRemindDay;
    private View mViewColorStart;
    private View mViewColorEnd;
    private SettingPresenter mPresenter;
    private int mIndex;

    @Override
    protected int getContentView() {
        return R.layout.activity_setting;
    }

    @Override
    protected void onInit(Bundle savedInstanceState) {
        mTxtRemindDay = (TextView) findViewById(R.id.set_txt_days_remind);
        mViewColorStart = findViewById(R.id.set_txt_color_start);
        mViewColorEnd = findViewById(R.id.set_txt_color_end);

        // Init presenter
        onInitPresenter();

        // Set listener
        findViewById(R.id.btn_save).setOnClickListener(this);
        mTxtRemindDay.setOnClickListener(this);
        mViewColorStart.setOnClickListener(this);
        mViewColorEnd.setOnClickListener(this);
        findViewById(R.id.set_lay_way_add_widget).setOnClickListener(this);
        findViewById(R.id.set_lay_way_add_float).setOnClickListener(this);

        final LinearLayout layout = (LinearLayout) findViewById(R.id.lay_data);
        assert layout != null;
        layout.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                Animation animation = AnimationUtils.loadAnimation(SettingActivity.this, R.anim.anim_in_slide_alpha_bottom_long);
                LayoutAnimationController lac = new LayoutAnimationController(animation);
                lac.setOrder(LayoutAnimationController.ORDER_NORMAL);
                lac.setDelay(0.28f);
                layout.setLayoutAnimation(lac);
            }

            @Override
            public void onViewDetachedFromWindow(View v) {

            }
        });
        layout.setVisibility(View.VISIBLE);
    }

    private void onInitPresenter() {
        mPresenter = new SettingPresenter(this);
        mPresenter.refresh();
    }

    @Override
    protected void onInitToolBar() {
        super.onInitToolBar();
        mToolbar.setTitle(getTitle());
        mToolbar.setNavigationIcon(R.mipmap.ic_action_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public int getLeadTime() {
        return (int) mTxtRemindDay.getTag();
    }

    @Override
    public int[] getColor() {
        int[] back = new int[2];
        back[0] = (int) mViewColorStart.getTag();
        back[1] = (int) mViewColorEnd.getTag();
        return back;
    }

    @Override
    public void setLeadTime(int time) {
        mTxtRemindDay.setTag(time);
        mTxtRemindDay.setText(time + "天提醒");
    }

    @Override
    public void setColor(int[] color) {
        if (color != null) {
            mViewColorStart.setTag(color[0]);
            mViewColorStart.setBackgroundColor(color[0]);
            mViewColorEnd.setTag(color[1]);
            mViewColorEnd.setBackgroundColor(color[1]);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.set_txt_color_start || v.getId() == R.id.set_txt_color_end) {
            int color = 0;
            switch (v.getId()) {
                case R.id.set_txt_color_start:
                    color = (int) mViewColorStart.getTag();
                    mIndex = 0;
                    break;
                case R.id.set_txt_color_end:
                    color = (int) mViewColorEnd.getTag();
                    mIndex = 1;
                    break;
                default:
                    break;
            }
            final ColorSelector selector = new ColorSelector(getLayoutInflater(), color);
            AlertDialog dialog = showDialog(SettingActivity.this, R.string.title_select_color,
                    selector.getView(), null,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            callBackColor(selector.getColor());
                        }
                    });
            dialog.show();
        } else if (v.getId() == R.id.btn_save) {
            mPresenter.save();
            finish();
        } else {
            switch (v.getId()) {
                case R.id.set_txt_days_remind:
                    final RemindDaySelector select = new RemindDaySelector(getLayoutInflater()
                            , (int) mTxtRemindDay.getTag());
                    AlertDialog dialogRemind = showDialog(SettingActivity.this,
                            R.string.txt_remind_day,
                            select.getView(), null,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    setLeadTime(select.getRemindDay());
                                }
                            });
                    dialogRemind.show();
                    break;
                case R.id.set_lay_way_add_widget:
                    Intent intent = new Intent(this, HelpActivity.class);
                    startActivity(intent);
                    //setBlur(SettingActivity.this);
                    break;
                case R.id.set_lay_way_add_float:
                    Toast.makeText(SettingActivity.this, R.string.btn_add_float_frame_way_toast, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    }

    private void callBackColor(int color) {
        if (mIndex == 0) {
            mViewColorStart.setBackgroundColor(color);
            mViewColorStart.setTag(color);
        } else if (mIndex == 1) {
            mViewColorEnd.setBackgroundColor(color);
            mViewColorEnd.setTag(color);
        }
    }
}
