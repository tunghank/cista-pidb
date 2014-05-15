package com.cista.pidb.ajax;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cista.pidb.md.dao.EsdTestDao;
import com.cista.pidb.md.to.EsdTestTo;

public class CheckEsdTestExist extends Action {
	@Override
	public ActionForward execute(final ActionMapping arg0,
			final ActionForm arg1, final HttpServletRequest arg2,
			final HttpServletResponse arg3) throws Exception {
		String wversion = arg2.getParameter("projCodeWVersion");
		String esdTesting = arg2.getParameter("idEsdTesting");
		PrintWriter out = arg3.getWriter();
		EsdTestDao esdDao = new EsdTestDao();
		EsdTestTo exist = esdDao.findByProjCodeWVersion(wversion, esdTesting);
		if (exist == null) {
			out.print("false");
		} else {
			out.print("true");
		}
		return null;
	}
}
