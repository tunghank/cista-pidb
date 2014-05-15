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
import com.cista.pidb.md.dao.CpMaterialDao;
import com.cista.pidb.md.dao.CpPolishMaterialDao;
import com.cista.pidb.md.to.CpPolishMaterialTo;

public class FetchMaxVariantPolish extends Action {

	public ActionForward execute(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {

		String projCodeWVersion = request.getParameter("projCodeWVersion");
		projCodeWVersion = null != projCodeWVersion ? projCodeWVersion : "";

		String polishVariant = request.getParameter("polishVariant");
		polishVariant = null != polishVariant ? polishVariant : "";

		String variant = polishVariant.substring(12, 13);

		String cpPolishMaterialNum = polishVariant.substring(0, 10) + "P"
				+ polishVariant.substring(11, 13);

		String remark = request.getParameter("remark");
		remark = null != remark ? remark : "";

		String status = request.getParameter("status");
		status = null != status ? status : "";

		String polishDesc = request.getParameter("polishDesc");
		polishDesc = null != polishDesc ? polishDesc : "";

		UserTo loginUser = PIDBContext.getLoginUser(request);
		StringBuilder sb = new StringBuilder();
		sb.append("|").append(cpPolishMaterialNum);
		sb.append("|").append(polishDesc);
		CpPolishMaterialDao polishDao = new CpPolishMaterialDao();
		CpPolishMaterialTo polishTo = new CpPolishMaterialTo();
		polishTo.setCpPolishMaterialNum(cpPolishMaterialNum);
		polishTo.setCpPolishVariant(variant);
		polishTo.setProjectCodeWVersion(projCodeWVersion);
		polishTo.setRemark(remark);
		polishTo.setDescription(polishDesc);
		polishTo.setCreatedBy(loginUser.getUserId());
		polishTo.setModifiedBy(loginUser.getUserId());
		polishTo.setMpStatus(status);

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

		polishTo.setUpdateDate(updateDay);
		String mes ="";
		if (polishDao.findPolishMaterial(cpPolishMaterialNum) != null){
			 mes ="true";
			 sb.append("|").append(mes);
		}else{
			polishDao.insert(polishTo, "PIDB_CP_POLISH_MATERIAL");
			mes = "false";
			sb.append("|").append(mes);
		}

		
		PrintWriter out = response.getWriter();
		out.print(sb.length() > 0 ? sb.substring(1) : "");
		request.setAttribute("variantList", new CpMaterialDao()
				.getCpMaterialVariantPolish(projCodeWVersion));
		// request.setAttribute("v", cpMaterialTo.getCpMaterialNum());

		return null;
	}

}
