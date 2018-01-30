package net.qiujuer.tips.open;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.widget.Toast;

import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import net.qiujuer.tips.common.tools.FileTool;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * UPMiss share
 */
public class Share {
    private static final int THUMB_SIZE = 256;

    public static final int TYPE_WX = 1;
    public static final int TYPE_WXF = 2;
    public static final int TYPE_WB = 3;
    public static final int TYPE_QQ = 4;

    private static Tencent TENCENT;
    private static IWXAPI WX_API;
    private static WeakReference<WbShareHandler> WB_API_REF;

    public static void init(Context context) {
        // 微博初始化
        AuthInfo mAuthInfo = new AuthInfo(context, Constants.WB_APP_ID, Constants.WB_REDIRECT_URL, Constants.WB_SCOPE);
        WbSdk.install(context, mAuthInfo);
    }

    private synchronized static Tencent getTencent(Context context) {
        if (TENCENT == null) {
            TENCENT = Tencent.createInstance(Constants.QQ_APP_ID, context.getApplicationContext());
        }
        return TENCENT;
    }

    public static void onActivityResult(Context context, int requestCode, int resultCode, Intent data) {
        if (requestCode == com.tencent.connect.common.Constants.REQUEST_QQ_SHARE) {
            Tencent.onActivityResultData(requestCode, resultCode, data, getQQShareListener(context));
        }
    }

    public static void onIntent(Activity activity, Intent intent) {
        if (intent != null && WB_API_REF != null && WB_API_REF.get() != null) {
            getWbApi(activity).doResultIntent(intent, getWbShareCallback(activity));
        }
    }

    private synchronized static IWXAPI getWxApi(Context context) {
        if (WX_API == null) {
            IWXAPI wxApi = WXAPIFactory.createWXAPI(context.getApplicationContext(), Constants.WX_APP_ID, true);
            wxApi.registerApp(Constants.WX_APP_ID);
            WX_API = wxApi;
        }
        return WX_API;
    }

    private synchronized static WbShareHandler getWbApi(Activity activity) {
        WbShareHandler handler;
        if (WB_API_REF == null || (handler = WB_API_REF.get()) == null) {
            handler = new WbShareHandler(activity);
            handler.registerApp();
            WB_API_REF = new WeakReference<>(handler);
        }
        return handler;
    }

    public static void share(String transaction, Activity activity, int type, String title, String summary, Bitmap img) {
        if (type == TYPE_WX) {
            shareToWechat(transaction, activity, title, summary, img, SendMessageToWX.Req.WXSceneSession);
        } else if (type == TYPE_WXF) {
            shareToWechat(transaction, activity, title, summary, img, SendMessageToWX.Req.WXSceneTimeline);
        } else if (type == TYPE_WB) {
            shareToWeibo(activity, title, summary, img);
        } else if (type == TYPE_QQ) {
            shareToQQ(activity, title, summary, img);
        }
    }

    private static void shareToQQ(Activity activity, String title, String summary, Bitmap img) {
        String path = FileTool.saveBitmap(img, "Image", "Share.png");
        if (path == null)
            return;

        final Bundle params = new Bundle();
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title + Constants.SUMMARY);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, Constants.TARGET_URL);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, summary);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, path);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, Constants.APP_NAME);
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, 0x00);
        getTencent(activity).shareToQQ(activity, params, getQQShareListener(activity));
    }

    private static void shareToWeibo(Activity activity, String title, String summary, Bitmap img) {
        ImageObject imageObject = new ImageObject();
        imageObject.imageData = bmpToByteArray(img);

        TextObject textObject = new TextObject();
        textObject.text = title + "\n" + summary + Constants.SUMMARY;

        WeiboMultiMessage message = new WeiboMultiMessage();
        message.textObject = textObject;
        message.imageObject = imageObject;

        WbShareHandler wbApi = getWbApi(activity);
        wbApi.shareMessage(message, false);
        // Bind cache
        activity.getWindow().getDecorView().setTag(wbApi);
    }


    private static void shareToWechat(String transaction, Activity activity, String title, String summary, Bitmap img, int flag) {
        IWXAPI wxApi = getWxApi(activity);

        WXImageObject imgObj = new WXImageObject(img);

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;
        msg.thumbData = bmpToThumbArray(img);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = transaction;
        req.message = msg;
        req.scene = flag;
        wxApi.sendReq(req);
    }


    private static WeakReference<WbShareCallback> WB_LISTENER_REF;

    private static WbShareCallback getWbShareCallback(Context context) {
        WbShareCallback wbShareCallback;
        if (WB_LISTENER_REF == null || (wbShareCallback = WB_LISTENER_REF.get()) == null) {
            wbShareCallback = new WbShareCallback();
            WB_LISTENER_REF = new WeakReference<>(wbShareCallback);
        }
        wbShareCallback.setContext(context);
        return wbShareCallback;
    }

    private static class WbShareCallback extends AbsShareCallback implements com.sina.weibo.sdk.share.WbShareCallback {

        @Override
        public void onWbShareSuccess() {
            showText(R.string.share_wb_code_success);
        }

        @Override
        public void onWbShareCancel() {
            showText(R.string.share_wb_code_cancel);
        }

        @Override
        public void onWbShareFail() {
            showText(R.string.share_wb_code_error);
        }
    }


    private static WeakReference<QQShareListener> QQ_LISTENER_REF;

    private static QQShareListener getQQShareListener(Context context) {
        QQShareListener qqShareListener;
        if (QQ_LISTENER_REF == null || (qqShareListener = QQ_LISTENER_REF.get()) == null) {
            qqShareListener = new QQShareListener();
            QQ_LISTENER_REF = new WeakReference<>(qqShareListener);
        }
        qqShareListener.setContext(context);
        return qqShareListener;
    }

    private static class QQShareListener extends AbsShareCallback implements IUiListener {
        @Override
        public void onCancel() {
            showText(R.string.share_qq_code_cancel);
        }

        @Override
        public void onComplete(Object response) {
            showText(R.string.share_qq_code_complete);
        }

        @Override
        public void onError(UiError e) {
            showText(R.string.share_qq_code_error);
        }
    }

    private static class AbsShareCallback {
        private WeakReference<Context> mContextRef;

        public void setContext(Context context) {
            mContextRef = new WeakReference<>(context);
        }

        protected void showText(int resId) {
            Context context = mContextRef.get();
            if (context == null) {
                return;
            }
            Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
        }
    }


    private static byte[] bmpToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream os = null;

        try {
            os = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            return os.toByteArray();
        } catch (Exception var12) {
            var12.printStackTrace();
            return null;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException var11) {
                var11.printStackTrace();
            }

        }
    }

    private static byte[] bmpToThumbArray(Bitmap bitmap) {
        float scale = THUMB_SIZE / (float) bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        // New Compress bitmap
        bitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        return bmpToByteArray(bitmap);
    }

}
