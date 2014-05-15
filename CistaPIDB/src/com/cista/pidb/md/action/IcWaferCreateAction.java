package com.cista.pidb.md.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cista.pidb.admin.dao.FunctionDao;
import com.cista.pidb.admin.dao.UserDao;
import com.cista.pidb.admin.to.UserTo;
import com.cista.pidb.ajax.AjaxHelper;
import com.cista.pidb.code.dao.FunctionParameterDao;
import com.cista.pidb.code.to.FunctionParameterTo;
import com.cista.pidb.core.HttpHelper;
import com.cista.pidb.core.PIDBContext;
import com.cista.pidb.md.dao.BumpMaskDao;
import com.cista.pidb.md.dao.ColorFilterMaterialDao;
import com.cista.pidb.md.dao.CpCspMaterialDao;
import com.cista.pidb.md.dao.CpMaterialDao;
import com.cista.pidb.md.dao.CpPolishMaterialDao;
import com.cista.pidb.md.dao.CpTsvMaterialDao;
import com.cista.pidb.md.dao.IcWaferDao;
import com.cista.pidb.md.dao.MpListDao;
import com.cista.pidb.md.dao.PidbChangeLogDao;
import com.cista.pidb.md.dao.ProjectCodeDao;
import com.cista.pidb.md.dao.ProjectDao;
import com.cista.pidb.md.dao.WaferColorFilterDao;
import com.cista.pidb.md.erp.ERPHelper;
import com.cista.pidb.md.erp.IcWaferERP;
import com.cista.pidb.md.erp.MpListAslERP;
import com.cista.pidb.md.erp.ReleaseERPException;
import com.cista.pidb.md.to.BumpMaskTo;
import com.cista.pidb.md.to.ColorFilterMaterialTo;
import com.cista.pidb.md.to.CpCspMaterialTo;
import com.cista.pidb.md.to.CpMaterialTo;
import com.cista.pidb.md.to.CpPolishMaterialTo;
import com.cista.pidb.md.to.CpTsvMaterialTo;
import com.cista.pidb.md.to.IcWaferTo;
import com.cista.pidb.md.to.MpListTo;
import com.cista.pidb.md.to.ProjectCodeTo;
import com.cista.pidb.md.to.ProjectTo;
import com.cista.pidb.md.to.WaferColorFilterTo;

public class IcWaferCreateAction extends DispatchAction {

	protected final Log logger = LogFactory.getLog(getClass());
	public ActionForward pre(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		String forward = "pre_success";
		if (request.getParameter("ref") != null) {
			// Create with reference
			String materialNum = request.getParameter("ref");
			IcWaferTo waferTo = new IcWaferDao().findByPrimaryKey(materialNum);
			request.setAttribute("ref", waferTo);
		}

		String fundName = "CSP";
		String funFieldName = "VERSION";
		List<FunctionParameterTo> cspVersionList = new FunctionParameterDao()
		.findValueList(fundName, funFieldName);
		
		fundName = "TSV";
		funFieldName = "VERSION";
		
		List<FunctionParameterTo> tsvVersionList = new FunctionParameterDao()
		.findValueList(fundName, funFieldName);
		
		request.setAttribute("cspVersionList", cspVersionList);
		request.setAttribute("tsvVersionList", tsvVersionList);
		request.setAttribute("maskNumList", new IcWaferDao().findMaskNum());
		return mapping.findForward(forward);
	}

