package com.cista.pidb.md.to;

public class RptProjCodeHitRateQueryTo 
{
	private String prodFamily;
	private String prodLine;
	private String projCode;
	private String projName;
	
	
	public String getProdLine() 
	{
		return prodLine;
	}
	public void setProdLine(String prodLine) {
		this.prodLine = prodLine;
	}
	public String getProdFamily() 
	{
		return prodFamily;
	}
	public void setProdFamily(String prodFamily) 
	{
		this.prodFamily = prodFamily;
	}
	public String getProjCode() 
	{
		return projCode;
	}
	public void setProjCode(String projCode) 
	{
		this.projCode = projCode;
	}
	public String getProjName() 
	{
		return projName;
	}
	public void setProjName(String projName) 
	{
		this.projName = projName;
	}
	
}
