package com.cista.pidb.ajax;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cista.pidb.md.dao.CornerLotCharDao;
import com.cista.pidb.md.to.CornerLotCharTo;

public class CheckCornerLotCharExist extends Action {
    @Override
    public ActionForward execute(final ActionMapping arg0, final ActionForm arg1,
            final HttpServletRequest arg2, final HttpServletResponse arg3) throws Exception {
        String wversion = arg2.getParameter("wversion");
        PrintWriter out = arg3.getWriter();
        CornerLotCharDao cornerDao = new CornerLotCharDao();
        CornerLotCharTo exist = cornerDao.findByProjCodeWVersion(wversion);
        if (exist != null) {
            out.print("true");
            return null;
        } else {
            out.print("false");
            return null;
        }
    }
}
