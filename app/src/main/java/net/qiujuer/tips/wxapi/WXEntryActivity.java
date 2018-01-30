package net.qiujuer.tips.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import net.qiujuer.tips.R;
import net.qiujuer.tips.open.Constants;

public class WXEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {
    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxentry);

        api = WXAPIFactory.createWXAPI(this, Constants.WB_APP_ID, false);
        try {
            api.handleIntent(getIntent(), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        Toast.makeText(this, "WX post new message.", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onResp(BaseResp baseResp) {
        int result;
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = R.string.share_wx_code_success;
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = R.string.share_wx_code_cancel;
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = R.string.share_wx_code_deny;
                break;
            case BaseResp.ErrCode.ERR_UNSUPPORT:
                result = R.string.share_wx_code_unsupported;
                break;
            case BaseResp.ErrCode.ERR_BAN:
                result = R.string.share_wx_code_ban;
                break;
            default:
                result = R.string.share_wx_code_unknown;
                Log.w(WXEntryActivity.class.getSimpleName(), "wx:resp:errCode:" + baseResp.errCode + ",errStr:"
                        + baseResp.errStr);
                break;
        }
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        finish();
    }
}
