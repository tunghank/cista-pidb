package com.cista.pidb.ajax;


import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cista.pidb.md.dao.IcFgDao;
import com.cista.pidb.md.to.IcFgTo;

public class FetchIcFgMCP extends Action {
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
        IcFgTo icFgTo = icFgDao.findByMaterialNum(icFgMaterialNum);
        
        StringBuilder sb = new StringBuilder();

        
        if (icFgTo != null) {
        	String mcpDieQty = icFgTo.getMcpDieQty();
        	mcpDieQty = null != mcpDieQty ? mcpDieQty : "";
        	String mcpPkg  = icFgTo.getMcpPkg();
        	mcpPkg = null != mcpPkg ? mcpPkg : "";
        	String mcpProd1 =icFgTo.getMcpProd1();
        	mcpProd1 = null != mcpProd1 ? mcpProd1 : "";
        	String mcpProd2 =icFgTo.getMcpProd2();
        	mcpProd2 = null != mcpProd2 ? mcpProd2 : "";
        	String mcpProd3 = icFgTo.getMcpProd3();
        	mcpProd3 = null != mcpProd3 ? mcpProd3 : "";
        	String mcpProd4 =icFgTo.getMcpProd4();
        	mcpProd4 = null != mcpProd4 ? mcpProd4 : "";

            
            sb.append(mcpDieQty).append("|").append(mcpPkg).append("|");
            sb.append(mcpProd1).append("|").append(mcpProd2).append("|");
            sb.append(mcpProd3).append("|").append(mcpProd4).append("|");
        }else{
            sb.append("").append("|").append("").append("|");
            sb.append("").append("|").append("").append("|");
            sb.append("").append("|").append("").append("|");
        }
        
        PrintWriter out = response.getWriter();
        out.print(sb.toString());

        return null;
    }
}
