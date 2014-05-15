package com.cista.pidb.md.action;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.cista.pidb.admin.to.UserTo;
import com.cista.pidb.core.HttpHelper;
import com.cista.pidb.core.PIDBContext;
import com.cista.pidb.md.dao.CpMaterialDao;
import com.cista.pidb.md.to.CpMaterialTo;

public class CpMaterialEditAction extends DispatchAction {

	public ActionForward pre(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		String forward = "viewJsp";
		String cpMaterial = (String) request.getParameter("cpMaterialNum");
		String projCodeWVersion = (String) request
				.getParameter("projCodeWVersion");

		request.setAttribute("ref", new CpMaterialDao().findByPrimaryKey(
				cpMaterial, projCodeWVersion));
		return mapping.findForward(forward);
	}

	public ActionForward save(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {

		String forward = "viewEditJsp";

		CpMaterialDao cpmaterialDao = new CpMaterialDao();
		CpMaterialTo cpMaterialTo = (CpMaterialTo) HttpHelper.pickupForm(
				CpMaterialTo.class, request, true);
		
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
		cpMaterialTo.setUpdateDate(updateDay);
		
		UserTo loginUser = PIDBContext.getLoginUser(request);
		cpMaterialTo.setModifiedBy(loginUser.getUserId());
		cpmaterialDao.updateCpMaterial(cpMaterialTo);
		String error = "Update Successfully";

		request.setAttribute("error", error);

		return mapping.findForward(forward);
	}
}
