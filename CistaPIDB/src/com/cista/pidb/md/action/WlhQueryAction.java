package com.cista.pidb.md.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.cista.pidb.code.dao.FunctionParameterDao;
import com.cista.pidb.code.to.FunctionParameterTo;
import com.cista.pidb.core.HttpHelper;
import com.cista.pidb.core.PIDBContext;
import com.cista.pidb.md.dao.WlhDao;
import com.cista.pidb.md.to.WlhQueryTo;
import com.cista.pidb.md.to.WlhTo;

public class WlhQueryAction extends DispatchAction{
	public ActionForward pre(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
		
		String forward = "pre_success";
		
   		String fundName = "TSV";
		String funFieldName = "VERSION";
		List<FunctionParameterTo> tsvVersionList = new FunctionParameterDao()
		.findValueList(fundName, funFieldName);
		
		fundName = "CSP";
		funFieldName = "VERSION";
		List<FunctionParameterTo> cspVersionList = new FunctionParameterDao()
		.findValueList(fundName, funFieldName);
		
		List<FunctionParameterTo> wlhVersionList = new ArrayList<FunctionParameterTo>();
		wlhVersionList.addAll(tsvVersionList);
		wlhVersionList.addAll(cspVersionList);
		
		fundName = "WLH";
		funFieldName = "LENS";
		List<FunctionParameterTo> lensList = new FunctionParameterDao()
		.findValueList(fundName, funFieldName);

		funFieldName = "PERSPECTIVE";
		List<FunctionParameterTo> perspectiveList = new FunctionParameterDao()
		.findValueList(fundName, funFieldName);
		
		funFieldName = "FNO";
		List<FunctionParameterTo> fnoList = new FunctionParameterDao()
		.findValueList(fundName, funFieldName);
		
		funFieldName = "IR_COATING";
		List<FunctionParameterTo> irCoatingList = new FunctionParameterDao()
		.findValueList(fundName, funFieldName);
		
		funFieldName = "GOL_COATING";
		List<FunctionParameterTo> golCoatingList = new FunctionParameterDao()
		.findValueList(fundName, funFieldName);
		
		funFieldName = "PROD_TYPE";
		List<FunctionParameterTo> prodTypeList = new FunctionParameterDao()
		.findValueList(fundName, funFieldName);
		
		funFieldName = "VENDOR_CODE";
		List<FunctionParameterTo> vendorCodeList = new FunctionParameterDao()
		.findValueList(fundName, funFieldName);
		
		funFieldName = "SERIAL_NUM";
		List<FunctionParameterTo> serialNumList = new FunctionParameterDao()
		.findValueList(fundName, funFieldName);
		
		request.setAttribute("wlhVersionList", wlhVersionList);
		request.setAttribute("lensList", lensList);
		request.setAttribute("perspectiveList", perspectiveList);
		request.setAttribute("fnoList", fnoList);
		request.setAttribute("irCoatingList", irCoatingList);
		request.setAttribute("golCoatingList", golCoatingList);
		request.setAttribute("prodTypeList", prodTypeList);
		request.setAttribute("vendorCodeList", vendorCodeList);
		request.setAttribute("serialNumList", serialNumList);
		
		return mapping.findForward(forward);
	}
	
	public ActionForward query(final ActionMapping mapping,
               final ActionForm form, final HttpServletRequest request,
               final HttpServletResponse response) {
           String forward = "query_success";
           WlhDao wlhDao = new WlhDao();
           WlhQueryTo queryTo = (WlhQueryTo) HttpHelper.pickupForm(WlhQueryTo.class, request, true);

	
           queryTo.setTotalResult(wlhDao.countResult(queryTo));
           request.setAttribute("result", wlhDao.query(queryTo));
           request.setAttribute("criteria", queryTo);
           return mapping.findForward(forward);
       }

	
       public ActionForward paging(final ActionMapping mapping,
               final ActionForm form, final HttpServletRequest request,
               final HttpServletResponse response) {
           String forward = "paging_success";
           WlhDao wlhDao = new WlhDao();
           WlhQueryTo queryTo = (WlhQueryTo) HttpHelper.pickupForm(WlhQueryTo.class, request, true);         
           request.setAttribute("result", wlhDao.query(queryTo));
           request.setAttribute("criteria", queryTo);
           return mapping.findForward(forward);
       }

       public ActionForward download(final ActionMapping mapping,
               final ActionForm form, final HttpServletRequest request,
               final HttpServletResponse response) {
    	   WlhDao wlhDao = new WlhDao();
    	   WlhQueryTo queryTo = (WlhQueryTo) HttpHelper.pickupForm(WlhQueryTo.class, request, true);
           queryTo.setPageNo(-1);
           List<WlhTo> result = wlhDao.query(queryTo);
           request.setAttribute("reportTitle", PIDBContext.getConfig("MD_33_TITLE"));
           request.setAttribute("reportContent", result);
           request.setAttribute("reportColumn", PIDBContext.getConfig("MD_33_SEQ"));
           return mapping.findForward("report");
       }

}
