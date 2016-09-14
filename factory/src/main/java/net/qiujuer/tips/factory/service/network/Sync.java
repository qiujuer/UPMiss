package net.qiujuer.tips.factory.service.network;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import net.qiujuer.tips.factory.R;
import net.qiujuer.tips.factory.model.Model;
import net.qiujuer.tips.factory.model.api.ContactTransModel;
import net.qiujuer.tips.factory.model.api.RecordTransModel;
import net.qiujuer.tips.factory.model.api.RspModel;
import net.qiujuer.tips.factory.model.api.SyncPullModel;
import net.qiujuer.tips.factory.model.api.SyncPullRspModel;
import net.qiujuer.tips.factory.model.api.SyncPushModel;
import net.qiujuer.tips.factory.model.api.SyncPushRspModel;
import net.qiujuer.tips.factory.model.db.ContactModel;
import net.qiujuer.tips.factory.model.db.RecordModel;
import net.qiujuer.tips.factory.model.xml.AccountPreference;
import net.qiujuer.tips.factory.util.http.HttpJsonObjectRequest;
import net.qiujuer.tips.factory.util.http.HttpKit;

import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

/**
 * Created by Chen
 * on 2015/8/17.
 */
public class Sync {
    private static Runnable mRunnable;
    private static SyncCallback mCallback;

    private static boolean isStart() {
        return mRunnable != null;
    }

    private static void setEnd(final int status) {
        mRunnable = null;
        SyncCallback callback = mCallback;
        mCallback = null;
        if (callback != null) {
            callback.syncStop(status);
        }
    }

    public static void sync(final SyncCallback callback) {
        if (isStart())
            return;

        mCallback = callback;

        // Thread
        mRunnable = new Runnable() {
            @Override
            public void run() {
                syncPull();
            }
        };
        Model.getThreadPool().execute(mRunnable);
    }

