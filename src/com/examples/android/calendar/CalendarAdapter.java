/*
 * Copyright 2011 Lauri Nevala.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.examples.android.calendar;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CalendarAdapter extends BaseAdapter {
	static final int FIRST_DAY_OF_WEEK = 0; // Sunday = 0, Monday = 1

	private final Context mContext;

	private final java.util.Calendar month;
	private final Calendar selectedDate;
	private ArrayList<String> items;

	public CalendarAdapter(final Context c, final Calendar monthCalendar) {
		month = monthCalendar;
		selectedDate = (Calendar) monthCalendar.clone();
		mContext = c;
		month.set(Calendar.DAY_OF_MONTH, 1);
		this.items = new ArrayList<String>();
		refreshDays();
	}

	public void setItems(final ArrayList<String> items) {
		this.items = items;
	}

	public int getCount() {
		return days.length;
	}

	public Object getItem(final int position) {
		return null;
	}

	public long getItemId(final int position) {
		return 0;
	}

	// create a new view for each item referenced by the Adapter
	public View getView(final int position, final View convertView, final ViewGroup parent) {
		View v = convertView;
		
		if (convertView == null) { // if it's not recycled, initialize some attributes
			final LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.calendar_item, null);
		}
		
		final TextView dayView = (TextView) v.findViewById(R.id.date);
		final ImageView iw = (ImageView) v.findViewById(R.id.date_icon);

		// create date string for comparison
		final String dateStr = days[position];
		
		dayView.setText(dateStr);

		// disable empty days from the beginning
		if (dateStr == null) {
			dayView.setClickable(false);
			dayView.setFocusable(false);
			v.setBackgroundResource(0);
			iw.setVisibility(View.INVISIBLE);
		} else {
			// mark current day as focused
			if (month.get(Calendar.YEAR) == selectedDate.get(Calendar.YEAR)
			    && month.get(Calendar.MONTH) == selectedDate.get(Calendar.MONTH)
			    && days[position].equals("" + selectedDate.get(Calendar.DAY_OF_MONTH))) {
				v.setBackgroundResource(R.drawable.item_background_focused);
			} else {
				v.setBackgroundResource(R.drawable.list_item_background);
			}
			
			// show icon if date is not empty and it exists in the items array
			if (items != null && items.contains(dateStr)) {
				iw.setVisibility(View.VISIBLE);
			} else {
				iw.setVisibility(View.INVISIBLE);
			}
		}

		return v;
	}

	public void refreshDays() {
		// clear items
		items.clear();

		final int lastDay = month.getActualMaximum(Calendar.DAY_OF_MONTH);
		final int firstDay = (int) month.get(Calendar.DAY_OF_WEEK);

		// figure size of the array
		if (firstDay == 1) {
			days = new String[lastDay + (FIRST_DAY_OF_WEEK * 6)];
		} else {
			days = new String[lastDay + firstDay - (FIRST_DAY_OF_WEEK + 1)];
		}

		final int j = firstDay > 1 ?
				firstDay - FIRST_DAY_OF_WEEK
				: FIRST_DAY_OF_WEEK * 6 + 1; // sunday => 1, monday => 7

		/*
		// populate empty days before first real day
		if (firstDay > 1) {
			j = firstDay - FIRST_DAY_OF_WEEK;
		} else {
			j = FIRST_DAY_OF_WEEK * 6 + 1; // sunday => 1, monday => 7
		}
		*/

		// populate days
		for (int i = j - 1, dayNumber=1; i < days.length; i++, dayNumber++) {
			days[i] = "" + dayNumber;
		}
	}

	// references to our items
	public String[] days;
}