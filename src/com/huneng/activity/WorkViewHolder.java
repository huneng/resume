package com.huneng.activity;

import java.util.List;

import com.huneng.data.WorkData;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class WorkViewHolder {
	private ResumeEditor parent;
	Button frontBtn, nextBtn;
	Button wnewBtn, wdeleteBtn;
	EditText companyEd, positionEd;
	EditText STimeEd, ETimeEd, scoreEd;
	List<WorkData> works;
	WorkData curWork;
	int curIndex;

	public WorkViewHolder(View view, ResumeEditor editor) {
		parent = editor;

		works = parent.resumeDataStorage.works;
		curIndex = 0;
		frontBtn = (Button) view.findViewById(R.id.work_front);
		nextBtn = (Button) view.findViewById(R.id.work_next);
		wnewBtn = (Button) view.findViewById(R.id.work_new);
		wdeleteBtn = (Button) view.findViewById(R.id.work_delete);

		frontBtn.setOnClickListener(cl);
		nextBtn.setOnClickListener(cl);
		wnewBtn.setOnClickListener(cl);
		wdeleteBtn.setOnClickListener(cl);

		STimeEd = (EditText) view.findViewById(R.id.work_start);
		ETimeEd = (EditText) view.findViewById(R.id.work_end);
		scoreEd = (EditText) view.findViewById(R.id.work_score);
		companyEd = (EditText) view.findViewById(R.id.company_edit);
		positionEd = (EditText) view.findViewById(R.id.work_edit);

	}

	private OnClickListener cl = new OnClickListener() {

		@Override
		public void onClick(View v) {
			saveData();
			switch (v.getId()) {
			case R.id.work_front:
				if (curIndex != 0)
					front();
				break;
			case R.id.work_next:
				if (curIndex != works.size() - 1)
					next();
				break;
			case R.id.work_new:
				newWork();
				break;
			case R.id.work_delete:
				delWork();
				break;

			}
			works.set(curIndex, curWork);
		}

	};
	public void setWorks(List<WorkData> works){
		this.works = works;
		this.curWork = works.get(0);
		curIndex = 0;
		initEdit(curWork);
		
	}
	public void initEdit(WorkData curWork) {
		positionEd.setText(curWork.workname);
		companyEd.setText(curWork.company);
		String str = "";
		if (curWork.begintime != 0)
			str = "" + curWork.begintime;
		STimeEd.setText(str);
		if (curWork.endtime != 0)
			str = "" + curWork.endtime;
		ETimeEd.setText(str);
		if (curWork.score != 0)
			str = "" + curWork.score;
		scoreEd.setText(str);
	}

	public void saveData() {
		String str = positionEd.getText().toString();
		curWork.workname = str;

		str = companyEd.getText().toString();
		curWork.company = str;

		str = STimeEd.getText().toString();
		if (str.equals(""))
			str = "0";
		curWork.begintime = Integer.parseInt(str);

		str = ETimeEd.getText().toString();
		if (str.equals(""))
			str = "0";
		curWork.endtime = Integer.parseInt(str);

		str = scoreEd.getText().toString();
		if (str.equals(""))
			str = "0";
		curWork.score = Integer.parseInt(str);

		works.set(curIndex, curWork);

	}

	protected void delWork() {
		works.remove(curIndex);
		curIndex = 0;
		if (works.size() == 0) {
			curWork = new WorkData();
			works.add(curWork);
		} else {
			curWork = works.get(0);
		}
		initEdit(curWork);
	}

	protected void newWork() {
		curIndex = works.size();
		curWork = new WorkData();
		works.add(curWork);
		initEdit(curWork);
	}

	protected void next() {
		curIndex++;
		curWork = works.get(curIndex);
		initEdit(curWork);
	}

	protected void front() {
		curIndex--;
		curWork = works.get(curIndex);
		initEdit(curWork);
	}
}
