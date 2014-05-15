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
import com.cista.pidb.md.dao.CpMaterialDao;
import com.cista.pidb.md.dao.CpTsvMaterialDao;
import com.cista.pidb.md.to.CpMaterialTo;
import com.cista.pidb.md.to.CpTsvMaterialTo;

public class FetchMaxVariantTsv extends Action {
	protected final Log logger = LogFactory.getLog(getClass());
	public ActionForward execute(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {

		String forward = "success";
		
		CpMaterialDao cpMaterialDao = new CpMaterialDao();
		
		String projCodeWVersion = request.getParameter("projCodeWVersion");
		projCodeWVersion = null != projCodeWVersion ? projCodeWVersion : "";

		String tsvVersion = request.getParameter("tsvVersion");
		tsvVersion = null != tsvVersion ? tsvVersion : "";
		
		String cpMaterial = request.getParameter("tsvVariant");
		cpMaterial = null != cpMaterial ? cpMaterial : "";

		
		String tsvDesc = request.getParameter("tsvDesc");
		tsvDesc = null != tsvDesc ? tsvDesc : "";
		
		UserTo loginUser = PIDBContext.getLoginUser(request);
		
		CpMaterialTo cpMaterialTo = cpMaterialDao.findByCpMaterialNum(cpMaterial);
		cpMaterialTo = null != cpMaterialTo ? cpMaterialTo : new CpMaterialTo();
		
		String cpVariant = cpMaterialTo.getCpVariant();
		
		String tsvMaterialNum = cpMaterialTo.getCpMaterialNum().substring(0, 10)
		+ tsvVersion + cpVariant;


		
		logger.debug("tsvMaterialNum " + tsvMaterialNum);
		/** *****塞入修改日期******** */
		// add update_Time
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
		Calendar rightNow = Calendar.getInstance();


		String updateDay = ( df.format(rightNow.getTime()) );
		/** *****End******** */
		CpTsvMaterialDao tsvDao = new CpTsvMaterialDao();
		CpTsvMaterialTo tsvTo = new CpTsvMaterialTo();
		tsvTo.setCpTsvMaterialNum(tsvMaterialNum);
		tsvTo.setCpTsvVariant(cpVariant);
		tsvTo.setProjectCodeWVersion(projCodeWVersion);
		tsvTo.setRemark(null);
		tsvTo.setDescription(tsvDesc);
		tsvTo.setCreatedBy(loginUser.getUserId());
		tsvTo.setModifiedBy(loginUser.getUserId());
		tsvTo.setMpStatus("N/A");
		tsvTo.setVersion(tsvVersion);
		tsvTo.setUpdateDate(updateDay);

		
		String message = "";
		if (tsvDao.findTsvMaterial(tsvMaterialNum) != null) {
			message = "Crate Fail , already exist";

		} else {
			tsvDao.insertTsvMaterial(tsvTo);
			message = "Crate Success.";

		}

		
		List<CpTsvMaterialTo> cpTsvList = new ArrayList<CpTsvMaterialTo>();
		
		//2.1 Return Value
		
		cpTsvList = tsvDao.findByProjectCode(projCodeWVersion);
		//Get CSP List
		String fundName = "TSV";
		String funFieldName = "VERSION";
		List tsvVersionList = new FunctionParameterDao()
		.findValueList(fundName, funFieldName);
		
		List variantList = new CpMaterialDao()
		.getCpMaterialVariant(projCodeWVersion);
		
		request.setAttribute("tsvVersionList", tsvVersionList);
		request.setAttribute("variantList", variantList);
		request.setAttribute("cpTsvList", cpTsvList);
		request.setAttribute("message", message);
		
		request.setAttribute("projCodeWVersion", projCodeWVersion);

		
		return mapping.findForward(forward);
	}

}
