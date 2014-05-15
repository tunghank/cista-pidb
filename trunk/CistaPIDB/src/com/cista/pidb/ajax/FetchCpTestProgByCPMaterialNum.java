package com.cista.pidb.ajax;


import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cista.pidb.md.dao.CpMaterialDao;
import com.cista.pidb.md.to.CpMaterialTo;


public class FetchCpTestProgByCPMaterialNum extends Action {
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

        String cpMaterialNum = request.getParameter("cpMaterialNum");

        CpMaterialDao cpMaterialDao = new CpMaterialDao();
        CpMaterialTo cpMaterialTo= cpMaterialDao.findByCpMaterialNum(cpMaterialNum);
        
        StringBuilder sb = new StringBuilder();
        if (cpMaterialTo != null) {
        	String testProg = cpMaterialTo.getCpTestProgramNameList();
        	testProg =null!=testProg?testProg:"";

        	if ( !testProg.equals("")){
	        	if (testProg.indexOf(",")>=0){
	        		sb.append("|"+testProg.replaceAll(",", "|"));
	        	}else{
	        		sb.append("|"+testProg);
	        	}
        	}
        }
        
        PrintWriter out = response.getWriter();
        out.print(sb.length() > 0 ?  sb.substring(1) : "");

        return null;
    }
}
