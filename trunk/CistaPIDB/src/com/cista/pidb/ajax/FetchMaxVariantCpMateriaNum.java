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

public class FetchMaxVariantCpMateriaNum extends Action {
    /**
     * Do action performance.
     * 
     * @param mapping
     *            ActionMapping
     * @param form
     *            ActionForm
     * @param request
     *            HttpServletRequest
     * @param response
     *            HttpServletResponse
     * @return ActionForward
     * @throws Exception
     *             exception
     */
    public ActionForward execute(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {

        
        String projCodeWVersion = request.getParameter("projCodeWVersion");
        projCodeWVersion = null!=projCodeWVersion?projCodeWVersion:"";
        
        String  materialNum = request.getParameter("materialNum");
        materialNum = null!=materialNum?materialNum:"";
        
        CpMaterialDao cpMaterialDao = new CpMaterialDao();
        CpMaterialTo cpMaterialTo = cpMaterialDao.getByProjCodeWVersionMaxVariant(projCodeWVersion);
        String maxVariantCpMaterialNum = "";
        String cpMaterialNum= "";
        String preVar = "";
        String realVar = "";
        // Create a new variant
        String maxVar = "";
        if ( cpMaterialTo !=null ){
        	cpMaterialNum= cpMaterialTo.getCpMaterialNum();
        	maxVar = (String) cpMaterialDao.findMaxVar(projCodeWVersion);
        	if (maxVar != null) {
        		preVar = maxVar.toString();
        		if (preVar != null && !preVar.equals("")) {
        			char c = (char) ((int) preVar.charAt(0) + 1);
        			
        			if (c > 57 && c < 65) {
        				realVar = new String(new char[] { 65 });
        			} else if (c > 90) {
        				realVar = new String(new char[] { 48 });
        			} else {
        				realVar = new String(new char[] { c });
        			}

        			cpMaterialTo.setCpVariant(realVar);
        		}
        	} else {
        		preVar = "0";
        		realVar = "0";
        	}

        	maxVariantCpMaterialNum = cpMaterialNum.substring(0, cpMaterialNum
        			.length() - 1) + realVar;
        }else{
        	realVar = "0";
        	maxVariantCpMaterialNum = "C" + materialNum.substring(1, materialNum
        			.length() - 1) + realVar;
        }
       
        PrintWriter out = response.getWriter();
        out.print(maxVariantCpMaterialNum);


        return null;
    }
}