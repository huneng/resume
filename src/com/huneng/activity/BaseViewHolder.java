package com.huneng.activity;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.huneng.data.BaseData;

public class BaseViewHolder {
	private EditText name_ed, phone_ed, addr_ed;
	private EditText job_ed, holi_ed, salary_ed;
	private EditText remark_ed;
	private Button btn, getPhoneBtn, sexBtn;
	private BaseData basedata;
	private ImageView photo;
	private ResumeEditor parent;

	public BaseViewHolder(View view, ResumeEditor editor) {
		
		name_ed = (EditText) view.findViewById(R.id.name_edit);
		phone_ed = (EditText) view.findViewById(R.id.phone_edit);
		addr_ed = (EditText) view.findViewById(R.id.address_edit);
		job_ed = (EditText) view.findViewById(R.id.job1_edit);
		holi_ed = (EditText) view.findViewById(R.id.holiday1_edit);
		salary_ed = (EditText) view.findViewById(R.id.salary1_edit);
		remark_ed = (EditText) view.findViewById(R.id.remark_edit);

		btn = (Button) view.findViewById(R.id.age_btn);
		getPhoneBtn = (Button) view.findViewById(R.id.get_tel);
		sexBtn = (Button) view.findViewById(R.id.sex_btn);
		photo = (ImageView) view.findViewById(R.id.photo_view);

		sexBtn.setOnClickListener(l);
		btn.setOnClickListener(l);
		getPhoneBtn.setOnClickListener(l);
		photo.setOnClickListener(l);

		parent = editor;
		basedata = parent.resumeDataStorage.basedata;
	}

	public String toString() {
		return null;
	}

	OnDateSetListener listener = new OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			String str = "" + year + '-' + (monthOfYear+1) + '-' + dayOfMonth;
			btn.setText(str);
			basedata.birth = str;
		}

	};
	OnClickListener l = new OnClickListener() {

		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.age_btn:
				Calendar calendar = Calendar.getInstance();
				new DatePickerDialog(parent, listener,
						calendar.get(Calendar.YEAR),
						calendar.get(Calendar.MONTH),
						calendar.get(Calendar.DAY_OF_MONTH)).show();
				break;
			case R.id.sex_btn:
				if (basedata.sex.equals("male"))
					basedata.sex = "female";
				else
					basedata.sex = "male";
				sexBtn.setText(basedata.sex);
				break;
			case R.id.get_tel:
				TelephonyManager tm = (TelephonyManager) parent
						.getSystemService(Context.TELEPHONY_SERVICE);
				String str = "";
				str = tm.getLine1Number();
				if (str == null || str.length() == 0) {
					phone_ed.setText("");
					Toast.makeText(parent, "Can't get", Toast.LENGTH_SHORT)
							.show();
					break;
				}
				phone_ed.setText(str);
				break;
			case R.id.photo_view:
				Intent i = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				parent.startActivityForResult(i, ResumeEditor.RESULT_LOAD_IMAGE);
				break;
			}
		}
	};

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		Uri selectedImage = data.getData();
		String[] filePathColumn = { MediaStore.Images.Media.DATA };

		Cursor cursor = parent.getContentResolver().query(selectedImage,
				filePathColumn, null, null, null);
		cursor.moveToFirst();

		int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		String picturePath = cursor.getString(columnIndex);
		cursor.close();
		basedata.photo = new String(picturePath);
		photo.setImageDrawable(Drawable.createFromPath(basedata.photo));
	}

	public void saveData() {
		basedata.name = name_ed.getText().toString();
		basedata.phone = phone_ed.getText().toString();
		basedata.address = addr_ed.getText().toString();
		basedata.job = job_ed.getText().toString();
		basedata.holiday = holi_ed.getText().toString();
		basedata.salary = salary_ed.getText().toString();
		basedata.remark = remark_ed.getText().toString();

	}

	public void init(BaseData basedata) {
		if (basedata.photo != "")
			photo.setImageDrawable(Drawable.createFromPath(basedata.photo));
		else
			photo.setBackgroundResource(R.drawable.people);
		name_ed.setText(basedata.name);
		sexBtn.setText(basedata.sex);
		phone_ed.setText(basedata.phone);
		addr_ed.setText(basedata.address);
		btn.setText(basedata.birth);
		job_ed.setText(basedata.job);
		holi_ed.setText(basedata.holiday);
		salary_ed.setText(basedata.salary);
		remark_ed.setText(basedata.remark);
	}
}
