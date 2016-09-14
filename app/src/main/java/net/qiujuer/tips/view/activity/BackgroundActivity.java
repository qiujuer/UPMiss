package net.qiujuer.tips.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import net.qiujuer.genius.graphics.Blur;
import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;
import net.qiujuer.tips.common.drawable.AnimBitmapDrawable;
import net.qiujuer.tips.common.tools.FileTool;

import java.io.File;

/**
 * Created by JuQiu
 * on 16/6/28.
 */

public class BackgroundActivity extends AppCompatActivity {
    private static final int SCALE_FACTOR = 10;
    private static final int BLUR_LEVEL = 4;
    private static final boolean NEED_CUT = Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT;
    private static final Object mBlurBitmapLock = new Object();
    private static Bitmap mBlurBitmap;
    private static Bitmap mSrcBitmap;

    public String saveToBitmapFile() {
        Bitmap bitmap = formatBlurBitmap(this, false);
        final String path = FileTool.saveBitmap(bitmap, "Image", System.currentTimeMillis() + ".png");
        if (path != null) {
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    Uri uri = Uri.fromFile(new File(path));
                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                }
            });
        }
        return path;
    }

    public void setBlurSrcAsync() {
        mBlurBitmap = null;
        mSrcBitmap = getRootBitmap();

        Thread thread = new Thread("GiveTips-BuildBlur-Async-Thread") {
            @Override
            public void run() {
                blurBitmap(mSrcBitmap);
            }
        };
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        final View view = this.getWindow().getDecorView();
        if (view != null)
            view.setDrawingCacheEnabled(true);
    }

    private Bitmap getRootBitmap() {
        final View view = this.getWindow().getDecorView();
        Bitmap bitmap = view.getDrawingCache();

        int statusBarHeight = 0;
        // If need cut statusBar on sdk < KITKAT
        if (NEED_CUT) {
            Rect frame = new Rect();
            view.getWindowVisibleDisplayFrame(frame);
            statusBarHeight = frame.top;
        }

        // Src
        bitmap = Bitmap.createBitmap(bitmap, 0, statusBarHeight, bitmap.getWidth(),
                bitmap.getHeight() - statusBarHeight);
        view.destroyDrawingCache();

        return bitmap;
    }


    private void blurBitmap(Bitmap src) {
        // Compress
        Matrix matrix = new Matrix();
        matrix.postScale(1.0f / SCALE_FACTOR, 1.0f / SCALE_FACTOR);

        // New Compress bitmap
        Bitmap bitmap = Bitmap.createBitmap(src, 0, 0,
                src.getWidth(), src.getHeight(), matrix, true);

        bitmap = Blur.onStackBlurPixels(bitmap, BLUR_LEVEL, true);
        synchronized (mBlurBitmapLock) {
            mBlurBitmap = bitmap;
            mBlurBitmapLock.notifyAll();
        }
    }


    private static Bitmap formatBlurBitmap(Activity activity, boolean isMatrix) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);

        Bitmap bitmap = view.getDrawingCache();

        int statusBarHeight = 0;

        // If need cut statusBar on sdk < KITKAT
        if (NEED_CUT) {
            Rect frame = new Rect();
            view.getWindowVisibleDisplayFrame(frame);
            statusBarHeight = frame.top;
        }

        if (isMatrix) {
            // Compress
            Matrix matrix = new Matrix();
            matrix.postScale(1.0f / SCALE_FACTOR, 1.0f / SCALE_FACTOR);
            // New Compress bitmap
            bitmap = Bitmap.createBitmap(bitmap, 0, statusBarHeight,
                    bitmap.getWidth(), bitmap.getHeight() - statusBarHeight, matrix, true);
        } else {
            bitmap = Bitmap.createBitmap(bitmap, 0, statusBarHeight, bitmap.getWidth(),
                    bitmap.getHeight() - statusBarHeight);
        }

        view.destroyDrawingCache();
        return bitmap;
    }

    public void setBlur(final Activity activity) {
        mBlurBitmap = null;
        synchronized (mBlurBitmapLock) {
            Bitmap bitmap = formatBlurBitmap(activity, true);
            mBlurBitmap = Blur.onStackBlurPixels(bitmap, BLUR_LEVEL, true);
            mBlurBitmapLock.notifyAll();
        }
    }

    protected Drawable getBlur() {
        if (mBlurBitmap == null) {
            synchronized (mBlurBitmapLock) {
                if (mBlurBitmap == null) {
                    try {
                        mBlurBitmapLock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        AnimBitmapDrawable drawable = new AnimBitmapDrawable(mBlurBitmap);
        mBlurBitmap = null;
        return drawable;
    }

    protected Bitmap getBlurSrcBitmap() {
        if (mSrcBitmap == null) {
            synchronized (mBlurBitmapLock) {
                if (mSrcBitmap == null) {
                    try {
                        mBlurBitmapLock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        Bitmap bitmap = mSrcBitmap;
        mSrcBitmap = null;
        return bitmap;
    }


    protected void injectionBlurBackground() {
        // Now this nothing, will doing
    }
}
