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
	private SkillData curSkill;
	private EditText snameEd, sstartEd, sscoresEd;
	private Button frontBtn, nextBtn;
	private Button sAddBtn, sDeleteBtn;
	private int curIndex;

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

	public void setCurSkill(SkillData curSkill, int i) {
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
