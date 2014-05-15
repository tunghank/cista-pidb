package com.cista.pidb.ajax;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cista.pidb.md.dao.FtTestProgramDao;
import com.cista.pidb.md.to.FtTestProgramTo;

public class CheckFtTestProgramExist extends Action {
    @Override
    public ActionForward execute(ActionMapping arg0, ActionForm arg1, HttpServletRequest arg2, HttpServletResponse arg3) throws Exception {
           FtTestProgramDao ftpDao = new FtTestProgramDao();
           String ftTestProgName = arg2.getParameter("ftTestProgName");
           String ftMaterialNum = arg2.getParameter("ftMaterialNum");
           System.out.println("ftMaterialNum " + ftMaterialNum);
           FtTestProgramTo proj = ftpDao.find(ftTestProgName.trim(), ftMaterialNum.trim());
           PrintWriter out = arg3.getWriter();
           if (proj == null) {
               out.print("false");
           } else {
               out.print("true");
           }

           return null;
       }
}
