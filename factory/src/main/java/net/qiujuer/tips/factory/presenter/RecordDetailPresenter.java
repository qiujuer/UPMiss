package net.qiujuer.tips.factory.presenter;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;
import net.qiujuer.tips.factory.R;
import net.qiujuer.tips.factory.cache.Cache;
import net.qiujuer.tips.factory.model.Model;
import net.qiujuer.tips.factory.model.db.RecordModel;
import net.qiujuer.tips.factory.view.RecordDetailView;

/**
 * Detail presenter
 */
public class RecordDetailPresenter {
    private RecordDetailView mView;
    private RecordModel mModel;

    public RecordDetailPresenter(RecordDetailView view) {
        mView = view;
    }

    public boolean refresh() {
        mModel = RecordModel.get(mView.getId());
        if (mModel != null) {
            mView.setType(mModel.getType());
            mView.setBrief(mModel.getBrief());
            mView.setTime(mModel.getDateCalender());
            mView.setColor(mModel.getColor());
            return true;
        }
        return false;
    }

    public void saveScreen() {
        Model.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                final RecordDetailView view = mView;
                if (view == null)
                    return;
                String path = view.saveToBitmapFile();
                if (path != null) {
                    Run.onUiAsync(new Action() {
                        @Override
                        public void call() {
                            view.setStatus(R.string.status_opt_save_ok);
                        }
                    });
                } else {
                    Run.onUiAsync(new Action() {
                        @Override
                        public void call() {
                            view.setStatus(R.string.status_opt_save_error);
                        }
                    });
                }
            }
        });
    }

    public void delete() {
        if (mModel != null) {
            try {
                if (mModel.isAdd()) {
                    mModel.delete();
                } else {
                    mModel.setStatus(RecordModel.STATUS_DELETE);
                    mModel.save();
                }
                Cache.getInstance().delete(mModel);
                mView.setStatus(R.string.status_opt_delete_ok);
            } catch (Exception e) {
                e.printStackTrace();
                mView.setStatus(R.string.status_opt_delete_error);
            }
        }
    }
}
