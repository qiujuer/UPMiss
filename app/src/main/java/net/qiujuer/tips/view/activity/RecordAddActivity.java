package net.qiujuer.tips.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import net.qiujuer.genius.res.Resource;
import net.qiujuer.tips.R;
import net.qiujuer.tips.factory.model.db.ContactModel;
import net.qiujuer.tips.factory.presenter.RecordAddPresenter;
import net.qiujuer.tips.factory.util.TipsCalender;
import net.qiujuer.tips.factory.view.RecordAddView;
import net.qiujuer.tips.view.adapter.AdapterSelectCallback;
import net.qiujuer.tips.view.adapter.ContactsAdapter;
import net.qiujuer.tips.view.util.ColorSelector;
import net.qiujuer.tips.view.util.DateManager;

import java.util.Calendar;
import java.util.Random;
import java.util.UUID;


public class RecordAddActivity extends BlurActivity implements RecordAddView,
        View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    protected EditText mBrief;
    protected RadioGroup mType;
    protected RadioGroup mTimeType;
    protected TextView mTime;
    protected View mColor;
    protected TextView mTxtContact;

    private RecordAddPresenter mPresenter;
    private ContactsAdapter mAdapter;
    private ContactModel mContactModel;

    public static void actionStart(BaseActivity context) {
        Intent intent = new Intent(context, RecordAddActivity.class);
        context.setBlur(context);
        context.startActivity(intent);
    }

    public static void actionStart(BaseActivity context, int type, String brief, int color, long date) {
        Intent intent = new Intent(context, RecordAddActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("brief", brief);
        intent.putExtra("color", color);
        intent.putExtra("date", date);
        context.setBlur(context);
        context.startActivity(intent);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_edit;
    }

    @Override
    protected void onInit(Bundle savedInstanceState) {
        // Find
        mBrief = (EditText) findViewById(R.id.edit_edit_brief);
        mTimeType = (RadioGroup) findViewById(R.id.edit_radio_time_type);
        mTime = (TextView) findViewById(R.id.edit_txt_time);
        mColor = findViewById(R.id.edit_view_color);
        mType = (RadioGroup) findViewById(R.id.edit_radio_type);
        mTxtContact = (TextView) findViewById(R.id.txt_view_contact_name);

        // Set Listener
        mTime.setOnClickListener(this);
        mColor.setOnClickListener(this);
        mTxtContact.setOnClickListener(this);
        findViewById(R.id.btn_save).setOnClickListener(this);

        // Init value
        onInitValues();
        onInitValues(getIntent().getExtras());

        // Init presenter
        onInitPresenter();

        // Add CheckedChangeListener
        mTimeType.setOnCheckedChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*
        final LinearLayout layout = (LinearLayout) findViewById(R.id.lay_data);
        assert layout != null;
        layout.setVisibility(View.VISIBLE);
        layout.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                Animation animation = AnimationUtils.loadAnimation(RecordAddActivity.this, R.anim.anim_in_slide_alpha_bottom_long);
                LayoutAnimationController lac = new LayoutAnimationController(animation);
                lac.setOrder(LayoutAnimationController.ORDER_NORMAL);
                lac.setDelay(0.28f);
                layout.setLayoutAnimation(lac);
            }

            @Override
            public void onViewDetachedFromWindow(View v) {

            }
        });
        */
    }

    protected void onInitValues(Bundle bundle) {
        if (bundle == null)
            return;
        int type = bundle.getInt("type");
        String brief = bundle.getString("brief");
        int color = bundle.getInt("color");
        long date = bundle.getLong("date");

        if (type <= 0 || brief == null || brief.length() == 0 || date < 100001010 || date > 999912311)
            return;

        // Type
        if (type == 1)
            mType.check(R.id.edit_radio_type_birthday);
        else if (type == 2)
            mType.check(R.id.edit_radio_type_memorial);
        else //if (type == 3)
            mType.check(R.id.edit_radio_type_future);

        // Brief
        mBrief.setText(brief);

        // Color
        mColor.setTag(color);
        mColor.setBackgroundColor(color);

        // Date
        TipsCalender tips = (TipsCalender) mTime.getTag();
        if (tips == null)
            tips = new TipsCalender(date);
        else
            tips.set(date);

        if (tips.getIsLunar())
            mTimeType.check(R.id.edit_radio_time_type_lunar);
        else
            mTimeType.check(R.id.edit_radio_time_type_solar);
        mTime.setTag(tips);
        mTime.setText(tips.toDate());
    }

    protected void onInitValues() {
        // Check
        mTimeType.check(R.id.edit_radio_time_type_solar);

        // Color
        int color = Resource.Color.COLORS[new Random().nextInt(Resource.Color.COLORS.length - 1) + 1];
        mColor.setTag(color);
        mColor.setBackgroundColor(color);

        // Time
        Calendar calendar = Calendar.getInstance();

        String year = String.format("%04d", calendar.get(Calendar.YEAR));
        String month = String.format("%02d", calendar.get(Calendar.MONTH) + 1);
        String day = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH));

        TipsCalender date = new TipsCalender(year + month + day + "0");
        mTime.setTag(date);
        mTime.setText(year + "-" + month + "-" + day);
        mTxtContact.setTag(1);
    }

    protected void onInitPresenter() {
        mPresenter = new RecordAddPresenter(this);
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

    @Override
    public int getType() {
        int id = mType.getCheckedRadioButtonId();
        if (id == R.id.edit_radio_type_birthday)
            return 1;
        else if (id == R.id.edit_radio_type_memorial)
            return 2;
        else //if (id == R.id.edit_radio_type_future)
            return 3;
    }

    @Override
    public String getBrief() {
        return mBrief.getText().toString();
    }

    public boolean isLunar() {
        int id = mTimeType.getCheckedRadioButtonId();
        return (id == R.id.edit_radio_time_type_lunar);
    }

    @Override
    public TipsCalender getDate() {
        TipsCalender date = (TipsCalender) mTime.getTag();
        date.setLunar(isLunar());
        return date;
    }

    @Override
    public int getColor() {
        return (int) mColor.getTag();
    }

    @Override
    public void setStatus(long status) {
        if (status == -1)
            Toast.makeText(this, R.string.txt_status_title_null, Toast.LENGTH_SHORT).show();
        else if (status >= 0) {
            //Toast.makeText(this, "New: " + status, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public ContactModel getContact() {
        return mContactModel;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.edit_txt_time) {
            final DateManager selector = new DateManager(getLayoutInflater(), getDate());

            AlertDialog dialog = showDialog(RecordAddActivity.this, R.string.title_select_date,
                    selector.getView(), null,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            TipsCalender date = selector.getDate();
                            mTime.setTag(date);
                            mTime.setText(date.toDate());
                        }
                    });
            dialog.show();
        } else if (id == R.id.edit_view_color) {

            // Create
            final ColorSelector selector = new ColorSelector(getLayoutInflater(), (int) mColor.getTag());

            //  Add Dialog
            AlertDialog dialog = showDialog(RecordAddActivity.this, R.string.title_select_color, selector.getView(), null,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            callBackColor(selector.getColor());
                        }
                    });
            dialog.show();
        } else if (id == R.id.btn_save) {
            // Create
            mPresenter.create();
        } else if (id == R.id.txt_view_contact_name) {
            //选择联系人
            View mRoot = getLayoutInflater().inflate(R.layout.fragment_contacts, null);
            final RecyclerView mRecycler = (RecyclerView) mRoot.findViewById(R.id.time_line_recycler);
            TextView mStatus = (TextView) mRoot.findViewById(R.id.text_status);
            final AlertDialog dialog = showDialog(RecordAddActivity.this,
                    R.string.txt_contacts_concern_relation, mRoot);
            mAdapter = new ContactsAdapter(mRecycler, mStatus, new AdapterSelectCallback() {
                @Override
                public void onItemSelected(UUID id) {
                    mContactModel = ContactModel.get(id);
                    mTxtContact.setText(mContactModel.getName());
                    dialog.cancel();
                }

                @Override
                public void setLoading(boolean isLoad) {

                }
            });
            mAdapter.refresh();
            //  Add Dialog

            dialog.show();
        }
    }

    private void callBackColor(int color) {
        mColor.setTag(color);
        mColor.setBackgroundColor(color);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        TipsCalender date = (TipsCalender) mTime.getTag();
        int dateDay = TipsCalender.coercionDay(date.getYear(), date.getMonth(), date.getDay()
                , isLunar());
        if (date.getDay() > dateDay) {
            date.setDay(dateDay);
            mTime.setTag(date);
            mTime.setText(date.toDate());
        }
    }
}
