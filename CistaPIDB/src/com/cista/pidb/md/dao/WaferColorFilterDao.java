package com.cista.pidb.md.dao;

import java.util.List;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.cista.pidb.core.GenericRowMapper;
import com.cista.pidb.core.PIDBDaoSupport;
import com.cista.pidb.md.to.WaferColorFilterQueryTo;
import com.cista.pidb.md.to.WaferColorFilterTo;

public class WaferColorFilterDao extends PIDBDaoSupport{
	
	/**
	 * Find all ctps in the table.
	 * 
	 * @return List of CpMaterialTo
	 */
	public List<WaferColorFilterTo> findAll() {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_WAFER_CF_MATERIAL order by WAFER_CF_MATERIAL_NUM ";
		logger.debug(sql);
		return sjt.query(sql, new GenericRowMapper<WaferColorFilterTo>(
				WaferColorFilterTo.class), new Object[] {});
	}
	
	public List<WaferColorFilterTo> getCpMaterialVariant(
			String projectCodeWVersion) {
		if (projectCodeWVersion == null && projectCodeWVersion.length() <= 0) {
			logger.warn("Project Code is employ,it is must field.");
		}
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "SELECT * FROM PIDB_COLOR_FILTER_MATERIAL  WHERE COLOR_FILTER_VARIANT NOT IN "
				+ "( SELECT CP_POLISH_VARIANT FROM PIDB_CP_POLISH_MATERIAL WHERE PROJECT_CODE_W_VERSION= ?) "
				+ " AND PROJECT_CODE_W_VERSION= ? ORDER BY 1 ";
		logger.debug(sql);
		List<WaferColorFilterTo> result = stj.query(sql,
				new GenericRowMapper<WaferColorFilterTo>(
						WaferColorFilterTo.class),
				new Object[] { projectCodeWVersion,projectCodeWVersion });

		return result;
	}
	
    public WaferColorFilterTo findByCpList(String cpList, String projCodeWVersion) {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        String sql = "select * from PIDB_WAFER_CF_MATERIAL where pidb_contain(CP_TEST_PROGRAM_NAME_LIST, ',', " + getSQLString(cpList) + ", ',')>=1 and pidb_contain(" + getSQLString(cpList) + ", ',', CP_TEST_PROGRAM_NAME_LIST, ',')>=1 and PROJECT_CODE_W_VERSION = ?";
        logger.debug(sql);
        GenericRowMapper<WaferColorFilterTo> rm = new GenericRowMapper<WaferColorFilterTo>(WaferColorFilterTo.class);
        List<WaferColorFilterTo> result = sjt.query(sql, rm, new Object[] { projCodeWVersion });
        if (result != null && result.size() > 0) {
            return result.get(0);
        } else {
            return null;
        }
    }
    
    public List <WaferColorFilterTo> findByCpTestProgName(String cpTestProgName, String projCodeWVersion) {
    
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        String sql = "select * from PIDB_WAFER_CF_MATERIAL where 1=1 and PROJECT_CODE_W_VERSION = ? ";
        if (cpTestProgName!=null && cpTestProgName.length()>0) {
            sql += " and pidb_include(CP_TEST_PROGRAM_NAME_LIST, ',', " + getSQLString(cpTestProgName) + ")>=1 ";
        	
        }

        logger.debug(sql);
        GenericRowMapper<WaferColorFilterTo> rm = new GenericRowMapper<WaferColorFilterTo>(WaferColorFilterTo.class);
        List<WaferColorFilterTo> result = sjt.query(sql, rm, new Object[] { projCodeWVersion });

        
        return result;
    }
    
    
    public Object findMaxVar(String projCodeWVersion) {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        String sql = "select max(WAFER_CF_VARIANT) from PIDB_WAFER_CF_MATERIAL where PROJECT_CODE_W_VERSION = ?";
        logger.debug(sql);
        Object obj = sjt.queryForObject(sql, Object.class, new Object[] { projCodeWVersion });
        return obj;
    }       
    
    public WaferColorFilterTo findByCpMaterialNum(String cpMaterialNum) {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        String sql = "select * from PIDB_WAFER_CF_MATERIAL where WAFER_CF_MATERIAL_NUM = ?";
        
        logger.debug(sql);

        
        GenericRowMapper<WaferColorFilterTo> rm = new GenericRowMapper<WaferColorFilterTo>(WaferColorFilterTo.class);
        List<WaferColorFilterTo> result = sjt.query(sql, rm, new Object[] {cpMaterialNum});
        
        if (result != null && result.size() > 0) {
            return result.get(0);
        } else {
            return null;
        }
        
    } 
    
    public WaferColorFilterTo findByPrimaryKey(String cpMaterialNum,String projCodeWVersion) {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        String sql = "select * from PIDB_WAFER_CF_MATERIAL where WAFER_CF_MATERIAL_NUM = ? AND PROJECT_CODE_W_VERSION = ?";
        
        logger.debug(sql);

        
        GenericRowMapper<WaferColorFilterTo> rm = new GenericRowMapper<WaferColorFilterTo>(WaferColorFilterTo.class);
        List<WaferColorFilterTo> result = sjt.query(sql, rm, new Object[] {cpMaterialNum,projCodeWVersion});
        
        if (result != null && result.size() > 0) {
            return result.get(0);
        } else {
            return null;
        }
        
    } 

