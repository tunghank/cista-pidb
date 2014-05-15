package com.cista.pidb.md.dao;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import com.cista.pidb.core.GenericRowMapper;
import com.cista.pidb.core.PIDBDaoSupport;
import com.cista.pidb.md.to.TsvQueryTo;
import com.cista.pidb.md.to.TsvTo;

public class TsvDao extends PIDBDaoSupport{

	public List<TsvTo> query(TsvQueryTo queryTo) {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        String sql = "Select * From PIDB_TSV where 1=1";
        String sqlWhere = generateWhereCause(queryTo);
        sql = sql + sqlWhere;
        sql += " order by PROJ_NAME,PKG_NAME";
        logger.debug(sql);
        GenericRowMapper<TsvTo> rm = new GenericRowMapper<TsvTo>(TsvTo.class);
        if (queryTo.getPageNo() > 0) {
            int cursorFrom = (queryTo.getPageNo() - 1) * queryTo.getPageSize() + 1;
            int cursorTo = (queryTo.getPageNo()) * queryTo.getPageSize();
            return sjt.query(getPagingSql(sql, cursorFrom, cursorTo), rm, new Object[]{});
        } else {
            return sjt.query(sql, rm, new Object[]{});
        }
    }

    private String generateWhereCause(final TsvQueryTo queryTo) {
        StringBuilder sb = new StringBuilder();
        if (queryTo.getProjName() != null && !queryTo.getProjName().equals("")) {
            String prodCodeQueryString = getSmartSearchQueryString("PROJ_NAME", queryTo.getProjName());
            if (prodCodeQueryString != null) {
                sb.append(" and (" + prodCodeQueryString + " )");
            }
        }

        if (queryTo.getPkgName() != null && !queryTo.getPkgName().equals("")) {
            String pkgNameQueryString = getSmartSearchQueryString("PKG_NAME", queryTo.getPkgName());
            if (pkgNameQueryString != null) {
                sb.append(" and (" + pkgNameQueryString + " )");
            }
        }

        if (queryTo.getPkgCode() != null && !queryTo.getPkgCode().equals("")) {
            String pkgCodeQueryString = getSmartSearchQueryString("PKG_CODE", queryTo.getPkgCode());
            if (pkgCodeQueryString != null) {
                sb.append(" and (" + pkgCodeQueryString + " )");
            }
        }
        return sb.toString();
    }

    public int countResult(TsvQueryTo queryTo) {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        String sql = "Select count(*) From PIDB_TSV where 1=1 ";
        String sqlWhere = generateWhereCause(queryTo);
        sql = sql + sqlWhere;
        logger.debug(sql);
        return sjt.queryForInt(sql, new Object[]{});
    }
    
    public TsvTo findByPkgName(String name) {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        String sql = "select * from PIDB_TSV where PKG_NAME='" + name + "'";
        TsvTo tsvTo;
        try {
        	tsvTo = sjt.queryForObject(sql, new GenericRowMapper<TsvTo>(TsvTo.class),
               new Object[] {});
        } catch (Exception e) {
            //e.printStackTrace();
            return null;
        }
        return tsvTo;
    }

