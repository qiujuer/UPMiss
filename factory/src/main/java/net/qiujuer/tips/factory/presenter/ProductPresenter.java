package net.qiujuer.tips.factory.presenter;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;
import net.qiujuer.tips.factory.model.api.ProductModel;
import net.qiujuer.tips.factory.model.api.ProductVersionModel;
import net.qiujuer.tips.factory.model.xml.SettingModel;
import net.qiujuer.tips.factory.util.http.HttpJsonObjectRequest;
import net.qiujuer.tips.factory.view.ProductView;

import org.json.JSONObject;

import java.lang.ref.SoftReference;
import java.util.List;

/**
 * Created by qiujuer
 * on 15/8/17.
 */
public class ProductPresenter {
    private static boolean ACTIVE_UPDATE;

    public static String getProductNewestUrl() {
        return "http://api.qiujuer.net/api/Product/9";
    }

    private SoftReference<ProductView> mView;

    private ProductView getView() {
        if (mView != null)
            return mView.get();
        return null;
    }


    public void update(ProductView view) {
        mView = new SoftReference<ProductView>(view);

        if (ACTIVE_UPDATE) return;
        ACTIVE_UPDATE = true;

        String url = getProductNewestUrl();
        HttpJsonObjectRequest jsonRequestObject = new HttpJsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            ProductModel product = ProductModel.fromJson(response);
                            if (product != null)
                                onSuccess(product);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        ACTIVE_UPDATE = false;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        onError();
                        ACTIVE_UPDATE = false;
                    }
                });
        AppPresenter.getRequestQueue().add(jsonRequestObject);
    }

    private ProductVersionModel getVersion(ProductModel model) {
        List<ProductVersionModel> versionModels = model.getVersions();
        if (versionModels == null || versionModels.size() == 0)
            return null;
        ProductVersionModel versionModel = versionModels.get(0);
        for (ProductVersionModel v : versionModels) {
            if (v.getVerCode() > versionModel.getVerCode())
                versionModel = v;
        }
        return versionModel;
    }

    private void onSuccess(final ProductModel model) {
        final ProductVersionModel versionModel = getVersion(model);
        if (versionModel != null && versionModel.getVerCode() > SettingModel.getVerCode()) {
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    showNewProduct(versionModel);
                }
            });
        } else {
            showIsNew();
        }
    }

    private void onError() {
        showIsNew();
    }

    private void showNewProduct(ProductVersionModel model) {
        ProductView view = getView();
        if (view != null)
            view.showNewProduct(model);
    }

    private void showIsNew() {
        ProductView view = getView();
        if (view != null)
            view.showIsNew();
    }
}
