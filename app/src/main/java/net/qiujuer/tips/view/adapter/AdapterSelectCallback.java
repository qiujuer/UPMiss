package net.qiujuer.tips.view.adapter;

import java.util.UUID;

/**
 * This is a callback return select UUID
 */
public interface AdapterSelectCallback {
    void onItemSelected(UUID id);

    void setLoading(boolean isLoad);
}
