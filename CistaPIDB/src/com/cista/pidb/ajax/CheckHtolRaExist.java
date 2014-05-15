package com.cista.pidb.ajax;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cista.pidb.md.dao.HtolRaDao;
import com.cista.pidb.md.to.HtolRaTo;

public class CheckHtolRaExist extends Action {
    @Override
    public ActionForward execute(ActionMapping arg0, ActionForm arg1, HttpServletRequest arg2, HttpServletResponse arg3) throws Exception {
        HtolRaDao htolRaDao = new HtolRaDao();
        String projCodeWVersion = arg2.getParameter("projCodeWVersion");
        String raTestItem = arg2.getParameter("raTestItem");
        
        //HtolRaTo to = htolRaDao.findByProjCodeWVersion(projCodeWVersion);
        HtolRaTo to = htolRaDao.findByProjCodeWVersion(projCodeWVersion, raTestItem);
        PrintWriter out = arg3.getWriter();

        if (to == null) {
            out.print("false");
        } else {
            out.print("true");
        }

        return null;
    }
}