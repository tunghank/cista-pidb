package com.cista.pidb.ajax;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cista.pidb.md.dao.CpMaterialMantainDao;
import com.cista.pidb.md.to.CpMaterialMatainTo;

public class CheckCpMaterialExist extends Action{
	
	public ActionForward execute(ActionMapping arg0, ActionForm arg1, HttpServletRequest arg2, HttpServletResponse arg3) throws Exception {
		CpMaterialMantainDao ctpDao = new CpMaterialMantainDao();
        String cpMaterialNum = arg2.getParameter("cpMaterialNum");

       
        CpMaterialMatainTo proj = ctpDao.findByCpMaterialNum(cpMaterialNum);
        PrintWriter out = arg3.getWriter();
        if (proj == null) {
            out.print("false");
        } else {
            out.print("true");
        }

        return null;
    }

}
