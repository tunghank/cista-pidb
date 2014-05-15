package com.cista.pidb.md.to;

import com.cista.pidb.core.BaseQueryTo;

public class IcTapeQueryTo extends BaseQueryTo {

	private String materialNum;
	private String pkgCode;
	private String pkgVersion;
	private String tapeName;
	private String prodName;
	private String tapeWidth;
	private String sprocketHoleNum;
	private String minPitch;
	private String tapeCust;
	private String tapeCustProjName;
	private String tapeVendor;
	private String releaseTo;

	public String getReleaseTo() {
		return releaseTo;
	}

	public void setReleaseTo(String releaseTo) {
		this.releaseTo = releaseTo;
	}

	public String getTapeVendor() {
		return tapeVendor;
	}

	public void setTapeVendor(String tapeVendor) {
		this.tapeVendor = tapeVendor;
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

	public String getSprocketHoleNum() {
		return sprocketHoleNum;
	}

	public void setSprocketHoleNum(String sprocketHoleNum) {
		this.sprocketHoleNum = sprocketHoleNum;
	}

	public String getTapeName() {
		return tapeName;
	}

	public void setTapeName(String tapeName) {
		this.tapeName = tapeName;
	}

	public String getTapeWidth() {
		return tapeWidth;
	}

	public void setTapeWidth(String tapeWidth) {
		this.tapeWidth = tapeWidth;
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
}
