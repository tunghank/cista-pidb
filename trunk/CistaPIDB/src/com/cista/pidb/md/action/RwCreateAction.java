package com.cista.pidb.md.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.cista.pidb.admin.dao.UserDao;
import com.cista.pidb.admin.to.UserTo;
import com.cista.pidb.code.dao.SapMasterVendorDao;
import com.cista.pidb.core.HttpHelper;
import com.cista.pidb.core.PIDBContext;
import com.cista.pidb.md.dao.RwDao;
import com.cista.pidb.md.to.RwTo;

public class RwCreateAction extends DispatchAction {
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
			SapMasterVendorDao vendorDao = new SapMasterVendorDao();
			request.setAttribute("vendorList", vendorDao.findAll());

			if (request.getParameter("ref") != null) {
				String getParams = request.getParameter("ref");
				String[] params = getParams.split(",");
				String prodCode = params[0];
				String pkgCode = params[1];
				request
						.setAttribute("ref", new RwDao()
								.find(prodCode, pkgCode));
			}
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
			RwDao rwDao = new RwDao();
			RwTo rwTo = (RwTo) HttpHelper.pickupForm(RwTo.class, request);
			UserTo loginUser = PIDBContext.getLoginUser(request);
			rwTo.setCreatedBy(loginUser.getUserId());

			// add assignTo and assignEmail
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
			rwTo.setAssignTo(assigns);
			rwTo.setAssignEmail(emails);

			rwDao.insert(rwTo, "PIDB_RW");

			// send mail
			SendMailDispatch.sendMailByCreate("MD-27", rwTo.getPkgCode() + "("
					+ rwTo.getProdCode() + ")", emails, SendMailDispatch
					.getUrl("MD_27_EDIT", request.getContextPath(), rwTo));

			String mes = "Save Successfully";
			request.setAttribute("error", mes);
			request.setAttribute("ref", rwTo);
			return mapping.findForward(forward);
		}

	}
