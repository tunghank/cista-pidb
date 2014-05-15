package com.cista.pidb.ajax;


import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cista.pidb.md.dao.WloDao;
import com.cista.pidb.md.to.WloTo;

public class GetWloMaterialNum extends Action {
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

        String material = request.getParameter("material");
        material =null!=material?material:"";
        
        WloDao wloDao = new WloDao();
        WloTo wloTo =wloDao.findByPrimaryKey(material);
        StringBuilder sb = new StringBuilder();
        if (wloTo != null) {
            
                sb.append("|").append(wloTo.getProdCode());
                sb.append("|").append(wloTo.getProjCode());
                sb.append("|").append(wloTo.getProdName());
                sb.append("|").append(wloTo.getSerialNum());
                sb.append("|").append(wloTo.getVariant());
                sb.append("|").append(wloTo.getReserved());
        }
        
        PrintWriter out = response.getWriter();
        out.print(sb.length() > 0 ? sb.substring(1) : "");

        return null;
    }
}
