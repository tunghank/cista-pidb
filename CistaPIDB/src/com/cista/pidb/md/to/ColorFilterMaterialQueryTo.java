package com.cista.pidb.md.to;

import com.cista.pidb.core.BaseQueryTo;

public class ColorFilterMaterialQueryTo extends BaseQueryTo{
	
	private String colorFilterMaterialNum;
	private String projectCodeWVersion;
	private String colorFilterVariant;
	
	public String getColorFilterMaterialNum() {
		return colorFilterMaterialNum;
	}
	public void setColorFilterMaterialNum(String colorFilterMaterialNum) {
		this.colorFilterMaterialNum = colorFilterMaterialNum;
	}
	public String getProjectCodeWVersion() {
		return projectCodeWVersion;
	}
	public void setProjectCodeWVersion(String projectCodeWVersion) {
		this.projectCodeWVersion = projectCodeWVersion;
	}
	public String getColorFilterVariant() {
		return colorFilterVariant;
	}
	public void setColorFilterVariant(String colorFilterVariant) {
		this.colorFilterVariant = colorFilterVariant;
	}

}
