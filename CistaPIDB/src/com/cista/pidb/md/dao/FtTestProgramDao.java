package com.cista.pidb.md.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.cista.pidb.core.GenericRowMapper;
import com.cista.pidb.core.PIDBDaoSupport;
import com.cista.pidb.md.to.FtTestProgramQueryTo;
import com.cista.pidb.md.to.FtTestProgramTo;

/**
 * .
 * 
 * @author Hu Meixia
 * 
 */
public class FtTestProgramDao extends PIDBDaoSupport {
	/**
	 * .
	 * 
	 * @param partNum
	 *            String
	 * @return List
	 */
	public List<FtTestProgramTo> findByPartNum(final String partNum) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_FT_TEST_PROGRAM where PART_NUM=? ";
		GenericRowMapper<FtTestProgramTo> rm = new GenericRowMapper<FtTestProgramTo>(
				FtTestProgramTo.class);

		return sjt.query(sql, rm, new Object[] { partNum });
	}

	/**
	 * .
	 * 
	 * @return List
	 */
	public List<String> findFtTestProgName() {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select distinct FT_TEST_PROG_NAME from PIDB_FT_TEST_PROGRAM order by FT_TEST_PROG_NAME";
		logger.debug(sql);
		List<Map<String, Object>> result = sjt.queryForList(sql,
				new Object[] {});
		List<String> ftTestProgRevision = new ArrayList<String>();
		if (result != null && result.size() > 0) {
			for (Map<String, Object> item : result) {
				ftTestProgRevision.add((String) item.get("FT_TEST_PROG_NAME"));
			}
		}
		return ftTestProgRevision;
	}

	/**
	 * .
	 * 
	 * @param ftTestProgName
	 *            String
	 * @return FtTestProgramTo
	 */
	public FtTestProgramTo find(final String ftTestProgName) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_FT_TEST_PROGRAM where FT_TEST_PROG_NAME = ?";
		GenericRowMapper<FtTestProgramTo> rm = new GenericRowMapper<FtTestProgramTo>(
				FtTestProgramTo.class);
		List<FtTestProgramTo> result = sjt.query(sql, rm,
				new Object[] { ftTestProgName });
		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
	}

	public FtTestProgramTo find(final String ftTestProgName,
			final String ftMaterialNum) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_FT_TEST_PROGRAM where FT_TEST_PROG_NAME = ? AND FT_MATERIAL_NUM = ? ";
		GenericRowMapper<FtTestProgramTo> rm = new GenericRowMapper<FtTestProgramTo>(
				FtTestProgramTo.class);
		List<FtTestProgramTo> result = sjt.query(sql, rm, new Object[] {
				ftTestProgName, ftMaterialNum });
		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
	}

	/**
	 * .
	 * 
	 * @param ftTestProgName
	 *            String
	 * @return FtTestProgramTo
	 */
	public FtTestProgramTo findByFtTestProgName(final String ftTestProgName,
			final String ftMaterialNum) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_FT_TEST_PROGRAM where FT_TEST_PROG_NAME = ? AND FT_MATERIAL_NUM = ? ";
		GenericRowMapper<FtTestProgramTo> rm = new GenericRowMapper<FtTestProgramTo>(
				FtTestProgramTo.class);
		List<FtTestProgramTo> result = sjt.query(sql, rm, new Object[] {
				ftTestProgName, ftMaterialNum });
		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
	}

	/**
	 * .
	 * 
	 * @param ftTestProgName
	 *            String
	 * @return FtTestProgramTo Add Hank 2007/12/21 Use for new release rule
	 */
	public FtTestProgramTo findByFtMaterialNum(final String ftMaterialNum) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_FT_TEST_PROGRAM where FT_MATERIAL_NUM = ? and MULTIPLE_STAGE='1' ";
		GenericRowMapper<FtTestProgramTo> rm = new GenericRowMapper<FtTestProgramTo>(
				FtTestProgramTo.class);
		List<FtTestProgramTo> result = sjt.query(sql, rm,
				new Object[] { ftMaterialNum });
		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
	}

	/**
	 * .
	 * 
	 * @param ftTestProgName
	 *            String
	 * @return FtTestProgramTo Add Hank 2007/12/21 Use for new release rule
	 */
	public List<String> findListByFtMaterialNum(final String ftMaterialNum) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_FT_TEST_PROGRAM where FT_MATERIAL_NUM = ? and MULTIPLE_STAGE='1' ";

		List<Map<String, Object>> result = sjt.queryForList(sql,
				new Object[] { ftMaterialNum });

		List<String> ftTestProgName = new ArrayList<String>();
		if (result != null && result.size() > 0) {
			for (Map<String, Object> item : result) {
				ftTestProgName.add((String) item.get("FT_TEST_PROG_NAME"));
			}
		}
		return ftTestProgName;

	}

	public FtTestProgramTo findByPrimaryKey(final String ftMaterialNum,
			final String ftTestProgName) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_FT_TEST_PROGRAM where FT_MATERIAL_NUM = ? and FT_TEST_PROG_NAME = ? ";
		GenericRowMapper<FtTestProgramTo> rm = new GenericRowMapper<FtTestProgramTo>(
				FtTestProgramTo.class);
		List<FtTestProgramTo> result = sjt.query(sql, rm, new Object[] {
				ftMaterialNum, ftTestProgName });
		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
	}

	/**
	 * .
	 * 
	 * @return List
	 */
	public List<String> findFtTestProgRevision() {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select distinct FT_TEST_PROG_REVISION from PIDB_FT_TEST_PROGRAM order by FT_TEST_PROG_REVISION";
		logger.debug(sql);
		List<Map<String, Object>> result = sjt.queryForList(sql,
				new Object[] {});
		List<String> ftTestProgRevision = new ArrayList<String>();
		if (result != null && result.size() > 0) {
			for (Map<String, Object> item : result) {
				ftTestProgRevision.add((String) item
						.get("FT_TEST_PROG_REVISION"));
			}
		}
		return ftTestProgRevision;
	}

	/**
	 * .
	 * 
	 * @param queryTo
	 *            FtTestProgramQueryTo
	 * @return int
	 */
	public int countResult(final FtTestProgramQueryTo queryTo) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "";
		sql = "select count(*) from "
				+ "(select distinct p.FT_TEST_PROG_NAME,p.PART_NUM,p.FT_TEST_PROG_REVISION,pd.PROD_CODE "
				+ " from PIDB_FT_TEST_PROGRAM p, PIDB_IC_FG pd where p.PART_NUM = pd.PART_NUM ";

		String whereCause = generateWhereCause(queryTo);
		sql += whereCause + ")";
		return sjt.queryForInt(sql, new Object[] {});
	}

	 /**
     * .
     * @param queryTo FtTestProgramQueryTo
     * @return List
     */
    public List<FtTestProgramQueryTo> query(final FtTestProgramQueryTo queryTo) {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        String sql = "";

            sql = "select "
                + "distinct p.FT_TEST_PROG_NAME,"
                + "p.PART_NUM,"
                + "p.FT_TEST_PROG_REVISION,"
                + "p.FT_MATERIAL_NUM,"
                + " p.TESTER,"
                + " p.MULTIPLE_STAGE,"
                + "pd.PROD_CODE"
                + " from PIDB_FT_TEST_PROGRAM p, PIDB_IC_FG pd where p.PART_NUM = pd.PART_NUM ";

        String whereCause = generateWhereCause(queryTo);
        sql += whereCause;
        sql += " order by p.PART_NUM";

        GenericRowMapper<FtTestProgramQueryTo> rm = new GenericRowMapper<FtTestProgramQueryTo>(FtTestProgramQueryTo.class);
        if (queryTo.getPageNo() > 0) {
            int cursorFrom = (queryTo.getPageNo() - 1) * queryTo.getPageSize() + 1;
            int cursorTo = (queryTo.getPageNo()) * queryTo.getPageSize();
            return sjt.query(getPagingSql(sql, cursorFrom, cursorTo), rm, new Object[]{});
        } else {
            return sjt.query(sql, rm, new Object[]{});
        }
    }

	/**
	 * .
	 * 
	 * @param queryTo
	 *            FtTestProgramQueryTo
	 * @return List
	 */
	public List<FtTestProgramTo> queryTo(final FtTestProgramQueryTo queryTo) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "";

		sql = "select "
				+ "distinct p.FT_TEST_PROG_NAME,"
				+ "p.PART_NUM,"
				+ "p.FT_TEST_PROG_REVISION,"
				+ "p.FT_TEST_PROG_RELEASE_DATE,"
				+ "p.FT_CPU_TIME,"
				+ "p.FT_INDEX_TIME,"
				+ "p.CONTACT_DIE_QTY,"
				+ "p.TESTER,"
				+ "p.TESTER_CONFIG,"
				+ "p.FIRST_FT_TEST_HOUSE,"
				+ "p.REMARK,"
				+ "p.ASSIGN_TO,"
				+ "p.ASSIGN_EMAIL,"
				+ "p.CREATED_BY,"
				+ "p.MODIFIED_BY,"
				+ "p.FT_MATERIAL_NUM,"
				+ "p.MULTIPLE_STAGE,"
				+ "pd.PROD_CODE"
				+ " from PIDB_FT_TEST_PROGRAM p, PIDB_IC_FG pd where p.PART_NUM = pd.PART_NUM ";

		String whereCause = generateWhereCause(queryTo);
		sql += whereCause;
		sql += " order by p.PART_NUM";

		GenericRowMapper<FtTestProgramTo> rm = new GenericRowMapper<FtTestProgramTo>(
				FtTestProgramTo.class);
		if (queryTo.getPageNo() > 0) {
			int cursorFrom = (queryTo.getPageNo() - 1) * queryTo.getPageSize()
					+ 1;
			int cursorTo = (queryTo.getPageNo()) * queryTo.getPageSize();
			return sjt.query(getPagingSql(sql, cursorFrom, cursorTo), rm,
					new Object[] {});
		} else {
			return sjt.query(sql, rm, new Object[] {});
		}
	}

	/**
	 * .
	 * 
	 * @param queryTo
	 *            FtTestProgramQueryTo
	 * @return String
	 */
	private String generateWhereCause(final FtTestProgramQueryTo queryTo) {
		StringBuilder sb = new StringBuilder();
		if (queryTo.getProdCode() != null
				&& (queryTo.getProdCode()).length() > 0) {
			String queryString = getSmartSearchQueryString("pd.PROD_CODE",
					queryTo.getProdCode());
			if (queryString != null) {
				sb.append(" and (" + queryString + " )");
			}
		}

		if (queryTo.getFtTestProgName() != null
				&& !queryTo.getFtTestProgName().equals("")) {
			String queryString = getSmartSearchQueryString(
					"p.FT_TEST_PROG_NAME", queryTo.getFtTestProgName());
			if (queryString != null) {
				sb.append(" and (" + queryString + " )");
			}
		}

		if (queryTo.getPartNum() != null && !queryTo.getPartNum().equals("")) {
			String queryString = getSmartSearchQueryString("p.PART_NUM",
					queryTo.getPartNum());
			if (queryString != null) {
				sb.append(" and (" + queryString + " )");
			}
		}

		if (queryTo.getFtMaterialNum() != null
				&& !queryTo.getFtMaterialNum().equals("")) {
			String queryString = getSmartSearchQueryString("p.FT_MATERIAL_NUM",
					queryTo.getFtMaterialNum());
			if (queryString != null) {
				sb.append(" and (" + queryString + " )");
			}
		}

		if (queryTo.getFtTestProgRevision() != null
				&& !queryTo.getFtTestProgRevision().equals("")) {
			String queryString = getSmartSearchQueryString(
					"p.FT_TEST_PROG_REVISION", queryTo.getFtTestProgRevision());
			if (queryString != null) {
				sb.append(" and (" + queryString + " )");
			}
		}
		return sb.toString();
	}

	/**
	 * .
	 * 
	 * @param ftTestProgNameList
	 *            String
	 * @return FtTestProgramTo
	 */
	public List<FtTestProgramTo> findByFtTestProgNameList(
			final String ftTestProgNameList) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_FT_TEST_PROGRAM where 1=1 and pidb_include('"
				+ ftTestProgNameList + "',',',FT_TEST_PROG_NAME) >= 1";
		GenericRowMapper<FtTestProgramTo> rm = new GenericRowMapper<FtTestProgramTo>(
				FtTestProgramTo.class);
		return sjt.query(sql, rm, new Object[] {});
	}
}
