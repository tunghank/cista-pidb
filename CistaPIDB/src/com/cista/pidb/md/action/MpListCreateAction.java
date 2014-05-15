package com.cista.pidb.md.action;
/*  @author Hu Meixia
 *  2010/03/16 FCG1  by jere : 新增回傳值  
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.cista.pidb.code.dao.FunctionParameterDao;
import com.cista.pidb.core.HttpHelper;
import com.cista.pidb.core.PIDBContext;
import com.cista.pidb.md.dao.BumpMaskDao;
import com.cista.pidb.md.dao.IcFgDao;
import com.cista.pidb.md.dao.IcTapeDao;
import com.cista.pidb.md.dao.IcWaferDao;
import com.cista.pidb.md.dao.MpListDao;
import com.cista.pidb.md.dao.PidbChangeLogDao;
import com.cista.pidb.md.erp.ERPHelper;
import com.cista.pidb.md.erp.IcFgERP;
import com.cista.pidb.md.erp.IcTapeERP;
import com.cista.pidb.md.erp.IcWaferERP;
import com.cista.pidb.md.erp.MpListAplERP;
import com.cista.pidb.md.erp.MpListAslERP;
import com.cista.pidb.md.erp.ReleaseERPException;
import com.cista.pidb.md.to.BumpMaskTo;
import com.cista.pidb.md.to.IcFgTo;
import com.cista.pidb.md.to.IcTapeTo;
import com.cista.pidb.md.to.IcWaferTo;
import com.cista.pidb.md.to.MpListTo;


public class MpListCreateAction extends DispatchAction {
	/**
	 * .
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
	 */
	public ActionForward pre(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		String forward = "viewJsp";

		if (request.getParameter("ref") != null) {
			// Create with reference
			String key = request.getParameter("ref");
			if (key.indexOf(",") >= 0) {
				String[] keys = key.split(",");
				if (keys != null && keys.length == 5) {
					request.setAttribute("ref", new MpListDao()
							.findByPrimaryKey(keys[0], keys[1], keys[2],
									keys[3], keys[4]));
				}
			}
		}
		//FCG1
		FunctionParameterDao funcParmDao = new FunctionParameterDao();
		//For Tray check condtion
		request.setAttribute("trayCustAlarmMapList", funcParmDao.findValueList(
				"MP_LIST", "Tray_Customer_Alarm" ) );

		return mapping.findForward(forward);
	}

	/**
	 * .
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
	 */
	public ActionForward save(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		String forward = "success";
		MpListDao mpListDao = new MpListDao();
		MpListTo mpListTo = (MpListTo) HttpHelper.pickupForm(MpListTo.class,
				request);
		UserTo loginUser = PIDBContext.getLoginUser(request);
		mpListTo.setCreatedBy(loginUser.getUserId());

		// add assignTo and assignEmail
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
		// emailList.add("(R)default_MP_announce");

		String emails = userDao.fetchEmail(emailList);
		mpListTo.setAssignTo(assigns);
		mpListTo.setAssignEmail(emails);

		String[] appList = request.getParameterValues("approveCust");
		String allApp = "";
		if (appList != null && appList.length > 0) {
			for (String app : appList) {
				allApp += "," + app;
			}
		}

		if (allApp != null && allApp.length() > 0) {
			mpListTo.setApproveCust(allApp + ",");
		}

		appList = request.getParameterValues("approveTapeVendor");
		allApp = "";
		if (appList != null && appList.length > 0) {
			for (String app : appList) {
				allApp += "," + app;
			}
		}

		if (allApp != null && allApp.length() > 0) {
			mpListTo.setApproveTapeVendor(allApp + ",");
		}

		appList = request.getParameterValues("approveBpVendor");
		allApp = "";
		if (appList != null && appList.length > 0) {
			for (String app : appList) {
				allApp += "," + app;
			}
		}

		if (allApp != null && allApp.length() > 0) {
			mpListTo.setApproveBpVendor(allApp + ",");
		}

		appList = request.getParameterValues("approveCpHouse");
		allApp = "";
		if (appList != null && appList.length > 0) {
			for (String app : appList) {
				allApp += "," + app;
			}
		}

		if (allApp != null && allApp.length() > 0) {
			mpListTo.setApproveCpHouse(allApp + ",");
		}

		appList = request.getParameterValues("approveAssyHouse");
		allApp = "";
		if (appList != null && appList.length > 0) {
			for (String app : appList) {
				allApp += "," + app;
			}
		}

		if (allApp != null && allApp.length() > 0) {
			mpListTo.setApproveAssyHouse(allApp + ",");
		}

		appList = request.getParameterValues("approveFtHouse");
		allApp = "";
		if (appList != null && appList.length > 0) {
			for (String app : appList) {
				allApp += "," + app;
			}
		}

		if (allApp != null && allApp.length() > 0) {
			mpListTo.setApproveFtHouse(allApp + ",");
		}

		// polish Vendor
		appList = request.getParameterValues("approvePolishVendor");
		allApp = "";
		if (appList != null && appList.length > 0) {
			for (String app : appList) {
				allApp += "," + app;
			}
		}

		if (allApp != null && allApp.length() > 0) {
			mpListTo.setApprovePolishVendor(allApp + ",");
		}

		// Color Filter Vendor
		appList = request.getParameterValues("approveColorFilterVendor");
		allApp = "";
		if (appList != null && appList.length > 0) {
			for (String app : appList) {
				allApp += "," + app;
			}
		}

		if (allApp != null && allApp.length() > 0) {
			mpListTo.setApproveColorFilterVendor(allApp + ",");
		}

		// TurnKey(Wafer + Color Filter) Vendor
		appList = request.getParameterValues("approveWaferCfVendor");
		allApp = "";
		if (appList != null && appList.length > 0) {
			for (String app : appList) {
				allApp += "," + app;
			}
		}

		if (allApp != null && allApp.length() > 0) {
			mpListTo.setApproveWaferCfVendor(allApp + ",");
		}
		
		// csp Vendor
		appList = request.getParameterValues("approveCpCspVendor");
		allApp = "";
		if (appList != null && appList.length > 0) {
			for (String app : appList) {
				allApp += "," + app;
			}
		}

		if (allApp != null && allApp.length() > 0) {
			mpListTo.setApproveCpCspVendor(allApp + ",");
		}
		
		// TSV Vendor
		appList = request.getParameterValues("approveCpTsvVendor");
		allApp = "";
		if (appList != null && appList.length > 0) {
			for (String app : appList) {
				allApp += "," + app;
			}
		}

		if (allApp != null && allApp.length() > 0) {
			mpListTo.setApproveCpTsvVendor(allApp + ",");
		}

		appList = request.getParameterValues("eolCust");
		allApp = "";
		if (appList != null && appList.length > 0) {
			for (String app : appList) {
				allApp += "," + app;
			}
		}

		if (allApp != null && allApp.length() > 0) {
			mpListTo.setEolCust(allApp + ",");
		}

		mpListDao.insert(mpListTo);
		//Change Log
		PidbChangeLogDao chlDao = new PidbChangeLogDao();
		chlDao.insertChange("MP_LIST", "INSET", AjaxHelper.bean2String(mpListTo), loginUser.getUserId());
		
		// ****************Vendor Update Start****************//
		/*
		 * 2.0 2008/01/11 Added Hank Update Vendor List For Vendor Change By
		 * Project Code
		 */
		// 2.1 Bumping Vendor Update
		// 2.1.1 Find Bump Update List
		String projCode = mpListTo.getProjCode();
		projCode = null != projCode ? projCode : "";
		String newBump = mpListTo.getApproveBpVendor();
		newBump = null != newBump ? newBump : "";
		if (!projCode.equals("")) {
			projCode = projCode.substring(0, 6);
		}
		List<MpListTo> upBumpMpListToList = mpListDao.findByProjCode(projCode);
		if (upBumpMpListToList != null && upBumpMpListToList.size() > 0) {
			for (MpListTo upBumpMpListTo : upBumpMpListToList) {
				upBumpMpListTo.setApproveBpVendor(newBump);
				Map<String, Object> keyBump = new HashMap<String, Object>();
				keyBump.put("IC_FG_MATERIAL_NUM", upBumpMpListTo
						.getIcFgMaterialNum());
				keyBump.put("PROJ_CODE_W_VERSION", upBumpMpListTo
						.getProjCodeWVersion());
				keyBump.put("TAPE_NAME", upBumpMpListTo.getTapeName());
				keyBump.put("PKG_CODE", upBumpMpListTo.getPkgCode());
				mpListDao.update(upBumpMpListTo);
				//Change Log
				chlDao.insertChange("MP_LIST", "UPDATE", AjaxHelper.bean2String(upBumpMpListTo), loginUser.getUserId());
			}
		}
		// 2.2 CP Vendor Update
		// 2.2.1 Find CP Update List
		String prodCode = mpListTo.getProdCode();
		prodCode = null != prodCode ? prodCode : "";
		String newCP = mpListTo.getApproveCpHouse();
		newCP = null != newCP ? newCP : "";
		List<MpListTo> upCpMpListToList;

		if (!prodCode.equals("") && prodCode.length() >= 8) {
			prodCode = prodCode.substring(0, 8);
			upCpMpListToList = mpListDao.findByProdCodeGreaterThan8(prodCode);
		} else {
			upCpMpListToList = mpListDao.findByProdCode(prodCode);
		}

		if (upCpMpListToList != null && upCpMpListToList.size() > 0) {
			for (MpListTo upCpMpListTo : upCpMpListToList) {
				upCpMpListTo.setApproveCpHouse(mpListTo.getApproveCpHouse());
				Map<String, Object> keyCP = new HashMap<String, Object>();
				keyCP.put("IC_FG_MATERIAL_NUM", upCpMpListTo
						.getIcFgMaterialNum());
				keyCP.put("PROJ_CODE_W_VERSION", upCpMpListTo
						.getProjCodeWVersion());
				keyCP.put("TAPE_NAME", upCpMpListTo.getTapeName());
				keyCP.put("PKG_CODE", upCpMpListTo.getPkgCode());
				mpListDao.update(upCpMpListTo);
				//Change Log
				chlDao.insertChange("MP_LIST", "UPDATE", AjaxHelper.bean2String(upCpMpListTo), loginUser.getUserId());
			}

		}

		// 2.3 Assy&FT Vendor Update
		// 2.3.1 Find Assy Update List
		// 2.3.1.1 Get PKG_CODE
		IcFgDao icFgDao = new IcFgDao();

		String icFgMaterialNum = mpListTo.getIcFgMaterialNum();
		icFgMaterialNum = null != icFgMaterialNum ? icFgMaterialNum : "";

		String partNum = mpListTo.getPartNum();
		partNum = null != partNum ? partNum : "";

		IcFgTo icFgToMaterial = icFgDao.findByMaterialNum(icFgMaterialNum);
		String pkgType = "";
		List<MpListTo> upAssyMpListToList;
		if (icFgToMaterial != null) {
			pkgType = icFgToMaterial.getPkgType();
			if (pkgType.equals("301") || pkgType.equals("302")) {
				String assyTapeName = mpListTo.getTapeName();
				assyTapeName = null != assyTapeName ? assyTapeName : "";
				upAssyMpListToList = mpListDao.findByTapeName(assyTapeName);
				/*
				 * }else if (pkgType.equals("303") ){ if( !prodCode.equals("") &&
				 * prodCode.length() >=8 ){ prodCode = prodCode.substring(0,8);
				 * upAssyMpListToList =
				 * mpListDao.findByProdCodeGreaterThan8(prodCode); }else{
				 * upAssyMpListToList = mpListDao.findByProdCode(prodCode); }
				 */
			} else if (pkgType.equals("304") || pkgType.equals("303")) {
				upAssyMpListToList = mpListDao.findByPartNum(partNum);
			} else {
				upAssyMpListToList = null;
			}
			if (upAssyMpListToList != null && upAssyMpListToList.size() > 0) {
				for (MpListTo upAssyMpListTo : upAssyMpListToList) {
					upAssyMpListTo.setApproveAssyHouse(mpListTo
							.getApproveAssyHouse());
					upAssyMpListTo.setApproveFtHouse(mpListTo
							.getApproveFtHouse());
					Map<String, Object> keyAssy = new HashMap<String, Object>();
					keyAssy.put("IC_FG_MATERIAL_NUM", upAssyMpListTo
							.getIcFgMaterialNum());
					keyAssy.put("PROJ_CODE_W_VERSION", upAssyMpListTo
							.getProjCodeWVersion());
					keyAssy.put("TAPE_NAME", upAssyMpListTo.getTapeName());
					keyAssy.put("PKG_CODE", upAssyMpListTo.getPkgCode());
					mpListDao.update(upAssyMpListTo);
					//Change Log
					chlDao.insertChange("MP_LIST", "UPDATE", AjaxHelper.bean2String(upAssyMpListTo), loginUser.getUserId());
				}

			}
		}

		// 2.4 Tape Vendor Update
		// 2.4.1 Find Tape Update List
		String upTapeName = mpListTo.getTapeName();
		upTapeName = null != upTapeName ? upTapeName : "";

		List<MpListTo> upTapeMpListToList;
		if (!upTapeName.trim().equals("NA")) {
			upTapeMpListToList = mpListDao.findByTapeName(upTapeName);
		} else {
			upTapeMpListToList = null;
		}

		if (upTapeMpListToList != null && upTapeMpListToList.size() > 0) {
			for (MpListTo upTapeMpListTo : upTapeMpListToList) {
				upTapeMpListTo.setApproveTapeVendor(mpListTo
						.getApproveTapeVendor());
				Map<String, Object> keyCP = new HashMap<String, Object>();
				keyCP.put("IC_FG_MATERIAL_NUM", upTapeMpListTo
						.getIcFgMaterialNum());
				keyCP.put("PROJ_CODE_W_VERSION", upTapeMpListTo
						.getProjCodeWVersion());
				keyCP.put("TAPE_NAME", upTapeMpListTo.getTapeName());
				keyCP.put("PKG_CODE", upTapeMpListTo.getPkgCode());
				mpListDao.update(upTapeMpListTo);
				//Change Log
				chlDao.insertChange("MP_LIST", "UPDATE", AjaxHelper.bean2String(upTapeMpListTo), loginUser.getUserId());
			}

		}
		// ****************Vendor Update End****************//

		// update IC Wafer
		IcWaferDao icWaferDao = new IcWaferDao();
		IcWaferTo tempWafer = null;
		List<IcWaferTo> icWaferToList = icWaferDao
				.findListByProjCodeWVersion(mpListTo.getProjCodeWVersion());
		if (icWaferToList != null && icWaferToList.size() > 0) {
			for (IcWaferTo to : icWaferToList) {
				/*
				 * Map<String, Object> tempMap = new HashMap<String,
				 * Object>(); tempMap.put("MATERIAL_NUM", to.getMaterialNum());
				 */

				to.setMpStatus("MP");
				icWaferDao.updateIcWafer(to);
			}
			tempWafer = icWaferToList.get(0);
		}

		// update IC Fg

		IcFgTo icFgTo = icFgDao
				.findByMaterialNum(mpListTo.getIcFgMaterialNum());
		if (icFgTo != null) {
			Map tempMap = new HashMap();
			tempMap.put("MATERIAL_NUM", icFgTo.getMaterialNum());

			icFgTo.setMpStatus("MP");
			icFgDao.update(icFgTo);

		}

		// send mail
		SendMailDispatch.sendMailByCreate("MD-13", mpListTo.getPartNum() + "("
				+ mpListTo.getIcFgMaterialNum() + ")", emails, SendMailDispatch
				.getUrl("MD_13_EDIT", request.getContextPath(), mpListTo));

		String subject = PIDBContext.getConfig("MD-13") + "  "
				+ mpListTo.getPartNum() + "(" + mpListTo.getIcFgMaterialNum()
				+ ")" + "  已完成儲存,請相關人員維護相關資料";
		String text = "請依以下Link進入維護 "
				+ SendMailDispatch.getUrl("MD_13_EDIT", request
						.getContextPath(), mpListTo);
		String defaultEmail = userDao
				.fetchEmail(new String[] { "(R)default_MP_announce" });
		SendMailDispatch.sendMailDefault(subject, text, defaultEmail);

		/*
		 * 3.0 2008/01/14 Added Hank Release to SAP ERP
		 */

		// 3.1 Release to SAP ERP
		// IcWaferDao icWaferDao = new IcWaferDao();
		String matWf = mpListTo.getMatWf();
		matWf = null != matWf ? matWf : "";
		IcWaferTo icWaferTo = icWaferDao.findByPrimaryKey(matWf);
		final IcWaferTo icWafer = icWaferTo;
		final UserTo user = loginUser;

		IcFgTo icFgErpTo = icFgDao.findByMaterialNum(icFgMaterialNum);
		final IcFgTo icFgErp = icFgErpTo;

		IcTapeDao icTapeDao = new IcTapeDao();
		String matTape = mpListTo.getMatTape();
		matTape = null != matTape ? matTape : "";
		String matTapeName = mpListTo.getTapeName();
		matTapeName = null != matTapeName ? matTapeName : "";

		IcTapeTo icTapeTo = icTapeDao.findByPrimaryKey(matTape, matTapeName);
		final IcTapeTo tapeErp = icTapeTo;
		// Add By Hank 2008/01/21 For ASL Release to SAP
		final MpListTo mpListErp = mpListTo;

		String toErp = request.getParameter("toErp");
		final String className = this.getClass().getName();
		if ("1".equals(toErp.trim())) {

			// *****3.1.1 Release IC Wafer*****//
			// IC Wafer Start Release to SAP
			TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
				public void doInTransactionWithoutResult(
						final TransactionStatus status) {
					String result = null;
					if (icWafer != null) {
						
						result = IcWaferERP.release(icWafer, user);
						if (result != null) {
							throw new ReleaseERPException(result);
						}

						if (icWafer.getMpStatus() != null
								&& icWafer.getMpStatus().equalsIgnoreCase(
										"Non-MP")) {
							// insert DS
							// result = IcWaferERP.releaseDsNoMp(icWafer, user);
							result = IcWaferERP.releaseDsfICWaferWithCharacter(
									icWafer, user);

							if (result != null) {
								throw new ReleaseERPException(result);
							}

							// insert CP
							if (icWafer.getRoutingCp()) {
								// result = IcWaferERP.releaseCpNoMp(icWafer,
								// user);
								result = IcWaferERP
										.releaseCpfICWaferWithCharacter(
												icWafer, user, className);
								if (result != null) {
									throw new ReleaseERPException(result);
								}
							}

							// insert BP
							if (icWafer.getRoutingBp()) {
								// result = IcWaferERP.releaseBpNoMp(icWafer,
								// user);
								result = IcWaferERP
										.releaseBpfICWaferWithCharacter(
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
								result = IcWaferERP
										.releaseCpfICWaferWithCharacter(
												icWafer, user, className);
								if (result != null) {
									throw new ReleaseERPException(result);
								}
							}

							// insert BP
							if (icWafer.getRoutingBp()) {
								BumpMaskDao bumpDao = new BumpMaskDao();
								List<BumpMaskTo> bumpToList = bumpDao
										.getByProjCode(icWafer.getProjCode());

								if (bumpToList != null && bumpToList.size() > 0) {
									result = IcWaferERP.releaseBumping(
											bumpToList.get(0), user, icWafer);
									if (result != null) {
										throw new ReleaseERPException(result);
									}
								}
							}
							// IC FG Start Release to SAP
							if (icFgErp != null) {
								result = IcFgERP.releaseForMPList(icFgErp,
										user, className);

								if (result != null) {
									throw new ReleaseERPException(result);
								}
							}
							// Tape Start Release to SAP
							if (tapeErp != null) {
								result = IcTapeERP.release(tapeErp, user);
								if (result != null) {
									throw new ReleaseERPException(result);
								}
							}

							/*
							 * 3.2 2008/01/21 Added Hank Release ASL List to SAP
							 * ERP
							 */

							// ****** 3.2 Start Release ASL List to SAP
							// ERP******//
							if (mpListErp != null) {
								result = MpListAslERP.release(mpListErp, user);
								if (result != null) {
									throw new ReleaseERPException(result);
								}
							}

							// ****** 3.2 End Release ASL List to SAP ERP
							// ******//

							/*
							 * 3.3 2008/04/17 Added Hank Release APL List to SAP
							 * ERP
							 */

							// ****** 3.3 Start Release APL List to SAP
							// ERP******//
							if (mpListErp != null) {
								result = MpListAplERP.release(mpListErp, user);
								if (result != null) {
									throw new ReleaseERPException(result);
								}
							}

							if (mpListErp != null) {
								result = MpListAplERP.releaseEOL(mpListErp,
										user);
								if (result != null) {
									throw new ReleaseERPException(result);
								}
							}
							// ****** 3.3 End Release APL List to SAP ERP
							// ******//
						}
						
						// insert Color Filter Material
						// if (icWafer.getRoutingColorFilter()){
						result = IcWaferERP.releaseColorFilterICWafer(
								icWafer, user, className);
						if (result != null) {
							throw new ReleaseERPException(result);
						}
						// }

						// insert TurnKey(Wafer + Color Filter)
						// if (icWafer.getRoutingWaferCf()){
						result = IcWaferERP.releaseWaferCFICWafer(icWafer,
								user, className);
						if (result != null) {
							throw new ReleaseERPException(result);
						}
						// }

						// insert cp Polish Material
						result = IcWaferERP.releasePolishICWafer(icWafer,
								user, className);
						if (result != null) {
							throw new ReleaseERPException(result);
						}
						
						// insert cp CSP Material
						result = IcWaferERP.releaseCpCspICWafer(icWafer,
								user, className);
						if (result != null) {
							throw new ReleaseERPException(result);
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
						
						
					}
				}
			};

			try {
				new FunctionDao().doInTransaction(callback);

			} catch (ReleaseERPException e) {
				request.setAttribute("error", ERPHelper.getErrorMessage(e
						.getMessage()));
				String targetForward = mapping.findForward("viewEditJsp")
						.getPath();

				if (targetForward.indexOf('?') == -1) {
					targetForward = targetForward + "?";
				} else {
					targetForward = targetForward + "&";
				}

				ActionForward actionForward = new ActionForward(targetForward
						+ " partNum=" + mpListTo.getPartNum()
						+ "&icFgMaterialNum=" + mpListTo.getIcFgMaterialNum()
						+ "&projCodeWVersion=" + mpListTo.getProjCodeWVersion()
						+ "&tapeName=" + mpListTo.getTapeName() + "&pkgCode="
						+ mpListTo.getPkgCode());

				return actionForward;
			}

			// 3.1.3 End Release to SAP

		}
		// ****** End Release To SAP ******//

		// redirect to IcFg modify
		// Remove for Hank 2008/02/12
		/*
		 * IcFgTo icFgTempTo =
		 * icFgDao.findByMaterialNum(mpListTo.getIcFgMaterialNum()); ProjectDao
		 * projectDao = new ProjectDao(); ProjectTo projectTo =
		 * projectDao.findByProjectCode(icFgTo.getProjCode());
		 * 
		 * String path = "/md/ic_fg_edit.do?m=pre&materialNum=" +
		 * mpListTo.getIcFgMaterialNum() + "&fab=" + projectTo.getFab() +
		 * "&reminder=1"; if (tempWafer != null && mpListTo != null &&
		 * mpListTo.getMpStatus().equals("1")) { path +=
		 * "&jumpUrl="+Escape.escape(SendMailDispatch.getUrl("MD_4_EDIT",
		 * request.getContextPath(), tempWafer)); } return new
		 * ActionForward(path, true);
		 */

		// Redirect to MP_List Edit Page
		String mes = "";
		if ("1".equals(toErp.trim())) {
			mes = "Save and Release to ERP Successfully";
		} else {
			mes = "Save Successfully";
		}

		request.setAttribute("error", mes);

		// Add for Hank 2008/02/02
		String targetForward = mapping.findForward("viewEditJsp").getPath();

		if (targetForward.indexOf('?') == -1) {
			targetForward = targetForward + "?";
		} else {
			targetForward = targetForward + "&";
		}

		ActionForward actionForward = new ActionForward(targetForward
				+ " partNum=" + mpListTo.getPartNum() + "&icFgMaterialNum="
				+ mpListTo.getIcFgMaterialNum() + "&projCodeWVersion="
				+ mpListTo.getProjCodeWVersion() + "&tapeName="
				+ mpListTo.getTapeName() + "&pkgCode=" + mpListTo.getPkgCode());

		return actionForward;
	}
}
