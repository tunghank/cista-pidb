package com.cista.pidb.md.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import com.cista.pidb.admin.dao.UserDao;
import com.cista.pidb.admin.to.UserTo;
import com.cista.pidb.core.HttpHelper;
import com.cista.pidb.core.PIDBContext;
import com.cista.pidb.md.dao.BumpMaskDao;
import com.cista.pidb.md.erp.BumpMaskERP;
import com.cista.pidb.md.erp.ERPHelper;
import com.cista.pidb.md.erp.ReleaseERPException;
import com.cista.pidb.md.to.BumpMaskTo;

/**
 * .
 * 
 * @author Hu Meixia
 */
public class BumpMaskEditAction extends DispatchAction {
    /**
     * .
     * 
     * @param mapping
     *            ActionMapping
     * @param form
     *            ActionForm
     * @param request
     *            HttpServletRequest
     * @param response
     *            HttpServletResponse
     * @return ActionForward
     */
    public ActionForward pre(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        String forward = "viewJsp";
        String maskName = request.getParameter("maskName");
        String projCode = request.getParameter("projCode");
        request.setAttribute("ref", new BumpMaskDao().findByPrimaryKey(
                maskName, projCode));
        return mapping.findForward(forward);
    }

    /**
     * .
     * 
     * @param mapping
     *            ActionMapping
     * @param form
     *            ActionForm
     * @param request
     *            HttpServletRequest
     * @param response
     *            HttpServletResponse
     * @return ActionForward
     */
    public ActionForward save(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        String forward = "success";
        BumpMaskDao bumpMaskDao = new BumpMaskDao();
        final BumpMaskTo bumpMaskTo = (BumpMaskTo) HttpHelper.pickupForm(
                BumpMaskTo.class, request);
        final UserTo loginUser = PIDBContext.getLoginUser(request);
        bumpMaskTo.setModifiedBy(loginUser.getUserId());

        Map<String, Object> key = new HashMap<String, Object>();
        key.put("MASK_NAME", request.getParameter("maskName"));
        key.put("PROJ_CODE", request.getParameter("projCode"));

        // add assignTo and assignEmail
        String assigns = "";
        String[] assignTo = request.getParameterValues("assignTo");
        if (assignTo != null) {
            for (String a : assignTo) {
                assigns += "," + a;
            }
        }

        assigns = assigns.length() > 0 ? assigns.substring(1) : assigns;

        UserDao userDao = new UserDao();
        String emails = userDao.fetchEmail(assignTo);
        bumpMaskTo.setAssignTo(assigns);
        bumpMaskTo.setAssignEmail(emails);

        bumpMaskDao.update(bumpMaskTo, "PIDB_BUMPING_MASK", key);

        // send mail
        SendMailDispatch.sendMailByModify("MD-3", bumpMaskTo.getMaskName(),
                emails, SendMailDispatch.getUrl("MD_3_EDIT", request
                        .getContextPath(), bumpMaskTo));

        String toErp = request.getParameter("toErp");
        if ("1".equals(toErp.trim())) {

            TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
                String result = null;

                public void doInTransactionWithoutResult(
                        final TransactionStatus status) {
                    result = BumpMaskERP.release(bumpMaskTo, loginUser);
                    if (result != null) {
                        throw new ReleaseERPException(result);
                    }
                }
            };

            try {
                new BumpMaskDao().doInTransaction(callback);
                
            } catch (ReleaseERPException e) {
                request.setAttribute("error", ERPHelper.getErrorMessage(e
                        .getMessage()));

                request.setAttribute("ref", bumpMaskTo);
                return mapping.findForward("viewJsp");
            }

            // send mail
            SendMailDispatch.sendMailByErp("MD-3", bumpMaskTo.getMaskName(),
                    emails, "");
        }
        return mapping.findForward(forward);
    }
}
