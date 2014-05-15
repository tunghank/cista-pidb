package com.cista.pidb.md.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.cista.pidb.code.dao.FabCodeDao;
import com.cista.pidb.code.dao.SapMasterProductFamilyDao;
import com.cista.pidb.code.to.FabCodeTo;
import com.cista.pidb.core.HttpHelper;
import com.cista.pidb.md.dao.ProductDao;
import com.cista.pidb.md.dao.ProjectDao;
import com.cista.pidb.md.dao.RptProdFabDao;
import com.cista.pidb.md.to.ProductTo;
import com.cista.pidb.md.to.ProjectQueryTo;
import com.cista.pidb.md.to.ProjectTo;
import com.cista.pidb.md.to.RptProdFabQueryTo;
import com.cista.pidb.md.to.WmVendorTo;


public class RptProdToFabQueryAction extends DispatchAction
{
	String forward;
	protected final Log logger = LogFactory.getLog(getClass());
	
	
	public ActionForward query(final ActionMapping mapping,	final ActionForm form, final HttpServletRequest request, 
			                   final HttpServletResponse response) 
	{
		forward = "result";
		ArrayList<String>   columnArrayList;	
		ArrayList<String>   gridDataArrayList;
		
		ProductDao          productNMDao;
		ProjectDao          projectDao;
		List<ProductTo>     productListTo;
		List<ProjectTo>     projectListTo;
		ProjectQueryTo      projQueryTo;
		RptProdFabDao       rptProdFabDao;
		List<WmVendorTo>    wmVendorListTo;
		FabCodeDao          fabCodeDao;
		StringBuffer        sbGridData;
		StringBuffer        sbFabData;
		FabCodeTo			fabCodeTo;
		SapMasterProductFamilyDao prodFamilyDao;
		
		String              sQueryProdNameList;
		String   			sFab;
		String 				sFunctionRemark;
		String 				sGrossDie;
		String              sFamily;
		String              sPKG;
		String				sProdName;
		String 				sFabData;
		
		RptProdFabQueryTo queryTo = (RptProdFabQueryTo) HttpHelper.pickupForm(RptProdFabQueryTo.class, request, true);
		sQueryProdNameList = queryTo.getProdName();
		
		//0. init var
		columnArrayList = new ArrayList<String>();
		productNMDao = new ProductDao();
		projectDao = new ProjectDao();
		rptProdFabDao = new RptProdFabDao();
		fabCodeDao = new FabCodeDao();
		prodFamilyDao = new SapMasterProductFamilyDao();
		sFamily = "";
		sProdName = "";
		sFunctionRemark = "";
		sPKG = "";
		sFabData = "";
		
		//0.1.表頭欄位, 取出vendor資料, 放入vendor name
		columnArrayList.add("Family");
		columnArrayList.add("Product Name");
		columnArrayList.add("Function");
		columnArrayList.add("PKG");
		wmVendorListTo = rptProdFabDao.getWmFabVendorList();
		for(WmVendorTo vendorObj: wmVendorListTo)
		{
			columnArrayList.add(vendorObj.getShortName().replaceAll(" ",""));
		}
		//1. 用prod name取得prod code, from pidb_product
		productListTo = productNMDao.findByProdName(sQueryProdNameList);
		
		//grid data
		sbGridData = new StringBuffer();
		//fab資料
		sbFabData = new StringBuffer();
		
		for(WmVendorTo vendorObj: wmVendorListTo)
		{
			sbFabData.append("'");
			for(ProductTo prodObj: productListTo)
			{
				//2. 用prod code取得fab, function, grodss_die資料, from pidb_project
				projQueryTo = new ProjectQueryTo();
				projQueryTo.setProdCode(prodObj.getProdCode());
				//projectListTo = projectDao.query(projQueryTo);
				projectListTo = rptProdFabDao.queryProject(projQueryTo);
				sProdName = prodObj.getProdName();				
				for(ProjectTo projObj:projectListTo)
				{
					sFab =  transParameterNull(projObj.getFab());
					sGrossDie =transParameterNull( projObj.getGrossDie());
					sFunctionRemark = transParameterNull(projObj.getFuncRemark());
					//replace \r\n
					sFunctionRemark = sFunctionRemark.replaceAll("\r\n", "<br>");
					
					sFamily = transParameterNull(projObj.getProdFamily());
					sPKG = transParameterNull(projObj.getProdLine());								
					fabCodeTo = fabCodeDao.getByFab(sFab);
					String sFabVendorCode= "";
					if(fabCodeTo != null)
					{
						sFabVendorCode = transParameterNull(fabCodeTo.getVendorCode());
					}
					//取vendor code比對
					if(sFabVendorCode.equals(vendorObj.getVendorCode()))
					{
						sbFabData.append(transParameterNull(projObj.getProjCode()) );
						sbFabData.append("<br>(");
						sbFabData.append(sGrossDie);
						sbFabData.append(")<br>");
					}
				}
			}
			sbFabData.append("',");
		}
		
		sFamily = prodFamilyDao.findDescByProdFamily(sFamily);
		//3. 依序放入arraylist, 並比對fab對應的資料
		sbGridData.append("['");
		
		sbGridData.append(sFamily);
		sbGridData.append("','");
		sbGridData.append(sProdName);
		sbGridData.append("','");
		sbGridData.append(sFunctionRemark);
		sbGridData.append("','");
		sbGridData.append(sPKG);
		sbGridData.append("',");
		//去,
		if(sbFabData.length()>0)
		{
			sFabData= sbFabData.substring(0, sbFabData.length()-1);
		}
		sbGridData.append(sFabData);
	
		sbGridData.append("]");		
		
		//column structe
		StringBuffer sbColumn = new StringBuffer();
		StringBuffer sbColReader = new StringBuffer();
		String       sColumnStruct = "";
		String       sColumnReader = "";
			
		for(String colObj:columnArrayList)
		{
			//grid column
			sbColumn.append("{header: '");
			sbColumn.append(colObj.trim());
			sbColumn.append("', id: '");
			sbColumn.append(colObj.replaceAll(" ",""));
			sbColumn.append("', dataIndex: '");
			sbColumn.append(colObj.replaceAll(" ",""));
			if("Function".equals(colObj.trim()) || "Function".equals(colObj.trim()) )
			{
				sbColumn.append("', width: 130, sortable: true}  ");
			}
			else
			{
				sbColumn.append("', width: 100, sortable: true}  ");
			}
			sbColumn.append(",");			
			//proxy reader
			sbColReader.append("{name:'");
			sbColReader.append(colObj.replaceAll(" ",""));
			sbColReader.append("'} ");
			sbColReader.append(",");	
		}
		if(columnArrayList.size()>0)
		{
			sColumnStruct = sbColumn.substring(0, sbColumn.length()-1);
			sColumnReader = sbColReader.substring(0, sbColReader.length()-1);
		}
		
		//選單資料
		request.setAttribute("reqProdName", sQueryProdNameList);	        
		//回傳資料
		request.setAttribute("gridColumn", sColumnStruct);
		request.setAttribute("gridColumnReader", sColumnReader);
		request.setAttribute("gridData", sbGridData.toString());

		return mapping.findForward(forward);
	}
	
	public String transParameterNull(String inputParameter)
    {
        int zeroLength = 0;
        String outParameter ="";
        boolean isParameterNull = (inputParameter == null ||
                                   inputParameter.length() == zeroLength);
        if(isParameterNull)
        {
        	outParameter = " ";
        }
        else
        {
        	outParameter = inputParameter;
        }
        return outParameter;
    }
}
