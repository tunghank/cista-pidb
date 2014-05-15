package com.cista.pidb.md.to;
import com.cista.pidb.core.BaseQueryTo;

public class PkgRaQueryTo extends BaseQueryTo{

    private String prodName;
    private String projName;
    private String pkgCode;
    private String worksheetNumber;
    private String partNum;
    private String tapeVendor;
    private String assySite;
    private String pkgRaActualStartTime;
    private String pkgRaActualEndTime;
    private String owner;
  

	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getAssySite() {
		return assySite;
	}
	public void setAssySite(String assySite) {
		this.assySite = assySite;
	}
	public String getTapeVendor() {
		return tapeVendor;
	}
	public void setTapeVendor(String tapeVendor) {
		this.tapeVendor = tapeVendor;
	}
	public String getWorksheetNumber() {
		return worksheetNumber;
	}
	public void setWorksheetNumber(String worksheetNumber) {
		this.worksheetNumber = worksheetNumber;
	}
	public String getPartNum() {
		return partNum;
	}
	public void setPartNum(String partNum) {
		this.partNum = partNum;
	}
	public String getProjName() {
		return projName;
	}
	public void setProjName(String projName) {
		this.projName = projName;
	}
	public String getPkgCode() {
        return pkgCode;
    }
    public void setPkgCode(String pkgCode) {
        this.pkgCode = pkgCode;
    }
   
    public String getProdName() {
        return prodName;
    }
    public void setProdName(String prodName) {
        this.prodName = prodName;
    }
	public String getPkgRaActualStartTime() {
		return pkgRaActualStartTime;
	}
	public void setPkgRaActualStartTime(String pkgRaActualStartTime) {
		this.pkgRaActualStartTime = pkgRaActualStartTime;
	}
	public String getPkgRaActualEndTime() {
		return pkgRaActualEndTime;
	}
	public void setPkgRaActualEndTime(String pkgRaActualEndTime) {
		this.pkgRaActualEndTime = pkgRaActualEndTime;
	}
    

}
