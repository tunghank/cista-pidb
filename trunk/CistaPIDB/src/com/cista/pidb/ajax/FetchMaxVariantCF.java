/*
 * 2010.03.22/FCG1 @Jere Huang - new cFMaterialTo 與補mp_status及COLOR_FILTER_VARIANT .
 */
package com.cista.pidb.ajax;

import java.io.PrintWriter;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cista.pidb.admin.to.UserTo;
import com.cista.pidb.core.PIDBContext;
import com.cista.pidb.md.dao.ColorFilterMaterialDao;
import com.cista.pidb.md.to.ColorFilterMaterialTo;

public class FetchMaxVariantCF extends Action{
	/**
	 * Do action performance.
	 * 
	 * @param mapping
	 *            ActionMapping
	 * @param form
	 *            ActionForm
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 *             exception
	 */
	public ActionForward execute(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {

		String projCodeWVersion = request.getParameter("projCodeWVersion");
		projCodeWVersion = null != projCodeWVersion ? projCodeWVersion : "";

		String materialNum = request.getParameter("materialNum");
		materialNum = null != materialNum ? materialNum : "";
		String remark = request.getParameter("remark");
		remark = null != remark ? remark : "";

		String colorFilterDesc = request.getParameter("colorFilterDesc");
		colorFilterDesc = null != colorFilterDesc ? colorFilterDesc : "";
		//FCG1
		String mpStatus = request.getParameter("mpStatus");
		mpStatus = null != mpStatus ? mpStatus : "";
		
		UserTo loginUser = PIDBContext.getLoginUser(request);
		StringBuilder sb = new StringBuilder();
		
		ColorFilterMaterialDao cFMaterialDao = new ColorFilterMaterialDao();
		ColorFilterMaterialTo cFMaterialTo = cFMaterialDao
				.getByProjCodeWVersionMaxVariant(projCodeWVersion);
		String maxVariantCFMaterialNum = "";
		String cFMaterialNum = "";
		String preVar = "";
		String realVar = "";
		// Create a new variant
		String maxVar = "";
		if (cFMaterialTo != null) {
			cFMaterialNum = cFMaterialTo.getColorFilterMaterialNum();
			maxVar = (String) cFMaterialDao.findMaxVar(projCodeWVersion);
			if (maxVar != null)
			{
				preVar = maxVar.toString();
				if (preVar != null && !preVar.equals("")) 
				{
					char c = (char) ((int) preVar.charAt(0) + 1);

					if (c > 57 && c < 65) 
					{
						realVar = new String(new char[] { 65 });
					} 
					else if (c > 90) 
					{
						realVar = new String(new char[] { 48 });
					}
					else 
					{
						realVar = new String(new char[] { c });
					}
					cFMaterialTo.setColorFilterVariant(realVar);
				}
			} 
			else 
			{
				preVar = "0";
				realVar = "0";
			}

			maxVariantCFMaterialNum = cFMaterialNum.substring(0, cFMaterialNum
					.length() - 1)
					+ realVar;
			sb.append("|").append(maxVariantCFMaterialNum);
		}else {
			//FCG1
			cFMaterialTo = new  ColorFilterMaterialTo();
			realVar = "0";
			maxVariantCFMaterialNum = "M"
					+ materialNum.substring(1, materialNum.length() - 1)
					+ realVar;
			sb.append("|").append(maxVariantCFMaterialNum);
		}
		sb.append("|").append(colorFilterDesc);

		/** *****塞入修改日期******** */
		// add update_Time
		Calendar rightNow = Calendar.getInstance();
		int y = rightNow.get(Calendar.YEAR);
		int m = rightNow.get(Calendar.MONTH) + 1;
		int d = rightNow.get(Calendar.DAY_OF_MONTH);
		int h = rightNow.get(Calendar.HOUR_OF_DAY);
		int min = rightNow.get(Calendar.MINUTE);
		int secend = rightNow.get(Calendar.SECOND);

		String year = String.valueOf(y);
		String month = String.valueOf(m);
		String day = String.valueOf(d);
		String hour = String.valueOf(h);
		String mini = String.valueOf(min);
		String sec = String.valueOf(secend);

		if (month.length() == 1) {
			month = "0" + month;
		}

		if (day.length() == 1) {
			day = "0" + day;
		}

		if (hour.length() == 1) {
			hour = "0" + hour;
		}

		if (mini.length() == 1) {
			mini = "0" + mini;
		}

		if (sec.length() == 1) {
			sec = "0" + sec;
		}

		String updateDay = (year + month + day + hour + mini + sec);
		/** *****End******** */
		cFMaterialTo.setColorFilterMaterialNum(maxVariantCFMaterialNum);
		/*
		 * if (maxVariantCpMaterialNum != null &&
		 * !maxVariantCpMaterialNum.equals("")) { String newVariant =
		 * maxVariantCpMaterialNum.substring(10, 13);
		 * 
		 * if (newVariant.substring(0, 1).equals("0") && newVariant.substring(1,
		 * 2).equals("0")) { String variant = newVariant.substring(2, 3);
		 * cpMaterialTo.setCpVariant(variant); } else if
		 * (newVariant.substring(0, 1).equals("0") && !newVariant.substring(1,
		 * 2).equals("0")) { String variant = newVariant.substring(1, 3);
		 * cpMaterialTo.setCpVariant(variant); } else {
		 * cpMaterialTo.setCpVariant(newVariant); } }
		 */
		// cpMaterialTo.setCpVariant(maxVariantCpMaterialNum.substring(11, 12));
		cFMaterialTo.setDescription(colorFilterDesc);
		cFMaterialTo.setProjectCodeWVersion(projCodeWVersion);
		cFMaterialTo.setRemark(remark);
		cFMaterialTo.setCreatedBy(loginUser.getUserId());
		cFMaterialTo.setModifiedBy(loginUser.getUserId());
		cFMaterialTo.setUpdateDate(updateDay);
		//FCG1
		cFMaterialTo.setColorFilterVariant(realVar);
		cFMaterialTo.setMpStatus(mpStatus);
		
		cFMaterialDao.insert(cFMaterialTo, "PIDB_COLOR_FILTER_MATERIAL");
		PrintWriter out = response.getWriter();
		out.print(sb.length() > 0 ? sb.substring(1) : "");
		// request.setAttribute("v", cpMaterialTo.getCpMaterialNum());

		return null;
	}

}
