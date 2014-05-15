package com.cista.pidb.md.to;

import com.cista.pidb.core.BaseQueryTo;

public class ProjectQueryTo extends BaseQueryTo {
	private String projCode;
	private String projName;
	private String prodCode;
	private String fab;
	private String projOption;
	private String panelType;
	private String prodFamily;
	private String prodLine;
	private String procTech;
	private String cust;
	private String teamMember;
	private String kickOffDateFrom;
	private String kickOffDateTo;
	private String status;
	private String estimated;
	private String nickName;
	private String releaseTo;

	public String getReleaseTo() {
		return releaseTo;
	}

	public void setReleaseTo(String releaseTo) {
		this.releaseTo = releaseTo;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getCust() {
		return cust;
	}

	public void setCust(String cust) {
		this.cust = cust;
	}

	public String getFab() {
		return fab;
	}

	public void setFab(String fab) {
		this.fab = fab;
	}

	public String getPanelType() {
		return panelType;
	}

	public void setPanelType(String panelType) {
		this.panelType = panelType;
	}

	public String getProcTech() {
		return procTech;
	}

	public void setProcTech(String procTech) {
		this.procTech = procTech;
	}

	public String getProdCode() {
		return prodCode;
	}

	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}

	public String getProdFamily() {
		return prodFamily;
	}

	public void setProdFamily(String prodFamily) {
		this.prodFamily = prodFamily;
	}

	public String getProdLine() {
		return prodLine;
	}

	public void setProdLine(String prodLine) {
		this.prodLine = prodLine;
	}

	public String getProjCode() {
		return projCode;
	}

	public void setProjCode(String projCode) {
		this.projCode = projCode;
	}

	public String getProjName() {
		return projName;
	}

	public void setProjName(String projName) {
		this.projName = projName;
	}

	public String getProjOption() {
		return projOption;
	}

	public void setProjOption(String projOption) {
		this.projOption = projOption;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTeamMember() {
		return teamMember;
	}

	public void setTeamMember(String teamMember) {
		this.teamMember = teamMember;
	}

	public String getEstimated() {
		return estimated;
	}

	public void setEstimated(String estimated) {
		this.estimated = estimated;
	}

	public String getKickOffDateFrom() {
		return kickOffDateFrom;
	}

	public void setKickOffDateFrom(String kickOffDateFrom) {
		this.kickOffDateFrom = kickOffDateFrom;
	}

	public String getKickOffDateTo() {
		return kickOffDateTo;
	}

	public void setKickOffDateTo(String kickOffDateTo) {
		this.kickOffDateTo = kickOffDateTo;
	}
}
