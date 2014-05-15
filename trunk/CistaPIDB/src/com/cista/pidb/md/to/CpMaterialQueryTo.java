package com.cista.pidb.md.to;

import com.cista.pidb.core.BaseQueryTo;

public class CpMaterialQueryTo extends BaseQueryTo{
	
	private String cpMaterialNum;
	private String projectCodeWVersion;
	private String cpVariant;
	public String getCpMaterialNum() {
		
		return cpMaterialNum;
	}
	public void setCpMaterialNum(String cpMaterialNum) {
		this.cpMaterialNum = cpMaterialNum;
	}
	public String getProjectCodeWVersion() {
		return projectCodeWVersion;
	}
	public void setProjectCodeWVersion(String projectCodeWVersion) {
		this.projectCodeWVersion = projectCodeWVersion;
	}
	public String getCpVariant() {
		return cpVariant;
	}
	public void setCpVariant(String cpVariant) {
		this.cpVariant = cpVariant;
	}
	
	

}
