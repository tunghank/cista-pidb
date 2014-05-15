package com.cista.pidb.md.to;

import com.cista.pidb.core.BaseQueryTo;

public class MpListQueryTo extends BaseQueryTo {

	private String partNum;
	private String prodCode;
	private String pkgCode;
	private String projCode;
	private String projCodeWVersion;
	private String tapeName;
	private String mpReleaseDateFrom;
	private String mpReleaseDateTo;
	private String materialNum;
	private String revisionItem;
	private String approveCust;
	private String releaseTo;

	public String getReleaseTo() {
		return releaseTo;
	}

	public void setReleaseTo(String releaseTo) {
		this.releaseTo = releaseTo;
	}

	public String getApproveCust() {
		return approveCust;
	}

	public void setApproveCust(String approveCust) {
		this.approveCust = approveCust;
	}

	public String getRevisionItem() {
		return revisionItem;
	}

	public void setRevisionItem(String revisionItem) {
		this.revisionItem = revisionItem;
	}

	public String getMaterialNum() {
		return materialNum;
	}

	public void setMaterialNum(String materialNum) {
		this.materialNum = materialNum;
	}

	public String getMpReleaseDateTo() {
		return mpReleaseDateTo;
	}

	public void setMpReleaseDateTo(String mpReleaseDateTo) {
		this.mpReleaseDateTo = mpReleaseDateTo;
	}

	public String getMpReleaseDateFrom() {
		return mpReleaseDateFrom;
	}

	public void setMpReleaseDateFrom(String mpReleaseDateFrom) {
		this.mpReleaseDateFrom = mpReleaseDateFrom;
	}

	public String getPartNum() {
		return partNum;
	}

	public void setPartNum(String partNum) {
		this.partNum = partNum;
	}

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

	public String getTapeName() {
		return tapeName;
	}

	public void setTapeName(String tapeName) {
		this.tapeName = tapeName;
	}

}
