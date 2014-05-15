package com.cista.pidb.admin.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.cista.pidb.admin.dao.RoleDao;
import com.cista.pidb.admin.dao.UserDao;
import com.cista.pidb.admin.to.RoleTo;
import com.cista.pidb.admin.to.UserTo;
import com.cista.pidb.core.HttpHelper;

public class UserEditAction extends DispatchAction {
    public ActionForward pre(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        String forward = "success";
        int id = Integer.parseInt(request.getParameter("selectedUser"));
        UserDao userDao = new UserDao();
        UserTo userTo = userDao.find(id);
        RoleDao roleDao = new RoleDao();
        List<RoleTo> checkedRoles = roleDao.findRoleByUserId(id);
        userTo.setRoles(checkedRoles);

        List<RoleTo> roles = roleDao.findAll();

        request.setAttribute("userTo", userTo);
        request.setAttribute("allRoles", roles);
        return mapping.findForward(forward);
    }

    public ActionForward save(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        String forward = "return2list";
        RoleDao roleDao = new RoleDao();
        UserDao userDao = new UserDao();
        UserTo newUser = (UserTo) HttpHelper.pickupForm(UserTo.class, request);
        String[] roles = request.getParameterValues("roles");

        if (roles != null && roles.length > 0) {
            for (String role : roles) {
                int roleId = Integer.parseInt(role);
                RoleTo roleTo = roleDao.find(roleId);
                newUser.addRole(roleTo);
            }
        }

        userDao.update(newUser);

        return mapping.findForward(forward);
    }
}
