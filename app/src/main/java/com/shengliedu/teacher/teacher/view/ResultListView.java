package com.shengliedu.teacher.teacher.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.shengliedu.teacher.teacher.adapter.IBaseAdapter;

/**
 * Created by liyang21 on 2016/11/22.
 * Email:liyang21@le.com
 */
public class ResultListView extends LinearLayout {
    private IBaseAdapter mListAdapter;

    public ResultListView(Context context) {
        super(context);
    }

    public ResultListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @SuppressLint("NewApi")
	public ResultListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setAdapter(IBaseAdapter adapter) {
        if (adapter == null) {
            return;
        }
        removeAllViews();
        mListAdapter = adapter;
        addChildView(mListAdapter.getCount(), mListAdapter.getRow(), mListAdapter.getColumn());
    }

    public void addChildView(int itemCount, int row, int column) {
    	LinearLayout layout;
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams
                .WRAP_CONTENT);
        for (int i = 0; i < row; i++) {
            layout = new LinearLayout(getContext());
            layoutParams.setMargins(0, 0, 0, mListAdapter.getDivider());
            layout.setOrientation(HORIZONTAL);
            layout.setLayoutParams(layoutParams);
//            layout.setFocusable(false);
//            layout.setFocusableInTouchMode(false);
            addView(layout);
            for (int j = 0; j < column; j++) {
                final int position = i * column + j;
                if (position + 1 > itemCount) {
                    return;
                }
                final View item = mListAdapter.getView(layout, position);
                LayoutParams itemParams = (LayoutParams) item.getLayoutParams();
                if (itemParams == null) {
                    itemParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup
                            .LayoutParams.MATCH_PARENT);
                }
                if ((j + 1) % column == 0) {
                    itemParams.setMargins(0, 0, 0, 0);
                } else {
                    itemParams.setMargins(0, 0, mListAdapter.getDivider(), 0);
                }
                item.setLayoutParams(itemParams);
                if (item != null) {
                    layout.addView(item);
                }
            }
        }
    }

}
