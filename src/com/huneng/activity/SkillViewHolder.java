package com.huneng.activity;

import java.util.List;

import com.huneng.data.SkillData;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SkillViewHolder {
	private ResumeEditor parent;
	public List<SkillData> skills;
	SkillData curSkill;
	EditText snameEd, sstartEd, sscoresEd;
	Button frontBtn, nextBtn;
	Button sAddBtn, sDeleteBtn;
	int curIndex;
	int starttime, endtime;

	public SkillViewHolder(View view, ResumeEditor editor) {
		parent = editor;
		skills = parent.resumeDataStorage.skills;
		frontBtn = (Button) view.findViewById(R.id.skill_front);
		nextBtn = (Button) view.findViewById(R.id.skill_next);
		sAddBtn = (Button) view.findViewById(R.id.skill_new);
		sDeleteBtn = (Button) view.findViewById(R.id.skill_delete);

		frontBtn.setOnClickListener(cl);
		nextBtn.setOnClickListener(cl);
		sAddBtn.setOnClickListener(cl);
		sDeleteBtn.setOnClickListener(cl);

		snameEd = (EditText) view.findViewById(R.id.skill_edit);
		sstartEd = (EditText) view.findViewById(R.id.skill_start_edit);
		sscoresEd = (EditText) view.findViewById(R.id.skill_score_edit);

		starttime = endtime = 0;
	}

	private OnClickListener cl = new OnClickListener() {

		@Override
		public void onClick(View v) {
			saveData();
			switch (v.getId()) {
			case R.id.skill_delete:
				delSkill();
				break;
			case R.id.skill_new:
				newSkill();
				break;
			case R.id.skill_front:
				if (curIndex > 0)
					front();
				break;
			case R.id.skill_next:
				if (curIndex < skills.size() - 1)
					next();
				break;
			}
		}

	};
	public void saveData() {
		String str;
		str = snameEd.getText().toString();
		curSkill.skillname = str;

		str = sstartEd.getText().toString();
		if (str.equals(""))
			str = "0";
		curSkill.starttime = Integer.parseInt(str);

		str = sscoresEd.getText().toString();
		if (str.equals(""))
			str = "0";
		curSkill.setScore(str);

		
		skills.set(curIndex, curSkill);
		if (starttime == 0) {
			starttime = curSkill.starttime;
		} else {
			if (starttime > curSkill.starttime) {
				starttime = curSkill.starttime;
			}
		}
		if (endtime == 0) {
			endtime = curSkill.starttime + curSkill.length-1 ;
		} else {
			if (endtime < curSkill.starttime + curSkill.length ) {
				endtime = curSkill.starttime + curSkill.length-1 ;
			}
		}
		if(parent.resumeDataStorage.basedata.starttime>starttime){
			parent.resumeDataStorage.basedata.starttime = starttime;
		}
		if(parent.resumeDataStorage.basedata.endtime<endtime){
			parent.resumeDataStorage.basedata.starttime = endtime;
		}
	}

	public void initEdit(SkillData curSkill) {
		snameEd.setText(curSkill.skillname);

		String str = "";
		if (curSkill.starttime != 0)
			str = "" + curSkill.starttime;
		sstartEd.setText(str);

		str = "";
		for (int i = 0; i < curSkill.length; i++) {
			str += curSkill.scores[i] + " ";
		}
		sscoresEd.setText(str);
		
	}
	public void setCurSkill(SkillData curSkill, int i){
		this.curIndex = i;
		this.curSkill = curSkill;
		initEdit(curSkill);
	}
	protected void next() {
		curIndex++;
		curSkill = skills.get(curIndex);
		initEdit(curSkill);
	}

	protected void front() {
		curIndex--;
		curSkill = skills.get(curIndex);
		initEdit(curSkill);
	}

	protected void newSkill() {
		curIndex = skills.size();
		curSkill = new SkillData();
		initEdit(curSkill);
		skills.add(curSkill);
	}

	protected void delSkill() {
		skills.remove(curIndex);
		curIndex = 0;
		if (skills.size() == 0) {
			curSkill = new SkillData();
			initEdit(curSkill);
			return;
		}
		curSkill = skills.get(0);
		initEdit(curSkill);
	}

}
