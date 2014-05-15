package com.cista.pidb.md.to;

import com.cista.pidb.core.BaseQueryTo;

public class CspQueryTo extends BaseQueryTo {

	private String projName;
	private String pkgName;
	private String pkgCode;

	public String getProjName() {
		return projName;
	}

	public void setProjName(String projName) {
		this.projName = projName;
	}

	public String getPkgName() {
		return pkgName;
	}

	public void setPkgName(String pkgName) {
		this.pkgName = pkgName;
	}

	public String getPkgCode() {
		return pkgCode;
	}

	public void setPkgCode(String pkgCode) {
		this.pkgCode = pkgCode;
	}

}