	public ActionForward save(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		PidbChangeLogDao chlDao = new PidbChangeLogDao();
		
		String forward = "save_success";
		IcWaferTo icWaferTo = (IcWaferTo) HttpHelper.pickupForm(
				IcWaferTo.class, request, true);

		IcWaferDao icWaferDao = new IcWaferDao();

		ProjectDao projectDao = new ProjectDao();

		ProjectCodeDao projectCodeDao = new ProjectCodeDao();

		ProjectCodeTo projCodeTo = projectCodeDao.findByProjectCode(icWaferTo
				.getProjCode());

		String projName = projCodeTo.getProjName();

		ProjectTo projectTo = projectDao.find(projName);

		String isRelease = (String) request.getParameter("isRelease");
		// status

		// if (icWaferTo.isRoutingBp() && icWaferTo.isRoutingCp() &&
		// icWaferTo.isRoutingWf()) {
		icWaferTo.setStatus("Completed");
		// } else {
		// icWaferTo.setStatus("Draft");
		// }
		// mp_Status
		MpListDao mpListDao = new MpListDao();
		MpListTo listTo = mpListDao.findByProjCodeWVersion(icWaferTo
				.getProjCodeWVersion());
		if (listTo == null) {
			icWaferTo.setMpStatus("Non-MP");
		} else {
			icWaferTo.setMpStatus("MP");
		}
		// add multiple select data
		String[] revisionItemList = request.getParameterValues("revisionItem");
		String allRevisionItem = "";
		if (revisionItemList != null && revisionItemList.length > 0) {
			for (String revisionItem : revisionItemList) {
				allRevisionItem += "," + revisionItem;
			}
		}
		if (allRevisionItem != null && allRevisionItem.length() > 0) {
			icWaferTo.setRevisionItem(allRevisionItem.substring(1));
		}
		// assignEmail
		String assigns = "";
		String[] assignTo = request.getParameterValues("assignTo");
		if (assignTo != null) {
			for (String a : assignTo) {
				assigns += "," + a;
			}
		} else {
			assignTo = new String[] {};
		}

		assigns = assigns.length() > 0 ? assigns.substring(1) : assigns;

		UserDao userDao = new UserDao();
		// fetch SAVE emails
		List<String> emailList = new ArrayList<String>(Arrays.asList(assignTo));
		emailList.add("(R)default_wf_save");

		String emails = userDao.fetchEmail(emailList);
		icWaferTo.setAssignTo(assigns);
		icWaferTo.setAssignEmail(emails);

		//
		UserTo loginUser = PIDBContext.getLoginUser(request);
		icWaferTo.setCreatedBy(loginUser.getUserId());
		icWaferTo.setModifiedBy(loginUser.getUserId());

		// insert cp,bp,ds
		icWaferTo.setCp("C" + icWaferTo.getMaterialNum().substring(1));
		icWaferTo.setBp("B" + icWaferTo.getMaterialNum().substring(1));
		icWaferTo.setDs("D" + icWaferTo.getMaterialNum().substring(1));

		// add update_Time
		Calendar rightNow = Calendar.getInstance();
		int y = rightNow.get(Calendar.YEAR);
		int mon = rightNow.get(Calendar.MONTH) + 1;
		int d = rightNow.get(Calendar.DAY_OF_MONTH);
		int h = rightNow.get(Calendar.HOUR_OF_DAY);
		int min = rightNow.get(Calendar.MINUTE);
		int secend = rightNow.get(Calendar.SECOND);

		String year = String.valueOf(y);
		String month = String.valueOf(mon);
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

		/**
		 * ******************insert to Polish Table********************
		 */

		if (icWaferTo.isRoutingPolish() != false) {
			CpPolishMaterialDao polishDao = new CpPolishMaterialDao();
			CpPolishMaterialTo polishTo = new CpPolishMaterialTo();
			String polishMaterial = icWaferTo.getCp().substring(0, 10)
					+ icWaferTo.getCp().substring(10, 11).replace("0", "P")
					+ "0" + icWaferTo.getVariant();
			polishTo.setCpPolishMaterialNum(polishMaterial);
			polishTo.setProjectCodeWVersion(icWaferTo.getProjCodeWVersion());
			polishTo.setDescription(icWaferTo.getMaterialDesc());
			polishTo.setRemark(icWaferTo.getRemark());
			polishTo.setCreatedBy(loginUser.getUserId());
			polishTo.setModifiedBy(loginUser.getUserId());
			polishTo.setMpStatus(icWaferTo.getMpStatus());
			polishTo.setCpPolishVariant(icWaferTo.getVariant());

			polishTo.setUpdateDate(updateDay);
			if (polishDao.findPolishMaterial(polishMaterial) == null) {
				polishDao.insertPolishMaterial(polishTo);
				//Change Log
				chlDao.insertChange("IC_WAFER_POLISH", "INSET", AjaxHelper.bean2String(polishTo), loginUser.getUserId());
			}

		}
		/** *************************************************************************** */

		/** *****************insert to CpMaterial Table******************* */

		if (icWaferTo.isRoutingCp() != false) {
			CpMaterialDao cpMaterialDao = new CpMaterialDao();
			CpMaterialTo cpMaterialTo = new CpMaterialTo();
			cpMaterialTo.setCpVariant(icWaferTo.getVariant());
			cpMaterialTo.setDescription(icWaferTo.getMaterialDesc());
			cpMaterialTo
					.setProjectCodeWVersion(icWaferTo.getProjCodeWVersion());
			cpMaterialTo.setRemark(icWaferTo.getRemark());
			cpMaterialTo.setCpTestProgramNameList("N/A");
			cpMaterialTo.setCreatedBy(loginUser.getUserId());
			cpMaterialTo.setModifiedBy(loginUser.getUserId());
			cpMaterialTo.setUpdateDate(updateDay);
			String material = "C" + icWaferTo.getMaterialNum().substring(1, 13);
			cpMaterialTo.setCpMaterialNum(material);
			if (cpMaterialDao.findByCpMaterialNum(material) == null) {
				cpMaterialDao.insert(cpMaterialTo, "PIDB_CP_MATERIAL");
				//Change Log
				chlDao.insertChange("IC_WAFER_CP", "INSET", AjaxHelper.bean2String(cpMaterialTo), loginUser.getUserId());
			}
		}

		/** *************************************************************************** */

		/** *****************insert to Color Filter Table******************* */

		/*if (icWaferTo.isRoutingColorFilter() != false) {
			ColorFilterMaterialDao cfDao = new ColorFilterMaterialDao();
			ColorFilterMaterialTo cfTo = new ColorFilterMaterialTo();
			cfTo.setColorFilterVariant(icWaferTo.getVariant());
			cfTo.setDescription(icWaferTo.getMaterialDesc());
			cfTo.setProjectCodeWVersion(icWaferTo.getProjCodeWVersion());
			cfTo.setRemark(icWaferTo.getRemark());
			cfTo.setCreatedBy(loginUser.getUserId());
			cfTo.setModifiedBy(loginUser.getUserId());
			cfTo.setUpdateDate(updateDay);
			
			cfTo.setSLayer("NA");
			cfTo.setLightPipe("NA");
			cfTo.setLightPipe("NA");
			cfTo.setRbgThickness("NA");
			cfTo.setMlThickness("NA");
			cfTo.setMlType("NA");
			cfTo.setProjCode(icWaferTo.getProjCode());
			
			String material = "M" + icWaferTo.getMaterialNum().substring(1, 13);
			cfTo.setColorFilterMaterialNum(material);
			if (cfDao.findByCpMaterialNum(material) == null) {
				cfDao.insert(cfTo);
				//Change Log
				chlDao.insertChange("IC_WAFER_COLOR_FILTER", "INSET", AjaxHelper.bean2String(cfTo), loginUser.getUserId());
			}
		}*/

		/** *************************************************************************** */

		/**
		 * *****************insert to Wafer + Color Filter
		 * Table*******************
		 */

		if (icWaferTo.isRoutingWaferCf() != false) {
			WaferColorFilterDao turnKeyDao = new WaferColorFilterDao();
			WaferColorFilterTo turnKeyTo = new WaferColorFilterTo();
			turnKeyTo.setWaferCfVariant(icWaferTo.getVariant());
			turnKeyTo.setDescription(icWaferTo.getMaterialDesc());
			turnKeyTo.setProjectCodeWVersion(icWaferTo.getProjCodeWVersion());
			turnKeyTo.setRemark(icWaferTo.getRemark());
			turnKeyTo.setCreatedBy(loginUser.getUserId());
			turnKeyTo.setModifiedBy(loginUser.getUserId());
			turnKeyTo.setUpdateDate(updateDay);
			String material = "T" + icWaferTo.getMaterialNum().substring(1, 13);
			turnKeyTo.setWaferCfMaterialNum(material);
			if (turnKeyDao.findByCpMaterialNum(material) == null) {
				turnKeyDao.insert(turnKeyTo, "PIDB_WAFER_CF_MATERIAL");
				//Change Log
				chlDao.insertChange("IC_WAFER_CF", "INSET", AjaxHelper.bean2String(turnKeyTo), loginUser.getUserId());
			}
		}

		/** *************************************************************************** */

		/**
		 * ******************insert to CP CSP Table********************
		 */

		if (icWaferTo.isRoutingCsp() != false) {
			CpCspMaterialDao cspDao = new CpCspMaterialDao();
			CpCspMaterialTo cspTo = new CpCspMaterialTo();
			String cspVersion = request.getParameter("cspVersion");
			cspVersion =null!=cspVersion?cspVersion:"";
			
			String cspMaterial = icWaferTo.getCp().substring(0, 10)
					+ cspVersion + icWaferTo.getVariant();
			logger.debug("cspMaterial " + cspMaterial);
			
			cspTo.setCpCspMaterialNum(cspMaterial);
			cspTo.setProjectCodeWVersion(icWaferTo.getProjCodeWVersion());
			cspTo.setDescription(icWaferTo.getMaterialDesc());
			cspTo.setRemark(null);
			cspTo.setCreatedBy(loginUser.getUserId());
			cspTo.setModifiedBy(loginUser.getUserId());
			cspTo.setMpStatus(icWaferTo.getMpStatus());
			cspTo.setCpCspVariant(icWaferTo.getVariant());
			cspTo.setVersion(cspVersion);
			cspTo.setUpdateDate(updateDay);
			if (cspDao.findCspMaterial(cspMaterial) == null) {
				cspDao.insertCspMaterial(cspTo);
				//Change Log
				chlDao.insertChange("IC_WAFER_CSP", "INSET", cspTo.toString(), loginUser.getUserId());
			}
		}

		/** *******************************************************************/

		/**
		 * ******************insert to CP TSV Table********************
		 */

		if (icWaferTo.isRoutingTsv() != false) {
			logger.debug("TSV True");
			
			CpTsvMaterialDao tsvDao = new CpTsvMaterialDao();
			CpTsvMaterialTo tsvTo = new CpTsvMaterialTo();
			String tsvVersion = request.getParameter("tsvVersion");
			tsvVersion =null!=tsvVersion?tsvVersion:"";
			
			String tsvMaterial = icWaferTo.getCp().substring(0, 10)
					+ tsvVersion + icWaferTo.getVariant();
			logger.debug("tsvMaterial " + tsvMaterial);
			
			tsvTo.setCpTsvMaterialNum(tsvMaterial);
			tsvTo.setProjectCodeWVersion(icWaferTo.getProjCodeWVersion());
			tsvTo.setDescription(icWaferTo.getMaterialDesc());
			tsvTo.setRemark(null);
			tsvTo.setCreatedBy(loginUser.getUserId());
			tsvTo.setModifiedBy(loginUser.getUserId());
			tsvTo.setMpStatus(icWaferTo.getMpStatus());
			tsvTo.setCpTsvVariant(icWaferTo.getVariant());
			tsvTo.setVersion(tsvVersion);
			tsvTo.setUpdateDate(updateDay);
			if (tsvDao.findTsvMaterial(tsvMaterial) == null) {
				tsvDao.insertTsvMaterial(tsvTo);
				//Change Log
				chlDao.insertChange("IC_WAFER_TSV", "INSET", tsvTo.toString(), loginUser.getUserId());
			}
		}else{
			logger.debug("TSV false");
		}

		/** *******************************************************************/		
		
		
		IcWaferTo icWaferToTemp = icWaferDao.findMaterialDesc(icWaferTo
				.getMaterialNum(), icWaferTo.getProjCodeWVersion());
		if (icWaferToTemp == null) {
			icWaferDao.insertIcWafer(icWaferTo);
			//Change Log
			chlDao.insertChange("IC_WAFER", "INSET", AjaxHelper.bean2String(icWaferTo), loginUser.getUserId());
		}
		/*
		 * generateToken(request); saveToken(request); } else {
		 * System.out.println("projWVersion= " +
		 * icWaferTo.getProjCodeWVersion()); System.out.println("materialNum= " +
		 * materialTemp); resetToken(request); return
		 * mapping.findForward("release_fail"); }
		 */

		// send mail
		SendMailDispatch.sendMailByCreate("MD-4", icWaferTo
				.getProjCodeWVersion()
				+ "(" + icWaferTo.getMaterialNum() + ")", emails,
				SendMailDispatch.getUrl("MD_4_EDIT", request.getContextPath(),
						icWaferTo));

		if (isRelease.equals("1")) {
			if (projectTo.getProdFamily() == null
					|| projectTo.getProdFamily().length() <= 0) {
				request.setAttribute("error", ERPHelper
						.getErrorMessage("ERP-02-001"));
				request.setAttribute("materiaNum", icWaferTo.getMaterialNum());
				request.setAttribute("codeW", icWaferTo.getProjCodeWVersion());
				return mapping.findForward("release_fail");
			}
		}

		final IcWaferTo icWafer = icWaferTo;
		final UserTo user = loginUser;
		final String className = this.getClass().getName();

		if (isRelease.equals("1")) {
			TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
				public void doInTransactionWithoutResult(
						final TransactionStatus status) {
					String result = IcWaferERP.release(icWafer, user);
					if (result != null) {
						throw new ReleaseERPException(result);
					}

					if (icWafer.getMpStatus() != null
							&& icWafer.getMpStatus().equalsIgnoreCase("Non-MP")) {
						// insert DS
						// CpMaterialDao cpDao = new CpMaterialDao();
						// List<CpMaterialTo> cpToList = cpDao
						// .getByProjCodeWVersion(icWafer
						// .getProjCodeWVersion());
						//                        
						// List<CpMaterialTo> cpMaterialList = cpToList;
						// if (cpMaterialList != null &&
						// cpMaterialList.size() >
						// 0) {
						// for (CpMaterialTo cpTo : cpMaterialList) {
						// result = IcWaferERP.releaseDs(cpTo, user,
						// icWafer.getProjCodeWVersion(), true);
						// if (result != null) {
						// throw new ReleaseERPException(result);
						// }
						// }
						// }
						// result = IcWaferERP.releaseDsNoMp(icWafer,user);
						result = IcWaferERP.releaseDsfICWaferWithCharacter(
								icWafer, user);
						if (result != null) {
							throw new ReleaseERPException(result);
						}

						// insert CP
						if (icWafer.getRoutingCp()) {
							// result =
							// IcWaferERP.releaseCpNoMp(icWafer,user);
							result = IcWaferERP.releaseCpfICWaferWithCharacter(
									icWafer, user, className);
							if (result != null) {
								throw new ReleaseERPException(result);
							}
						}

						// insert cp Polish Material
						result = IcWaferERP.releasePolishICWafer(icWafer, user,
								className);
						if (result != null) {
							throw new ReleaseERPException(result);
						}

						// insert BP
						if (icWafer.getRoutingBp()) {
							// result = IcWaferERP.releaseBpNoMp(icWafer,
							// user);
							result = IcWaferERP.releaseBpfICWaferWithCharacter(
									icWafer, user);
							if (result != null) {
								throw new ReleaseERPException(result);
							}
						}

					} else {
						// insert DS
						result = IcWaferERP.releaseDsfICWaferWithCharacter(
								icWafer, user);
						if (result != null) {
							throw new ReleaseERPException(result);
						}
						// insert CP
						if (icWafer.getRoutingCp()) {
							// result = IcWaferERP.releaseCpNoMp(icWafer,
							// user);
							result = IcWaferERP.releaseCpfICWaferWithCharacter(
									icWafer, user, className);
							if (result != null) {
								throw new ReleaseERPException(result);
							}
						}

						// insert cp Polish Material
						result = IcWaferERP.releasePolishICWafer(icWafer, user,
								className);
						if (result != null) {
							throw new ReleaseERPException(result);
						}

						// insert BP
						if (icWafer.getRoutingBp()) {
							BumpMaskDao bumpDao = new BumpMaskDao();
							List<BumpMaskTo> bumpToList = bumpDao
									.getByProjCode(icWafer.getProjCode());

							if (bumpToList != null && bumpToList.size() > 0) {
								result = IcWaferERP.releaseBumping(bumpToList
										.get(0), user, icWafer);
								if (result != null) {
									throw new ReleaseERPException(result);
								}
							}
						}
					}
					
					// Release CSP to SAP
					if (icWafer.getRoutingCsp()) {
						// result = IcWaferERP.releaseCpNoMp(icWafer, user);
						result = IcWaferERP.releaseCpCspICWafer(
								icWafer, user, className);
						if (result != null) {
							throw new ReleaseERPException(result);
						}
					}
					// Release TSV to SAP
					if (icWafer.getRoutingTsv()) {
						// result = IcWaferERP.releaseCpNoMp(icWafer, user);
						result = IcWaferERP.releaseCpTsvICWafer(
								icWafer, user, className);
						if (result != null) {
							throw new ReleaseERPException(result);
						}
					}
					
					/*
					 * 3.1 2008/02/27 Added Hank Release ASL List to SAP ERP
					 */

					// ****** 3.1 Start Release ASL List to SAP ERP******//
					if (icWafer != null) {
						result = MpListAslERP.releaseFormICWafer(icWafer, user);
						if (result != null) {
							throw new ReleaseERPException(result);
						}
					}
					// ****** 3.1 End Release ASL List to SAP ERP ******//

				}
			};

			try {
				new FunctionDao().doInTransaction(callback);
			} catch (ReleaseERPException e) {
				request.setAttribute("error", ERPHelper.getErrorMessage(e
						.getMessage()));
				request.setAttribute("materiaNum", icWaferTo.getMaterialNum());
				request.setAttribute("codeW", icWaferTo.getProjCodeWVersion());
				return mapping.findForward("release_fail");
			}

			// send mail
			emailList.remove("(R)default_wf_save");
			emailList.add("(R)default_wf_erp");
			emails = userDao.fetchEmail(emailList);

			SendMailDispatch.sendMailByErp("MD-4", " : " + icWaferTo
					.getProjCodeWVersion()
					+ "(" + icWaferTo.getMaterialNum() + ")", emails, "");

			// update status Released
			icWaferTo.setStatus("Released");
			/*
			 * Map<String, Object> m = new HashMap<String, Object>();
			 * m.put("MATERIAL_NUM", icWaferTo.getMaterialNum());
			 */
			icWaferDao.updateIcWafer(icWaferTo);

		}

		String mes = "";
		if (isRelease.equals("1")) {
			mes = "Save and Release to ERP Successfully";
		} else {
			mes = "Save Successfully";
		}

		request.setAttribute("error", mes);
		request.setAttribute("materiaNum", icWaferTo.getMaterialNum());
		request.setAttribute("codeW", icWaferTo.getProjCodeWVersion());

		return mapping.findForward("release_fail");
	}
}
