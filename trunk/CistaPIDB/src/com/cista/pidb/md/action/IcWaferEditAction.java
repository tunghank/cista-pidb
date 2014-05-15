package com.cista.pidb.md.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import com.cista.pidb.admin.dao.FunctionDao;
import com.cista.pidb.admin.dao.UserDao;
import com.cista.pidb.admin.to.UserTo;
import com.cista.pidb.ajax.AjaxHelper;
import com.cista.pidb.core.HttpHelper;
import com.cista.pidb.core.PIDBContext;
import com.cista.pidb.md.dao.BumpMaskDao;
import com.cista.pidb.md.dao.ColorFilterMaterialDao;
import com.cista.pidb.md.dao.CpCspMaterialDao;
import com.cista.pidb.md.dao.CpMaterialDao;
import com.cista.pidb.md.dao.CpPolishMaterialDao;
import com.cista.pidb.md.dao.CpTsvMaterialDao;
import com.cista.pidb.md.dao.IcWaferDao;
import com.cista.pidb.md.dao.PidbChangeLogDao;
import com.cista.pidb.md.dao.WaferColorFilterDao;
import com.cista.pidb.md.erp.ERPHelper;
import com.cista.pidb.md.erp.IcWaferERP;
import com.cista.pidb.md.erp.MpListAslERP;
import com.cista.pidb.md.erp.ReleaseERPException;
import com.cista.pidb.md.to.BumpMaskTo;
import com.cista.pidb.md.to.CpCspMaterialTo;
import com.cista.pidb.md.to.CpMaterialTo;
import com.cista.pidb.md.to.IcWaferTo;

public class IcWaferEditAction extends DispatchAction {

	public ActionForward pre(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		String forward = "pre_success";

		IcWaferDao icWaferDao = new IcWaferDao();
		CpMaterialDao cpMaterialDao = new CpMaterialDao();
		//
		String projWVersion = request.getParameter("projCodeWVersion");
		String materialNumber = (String) request.getParameter("materialNum");
		//add by jere
		String rCF = (String) request.getParameter("rCF");
		
		String error = (String) request.getAttribute("error");
		IcWaferTo icWaferTo = icWaferDao.findByPrimaryKey(materialNumber);

		String cspVersion = new CpCspMaterialDao()
				.findVersionByProjwVersion(projWVersion);

		
		if (error != null) {
			request.setAttribute("error", error);
		}
		request.setAttribute("waferCFList", new WaferColorFilterDao()
				.getByProjCodeWVersion(projWVersion));
		request.setAttribute("cfList", new ColorFilterMaterialDao()
				.getByProjCodeWVersion(projWVersion));
		request.setAttribute("polishList", new CpPolishMaterialDao()
				.findByProjectCode(projWVersion));
		request.setAttribute("icWaferTo", icWaferTo);
		request.setAttribute("maskNumList", new IcWaferDao().findMaskNum());
		request.setAttribute("materialList", cpMaterialDao
				.getByProjCodeWVersion(projWVersion));
		request.setAttribute("CFMaterialList", new ColorFilterMaterialDao()
				.getByProjCodeWVersion(projWVersion));
		request.setAttribute("cspMaterialList", new CpCspMaterialDao()
				.findByProjectCode(projWVersion));
		request.setAttribute("tsvMaterialList", new CpTsvMaterialDao()
		.findByProjectCode(projWVersion));
		
		request.setAttribute("cspVersion", cspVersion);
		request.setAttribute("rCF", rCF);

		return mapping.findForward(forward);

	}
	
