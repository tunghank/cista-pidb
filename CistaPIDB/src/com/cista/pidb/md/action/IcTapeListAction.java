package com.cista.pidb.md.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.cista.pidb.md.dao.IcTapeDao;

public class IcTapeListAction extends DispatchAction {
       public ActionForward list(final ActionMapping mapping,
                final ActionForm form, final HttpServletRequest request,
                final HttpServletResponse response) {
            String forward = "success";
            String callback = request.getParameter("callback");
            IcTapeDao icTapeDao = new IcTapeDao();
            request.setAttribute("selectList", icTapeDao.findPkgVersion());
            request.setAttribute("callback", callback);
            return mapping.findForward(forward);
        }
}