    public WaferColorFilterTo findByCpMaterialNum(String cpMaterialNum, String cpVariant) {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        String sql = "select * from PIDB_WAFER_CF_MATERIAL where WAFER_CF_MATERIAL_NUM = ? AND WAFER_CF_VARIANT = ? ";
        
        logger.debug(sql);

        
        GenericRowMapper<WaferColorFilterTo> rm = new GenericRowMapper<WaferColorFilterTo>(WaferColorFilterTo.class);
        List<WaferColorFilterTo> result = sjt.query(sql, rm, new Object[] {cpMaterialNum, cpVariant});
        
        if (result != null && result.size() > 0) {
            return result.get(0);
        } else {
            return null;
        }
        
    } 
    public List<WaferColorFilterTo> getByProjCodeWVersion(final String projCodeWVersion) {
    	SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        String sql = "select * from PIDB_WAFER_CF_MATERIAL where PROJECT_CODE_W_VERSION = ?";
        GenericRowMapper<WaferColorFilterTo> rm = new GenericRowMapper<WaferColorFilterTo>(WaferColorFilterTo.class);
        return sjt.query(sql, rm, new Object[]{projCodeWVersion});
    }
    
    public List<WaferColorFilterTo> getAll () {
    	SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        String sql = "select * from PIDB_WAFER_CF_MATERIAL";
        GenericRowMapper<WaferColorFilterTo> rm = new GenericRowMapper<WaferColorFilterTo>(WaferColorFilterTo.class);
        return sjt.query(sql, rm, new Object[]{});
    }
    
    public WaferColorFilterTo getByProjCodeWVersionMaxVariant(final String projCodeWVersion) {
    	SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
    	String sql = "SELECT * FROM PIDB_WAFER_CF_MATERIAL B " +
    				" WHERE B.PROJECT_CODE_W_VERSION||'-VARIANT'||B.WAFER_CF_VARIANT IN ( " +
    				" SELECT A.PROJECT_CODE_W_VERSION||'-VARIANT'||MAX(A.WAFER_CF_VARIANT) WAFER_CF_VARIANT " +
    				" FROM PIDB_WAFER_CF_MATERIAL A " +
    				" WHERE A.PROJECT_CODE_W_VERSION = ? " +
    				" GROUP BY PROJECT_CODE_W_VERSION )";
    	       
        GenericRowMapper<WaferColorFilterTo> rm = new GenericRowMapper<WaferColorFilterTo>(WaferColorFilterTo.class);
        List<WaferColorFilterTo> result = sjt.query(sql, rm, new Object[] {projCodeWVersion});
        
        if (result != null && result.size() > 0) {
            return result.get(0);
        } else {
            return null;
        }
    }
        
    /**
	 * .
	 * 
	 * @param queryTo
	 *            CpMaterialQueryTo
	 * @return List
	 */
	public List<WaferColorFilterTo> query(
			final WaferColorFilterQueryTo queryTo) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "";
		sql = "SELECT distinct " + " WAFER_CF_MATERIAL_NUM,"
				+ " PROJECT_CODE_W_VERSION," + " WAFER_CF_VARIANT,"
				+ " DESCRIPTION," + " REMARK,"
				+ " UPDATE_DATE," + " CREATED_BY," + " MODIFIED_BY "
				+ " FROM PIDB_WAFER_CF_MATERIAL where 1=1 ";

		String whereCause = generateWhereCause(queryTo);
		sql += whereCause;
		sql += " order by WAFER_CF_MATERIAL_NUM";

		GenericRowMapper<WaferColorFilterTo> rm = new GenericRowMapper<WaferColorFilterTo>(
				WaferColorFilterTo.class);
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
	 *            CpMaterialQueryTo
	 * @return String
	 */
	private String generateWhereCause(final WaferColorFilterQueryTo queryTo) {
		StringBuilder sb = new StringBuilder();

		if (queryTo.getWaferCfMaterialNum() != null
				&& !queryTo.getWaferCfMaterialNum().equals("")) {
			String queryString = getSmartSearchQueryString("WAFER_CF_MATERIAL_NUM",
					queryTo.getWaferCfMaterialNum());
			if (queryString != null) {
				sb.append(" and (" + queryString + " )");
			}
		}
		if (queryTo.getProjectCodWVersion() != null
				&& !queryTo.getProjectCodWVersion().equals("")) {
			String queryString = getSmartSearchQueryString(
					"PROJECT_CODE_W_VERSION", queryTo.getProjectCodWVersion());
			if (queryString != null) {
				sb.append(" and (" + queryString + " )");
			}
		}
		if (queryTo.getWaferCfVariant() != null
				&& !"".equals(queryTo.getWaferCfVariant().trim())) {
			String queryString = getSmartSearchQueryString("WAFER_CF_VARIANT",
					queryTo.getWaferCfVariant());
			if (queryString != null) {
				sb.append(" and (" + queryString + " )");
			}
		}
		return sb.toString();
	}
	
	/**
	 * .
	 * 
	 * @param queryTo
	 *            CpMaterialQueryTo
	 * @return int
	 */
	public int countResult(final WaferColorFilterQueryTo queryTo) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "";
		sql = " select count(*) from PIDB_WAFER_CF_MATERIAL where 1=1";
		String whereCause = generateWhereCause(queryTo);
		sql += whereCause;
		return sjt.queryForInt(sql, new Object[] {});
	}

}
