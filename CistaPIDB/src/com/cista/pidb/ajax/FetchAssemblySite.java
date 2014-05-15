package com.cista.pidb.ajax;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cista.pidb.code.dao.FunctionParameterDao;
import com.cista.pidb.code.to.FunctionParameterTo;

public class FetchAssemblySite extends Action {

	public ActionForward execute(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		/** ***明確告知要將編碼型態傳送至前端頁面***Add by 990044** */
		response.setContentType("text/plain; charset=UTF-8");
		/** ***************** */
		StringBuilder sb = new StringBuilder();
		PrintWriter out = response.getWriter();
		String pkgType = request.getParameter("pkgType");

		FunctionParameterDao fpDao = new FunctionParameterDao();

		if (pkgType.equals("TCP") | pkgType.equals("COF")
				| pkgType.equals("COG")) {
			String funName = "IC_TAPE";
			String funFieldName = "ASSEMBLY_SITE";

			List<FunctionParameterTo> fpToList = fpDao.findValueList(funName,
					funFieldName);
			if (fpToList != null && fpToList.size() > 0) {
				for (FunctionParameterTo fpTo : fpToList) {
					if (fpTo != null && !fpTo.equals("")) {
						sb.append("|").append(fpTo.getFieldValue());
					}
				}
				out.print(sb.length() > 0 ? sb.substring(1) : "");
			}

		} else {
			String funName = "TRAD_PKG";
			String funFieldName = "ASSEMBLY_HOUSE";
			List<FunctionParameterTo> fpToList = fpDao.findValueList(funName,
					funFieldName);
			if (fpToList != null && fpToList.size() > 0) {
				for (FunctionParameterTo fpTo : fpToList) {
					if (fpTo != null && !fpTo.equals("")) {
						sb.append("|").append(fpTo.getFieldValue());
					}
				}
				out.print(sb.length() > 0 ? sb.substring(1) : "");
			}

		}
		return null;

	}
}
