package com.cista.pidb.md.dao;

import java.util.List;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import com.cista.pidb.core.GenericRowMapper;
import com.cista.pidb.core.PIDBDaoSupport;
import com.cista.pidb.md.to.ColorFilterMaterialQueryTo;
import com.cista.pidb.md.to.ColorFilterMaterialTo;

public class ColorFilterMaterialDao extends PIDBDaoSupport{
	
	/**
	 * Find all ctps in the table.
	 * 
	 * @return List of CpMaterialTo
	 */
	public List<ColorFilterMaterialTo> findAll() {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_COLOR_FILTER_MATERIAL order by COLOR_FILTER_MATERIAL_NUM ";
		logger.debug(sql);
		return sjt.query(sql, new GenericRowMapper<ColorFilterMaterialTo>(
				ColorFilterMaterialTo.class), new Object[] {});
	}
	
	public List<ColorFilterMaterialTo> getCpMaterialVariant(
			String projectCodeWVersion) {
		if (projectCodeWVersion == null && projectCodeWVersion.length() <= 0) {
			logger.warn("Project Code is employ,it is must field.");
		}
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "SELECT * FROM PIDB_COLOR_FILTER_MATERIAL  WHERE COLOR_FILTER_VARIANT NOT IN "
				+ "( SELECT CP_POLISH_VARIANT FROM PIDB_CP_POLISH_MATERIAL WHERE PROJECT_CODE_W_VERSION= ?) "
				+ " AND PROJECT_CODE_W_VERSION= ? ORDER BY 1 ";
		logger.debug(sql);
		List<ColorFilterMaterialTo> result = stj.query(sql,
				new GenericRowMapper<ColorFilterMaterialTo>(
						ColorFilterMaterialTo.class),
				new Object[] { projectCodeWVersion,projectCodeWVersion });

		return result;
	}
	
    public ColorFilterMaterialTo findByCpList(String cpList, String projCodeWVersion) {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        String sql = "select * from PIDB_COLOR_FILTER_MATERIAL where pidb_contain(CP_TEST_PROGRAM_NAME_LIST, ',', " + getSQLString(cpList) + ", ',')>=1 and pidb_contain(" + getSQLString(cpList) + ", ',', CP_TEST_PROGRAM_NAME_LIST, ',')>=1 and PROJECT_CODE_W_VERSION = ?";
        logger.debug(sql);
        GenericRowMapper<ColorFilterMaterialTo> rm = new GenericRowMapper<ColorFilterMaterialTo>(ColorFilterMaterialTo.class);
        List<ColorFilterMaterialTo> result = sjt.query(sql, rm, new Object[] { projCodeWVersion });
        if (result != null && result.size() > 0) {
            return result.get(0);
        } else {
            return null;
        }
    }
    
    public List <ColorFilterMaterialTo> findByCpTestProgName(String cpTestProgName, String projCodeWVersion) {
    
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        String sql = "select * from PIDB_COLOR_FILTER_MATERIAL where 1=1 and PROJECT_CODE_W_VERSION = ? ";
        if (cpTestProgName!=null && cpTestProgName.length()>0) {
            sql += " and pidb_include(CP_TEST_PROGRAM_NAME_LIST, ',', " + getSQLString(cpTestProgName) + ")>=1 ";
        	
        }

        logger.debug(sql);
        GenericRowMapper<ColorFilterMaterialTo> rm = new GenericRowMapper<ColorFilterMaterialTo>(ColorFilterMaterialTo.class);
        List<ColorFilterMaterialTo> result = sjt.query(sql, rm, new Object[] { projCodeWVersion });

        
        return result;
    }
    
    
    public Object findMaxVar(String projCodeWVersion) {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        String sql = "Select max(COLOR_FILTER_VARIANT) from PIDB_COLOR_FILTER_MATERIAL where PROJECT_CODE_W_VERSION = ?";
        logger.debug(sql);
        Object obj = sjt.queryForObject(sql, Object.class, new Object[] { projCodeWVersion });
        return obj;
    }       
    
    public ColorFilterMaterialTo findByCpMaterialNum(String cpMaterialNum) {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        String sql = "select * from PIDB_COLOR_FILTER_MATERIAL where COLOR_FILTER_MATERIAL_NUM = ?";
        
        logger.debug(sql);

        
        GenericRowMapper<ColorFilterMaterialTo> rm = new GenericRowMapper<ColorFilterMaterialTo>(ColorFilterMaterialTo.class);
        List<ColorFilterMaterialTo> result = sjt.query(sql, rm, new Object[] {cpMaterialNum});
        
        if (result != null && result.size() > 0) {
            return result.get(0);
        } else {
            return null;
        }
        
    } 
    
