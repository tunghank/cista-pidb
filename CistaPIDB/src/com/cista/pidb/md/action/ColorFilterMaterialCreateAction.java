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

import com.cista.pidb.admin.to.UserTo;
import com.cista.pidb.code.dao.FunctionParameterDao;
import com.cista.pidb.code.to.FunctionParameterTo;
import com.cista.pidb.core.HttpHelper;
import com.cista.pidb.core.PIDBContext;
import com.cista.pidb.md.dao.ColorFilterMaterialDao;
import com.cista.pidb.md.dao.IcWaferDao;
import com.cista.pidb.md.to.ColorFilterMaterialTo;
import com.cista.pidb.md.to.IcWaferTo;

public class ColorFilterMaterialCreateAction extends DispatchAction{
	 protected final Log logger = LogFactory.getLog(getClass());
	
	 public ActionForward pre(final ActionMapping mapping,
	            final ActionForm form, final HttpServletRequest request,
	            final HttpServletResponse response) {
	            String forward = "pre_success";
	            if (request.getParameter("ref") != null) {
	            	ColorFilterMaterialDao cfDao = new ColorFilterMaterialDao();
		            //Create with reference
		            String materialNum = request.getParameter("ref");
		            ColorFilterMaterialTo cfTo = cfDao.findByPrimaryKey(materialNum);
		            request.setAttribute("ref", cfTo);
	            }
	            FunctionParameterDao paramDao = new FunctionParameterDao();
	    		String fundName = "CF";
	    		String funFieldName = "MASK_HOUSE";
	    		List<FunctionParameterTo> maskHouseList = paramDao.findValueList(fundName, funFieldName);
	    		funFieldName = "ML_TYPE";
	    		List<FunctionParameterTo> mlTypeList = paramDao.findValueList(fundName, funFieldName);
	    		funFieldName = "NEW_MASK_NUMBER";
	    		List<FunctionParameterTo> newMaskList = paramDao.findValueList(fundName, funFieldName);
	    		funFieldName = "RBG_THICKNESS";
	    		List<FunctionParameterTo> rbgThicknessList = paramDao.findValueList(fundName, funFieldName);
	    		funFieldName = "REVISION_ITEM";
	    		List<FunctionParameterTo> revisionItemList = paramDao.findValueList(fundName, funFieldName);
	    		funFieldName = "TAPE_OUT_TYPE";
	    		List<FunctionParameterTo> tapeOutTypeList = paramDao.findValueList(fundName, funFieldName);
	    		
	    		request.setAttribute("maskHouseList", maskHouseList);
	    		request.setAttribute("mlTypeList", mlTypeList);
	    		request.setAttribute("newMaskList", newMaskList);
	    		request.setAttribute("rbgThicknessList", rbgThicknessList);
	    		request.setAttribute("revisionItemList", revisionItemList);
	    		request.setAttribute("tapeOutTypeList", tapeOutTypeList);
	    		
	            return mapping.findForward(forward);
	    }

	     public ActionForward save(final ActionMapping mapping,
	            final ActionForm form, final HttpServletRequest request,
	            final HttpServletResponse response) {
	    	 
	             String forward = "save_success";
	             ColorFilterMaterialDao cfDao = new ColorFilterMaterialDao();
	             ColorFilterMaterialTo cfTo = (ColorFilterMaterialTo) 
	             	HttpHelper.pickupForm(ColorFilterMaterialTo.class, request);
	             

	    		ColorFilterMaterialTo cFMaterialTo = cfDao
	    				.getByProjCodeWVersionMaxVariant(cfTo.getProjectCodeWVersion());
	    		
	    		String maxVariantCFMaterialNum = "";
	    		String cFMaterialNum = "";
	    		String preVar = "";
	    		String realVar = "";
	    		// Create a new variant
	    		String maxVar = "";
	    		if (cFMaterialTo != null) {
	    			cFMaterialNum = cFMaterialTo.getColorFilterMaterialNum();
	    			maxVar = (String) cfDao.findMaxVar(cfTo.getProjectCodeWVersion());
	    			if (maxVar != null){
	    				preVar = maxVar.toString();
	    				if (preVar != null && !preVar.equals("")) 
	    				{
	    					char c = (char) ((int) preVar.charAt(0) + 1);

	    					if (c > 57 && c < 65) 
	    					{
	    						realVar = new String(new char[] { 65 });
	    					} 
	    					else if (c > 90) 
	    					{
	    						realVar = new String(new char[] { 48 });
	    					}
	    					else 
	    					{
	    						realVar = new String(new char[] { c });
	    					}
	    				}
	    			} else {
	    				preVar = "0";
	    				realVar = "0";
	    			}

	    			maxVariantCFMaterialNum = cFMaterialNum.substring(0, cFMaterialNum .length() - 1)
	    					+ realVar;
	    			cfTo.setColorFilterVariant(realVar);
	    		}else {
	    			//No Old CF Material
	    			IcWaferDao icWaferDao = new IcWaferDao();
	    			IcWaferTo icWaferTo = icWaferDao.findByProjCodeWVersion(cfTo.getProjectCodeWVersion());
	    			String icWafer = icWaferTo.getMaterialNum();
	    			
	    			cFMaterialTo = new  ColorFilterMaterialTo();
	    			realVar = "0";
	    			maxVariantCFMaterialNum = "M" + icWafer.substring(1, icWafer.length() - 1)
	    					+ realVar;
	    			cfTo.setColorFilterVariant(realVar);

	    		}
	    		/** *****End******** */
	    		logger.debug("CFMaterialNum " +  maxVariantCFMaterialNum);
	    		logger.debug("fetchMaskLayerCom " + cfTo.getFetchMaskLayerCom());
	    		
	    		// add multiple select data
	    		String[] revisionItemList = request.getParameterValues("revisionItem");
	    		String allRevisionItem = "";
	    		if (revisionItemList != null && revisionItemList.length > 0) {
	    			for (String revisionItem : revisionItemList) {
	    				allRevisionItem += "," + revisionItem;
	    			}
	    		}
	    		if (allRevisionItem != null && allRevisionItem.length() > 0) {
	    			cfTo.setRevisionItem(allRevisionItem.substring(1));
	    		}
	    		
	    		cfTo.setColorFilterMaterialNum(maxVariantCFMaterialNum);
	             
	             
	             //created by
	             UserTo loginUser = PIDBContext.getLoginUser(request);
	             
	             cfTo.setCreatedBy(loginUser.getUserId());
	             cfDao.insert(cfTo);
	             
	             //Get Create Data
	             
	             cfTo = cfDao.findByPrimaryKey(cfTo.getColorFilterMaterialNum());
	             String mes = "Save Successfully";

	             
	             request.setAttribute("error", mes);        
	             request.setAttribute("ref", cfTo);
	             request.setAttribute("cfMaterial", cfTo.getColorFilterMaterialNum());
	             return mapping.findForward(forward);
	     }

}
