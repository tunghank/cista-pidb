package com.cista.pidb.md.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.cista.pidb.core.GenericRowMapper;
import com.cista.pidb.core.PIDBDaoSupport;
import com.cista.pidb.md.to.ProjectCodeTo;

public class ProjectCodeDao extends PIDBDaoSupport {

	public List<String> findAllProjectCode() {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_PROJECT_CODE";
		logger.debug(sql);
		List projectCodeList = stj.query(sql,
				new GenericRowMapper<ProjectCodeTo>(ProjectCodeTo.class),
				new Object[] {});
		List<String> projCodeList = new ArrayList<String>();
		if (projectCodeList != null && projectCodeList.size() > 0) {
			for (int i = 0; i < projectCodeList.size(); i++) {
				ProjectCodeTo oneProjCodeTo = (ProjectCodeTo) projectCodeList
						.get(i);
				projCodeList.add(oneProjCodeTo.getProjCode());
			}
		}
		return projCodeList;
	}

	public List<ProjectCodeTo> findAll() {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_PROJECT_CODE order by PROJ_CODE";
		logger.debug(sql);
		return sjt.query(sql, new GenericRowMapper<ProjectCodeTo>(
				ProjectCodeTo.class), new Object[] {});
	}

	public ProjectCodeTo findByProjectCode(String projCode) {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_PROJECT_CODE where PROJ_CODE = '"
				+ projCode + "'";
		logger.debug(sql);
		GenericRowMapper<ProjectCodeTo> mapper = new GenericRowMapper<ProjectCodeTo>(
				ProjectCodeTo.class);
		List<ProjectCodeTo> list = stj.query(sql, mapper, new Object[] {});
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public ProjectCodeTo findByProjectCodeData(String projCode,
			String projOption) {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_PROJECT_CODE where PROJ_CODE = ? "
				+ " And proj_option = ? ";
		logger.debug(sql);
		GenericRowMapper<ProjectCodeTo> mapper = new GenericRowMapper<ProjectCodeTo>(
				ProjectCodeTo.class);
		List<ProjectCodeTo> list = stj.query(sql, mapper, new Object[] {
				projCode, projOption });
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public List<String> findDistAllOption() {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select distinct PROJ_OPTION from PIDB_PROJECT_CODE order by PROJ_OPTION";
		logger.debug(sql);
		List<Map<String, Object>> result = stj.queryForList(sql,
				new Object[] {});
		List<String> projOptions = new ArrayList<String>();
		if (result != null && result.size() > 0) {
			for (Map<String, Object> item : result) {
				projOptions.add((String) item.get("PROJ_OPTION"));
			}
		}
		return projOptions;
	}

	/**
	 * Find project codes by project name.
	 * 
	 * @param projName
	 *            PROJ_NAME
	 * @return List of project codes.
	 */
	public List<String> findProjCode(final String projName) {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select distinct PROJ_CODE from PIDB_PROJECT_CODE where PROJ_NAME = ? order by PROJ_CODE";
		logger.debug(sql);
		List<Map<String, Object>> result = stj.queryForList(sql,
				new Object[] { projName });
		List<String> projCodes = new ArrayList<String>();
		if (result != null && result.size() > 0) {
			for (Map<String, Object> item : result) {
				projCodes.add((String) item.get("PROJ_CODE"));
			}
		}
		return projCodes;
	}

	public String findOptionByProjName(final String projName) {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select* from PIDB_PROJECT_CODE where PROJ_NAME = ?";
		logger.debug(sql);
		GenericRowMapper<ProjectCodeTo> mapper = new GenericRowMapper<ProjectCodeTo>(
				ProjectCodeTo.class);
		List<ProjectCodeTo> list = stj.query(sql, mapper,
				new Object[] { projName });
		if (list != null && list.size() > 0) {
			for (ProjectCodeTo p : list) {
				if (p.getProjOption() != null && p.getProjOption().length() > 0) {
					return p.getProjOption();
				}
			}
		}
		return null;
	}

	public List<ProjectCodeTo> findByProjectName(String projName) {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_PROJECT_CODE where PROJ_NAME = ?";
		logger.debug(sql);
		GenericRowMapper<ProjectCodeTo> mapper = new GenericRowMapper<ProjectCodeTo>(
				ProjectCodeTo.class);
		return stj.query(sql, mapper, new Object[] { projName });
	}
	
	public List<ProjectCodeTo> findByProdCode(String prodCode) {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_PROJECT_CODE where PROD_CODE like '%" + prodCode +"%'";
		logger.debug(sql);
		GenericRowMapper<ProjectCodeTo> mapper = new GenericRowMapper<ProjectCodeTo>(
				ProjectCodeTo.class);
		return stj.query(sql, mapper, new Object[] {});
	}
}
