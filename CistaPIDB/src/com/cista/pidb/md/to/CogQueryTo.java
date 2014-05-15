package com.cista.pidb.md.to;

import com.cista.pidb.core.BaseQueryTo;

public class CogQueryTo extends BaseQueryTo {
    private String prodName;
    private String prodCode;
    private String pkgCode;
    private String traySize;
    private String trayDrawingNo;
    private String trayDrawingNoVer;
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
    public String getTrayDrawingNo() {
        return trayDrawingNo;
    }
    public void setTrayDrawingNo(String trayDrawingNo) {
        this.trayDrawingNo = trayDrawingNo;
    }
    public String getTrayDrawingNoVer() {
        return trayDrawingNoVer;
    }
    public void setTrayDrawingNoVer(String trayDrawingNoVer) {
        this.trayDrawingNoVer = trayDrawingNoVer;
    }
    public String getTraySize() {
        return traySize;
    }
    public void setTraySize(String traySize) {
        this.traySize = traySize;
    }
	public String getProdName() {
		return prodName;
	}
	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

}
