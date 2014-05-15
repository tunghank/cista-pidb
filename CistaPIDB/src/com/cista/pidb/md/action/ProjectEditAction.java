package com.cista.pidb.md.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import com.cista.pidb.admin.dao.UserDao;
import com.cista.pidb.admin.to.UserTo;
import com.cista.pidb.code.dao.FabCodeDao;
import com.cista.pidb.code.dao.FunctionParameterDao;
import com.cista.pidb.code.dao.SapMasterIcTypeDao;
import com.cista.pidb.code.dao.SapMasterProductFamilyDao;
import com.cista.pidb.core.HttpHelper;
import com.cista.pidb.core.PIDBContext;
import com.cista.pidb.md.dao.BumpMaskDao;
import com.cista.pidb.md.dao.IcFgDao;
import com.cista.pidb.md.dao.IcTapeDao;
import com.cista.pidb.md.dao.IcWaferDao;
import com.cista.pidb.md.dao.ProjectCodeDao;
import com.cista.pidb.md.dao.ProjectDao;
import com.cista.pidb.md.erp.ERPHelper;
import com.cista.pidb.md.erp.IcFgERP;
import com.cista.pidb.md.erp.IcTapeERP;
import com.cista.pidb.md.erp.IcWaferERP;
import com.cista.pidb.md.erp.ProjERP;
import com.cista.pidb.md.erp.ReleaseERPException;
import com.cista.pidb.md.to.BumpMaskTo;
import com.cista.pidb.md.to.IcFgTo;
import com.cista.pidb.md.to.IcTapeTo;
import com.cista.pidb.md.to.IcWaferTo;
import com.cista.pidb.md.to.ProjectTo;

public class ProjectEditAction extends DispatchAction {
	public ActionForward pre(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		String forward = "edit";
		// Prepare for dropdown list
		request.setAttribute("fabCodeList", new FabCodeDao().findAll());
		request.setAttribute("productFamilyList",
				new SapMasterProductFamilyDao().findAll());
		request.setAttribute("icTypeList", new SapMasterIcTypeDao().findAll());
		String fundName = "PROJECT";
		String funFieldName = "PROCESS_TECHNOLOGY";
		request.setAttribute("processTechnology", new FunctionParameterDao()
				.findValueList(fundName, funFieldName));

		String fundName2 = "RELEASE_TO_SAP";
		String funFieldName2 = "RELEASE_TO";
		request.setAttribute("companyNameList", new FunctionParameterDao()
				.findValueList(fundName2, funFieldName2));

		String maskLayers = "MASK_LAYERS";
		String scribeLine = "SCRIBE_LINE";
		String waferThickness = "WAFER_THICKNESS";
		String processingLayers = "PROCESSING_LAYERS";
		String sealring = "SEALRING";
		
		String projectType = "PROJECT_TYPE";

		request.setAttribute("maskLayersList", new FunctionParameterDao()
				.findValueList(fundName, maskLayers));
		request.setAttribute("scribeLineList", new FunctionParameterDao()
				.findValueList(fundName, scribeLine));
		request.setAttribute("waferThicknessList", new FunctionParameterDao()
				.findValueList(fundName, waferThickness));
		request.setAttribute("processingLayersList", new FunctionParameterDao()
		.findValueList(fundName, processingLayers));
		request.setAttribute("sealringList", new FunctionParameterDao()
		.findValueList(fundName, sealring));
		request.setAttribute("projectTypeList", new FunctionParameterDao()
		.findValueList(fundName, projectType));

		String projCode = request.getParameter("projCode");
		if (projCode == null || "".equals(projCode.trim())) {
			projCode = (String) request.getAttribute("projCode");
		}

		ProjectTo ref = new ProjectDao().findByProjectCode(projCode);
		if ("00".equals(ref.getProjOption()) || "".equals(ref.getProjOption())) {
			ref.setProjOption("");
		}
		request.setAttribute("ref", ref);

		return mapping.findForward(forward);
	}

