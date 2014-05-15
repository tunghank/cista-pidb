package com.cista.pidb.ajax;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cista.pidb.md.dao.ProjectCodeDao;
import com.cista.pidb.md.to.ProjectCodeTo;


/**
 * Check if project existed.
 * @author fumingjie
 */
public class CheckProjectExist extends Action {

    /**
     * Perform action.
     * @param arg0 ActionMapping
     * @param arg1 ActionForm
     * @param arg2 HttpServletRequest
     * @param arg3 HttpServletResponse
     * @throws Exception Exception
     * @return an actionforward
     */
    @Override
    public ActionForward execute(final ActionMapping arg0,
            final ActionForm arg1,
            final HttpServletRequest arg2,
            final HttpServletResponse arg3)
            throws Exception {
        ProjectCodeDao projDao = new ProjectCodeDao();

        String projName = arg2.getParameter("projName");
        String projOption = arg2.getParameter("projOption");
        PrintWriter out = arg3.getWriter();
        if (projOption.trim().equals("") || projOption.equals("00")){
        	projOption = "00";
        }
        //projOption = projOption == null ? "" : projOption.trim();
        String projCode = projName;
        if (!projOption.equals("00")) {
        	 projCode = "-" + projOption;
        }else{
        	projCode = projName;
        }
        ProjectCodeTo codeTo = projDao.findByProjectCodeData(projCode,projOption);
        

        if (codeTo == null) {
            out.print("false");
        } else {
            out.print("true");
        }

        return null;
    }
}
