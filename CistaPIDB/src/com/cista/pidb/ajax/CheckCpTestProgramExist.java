package com.cista.pidb.ajax;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cista.pidb.md.dao.CpTestProgramDao;
import com.cista.pidb.md.to.CpTestProgramTo;

public class CheckCpTestProgramExist extends Action {
    @Override
    public ActionForward execute(ActionMapping arg0, ActionForm arg1, HttpServletRequest arg2, HttpServletResponse arg3) throws Exception {
           CpTestProgramDao ctpDao = new CpTestProgramDao();
           String cpTestProgName = arg2.getParameter("cpTestProgName").trim();
           String projCodeWVersion = arg2.getParameter("projCodeWVersion");
           String cpMaterialNum = arg2.getParameter("cpMaterialNum");
           
           //CpTestProgramTo proj = ctpDao.findByPrimaryKey(cpTestProgName, projCodeWVersion);
           CpTestProgramTo proj = ctpDao.findByPrimaryKey(cpTestProgName, projCodeWVersion, cpMaterialNum);
           PrintWriter out = arg3.getWriter();
           if (proj == null) {
               out.print("false");
           } else {
               out.print("true");
           }

           return null;
       }
}
