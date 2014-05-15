package com.cista.pidb.md.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.cista.pidb.core.HttpHelper;
import com.cista.pidb.md.dao.MaskLayerMappingDao;
import com.cista.pidb.md.dao.ProjectDao;
import com.cista.pidb.md.dao.RptMasksetDao;
import com.cista.pidb.md.to.IcWaferTo;
import com.cista.pidb.md.to.MaskLayerMappingTo;
import com.cista.pidb.md.to.RptMasksetQueryTo;

public class RptMasksetQueryAction extends DispatchAction  
{
	String forward;
	protected final Log logger = LogFactory.getLog(getClass());
	 
	public ActionForward pre(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) 
	{
		String forward = "success";
        ProjectDao projectDao = new ProjectDao();
        request.setAttribute("projNameList", projectDao.findProjName());
        return mapping.findForward(forward);
	}
	
	public ActionForward query(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) 
	{
		forward = "result";
		//Hashtable<String,String> maskNoHt;
		LinkedHashMap<String, String>  maskNoLinkMap;
		Hashtable<String,String> projVersionLayerHt;
		ArrayList<String>   columnArrayList;
		String sMaskLayerCom;
		String sMasksetName;
		String sMasksetNum;
		String sQueryProjName;
		String sProjCodeWVer;
		String sProjVer;
		
		
		RptMasksetDao masksetDao = new RptMasksetDao();
		RptMasksetQueryTo queryTo = (RptMasksetQueryTo) HttpHelper.pickupForm(RptMasksetQueryTo.class, request, true);
		sQueryProjName = queryTo.getProjName();
		//1. 1取的MASK_LAYER_COM -> column
		List<IcWaferTo> icWaferResult = masksetDao.queryMasksetLayer(queryTo);
		
		//取的layer name mapping
		MaskLayerMappingDao maskMappingDao = new MaskLayerMappingDao();
		List<MaskLayerMappingTo> maskMappingList =maskMappingDao.findAll();
		
		//將layercom取出每一個值, 放到hashtable中
		//maskNoHt = new Hashtable<String,String>();
		maskNoLinkMap = new LinkedHashMap<String, String>();
		
		projVersionLayerHt = new Hashtable<String,String>();
		columnArrayList = new ArrayList<String>();
		
		//固定表頭欄位
		columnArrayList.add("maskLayer");
		columnArrayList.add("maskNo");
		
		for(IcWaferTo obj:icWaferResult)
		{
			sMaskLayerCom = obj.getMaskLayerCom();
			//1.2 放PROJ_CODE_W_VERSION與MASK_LAYER_COM 關係  [A-01A] [192A/191A/160A_B]
			sProjCodeWVer = obj.getProjCodeWVersion();
			sProjVer = sProjCodeWVer.replaceAll(sQueryProjName, "");
			if(sMaskLayerCom==null)
			{
				sMaskLayerCom = "";
			}
			projVersionLayerHt.put(sProjVer, sMaskLayerCom);
			columnArrayList.add(sProjVer);	
			String[] arMaskLayerCom = sMaskLayerCom.split("/");			
			for(String layerObj:arMaskLayerCom)
			{
				logger.debug("3333333333333=" + layerObj);
				sMasksetName = "";
				//去除空白
				layerObj = layerObj.trim();
				//取前3碼
				if("".equals(layerObj) && arMaskLayerCom.length==1 )
				{
					MaskLayerMappingTo maskLayerTo = new MaskLayerMappingTo();
					maskLayerTo.setMaskNum("999");
					maskLayerTo.setMaskLayer("ERROR");
					maskMappingList.add(maskLayerTo);	
					layerObj = "ERROR";
				}
				else
				{
					layerObj = layerObj.substring(0,3);
				}
				
				//比對mapping table取出Name
				for(MaskLayerMappingTo mappingObj :maskMappingList)
				{
					sMasksetNum = mappingObj.getMaskNum();
					if(layerObj.indexOf(sMasksetNum)>=0)
					{
						sMasksetName = mappingObj.getMaskLayer();					
						break;
					}
				}				
				//key = mask_no  value = mask_name   [192] [NW]
				if(sMasksetName==null)
				{
					sMasksetName = "";
				}
				//maskNoHt.put(layerObj, sMasksetName);	
				maskNoLinkMap.put(layerObj, sMasksetName);				
			}		
		}
		//column structe
		StringBuffer sbColumn = new StringBuffer();
		StringBuffer sbColReader = new StringBuffer();
		String       sColumnStruct = "";
		String       sColumnReader = "";
		String       sLayerId      = "";
			
		for(String colObj:columnArrayList)
		{
			//grid column
			sbColumn.append("{header: '");
			sbColumn.append(colObj.trim());
			sbColumn.append("', id: '");
			sbColumn.append(colObj.trim());
			sbColumn.append("', dataIndex: '");
			sbColumn.append(colObj.trim());
			if("maskLayer".equals(colObj.trim()) || "maskNo".equals(colObj.trim()) )
			{
				sbColumn.append("', width: 100, sortable: true}  ");
			}
			else
			{
				sbColumn.append("', width: 60, sortable: true}  ");
			}
			sbColumn.append(",");	
			
			//proxy reader
			sbColReader.append("{name:'");
			sbColReader.append(colObj.trim());
			sbColReader.append("'} ");
			sbColReader.append(",");	
		}
		if(columnArrayList.size()>0)
		{
			sColumnStruct = sbColumn.substring(0, sbColumn.length()-1);
			sColumnReader = sbColReader.substring(0, sbColReader.length()-1);
		}
		//grid data
		StringBuffer sbGridData = new StringBuffer();
		Object oMasksetNo;
		String sMasksetNo = "";
		String sMasksetVer = "";
		String sGridData= "";
		sMasksetName = "";
		//用key值當一筆資料
		//for (Enumeration<String> e = maskNoHt.keys() ; e.hasMoreElements() ;) 
		Iterator masksetIt = maskNoLinkMap.keySet().iterator();
		while (masksetIt.hasNext())
	    {
			oMasksetNo= masksetIt.next();
			sMasksetNo = oMasksetNo.toString();
			//sMasksetName = maskNoHt.get(oMasksetNo);		
			sMasksetName = maskNoLinkMap.get(oMasksetNo);
			sbGridData.append("['");
	
			sbGridData.append(sMasksetName);
			sbGridData.append("','");
			sbGridData.append(sMasksetNo);
			sbGridData.append("',");

			for(int i=2; i <columnArrayList.size();i++)
			{
				String sColumnName=  columnArrayList.get(i);			
				sMaskLayerCom = projVersionLayerHt.get(sColumnName);			
				//用每個欄位取出 mask layer com資料, 在split後 比對此筆資料的名稱, 一致則秀英文字
				String[] arMaskLayerCom = sMaskLayerCom.split("/");	
				for(String layerObj:arMaskLayerCom)
				{
					sMasksetName = "";
					//去除空白
					layerObj = layerObj.trim();
					if("".equals(layerObj) && arMaskLayerCom.length==1 )
					{
						sbGridData.append("'',");
						continue;
					}
					//取前3碼
					sLayerId = layerObj.substring(0,3);				
					//取尾碼
					sMasksetVer = layerObj.substring(3);
					if(sMasksetNo.equals(sLayerId))
					{
						sbGridData.append("'");
						sbGridData.append(sMasksetVer);
						sbGridData.append("',");						
						break;
					}
				}
			}
			//去,
			sbGridData.setLength(sbGridData.length()-1);
			sbGridData.append("],");
	    }
		
		//去,
		//if(maskNoHt.size()>0)
		if(maskNoLinkMap.size()>0)
		{
			sGridData= sbGridData.substring(0, sbGridData.length()-1);
		}
		
		//選單資料
		ProjectDao projectDao = new ProjectDao();
	    request.setAttribute("projNameList", projectDao.findProjName());
		request.setAttribute("reqProjName", sQueryProjName);
	        
		//回傳資料
		request.setAttribute("gridColumn", sColumnStruct);
		request.setAttribute("gridColumnReader", sColumnReader);
		request.setAttribute("gridData", sGridData);
		
		return mapping.findForward(forward);
	}
	
}