	public ActionForward save(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {

		String forward = "save_success";
		IcWaferDao icWaferDao = new IcWaferDao();
		IcWaferTo icWaferTo = (IcWaferTo) HttpHelper.pickupForm(
				IcWaferTo.class, request, true);
		// status
		// if (icWaferTo.isRoutingBp() && icWaferTo.isRoutingCp()
		// && icWaferTo.isRoutingWf()) {
		icWaferTo.setStatus("Completed");
		// } else {
		// icWaferTo.setStatus("Draft");
		// }
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
		List<String> emailList = new ArrayList<String>(Arrays.asList(assignTo));
		// emailList
		emailList.add("(R)default_wf_save");

		String emails = userDao.fetchEmail(emailList);
		icWaferTo.setAssignTo(assigns);
		icWaferTo.setAssignEmail(emails);

		//
		UserTo loginUser = PIDBContext.getLoginUser(request);
		icWaferTo.setModifiedBy(loginUser.getUserId());
		icWaferDao.updateIcWafer(icWaferTo);

		//Change Log
		PidbChangeLogDao chlDao = new PidbChangeLogDao();
		chlDao.insertChange("IC_WAFER", "UPDATE", AjaxHelper.bean2String(icWaferTo), loginUser.getUserId());
		
		// Add Hank 2008/01/10 insert Data to PIDB_CP_MATERIAL
		CpMaterialDao cpMaterialDao = new CpMaterialDao();
		CpCspMaterialDao cpCspMaterialDao = new CpCspMaterialDao();
		// String parameterValue = request.getParameter(fieldName);
		// String cpWithVariant = icWaferTo.getCpWithVariant();
		// ColorFilterMaterialDao colorFilterDao = new ColorFilterMaterialDao();
		// WaferColorFilterDao waferCFDao = new WaferColorFilterDao();

		// Find 20090219 CP MATERIAL List
		final List<CpMaterialTo> cpMaterialTo = cpMaterialDao
				.getByProjCodeWVersion(icWaferTo.getProjCodeWVersion());
		
		final List<CpCspMaterialTo> cpCspMaterialTo = cpCspMaterialDao
		.findByProjectCode(icWaferTo.getProjCodeWVersion());

		// Find 20090219 CP MATERIAL List
		/*
		 * final List<ColorFilterMaterialTo> colorFilterTo = colorFilterDao
		 * .getByProjCodeWVersion(icWaferTo.getProjCodeWVersion());
		 * 
		 * final List<WaferColorFilterTo> waferCFTo = waferCFDao
		 * .getByProjCodeWVersion(icWaferTo.getProjCodeWVersion());
		 */

		// send mail
		SendMailDispatch.sendMailByModify("MD-4", icWaferTo
				.getProjCodeWVersion()
				+ "(" + icWaferTo.getMaterialNum() + ")", emails,
				SendMailDispatch.getUrl("MD_4_EDIT", request.getContextPath(),
						icWaferTo));

		final IcWaferTo icWafer = icWaferTo;
		final UserTo user = loginUser;
		// final String finalCpWithVariant = "";
		// Start Release to SAP
		String isRelease = (String) request.getParameter("isRelease");
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
						// result = IcWaferERP.releaseDsNoMp(icWafer, user);
						result = IcWaferERP.releaseDsfICWaferWithCharacter(
								icWafer, user);

						if (result != null) {
							throw new ReleaseERPException(result);
						}

						// insert cp Polish Material
						result = IcWaferERP.releasePolishICWafer(icWafer, user,
								className);
						if (result != null) {
							throw new ReleaseERPException(result);
						}

						// insert Color Filter
						// if (icWafer.getRoutingColorFilter()) {
						// result = IcWaferERP.releaseCpNoMp(icWafer, user);
						result = IcWaferERP.releaseColorFilterICWafer(icWafer,
								user, className);
						if (result != null) {
							throw new ReleaseERPException(result);
						}
						// }

						// insert TurnKey(Wafer + Color Filter)
						// if (icWafer.getRoutingWaferCf()) {
						// result = IcWaferERP.releaseCpNoMp(icWafer, user);
						result = IcWaferERP.releaseWaferCFICWafer(icWafer,
								user, className);
						if (result != null) {
							throw new ReleaseERPException(result);
						}
						// }

						// insert CP
						if (icWafer.getRoutingCp()) {
							// result = IcWaferERP.releaseCpNoMp(icWafer, user);
							result = IcWaferERP.releaseCpfICWaferWithCharacter(
									icWafer, user, className);
							if (result != null) {
								throw new ReleaseERPException(result);
							}
						}
						// insert CP WithVariant
						for (CpMaterialTo cm : cpMaterialTo) {
							final String finalCpWithVariant = cm
									.getCpMaterialNum();
							if (!finalCpWithVariant.equals("")) {

								result = IcWaferERP
										.releaseCpWithVariantICWaferWithCharacter(
												icWafer, user,
												finalCpWithVariant);
								if (result != null) {
									throw new ReleaseERPException(result);
								}
							}
							// insert DS WithVariant
							if (!finalCpWithVariant.equals("")) {

								result = IcWaferERP
										.releaseDsWithVariantICWaferWithCharacter(
												icWafer, user,
												finalCpWithVariant);
								if (result != null) {
									throw new ReleaseERPException(result);
								}
							}
						}
						
						/* insert CSP WithVariant
						for (CpCspMaterialTo cm : cpCspMaterialTo) {
							final String finalCpWithVariant = cm
									.getCpCspMaterialNum();
							if (!finalCpWithVariant.equals("")) {

								result = IcWaferERP
										.releaseCpCspICWafer(
												icWafer, user,
												finalCpWithVariant);
								if (result != null) {
									throw new ReleaseERPException(result);
								}
							}
						}*/
						// insert BP
						if (icWafer.getRoutingBp()) {
							// result = IcWaferERP.releaseBpNoMp(icWafer, user);
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
							// result = IcWaferERP.releaseCpNoMp(icWafer, user);
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

						// insert Color Filter
						// if (icWafer.getRoutingColorFilter()) {
						// result = IcWaferERP.releaseCpNoMp(icWafer, user);
						result = IcWaferERP.releaseColorFilterICWafer(icWafer,
								user, className);
						if (result != null) {
							throw new ReleaseERPException(result);
						}
						// }

						// insert TurnKey(Wafer + Color Filter)
						// if (icWafer.getRoutingWaferCf()) {
						// result = IcWaferERP.releaseCpNoMp(icWafer, user);
						result = IcWaferERP.releaseWaferCFICWafer(icWafer,
								user, className);
						if (result != null) {
							throw new ReleaseERPException(result);
						}
						// }

						for (CpMaterialTo cm : cpMaterialTo) {
							final String finalCpWithVariant = cm
									.getCpMaterialNum();
							// insert CP WithVariant
							if (!finalCpWithVariant.equals("")) {

								result = IcWaferERP
										.releaseCpWithVariantICWaferWithCharacter(
												icWafer, user,
												finalCpWithVariant);
								if (result != null) {
									throw new ReleaseERPException(result);
								}
							}

							// insert DS WithVariant
							if (!finalCpWithVariant.equals("")) {

								result = IcWaferERP
										.releaseDsWithVariantICWaferWithCharacter(
												icWafer, user,
												finalCpWithVariant);
								if (result != null) {
									throw new ReleaseERPException(result);
								}
							}
						}
						
						/* insert CSP WithVariant
						for (CpCspMaterialTo cm : cpCspMaterialTo) {
							final String finalCpWithVariant = cm
									.getCpCspMaterialNum();
							if (!finalCpWithVariant.equals("")) {

								result = IcWaferERP
										.releaseCpCspICWafer(
												icWafer, user,
												finalCpWithVariant);
								if (result != null) {
									throw new ReleaseERPException(result);
								}
							}
						}*/
						
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
			// End Release to SAP

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
		// return mapping.findForward(forward);

	}
}
