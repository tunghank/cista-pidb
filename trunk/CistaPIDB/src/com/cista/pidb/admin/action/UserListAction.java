package com.cista.pidb.admin.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.cista.pidb.admin.dao.UserDao;
import com.cista.pidb.admin.to.UserTo;
import com.cista.pidb.core.HttpHelper;
import com.cista.pidb.core.PIDBContext;


public class UserListAction extends DispatchAction {
    public ActionForward pre(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        String forward = "success";
        return mapping.findForward(forward);
    }

    public ActionForward query(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        String forward = "success";
        UserTo queryTo = (UserTo) HttpHelper.pickupForm(UserTo.class, request);
        UserDao userDao = new UserDao();
        List<UserTo> result = userDao.find(queryTo);
        String statusMsg = "";
        if (result == null || result.size() == 0) {
            statusMsg = "No user found.";
        } else {
            statusMsg = "Total &lt;" + result.size() + "&gt; queried and displayed.";
        }
        request.setAttribute("result", result);
        request.setAttribute("queryTo", queryTo);
        request.setAttribute("statusMsg", statusMsg);
        return mapping.findForward(forward);
    }

    public ActionForward delete(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        String forward = "success";
        String statusMsg = "";
        UserDao userDao = new UserDao();
        int selectedUser = Integer.parseInt(request.getParameter("selectedUser"));
        UserTo deleteUser = userDao.find(selectedUser);
        UserTo loginUser = PIDBContext.getLoginUser(request);
        if (loginUser.getId() != selectedUser) {
            userDao.delete(selectedUser);
            statusMsg = "User '" + deleteUser.getUserId() + "' is deleted.";
        } else {
            statusMsg = "Can not delete yourself.";
        }

        UserTo queryTo = (UserTo) HttpHelper.pickupForm(UserTo.class, request);
        List<UserTo> result = userDao.find(queryTo);

        request.setAttribute("result", result);
        request.setAttribute("queryTo", queryTo);
        request.setAttribute("statusMsg", statusMsg);
        return mapping.findForward(forward);
    }
}
