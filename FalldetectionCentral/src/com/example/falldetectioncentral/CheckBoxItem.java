package com.example.falldetectioncentral;

import android.widget.CheckBox;
import android.widget.EditText;

public class CheckBoxItem {

	
	private CheckBox checkBox;
	private String Global_ID;
	private EditText type;
	
	
	public CheckBoxItem(CheckBox checkBox, String Global_ID, EditText type){
		
		this.setCheckBox(checkBox);
		this.setGlobal_ID(Global_ID);
		this.setType(type);
		
	}


	public String getGlobal_ID() {
		return Global_ID;
	}


	public void setGlobal_ID(String global_ID) {
		Global_ID = global_ID;
	}


	public CheckBox getCheckBox() {
		return checkBox;
	}


	public void setCheckBox(CheckBox checkBox) {
		this.checkBox = checkBox;
	}


	public EditText getType() {
		return type;
	}


	public void setType(EditText type) {
		this.type = type;
	}
	
}
