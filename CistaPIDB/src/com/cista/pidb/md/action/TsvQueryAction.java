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
import com.cista.pidb.md.dao.TsvDao;
import com.cista.pidb.md.to.TsvQueryTo;
import com.cista.pidb.md.to.TsvTo;

public class TsvQueryAction extends DispatchAction{
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
           TsvDao tsvDao = new TsvDao();
           TsvQueryTo queryTo = (TsvQueryTo) HttpHelper.pickupForm(TsvQueryTo.class, request, true);

           queryTo.setTotalResult(tsvDao.countResult(queryTo));
           request.setAttribute("result", tsvDao.query(queryTo));
           request.setAttribute("criteria", queryTo);
           return mapping.findForward(forward);
       }

	
       public ActionForward paging(final ActionMapping mapping,
               final ActionForm form, final HttpServletRequest request,
               final HttpServletResponse response) {
           String forward = "paging_success";
           TsvDao tsvDao = new TsvDao();
           TsvQueryTo queryTo = (TsvQueryTo) HttpHelper.pickupForm(TsvQueryTo.class, request, true);         
           request.setAttribute("result", tsvDao.query(queryTo));
           request.setAttribute("criteria", queryTo);
           return mapping.findForward(forward);
       }

       public ActionForward download(final ActionMapping mapping,
               final ActionForm form, final HttpServletRequest request,
               final HttpServletResponse response) {
    	   TsvDao tsvDao = new TsvDao();
    	   TsvQueryTo queryTo = (TsvQueryTo) HttpHelper.pickupForm(TsvQueryTo.class, request, true);
           queryTo.setPageNo(-1);
           List<TsvTo> result = tsvDao.query(queryTo);
           request.setAttribute("reportTitle", PIDBContext.getConfig("MD_30_TITLE"));
           request.setAttribute("reportContent", result);
           request.setAttribute("reportColumn", PIDBContext.getConfig("MD_30_SEQ"));
           return mapping.findForward("report");
       }

}
