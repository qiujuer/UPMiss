package net.qiujuer.tips.view.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;

import net.qiujuer.tips.Application;
import net.qiujuer.tips.R;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * This is Tips base activity
 */
public class BaseActivity extends BackgroundActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((Application) getApplication()).addActivity(this);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((Application) getApplication()).removeActivity(this);
    }

    public AlertDialog showDialog(Context context, View content) {
        return showDialog(context, 0, content);
    }

    public AlertDialog showDialog(Context context, int title, View content) {
        return showDialog(context, title, content, null, null);
    }

    public AlertDialog showDialog(Context context, int title, View content,
                                  DialogInterface.OnClickListener negativeButtonListener,
                                  DialogInterface.OnClickListener positiveButtonListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.Theme_Dialog);
        if (title != 0)
            builder.setTitle(title);
        if (content != null)
            builder.setView(content);
        builder.setNegativeButton(R.string.txt_dialog_negative, negativeButtonListener);
        builder.setPositiveButton(R.string.txt_dialog_positive, positiveButtonListener);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setGravity(Gravity.BOTTOM);
        return alertDialog;
    }

    public AlertDialog showDialog(Context context, String title, View content,
                                  String negativeButtonStr,
                                  String positiveButtonStr,
                                  DialogInterface.OnClickListener negativeButtonListener,
                                  DialogInterface.OnClickListener positiveButtonListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.Theme_Dialog);
        if (title != null)
            builder.setTitle(title);
        if (content != null)
            builder.setView(content);
        builder.setNegativeButton(negativeButtonStr, negativeButtonListener);
        builder.setPositiveButton(positiveButtonStr, positiveButtonListener);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setGravity(Gravity.BOTTOM);
        return alertDialog;
    }

    protected AlertDialog showDialogMsg(Context context, int title, View view, DialogInterface.OnClickListener positiveButtonListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.Theme_Dialog);
        if (title != 0)
            builder.setTitle(title);
        if (view != null)
            builder.setView(view);
        builder.setPositiveButton(R.string.txt_dialog_positive, positiveButtonListener);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setGravity(Gravity.BOTTOM);
        return alertDialog;
    }

    protected AlertDialog showDialogShare(Context context, int title, View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.Theme_Dialog);
        if (title != 0)
            builder.setTitle(title);
        if (view != null)
            builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setGravity(Gravity.BOTTOM);
        return alertDialog;
    }
}
