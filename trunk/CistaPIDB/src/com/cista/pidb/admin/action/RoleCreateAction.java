package com.cista.pidb.admin.action;

import java.util.ArrayList;
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

public class RoleCreateAction extends DispatchAction {
    public ActionForward pre(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        String forward = "success";
        FunctionDao functionDao = new FunctionDao();
        List<FunctionTo> functions = functionDao.findAll();

        request.setAttribute("allFunctions", functions);

        String functionTable = FunctionTable.draw(functions, new ArrayList<FunctionTo>());
        request.setAttribute("functionTable", functionTable);
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
            for (String func : functions) {
                int funcId = Integer.parseInt(func);
                FunctionTo functionTo = functionDao.find(funcId);
                newRole.addFunction(functionTo);
            }
        }

        roleDao.insert(newRole);

        return mapping.findForward(forward);
    }
}
