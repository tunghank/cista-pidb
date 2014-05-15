package com.cista.pidb.ajax;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cista.pidb.md.dao.MpListDao;
import com.cista.pidb.md.to.MpListTo;

public class CheckMpListExist extends Action {
	@Override
	public ActionForward execute(ActionMapping arg0, ActionForm arg1,
			HttpServletRequest arg2, HttpServletResponse arg3) throws Exception {
		MpListDao mpListDao = new MpListDao();
		String icFgMaterialNum = arg2.getParameter("icFgMaterialNum");
		String projCodeWVersion = arg2.getParameter("projCodeWVersion");
		String tapeName = arg2.getParameter("tapeName");
		String pkgCode = arg2.getParameter("pkgCode");
		String partNum = arg2.getParameter("partNum");
		
		// MpListTo proj = mpListDao.find(partNum.trim());
		MpListTo proj = mpListDao.findByPrimaryKey(partNum,icFgMaterialNum,
				projCodeWVersion, tapeName, pkgCode );
		PrintWriter out = arg3.getWriter();

		if (proj == null) {
			out.print("false");
		} else {
			out.print("true");
		}

		return null;
	}
}
