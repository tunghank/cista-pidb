package com.cista.pidb.ajax;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cista.pidb.admin.dao.ParameterDao;
import com.cista.pidb.admin.to.ParameterTo;

public class FetchItem extends Action {
	public ActionForward execute(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		/** ***明確告知要將編碼型態傳送至前端頁面***Add by 990044** */
		response.setContentType("text/plain; charset=UTF-8");
		/** ***************** */
		String funName = request.getParameter("funName");
		String funFieldName = request.getParameter("funFieldName");
		PrintWriter out = response.getWriter();
		ParameterDao dao = new ParameterDao();
		List<ParameterTo> paraList = dao.findMaxItem(funName, funFieldName);
		if (paraList != null && paraList.size() > 0) {
			for (ParameterTo icTo : paraList) {
				if (icTo.getItem() != null && icTo.getItem().length() > 0) {
					int i =(Integer.parseInt(icTo.getItem())+1);

					out.print(String.valueOf(i));

				}
			}

		}
		return null;
	}

}
