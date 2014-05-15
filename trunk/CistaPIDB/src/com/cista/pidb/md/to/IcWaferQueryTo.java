package com.cista.pidb.md.to;


import com.cista.pidb.core.BaseQueryTo;

public class IcWaferQueryTo extends BaseQueryTo{
    private String materialNum;
    private String projCode;
    private String bodyVer;
    private String optionVer;
    private String projCodeWVersion;
    private String routingWf;
    private String routingBp;
    private String routingCp;
    private String status;
    private String tapeOutDateFrom;
    private String tapeOutDateTo;
    private String fabDeviceId;
    private String revisionItem;
    private String maskHouse;
    private String releaseTo;
    
    public String getReleaseTo() {
		return releaseTo;
	}
	public void setReleaseTo(String releaseTo) {
		this.releaseTo = releaseTo;
	}
	public String getMaskHouse() {
		return maskHouse;
	}
	public void setMaskHouse(String maskHouse) {
		this.maskHouse = maskHouse;
	}
	public String getBodyVer() {
        return bodyVer;
    }
    public void setBodyVer(String bodyVer) {
        this.bodyVer = bodyVer;
    }
    public String getMaterialNum() {
        return materialNum;
    }
    public void setMaterialNum(String materialNum) {
        this.materialNum = materialNum;
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
   
    public String getRoutingBp() {
      	 return routingBp;
      }
    public void setRoutingBp(String routingBp) {
        this.routingBp = routingBp;
    }
    
    public String getRoutingCp() {
    	return routingCp;
    }
    public void setRoutingCp(String routingCp) {
        this.routingCp = routingCp;
    }
    
    public String getRoutingWf() {
    	return routingWf;
    }
    public void setRoutingWf(String routingWf) {
        this.routingWf = routingWf;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
	public String getTapeOutDateFrom() {
		return tapeOutDateFrom;
	}
	public void setTapeOutDateFrom(String tapeOutDateFrom) {
		this.tapeOutDateFrom = tapeOutDateFrom;
	}
	public String getTapeOutDateTo() {
		return tapeOutDateTo;
	}
	public void setTapeOutDateTo(String tapeOutDateTo) {
		this.tapeOutDateTo = tapeOutDateTo;
	}
    public String getFabDeviceId() {
        return fabDeviceId;
    }
    public void setFabDeviceId(String fabDeviceId) {
        this.fabDeviceId = fabDeviceId;
    }
    public String getRevisionItem() {
        return revisionItem;
    }
    public void setRevisionItem(String revisionItem) {
        this.revisionItem = revisionItem;
    }
}
