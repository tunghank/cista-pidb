package com.cista.pidb.ajax;


import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FetchWloMaterialNum extends Action {
	
	protected final Log logger = LogFactory.getLog(getClass());
	
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

        String prodName = request.getParameter("prodName");
        prodName =null!=prodName?prodName:"";
        
        //String packageCode = request.getParameter("packageCode");
        //packageCode =null!=packageCode?packageCode:"";
        String packageCode = "00";
        
        String prodType = request.getParameter("prodType");
        prodType =null!=prodType?prodType:"";
        
        String vendorCode = request.getParameter("vendorCode");
        vendorCode =null!=vendorCode?vendorCode:"";
        
        String serialNum = request.getParameter("serialNum");
        serialNum =null!=serialNum?serialNum:"";
        
        String variant = request.getParameter("variant");
        variant =null!=variant?variant:"";      
        variant = variant.toUpperCase();
        
        String reserved = request.getParameter("reserved");
        reserved =null!=reserved?reserved:"";
        reserved = reserved.toUpperCase();
        
        String packingType = request.getParameter("packingType");
        packingType =null!=packingType?packingType:"";
        
        String partNum = prodName.substring(0, 6) + "-000" + "-" + variant  
        + prodType + vendorCode + serialNum + reserved +  packingType;
        
        partNum =null!=partNum?partNum:"";
        
		if (prodName.length() >= 8) {
			prodName = prodName.substring(2, 6) + "0";
		} else if (prodName.length() >= 6) {
			prodName = prodName.substring(2, 6) + "0";
		}

        String wlmMaterial = "F" + prodName + packageCode + variant 
        + prodType + vendorCode + serialNum + reserved +  packingType;
        
        wlmMaterial =null!=wlmMaterial?wlmMaterial:"";
        
        PrintWriter out = response.getWriter();
        out.print(wlmMaterial+"|"+partNum);

        return null;
    }
}

