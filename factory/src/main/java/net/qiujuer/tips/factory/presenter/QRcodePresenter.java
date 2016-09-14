package net.qiujuer.tips.factory.presenter;

import net.qiujuer.tips.factory.model.code.SimpleRecordModel;


public class QRcodePresenter {
    public final static String MARK_ADD_RECORD = "TIPS_A";
    public final static String MARK_FLOW = "TIPS_B";

    public Object decode(String str) {
        if (str == null || str.length() == 0)
            return null;

        if (str.contains(MARK_ADD_RECORD)) {
            return decodeAddRecord(str);
        } else if (str.contains(MARK_FLOW))
            return decodeFlow(str);

        return null;
    }

    private Object decodeAddRecord(String str) {
        str = str.substring(MARK_ADD_RECORD.length());
        return SimpleRecordModel.fromJson(str);
    }

    private Object decodeFlow(String str) {
        str = str.substring(MARK_FLOW.length());
        return str;
    }
}
