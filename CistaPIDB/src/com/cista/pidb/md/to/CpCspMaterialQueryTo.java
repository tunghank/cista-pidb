package com.cista.pidb.md.to;

import com.cista.pidb.core.BaseQueryTo;

public class CpCspMaterialQueryTo extends BaseQueryTo {

	private String cpCspMaterialNum;
	private String projectCodeWVersion;
	private String cpCspVariant;

	public String getCpCspMaterialNum() {
		return cpCspMaterialNum;
	}

	public void setCpCspMaterialNum(String cpCspMaterialNum) {
		this.cpCspMaterialNum = cpCspMaterialNum;
	}

	public String getProjectCodeWVersion() {
		return projectCodeWVersion;
	}

	public void setProjectCodeWVersion(String projectCodeWVersion) {
		this.projectCodeWVersion = projectCodeWVersion;
	}

	public String getCpCspVariant() {
		return cpCspVariant;
	}

	public void setCpCspVariant(String cpCspVariant) {
		this.cpCspVariant = cpCspVariant;
	}

}
