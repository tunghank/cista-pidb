package com.cista.pidb.md.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.cista.pidb.core.HttpHelper;
import com.cista.pidb.core.PIDBContext;
import com.cista.pidb.md.dao.CspDao;
import com.cista.pidb.md.to.CspQueryTo;
import com.cista.pidb.md.to.CspTo;

public class CspQueryAction extends DispatchAction{
	public ActionForward pre(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
		String forward = "pre_success";
		return mapping.findForward(forward);
	}
	
	public ActionForward query(final ActionMapping mapping,
               final ActionForm form, final HttpServletRequest request,
               final HttpServletResponse response) {
           String forward = "query_success";
           CspDao cspDao = new CspDao();
           CspQueryTo queryTo = (CspQueryTo) HttpHelper.pickupForm(CspQueryTo.class, request, true);

           queryTo.setTotalResult(cspDao.countResult(queryTo));
           request.setAttribute("result", cspDao.query(queryTo));
           request.setAttribute("criteria", queryTo);
           return mapping.findForward(forward);
       }

       public ActionForward paging(final ActionMapping mapping,
               final ActionForm form, final HttpServletRequest request,
               final HttpServletResponse response) {
           String forward = "paging_success";
           CspDao cspDao = new CspDao();
           CspQueryTo queryTo = (CspQueryTo) HttpHelper.pickupForm(CspQueryTo.class, request, true);         
           request.setAttribute("result", cspDao.query(queryTo));
           request.setAttribute("criteria", queryTo);
           return mapping.findForward(forward);
       }

       public ActionForward download(final ActionMapping mapping,
               final ActionForm form, final HttpServletRequest request,
               final HttpServletResponse response) {
    	   CspDao cspDao = new CspDao();
    	   CspQueryTo queryTo = (CspQueryTo) HttpHelper.pickupForm(CspQueryTo.class, request, true);
           queryTo.setPageNo(-1);
           List<CspTo> result = cspDao.query(queryTo);
           request.setAttribute("reportTitle", PIDBContext.getConfig("MD_28_TITLE"));
           request.setAttribute("reportContent", result);
           request.setAttribute("reportColumn", PIDBContext.getConfig("MD_28_SEQ"));
           return mapping.findForward("report");
       }

}
