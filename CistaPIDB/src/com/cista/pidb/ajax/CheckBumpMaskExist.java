package com.cista.pidb.ajax;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cista.pidb.md.dao.BumpMaskDao;
import com.cista.pidb.md.to.BumpMaskTo;

public class CheckBumpMaskExist extends Action {
    @Override
    public ActionForward execute(ActionMapping arg0, ActionForm arg1, HttpServletRequest arg2, HttpServletResponse arg3) throws Exception {
           BumpMaskDao bumpMaskDao = new BumpMaskDao();
           String maskName = arg2.getParameter("maskName");
           String projCode = arg2.getParameter("projCode");
           BumpMaskTo proj = bumpMaskDao.findByPrimaryKey(maskName.trim(),projCode.trim());
           PrintWriter out = arg3.getWriter();
           if (proj == null) {
               out.print("false");
           } else {
               out.print("true");
           }

           return null;
       }
}
