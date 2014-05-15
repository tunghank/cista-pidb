package com.cista.pidb.ajax;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cista.pidb.code.dao.FunctionParameterDao;
import com.cista.pidb.code.dao.SapMasterPackageTypeDao;
import com.cista.pidb.code.to.SapMasterPackageTypeTo;
import com.cista.pidb.md.dao.IcFgDao;
import com.cista.pidb.md.dao.TradPkgDao;
import com.cista.pidb.md.to.IcFgTo;
import com.cista.pidb.md.to.TradPkgTo;

public class FetchPkgType extends Action {

	public ActionForward execute(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		/** ***明確告知要將編碼型態傳送至前端頁面***Add by 990044** */
		response.setContentType("text/plain; charset=UTF-8");
		/** ***************** */
		String prod = request.getParameter("prodName");
		String pkgCode = request.getParameter("pkgCode");
		StringBuilder sb = new StringBuilder();
		PrintWriter out = response.getWriter();
		IcFgDao icFgDao = new IcFgDao();
		TradPkgDao tradDao = new TradPkgDao();
		SapMasterPackageTypeDao sapMasterPackageTypeDao = new SapMasterPackageTypeDao();
		// System.out.println("go to here!!!");

		IcFgTo icFgToList = icFgDao.findPkgType(pkgCode, prod);
		String pkgType = icFgToList.getPkgType();
		String projCode = icFgToList.getProjCode();
		if (pkgType != null && !pkgType.equals("")) {
			if (pkgType.equals("301") | pkgType.equals("302")
					| pkgType.equals("303")) {
				SapMasterPackageTypeTo sapMasterPackageTypeTo = sapMasterPackageTypeDao
						.findByPkgType(icFgToList.getPkgType());
				if (sapMasterPackageTypeTo != null
						&& !sapMasterPackageTypeTo.equals("")) {
					sb.append("|").append(
							sapMasterPackageTypeTo.getDescription());
				}

			} else if (pkgType.equals("304")) {
				TradPkgTo tradTo = tradDao.findByProjCodeAndPkgCode(pkgCode,
						projCode);
				if (tradTo != null && !tradTo.equals("")) {
					sb.append("|").append(tradTo.getTradPkgType());
				}
				String funName = "TRAD_PKG";
				String funFieldName = "ASSEMBLY_HOUSE";
				request.setAttribute("assemblyHouseList",
						new FunctionParameterDao().findValueList(funName,
								funFieldName));
			}
		}

		/*
		 * }
		 */
		out.print(sb.length() > 0 ? sb.substring(1) : "");
		return null;

	}

}
