package com.cista.pidb.ajax;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cista.pidb.admin.dao.RoleDao;
import com.cista.pidb.admin.to.RoleTo;

public class CheckRoleExist extends Action {

    @Override
    public ActionForward execute(ActionMapping arg0, ActionForm arg1, HttpServletRequest arg2, HttpServletResponse arg3) throws Exception {
        RoleDao roleDao = new RoleDao();
        String roleName = arg2.getParameter("roleName");
        RoleTo role = roleDao.find(roleName);
        PrintWriter out = arg3.getWriter();
        
        if (role == null) {
            out.print("false");
        } else {
            out.print("true");
        }
        
        return null;
    }

}
