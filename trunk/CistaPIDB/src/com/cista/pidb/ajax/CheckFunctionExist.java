package com.cista.pidb.ajax;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cista.pidb.admin.dao.FunctionDao;
import com.cista.pidb.admin.to.FunctionTo;

public class CheckFunctionExist extends Action {

    @Override
    public ActionForward execute(ActionMapping arg0, ActionForm arg1,
            HttpServletRequest arg2, HttpServletResponse arg3) throws Exception {
        FunctionDao functionDao = new FunctionDao();
        String funcName = arg2.getParameter("funcName");
        FunctionTo function = functionDao.find(funcName);
        PrintWriter out = arg3.getWriter();

        if (function == null) {
            out.print("false");
        } else {
            out.print("true");
        }

        return null;
    }

}

