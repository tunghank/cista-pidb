package com.cista.pidb.ajax;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cista.pidb.md.dao.IcTapeDao;
import com.cista.pidb.md.to.IcTapeTo;

public class CheckIcTapeExist extends Action {
    @Override
    public ActionForward execute(ActionMapping arg0, ActionForm arg1,
            HttpServletRequest arg2, HttpServletResponse arg3) throws Exception {
        PrintWriter out = arg3.getWriter();

        IcTapeDao icTapeDao = new IcTapeDao();
        String prodName = arg2.getParameter("prodName");
        String pkgCode = arg2.getParameter("pkgCode");
        String pkgVersion = arg2.getParameter("pkgVersion");
        String tapeName = arg2.getParameter("tapeName");

        String tapeVariant = arg2.getParameter("tapeVariant") != null
                && !arg2.getParameter("tapeVariant").equals("") ? arg2
                .getParameter("tapeVariant") : "";

        if (tapeVariant.equals("")) {
            // Create a new variant
            String maxVar = (String) icTapeDao.findMaxVar(prodName, pkgCode,
                    pkgVersion);
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
                    tapeVariant = realVar;
                }
            } else {
                tapeVariant = "0";
            }
        }

        if (prodName.length() >= 8) {
            prodName = prodName.substring(2, 6) + prodName.charAt(7);
        } else if (prodName.length() >= 6) {
            prodName = prodName.substring(2, 6) + "0";
        } else {
            out.print("prodnameerror");
            return null;
        }

        int pkgLen = pkgVersion.length();
        for (int i = 0; i < 2 - pkgLen; i++) {
            pkgVersion = pkgVersion + "0";
        }

        if (pkgVersion == null
                || pkgVersion.equals("")) {
            out.print("pkgversionerror");
            return null;
        }
        if (pkgCode == null
                || pkgCode.equals("")) {
            out.print("pkgcodeerror");
            return null;
        }        
        
        String materialNum = "P" + prodName + pkgCode + pkgVersion
                + tapeVariant;

        IcTapeTo icTapeTo = icTapeDao.findByPrimaryKey(materialNum, tapeName);

        if (icTapeTo == null) {
            out.print("false");
        } else {
            out.print("true");
        }

        return null;
    }
}