    private static void syncPull() {
        // Init Sync Pull Model
        SyncPullModel model = new SyncPullModel();

        JSONObject json = model.toJson();
        if (json == null)
            return;

        log("SyncPull: " + json.toString());

        HttpJsonObjectRequest jsonRequestObject = new HttpJsonObjectRequest(Request.Method.POST,
                HttpKit.getRecordSyncUrl(), json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        log("SyncPull OK: " + response.toString());

                        RspModel<SyncPullRspModel> model = RspModel.fromJson(response, SyncPullRspModel.getRspType());
                        if (model == null) {
                            setEnd(R.string.status_error_un_know);
                            return;
                        }
                        if (model.isOk()) {
                            SyncPullRspModel rspModel = model.getResult();
                            if (rspModel == null) {
                                syncPush();
                            } else {
                                // Last
                                AccountPreference.updateLastDate(rspModel.getLast());
                                if (rspModel.isNull()) {
                                    syncPush();
                                } else {
                                    syncPull(rspModel);
                                }
                            }
                        } else {
                            setEnd(model.getStatusStringRes());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        log("SyncPull Error: " + error.toString());
                        setEnd(R.string.status_network_error);
                    }
                });
        Model.getRequestQueue().add(jsonRequestObject);
    }

    private static void syncPull(SyncPullRspModel rsp) {
        SyncChangeModel syncModel = new SyncChangeModel();

        // ContactEdit
        List<ContactTransModel> contacts = rsp.getContactEdit();
        if (contacts.size() > 0) {
            //ActiveAndroid.beginTransaction();
            for (ContactTransModel trans : contacts) {
                try {
                    ContactModel model = ContactModel.get(trans.getId());
                    if (model == null) {
                        model = trans.create();
                        syncModel.addContactAdd(model.getMark());
                    } else {
                        trans.edit(model);
                        syncModel.addContactEdit(model.getMark());
                    }
                    model.save();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //ActiveAndroid.setTransactionSuccessful();
            //ActiveAndroid.endTransaction();
        }

        // ContactAdd
        contacts = rsp.getContactAdd();
        if (contacts.size() > 0) {
            //ActiveAndroid.beginTransaction();
            for (ContactTransModel trans : contacts) {
                try {
                    ContactModel model = ContactModel.get(trans.getId());
                    if (model == null) {
                        model = trans.create();
                        syncModel.addContactAdd(model.getMark());
                    } else {
                        trans.edit(model);
                        syncModel.addContactEdit(model.getMark());
                    }
                    model.save();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //ActiveAndroid.setTransactionSuccessful();
            //ActiveAndroid.endTransaction();
        }

        // RecordDelete
        List<UUID> deletes = rsp.getRecordDelete();
        if (deletes.size() > 0) {
            //ActiveAndroid.beginTransaction();
            for (UUID id : deletes) {
                try {
                    RecordModel model = RecordModel.get(id);
                    if (model != null) {
                        model.delete();
                        syncModel.addRecordDelete(id);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //ActiveAndroid.setTransactionSuccessful();
            //ActiveAndroid.endTransaction();
        }

        // RecordEdit
        List<RecordTransModel> records = rsp.getRecordEdit();
        if (records.size() > 0) {
            //ActiveAndroid.beginTransaction();
            for (RecordTransModel trans : records) {
                try {
                    RecordModel model = RecordModel.get(trans.getId());
                    if (model == null) {
                        model = trans.create();
                        syncModel.addRecordAdd(trans.getId());
                    } else {
                        trans.edit(model);
                        syncModel.addRecordEdit(trans.getId());
                    }
                    model.save();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //ActiveAndroid.setTransactionSuccessful();
            //ActiveAndroid.endTransaction();
        }

        // RecordAdd
        records = rsp.getRecordAdd();
        if (records.size() > 0) {
            //ActiveAndroid.beginTransaction();
            for (RecordTransModel trans : records) {
                try {
                    RecordModel model = RecordModel.get(trans.getId());
                    if (model == null) {
                        model = trans.create();
                        syncModel.addRecordAdd(trans.getId());
                    } else {
                        trans.edit(model);
                        syncModel.addRecordEdit(trans.getId());
                    }
                    model.save();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //ActiveAndroid.setTransactionSuccessful();
            //ActiveAndroid.endTransaction();
        }

        // Update
        mCallback.syncUpdate(syncModel);

        // Sync
        syncPush();
    }

    private static void syncPush() {
        SyncPushModel model = SyncPushModel.build();
        if (model == null) {
            setEnd(R.string.status_sync_succeed);
            return;
        }

        JSONObject json = model.toJson();
        if (json == null)
            return;

        log("SyncPush json: " + json.toString());

        HttpJsonObjectRequest jsonRequestObject = new HttpJsonObjectRequest(Request.Method.POST, HttpKit.getRecordPushUrl(), json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        log("SyncPush OK: " + response.toString());
                        RspModel<SyncPushRspModel> model = RspModel.fromJson(response, SyncPushRspModel.getRspType());
                        if (model == null) {
                            setEnd(R.string.status_error_un_know);
                            return;
                        }

                        if (model.isOk()) {
                            SyncPushRspModel pushRspModel = model.getResult();

                            if (pushRspModel == null || pushRspModel.isNull()) {
                                setEnd(R.string.status_sync_failed);
                            } else {
                                AccountPreference.updateLastDate(pushRspModel.getLast());
                                syncPush(pushRspModel);
                            }
                        } else {
                            setEnd(model.getStatusStringRes());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        log("SyncPush Error: " + error.toString());
                        setEnd(R.string.status_network_error);
                    }
                });
        Model.getRequestQueue().add(jsonRequestObject);
    }

    private static void syncPush(SyncPushRspModel rsp) {
        //ActiveAndroid.beginTransaction();
        List<SyncPushRspModel.PushStateModel> stateModels = rsp.getRecords();
        for (SyncPushRspModel.PushStateModel out : stateModels) {
            RecordModel recordModel = RecordModel.get(out.getId());
            if (recordModel == null)
                return;
            if (out.getType() == SyncPushRspModel.PushStateModel.TYPE_DELETE) {
                recordModel.delete();
            } else {
                recordModel.setLast(recordModel.getLast());
                recordModel.setStatus(RecordModel.STATUS_UPLOADED);
            }
            recordModel.save();
        }

        stateModels = rsp.getContacts();
        for (SyncPushRspModel.PushStateModel out : stateModels) {
            ContactModel contactModel = ContactModel.get(out.getId());
            if (contactModel == null)
                return;
            if (out.getType() == SyncPushRspModel.PushStateModel.TYPE_DELETE) {
                contactModel.delete();
            } else {
                contactModel.setLast(out.getLast());
                contactModel.setStatus(ContactModel.STATUS_UPLOADED);
            }
            contactModel.save();
        }
        //ActiveAndroid.setTransactionSuccessful();
        //ActiveAndroid.endTransaction();
        setEnd(R.string.status_sync_succeed);
    }

    private static void log(String msg) {
        if (Model.DEBUG)
            Log.e(Sync.class.getName(), msg);
    }
}