    public void insertTsv(final TsvTo newInstance)  {
        if (null == newInstance) {
            logger.error("Error the new TSV object is null.");
        }
       /* super.insert(newInstance, "PIDB_IC_WAFER");*/
        TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(
                    final TransactionStatus status) {
                //int newIcWaferId = SequenceSupport.nextValue(SequenceSupport.SEQ_TRAD_PKG);

                SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();

                String sql = "insert into PIDB_TSV "
                + "(PROJ_NAME,"
                + "PKG_NAME,"
                + "PKG_CODE,"
                + "TSV_TYPE,"
                + "PIN_COUNT,"
                + "LEAD_FRAME_TYPE,"
                + "EDHS_LAYER,"
                + "GREEN_PKG,"
                + "GOLDEN_WIRE_WIDTH,"
                + "BODY_SIZE,"
                + "MCP_DIE_QTY,"
                + "MCP_PKG,"
                + "SUBTRACT_LAYER,"
                + "ASSY_HOUSE1,"
                + "ASSY_HOUSE2,"
                + "ASSY_HOUSE3,"
                + "ASSY_HOUSE4,"
                + "ASSY_HOUSE5,"
                + "ASSY_HOUSE1_SPEC_NUM,"
                + "ASSY_HOUSE2_SPEC_NUM,"
                + "ASSY_HOUSE3_SPEC_NUM,"
                + "ASSY_HOUSE4_SPEC_NUM,"
                + "ASSY_HOUSE5_SPEC_NUM,"
                + "REMARK,"
                + "ASSIGN_TO,"
                + "ASSIGN_EMAIL,"
                + "CREATED_BY,"
                + "MODIFIED_BY,"
                + "MCP_TYPE,"
                + "PASSIVE_COMPONENT)"
                + " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                logger.debug(sql);
                sjt.update(sql, newInstance.getProjName(),
                        newInstance.getPkgName(), newInstance.getPkgCode(),
                        newInstance.getTsvType(), newInstance.getPinCount(),
                        newInstance.getLeadFrameType(), newInstance.getEdhsLayer(),
                        newInstance.getGreenPkg(), newInstance.getGoldenWireWidth(),
                        newInstance.getBodySize(), newInstance.getMcpDieQty(),
                        newInstance.getMcpPkg(), newInstance.getSubtractLayer(),
                        newInstance.getAssyHouse1(), newInstance.getAssyHouse2(),
                        newInstance.getAssyHouse3(), newInstance.getAssyHouse4(),
                        newInstance.getAssyHouse5(), newInstance.getAssyHouse1SpecNum(),
                        newInstance.getAssyHouse2SpecNum(), newInstance.getAssyHouse3SpecNum(),
                        newInstance.getAssyHouse4SpecNum(), newInstance.getAssyHouse5SpecNum(),
                        newInstance.getRemark(), newInstance.getAssignTo(),
                        newInstance.getAssignEmail(), newInstance.getCreatedBy(),
                        newInstance.getModifiedBy(),newInstance.getMcpType(),
                        newInstance.getPassiveComponent()
                );
            }
        };
        doInTransaction(callback);
    }

    public void updateTsv (final TsvTo newInstance) {
        if (newInstance == null) {
            logger.error("Error the parameter is null.");
        }
        /*Map<String, Object> keyMap = new HashMap<String, Object>();
        keyMap.put("PKG_NAME", newInstance.getPkgName());
        
        super.update(newInstance, "PIDB_TRADITIONAL_PKG", keyMap);*/
        TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(
                    final TransactionStatus status) {
                SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();

                String sql = "Update PIDB_TSV Set "
                + "PROJ_NAME = ?,"
                + "PKG_CODE = ?,"
                + "TSV_TYPE = ?,"
                + "PIN_COUNT = ?,"
                + "LEAD_FRAME_TYPE = ?,"
                + "EDHS_LAYER = ?,"
                + "GREEN_PKG = ?,"
                + "GOLDEN_WIRE_WIDTH = ?,"
                + "BODY_SIZE = ?,"
                + "MCP_DIE_QTY = ?,"
                + "MCP_PKG = ?,"
                + "SUBTRACT_LAYER = ?,"
                + "ASSY_HOUSE1 = ?,"
                + "ASSY_HOUSE2 = ?,"
                + "ASSY_HOUSE3 = ?,"
                + "ASSY_HOUSE4 = ?,"
                + "ASSY_HOUSE5 = ?,"
                + "ASSY_HOUSE1_SPEC_NUM = ?,"
                + "ASSY_HOUSE2_SPEC_NUM = ?,"
                + "ASSY_HOUSE3_SPEC_NUM = ?,"
                + "ASSY_HOUSE4_SPEC_NUM = ?,"
                + "ASSY_HOUSE5_SPEC_NUM = ?,"
                + "REMARK = ?,"
                + "ASSIGN_TO = ?,"
                + "ASSIGN_EMAIL = ?,"
                + "CREATED_BY = ?,"
                + "MODIFIED_BY = ?, " 
                + "MCP_TYPE = ?, " 
                + "PASSIVE_COMPONENT = ? " 
                + " Where PKG_NAME = ?";
                logger.debug(sql);
                sjt.update(sql, newInstance.getProjName(),
                         newInstance.getPkgCode(),
                        newInstance.getTsvType(), newInstance.getPinCount(),
                        newInstance.getLeadFrameType(), newInstance.getEdhsLayer(),
                        newInstance.getGreenPkg(), newInstance.getGoldenWireWidth(),
                        newInstance.getBodySize(), newInstance.getMcpDieQty(),
                        newInstance.getMcpPkg(), newInstance.getSubtractLayer(),
                        newInstance.getAssyHouse1(), newInstance.getAssyHouse2(),
                        newInstance.getAssyHouse3(), newInstance.getAssyHouse4(),
                        newInstance.getAssyHouse5(), newInstance.getAssyHouse1SpecNum(),
                        newInstance.getAssyHouse2SpecNum(), newInstance.getAssyHouse3SpecNum(),
                        newInstance.getAssyHouse4SpecNum(), newInstance.getAssyHouse5SpecNum(),
                        newInstance.getRemark(), newInstance.getAssignTo(),
                        newInstance.getAssignEmail(), newInstance.getCreatedBy(),
                        newInstance.getModifiedBy(), newInstance.getMcpType(),
                        newInstance.getPassiveComponent(), newInstance.getPkgName()
                );
            }
        };
        doInTransaction(callback);
    }
    
	public List<Map<String, Object>> findPkgCode(final String prodCode) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "Select Distinct PKG_CODE from PIDB_TSV where PKG_NAME=? and "
				+ getAssertEmptyString("PKG_CODE") + " order by PKG_CODE";

		return sjt.queryForList(sql, new Object[] { prodCode });
	}
}
