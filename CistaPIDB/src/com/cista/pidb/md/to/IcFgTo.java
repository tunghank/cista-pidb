package com.cista.pidb.md.to;

public class IcFgTo {

	private String materialNum;
	private String prodCode;
	private String variant;
	private String projCode;
	private String pkgType;
	private String pkgCode;
	private String partNum;
	private boolean routingFg;
	private boolean routingAs;
	private String mpStatus;
	private String cust;
	private String apModel;
	private String appCategory;
	private String mcpDieQty;
	private String mcpPkg;
	private String mcpProd1;
	private String mcpProd2;
	private String mcpProd3;
	private String mcpProd4;
	private String remark;
	private String cpMaterialNum;
	private String cpTestProgNameList;
	private String ftTestProgList;
	private String assignTo;
	private String assignEmail;
	private String status;
	private String createdBy;
	private String modifiedBy;
	private String releaseTo;
	
	//For Lead Frame 相關欄位需求 Add by 990044
    private String lfTool;
    private String closeLfName;
    //For HI 採買 HX F料號產品使用,用來知道採買的是對方那一個產品
    private String vendorDevice;


	/**
	 * @return the lfTool
	 */
	public String getLfTool() {
		return lfTool;
	}

	/**
	 * @param lfTool the lfTool to set
	 */
	public void setLfTool(String lfTool) {
		this.lfTool = lfTool;
	}

	/**
	 * @return the closeLfName
	 */
	public String getCloseLfName() {
		return closeLfName;
	}

	/**
	 * @param closeLfName the closeLfName to set
	 */
	public void setCloseLfName(String closeLfName) {
		this.closeLfName = closeLfName;
	}

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

	public String getAppCategory() {
		return appCategory;
	}

	public void setAppCategory(String appCategory) {
		this.appCategory = appCategory;
	}

	public String getAssignEmail() {
		return assignEmail;
	}

	public void setAssignEmail(String assignEmail) {
		this.assignEmail = assignEmail;
	}

	public String getAssignTo() {
		return assignTo;
	}

	public void setAssignTo(String assignTo) {
		this.assignTo = assignTo;
	}

	public String getCpMaterialNum() {
		return cpMaterialNum;
	}

	public void setCpMaterialNum(String cpMaterialNum) {
		this.cpMaterialNum = cpMaterialNum;
	}

	public String getCpTestProgNameList() {
		return cpTestProgNameList;
	}

	public void setCpTestProgNameList(String cpTestProgNameList) {
		this.cpTestProgNameList = cpTestProgNameList;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCust() {
		return cust;
	}

	public void setCust(String cust) {
		this.cust = cust;
	}

	public String getFtTestProgList() {
		return ftTestProgList;
	}

	public void setFtTestProgList(String ftTestProgList) {
		this.ftTestProgList = ftTestProgList;
	}

	public String getMaterialNum() {
		return materialNum;
	}

	public void setMaterialNum(String materialNum) {
		this.materialNum = materialNum;
	}

	public String getMcpDieQty() {
		return mcpDieQty;
	}

	public void setMcpDieQty(String mcpDieQty) {
		this.mcpDieQty = mcpDieQty;
	}

	public String getMcpPkg() {
		return mcpPkg;
	}

	public void setMcpPkg(String mcpPkg) {
		this.mcpPkg = mcpPkg;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
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

	public String getPkgType() {
		return pkgType;
	}

	public void setPkgType(String pkgType) {
		this.pkgType = pkgType;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public boolean getRoutingAs() {
		return routingAs;
	}

	public void setRoutingAs(boolean routingAs) {
		this.routingAs = routingAs;
	}

	public boolean getRoutingFg() {
		return routingFg;
	}

	public void setRoutingFg(boolean routingFg) {
		this.routingFg = routingFg;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getVariant() {
		return variant;
	}

	public void setVariant(String variant) {
		this.variant = variant;
	}

	public String getMcpProd1() {
		return mcpProd1;
	}

	public void setMcpProd1(String mcpProd1) {
		this.mcpProd1 = mcpProd1;
	}

	public String getMcpProd2() {
		return mcpProd2;
	}

	public void setMcpProd2(String mcpProd2) {
		this.mcpProd2 = mcpProd2;
	}

	public String getMcpProd3() {
		return mcpProd3;
	}

	public void setMcpProd3(String mcpProd3) {
		this.mcpProd3 = mcpProd3;
	}

	public String getMcpProd4() {
		return mcpProd4;
	}

	public void setMcpProd4(String mcpProd4) {
		this.mcpProd4 = mcpProd4;
	}

	/**
	 * @return the vendorDevice
	 */
	public String getVendorDevice() {
		return vendorDevice;
	}

	/**
	 * @param vendorDevice the vendorDevice to set
	 */
	public void setVendorDevice(String vendorDevice) {
		this.vendorDevice = vendorDevice;
	}



}
