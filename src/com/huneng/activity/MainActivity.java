package com.huneng.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

public class MainActivity extends Activity {

	private static final int GET_FILE = 0;
	public static String path;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		path = Environment.getExternalStorageDirectory().getPath();
		path += "/ImageResume";
		File file = new File(path + "/img");
		if (!file.exists()) {
			file.mkdirs();
		}
		file = new File(path + "/file");
		if (!file.exists()) {
			file.mkdirs();
		}
		setContentView(R.layout.main);
	}

	public void editTextResume(View v) {
		Intent intent = new Intent();
		intent.setClass(this, ResumeEditor.class);
		startActivity(intent);
	}

	public void paintPicResume(View v) {
		Intent intent = new Intent();
		intent.setClass(this, FileManager.class);
		startActivityForResult(intent, GET_FILE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (!(requestCode == MainActivity.GET_FILE && resultCode == FileManager.RESULT_FILE))
			return;
		String path = data.getStringExtra("resume_file");
		File file = new File(path);
		if (file.isFile()) {
			String str = "";
			try {
				str = readFromFile(path);
			} catch (IOException e) {
			}
			Intent intent = new Intent();
			intent.setClass(this, ResumePainter.class);
			intent.putExtra("resume_data", str);
			startActivity(intent);
		}
	}

	public String readFromFile(String fileName) throws IOException {
		File file = new File(fileName);
		FileInputStream is = new FileInputStream(file);
		InputStreamReader inReader = new InputStreamReader(is);
		char[] buf = new char[50000];
		inReader.read(buf);
		inReader.close();
		is.close();
		String str = new String(buf);
		return str;
	}
}