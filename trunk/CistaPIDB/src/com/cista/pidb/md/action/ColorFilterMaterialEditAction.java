package com.cista.pidb.md.action;

import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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
import com.cista.pidb.md.to.ColorFilterMaterialTo;

public class ColorFilterMaterialEditAction extends DispatchAction{
	protected final Log logger = LogFactory.getLog(getClass());
	
	public ActionForward pre(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		String forward = "pre_success";
		
		String colorFilterMaterialNum= (String) request.getParameter("colorFilterMaterialNum");
		String cfMaterial = "";
        if (colorFilterMaterialNum == null || colorFilterMaterialNum.length() <= 0) {
        	cfMaterial = (String) request.getAttribute("cfMaterial");
        }else{
        	cfMaterial = colorFilterMaterialNum;
        }
		
		logger.debug("cfMaterial " + cfMaterial);
		
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
		
		ColorFilterMaterialTo cfTo =new ColorFilterMaterialDao().findByPrimaryKey(
				cfMaterial);
		request.setAttribute("ref", cfTo);
		
		return mapping.findForward(forward);
	}

    public ActionForward save(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
             String forward = "save_success";
             
             ColorFilterMaterialDao cfDao = new ColorFilterMaterialDao();
             ColorFilterMaterialTo cfTo = (ColorFilterMaterialTo) 
             	HttpHelper.pickupForm(ColorFilterMaterialTo.class, request);
             


    		/** *****End******** */
    		logger.debug("CFMaterialNum " +  cfTo.getColorFilterMaterialNum());
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
             
             
             //Modify by
    		 SimpleDateFormat df= new SimpleDateFormat("yyyyMMddHHmmss");
    		 
             UserTo loginUser = PIDBContext.getLoginUser(request);
             
             cfTo.setModifiedBy(loginUser.getUserId());
             cfTo.setUpdateDate(df.format(Calendar.getInstance().getTime()));
             cfDao.update(cfTo);
             
             //Get Create Data
             
             cfTo = cfDao.findByPrimaryKey(cfTo.getColorFilterMaterialNum());
             
             String mes = "Save Successfully";

             
             request.setAttribute("error", mes);        
             request.setAttribute("ref", cfTo);
             request.setAttribute("cfMaterial", cfTo.getColorFilterMaterialNum());
             return mapping.findForward(forward);
    }
}
