package com.shengliedu.teacher.teacher.adapter;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.bean.LianXianItems;
import com.shengliedu.teacher.teacher.util.MyImageGetter;

import java.util.List;
import java.util.Map;

public class SynLianXianAdapter extends BaseAdapter {
	List<LianXianItems> afc;
	private LayoutInflater layoutInflater;
	private Map<Integer, Boolean> map;
	Context context;
	public SynLianXianAdapter(Context context, List<LianXianItems> afc,
			Map<Integer, Boolean> map) {
		super();
		this.context = context;
		this.afc = afc;
		this.map = map;
		layoutInflater = LayoutInflater.from(context);

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return afc.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return afc.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (arg1 == null) {
			holder = new ViewHolder();
			arg1 = layoutInflater.inflate(R.layout.syn_select_question_list,
					null);
			holder.syn_select_a = (TextView) arg1
					.findViewById(R.id.syn_select_a);
			arg1.setTag(holder);
		} else {
			holder = (ViewHolder) arg1.getTag();
		}
//		holder.syn_select_a.setText(Html.fromHtml(afc.get(arg0).title
//				+ ". " + afc.get(arg0).content));
		Spanned spanned = Html.fromHtml(afc.get(arg0).title
				+ ". " + afc.get(arg0).content,new MyImageGetter(context,holder.syn_select_a),null);
		holder.syn_select_a.setText(spanned);
		if (map.get(arg0) != null) {
			holder.syn_select_a.setBackgroundResource(R.mipmap.button2);
		} else {
			holder.syn_select_a.setBackgroundResource(R.mipmap.button1);
		}
		return arg1;
	}

	static class ViewHolder {
		TextView syn_select_a;
	}

}
