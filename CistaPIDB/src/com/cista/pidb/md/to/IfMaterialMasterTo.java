package com.cista.pidb.md.to;

import java.util.Date;

public class IfMaterialMasterTo {
    private String materialNum;
    private String materialType;
    private String materialDesc;
    private String materialGroup;
    private String appCategory;
    private String productFamily;
    private String pkgType;
    private String icType;
    private String purchaseOrderText;
    private String basicDataText;
    private String sapStatus;
    private String infoMessage;
    private Date timeStamp;
    private int id;
    private String releasedBy;
    private String mpStatus;
    private String releaseTo;
    
    public String getReleaseTo() {
		return releaseTo;
	}

	public void setReleaseTo(String releaseTo) {
		this.releaseTo = releaseTo;
	}

	public String getMpStatus() {
        return mpStatus;
    }

    public void setMpStatus(String mpStatus) {
        this.mpStatus = mpStatus;
    }
    
    public String getAppCategory() {
        return appCategory;
    }
    public void setAppCategory(String appCategory) {
        this.appCategory = appCategory;
    }
    public String getBasicDataText() {
        return basicDataText;
    }
    public void setBasicDataText(String basicDataText) {
        this.basicDataText = basicDataText;
    }
    public String getIcType() {
        return icType;
    }
    public void setIcType(String icType) {
        this.icType = icType;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getInfoMessage() {
        return infoMessage;
    }
    public void setInfoMessage(String infoMessage) {
        this.infoMessage = infoMessage;
    }
    public String getMaterialDesc() {
        return materialDesc;
    }
    public void setMaterialDesc(String materialDesc) {
        this.materialDesc = materialDesc;
    }
    public String getMaterialGroup() {
        return materialGroup;
    }
    public void setMaterialGroup(String materialGroup) {
        this.materialGroup = materialGroup;
    }
    public String getMaterialNum() {
        return materialNum;
    }
    public void setMaterialNum(String materialNum) {
        this.materialNum = materialNum;
    }
    public String getMaterialType() {
        return materialType;
    }
    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }
    public String getPkgType() {
        return pkgType;
    }
    public void setPkgType(String pkgType) {
        this.pkgType = pkgType;
    }
    public String getProductFamily() {
        return productFamily;
    }
    public void setProductFamily(String productFamily) {
        this.productFamily = productFamily;
    }
    public String getPurchaseOrderText() {
        return purchaseOrderText;
    }
    public void setPurchaseOrderText(String purchasingOrderText) {
        this.purchaseOrderText = purchasingOrderText;
    }
    public String getReleasedBy() {
        return releasedBy;
    }
    public void setReleasedBy(String releasedBy) {
        this.releasedBy = releasedBy;
    }
    public String getSapStatus() {
        return sapStatus;
    }
    public void setSapStatus(String sapStatus) {
        this.sapStatus = sapStatus;
    }
    public Date getTimeStamp() {
        return timeStamp;
    }
    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }
}
