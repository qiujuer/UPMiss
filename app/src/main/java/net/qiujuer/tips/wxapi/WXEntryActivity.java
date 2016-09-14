package net.qiujuer.tips.wxapi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import net.qiujuer.tips.R;
import net.qiujuer.tips.open.Constants;

public class WXEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        api = WXAPIFactory.createWXAPI(this, Constants.WB_APP_ID, false);
        api.handleIntent(getIntent(), this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxentry);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        Log.e(WXEntryActivity.class.getSimpleName(), "resp.errCode:" + baseResp.errCode + ",resp.errStr:"
                + baseResp.errStr);
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                //分享成功
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                //分享取消
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                //分享拒绝
                break;
        }
    }
}
