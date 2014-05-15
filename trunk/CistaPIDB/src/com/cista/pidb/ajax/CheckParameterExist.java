package com.cista.pidb.ajax;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cista.pidb.admin.dao.ParameterDao;
import com.cista.pidb.admin.to.ParameterTo;

public class CheckParameterExist extends Action {
	public ActionForward execute(ActionMapping arg0, ActionForm arg1,
			HttpServletRequest arg2, HttpServletResponse arg3) throws Exception {
		// arg3.setContentType("text/plain; charset=UTF-8");
		ParameterDao paraDao = new ParameterDao();
		String funName = arg2.getParameter("funName");
		String funFieldName = arg2.getParameter("funFieldName");
		String fieldValue = arg2.getParameter("fieldValue");
		String fieldShowName =arg2.getParameter("fieldShowName");
		ParameterTo paraTo = paraDao.findList(funName, funFieldName,
				fieldValue, fieldShowName);
		PrintWriter out = arg3.getWriter();
		if (paraTo != null) {
			out.print("true");
			return null;
		} else {
			out.print("false");
			return null;
		}
	}
}
