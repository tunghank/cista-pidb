package com.cista.pidb.md.to;

import com.cista.pidb.core.BaseQueryTo;

public class ZsUsbramQueryTo extends BaseQueryTo {

	private String materialNum;
	private String memorySize;
	private String operationVoltage;
	private String vendorCode;
	private String io;
	private String maxResolution;
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

	public String getMemorySize() {
		return memorySize;
	}

	public void setMemorySize(String memorySize) {
		this.memorySize = memorySize;
	}

	public String getOperationVoltage() {
		return operationVoltage;
	}

	public void setOperationVoltage(String operationVoltage) {
		this.operationVoltage = operationVoltage;
	}

	public String getVendorCode() {
		return vendorCode;
	}

	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}

	public String getIo() {
		return io;
	}

	public void setIo(String io) {
		this.io = io;
	}

	public String getMaxResolution() {
		return maxResolution;
	}

	public void setMaxResolution(String maxResolution) {
		this.maxResolution = maxResolution;
	}

}
