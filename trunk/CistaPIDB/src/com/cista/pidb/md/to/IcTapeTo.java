package com.cista.pidb.md.to;

import java.util.Date;

public class IcTapeTo {

    private String materialNum;
    private String tapeName;
    private String tapeVariant;
    private String pkgVersion;
    private String prodName;
    private String pkgCode;
    private String tapeType;
    private String tapeVendor;
    private String tapeWidth;
    private String sprocketHoleNum;
    private String minPitch;
    private String tapeMaterial;
    private String assySite;
    private String tapeCust;
    private String tapeCustProjName;
    private Date tapeApproveDate;
    private String remark;
    private Date custDrawingIssueDate;
    private Date tcpDrawingFinishDate;
    private Date tapeMakerConfirmDoneDate;
    private Date custApprovalDoneDate;
    private Date tapeMakingFinishDoneDate;
    private String tpInfo;
    private String assignTo;
    private String assignEmail;
    private String status;
    private String createdBy;
    private String modifiedBy;
    private String ilPitch;
    private String cuLayer; //Add by 900it 2008/04/15
    private String cuThicknessPattern; //Add by 900it 2008/04/15
    private String cuThicknessBack; //Add by 900it 2008/04/15
    private String revisionReason; //Add by 900it 2008/04/15
    private String tapeProcess; //Add by 990044 2008/06/03
    private String releaseTo;
    
    //***2010/08/26***//
    private String olbCrossTop;
    private String olbCrossBottom;
    private String newProcessReason;
    private String olOsTotalPitch;
    private String olIsTotalPitch;
    private String outputChannel;
    
    private String chipSize;
    private String reelSize;
    private String srMaterial;
    private String spacerType;
    private String cdt;
    private String udt;
    
