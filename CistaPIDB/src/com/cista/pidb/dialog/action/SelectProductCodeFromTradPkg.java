package com.cista.pidb.dialog.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.cista.pidb.md.dao.TradPkgDao;

public class SelectProductCodeFromTradPkg extends DispatchAction {

        public ActionForward pre(final ActionMapping mapping,
                  final ActionForm form, final HttpServletRequest request,
                  final HttpServletResponse response) {
            String forward = "pre_success";
            String callback = request.getParameter("callback");
            TradPkgDao dao = new TradPkgDao();
            request.setAttribute("selectList", dao.findDistAllProdCode());
            request.setAttribute("callback", callback);
            return mapping.findForward(forward);
        }
}
