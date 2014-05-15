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
import com.cista.pidb.md.dao.MaskLayerMappingDao;
import com.cista.pidb.md.to.MaskLayerMappingQueryTo;
import com.cista.pidb.md.to.MaskLayerMappingTo;

public class MaskLayerMappingQueryAction extends DispatchAction {
    public ActionForward pre(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        String forward = "success";
        return mapping.findForward(forward);
    }

    public ActionForward query(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        String forward = "result";
        MaskLayerMappingDao maskLayerMappingDao = new MaskLayerMappingDao();
        MaskLayerMappingQueryTo queryTo = (MaskLayerMappingQueryTo) HttpHelper.pickupForm(MaskLayerMappingQueryTo.class, request, true);
        queryTo.setTotalResult(maskLayerMappingDao.countResult(queryTo));
        request.setAttribute("result", maskLayerMappingDao.query(queryTo));
        request.setAttribute("queryTo", queryTo);
        return mapping.findForward(forward);
    }

    public ActionForward paging(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        String forward = "result";
        MaskLayerMappingDao maskLayerMappingDao = new MaskLayerMappingDao();
        MaskLayerMappingQueryTo queryTo = (MaskLayerMappingQueryTo) HttpHelper.pickupForm(MaskLayerMappingQueryTo.class, request, true);
        queryTo.setTotalResult(maskLayerMappingDao.countResult(queryTo));
        request.setAttribute("result", maskLayerMappingDao.query(queryTo));
        request.setAttribute("queryTo", queryTo);
        return mapping.findForward(forward);
    }
    
    public ActionForward download(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        MaskLayerMappingDao maskLayerMappingDao = new MaskLayerMappingDao();
        MaskLayerMappingQueryTo queryTo = (MaskLayerMappingQueryTo) HttpHelper.pickupForm(MaskLayerMappingQueryTo.class, request, true);
        queryTo.setPageNo(-1);
        List<MaskLayerMappingTo> result = maskLayerMappingDao.query(queryTo);
        request.setAttribute("reportTitle", PIDBContext.getConfig("MD_16_TITLE"));
        request.setAttribute("reportContent", result);
        request.setAttribute("reportColumn", PIDBContext.getConfig("MD_16_SEQ"));
        return mapping.findForward("report");
    }    
}