    public String getReleaseTo() {
		return releaseTo;
	}
	public void setReleaseTo(String releaseTo) {
		this.releaseTo = releaseTo;
	}
	public String getTapeProcess() {
		return tapeProcess;
	}
	public void setTapeProcess(String tapeProcess) {
		this.tapeProcess = tapeProcess;
	}
	public String getAssignEmail() {
        return assignEmail;
    }
    public String getCuLayer() { //Add by 900it 2008/04/15
		return cuLayer;
	}
	public void setCuLayer(String cuLayer) { //Add by 900it 2008/04/15
		this.cuLayer = cuLayer;
	}
	public String getCuThicknessPattern() { //Add by 900it 2008/04/15
		return cuThicknessPattern;
	}
	public void setCuThicknessPattern(String cuThicknessPattern) { //Add by 900it 2008/04/15
		this.cuThicknessPattern = cuThicknessPattern;
	}
	public String getCuThicknessBack() { //modify by 900it 2008/04/15
		return cuThicknessBack;
	}
	public void setCuThicknessBack(String cuThicknessBack) { //Add by 900it 2008/04/15
		this.cuThicknessBack = cuThicknessBack;
	}
	public String getRevisionReason() { //Add by 900it 2008/04/15
		return revisionReason;
	}
	public void setRevisionReason(String revisionReason) { //Add by 900it 2008/04/15
		this.revisionReason = revisionReason;
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
    public String getAssySite() {
        return assySite;
    }
    public void setAssySite(String assySite) {
        this.assySite = assySite;
    }
    public String getCreatedBy() {
        return createdBy;
    }
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    public Date getCustApprovalDoneDate() {
        return custApprovalDoneDate;
    }
    public void setCustApprovalDoneDate(Date custApprovalDoneDate) {
        this.custApprovalDoneDate = custApprovalDoneDate;
    }
    public Date getCustDrawingIssueDate() {
        return custDrawingIssueDate;
    }
    public void setCustDrawingIssueDate(Date custDrawingIssueDate) {
        this.custDrawingIssueDate = custDrawingIssueDate;
    }
    public String getMaterialNum() {
        return materialNum;
    }
    public void setMaterialNum(String materialNum) {
        this.materialNum = materialNum;
    }
    public String getMinPitch() {
        return minPitch;
    }
    public void setMinPitch(String minPitch) {
        this.minPitch = minPitch;
    }
    public String getModifiedBy() {
        return modifiedBy;
    }
    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
    public String getPkgCode() {
        return pkgCode;
    }
    public void setPkgCode(String pkgCode) {
        this.pkgCode = pkgCode;
    }
    public String getPkgVersion() {
        return pkgVersion;
    }
    public void setPkgVersion(String pkgVersion) {
        this.pkgVersion = pkgVersion;
    }
    public String getProdName() {
        return prodName;
    }
    public void setProdName(String prodName) {
        this.prodName = prodName;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getSprocketHoleNum() {
        return sprocketHoleNum;
    }
    public void setSprocketHoleNum(String sprocketHoleNum) {
        this.sprocketHoleNum = sprocketHoleNum;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public Date getTapeApproveDate() {
        return tapeApproveDate;
    }
    public void setTapeApproveDate(Date tapeApproveDate) {
        this.tapeApproveDate = tapeApproveDate;
    }
    public String getTapeCust() {
        return tapeCust;
    }
    public void setTapeCust(String tapeCust) {
        this.tapeCust = tapeCust;
    }
    public String getTapeCustProjName() {
        return tapeCustProjName;
    }
    public void setTapeCustProjName(String tapeCustProjName) {
        this.tapeCustProjName = tapeCustProjName;
    }
    public Date getTapeMakerConfirmDoneDate() {
        return tapeMakerConfirmDoneDate;
    }
    public void setTapeMakerConfirmDoneDate(Date tapeMakerConfirmDoneDate) {
        this.tapeMakerConfirmDoneDate = tapeMakerConfirmDoneDate;
    }
    public Date getTapeMakingFinishDoneDate() {
        return tapeMakingFinishDoneDate;
    }
    public void setTapeMakingFinishDoneDate(Date tapeMakingFinishDoneDate) {
        this.tapeMakingFinishDoneDate = tapeMakingFinishDoneDate;
    }
    public String getTapeMaterial() {
        return tapeMaterial;
    }
    public void setTapeMaterial(String tapeMaterial) {
        this.tapeMaterial = tapeMaterial;
    }
    public String getTapeName() {
        return tapeName;
    }
    public void setTapeName(String tapeName) {
        this.tapeName = tapeName;
    }
    public String getTapeType() {
        return tapeType;
    }
    public void setTapeType(String tapeType) {
        this.tapeType = tapeType;
    }
    public String getTapeVariant() {
        return tapeVariant;
    }
    public void setTapeVariant(String tapeVariant) {
        this.tapeVariant = tapeVariant;
    }
    public String getTapeVendor() {
        return tapeVendor;
    }
    public void setTapeVendor(String tapeVendor) {
        this.tapeVendor = tapeVendor;
    }
    public String getTapeWidth() {
        return tapeWidth;
    }
    public void setTapeWidth(String tapeWidth) {
        this.tapeWidth = tapeWidth;
    }
    public Date getTcpDrawingFinishDate() {
        return tcpDrawingFinishDate;
    }
    public void setTcpDrawingFinishDate(Date tcpDrawingFinishDate) {
        this.tcpDrawingFinishDate = tcpDrawingFinishDate;
    }
    public String getTpInfo() {
        return tpInfo;
    }
    public void setTpInfo(String tpInfo) {
        this.tpInfo = tpInfo;
    }
    public String getIlPitch() {
        return ilPitch;
    }
    public void setIlPitch(String ilPitch) {
        this.ilPitch = ilPitch;
    }

	/**
	 * @return the olbCrossTop
	 */
	public String getOlbCrossTop() {
		return olbCrossTop;
	}
	/**
	 * @param olbCrossTop the olbCrossTop to set
	 */
	public void setOlbCrossTop(String olbCrossTop) {
		this.olbCrossTop = olbCrossTop;
	}
	/**
	 * @return the olbCrossBottom
	 */
	public String getOlbCrossBottom() {
		return olbCrossBottom;
	}
	/**
	 * @param olbCrossBottom the olbCrossBottom to set
	 */
	public void setOlbCrossBottom(String olbCrossBottom) {
		this.olbCrossBottom = olbCrossBottom;
	}
	/**
	 * @return the newProcessReason
	 */
	public String getNewProcessReason() {
		return newProcessReason;
	}
	/**
	 * @param newProcessReason the newProcessReason to set
	 */
	public void setNewProcessReason(String newProcessReason) {
		this.newProcessReason = newProcessReason;
	}
	/**
	 * @return the olOsTotalPitch
	 */
	public String getOlOsTotalPitch() {
		return olOsTotalPitch;
	}
	/**
	 * @param olOsTotalPitch the olOsTotalPitch to set
	 */
	public void setOlOsTotalPitch(String olOsTotalPitch) {
		this.olOsTotalPitch = olOsTotalPitch;
	}
	/**
	 * @return the olIsTotalPitch
	 */
	public String getOlIsTotalPitch() {
		return olIsTotalPitch;
	}
	/**
	 * @param olIsTotalPitch the olIsTotalPitch to set
	 */
	public void setOlIsTotalPitch(String olIsTotalPitch) {
		this.olIsTotalPitch = olIsTotalPitch;
	}
	/**
	 * @return the outputChannel
	 */
	public String getOutputChannel() {
		return outputChannel;
	}
	/**
	 * @param outputChannel the outputChannel to set
	 */
	public void setOutputChannel(String outputChannel) {
		this.outputChannel = outputChannel;
	}
	/**
	 * @return the chipSize
	 */
	public String getChipSize() {
		return chipSize;
	}
	/**
	 * @param chipSize the chipSize to set
	 */
	public void setChipSize(String chipSize) {
		this.chipSize = chipSize;
	}
	/**
	 * @return the reelSize
	 */
	public String getReelSize() {
		return reelSize;
	}
	/**
	 * @param reelSize the reelSize to set
	 */
	public void setReelSize(String reelSize) {
		this.reelSize = reelSize;
	}
	/**
	 * @return the srMaterial
	 */
	public String getSrMaterial() {
		return srMaterial;
	}
	/**
	 * @param srMaterial the srMaterial to set
	 */
	public void setSrMaterial(String srMaterial) {
		this.srMaterial = srMaterial;
	}
	/**
	 * @return the spacerType
	 */
	public String getSpacerType() {
		return spacerType;
	}
	/**
	 * @param spacerType the spacerType to set
	 */
	public void setSpacerType(String spacerType) {
		this.spacerType = spacerType;
	}
	/**
	 * @return the cdt
	 */
	public String getCdt() {
		return cdt;
	}
	/**
	 * @param cdt the cdt to set
	 */
	public void setCdt(String cdt) {
		this.cdt = cdt;
	}
	/**
	 * @return the udt
	 */
	public String getUdt() {
		return udt;
	}
	/**
	 * @param udt the udt to set
	 */
	public void setUdt(String udt) {
		this.udt = udt;
	}

    
}
