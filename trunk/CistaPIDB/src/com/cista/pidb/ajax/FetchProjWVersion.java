package com.cista.pidb.ajax;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cista.pidb.md.dao.IcWaferDao;
import com.cista.pidb.md.dao.ProjectCodeDao;
import com.cista.pidb.md.dao.ProjectDao;
import com.cista.pidb.md.to.ProjectCodeTo;
import com.cista.pidb.md.to.ProjectTo;

/**
 * Ajax Action for fetching FetchProjWVersion by project code.
 * 
 * @author fumingjie
 */
public class FetchProjWVersion extends Action {

	/**
	 * Do action perform.
	 * 
	 * @param mapping
	 *            ActionMapping
	 * @param form
	 *            ActionForm
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @throws Exception
	 *             Exception
	 * @return ActionForward instance.
	 */
	public ActionForward execute(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {

		String projCode = request.getParameter("projCode");
		projCode = null != projCode ? projCode : "";

		String optionVer = request.getParameter("optionVer");
		optionVer = null != optionVer ? optionVer : "";

		String bodyVer = request.getParameter("bodyVer");
		bodyVer = null != bodyVer ? bodyVer : "";

		IcWaferDao icWaferDao = new IcWaferDao();

		ProjectCodeDao projCodeDao = new ProjectCodeDao();

		ProjectCodeTo projCodeTo = projCodeDao.findByProjectCode(projCode);

		StringBuilder sb = new StringBuilder();

		PrintWriter out = response.getWriter();

		//if (!optionVer.equals("") && !bodyVer.equals("")) {

			String projName = projCodeTo.getProjName();
			String optionFor = projCodeTo.getProjOption();
			if (optionFor == null) {
				optionFor = "00";
			}

			if (optionFor.equals("00")) {
				optionVer = "0";
			}

			//
			ProjectDao projectDao = new ProjectDao();
			ProjectTo projectTo = projectDao.find(projName);

			// project_code_w_version
			String projectCodeWVersion;
			if (projCode.length() == 6) {
				projectCodeWVersion = projCode + bodyVer;
			} else {

				projectCodeWVersion = projName + bodyVer + "-" + optionFor
						+ optionVer;
			}
			sb.append("|").append(projectCodeWVersion);
			// variant
			String variant = "";
			String maxVar = icWaferDao.findMaxVar(projectCodeWVersion,
					projectTo.getFab());
			if (maxVar != null) {
				String preVar = maxVar.toString();
				if (preVar != null && !preVar.equals("")) {
					char c = (char) ((int) preVar.charAt(0) + 1);
					if (c > 57 && c < 65) {
						variant = new String(new char[] { 65 });
					} else if (c > 90) {
						variant = new String(new char[] { 48 });
					} else {
						variant = new String(new char[] { c });
					}
				}
			} else {
				variant = "0";
			}
			sb.append("|").append(variant);

			// for materialNum.
			int len = projName.length();
			String projNameFor = projName;
			if (len > 4) {
				projNameFor = projName.substring(len - 4, len);
			}
			String materialNum = "W" + projNameFor + bodyVer
					+ optionFor;
			materialNum = materialNum + optionVer;
			if (projectTo.getFab() != null && projectTo.getFab().length() > 0) {
				materialNum += projectTo.getFab();
			}
			materialNum += "00" + variant;
			sb.append("|").append(materialNum);
			out.print(sb.length() > 0 ? sb.substring(1) : "");
			
		/*} else {
			String projName = projCodeTo.getProjName();
			String optionFor = projCodeTo.getProjOption();
			if (optionFor == null) {
				optionFor = "00";
			}

			if (optionFor.equals("00")) {
				optionVer = "0";
			}

			//
			ProjectDao projectDao = new ProjectDao();
			ProjectTo projectTo = projectDao.find(projName);

			// project_code_w_version
			String projectCodeWVersion;
			if (projCode.length() == 6) {
				projectCodeWVersion = projCode + bodyVer;
			} else {

				projectCodeWVersion = projName + bodyVer + "-" + optionFor
						+ optionVer;
			}

			sb.append("|").append(projectCodeWVersion);

			out.print(sb.length() > 0 ? sb.substring(1) : "");
		}*/

		return null;
	}
}
