package net.qiujuer.tips.view.util;

import android.view.LayoutInflater;
import android.view.View;

import net.qiujuer.tips.R;
import net.qiujuer.tips.common.widget.RelationView;

/**
 * Created by Chen on 2015/7/24.
 */
public class RelationManger {
    private View mRoot;
    private RelationView mContactsOfRelation;

    public RelationManger(LayoutInflater inflater, int relation, int gender) {
        mRoot = inflater.inflate(R.layout.dialog_relation_select, null);
        mContactsOfRelation = (RelationView) mRoot.findViewById(R.id.contacts_relation);
        mContactsOfRelation.setGender(gender);
        mContactsOfRelation.setRelation(relation);
    }

    public View getView() {
        return mRoot;
    }

    public int getSelectRelation() {
        return mContactsOfRelation.getRelation();
    }
}
