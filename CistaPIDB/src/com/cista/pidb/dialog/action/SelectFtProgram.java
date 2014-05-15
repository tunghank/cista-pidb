package com.cista.pidb.dialog.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.cista.pidb.md.dao.FtTestProgramDao;
import com.cista.pidb.md.dao.ProdStdTestRefDao;

public class SelectFtProgram extends DispatchAction {
    /**
     * .
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     */
    public ActionForward list(final ActionMapping mapping,
               final ActionForm form, final HttpServletRequest request,
               final HttpServletResponse response) {
           String forward = "success";
           String callback = request.getParameter("callback");
           String partNum = request.getParameter("partNum");
           String projCode = request.getParameter("projCode");
           FtTestProgramDao ftTestProgramDao = new FtTestProgramDao();
           List selectList = ftTestProgramDao.findByPartNum(partNum);

           if (selectList == null || selectList.size() < 1) {
               ProdStdTestRefDao prodStdTestRefDao = new ProdStdTestRefDao();
               selectList = prodStdTestRefDao.findByProjCode(projCode);
               forward = "successToStd";
           }

           request.setAttribute("selectList", selectList);
           request.setAttribute("callback", callback);
           return mapping.findForward(forward);
       }
}
