/*
 * 2010.03.10/FCG1 @Jere Huang - Initial Version.
 * 2010.04.21/FCG2 @Jere Huang - 新增NTO date
 */
package com.cista.pidb.md.action;

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
import com.cista.pidb.md.dao.RptProjCodeHitRateDao;
import com.cista.pidb.md.to.RptProjCodeHitRateListTo;
import com.cista.pidb.md.to.RptProjCodeHitRateQueryTo;

public class RptProjCodeHitRateAction extends DispatchAction
{
	String forward;
	protected final Log logger = LogFactory.getLog(getClass());
	
	
	public ActionForward query(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response)
	{
		forward = "result";
		
		int 							 iToCount;	
		int								 iRowCout;
		boolean							 bMPFlag;
		String							 sMP;
		String							 sOPTION_NTO;
		String						     sGetProjCode;
		String							 sGetRevisionItem;
		String                           sKeepProjCode;
		String						     sKeepProdLine;
		String							 sKeepProdName;
		String							 sKeepProdFamily;
		String							 sMpStatus;
		String							 sQueryProdFamilyName;
		String							 sQueryProdFamilyCode;
		String							 sKeepTapeOutDate; //FCG2
		String 							 sGetTapeOutDate;  //FCG2
		StringBuffer                     sbGridData;
		SapMasterProductFamilyDao        prodFamilyDao;
		RptProjCodeHitRateQueryTo  		 projCodeHitRateQueryTo;
		RptProjCodeHitRateDao            projCodeHitRateDao;
		List<RptProjCodeHitRateListTo>   projCodeHitRateListTo;

		projCodeHitRateQueryTo= (RptProjCodeHitRateQueryTo) HttpHelper.pickupForm(RptProjCodeHitRateQueryTo.class, request, true);
		
		//0. init variable
		projCodeHitRateDao = new RptProjCodeHitRateDao();
		prodFamilyDao = new SapMasterProductFamilyDao();
		sbGridData = new StringBuffer();
		sKeepProjCode = "";
		sKeepProdLine = "";
		sKeepProdName = "";
		sKeepProdFamily = "";
		sKeepTapeOutDate = "";
		iToCount = 0;
		bMPFlag = false;
		sMP = "MP";
		sOPTION_NTO = "Option NTO";
		iRowCout = 1;
		sGetRevisionItem = "";
		sGetTapeOutDate = ""; //FCG2
		//轉換成數字
		sQueryProdFamilyName = projCodeHitRateQueryTo.getProdFamily();
		if(sQueryProdFamilyName != null)
		{
			sQueryProdFamilyCode = prodFamilyDao.findFamilyCodeByDesc(sQueryProdFamilyName);	
			projCodeHitRateQueryTo.setProdFamily(sQueryProdFamilyCode);
		}
		
		//1. 依條件取得ic wafer資料
		projCodeHitRateListTo = projCodeHitRateDao.queryProjCodeHitRate(projCodeHitRateQueryTo);
		for(RptProjCodeHitRateListTo hitRateObj:projCodeHitRateListTo)
		{
			sGetProjCode = transParameterNull(hitRateObj.getProjCode() );
			sMpStatus = transParameterNull(hitRateObj.getMpStatus() );
			sGetRevisionItem = transParameterNull(hitRateObj.getRevisionItem() );
			sGetTapeOutDate= transParameterNull(hitRateObj.getTapeOutDate() );
			
			//第一筆
			if(iRowCout ==1)
			{
				sKeepProjCode = sGetProjCode;
			}
			
			
			//1.1不相同, 則將count and mp, other data顯示
			if(!sGetProjCode.equals(sKeepProjCode))
			{
				sbGridData.append("['");
				sbGridData.append(sKeepProdLine);
				sbGridData.append("','");
				sbGridData.append(sKeepProdName);
				sbGridData.append("','");
				sbGridData.append(sKeepProjCode);
				sbGridData.append("','");
				sbGridData.append(Integer.toString(iToCount) );
				sbGridData.append("','");
				sbGridData.append(bMPFlag?"YES":"NO" );
				sbGridData.append("','");
				sbGridData.append(sKeepTapeOutDate);
				sbGridData.append("','");
				sbGridData.append(sKeepProdLine );
				sbGridData.append("-");
				sbGridData.append(sKeepProdFamily );
				sbGridData.append("'],");
				
				//1.2 初始
				iToCount = 0;
				bMPFlag = false;
				sKeepTapeOutDate = "";
			}	
			
			//判斷tape out資料
			if(sKeepTapeOutDate.trim().length()==0  && sGetTapeOutDate != null)
			{
				sKeepTapeOutDate = sGetTapeOutDate;
			}
			else if(sGetTapeOutDate != null && sGetTapeOutDate.length()>5)
			{
				//判斷日期大小
				int iGetDate=Integer.parseInt(sGetTapeOutDate.replace("/", "") );
				int iKeepDate=Integer.parseInt(sKeepTapeOutDate.replace("/", "") );
				
				if(iGetDate < iKeepDate)
				{
					sKeepTapeOutDate = sGetTapeOutDate;
				}
			}
			
			//若是option NTO, 次數要-1
			/*
			if(sOPTION_NTO.equals(sGetRevisionItem) )
			{
				--iToCount;
			}
			*/
			
			if(sMP.equals(sMpStatus) )
			{
				bMPFlag = true;
			}			
			
			++iToCount;
			
			//最後一筆total
			if(iRowCout==projCodeHitRateListTo.size())
			{
				sbGridData.append("['");
				sbGridData.append(hitRateObj.getProdLine());
				sbGridData.append("','");
				sbGridData.append(hitRateObj.getProjName());
				sbGridData.append("','");
				sbGridData.append(sGetProjCode);
				sbGridData.append("','");
				sbGridData.append(Integer.toString(iToCount) );
				sbGridData.append("','");
				sbGridData.append(bMPFlag?"YES":"NO" );
				sbGridData.append("','");
				sbGridData.append(sKeepTapeOutDate);
				sbGridData.append("','");
				sbGridData.append(hitRateObj.getProdLine() );
				sbGridData.append("-");
				sbGridData.append(hitRateObj.getProdFamily() );
				sbGridData.append("'],");
			}
			
			++iRowCout;
			
			sKeepProjCode = sGetProjCode;	
			sKeepProdLine = hitRateObj.getProdLine();
			sKeepProdName = hitRateObj.getProjName();
			sKeepProdFamily = hitRateObj.getProdFamily();
		}
		//去,  完整資料
		if(projCodeHitRateListTo.size()>0)
		{
			sbGridData.setLength(sbGridData.length()-1);
		}
		
		//選單資料
		request.setAttribute("reqProdFamily", sQueryProdFamilyName);
		request.setAttribute("reqProjName", projCodeHitRateQueryTo.getProjName());
		request.setAttribute("reqProjCode", projCodeHitRateQueryTo.getProjCode());
		request.setAttribute("reqProdLine", projCodeHitRateQueryTo.getProdLine());
		
		//回傳資料
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