    public ColorFilterMaterialTo findByPrimaryKey(String cfMaterialNum) {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        String sql = "select * from PIDB_COLOR_FILTER_MATERIAL Where COLOR_FILTER_MATERIAL_NUM = ?";
        
        logger.debug(sql);

        
        GenericRowMapper<ColorFilterMaterialTo> rm = new GenericRowMapper<ColorFilterMaterialTo>(ColorFilterMaterialTo.class);
        List<ColorFilterMaterialTo> result = sjt.query(sql, rm, new Object[] {cfMaterialNum});
        
        if (result != null && result.size() > 0) {
            return result.get(0);
        } else {
            return null;
        }
        
    } 

    public ColorFilterMaterialTo findByCpMaterialNum(String cpMaterialNum, String cpVariant) {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        String sql = "select * from PIDB_COLOR_FILTER_MATERIAL where COLOR_FILTER_MATERIAL_NUM = ? AND CP_VARIANT = ? ";
        
        logger.debug(sql);

        
        GenericRowMapper<ColorFilterMaterialTo> rm = new GenericRowMapper<ColorFilterMaterialTo>(ColorFilterMaterialTo.class);
        List<ColorFilterMaterialTo> result = sjt.query(sql, rm, new Object[] {cpMaterialNum, cpVariant});
        
        if (result != null && result.size() > 0) {
            return result.get(0);
        } else {
            return null;
        }
        
    } 
    public List<ColorFilterMaterialTo> getByProjCodeWVersion(final String projCodeWVersion) {
    	SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        String sql = "select * from PIDB_COLOR_FILTER_MATERIAL where PROJECT_CODE_W_VERSION = ?";
        GenericRowMapper<ColorFilterMaterialTo> rm = new GenericRowMapper<ColorFilterMaterialTo>(ColorFilterMaterialTo.class);
        return sjt.query(sql, rm, new Object[]{projCodeWVersion});
    }
    
    public List<ColorFilterMaterialTo> getAll () {
    	SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        String sql = "select * from PIDB_COLOR_FILTER_MATERIAL";
        GenericRowMapper<ColorFilterMaterialTo> rm = new GenericRowMapper<ColorFilterMaterialTo>(ColorFilterMaterialTo.class);
        return sjt.query(sql, rm, new Object[]{});
    }
    
