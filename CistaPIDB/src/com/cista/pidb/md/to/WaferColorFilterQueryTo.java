package com.cista.pidb.md.to;

import com.cista.pidb.core.BaseQueryTo;

public class WaferColorFilterQueryTo extends BaseQueryTo{
	
	private String waferCfMaterialNum;
	private String projectCodWVersion;
	private String waferCfVariant;

	public String getWaferCfMaterialNum() {
		return waferCfMaterialNum;
	}

	public void setWaferCfMaterialNum(String waferCfMaterialNum) {
		this.waferCfMaterialNum = waferCfMaterialNum;
	}

	public String getProjectCodWVersion() {
		return projectCodWVersion;
	}

	public void setProjectCodWVersion(String projectCodWVersion) {
		this.projectCodWVersion = projectCodWVersion;
	}

	public String getWaferCfVariant() {
		return waferCfVariant;
	}

	public void setWaferCfVariant(String waferCfVariant) {
		this.waferCfVariant = waferCfVariant;
	}

}
