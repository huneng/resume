package com.huneng.activity;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.app.ListActivity;
import android.content.Intent;

public class FileManager extends ListActivity {
	List<String> items;
	private String rootPath, path;
	public static final int RESULT_FILE = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		items = new LinkedList<String>();
		rootPath = MainActivity.path;
		path = ""+rootPath;
		initList();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		path = path + '/' + items.get(position);
		initList();
		super.onListItemClick(l, v, position, id);
	}

	void initList() {
		File file = new File(path);
		if (file.isFile()) {
			endActivityAndSetResult();

		} else if (file.isDirectory()) {
			items.clear();
			File[] files = file.listFiles();
			int len = files.length;
			for (int i = 0; i < len; i++) {
				if (files[i].isFile()) {
					String fileName = files[i].getName();
					String suffix = fileName.substring(fileName
							.lastIndexOf('.'));
					if (!suffix.equals(".txt"))
						continue;
				}
				items.add(files[i].getName());

			}
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, items);
			this.setListAdapter(adapter);
		}
	}

	void endActivityAndSetResult() {
		Intent data = new Intent();
		data.putExtra("resume_file", path);
		this.setResult(RESULT_FILE, data);
		this.finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (path.equals(rootPath)) {
				path="";
				endActivityAndSetResult();
				return true;
			}
			File file = new File(path);
			path = file.getParent();
			initList();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
