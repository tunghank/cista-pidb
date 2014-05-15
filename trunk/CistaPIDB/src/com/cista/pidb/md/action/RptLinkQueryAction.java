package com.cista.pidb.md.action;
/*------- Header---------------------------------
* 2010.01.11/FCG1 @Jere Huang - Initial Version.
*
*/

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.cista.pidb.code.dao.FabCodeDao;
import com.cista.pidb.core.HttpHelper;
import com.cista.pidb.core.PIDBContext;
import com.cista.pidb.md.dao.RptDao;
import com.cista.pidb.md.to.RptLinkQueryTo;
import com.cista.pidb.md.to.RptLinkTo;


public class RptLinkQueryAction extends DispatchAction 
{
	String forward;
	
	public ActionForward pre(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) 
	{
		forward = "success";
		//init combo value
		FabCodeDao fabCodeDao = new FabCodeDao();
		request.setAttribute("fabCodeList", fabCodeDao.findAll());
					
		return mapping.findForward(forward);
	}
	
	
	public ActionForward query(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) 
	{
		forward = "result";
		
		RptDao rptDao = new RptDao();
		RptLinkQueryTo queryTo = (RptLinkQueryTo) HttpHelper.pickupForm(RptLinkQueryTo.class, request, true);
		
		List<RptLinkTo> result=rptDao.query(queryTo);
		
		request.setAttribute("result", result);
		request.setAttribute("queryTo", queryTo);
		
		return mapping.findForward(forward);
	}
	
	
	public ActionForward paging(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response)
	{
		forward = "result";
		
		RptDao rptDao = new RptDao();
		RptLinkQueryTo queryTo = (RptLinkQueryTo) HttpHelper.pickupForm(RptLinkQueryTo.class, request, true);
		
		List<RptLinkTo> result=rptDao.query(queryTo);
		request.setAttribute("result", result);
		request.setAttribute("queryTo", queryTo);

		return mapping.findForward(forward);
	}
	
	
	public ActionForward download(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) 
	{
		forward = "report";
		RptDao rptDao = new RptDao();
		RptLinkQueryTo queryTo = (RptLinkQueryTo) HttpHelper.pickupForm(RptLinkQueryTo.class, request, true);
		
		queryTo.setPageNo(-1);
		
		List<RptLinkTo> result = rptDao.query(queryTo);		
		request.setAttribute("reportTitle", PIDBContext.getConfig("MD_29_TITLE"));
		request.setAttribute("reportContent", result);
		request.setAttribute("reportColumn", PIDBContext.getConfig("MD_29_SEQ"));
		
		return mapping.findForward(forward);
	}
	
}
