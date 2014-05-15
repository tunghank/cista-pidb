package com.cista.pidb.md.to;

import java.util.Date;

public class IfProjectCodeTo {
    private String projCode;
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
	public String getProjCode() {
		return projCode;
	}
	public void setProjCode(String projCode) {
		this.projCode = projCode;
	}
 
}
