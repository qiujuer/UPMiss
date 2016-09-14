package net.qiujuer.tips.view.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import net.qiujuer.tips.R;
import net.qiujuer.tips.factory.presenter.SharePresenter;
import net.qiujuer.tips.factory.view.ShareView;
import net.qiujuer.tips.open.Share;

public class ShareActivity extends BlurActivity implements View.OnClickListener, ShareView {
    private ImageView mSrcView;
    private Bitmap mSrc;
    private SharePresenter mPresenter;
    private String mTitle;
    private String mSummary;
    private View mOperation;

    public static void actionStart(Context context, String title, String summary) {
        Intent intent = new Intent(context, ShareActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("summary", summary);
        context.startActivity(intent);
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            mTitle = intent.getStringExtra("title");
            mSummary = intent.getStringExtra("summary");
        }
        if (mTitle == null || mTitle.length() == 0)
            mTitle = "";
        if (mSummary == null || mSummary.length() == 0)
            mSummary = "";
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_share;
    }

    @Override
    protected void onInit(Bundle savedInstanceState) {
        initData();
        mPresenter = new SharePresenter(this);

        mSrcView = (ImageView) findViewById(R.id.share_src);
        mOperation = findViewById(R.id.lay_operation);
        findViewById(R.id.share_wechat).setOnClickListener(this);
        findViewById(R.id.share_wechatfriends).setOnClickListener(this);
        findViewById(R.id.share_sina_weibo).setOnClickListener(this);
        findViewById(R.id.share_qq).setOnClickListener(this);


        mSrc = getBlurSrcBitmap();
        mSrcView.setOnClickListener(this);
        mSrcView.setImageBitmap(mSrc);

        shrink(mSrcView);
    }

    @Override
    protected void onInitToolBar() {
        // NULL
        /*
        Animation animation = AnimationUtils.loadAnimation(this,
                R.anim.anim_in_slide_left);
        LayoutAnimationController checkAnim = new LayoutAnimationController(animation, 0.3f);
        checkAnim.setOrder(LayoutAnimationController.ORDER_NORMAL);
        fControl.setLayoutAnimation(checkAnim);
        */
    }

    public void shrink(final View view) {
        ObjectAnimator anim = ObjectAnimator
                .ofFloat(view, "size", 1.0F, 0.75F)
                .setDuration(480);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float cVal = (Float) animation.getAnimatedValue();
                view.setScaleX(cVal);
                view.setScaleY(cVal);
                if (cVal <= 0.75) {
                    showOperation();
                }
            }
        });
        anim.start();
    }

    public void blowup(final View view) {
        ObjectAnimator anim = ObjectAnimator
                .ofFloat(view, "size", 0.75F, 1.0F)
                .setDuration(360);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float cVal = (Float) animation.getAnimatedValue();
                view.setScaleX(cVal);
                view.setScaleY(cVal);
                if (cVal == 1.0F) {
                    finish();
                }
            }
        });
        anim.start();
    }

    private void showOperation() {
        Animation anim = AnimationUtils.loadAnimation(this,
                R.anim.anim_in_slide_alpha_bottom);
        mOperation.setVisibility(View.VISIBLE);
        mOperation.startAnimation(anim);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSrc.recycle();
        mSrc = null;
    }

    @Override
    public void onBackPressed() {
        blowup(mSrcView);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.share_src) {
            blowup(mSrcView);
        } else if (id == R.id.share_wechat) {
            mPresenter.share(Share.TYPE_WX);
        } else if (id == R.id.share_wechatfriends) {
            mPresenter.share(Share.TYPE_WXF);
        } else if (id == R.id.share_sina_weibo) {
            mPresenter.share(Share.TYPE_WB);
        } else if (id == R.id.share_qq) {
            mPresenter.share(Share.TYPE_QQ);
        }
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public Bitmap getBitmap() {
        return mSrc;
    }

    @Override
    public String getShareTitle() {
        return mTitle;
    }

    @Override
    public String getShareSummary() {
        return mSummary;
    }
}
