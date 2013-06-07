package com.huneng.resume;

import java.io.File;
import java.util.List;

import com.huneng.activity.R;
import com.huneng.data.BaseData;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

public class BaseResume {
	private Context c;
	private int width, height;
	private Bitmap photo;
	public View view;
	public BaseResume(Context context) {
		c = context;
	}

	public static BaseResume createBaseResume(Context context, int width,
			int height) {
		BaseResume resume = new BaseResume(context);
		resume.width = width;
		resume.height = height;
		return resume;
	}

	public void init(BaseData data, List<String> remarks) {
		LayoutInflater inflater = LayoutInflater.from(c);
		View view = inflater.inflate(R.layout.simple_resume, null);
		view.setLayoutParams(new LayoutParams(width, height));
		this.view =view; 
		if(data.photo!=""){
			File file = new File(data.photo);
			if(file.isFile()){
				photo = BitmapFactory.decodeFile(data.photo);
				ImageView img =(ImageView)view.findViewById(R.id.photo_draw); 
				img.setImageBitmap(photo);
			}
		}
		TextView name = (TextView)view.findViewById(R.id.name_draw);
		name.setText(data.name);
		TextView birth = (TextView)view.findViewById(R.id.birth_draw);
		birth.setText(data.birth);
		TextView sex = (TextView)view.findViewById(R.id.sex_draw);
		sex.setText(data.sex);
		TextView phone = (TextView)view.findViewById(R.id.phone_draw);
		phone.setText(data.phone);
		TextView addr = (TextView)view.findViewById(R.id.addr_draw);
		addr.setText(data.address);
		TextView salary = (TextView)view.findViewById(R.id.salary_draw);
		salary.setText(data.salary);
		TextView job = (TextView)view.findViewById(R.id.job_draw);
		job.setText(data.job);
		TextView holiday = (TextView)view.findViewById(R.id.holi_draw);
		holiday.setText(data.holiday);
		TextView remark = (TextView)view.findViewById(R.id.remark_draw);
		StringBuffer str = new StringBuffer();
		for(int i = 0; i < remarks.size(); i++){
			str.append(remarks.get(i)+"\n");
		}
		remark.setText(str.toString());
	}

}
