package com.cista.pidb.ajax;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cista.pidb.admin.to.UserTo;
import com.cista.pidb.core.LdapHelper;

public class FetchEmail extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String users = request.getParameter("users");
        String successItem = "";
        String errorItem = "";
        String[] userList = users.split(",");
        for (String user : userList) {
            if (user != null && !user.trim().equals("")) {
                UserTo u = LdapHelper.getUser(user);
                if (u == null || u.getEmail() == null || u.getEmail().equals("")) {
                    errorItem += "," + user;
                } else {
                    successItem += "," + u.getEmail();
                }
            }
        }

        if (successItem.length() > 0) {
            successItem = successItem.substring(1);
        }

        if (errorItem.length() > 0) {
            errorItem = errorItem.substring(1);
        }

        PrintWriter out = response.getWriter();
        out.print(errorItem + "|" + successItem);
        return super.execute(mapping, form, request, response);
    }
}
