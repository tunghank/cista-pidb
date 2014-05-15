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
import com.cista.pidb.md.dao.TradPkgDao;
import com.cista.pidb.md.to.TradPkgQueryTo;
import com.cista.pidb.md.to.TradPkgTo;

public class TradPkgQueryAction extends DispatchAction {

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
           TradPkgDao tradPkgDao = new TradPkgDao();
           TradPkgQueryTo queryTo = (TradPkgQueryTo) HttpHelper.pickupForm(TradPkgQueryTo.class, request, true);

           queryTo.setTotalResult(tradPkgDao.countResult(queryTo));
           request.setAttribute("result", tradPkgDao.query(queryTo));
           request.setAttribute("criteria", queryTo);
           return mapping.findForward(forward);
       }

       public ActionForward paging(final ActionMapping mapping,
               final ActionForm form, final HttpServletRequest request,
               final HttpServletResponse response) {
           String forward = "paging_success";
           TradPkgDao tradPkgDao = new TradPkgDao();
           TradPkgQueryTo queryTo = (TradPkgQueryTo) HttpHelper.pickupForm(TradPkgQueryTo.class, request, true);         
           request.setAttribute("result", tradPkgDao.query(queryTo));
           request.setAttribute("criteria", queryTo);
           return mapping.findForward(forward);
       }

       public ActionForward download(final ActionMapping mapping,
               final ActionForm form, final HttpServletRequest request,
               final HttpServletResponse response) {
           TradPkgDao tradPkgDao = new TradPkgDao();
           TradPkgQueryTo queryTo = (TradPkgQueryTo) HttpHelper.pickupForm(TradPkgQueryTo.class, request, true);
           queryTo.setPageNo(-1);
           List<TradPkgTo> result = tradPkgDao.query(queryTo);
           request.setAttribute("reportTitle", PIDBContext.getConfig("MD_12_TITLE"));
           request.setAttribute("reportContent", result);
           request.setAttribute("reportColumn", PIDBContext.getConfig("MD_12_SEQ"));
           return mapping.findForward("report");
       }
}
