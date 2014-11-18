package tinkercoder.budgetcalculator.navdrawer;

import tinkercoder.budgetcalculator.budgetcalculator.R;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DrawerItemAdapter extends ArrayAdapter<ObjectDrawerItem> {

	Context mContext;
	int layoutResourceId;
	ObjectDrawerItem data[] = null;

	// constructor
	public DrawerItemAdapter(Context mContext, int layoutResourceId,
			ObjectDrawerItem[] data) {

		super(mContext, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.mContext = mContext;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();

		ObjectDrawerItem folder = data[position];

//		if (convertView == null) {
//			if (position == 1 || position == 7) {// title
//				convertView = inflater
//						.inflate(R.layout.drawer_list_title, null);
//
//				TextView textViewName = (TextView) convertView
//						.findViewById(R.id.text_view_title);
//
//				textViewName.setText(folder.name);
//
//				// disable item click event
//				convertView.setOnClickListener(null);
//				convertView.setOnLongClickListener(null);
//				convertView.setLongClickable(false);
//			} else {
		if (convertView == null) {
				convertView = inflater.inflate(R.layout.drawer_list_item, null);

				ImageView imageViewIcon = (ImageView) convertView
						.findViewById(R.id.image_view_icon);
				TextView textViewName = (TextView) convertView
						.findViewById(R.id.text_view_name);

				imageViewIcon.setImageResource(folder.icon);
				textViewName.setText(folder.name);
			}
//		}
		return convertView;
	}

}