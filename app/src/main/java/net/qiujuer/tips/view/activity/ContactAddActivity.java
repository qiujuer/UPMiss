package net.qiujuer.tips.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import net.qiujuer.tips.R;
import net.qiujuer.tips.factory.model.db.ContactModel;
import net.qiujuer.tips.factory.presenter.ContactAddPresenter;
import net.qiujuer.tips.factory.view.ContactAddView;
import net.qiujuer.tips.view.util.RelationManger;

public class ContactAddActivity extends BlurActivity implements ContactAddView,
        View.OnClickListener {
    protected EditText mEdtTxtName;
    protected Button mBtnQuickAdd;
    protected EditText mEdtTxtPhone;
    protected EditText mEdtTxtQQ;
    protected RadioGroup mRdoBtnGender;
    protected TextView mTxtContactsRelation;
    protected Button mBtnSave;
    private ContactAddPresenter mPresenter;
    protected int mGender;
    protected int mRelation;
    private String[] mRelationStr;

    public static void actionStart(BaseActivity context) {
        Intent intent = new Intent(context, ContactAddActivity.class);
        context.startActivity(intent);
        context.setBlur(context);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_contacts_add;
    }

    @Override
    protected void onInit(Bundle savedInstanceState) {
        // Find
        mEdtTxtName = (EditText) findViewById(R.id.contacts_edit_name);
        mBtnQuickAdd = (Button) findViewById(R.id.contacts_quick_add);
        mEdtTxtPhone = (EditText) findViewById(R.id.contacts_edit_phone_number);
        mEdtTxtQQ = (EditText) findViewById(R.id.contacts_edit_QQ);
        mRdoBtnGender = (RadioGroup) findViewById(R.id.contacts_radio_gender);
        mTxtContactsRelation = (TextView) findViewById(R.id.contacts_txt_relation);
        mBtnSave = (Button) findViewById(R.id.btn_save);

        // Set Listener
        mRdoBtnGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.contacts_radio_gender_man) {
                    mGender = ContactModel.GENDER_MAN;
                } else if (checkedId == R.id.contacts_radio_gender_woman) {
                    mGender = ContactModel.GENDER_WOMAN;
                }
                setRelation(mRelation);
            }
        });

        // Init value
        onInitValues();
        mRelationStr = getResources().getStringArray(R.array.array_contacts_relations);
        mBtnQuickAdd.setOnClickListener(this);
        mBtnSave.setOnClickListener(this);
        mTxtContactsRelation.setOnClickListener(this);

        // Init presenter
        onInitPresenter();
    }

    @Override
    protected void onInitToolBar() {
        super.onInitToolBar();
        mToolbar.setTitle(getTitle());
        mToolbar.setNavigationIcon(R.mipmap.ic_action_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    protected void onInitValues() {
        mTxtContactsRelation.setText(R.string.txt_contacts_man);
        mGender = ContactModel.GENDER_MAN;
        mRelation = 1;
    }

    protected void onInitPresenter() {
        mPresenter = new ContactAddPresenter(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor cursor = managedQuery(contactData, null, null, null,
                            null);
                    cursor.moveToFirst();

                    String Info[] = this.getContactPhone(cursor);
                    if (Info == null) {
                        Toast.makeText(this, R.string.txt_contacts_gain_failure, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mEdtTxtName.setText(Info[0]);
                    mEdtTxtPhone.setText(Info[1]);
                }
                break;
            default:
                break;
        }
    }

    private String[] getContactPhone(Cursor cursor) {
        int phoneColumn = cursor
                .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
        try {
            int phoneNum = cursor.getInt(phoneColumn);

            String[] result = new String[2];
            if (phoneNum > 0) {
                // 获得联系人的ID号
                int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
                String contactId = cursor.getString(idColumn);
                // 获得联系人电话的cursor
                Cursor phone = getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "="
                                + contactId, null, null);
                if (phone == null)
                    return null;
                if (phone.moveToFirst()) {
                    for (; !phone.isAfterLast(); phone.moveToNext()) {
                        int index = phone
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        result[1] = phone.getString(index);
                        int name = phone
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                        result[0] = phone.getString(name);
                    }
                    if (!phone.isClosed()) {
                        phone.close();
                    }
                }
            }
            return result;
        } catch (Exception e) {
            return null;
        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.contacts_quick_add) {
            Intent intent = new Intent(Intent.ACTION_PICK,
                    ContactsContract.Contacts.CONTENT_URI);
            ContactAddActivity.this.startActivityForResult(intent, 1);
        } else if (id == R.id.btn_save) {
            mPresenter.create();
        } else if (id == R.id.contacts_txt_relation) {
            if (mEdtTxtName.getText() == null || mEdtTxtName.getText().length() < 1) {
                Toast.makeText(this, R.string.txt_contacts_name, Toast.LENGTH_SHORT).show();
            } else {
                final RelationManger selector = new RelationManger(getLayoutInflater(), mRelation
                        , mGender);
                AlertDialog dialog = showDialog(ContactAddActivity.this, R.string.title_dialog_relation,
                        selector.getView(), null,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mRelation = selector.getSelectRelation();
                                setRelation(mRelation);
                            }
                        });
                dialog.show();
            }
        }
    }

    @Override
    public String getNameStr() {
        return mEdtTxtName.getText().toString();
    }

    @Override
    public String getPhoneNumber() {
        return mEdtTxtPhone.getText().toString();
    }

    @Override
    public String getQQ() {
        return mEdtTxtQQ.getText().toString();
    }

    @Override
    public void setRelation(int relation) {
        //"1" is man
        int mark;
        mRelation = relation;
        if (mGender == 1) {
            mark = mRelation + 6;
        } else {
            if (mRelation > 6) {
                mark = mRelation + 6;
            } else {
                mark = mRelation;
            }
        }
        mTxtContactsRelation.setText(mRelationStr[mark]);
    }

    @Override
    public int getGender() {
        return mGender;
    }

    @Override
    public int getRelation() {
        return mRelation;
    }

    @Override
    public void setStatus(long status) {
        if (status == -1)
            Toast.makeText(this, R.string.txt_contacts_save_failing, Toast.LENGTH_SHORT).show();
        else if (status == -2)
            Toast.makeText(this, R.string.txt_contacts_name, Toast.LENGTH_SHORT).show();
        else if (status == -3)
            Toast.makeText(this, R.string.txt_contacts_people, Toast.LENGTH_SHORT).show();
        else if (status >= 0) {
            //Toast.makeText(this, "New: " + status, Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
