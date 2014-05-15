package com.cista.pidb.ajax;


import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cista.pidb.md.dao.IcTapeDao;
import com.cista.pidb.md.to.IcTapeTo;

public class FetchIcTapeMaterialNum extends Action {
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

        String tapeName = request.getParameter("tapeName");
        
        IcTapeDao icTapeDao = new IcTapeDao();
        List<IcTapeTo> icTapeList = icTapeDao.findByTapeName(tapeName);
        StringBuilder sb = new StringBuilder();
        if (icTapeList != null) {
            for (IcTapeTo to : icTapeList) {
                sb.append("|").append(to.getMaterialNum());
            }
        }
        
        PrintWriter out = response.getWriter();
        out.print(sb.length() > 0 ? sb.substring(1) : "");

        return null;
    }
}
