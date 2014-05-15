package com.cista.pidb.md.to;

import com.cista.pidb.core.BaseQueryTo;

public class IcFgQueryTo extends BaseQueryTo {
	
	private String materialNum;
	private String pkgCode;
	private String partNum;
	private String prodCode;
	private String fab;
	private String projOption;
	private String projCode;
	private String routingFg;
	private String routingAs;
	private String mpStatus;
	private String cust;
	private String apModel;
	private String mcpPkg;
	private String status;
	private String releaseTo;
	private String pkgType;
	
	public String getReleaseTo() {
		return releaseTo;
	}

	public void setReleaseTo(String releaseTo) {
		this.releaseTo = releaseTo;
	}

	public String getApModel() {
		return apModel;
	}

	public void setApModel(String apModel) {
		this.apModel = apModel;
	}

	public String getFab() {
		return fab;
	}

	public void setFab(String fab) {
		this.fab = fab;
	}

	public String getMaterialNum() {
		return materialNum;
	}

	public void setMaterialNum(String materialNum) {
		this.materialNum = materialNum;
	}

	public String getMcpPkg() {
		return mcpPkg;
	}

	public void setMcpPkg(String mcpPkg) {
		this.mcpPkg = mcpPkg;
	}

	public String getMpStatus() {
		return mpStatus;
	}

	public void setMpStatus(String mpStatus) {
		this.mpStatus = mpStatus;
	}

	public String getPartNum() {
		return partNum;
	}

	public void setPartNum(String partNum) {
		this.partNum = partNum;
	}

	public String getPkgCode() {
		return pkgCode;
	}

	public void setPkgCode(String pkgCode) {
		this.pkgCode = pkgCode;
	}

	public String getProdCode() {
		return prodCode;
	}

	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}

	public String getProjCode() {
		return projCode;
	}

	public void setProjCode(String projCode) {
		this.projCode = projCode;
	}

	public String getRoutingAs() {
		return routingAs;
	}

	public void setRoutingAs(String routingAs) {
		this.routingAs = routingAs;
	}

	public String getRoutingFg() {
		return routingFg;
	}

	public void setRoutingFg(String routingFg) {
		this.routingFg = routingFg;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCust() {
		return cust;
	}

	public void setCust(String cust) {
		this.cust = cust;
	}

	public String getProjOption() {
		return projOption;
	}

	public void setProjOption(String projOption) {
		this.projOption = projOption;
	}

	/**
	 * @return the pkgType
	 */
	public String getPkgType() {
		return pkgType;
	}

	/**
	 * @param pkgType the pkgType to set
	 */
	public void setPkgType(String pkgType) {
		this.pkgType = pkgType;
	}

}
