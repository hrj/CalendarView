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
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

public class CalendarView extends Activity {

	private Calendar month;
	private CalendarAdapter adapter;
	final private Handler handler = new Handler();
	private ArrayList<String> items; // container to store special dates

	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calendar);
		month = Calendar.getInstance();
		onNewIntent(getIntent());

		items = new ArrayList<String>();
		adapter = new CalendarAdapter(this, month);

		final GridView gridview = (GridView) findViewById(R.id.gridview);
		gridview.setAdapter(adapter);

		handler.post(calendarUpdater);

		final TextView title = (TextView) findViewById(R.id.title);
		title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));

		final TextView previous = (TextView) findViewById(R.id.previous);
		previous.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				if (month.get(Calendar.MONTH) == month.getActualMinimum(Calendar.MONTH)) {
					month.set((month.get(Calendar.YEAR) - 1),
					    month.getActualMaximum(Calendar.MONTH), 1);
				} else {
					month.set(Calendar.MONTH, month.get(Calendar.MONTH) - 1);
				}
				refreshCalendar();
			}
		});

		final TextView next = (TextView) findViewById(R.id.next);
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				if (month.get(Calendar.MONTH) == month.getActualMaximum(Calendar.MONTH)) {
					month.set((month.get(Calendar.YEAR) + 1),
					    month.getActualMinimum(Calendar.MONTH), 1);
				} else {
					month.set(Calendar.MONTH, month.get(Calendar.MONTH) + 1);
				}
				refreshCalendar();

			}
		});

		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(final AdapterView<?> parent, final View v,
			    final int position, final long id) {
				final TextView date = (TextView) v.findViewById(R.id.date);
				if (date instanceof TextView && !date.getText().equals("")) {

					final Intent intent = new Intent();
					/*
					 * String day = date.getText().toString(); if(day.length()==1) { day =
					 * "0"+day; }
					 */
					final int day = Integer.parseInt(date.getText().toString());
					// return chosen date as string format
					intent.putExtra("date-year", month.get(Calendar.YEAR));
					intent.putExtra("date-month", month.get(Calendar.MONTH));
					intent.putExtra("date-day", day);
					// intent.putExtra("date",
					// android.text.format.DateFormat.format("yyyy-MM", month)+"-"+day);
					setResult(RESULT_OK, intent);
					finish();
				}

			}
		});
	}

	public void refreshCalendar() {
		final TextView title = (TextView) findViewById(R.id.title);

		adapter.refreshDays();
		// adapter.notifyDataSetChanged();
		handler.post(calendarUpdater); // generate some random calendar items

		title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
	}

	public void onNewIntent(final Intent intent) {
		final int yearIn = intent.getIntExtra("date-year", 0);
		final int monthIn = intent.getIntExtra("date-month", 0);
		final int dayIn = intent.getIntExtra("date-day", 0);
		month.set(yearIn, monthIn, dayIn);
	}

	public Runnable calendarUpdater = new Runnable() {

		@Override
		public void run() {
			items.clear();
			// mark all multiples of month value
			final int j = month.get(Calendar.MONTH) + 1;
			for (int i = 0; i < 31; i++) {
				if (i % j == 0) {
					items.add(Integer.toString(i));
				}
			}

			adapter.setItems(items);
			adapter.notifyDataSetChanged();
		}
	};
}
