package com.shengliedu.teacher.teacher.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.chat.constant.MyApplication;

/**
 * @Time 16/11/30 16 41
 */

public abstract class IBaseAdapter {
    protected int row = 2;
    protected int column = 2;

    public IBaseAdapter(int row, int column) {
        this.row = row;
        this.column = column;
    }

    protected int mDivider = (int) MyApplication.getInstance().getApplicationContext().getResources()
            .getDimension(R.dimen.search_result_item_divider);

    public abstract int getCount();

    public abstract View getView(ViewGroup parent, int posterPosition);

    public int getRow(){
        return row;
    }

    public int getColumn(){
        return column;
    }

    public int getDivider(){
        return mDivider;
    }

    void setDivider(int divider){
        this.mDivider = divider;
    }
}
