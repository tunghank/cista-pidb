package com.cista.pidb.ajax;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cista.pidb.md.dao.IcFgDao;
import com.cista.pidb.md.to.IcFgTo;

public class FetchIcFg extends Action {
    /**
     * Do action performance.
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception exception
     */
    public ActionForward execute(
            final ActionMapping mapping,
            final ActionForm form,
            final HttpServletRequest request,
            final HttpServletResponse response)
        throws Exception {

        String icFgMaterialNum = request.getParameter("icFgMaterialNum");

        IcFgDao icFgDao = new IcFgDao();
        IcFgTo  icFgTo =icFgDao.findByMaterialNum(icFgMaterialNum);
        icFgTo.setRemark("");
        String xml = AjaxHelper.bean2XmlString(icFgTo);
        
        response.setContentType("text/xml");
        response.getWriter().write(xml == null ? "" : xml);

        return null;
    }
}
