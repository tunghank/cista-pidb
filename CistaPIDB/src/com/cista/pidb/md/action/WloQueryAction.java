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
import com.cista.pidb.md.dao.WloDao;
import com.cista.pidb.md.to.WloQueryTo;
import com.cista.pidb.md.to.WloTo;

public class WloQueryAction extends DispatchAction{
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
		
		List<FunctionParameterTo> wloVersionList = new ArrayList<FunctionParameterTo>();
		wloVersionList.addAll(tsvVersionList);
		wloVersionList.addAll(cspVersionList);
		
		fundName = "WLO";
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
		
		request.setAttribute("wloVersionList", wloVersionList);
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
           WloDao wloDao = new WloDao();
           WloQueryTo queryTo = (WloQueryTo) HttpHelper.pickupForm(WloQueryTo.class, request, true);

	
           queryTo.setTotalResult(wloDao.countResult(queryTo));
           request.setAttribute("result", wloDao.query(queryTo));
           request.setAttribute("criteria", queryTo);
           return mapping.findForward(forward);
       }

	
       public ActionForward paging(final ActionMapping mapping,
               final ActionForm form, final HttpServletRequest request,
               final HttpServletResponse response) {
           String forward = "paging_success";
           WloDao wloDao = new WloDao();
           WloQueryTo queryTo = (WloQueryTo) HttpHelper.pickupForm(WloQueryTo.class, request, true);         
           request.setAttribute("result", wloDao.query(queryTo));
           request.setAttribute("criteria", queryTo);
           return mapping.findForward(forward);
       }

       public ActionForward download(final ActionMapping mapping,
               final ActionForm form, final HttpServletRequest request,
               final HttpServletResponse response) {
    	   WloDao wloDao = new WloDao();
    	   WloQueryTo queryTo = (WloQueryTo) HttpHelper.pickupForm(WloQueryTo.class, request, true);
           queryTo.setPageNo(-1);
           List<WloTo> result = wloDao.query(queryTo);
           request.setAttribute("reportTitle", PIDBContext.getConfig("MD_32_TITLE"));
           request.setAttribute("reportContent", result);
           request.setAttribute("reportColumn", PIDBContext.getConfig("MD_32_SEQ"));
           return mapping.findForward("report");
       }

}
