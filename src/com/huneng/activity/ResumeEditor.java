package com.huneng.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.huneng.data.MyJson;
import com.huneng.data.SkillData;
import com.huneng.data.WorkData;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class ResumeEditor extends Activity {
	public static final int RESULT_LOAD_IMAGE = 0;
	private static final int FILE_GET = 1;
	private ViewPager mTabPager;
	private List<View> centerContentView;
	private BaseViewHolder holder1;
	private SkillViewHolder holder2;
	private WorkViewHolder holder3;
	public MyJson resumeDataStorage;
	private Button saveBtn;
	private LinearLayout saveLayout;
	private EditText filenameEdit;
	private RadioButton baseSelect;
	private RadioButton skillSelect;
	private RadioButton workSelect;
	private RadioButton operSelect;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.main_editor);
		resumeDataStorage = new MyJson();
		initCenterContent();
		initTab();
		initViewData();
	}

	private void initCenterContent() {
		LayoutInflater inflater = LayoutInflater.from(this);
		View view1 = inflater.inflate(R.layout.base, null);
		View view2 = inflater.inflate(R.layout.skill, null);
		View view3 = inflater.inflate(R.layout.work, null);
		View view4 = inflater.inflate(R.layout.editor_operation, null);
		holder1 = new BaseViewHolder(view1, this);
		holder2 = new SkillViewHolder(view2, this);
		holder3 = new WorkViewHolder(view3, this);
		saveBtn = (Button) view4.findViewById(R.id.file_save_btn);
		saveLayout = (LinearLayout) view4.findViewById(R.id.save_layout);
		filenameEdit = (EditText) view4.findViewById(R.id.filename_edit);

		centerContentView = new ArrayList<View>();
		centerContentView.add(view1);
		centerContentView.add(view2);
		centerContentView.add(view3);
		centerContentView.add(view4);
		PagerAdapter mPagerAdapter = new PagerAdapter() {

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return centerContentView.size();
			}

			@Override
			public void destroyItem(View container, int position, Object object) {
				((ViewPager) container).removeView(centerContentView
						.get(position));
			}

			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager) container)
						.addView(centerContentView.get(position));
				return centerContentView.get(position);
			}
		};
		mTabPager = (ViewPager) findViewById(R.id.editor_tabpager);
		mTabPager.setAdapter(mPagerAdapter);
		mTabPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				switch (arg0) {
				case 0:
					gotoView(baseSelect);
					break;
				case 1:
					gotoView(skillSelect);
					break;
				case 2:
					gotoView(workSelect);
					break;
				case 3:
					gotoView(operSelect);
					break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	private void initTab() {

		baseSelect = (RadioButton) findViewById(R.id.editor_base);
		workSelect = (RadioButton) findViewById(R.id.editor_work);
		skillSelect = (RadioButton) findViewById(R.id.editor_study);
		operSelect = (RadioButton) findViewById(R.id.editor_operation);

		gotoView(baseSelect);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == RESULT_LOAD_IMAGE
				&& resultCode == Activity.RESULT_OK && null != data) {
			holder1.onActivityResult(requestCode, resultCode, data);
		}
		if (requestCode == FILE_GET && resultCode == FileManager.RESULT_FILE) {
			String str = data.getStringExtra("resume_file");

			String filedata = null;
			try {
				filedata = readFromFile(str);
			} catch (IOException e) {
				Toast.makeText(this, "File can't read!", Toast.LENGTH_LONG)
						.show();
				return;
			}
			try {
				resumeDataStorage = new MyJson(new JSONObject(filedata));
			} catch (JSONException e) {
				Toast.makeText(this, "Data wrong!", Toast.LENGTH_LONG).show();
			}
			initViewData();
		}
	}

	private void initViewData() {
		holder1.init(resumeDataStorage.basedata);
		if (resumeDataStorage.skills.size() == 0)
			resumeDataStorage.skills.add(new SkillData());
		if (resumeDataStorage.works.size() == 0)
			resumeDataStorage.works.add(new WorkData());
		holder2.setCurSkill(resumeDataStorage.skills.get(0), 0);
		holder3.setCurWork(resumeDataStorage.works.get(0), 0);
	}

	public void fileOpen(View v) {
		Intent intent = new Intent();
		intent.setClass(this, FileManager.class);
		startActivityForResult(intent, FILE_GET);
	}

	public void showFileNameEdit(View v) {
		holder1.saveData();
		holder2.saveData();
		holder3.saveData();
		saveLayout.setVisibility(View.VISIBLE);
		saveBtn.setVisibility(View.GONE);
	}

	public void fileSave(View v) {
		String filename = filenameEdit.getText().toString();
		saveLayout.setVisibility(View.GONE);
		saveBtn.setVisibility(View.VISIBLE);
		if (filename == "")
			return;
		filename = MainActivity.path + "/file/" + filename + ".txt";
		try {
			writeToFile(filename, resumeDataStorage.changToJsonData());
		} catch (IOException e) {
			Toast.makeText(this, "File can't save!", Toast.LENGTH_LONG).show();
		} catch (JSONException e) {
			Toast.makeText(this, "Data wrong!", Toast.LENGTH_LONG).show();
		}
	}

	public void writeToFile(String filename, String data) throws IOException {
		File file = new File(filename);
		FileOutputStream os = new FileOutputStream(file);
		OutputStreamWriter outWriter = new OutputStreamWriter(os);
		outWriter.write(data);
		outWriter.flush();
		outWriter.close();
		os.close();
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

	public void paintResume(View v) {
		Intent intent = new Intent();
		intent.setClass(this, ResumePainter.class);
		String str = "";
		try {
			str = resumeDataStorage.changToJsonData();
		} catch (JSONException e) {
		}
		intent.putExtra("resume_data", str);
		startActivity(intent);
	}

	public void gotoView(View v) {
		switch (v.getId()) {
		case R.id.editor_base:
			if (baseSelect.isSelected())
				return;
			if (mTabPager.getCurrentItem() != 0)
				mTabPager.setCurrentItem(0);
			baseSelect.setBackgroundResource(R.drawable.widget_bar_bg_p);
			skillSelect.setBackgroundResource(R.drawable.widget_bar_bg_n);
			workSelect.setBackgroundResource(R.drawable.widget_bar_bg_n);
			operSelect.setBackgroundResource(R.drawable.widget_bar_bg_n);

			break;
		case R.id.editor_study:
			if (skillSelect.isSelected())
				return;

			if (mTabPager.getCurrentItem() != 1)
				mTabPager.setCurrentItem(1);
			baseSelect.setBackgroundResource(R.drawable.widget_bar_bg_n);
			skillSelect.setBackgroundResource(R.drawable.widget_bar_bg_p);
			workSelect.setBackgroundResource(R.drawable.widget_bar_bg_n);
			operSelect.setBackgroundResource(R.drawable.widget_bar_bg_n);
			break;
		case R.id.editor_work:
			if (baseSelect.isSelected())
				return;
			if (mTabPager.getCurrentItem() != 2)
				mTabPager.setCurrentItem(2);
			baseSelect.setBackgroundResource(R.drawable.widget_bar_bg_n);
			skillSelect.setBackgroundResource(R.drawable.widget_bar_bg_n);
			workSelect.setBackgroundResource(R.drawable.widget_bar_bg_p);
			operSelect.setBackgroundResource(R.drawable.widget_bar_bg_n);
			break;
		case R.id.editor_operation:
			if (baseSelect.isSelected())
				return;
			if (mTabPager.getCurrentItem() != 3)
				mTabPager.setCurrentItem(3);
			baseSelect.setBackgroundResource(R.drawable.widget_bar_bg_n);
			skillSelect.setBackgroundResource(R.drawable.widget_bar_bg_n);
			workSelect.setBackgroundResource(R.drawable.widget_bar_bg_n);
			operSelect.setBackgroundResource(R.drawable.widget_bar_bg_p);
			break;
		}
	}

}
