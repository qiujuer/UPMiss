package net.qiujuer.tips.factory.util.http;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONObject;


public class HttpJsonArrayRequest extends JsonArrayRequest {
    private RetryPolicy mRetryPolicy = new DefaultRetryPolicy(HttpKit.SOCKET_TIMEOUT_MS,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

    public HttpJsonArrayRequest(int method, String url, JSONObject jsonRequest, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
        setRetryPolicy(mRetryPolicy);
    }

    public HttpJsonArrayRequest(String url, JSONObject jsonRequest, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        super(url, jsonRequest, listener, errorListener);
        setRetryPolicy(mRetryPolicy);
    }

    @Override
    public RetryPolicy getRetryPolicy() {
        return mRetryPolicy;
    }
}