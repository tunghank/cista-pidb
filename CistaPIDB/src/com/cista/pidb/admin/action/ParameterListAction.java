package com.cista.pidb.admin.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.cista.pidb.admin.dao.ParameterDao;
import com.cista.pidb.admin.to.ParameterTo;
import com.cista.pidb.core.HttpHelper;


public class ParameterListAction extends DispatchAction {
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
        ParameterTo queryTo = (ParameterTo) HttpHelper.pickupForm(ParameterTo.class, request);
        ParameterDao paraDao = new ParameterDao();
        List<ParameterTo> result = paraDao.find(queryTo);
        String statusMsg = "";
        if (result == null || result.size() == 0) {
            statusMsg = "No Parameter List found.";
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
        ParameterDao paraDao = new ParameterDao();
        ParameterTo queryTo = (ParameterTo) HttpHelper.pickupForm(ParameterTo.class, request);
        //int selectedUser = Integer.parseInt(request.getParameter("selectedUser"));
        //ParameterTo deleteUser = paraDao.find(queryTo);
        //UserTo loginUser = PIDBContext.getLoginUser(request);
        //if (loginUser.getId() != selectedUser) {
        paraDao.delete(queryTo);
            statusMsg = "deleted Parameter List is success";
       /* } else {
            statusMsg = "Can not delete yourself.";
        }*/

        //ParameterTo queryTo = (ParameterTo) HttpHelper.pickupForm(ParameterTo.class, request);
        List<ParameterTo> result = paraDao.find(queryTo);

        request.setAttribute("result", result);
        request.setAttribute("queryTo", queryTo);
        request.setAttribute("statusMsg", statusMsg);
        return mapping.findForward(forward);
    }
}
