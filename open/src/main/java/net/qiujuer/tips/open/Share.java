package net.qiujuer.tips.open;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;

import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;
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
import net.qiujuer.tips.open.weibo.AccessTokenKeeper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

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
    private static IWeiboShareAPI WB_API;

    private static Context APP_CONTEXT;

    public static void init(Context context) {
        APP_CONTEXT = context;
        if (TENCENT == null)
            TENCENT = Tencent.createInstance(Constants.QQ_APP_ID, context);
    }

    private synchronized static Tencent getTencent() {
        if (TENCENT == null) {
            TENCENT = Tencent.createInstance(Constants.QQ_APP_ID, APP_CONTEXT);
        }
        return TENCENT;
    }


    private synchronized static IWXAPI getWxApi() {
        if (WX_API == null) {
            IWXAPI wxApi = WXAPIFactory.createWXAPI(APP_CONTEXT, Constants.WX_APP_ID, true);
            wxApi.registerApp(Constants.WX_APP_ID);
            WX_API = wxApi;
        }
        return WX_API;
    }

    private synchronized static IWeiboShareAPI getWbApi() {
        if (WB_API == null) {
            IWeiboShareAPI mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(APP_CONTEXT, Constants.WB_APP_ID);
            mWeiboShareAPI.registerApp();
            WB_API = mWeiboShareAPI;
        }
        return WB_API;
    }


    public static void share(String transaction, Activity activity, int type, String title, String summary, Bitmap img) {
        if (type == TYPE_WX) {
            shareToWechat(transaction, activity, title, summary, img, SendMessageToWX.Req.WXSceneSession);
        } else if (type == TYPE_WXF) {
            shareToWechat(transaction, activity, title, summary, img, SendMessageToWX.Req.WXSceneTimeline);
        } else if (type == TYPE_WB) {
            shareToWeibo(transaction, activity, title, summary, img);
        } else if (type == TYPE_QQ) {
            shareToQQ(activity, title, summary, img);
        }
    }

    public static void shareToQQ(Activity activity, String title, String summary, Bitmap img) {
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
        TENCENT.shareToQQ(activity, params, qqShareListener);
    }

    public static void shareToWeibo(String transaction, Activity activity, String title, String summary, Bitmap img) {
        ImageObject imageObject = new ImageObject();
        imageObject.imageData = bmpToByteArray(img);

        TextObject textObject = new TextObject();
        textObject.text = title + "\n" + summary + Constants.SUMMARY;

        WeiboMultiMessage message = new WeiboMultiMessage();
        message.textObject = textObject;
        message.imageObject = imageObject;

        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        request.transaction = transaction;
        request.multiMessage = message;

        AuthInfo authInfo = new AuthInfo(activity, Constants.WB_APP_ID, Constants.WB_REDIRECT_URL, Constants.WB_SCOPE);
        Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(APP_CONTEXT);
        String token = "";
        if (accessToken != null) {
            token = accessToken.getToken();
        }
        IWeiboShareAPI api = getWbApi();
        api.sendRequest(activity, request, authInfo, token, weiboAuthListener);
    }


    private static void shareToWechat(String transaction, Activity activity, String title, String summary, Bitmap img, int flag) {
        IWXAPI wxApi = getWxApi();

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


    private static WeiboAuthListener weiboAuthListener = new WeiboAuthListener() {

        @Override
        public void onWeiboException(WeiboException arg0) {

        }

        @Override
        public void onComplete(Bundle bundle) {
            Oauth2AccessToken newToken = Oauth2AccessToken.parseAccessToken(bundle);
            AccessTokenKeeper.writeAccessToken(APP_CONTEXT, newToken);
        }

        @Override
        public void onCancel() {

        }
    };

    private static IUiListener qqShareListener = new IUiListener() {
        @Override
        public void onCancel() {

        }

        @Override
        public void onComplete(Object response) {

        }

        @Override
        public void onError(UiError e) {

        }
    };

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
