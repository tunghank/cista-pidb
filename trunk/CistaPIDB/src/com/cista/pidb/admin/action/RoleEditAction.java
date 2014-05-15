package com.cista.pidb.admin.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.cista.pidb.admin.dao.FunctionDao;
import com.cista.pidb.admin.dao.RoleDao;
import com.cista.pidb.admin.to.FunctionTo;
import com.cista.pidb.admin.to.RoleTo;
import com.cista.pidb.core.HttpHelper;

public class RoleEditAction extends DispatchAction {
    public ActionForward pre(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        String forward = "success";
        int id = Integer.parseInt(request.getParameter("selectedRole"));
        RoleDao roleDao = new RoleDao();
        RoleTo roleTo = roleDao.find(id);
        FunctionDao functionDao = new FunctionDao();
        List<FunctionTo> checkedFunctions = functionDao.findFuncByRoleId(id);

        List<FunctionTo> functions = functionDao.findAll();

        String functionTable = FunctionTable.draw(functions, checkedFunctions);
        request.setAttribute("functionTable", functionTable);

        request.setAttribute("roleTo", roleTo);
        return mapping.findForward(forward);
    }

    public ActionForward save(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        String forward = "return2list";
        FunctionDao functionDao = new FunctionDao();
        RoleDao roleDao = new RoleDao();
        RoleTo newRole = (RoleTo) HttpHelper.pickupForm(RoleTo.class, request);
        String[] functions = request.getParameterValues("functions");

        if (functions != null && functions.length > 0) {
            for (String function : functions) {
                int roleId = Integer.parseInt(function);
                FunctionTo functionTo = functionDao.find(roleId);
                newRole.addFunction(functionTo);
            }
        }

        roleDao.update(newRole);

        return mapping.findForward(forward);
    }
}
