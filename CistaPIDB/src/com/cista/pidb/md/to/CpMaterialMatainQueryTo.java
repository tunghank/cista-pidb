package com.cista.pidb.md.to;

import com.cista.pidb.core.BaseQueryTo;

public class CpMaterialMatainQueryTo extends BaseQueryTo{
	private String cpMaterialNum;
	private String projCodeWVersion;
	private String cpVariant;
	private String cpTestProgramNameList;
	
	public String getCpMaterialNum() {
		return cpMaterialNum;
	}
	public void setCpMaterialNum(String cpMaterialNum) {
		this.cpMaterialNum = cpMaterialNum;
	}
	public String getProjCodeWVersion() {
		return projCodeWVersion;
	}
	public void setProjCodeWVersion(String projCodeWVersion) {
		this.projCodeWVersion = projCodeWVersion;
	}
	public String getCpVariant() {
		return cpVariant;
	}
	public void setCpVariant(String cpVariant) {
		this.cpVariant = cpVariant;
	}
	public String getCpTestProgramNameList() {
		return cpTestProgramNameList;
	}
	public void setCpTestProgramNameList(String cpTestProgramNameList) {
		this.cpTestProgramNameList = cpTestProgramNameList;
	}
	
	

}
