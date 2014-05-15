package com.cista.pidb.md.to;

import com.cista.pidb.core.BaseQueryTo;

public class ZsEepromQueryTo extends BaseQueryTo {
	private String materialNum;
	private String speed;
	private String density;
	private String operationVoltage;
	private String applicationProduct;
	private String releaseTo;

	public String getReleaseTo() {
		return releaseTo;
	}

	public void setReleaseTo(String releaseTo) {
		this.releaseTo = releaseTo;
	}

	public String getApplicationProduct() {
		return applicationProduct;
	}

	public void setApplicationProduct(String applicationProduct) {
		this.applicationProduct = applicationProduct;
	}

	public String getMaterialNum() {
		return materialNum;
	}

	public void setMaterialNum(String materialNum) {
		this.materialNum = materialNum;
	}

	public String getSpeed() {
		return speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	public String getDensity() {
		return density;
	}

	public void setDensity(String density) {
		this.density = density;
	}

	public String getOperationVoltage() {
		return operationVoltage;
	}

	public void setOperationVoltage(String operationVoltage) {
		this.operationVoltage = operationVoltage;
	}

}
