package com.cista.pidb.md.to;

import java.util.Date;

/**
 * @author Hu Meixia
 */
public class MpListTo {

	private String partNum;
	private String icFgMaterialNum;
	private String prodCode;
	private String pkgCode;
	private String projCode;
	private String projCodeWVersion;
	private String tapeName;
	private String mpStatus;
	private Date mpReleaseDate;
	private String approveCust;
	private String approveTapeVendor;
	private String approveBpVendor;
	private String approveCpHouse;
	private String approveAssyHouse;
	private String assignTo;
	private String assignEmail;
	private String createdBy;
	private String modifiedBy;
	private String mpTrayDrawingNo1;
	private String mpTrayDrawingNoVer1;
	private String mpColor1;
	private String mpCustomerName1;
	private String mpTrayDrawingNo2;
	private String mpTrayDrawingNoVer2;
	private String mpColor2;
	private String mpCustomerName2;
	private String mpTrayDrawingNo3;
	private String mpTrayDrawingNoVer3;
	private String mpColor3;
	private String mpCustomerName3;
	private String mpTrayDrawingNo4;
	private String mpTrayDrawingNoVer4;
	private String mpColor4;
	private String mpCustomerName4;
	private String processFlow;
	private String matTape;
	private String matBp;
	private String matCp;
	private String matAs;
	private String matWf;
	private String matPolish;
	private String remark;
	private String approveFtHouse;
	private String approvePolishVendor;
	private String eolCust;
	private String revisionItem;
	private String updateTime;
	private String cdt;
	private String matCf;
	private String approveColorFilterVendor;
	private String matWafercf;
	private String approveWaferCfVendor;
	private String releaseTo;

	private String matCsp;
	private String approveCpCspVendor;
	private String matTsv;
	private String approveCpTsvVendor;
	private String cpBin;
	//For Lead Frame 相關欄位需求 Add by 990044
	private String mcpDieQty;
	private String mcpPkg;
	private String mcpProd1;
	private String mcpProd2;
	private String mcpProd3;
	private String mcpProd4;
	private String lfTool;
	private String closeLfName;
	
	
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

	public String getMatCsp() {
		return matCsp;
	}

	public void setMatCsp(String matCsp) {
		this.matCsp = matCsp;
	}

	public String getApproveCpCspVendor() {
		return approveCpCspVendor;
	}

	public void setApproveCpCspVendor(String approveCpCspVendor) {
		this.approveCpCspVendor = approveCpCspVendor;
	}

	public String getReleaseTo() {
		return releaseTo;
	}

	public void setReleaseTo(String releaseTo) {
		this.releaseTo = releaseTo;
	}

	public String getMatCf() {
		return matCf;
	}

	public void setMatCf(String matCf) {
		this.matCf = matCf;
	}

	public String getApproveColorFilterVendor() {
		return approveColorFilterVendor;
	}

	public void setApproveColorFilterVendor(String approveColorFilterVendor) {
		this.approveColorFilterVendor = approveColorFilterVendor;
	}

	public String getMatWafercf() {
		return matWafercf;
	}

	public void setMatWafercf(String matWafercf) {
		this.matWafercf = matWafercf;
	}

	public String getApproveWaferCfVendor() {
		return approveWaferCfVendor;
	}

	public void setApproveWaferCfVendor(String approveWaferCfVendor) {
		this.approveWaferCfVendor = approveWaferCfVendor;
	}

	public String getCdt() {
		return cdt;
	}