    public ColorFilterMaterialTo getByProjCodeWVersionMaxVariant(final String projCodeWVersion) {
    	SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
    	String sql = "SELECT * FROM PIDB_COLOR_FILTER_MATERIAL B " +
    				" WHERE B.PROJECT_CODE_W_VERSION||'-VARIANT'||B.COLOR_FILTER_VARIANT IN ( " +
    				" SELECT A.PROJECT_CODE_W_VERSION||'-VARIANT'||MAX(A.COLOR_FILTER_VARIANT) COLOR_FILTER_VARIANT " +
    				" FROM PIDB_COLOR_FILTER_MATERIAL A " +
    				" WHERE A.PROJECT_CODE_W_VERSION = ? " +
    				" GROUP BY PROJECT_CODE_W_VERSION )";
    	       
        GenericRowMapper<ColorFilterMaterialTo> rm = new GenericRowMapper<ColorFilterMaterialTo>(ColorFilterMaterialTo.class);
        List<ColorFilterMaterialTo> result = sjt.query(sql, rm, new Object[] {projCodeWVersion});
        
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
	public List<ColorFilterMaterialTo> query(
			final ColorFilterMaterialQueryTo queryTo) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "";
		sql = "SELECT * " +
			 " FROM PIDB_COLOR_FILTER_MATERIAL where 1=1 ";

		String whereCause = generateWhereCause(queryTo);
		sql += whereCause;
		sql += " Order by COLOR_FILTER_MATERIAL_NUM";

		GenericRowMapper<ColorFilterMaterialTo> rm = new GenericRowMapper<ColorFilterMaterialTo>(
				ColorFilterMaterialTo.class);
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
	private String generateWhereCause(final ColorFilterMaterialQueryTo queryTo) {
		StringBuilder sb = new StringBuilder();

		if (queryTo.getColorFilterMaterialNum() != null
				&& !queryTo.getColorFilterMaterialNum().equals("")) {
			String queryString = getSmartSearchQueryString("COLOR_FILTER_MATERIAL_NUM",
					queryTo.getColorFilterMaterialNum());
			if (queryString != null) {
				sb.append(" and (" + queryString + " )");
			}
		}
		if (queryTo.getProjectCodeWVersion() != null
				&& !queryTo.getProjectCodeWVersion().equals("")) {
			String queryString = getSmartSearchQueryString(
					"PROJECT_CODE_W_VERSION", queryTo.getProjectCodeWVersion());
			if (queryString != null) {
				sb.append(" and (" + queryString + " )");
			}
		}
		if (queryTo.getColorFilterVariant() != null
				&& !"".equals(queryTo.getColorFilterVariant().trim())) {
			String queryString = getSmartSearchQueryString("COLOR_FILTER_VARIANT",
					queryTo.getColorFilterVariant());
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
	public int countResult(final ColorFilterMaterialQueryTo queryTo) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "";
		sql = " select count(*) from PIDB_COLOR_FILTER_MATERIAL where 1=1";
		String whereCause = generateWhereCause(queryTo);
		sql += whereCause;
		return sjt.queryForInt(sql, new Object[] {});
	}

    public void insert(final ColorFilterMaterialTo newInstance)  {
        if (null == newInstance) {
            logger.error("Error the new TSV object is null.");
        }
       /* super.insert(newInstance, "PIDB_IC_WAFER");*/
        TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(
                    final TransactionStatus status) {
                //int newIcWaferId = SequenceSupport.nextValue(SequenceSupport.SEQ_TRAD_PKG);

                SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();

                String sql = "Insert Into PIDB_COLOR_FILTER_MATERIAL " +
                "( COLOR_FILTER_MATERIAL_NUM, PROJ_CODE, " +
                " PROJECT_CODE_W_VERSION, DESCRIPTION, COLOR_FILTER_VARIANT, " +
                " TAPE_OUT_TYPE, FAB_DEVICE_ID, MASK_LAYER_COM, " +
                " TAPE_OUT_DATE, MASK_ID, REVISION_ITEM, MASK_HOUSE, " +
                " NEW_MASK_NUM, FETCH_MASK_LAYER_COM, S_LAYER, LIGHT_PIPE, " +
                " RBG_THICKNESS, ML_THICKNESS, ML_TYPE, REMARK, " +
                "  CREATED_BY,  MP_STATUS )"
                + " Values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                logger.debug(sql);
                sjt.update(sql, newInstance.getColorFilterMaterialNum(),
                        newInstance.getProjCode(), newInstance.getProjectCodeWVersion(),
                        newInstance.getDescription(), newInstance.getColorFilterVariant(),
                        newInstance.getTapeOutType(), newInstance.getFabDeviceId(),
                        newInstance.getMaskLayerCom(), newInstance.getTapeOutDate(),
                        newInstance.getMaskId(), newInstance.getRevisionItem(),
                        newInstance.getMaskHouse(), newInstance.getNewMaskNum(),
                        newInstance.getFetchMaskLayerCom(), newInstance.getSLayer(),
                        newInstance.getLightPipe(), newInstance.getRbgThickness(),
                        newInstance.getMlThickness(), newInstance.getMlType(),
                        newInstance.getRemark(), newInstance.getCreatedBy(),
                        newInstance.getMpStatus()
                );
            }
        };
        doInTransaction(callback);
    }

    public void update(final ColorFilterMaterialTo newInstance)  {
        if (null == newInstance) {
            logger.error("Error the new TSV object is null.");
        }
       /* super.insert(newInstance, "PIDB_IC_WAFER");*/
        TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(
                    final TransactionStatus status) {
                //int newIcWaferId = SequenceSupport.nextValue(SequenceSupport.SEQ_TRAD_PKG);

                SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();

                String sql = "Update PIDB_COLOR_FILTER_MATERIAL " +
                " SET DESCRIPTION = ?, TAPE_OUT_TYPE = ?, FAB_DEVICE_ID = ? , " +
                " MASK_LAYER_COM = ?, TAPE_OUT_DATE = ?, MASK_ID = ?, " +
                " REVISION_ITEM = ?, MASK_HOUSE = ?, NEW_MASK_NUM = ?, " +
                " FETCH_MASK_LAYER_COM = ?, S_LAYER = ?, " +
                " LIGHT_PIPE = ?, RBG_THICKNESS = ?, ML_THICKNESS = ?, " +
                " ML_TYPE = ?, REMARK = ?, MP_STATUS = ? , MODIFIED_BY = ?, " +
                " UPDATE_DATE = ? " +
                " WHERE COLOR_FILTER_MATERIAL_NUM = ?";
                
                logger.debug(sql);
                sjt.update(sql, 
                        newInstance.getDescription(), newInstance.getTapeOutType(), 
                        newInstance.getFabDeviceId(),
                        newInstance.getMaskLayerCom(), newInstance.getTapeOutDate(),
                        newInstance.getMaskId(), newInstance.getRevisionItem(),
                        newInstance.getMaskHouse(), newInstance.getNewMaskNum(),
                        newInstance.getFetchMaskLayerCom(), newInstance.getSLayer(),
                        newInstance.getLightPipe(), newInstance.getRbgThickness(),
                        newInstance.getMlThickness(), newInstance.getMlType(),
                        newInstance.getRemark(), newInstance.getMpStatus(),
                        newInstance.getModifiedBy(), newInstance.getUpdateDate(),
                        newInstance.getColorFilterMaterialNum()
                );
            }
        };
        doInTransaction(callback);
    }
}
