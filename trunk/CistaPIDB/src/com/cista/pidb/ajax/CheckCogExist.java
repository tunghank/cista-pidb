package com.cista.pidb.ajax;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cista.pidb.md.dao.CogDao;
import com.cista.pidb.md.to.CogTo;

public class CheckCogExist extends Action {
    @Override
    public ActionForward execute(ActionMapping arg0, ActionForm arg1, HttpServletRequest arg2, HttpServletResponse arg3) throws Exception {
        CogDao cogDao = new CogDao();
        String pkgCode = arg2.getParameter("pkgCode");
        String prodCode = arg2.getParameter("prodCode");
        CogTo cog = cogDao.find(prodCode.trim(), pkgCode.trim());
        PrintWriter out = arg3.getWriter();
        if (cog != null) {
            out.print("true");
        } else {
            out.print("false");
        }
        return null;
    }
}
