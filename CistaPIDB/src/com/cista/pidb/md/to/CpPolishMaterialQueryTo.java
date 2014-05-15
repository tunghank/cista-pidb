package com.cista.pidb.md.to;

import com.cista.pidb.core.BaseQueryTo;

public class CpPolishMaterialQueryTo extends BaseQueryTo {

	private String cpPolishMaterialNum;
	private String projectCodeWVersion;
	private String cpPolishVariant;

	public String getCpPolishMaterialNum() {
		return cpPolishMaterialNum;
	}

	public void setCpPolishMaterialNum(String cpPolishMaterialNum) {
		this.cpPolishMaterialNum = cpPolishMaterialNum;
	}

	public String getProjectCodeWVersion() {
		return projectCodeWVersion;
	}

	public void setProjectCodeWVersion(String projectCodeWVersion) {
		this.projectCodeWVersion = projectCodeWVersion;
	}

	public String getCpPolishVariant() {
		return cpPolishVariant;
	}

	public void setCpPolishVariant(String cpPolishVariant) {
		this.cpPolishVariant = cpPolishVariant;
	}

}
