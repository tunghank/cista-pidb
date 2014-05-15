package com.cista.pidb.ajax;


import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cista.pidb.md.dao.TradPkgDao;
import com.cista.pidb.md.to.TradPkgTo;

public class FetchTradPkgLF extends Action {
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

        String icFgPartNum = request.getParameter("partNum");

        TradPkgDao tradPkgDao = new TradPkgDao();
        TradPkgTo tradPkgTo = tradPkgDao.findLFTools(icFgPartNum);
        
        StringBuilder sb = new StringBuilder();

        
        if (tradPkgTo != null) {
            String lfTool = tradPkgTo.getLfTool();
            lfTool = null != lfTool ? lfTool : "";
            
            String closeLfName = tradPkgTo.getCloseLfName();
            closeLfName = null != closeLfName ? closeLfName : "";
            
            sb.append(lfTool).append("|").append(closeLfName).append("|");
            
        }else{
        	sb.append("").append("|").append("").append("|");
        }
        
        PrintWriter out = response.getWriter();
        out.print(sb.toString());

        return null;
    }
}
