package com.cista.pidb.md.to;

import java.util.Date;

public class PidbSourceListTo {
	private int id;
    private String materialNum;
    private String vendor;
    private String mpStatus;

    private String plantCode;
    private String sapStatus;
    private String infoMessage;
    private Date timeStamp;
    private String releasedBy;
    private String releaseTo;
    

    public String getReleaseTo() {
		return releaseTo;
	}
	public void setReleaseTo(String releaseTo) {
		this.releaseTo = releaseTo;
	}
	public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    
    public String getMaterialNum() {
        return materialNum;
    }
    public void setMaterialNum(String materialNum) {
        this.materialNum = materialNum;
    }
    
    public String getVendor() {
        return vendor;
    }
    public void setVendor(String vendor) {
        this.vendor = vendor;
    }
    
    public String getMpStatus() {
        return mpStatus;
    }
    public void setMpStatus(String mpStatus) {
        this.mpStatus = mpStatus;
    }
    public String getPlantCode() {
        return plantCode;
    }
    public void setPlantCode(String plantCode) {
        this.plantCode = plantCode;
    }    
    public String getSapStatus() {
        return sapStatus;
    }
    public void setSapStatus(String sapStatus) {
        this.sapStatus = sapStatus;
    }
    public String getInfoMessage() {
        return infoMessage;
    }
    public void setInfoMessage(String infoMessage) {
        this.infoMessage = infoMessage;
    }
    public Date getTimeStamp() {
        return timeStamp;
    }
    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }
    public String getReleasedBy() {
        return releasedBy;
    }
    public void setReleasedBy(String releasedBy) {
        this.releasedBy = releasedBy;
    }

}
