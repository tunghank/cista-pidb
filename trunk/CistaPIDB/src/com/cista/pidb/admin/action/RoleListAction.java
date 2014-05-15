package com.cista.pidb.admin.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.cista.pidb.admin.dao.RoleDao;
import com.cista.pidb.admin.to.RoleTo;

public class RoleListAction extends DispatchAction {
    public ActionForward pre(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        String forward = "success";
        RoleDao roleDao = new RoleDao();
        List<RoleTo> roles = roleDao.findAll();
        request.setAttribute("allRoles", roles);
        return mapping.findForward(forward);
    }

    public ActionForward delete(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        String forward = "success";
        String statusMsg = "";
        RoleDao roleDao = new RoleDao();
        int selectedRole = Integer.parseInt(request.getParameter("selectedRole"));
        RoleTo deleteRole = roleDao.find(selectedRole);
        roleDao.delete(selectedRole);
        statusMsg = "User '" + deleteRole.getRoleName() + "' is deleted.";

        List<RoleTo> roles = roleDao.findAll();
        request.setAttribute("allRoles", roles);
        request.setAttribute("statusMsg", statusMsg);
        return mapping.findForward(forward);
    }
}
