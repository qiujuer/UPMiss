package net.qiujuer.tips.factory.model.api;

import com.google.gson.annotations.SerializedName;

import net.qiujuer.tips.factory.R;
import net.qiujuer.tips.factory.util.http.HttpKit;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * Created by JuQiu
 * on 16/9/5.
 */
public class RspModel<Result> {
    // -1：服务器错误
    public static final int CODE_ERROR_SERVER = -1;
    // 0：操作成功
    public static final int CODE_SUCCEED = 0;

    // AppInfo 验证失败
    public static final int CODE_ERROR_APPINFO_INVALID = 201;
    // Token 无效
    public static final int CODE_ERROR_TOKEN_INVALID = 202;

    // 已有该用户名
    public static final int CODE_ERROR_HAVE_NAME = 101;
    // 已有该邮箱
    public static final int CODE_ERROR_HAVE_EMAIL = 102;
    // 注册失败
    public static final int CODE_ERROR_REGISTER = 103;
    // 邮箱格式不正确
    public static final int CODE_ERROR_NOT_EMAIL = 104;

    // 账户不正确
    public static final int CODE_ERROR_ACCOUNT = 111;
    // 密码不正确
    public static final int CODE_ERROR_PASSWORD = 112;

    // 手机未绑定
    public static final int CODE_ERROR_SYNC_DEVICE_UNBIND = 121;

    @SerializedName("Code")
    private int code;
    @SerializedName("Message")
    private String message;
    @SerializedName("Time")
    private Date time;
    @SerializedName("Result")
    private Result result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public boolean isOk() {
        return code == CODE_SUCCEED;
    }

    public int getStatusStringRes() {
        switch (code) {
            case CODE_ERROR_SERVER:
                return R.string.status_server_error;
            case CODE_ERROR_APPINFO_INVALID:
            case CODE_ERROR_TOKEN_INVALID:
                return R.string.status_app_info_error;
            case CODE_ERROR_HAVE_NAME:
            case CODE_ERROR_HAVE_EMAIL:
                return R.string.status_account_register_have_email;
            case CODE_ERROR_REGISTER:
                return R.string.status_account_register_error;
            case CODE_ERROR_NOT_EMAIL:
                return R.string.status_account_register_not_email;
            case CODE_ERROR_ACCOUNT:
                return R.string.status_account_login_account_error;
            case CODE_ERROR_PASSWORD:
                return R.string.status_account_login_password_error;
            case CODE_ERROR_SYNC_DEVICE_UNBIND:
                return R.string.status_account_phone_unbind;
        }
        return R.string.status_error_un_know;
    }


    public static <R> RspModel<R> fromJson(JSONObject jsonObject, Type type) {
        try {
            return HttpKit.getRspGsonBuilder()
                    .create()
                    .fromJson(jsonObject.toString(), type);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
