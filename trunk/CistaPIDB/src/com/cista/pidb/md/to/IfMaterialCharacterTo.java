package com.cista.pidb.md.to;

import java.util.Date;

public class IfMaterialCharacterTo {
    private String materialNum;
    private String materialType;
    private String chTechName;
    private String chValue;
    private String sapStatus;
    private String infoMessage;
    private Date timeStamp;
    private int id;
    private String releasedBy;
    private String releaseTo;
    
    public String getReleaseTo() {
		return releaseTo;
	}
	public void setReleaseTo(String releaseTo) {
		this.releaseTo = releaseTo;
	}
	public String getChTechName() {
        return chTechName;
    }
    public void setChTechName(String chTechName) {
        this.chTechName = chTechName;
    }
    public String getChValue() {
        return chValue;
    }
    public void setChValue(String chValue) {
        this.chValue = chValue;
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
