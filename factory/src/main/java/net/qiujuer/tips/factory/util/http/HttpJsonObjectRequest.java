package net.qiujuer.tips.factory.util.http;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;


public class HttpJsonObjectRequest extends JsonObjectRequest {
    private OnFinishListener mOnFinishListener;

    private RetryPolicy mRetryPolicy = new DefaultRetryPolicy(HttpKit.SOCKET_TIMEOUT_MS,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);


    public HttpJsonObjectRequest(int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
        setRetryPolicy(mRetryPolicy);
    }

    public HttpJsonObjectRequest(String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(url, jsonRequest, listener, errorListener);
        setRetryPolicy(mRetryPolicy);
    }

    @Override
    public RetryPolicy getRetryPolicy() {
        return mRetryPolicy;
    }

    public HttpJsonObjectRequest setOnFinishListener(OnFinishListener listener) {
        mOnFinishListener = listener;
        return this;
    }

    @Override
    protected void onFinish() {
        super.onFinish();
        OnFinishListener listener = mOnFinishListener;
        mOnFinishListener = null;
        if (listener != null) {
            listener.onFinish();
        }
    }

    public interface OnFinishListener {
        void onFinish();
    }
}