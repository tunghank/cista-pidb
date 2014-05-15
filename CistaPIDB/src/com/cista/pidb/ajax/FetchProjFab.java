package com.cista.pidb.ajax;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cista.pidb.md.dao.ProjectDao;
import com.cista.pidb.md.to.ProjectTo;

public class FetchProjFab extends Action {
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String projCode = request.getParameter("projCode");
        ProjectDao projectDao = new ProjectDao();
        ProjectTo to = projectDao.findByProjectCode(projCode);
        
        PrintWriter out = response.getWriter();
        out.print(to.getFab() != null ? to.getFab() : "");
        return null;
    }
}

