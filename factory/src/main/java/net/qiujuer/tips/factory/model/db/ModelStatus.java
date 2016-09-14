package net.qiujuer.tips.factory.model.db;

/**
 * Created by qiujuer
 * on 15/8/15.
 */
public interface ModelStatus {
    public final static int STATUS_DELETE = -1;
    public final static int STATUS_ADD = 0;
    public final static int STATUS_EDIT = 2;
    public final static int STATUS_UPLOADED = 8;
}