	public void setCdt(String cdt) {
		this.cdt = cdt;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getRevisionItem() {
		return revisionItem;
	}

	public void setRevisionItem(String revisionItem) {
		this.revisionItem = revisionItem;
	}

	public String getEolCust() {
		return eolCust;
	}

	public void setEolCust(String eolCust) {
		this.eolCust = eolCust;
	}

	public String getApproveFtHouse() {
		return approveFtHouse;
	}

	public void setApproveFtHouse(String approveFtHouse) {
		this.approveFtHouse = approveFtHouse;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getMatWf() {
		return matWf;
	}

	public void setMatWf(String matWf) {
		this.matWf = matWf;
	}

	public String getMatAs() {
		return matAs;
	}

	public void setMatAs(String matAs) {
		this.matAs = matAs;
	}

	public String getMatCp() {
		return matCp;
	}

	public void setMatCp(String matCp) {
		this.matCp = matCp;
	}

	public String getMatBp() {
		return matBp;
	}

	public void setMatBp(String matBp) {
		this.matBp = matBp;
	}

	public String getMatTape() {
		return matTape;
	}

	public void setMatTape(String matTape) {
		this.matTape = matTape;
	}

	public String getApproveAssyHouse() {
		return approveAssyHouse;
	}

	public void setApproveAssyHouse(String approveAssyHouse) {
		this.approveAssyHouse = approveAssyHouse;
	}

	public String getApproveBpVendor() {
		return approveBpVendor;
	}

	public void setApproveBpVendor(String approveBpVendor) {
		this.approveBpVendor = approveBpVendor;
	}

	public String getApproveCpHouse() {
		return approveCpHouse;
	}

	public void setApproveCpHouse(String approveCpHouse) {
		this.approveCpHouse = approveCpHouse;
	}

	public String getApproveCust() {
		return approveCust;
	}

	public void setApproveCust(String approveCust) {
		this.approveCust = approveCust;
	}

	public String getApproveTapeVendor() {
		return approveTapeVendor;
	}

	public void setApproveTapeVendor(String approveTapeVendor) {
		this.approveTapeVendor = approveTapeVendor;
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

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getIcFgMaterialNum() {
		return icFgMaterialNum;
	}

	public void setIcFgMaterialNum(String icFgMaterialNum) {
		this.icFgMaterialNum = icFgMaterialNum;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getMpReleaseDate() {
		return mpReleaseDate;
	}

	public void setMpReleaseDate(Date mpReleaseDate) {
		this.mpReleaseDate = mpReleaseDate;
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

	public String getProjCodeWVersion() {
		return projCodeWVersion;
	}

	public void setProjCodeWVersion(String projCodeWVersion) {
		this.projCodeWVersion = projCodeWVersion;
	}

	public String getTapeName() {
		return tapeName;
	}

	public void setTapeName(String tapeName) {
		this.tapeName = tapeName;
	}

	public String getMpColor1() {
		return mpColor1;
	}

	public void setMpColor1(String mpColor1) {
		this.mpColor1 = mpColor1;
	}

	public String getMpColor2() {
		return mpColor2;
	}

	public void setMpColor2(String mpColor2) {
		this.mpColor2 = mpColor2;
	}

	public String getMpColor3() {
		return mpColor3;
	}

	public void setMpColor3(String mpColor3) {
		this.mpColor3 = mpColor3;
	}

	public String getMpColor4() {
		return mpColor4;
	}

	public void setMpColor4(String mpColor4) {
		this.mpColor4 = mpColor4;
	}

	public String getMpCustomerName1() {
		return mpCustomerName1;
	}

	public void setMpCustomerName1(String mpCustomerName1) {
		this.mpCustomerName1 = mpCustomerName1;
	}

	public String getMpCustomerName2() {
		return mpCustomerName2;
	}

	public void setMpCustomerName2(String mpCustomerName2) {
		this.mpCustomerName2 = mpCustomerName2;
	}

	public String getMpCustomerName3() {
		return mpCustomerName3;
	}

	public void setMpCustomerName3(String mpCustomerName3) {
		this.mpCustomerName3 = mpCustomerName3;
	}

	public String getMpCustomerName4() {
		return mpCustomerName4;
	}

	public void setMpCustomerName4(String mpCustomerName4) {
		this.mpCustomerName4 = mpCustomerName4;
	}

	public String getMpTrayDrawingNo1() {
		return mpTrayDrawingNo1;
	}

	public void setMpTrayDrawingNo1(String mpTrayDrawingNo1) {
		this.mpTrayDrawingNo1 = mpTrayDrawingNo1;
	}

	public String getMpTrayDrawingNo2() {
		return mpTrayDrawingNo2;
	}

	public void setMpTrayDrawingNo2(String mpTrayDrawingNo2) {
		this.mpTrayDrawingNo2 = mpTrayDrawingNo2;
	}

	public String getMpTrayDrawingNo3() {
		return mpTrayDrawingNo3;
	}

	public void setMpTrayDrawingNo3(String mpTrayDrawingNo3) {
		this.mpTrayDrawingNo3 = mpTrayDrawingNo3;
	}

	public String getMpTrayDrawingNo4() {
		return mpTrayDrawingNo4;
	}

	public void setMpTrayDrawingNo4(String mpTrayDrawingNo4) {
		this.mpTrayDrawingNo4 = mpTrayDrawingNo4;
	}

	public String getMpTrayDrawingNoVer1() {
		return mpTrayDrawingNoVer1;
	}

	public void setMpTrayDrawingNoVer1(String mpTrayDrawingNoVer1) {
		this.mpTrayDrawingNoVer1 = mpTrayDrawingNoVer1;
	}

	public String getMpTrayDrawingNoVer2() {
		return mpTrayDrawingNoVer2;
	}

	public void setMpTrayDrawingNoVer2(String mpTrayDrawingNoVer2) {
		this.mpTrayDrawingNoVer2 = mpTrayDrawingNoVer2;
	}

	public String getMpTrayDrawingNoVer3() {
		return mpTrayDrawingNoVer3;
	}

	public void setMpTrayDrawingNoVer3(String mpTrayDrawingNoVer3) {
		this.mpTrayDrawingNoVer3 = mpTrayDrawingNoVer3;
	}

	public String getMpTrayDrawingNoVer4() {
		return mpTrayDrawingNoVer4;
	}

	public void setMpTrayDrawingNoVer4(String mpTrayDrawingNoVer4) {
		this.mpTrayDrawingNoVer4 = mpTrayDrawingNoVer4;
	}

	public String getProcessFlow() {
		return processFlow;
	}

	public void setProcessFlow(String processFlow) {
		this.processFlow = processFlow;
	}

	public String getMatPolish() {
		return matPolish;
	}

	public void setMatPolish(String matPolish) {
		this.matPolish = matPolish;
	}

	public String getApprovePolishVendor() {
		return approvePolishVendor;
	}

	public void setApprovePolishVendor(String approvePolishVendor) {
		this.approvePolishVendor = approvePolishVendor;
	}

	/**
	 * @return the cpBin
	 */
	public String getCpBin() {
		return cpBin;
	}

	/**
	 * @param cpBin the cpBin to set
	 */
	public void setCpBin(String cpBin) {
		this.cpBin = cpBin;
	}

	/**
	 * @return the matTsv
	 */
	public String getMatTsv() {
		return matTsv;
	}

	/**
	 * @param matTsv the matTsv to set
	 */
	public void setMatTsv(String matTsv) {
		this.matTsv = matTsv;
	}

	/**
	 * @return the approveCpTsvVendor
	 */
	public String getApproveCpTsvVendor() {
		return approveCpTsvVendor;
	}

	/**
	 * @param approveCpTsvVendor the approveCpTsvVendor to set
	 */
	public void setApproveCpTsvVendor(String approveCpTsvVendor) {
		this.approveCpTsvVendor = approveCpTsvVendor;
	}

}
