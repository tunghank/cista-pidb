package com.cista.pidb.ajax;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cista.pidb.md.dao.CpMaterialDao;
import com.cista.pidb.md.dao.IcWaferDao;
import com.cista.pidb.md.to.CpMaterialTo;
import com.cista.pidb.md.to.IcWaferTo;

public class FetchIcWaferMaterialDesc extends Action {
	public ActionForward execute(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {

		String projWver = request.getParameter("projWver");
		String cpMaterialNum = request.getParameter("cpMaterialNum");
		IcWaferDao icWaferDao = new IcWaferDao();
		CpMaterialDao materialDao = new CpMaterialDao();
		StringBuilder sb = new StringBuilder();
		String newMaterialNum = 'W' + cpMaterialNum.substring(1, 13);
		IcWaferTo icWaferList = icWaferDao.findMaterialDesc(
				newMaterialNum, projWver);
		if (icWaferList != null) {
				sb.append(",").append(icWaferList.getMaterialDesc());		
		} else {
			CpMaterialTo materialTo = materialDao
					.findByCpMaterialNum(cpMaterialNum);
			if (materialTo != null) {
				sb.append(",").append(materialTo.getProjectCodeWVersion());
			}
		}

		PrintWriter out = response.getWriter();
		out.print(sb.length() > 0 ? sb.substring(1) : "");

		return null;
	}

}
