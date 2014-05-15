package com.cista.pidb.ajax;


import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cista.pidb.md.dao.IcCpDao;
import com.cista.pidb.md.to.IcCpTo;

public class FetchIcCpMaterialNum extends Action {
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
               
        IcCpDao icCpDao = new IcCpDao();
        List<IcCpTo> icCpList = icCpDao.findByProjWver(projWver);
        StringBuilder sb = new StringBuilder();
        if (icCpList != null) {
            for (IcCpTo to : icCpList) {
                sb.append("|").append(to.getMaterialNum());
            }
        }
        
        PrintWriter out = response.getWriter();
        out.print(sb.length() > 0 ? sb.substring(1) : "");

        return null;
    }
}

