package net.qiujuer.tips.view.fragment;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.qiujuer.tips.R;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class GuideFragment extends Fragment {
    private int mTitle;
    private int mSrc;
    private int mDetail;

    public GuideFragment() {

    }

    public GuideFragment(int title, int src, int detail) {
        this.mTitle = title;
        this.mSrc = src;
        this.mDetail = detail;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_guide, container, false);
        TextView title = (TextView) mView.findViewById(R.id.txt_guide_pager_title);
        ImageView functionImg = (ImageView) mView.findViewById(R.id.img_guide_pager_content);
        TextView details = (TextView) mView.findViewById(R.id.txt_guide_pager_content_Details);

        title.setText(mTitle);
        functionImg.setImageResource(mSrc);
        details.setText(mDetail);
        return mView;
    }
}
