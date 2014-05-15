package com.cista.pidb.ajax;


import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cista.pidb.md.dao.IcWaferDao;
import com.cista.pidb.md.to.IcWaferTo;

public class FetchIcWfMaterialNum extends Action {
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

        String projWver = request.getParameter("projWver");
        
        IcWaferDao icWaferDao = new IcWaferDao();
        List<IcWaferTo> icWfList = icWaferDao.findByProjWver(projWver);
        StringBuilder sb = new StringBuilder();
        if (icWfList != null) {
            for (IcWaferTo to : icWfList) {
                sb.append("|").append(to.getMaterialNum());
            }
        }
        
        PrintWriter out = response.getWriter();
        out.print(sb.length() > 0 ? sb.substring(1) : "");

        return null;
    }
}
