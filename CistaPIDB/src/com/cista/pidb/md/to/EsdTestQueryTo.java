package com.cista.pidb.md.to;

import com.cista.pidb.core.BaseQueryTo;

public class EsdTestQueryTo extends BaseQueryTo {
	private String prodCode;
	private String idEsdTesting;
	private String projCodeWVersion;
	private String owner;
	private String esdFinishDate;

	public String getOwner() {
		return owner;
	}

	public String getIdEsdTesting() {
		return idEsdTesting;
	}

	public void setIdEsdTesting(String idEsdTesting) {
		this.idEsdTesting = idEsdTesting;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getProdCode() {
		return prodCode;
	}

	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}

	public String getProjCodeWVersion() {
		return projCodeWVersion;
	}

	public void setProjCodeWVersion(String projCodeWVersion) {
		this.projCodeWVersion = projCodeWVersion;
	}

	public String getEsdFinishDate() {
		return esdFinishDate;
	}

	public void setEsdFinishDate(String esdFinishDate) {
		this.esdFinishDate = esdFinishDate;
	}

}
