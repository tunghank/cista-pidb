package com.cista.pidb.md.erp;

import java.util.Date;
import java.util.List;

import com.cista.pidb.admin.to.UserTo;
import com.cista.pidb.core.SequenceSupport;
import com.cista.pidb.md.dao.IfProjectCodeDao;
import com.cista.pidb.md.dao.ProjectCodeDao;
import com.cista.pidb.md.to.IfProjectCodeTo;
import com.cista.pidb.md.to.ProjectCodeTo;
import com.cista.pidb.md.to.ProjectTo;

/**
 * Release to erp for project master.
 * 
 * @author fumingjie
 */
public final class ProjERP {

	/**
	 * Do release.
	 * 
	 * @param projTo
	 *            bean object
	 * @param userTo
	 *            UserTo
	 * @return String of error message
	 */
	public static String release(final ProjectTo projTo, final UserTo userTo) {
		releaseCode(projTo, userTo.getUserId());
		return null;
	}

	/**
	 * Do project code release.
	 * 
	 * @param projTo
	 *            ProjectTo
	 * @param sessionUser
	 *            sessionUser User's id from session
	 * @return A message of strng
	 */
	private static String releaseCode(final ProjectTo projTo,
			final String sessionUser) {
		ProjectCodeDao projectCodeDao = new ProjectCodeDao();
		IfProjectCodeDao ifCodeDao = new IfProjectCodeDao();

		List<ProjectCodeTo> projCodeList = projectCodeDao
				.findByProjectName(projTo.getProjName());

		if (projCodeList != null && projCodeList.size() > 0) {
			for (ProjectCodeTo projCodeData : projCodeList) {
				if (!projCodeData.getProjCode().equals("")
						&& projCodeData.getProjCode() != null) {
					IfProjectCodeTo ifProjCodeTo = new IfProjectCodeTo();
					ifProjCodeTo.setId(SequenceSupport
							.nextValue(SequenceSupport.SEQ_IF_PROJECT_CODE));
					ifProjCodeTo.setSapStatus("");
					ifProjCodeTo.setInfoMessage("");
					ifProjCodeTo.setTimeStamp(new Date());
					ifProjCodeTo.setReleasedBy(sessionUser);
					ifProjCodeTo.setProjCode(projCodeData.getProjCode());
					ifProjCodeTo.setReleaseTo(projCodeData.getReleaseTo());
					//Remark in 2009/04/14 Hank
					//if (ifCodeDao.findByProjCode(projCodeData.getProjCode()) == null) {
						ifCodeDao.insert(ifProjCodeTo);
					//}
				}
			}
		}

		return null;
	}

	/**
	 * Private constructor.
	 */
	private ProjERP() {
	}
}
