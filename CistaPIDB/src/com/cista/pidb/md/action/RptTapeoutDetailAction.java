/*
 * 2010.03.05/FCG1 @Jere Huang - Initial Version.
 */
package com.cista.pidb.md.action;

import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.cista.pidb.code.dao.SapMasterProductFamilyDao;
import com.cista.pidb.core.HttpHelper;
import com.cista.pidb.md.dao.RptTapeoutDetailDao;
import com.cista.pidb.md.to.ProjectQueryTo;
import com.cista.pidb.md.to.ProjectTo;
import com.cista.pidb.md.to.RptProdTapeoutPeriodQueryTo;
import com.cista.pidb.md.to.RptProdTapeoutRevisionQueryTo;
import com.cista.pidb.md.to.RptTapeOutIcWaferListTo;
import com.cista.pidb.md.to.RptTapeOutMasksetListTo;
import com.cista.pidb.md.to.RptTapeOutMasksetQueryTo;



public class RptTapeoutDetailAction extends DispatchAction
{
	String forward;
	protected final Log logger = LogFactory.getLog(getClass());
	
	
	public ActionForward query(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response)
	{
		forward = "result";
		int							  iTapeoutCount;
		int							  iCount;
		String    					  sQueryTapeOutYearFrom = "";
		String    					  sQueryTapeOutMonthFrom = "";
		String    					  sQueryTapeOutYearTo = "";
		String    					  sQueryTapeOutMonthTo = "";
		String						  sProjName = "";
		String 						  sVersion = "";
		String						  sProjCodeWVer = "";
		String						  sTapeOutDate ="";
		String						  sCurrMaskset = "";
		String						  sDiffMaskset = "";
		String						  sYearMonth = "";
		String						  sKeepYearMonth = "";
		String						  sQueryProdLine = "";
		String						  sQueryProdFamilyCode = "";
		String						  sQueryProdFamilyName = "";
		StringBuffer                  sbGridData;
		List<ProjectTo>               projectListTo;
		ProjectQueryTo				  projQueryTo;
		RptTapeoutDetailDao           tapeOutDetailDao;
		SapMasterProductFamilyDao     sapProdFamilyDao;
		SapMasterProductFamilyDao     prodFamilyDao;
		RptTapeOutMasksetQueryTo      masksetQueryTo;
		RptProdTapeoutRevisionQueryTo revisionQueryTo;
		List<RptTapeOutIcWaferListTo> tapeOutWaferListTo;
		List<RptTapeOutMasksetListTo> tapeOutMasksetListTo;
		
		prodFamilyDao = new SapMasterProductFamilyDao();
		
		RptProdTapeoutPeriodQueryTo queryTo = (RptProdTapeoutPeriodQueryTo) HttpHelper.pickupForm(RptProdTapeoutPeriodQueryTo.class, request, true);
		revisionQueryTo = (RptProdTapeoutRevisionQueryTo) HttpHelper.pickupForm(RptProdTapeoutRevisionQueryTo.class, request, true);
		sQueryTapeOutYearFrom = revisionQueryTo.getTapeOutYearFrom();
		sQueryTapeOutMonthFrom = revisionQueryTo.getTapeOutMonthFrom();
		sQueryTapeOutYearTo = revisionQueryTo.getTapeOutYearTo();
		sQueryTapeOutMonthTo = revisionQueryTo.getTapeOutMonthTo();
		sQueryProdLine = revisionQueryTo.getProdLine();
		//轉換成數字
		sQueryProdFamilyName = queryTo.getProdFamily();
		if(sQueryProdFamilyName != null)
		{
			sQueryProdFamilyCode = prodFamilyDao.findFamilyCodeByDesc(sQueryProdFamilyName);	
            queryTo.setProdFamily(sQueryProdFamilyCode);
		}
		
		//0. init variable
		sbGridData = new StringBuffer();
		sapProdFamilyDao = new SapMasterProductFamilyDao();
		masksetQueryTo = new RptTapeOutMasksetQueryTo();
		Calendar calDateFrom = Calendar.getInstance();
		calDateFrom.set(Calendar.YEAR, Integer.parseInt(queryTo.getTapeOutYearFrom()) );
		calDateFrom.set(Calendar.MONTH, Integer.parseInt(queryTo.getTapeOutMonthFrom())-1 );
		
		Calendar calDatetTo = Calendar.getInstance();
		calDatetTo.set(Calendar.YEAR, Integer.parseInt(queryTo.getTapeOutYearTo()) );
		calDatetTo.set(Calendar.MONTH, Integer.parseInt(queryTo.getTapeOutMonthTo()) );
		
		//1. 依照日期順序找出ic wafer資料, tape_out_date, revision_item, proj_code_w_version, mask_layer_com.
		tapeOutDetailDao = new RptTapeoutDetailDao();
tapeOutWaferListTo = tapeOutDetailDao.queryTapeOutDetailByPeriod(queryTo);
		sKeepYearMonth = ""; 
		iTapeoutCount = 1;
		iCount = 1;
		for(RptTapeOutIcWaferListTo tapeWaferObj :tapeOutWaferListTo)
		{
			//1.1 取的version資料
			sProjName =transParameterNull(tapeWaferObj.getProjName() );
			sProjCodeWVer = tapeWaferObj.getProjCodeWVersion();
			sVersion =  sProjCodeWVer.replaceAll(sProjName, "");
			sCurrMaskset =  tapeWaferObj.getMaskLayerCom();
			sYearMonth = tapeWaferObj.getYearMonth();
			//第一筆
			if(sbGridData.length()==0)
			{
				sKeepYearMonth = sYearMonth;
			}
			//判斷是否有要加total資料
			if(! sYearMonth.equals(sKeepYearMonth))
			{
				sbGridData.append("['");
				sbGridData.append(sKeepYearMonth );
				sbGridData.append("','','','','','','','','");
				sbGridData.append(Integer.toString(iTapeoutCount-1) );
				sbGridData.append("'],");
				//將count重新計算
				iTapeoutCount = 1;
			}
			
//			sbGridData.append("['");
//			sbGridData.append(sYearMonth );
//			sbGridData.append("','");
//			sbGridData.append(tapeWaferObj.getProjName() );
//			sbGridData.append("','");
//			sbGridData.append(transParameterNull(sVersion) );
//			sbGridData.append("','");
			
				
			//2. from project, 用proj_name找出wafer_inch, prod_family, prod_line
			projQueryTo = new ProjectQueryTo();
			projQueryTo.setProjName(sProjName);
			//若有傳入family, line, 要加條件
			projQueryTo.setProdFamily(queryTo.getProdFamily());
			projQueryTo.setProdLine(queryTo.getProdLine());
projectListTo = tapeOutDetailDao.queryTapeOutProject(projQueryTo);
			if(projectListTo.size()>0)
			{
				
				sbGridData.append("['");
				sbGridData.append(sYearMonth );
				sbGridData.append("','");
				sbGridData.append(tapeWaferObj.getProjName() );
				sbGridData.append("','");
				sbGridData.append(transParameterNull(sVersion) );
				sbGridData.append("','");
				
				ProjectTo projectToObj = projectListTo.get(0);
				sTapeOutDate = tapeWaferObj.getTapeOutDate();
				sDiffMaskset = "";
				//2.1 用project name, 找到之前的版本, 比對mask layercom
				masksetQueryTo.setProjCodeWVersion(sProjCodeWVer);
				masksetQueryTo.setProjName(sProjName);
				masksetQueryTo.setTapeOutDate(sTapeOutDate);
				tapeOutMasksetListTo = tapeOutDetailDao.queryTapeOutBeforeMaskset(masksetQueryTo);
				if(sCurrMaskset != null)
				{
					sDiffMaskset = getDiffentMaskset(sCurrMaskset, tapeOutMasksetListTo);
				}
				else
				{
					sDiffMaskset = "";
				}
				
				sbGridData.append(sDiffMaskset);
				sbGridData.append("','");
				//ic family
				sbGridData.append(projectToObj.getProdLine());
				sbGridData.append("-");
				sbGridData.append(transParameterNull(sapProdFamilyDao.findDescByProdFamily(projectToObj.getProdFamily())) );
				sbGridData.append("','");
				sbGridData.append(transParameterNull(projectToObj.getWaferInch()) );  //wafer inch
				sbGridData.append("','");
				sbGridData.append(sTapeOutDate );
				sbGridData.append("','");
				sbGridData.append(transParameterNull(tapeWaferObj.getRevisionItem()) );				
				sbGridData.append("',''],");
				++iTapeoutCount;
			}
			else
			{
				//sbGridData.append("','','','','',''], ");
			}
			//最後一筆total
			if(iCount ==tapeOutWaferListTo.size())
			{
				sbGridData.append("['");
				sbGridData.append(sKeepYearMonth );
				sbGridData.append("','','','','','','','','");
				sbGridData.append(Integer.toString(iTapeoutCount-1) );
				sbGridData.append("'],");
			}
			
			sKeepYearMonth = sYearMonth;
			//++iTapeoutCount;
			++iCount;
		}
		
		//去,  完整資料
		if(tapeOutWaferListTo.size()>0)
		{
			sbGridData.setLength(sbGridData.length()-1);
		}
		
		//選單資料
		request.setAttribute("reqYearFrom", sQueryTapeOutYearFrom);
		request.setAttribute("reqMonthFrom", sQueryTapeOutMonthFrom);
		request.setAttribute("reqYearTo", sQueryTapeOutYearTo);
		request.setAttribute("reqMonthTo", sQueryTapeOutMonthTo);
		request.setAttribute("reqProdFamily", sQueryProdFamilyName);
		request.setAttribute("reqProdLine", sQueryProdLine);
		//回傳資料
		request.setAttribute("gridData", sbGridData.toString());

		return mapping.findForward(forward);
	}
	
