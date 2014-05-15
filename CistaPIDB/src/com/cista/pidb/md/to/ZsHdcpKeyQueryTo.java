package com.cista.pidb.md.to;

import com.cista.pidb.core.BaseQueryTo;

public class ZsHdcpKeyQueryTo extends BaseQueryTo {

	private String materialNum;
	private String vendorCode;
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

	public String getVendorCode() {
		return vendorCode;
	}

	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}

}
