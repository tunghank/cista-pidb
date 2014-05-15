package com.cista.pidb.ajax;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cista.pidb.md.dao.ProdStdTestRefDao;
import com.cista.pidb.md.to.ProdStdTestRefTo;

public class CheckProdStdTestRefExist extends Action {
    @Override
    public ActionForward execute(ActionMapping arg0, ActionForm arg1, HttpServletRequest arg2,
           HttpServletResponse arg3) throws Exception {
           ProdStdTestRefDao prodStdTestRefDao = new ProdStdTestRefDao();
           String testReferenceId = "";
           String variant = "0";
           String productLine = arg2.getParameter("productLine");
           String productFamily = arg2.getParameter("productFamily");
           String maxVar = (String) prodStdTestRefDao.findMaxVar(productLine.trim(), productFamily.trim());
           if (maxVar != null) {
               String preVar = maxVar.toString();
               if (preVar != null && !preVar.equals("")) {
                   char c = (char) ((int) preVar.charAt(0) + 1);
                   String realVar = "";
                   if (c > 57 && c < 65) {
                       realVar = new String(new char[]{65});
                   } else if (c > 90) {
                       realVar = new String(new char[]{48});
                   } else {
                       realVar = new String(new char[]{c});
                   }
                   variant = realVar;
               }
           }
           testReferenceId = productLine + productFamily + variant;
           ProdStdTestRefTo proj = prodStdTestRefDao.findByTestReferenceId(testReferenceId);
           PrintWriter out = arg3.getWriter();
           if (proj == null) {
               out.print("false");
           } else {
               out.print("true");
           }

           return null;
       }
}