	public ActionForward save(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		String forward = "success";
		final ProjectDao projectDao = new ProjectDao();
		final ProjectTo projectTo = (ProjectTo) HttpHelper.pickupForm(
				ProjectTo.class, request);
		final UserTo loginUser = PIDBContext.getLoginUser(request);
		projectTo.setModifiedBy(loginUser.getUserId());

		if (projectTo.getProjOption().trim().equals("")
				|| projectTo.getProjOption().trim().equals("")) {
			projectTo.setProjOption("00");
		}

		boolean isDraft = false;
		if (projectTo.getFab() == null || projectTo.getFab().equals("")) {
			isDraft = true;
		}
		if (projectTo.getProdFamily() == null
				|| projectTo.getProdFamily().equals("")) {
			isDraft = true;
		}
		if (projectTo.getPanelType() == null
				|| projectTo.getPanelType().equals("")) {
			isDraft = true;
		}
		if (projectTo.getIcType() == null || projectTo.getIcType().equals("")) {
			isDraft = true;
		}
		if (projectTo.getProdLine() == null
				|| projectTo.getProdLine().equals("")) {
			isDraft = true;
		}
		if (!(projectTo.getProdLine() != null && projectTo.getProdLine()
				.equalsIgnoreCase("IP"))) {
			if (projectTo.getProdCodeList() == null
					|| projectTo.getProdCodeList().equals("")) {
				isDraft = true;
			}
		}

		if (isDraft) {
			projectTo.setStatus("Draft");
		} else {
			projectTo.setStatus("Completed");
		}

		String[] prodCodes = request.getParameterValues("prodCodeList");
		String allProdCode = "";
		if (prodCodes != null && prodCodes.length > 0) {
			for (String prodCode : prodCodes) {
				allProdCode += "," + prodCode;
			}
		}

		if (allProdCode != null && allProdCode.length() > 0) {
			projectTo.setProdCodeList(allProdCode.substring(1));
		}

		// Retrieve assignTo email.
		String assigns = "";
		String[] assignTo = request.getParameterValues("assignTo");
		if (assignTo != null) {
			for (String a : assignTo) {
				assigns += "," + a;
			}
		}

		assigns = assigns.length() > 0 ? assigns.substring(1) : assigns;

		UserDao userDao = new UserDao();
		String emails = userDao.fetchEmail(assignTo);
		projectTo.setAssignTo(assigns);
		projectTo.setAssignEmail(emails);

		projectDao.update(projectTo);

		SendMailDispatch.sendMailByModify("MD-1", projectTo.getProjCode(),
				emails, SendMailDispatch.getUrl("MD_1_EDIT", request
						.getContextPath(), projectTo));

		/**
		 * Do release.
		 */
		final String actType = request.getParameter("actType");
		if (actType != null && "release".equals(actType)) {
			TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
				public void doInTransactionWithoutResult(
						final TransactionStatus status) {
					// do release if actType equals 'release'
					String releaseType = request.getParameter("releaseType");
					ProjERP.release(projectTo, loginUser);
					if ("all".equals(releaseType)) {
						// Now use current project code for testing.
						List<String> projCodes = new ProjectCodeDao()
								.findProjCode(projectTo.getProjName());
						for (String pjCode : projCodes) {
							// TODO IC-Wafer release
							IcWaferDao icWaferDao = new IcWaferDao();
							List<IcWaferTo> wafers = icWaferDao
									.findByProjCode(pjCode);

							if (wafers != null && wafers.size() > 0) {
								// Check status
								for (IcWaferTo wafer : wafers) {

									if (!"Released".equals(wafer.getStatus())) {
										continue;
									}

									String result = IcWaferERP.release(wafer,
											loginUser);
									if (result != null) {
										throw new ReleaseERPException(result);
									}

									if (wafer.getRoutingBp()) {
										BumpMaskDao bumpDao = new BumpMaskDao();
										List<BumpMaskTo> bumpToList = bumpDao
												.getByProjCode(wafer
														.getProjCode());

										if (bumpToList != null
												&& bumpToList.size() > 0) {
											result = IcWaferERP.releaseBumping(
													bumpToList.get(0),
													loginUser, wafer);
											if (result != null) {
												throw new ReleaseERPException(
														result);
											}
										}
									}

									// CpMaterialDao cpDao = new
									// CpMaterialDao();
									// if (wafer.getRoutingCp()) {
									// List<CpMaterialTo> cpToList = cpDao
									// .getByProjCodeWVersion(wafer
									// .getProjCodeWVersion());
									// // if (cpToList == null
									// // || cpToList.size() <= 0) {
									// // throw new ReleaseERPException(
									// // "ERP-02-003");
									// // }
									// if (cpToList != null
									// && cpToList.size() > 0) {
									// for (CpMaterialTo cpTo : cpToList) {
									// result = IcWaferERP
									// .releaseCpMaterial(
									// cpTo, loginUser);
									// if (result != null) {
									// throw new ReleaseERPException(
									// result);
									// }
									// }
									// }
									//
									// // List<CpMaterialTo> cpMaterialList =
									// // cpDao.getAll();
									// List<CpMaterialTo> cpMaterialList =
									// cpToList;
									// if (cpMaterialList != null
									// && cpMaterialList.size() > 0) {
									// for (CpMaterialTo cpTo : cpMaterialList)
									// {
									// result = IcWaferERP
									// .releaseDs(
									// cpTo,
									// loginUser,
									// wafer
									// .getProjCodeWVersion());
									// if (result != null) {
									// throw new ReleaseERPException(
									// result);
									// }
									// }
									// }
									// }

								}
							}
							// TODO IC-FG release
							IcFgDao icFgDao = new IcFgDao();
							List<IcFgTo> fgs = icFgDao.findByProjCode(pjCode);
							if (fgs != null && fgs.size() > 0) {
								for (IcFgTo fg : fgs) {
									if (!"Released".equals(fg.getStatus())) {
										continue;
									}

									String result = IcFgERP.releaseForProject(
											fg, loginUser);
									if (result != null) {
										throw new ReleaseERPException(result);
									}
								}
							}

							// TODO IC Tape release
							IcTapeDao icTapeDao = new IcTapeDao();
							// ProjectTo with project code for fetching product
							// code list
							ProjectTo projTo = projectDao
									.findByProjectCode(pjCode);
							String prodCode = projTo.getProdCodeList();
							List<IcTapeTo> tapes = icTapeDao
									.findByProdCodeList(prodCode);
							if (tapes != null && tapes.size() > 0) {
								for (IcTapeTo tape : tapes) {
									if (!"Released".equals(tape.getStatus())) {
										continue;
									}
									String result = IcTapeERP
											.releaseForProject(tape, loginUser);
									if (result != null) {
										throw new ReleaseERPException(result);
									}
								}
							}
						}
					}
				}
			};
			try {
				projectDao.doInTransaction(callback);
				projectTo.setStatus("Released");
				// projectDao.update(projectTo);
			} catch (ReleaseERPException e) {
				request.setAttribute("error", ERPHelper.getErrorMessage(e
						.getMessage()));
				request.setAttribute("ref", projectTo);
				// projectDao.update(projectTo);
				return mapping.findForward("fail");
			}

			projectDao.update(projectTo);

			// send mail
			SendMailDispatch.sendMailByErp("MD-1", projectTo.getProjCode(),
					emails, "");
		} else {
			// projectDao.update(projectTo);
		}

		String mes = "";
		if (actType != null && "release".equals(actType)) {
			mes = "Save and Release to ERP Successfully";
		} else {
			mes = "Save Successfully";
		}

		request.setAttribute("error", mes);
		request.setAttribute("ref", projectTo);
		request.setAttribute("projCode", projectTo.getProjCode());
		return mapping.findForward("fail");
	}

}
