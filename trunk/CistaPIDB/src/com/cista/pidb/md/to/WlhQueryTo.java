package com.cista.pidb.md.to;

import com.cista.pidb.core.BaseQueryTo;

public class WlhQueryTo extends BaseQueryTo {

	private String materialNum;
	private String prodName;
	private String packageCode;
	private String prodType;
	private String vendorCode;
	
	/**
	 * @return the materialNum
	 */
	public String getMaterialNum() {
		return materialNum;
	}
	/**
	 * @param materialNum the materialNum to set
	 */
	public void setMaterialNum(String materialNum) {
		this.materialNum = materialNum;
	}
	/**
	 * @return the prodName
	 */
	public String getProdName() {
		return prodName;
	}
	/**
	 * @param prodName the prodName to set
	 */
	public void setProdName(String prodName) {
		this.prodName = prodName;
	}
	/**
	 * @return the packageCode
	 */
	public String getPackageCode() {
		return packageCode;
	}
	/**
	 * @param packageCode the packageCode to set
	 */
	public void setPackageCode(String packageCode) {
		this.packageCode = packageCode;
	}
	/**
	 * @return the prodType
	 */
	public String getProdType() {
		return prodType;
	}
	/**
	 * @param prodType the prodType to set
	 */
	public void setProdType(String prodType) {
		this.prodType = prodType;
	}
	/**
	 * @return the vendorCode
	 */
	public String getVendorCode() {
		return vendorCode;
	}
	/**
	 * @param vendorCode the vendorCode to set
	 */
	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}



}
