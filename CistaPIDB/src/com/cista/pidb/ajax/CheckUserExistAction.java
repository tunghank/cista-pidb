package com.cista.pidb.ajax;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cista.pidb.admin.dao.UserDao;
import com.cista.pidb.admin.to.UserTo;

public class CheckUserExistAction extends Action {

    @Override
    public ActionForward execute(ActionMapping arg0, ActionForm arg1, HttpServletRequest arg2, HttpServletResponse arg3) throws Exception {
        UserDao userDao = new UserDao();
        String userId = arg2.getParameter("userId");
        String status = arg2.getParameter("status");
        UserTo userTo = userDao.find(userId);
        PrintWriter out = arg3.getWriter();
        if (userTo != null) {
            if (status != null && status.equals("active")) {
                if (userTo.getActive()) {
                    out.print("true");
                } else {
                    out.print("true");
                }
            } else {
                out.print("true");
            }
        } else {
            out.print("true");
        }
        return null;
    }

}
