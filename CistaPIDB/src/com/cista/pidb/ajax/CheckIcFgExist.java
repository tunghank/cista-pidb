package com.cista.pidb.ajax;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cista.pidb.md.dao.IcFgDao;
import com.cista.pidb.md.dao.ProductDao;
import com.cista.pidb.md.dao.ProjectDao;
import com.cista.pidb.md.to.IcFgTo;
import com.cista.pidb.md.to.ProductTo;
import com.cista.pidb.md.to.ProjectTo;

public class CheckIcFgExist extends Action {
    @Override
    public ActionForward execute(ActionMapping arg0, ActionForm arg1,
            HttpServletRequest arg2, HttpServletResponse arg3) throws Exception {
        PrintWriter out = arg3.getWriter();

        IcFgDao icFgDao = new IcFgDao();
        String materialNum = "";
        
        if (arg2.getParameter("materialNum") != null && arg2.getParameter("materialNum").length() > 0) {
            materialNum = arg2.getParameter("materialNum");
        } else {        
            String prodCode = arg2.getParameter("prodCode");
            String projCode = arg2.getParameter("projCode");
            String pkgCode = arg2.getParameter("pkgCode");
            String partNum = arg2.getParameter("partNum");
            String pkgType = arg2.getParameter("pkgType");
    
            String variant = arg2.getParameter("variant") != null
                    && !arg2.getParameter("variant").equals("") ? arg2
                    .getParameter("variant") : "";
    
            if (variant.equals("")) {
                // Create a new variant
                String maxVar = (String) icFgDao.findMaxVar(prodCode, projCode,
                        pkgCode);
                if (maxVar != null) {
                    String preVar = maxVar.toString();
                    if (preVar != null && !preVar.equals("")) {
                        char c = (char) ((int) preVar.charAt(0) + 1);
                        String realVar = "";
                        if (c > 57 && c < 65) {
                            realVar = new String(new char[] { 65 });
                        } else if (c > 90) {
                            // realVar = new String(new char[]{48});
                            out.print("varianterror");
                            return null;
                        } else {
                            realVar = new String(new char[] { c });
                        }
                        variant = realVar;
                    }
                } else {
                    variant = "0";
                }
            }
    
            ProductDao productDao = new ProductDao();
            ProductTo productTo = productDao.findByProdCode(prodCode);
            String prodName = productTo.getProdName();
            // prodName = prodName.length() > 5 ? prodName
            // .substring(prodName.length() - 5) : prodName;
                    
            if (prodName.length() >= 8) {
                prodName = prodName.substring(2, 6) + prodName.charAt(7);
            } else if (prodName.length() >= 6) {
                prodName = prodName.substring(2, 6) + "0";
            } else {
                out.print("prodnameerror");
                return null;            
            }
    
            ProjectDao projectDao = new ProjectDao();
            ProjectTo projectTo = projectDao.findByProjectCode(projCode);
    
            if (pkgType != null && pkgType.equals("303")) {
                pkgCode = "G" + pkgCode.substring(2);
            }            
            int pkgLen = pkgCode.length();
            for (int i = 0; i < 5 - pkgLen; i++) {
                pkgCode = pkgCode + "0";
            }
    
            if (pkgCode.equals("")) {
                out.print("pkgcodeerror");
                return null;
            }
            if (projectTo.getFab() == null || projectTo.getFab().equals("")) {
                out.print("faberror");
                return null;
            }
            if (projectTo.getProjOption() == null
                    || projectTo.getProjOption().equals("")) {
                out.print("optionerror");
                return null;
            }
    
            materialNum = "F" + prodName + projectTo.getProjOption()
                    + projectTo.getFab() + pkgCode + variant;
        }


        
        //IcFgTo icFgTo = icFgDao.findByPrimaryKey(materialNum, partNum);
        IcFgTo icFgTo = icFgDao.findByMaterialNum(materialNum);

        if (icFgTo == null) {
            out.print("false");
        } else {
            out.print("true");
        }

        return null;
    }
}
