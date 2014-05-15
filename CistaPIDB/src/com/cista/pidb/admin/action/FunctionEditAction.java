package com.cista.pidb.admin.action;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.cista.pidb.admin.dao.FunctionDao;
import com.cista.pidb.admin.to.FunctionTo;
import com.cista.pidb.core.DynamicTable;
import com.cista.pidb.core.HttpHelper;

public class FunctionEditAction extends DispatchAction {
    public ActionForward pre(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        String forward = "success";
        int id = Integer.parseInt(request.getParameter("selectedFunction"));
        FunctionDao functionDao = new FunctionDao();
        FunctionTo functionTo = functionDao.find(id);
        String uriPattern = functionTo.getUriPattern();
        String[] uris = uriPattern != null ? uriPattern.split(";")
                : new String[] { "" };
        String[][] packUri = new String[uris.length][1];
        for (int i = 0; i < packUri.length; i++) {
            packUri[i][0] = uris[i];
        }
        functionTo.setUriPattern(DynamicTable.pack(packUri));

        request.setAttribute("functionTo", functionTo);

        return mapping.findForward(forward);
    }

    public ActionForward save(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        String forward = "return2list";
        FunctionDao functionDao = new FunctionDao();
        FunctionTo newFunction = (FunctionTo) HttpHelper.pickupForm(
                FunctionTo.class, request);
        String[][] uris = DynamicTable.unpack(newFunction.getUriPattern());
        StringBuilder realUri = new StringBuilder();
        if (uris != null) {
            for (String[] uri : uris) {
                for (String u : uri) {
                    if (!u.equals("")) {
                        realUri.append(";").append(u);
                    }
                }
            }
        }

        if (realUri.length() > 1) {
            newFunction.setUriPattern(realUri.substring(1));
        }

        functionDao.update(newFunction);

        return mapping.findForward(forward);
    }
}

