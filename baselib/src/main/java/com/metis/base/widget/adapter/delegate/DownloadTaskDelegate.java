package com.metis.base.widget.adapter.delegate;

import com.metis.base.module.DownloadTaskImpl;
import com.metis.base.widget.adapter.MeDelegateType;

/**
 * Created by Beak on 2015/10/22.
 */
public class DownloadTaskDelegate extends BaseDelegate<DownloadTaskImpl> {

    public DownloadTaskDelegate(DownloadTaskImpl task) {
        super(task);
    }

    @Override
    public int getDelegateType() {
        return MeDelegateType.TYPE_DOWNLOADING_ITEM.getType();
    }

}
