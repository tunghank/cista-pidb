/*
 * 2010.05.07/FCG1 @Jere Huang - 新增tape out type, tape out project name.
 */

package com.cista.pidb.md.to;

import java.util.Date;

/**
 * @author smilly IC_WAFER
 */
public class IcWaferTo {

	private String materialNum;
	private String variant;
	private String projCode;
	private String bodyVer;
	private String optionVer;
	private String projCodeWVersion;
	private boolean routingWf;
	private boolean routingBp;
	private boolean routingCp;
	private String mpStatus;
	private String remark;
	private String fabDeviceId;
	private String maskLayerCom;
	private Date tapeOutDate;
	private String maskId;
	private String revisionItem;
	private String assignTo;
	private String assignEmail;
	private String status;
	private String createdBy;
	private String modifiedBy;
	private String cp;
	private String bp;
	private String ds;
	private String materialDesc;
	private String maskHouse;
	private String maskNum;
	private String cpPolishMaterialNum;
	private boolean routingPolish;
	private boolean routingColorFilter;
	private boolean routingWaferCf;
	private String releaseTo;

	private boolean routingCsp;
	private boolean routingTsv;
	private String version;
	//FCG1
	private String tapeOutType;
	private String tapeOutProjName;
	
	
	
	public String getTapeOutType() {
		return tapeOutType;
	}

	public void setTapeOutType(String tapeOutType) {
		this.tapeOutType = tapeOutType;
	}

	public String getTapeOutProjName() {
		return tapeOutProjName;
	}

	public void setTapeOutProjName(String tapeOutProjName) {
		this.tapeOutProjName = tapeOutProjName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getReleaseTo() {
		return releaseTo;
	}

	public void setReleaseTo(String releaseTo) {
		this.releaseTo = releaseTo;
	}

	public boolean isRoutingColorFilter() {
		return routingColorFilter;
	}

	public void setRoutingColorFilter(boolean routingColorFilter) {
		this.routingColorFilter = routingColorFilter;
	}

	public boolean getRoutingColorFilter() {
		return routingColorFilter;
	}

	public String getCpPolishMaterialNum() {
		return cpPolishMaterialNum;
	}

	public void setCpPolishMaterialNum(String cpPolishMaterialNum) {
		this.cpPolishMaterialNum = cpPolishMaterialNum;
	}

	public boolean isRoutingPolish() {
		return routingPolish;
	}

	public void setRoutingPolish(boolean routingPolish) {
		this.routingPolish = routingPolish;
	}

	public boolean getRoutingPolish() {
		return routingPolish;
	}

	public String getMaskNum() {
		return maskNum;
	}

	public void setMaskNum(String maskNum) {
		this.maskNum = maskNum;
	}

	public String getMaskHouse() {
		return maskHouse;
	}

	public void setMaskHouse(String maskHouse) {
		this.maskHouse = maskHouse;
	}

	public String getMaterialDesc() {
		return materialDesc;
	}

	public void setMaterialDesc(String materialDesc) {
		this.materialDesc = materialDesc;
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

	public String getBodyVer() {
		return bodyVer;
	}

	public void setBodyVer(String bodyVer) {
		this.bodyVer = bodyVer;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getFabDeviceId() {
		return fabDeviceId;
	}

	public void setFabDeviceId(String fabDeviceId) {
		this.fabDeviceId = fabDeviceId;
	}

	public String getMaskId() {
		return maskId;
	}

	public void setMaskId(String maskId) {
		this.maskId = maskId;
	}

	public String getMaskLayerCom() {
		return maskLayerCom;
	}

	public void setMaskLayerCom(String maskLayerCom) {
		this.maskLayerCom = maskLayerCom;
	}

	public String getMaterialNum() {
		return materialNum;
	}

	public void setMaterialNum(String materialNum) {
		this.materialNum = materialNum;
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

	public String getOptionVer() {
		return optionVer;
	}

	public void setOptionVer(String optionVer) {
		this.optionVer = optionVer;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRevisionItem() {
		return revisionItem;
	}

	public void setRevisionItem(String revisionItem) {
		this.revisionItem = revisionItem;
	}

	public boolean isRoutingBp() {
		return routingBp;
	}

	public boolean getRoutingBp() {
		return routingBp;
	}

	public void setRoutingBp(boolean routingBp) {
		this.routingBp = routingBp;
	}

	public boolean isRoutingCp() {
		return routingCp;
	}

	public boolean getRoutingCp() {
		return routingCp;
	}

	public void setRoutingCp(boolean routingCp) {
		this.routingCp = routingCp;
	}

	public boolean isRoutingWf() {
		return routingWf;
	}

	public boolean getRoutingWf() {
		return routingWf;
	}

	public void setRoutingWf(boolean routingWf) {
		this.routingWf = routingWf;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getTapeOutDate() {
		return tapeOutDate;
	}

	public void setTapeOutDate(Date tapeOutDate) {
		this.tapeOutDate = tapeOutDate;
	}

	public String getVariant() {
		return variant;
	}

	public void setVariant(String variant) {
		this.variant = variant;
	}

	public String getBp() {
		return bp;
	}

	public void setBp(String bp) {
		this.bp = bp;
	}

	public String getCp() {
		return cp;
	}

	public void setCp(String cp) {
		this.cp = cp;
	}

	public String getDs() {
		return ds;
	}

	public void setDs(String ds) {
		this.ds = ds;
	}

	public boolean isRoutingWaferCf() {
		return routingWaferCf;
	}

	public void setRoutingWaferCf(boolean routingWaferCf) {
		this.routingWaferCf = routingWaferCf;
	}

	public boolean getRoutingWaferCf() {
		return routingWaferCf;
	}

	public boolean isRoutingCsp() {
		return routingCsp;
	}

	public void setRoutingCsp(boolean routingCsp) {
		this.routingCsp = routingCsp;
	}

	public boolean getRoutingCsp() {
		return routingCsp;
	}

	/**
	 * @return the routingTsv
	 */
	public boolean isRoutingTsv() {
		return routingTsv;
	}

	/**
	 * @param routingTsv the routingTsv to set
	 */
	public void setRoutingTsv(boolean routingTsv) {
		this.routingTsv = routingTsv;
	}
	
	public boolean getRoutingTsv() {
		return routingTsv;
	}
}
