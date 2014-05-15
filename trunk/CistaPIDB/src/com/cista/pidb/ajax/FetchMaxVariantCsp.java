package com.cista.pidb.ajax;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.text.SimpleDateFormat;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cista.pidb.admin.to.UserTo;
import com.cista.pidb.code.dao.FunctionParameterDao;
import com.cista.pidb.core.PIDBContext;
import com.cista.pidb.md.dao.CpCspMaterialDao;
import com.cista.pidb.md.dao.CpMaterialDao;
import com.cista.pidb.md.dao.IcWaferDao;
import com.cista.pidb.md.to.CpCspMaterialTo;
import com.cista.pidb.md.to.CpMaterialTo;
import com.cista.pidb.md.to.IcWaferTo;

public class FetchMaxVariantCsp extends Action {
	protected final Log logger = LogFactory.getLog(getClass());
	public ActionForward execute(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {

		String forward = "success";
		
		CpMaterialDao cpMaterialDao = new CpMaterialDao();
		
		String projCodeWVersion = request.getParameter("projCodeWVersion");
		projCodeWVersion = null != projCodeWVersion ? projCodeWVersion : "";

		String cspVersion = request.getParameter("cspVersion");
		cspVersion = null != cspVersion ? cspVersion : "";
		
		String cpMaterial = request.getParameter("cspVariant");
		cpMaterial = null != cpMaterial ? cpMaterial : "";

		String cspDesc = request.getParameter("cspDesc");
		cspDesc = null != cspDesc ? cspDesc : "";
		
		UserTo loginUser = PIDBContext.getLoginUser(request);
		
		CpMaterialTo cpMaterialTo = cpMaterialDao.findByCpMaterialNum(cpMaterial);
		cpMaterialTo = null != cpMaterialTo ? cpMaterialTo : new CpMaterialTo();
		
		String cpVariant = cpMaterialTo.getCpVariant();


		String cspMaterialNum = cpMaterialTo.getCpMaterialNum().substring(0, 10)
		+ cspVersion + cpVariant;

		/** *****塞入修改日期******** */
		// add update_Time
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
		Calendar rightNow = Calendar.getInstance();


		String updateDay = ( df.format(rightNow.getTime()) );
		/** *****End******** */
		CpCspMaterialDao cspDao = new CpCspMaterialDao();
		CpCspMaterialTo cspTo = new CpCspMaterialTo();
		cspTo.setCpCspMaterialNum(cspMaterialNum);
		cspTo.setCpCspVariant(cpVariant);
		cspTo.setProjectCodeWVersion(projCodeWVersion);
		cspTo.setRemark(null);
		cspTo.setDescription(cspDesc);
		cspTo.setCreatedBy(loginUser.getUserId());
		cspTo.setModifiedBy(loginUser.getUserId());
		cspTo.setMpStatus("N/A");
		cspTo.setVersion(cspVersion);
		cspTo.setUpdateDate(updateDay);

		
		String message = "";
		if (cspDao.findCspMaterial(cspMaterialNum) != null) {
			message = "Crate Fail , already exist";

		} else {
			cspDao.insertCspMaterial(cspTo);
			message = "Crate Success.";

		}

		
		List<CpCspMaterialTo> cpCspList = new ArrayList<CpCspMaterialTo>();
		
		//2.1 Return Value
		
		cpCspList = cspDao.findByProjectCode(projCodeWVersion);
		//Get CSP List
		String fundName = "CSP";
		String funFieldName = "VERSION";
		List versionList = new FunctionParameterDao()
		.findValueList(fundName, funFieldName);
		
		List variantList = new CpMaterialDao()
		.getCpMaterialVariant(projCodeWVersion);
		
		request.setAttribute("versionList", versionList);
		request.setAttribute("variantList", variantList);
		request.setAttribute("cpCspList", cpCspList);
		request.setAttribute("message", message);
		
		request.setAttribute("projCodeWVersion", projCodeWVersion);

		
		return mapping.findForward(forward);
	}

}
