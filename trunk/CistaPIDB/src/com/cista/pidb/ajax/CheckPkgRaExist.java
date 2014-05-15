package com.cista.pidb.ajax;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cista.pidb.md.dao.PkgRaDao;
import com.cista.pidb.md.to.PkgRaTo;

public class CheckPkgRaExist extends Action {
	@Override
	public ActionForward execute(final ActionMapping arg0,
			final ActionForm arg1, final HttpServletRequest arg2,
			final HttpServletResponse arg3) throws Exception {
		String prodName = arg2.getParameter("prodName");
		String pkgCode = arg2.getParameter("pkgCode");
		String pkgType = arg2.getParameter("pkgType");
		String worksheetNumber = arg2.getParameter("worksheetNumber");
		PrintWriter out = arg3.getWriter();
		PkgRaDao dao = new PkgRaDao();
		PkgRaTo exist = dao.findByPrimaryKey(prodName, pkgCode, pkgType,
				worksheetNumber);
		if (exist != null) {
			out.print("true");
			return null;
		} else {
			out.print("false");
			return null;
		}
	}
}