	private String getDiffentMaskset(String currMaskset,List<RptTapeOutMasksetListTo> beforeMasksetList )
	{
		String sBeforeMaskset = "";
		StringBuffer sbMaskset;
		
		//init
		Hashtable<String, String> beforeMasksetHt;
		sbMaskset = new StringBuffer();
		
		beforeMasksetHt = new Hashtable<String, String>();
		//將舊的資料放入hashtable中
		for(RptTapeOutMasksetListTo beforeMasksetObj:beforeMasksetList)
		{
			sBeforeMaskset = transParameterNull(beforeMasksetObj.getMaskLayerCom());
			String[] arBeforeMaskset = sBeforeMaskset.split("/");	
			for(String masksetObj:arBeforeMaskset)
			{
				//去除空白
				masksetObj = masksetObj.trim();
				beforeMasksetHt.put(masksetObj,masksetObj) ;
			}			
		}
		
		//用本次版本與之前的比對, 若無則顯示
		String[] arCurrMaskset = currMaskset.split("/");	
		for(String currMasksetObj:arCurrMaskset)
		{
			//去除空白
			currMasksetObj = currMasksetObj.trim();
			//用此版當key取舊的hashtable
			sBeforeMaskset = beforeMasksetHt.get(currMasksetObj);
			if(sBeforeMaskset==null)
			{
				sbMaskset.append(currMasksetObj);
				sbMaskset.append("/");
			}
		}
		if(sbMaskset.length()>0)
		{
			sbMaskset.setLength(sbMaskset.length()-1);
		}

		return sbMaskset.toString();
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
