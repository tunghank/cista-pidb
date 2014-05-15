package com.cista.pidb.md.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.cista.pidb.code.dao.FunctionParameterDao;
import com.cista.pidb.code.to.FunctionParameterTo;
import com.cista.pidb.core.GenericRowMapper;
import com.cista.pidb.core.PIDBDaoSupport;
import com.cista.pidb.md.to.EsdTestQueryTo;
import com.cista.pidb.md.to.EsdTestTo;

public class EsdTestDao extends PIDBDaoSupport {

	FunctionParameterDao fpDao = new FunctionParameterDao();

	public void insertEsdTest(final EsdTestTo newInstance) {
		super.insert(newInstance, "PIDB_ESD_TEST");
	}

	public List<EsdTestQueryTo> queryAll(EsdTestQueryTo queryTo) {

		SimpleJdbcTemplate stj = super.getSimpleJdbcTemplate();
		String sql = "select distinct * from PIDB_ESD_TEST";
		List<EsdTestTo> result = stj.query(sql,
				new GenericRowMapper<EsdTestTo>(EsdTestTo.class),
				new Object[] {});
		List<EsdTestQueryTo> queryList = new ArrayList<EsdTestQueryTo>();
		if (result != null && result.size() > 0) {
			for (int i = 0; i < result.size(); i++) {
				EsdTestQueryTo oneQuery = new EsdTestQueryTo();
				oneQuery.setProjCodeWVersion(result.get(i)
						.getProjCodeWVersion());
				queryList.add(oneQuery);
			}
		}
		return queryList;
	}

	public List<EsdTestTo> queryForDownload(final EsdTestQueryTo queryTo) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select distinct " + "phr.* "
				+ "from PIDB_ESD_TEST phr where 1=1 ";

		if (queryTo.getEsdFinishDate() != null
				&& !queryTo.getEsdFinishDate().equals("")) {
			String dateFrom = getSQLDateString(queryTo.getEsdFinishDate()
					+ " 00:00:00", "yyyy/MM/dd HH:mm:ss");
			sql += " and phr.ESD_FINISH_DATE=" + dateFrom;
		}

		String whereCause = generateWhereCause(queryTo);
		sql += whereCause;
		sql += " order by phr.PROJ_CODE_W_VERSION";

		logger.debug(sql);

		GenericRowMapper<EsdTestTo> rm = new GenericRowMapper<EsdTestTo>(
				EsdTestTo.class);

		List<EsdTestTo> result = null;
		if (queryTo.getPageNo() > 0) {

			int cursorFrom = (queryTo.getPageNo() - 1) * queryTo.getPageSize()
					+ 1;
			int cursorTo = (queryTo.getPageNo()) * queryTo.getPageSize();
			result = sjt.query(getPagingSql(sql, cursorFrom, cursorTo), rm,
					new Object[] {});
		} else {

			result = sjt.query(sql, rm, new Object[] {});

		}

