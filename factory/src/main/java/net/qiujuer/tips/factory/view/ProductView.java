package net.qiujuer.tips.factory.view;

import net.qiujuer.tips.factory.model.api.ProductVersionModel;

/**
 * Created by qiujuer
 * on 15/8/17.
 */
public interface ProductView {
    void showIsNew();

    void showNewProduct(ProductVersionModel model);
}
