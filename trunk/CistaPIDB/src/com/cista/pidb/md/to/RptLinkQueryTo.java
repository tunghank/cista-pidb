package com.cista.pidb.md.to;

import com.cista.pidb.core.BaseQueryTo;

public class RptLinkQueryTo extends BaseQueryTo
{

	private	String	partNo;	
	private	String	projCode;
	private	String	fab;
	private	String	tapeName;
	private	String	cpTester;
	private	String	ftTester;
	
	
	public String getPartNo() {
		return partNo;
	}
	public void setPartNo(String partNo) {
		this.partNo = partNo;
	}
	public String getProjCode() {
		return projCode;
	}
	public void setProjCode(String projCode) {
		this.projCode = projCode;
	}
	public String getFab() {
		return fab;
	}
	public void setFab(String fab) {
		this.fab = fab;
	}
	public String getTapeName() {
		return tapeName;
	}
	public void setTapeName(String tapeName) {
		this.tapeName = tapeName;
	}
	public String getCpTester() {
		return cpTester;
	}
	public void setCpTester(String cpTester) {
		this.cpTester = cpTester;
	}
	public String getFtTester() {
		return ftTester;
	}
	public void setFtTester(String ftTester) {
		this.ftTester = ftTester;
	}

}