		if (result != null && result.size() > 0) {
			for (EsdTestTo t : result) {
				if (t.getOwner() == null) {
					t.setOwner("");
				} else if (!t.getOwner().equals("") && t.getOwner() != null) {
					String fundName = "RA";
					String funFieldName = "OWNER";
					FunctionParameterTo to = fpDao.findValueByFiledValue(
							fundName, funFieldName, t.getOwner());
					t.setOwner(to.getFieldShowName());
				}
			}
		}
		return result;
	}

	public void insertToTempTable(final List<String> projNameList) {

		String tempSql = "insert into PIDB_TEMP_PROJECT_NAME values (?)";
		BatchPreparedStatementSetter bpss = new BatchPreparedStatementSetter() {
			public int getBatchSize() {
				return projNameList.size();
			}

			public void setValues(PreparedStatement ps, int i) {
				try {
					ps.setString(1, projNameList.get(i));
				} catch (SQLException e) {
					logger.error(e);
				}
			}
		};
		getJdbcTemplate().batchUpdate(tempSql, bpss);

	}

	public int countResult(final EsdTestQueryTo queryTo) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select count(*) from PIDB_ESD_TEST phr " + "  where 1=1";

		if (queryTo.getEsdFinishDate() != null
				&& !queryTo.getEsdFinishDate().equals("")) {
			String dateFrom = getSQLDateString(queryTo.getEsdFinishDate()
					+ " 00:00:00", "yyyy/MM/dd HH:mm:ss");
			sql += " and phr.ESD_FINISH_DATE=" + dateFrom;
		}

		String whereCause = generateWhereCause(queryTo);
		sql += whereCause;
		logger.debug(sql);
		return sjt.queryForInt(sql, new Object[] {});

	}

	public EsdTestTo findByProjCodeWVersion(String projCodeWVersion) {
		SimpleJdbcTemplate stj = super.getSimpleJdbcTemplate();
		String sql = "select * from PIDB_ESD_TEST where PROJ_CODE_W_VERSION = ?";
		logger.debug(sql);

		List<EsdTestTo> result = stj.query(sql,
				new GenericRowMapper<EsdTestTo>(EsdTestTo.class),
				new Object[] { projCodeWVersion });
		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			/*
			 * logger.warn("User with ProjectCodeWVersion = " + projCodeWVersion + "
			 * is not found.");
			 */
			return null;
		}
	}

	public List<EsdTestTo> findProjCodeWVersion() {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select distinct PROJ_CODE_W_VERSION from PIDB_ESD_TEST where "
				+ getAssertEmptyString("PROJ_CODE_W_VERSION")
				+ " order by PROJ_CODE_W_VERSION";
		logger.debug(sql);
		List<EsdTestTo> result = stj.query(sql,
				new GenericRowMapper<EsdTestTo>(EsdTestTo.class),
				new Object[] {});
		return result;
	}

	public EsdTestTo findByProjCodeWVersion(String projCodeWVersion,
			String idEsdTesting) {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_ESD_TEST where PROJ_CODE_W_VERSION=? And ID_ESD_TESTING=?";
		logger.debug(sql);
		List<EsdTestTo> result = stj.query(sql,
				new GenericRowMapper<EsdTestTo>(EsdTestTo.class), new Object[] {
						projCodeWVersion, idEsdTesting });
		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}

	}

	public List<EsdTestQueryTo> query(final EsdTestQueryTo queryTo) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select phr.PROD_CODE,phr.PROJ_CODE_W_VERSION,phr.ID_ESD_TESTING,to_char(phr.ESD_FINISH_DATE,'yyyy-mm-dd') as ESD_FINISH_DATE,"
				+ " phr.OWNER from PIDB_ESD_TEST phr " + " where 1=1 ";

		if (queryTo.getEsdFinishDate() != null
				&& !queryTo.getEsdFinishDate().equals("")) {
			String dateFrom = getSQLDateString(queryTo.getEsdFinishDate()
					+ " 00:00:00", "yyyy/MM/dd HH:mm:ss");
			sql += " and phr.ESD_FINISH_DATE=" + dateFrom;
		}

		String whereCause = generateWhereCause(queryTo);
		sql += whereCause;
		sql += " order by phr.PROJ_CODE_W_VERSION";

		logger.debug(sql);

		GenericRowMapper<EsdTestQueryTo> rm = new GenericRowMapper<EsdTestQueryTo>(
				EsdTestQueryTo.class);

		List<EsdTestQueryTo> result = null;
		if (queryTo.getPageNo() > 0) {

			int cursorFrom = (queryTo.getPageNo() - 1) * queryTo.getPageSize()
					+ 1;
			int cursorTo = (queryTo.getPageNo()) * queryTo.getPageSize();
			result = sjt.query(getPagingSql(sql, cursorFrom, cursorTo), rm,
					new Object[] {});
		} else {

			result = sjt.query(sql, rm, new Object[] {});

		}

		return result;
	}

	private String generateWhereCause(final EsdTestQueryTo queryTo) {
		StringBuilder sb = new StringBuilder();

		if (queryTo.getProdCode() != null && !queryTo.getProdCode().equals("")) {
			String prodCode = getSmartSearchQueryLike("phr.PROD_Code", queryTo
					.getProdCode().toUpperCase());
			if (prodCode != null) {
				sb.append(" and (" + prodCode + " )");
			}
		}

		/*
		 * if (queryTo.getProjName() != null &&
		 * !queryTo.getProjName().equals("")) { String projName =
		 * getSmartSearchQueryString("ppj.PROJ_NAME", queryTo.getProjName()); if
		 * (projName != null) { sb.append(" and (" + projName + " )"); } }
		 */

		if (queryTo.getProjCodeWVersion() != null
				&& !queryTo.getProjCodeWVersion().equals("")) {
			String projCodeWVersion = getSmartSearchQueryLike(
					"phr.PROJ_CODE_W_VERSION", queryTo.getProjCodeWVersion()
							.toUpperCase());
			if (projCodeWVersion != null) {
				sb.append(" and (" + projCodeWVersion + " )");
			}
		}

		if (queryTo.getOwner() != null && !queryTo.getOwner().equals("")) {
			String owner = getSmartSearchQueryLike("phr.OWNER", queryTo
					.getOwner());
			if (owner != null) {
				sb.append(" and (" + owner + " )");
			}
		}

		return sb.toString();
	}

}
