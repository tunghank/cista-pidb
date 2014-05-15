package com.cista.pidb.md.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import com.cista.pidb.core.GenericRowMapper;
import com.cista.pidb.core.PIDBDaoSupport;
import com.cista.pidb.core.SequenceSupport;
import com.cista.pidb.md.to.CornerLotCharQueryTo;
import com.cista.pidb.md.to.CornerLotCharTo;


public class CornerLotCharDao extends PIDBDaoSupport {

    public void insertCornerLotChar(final CornerLotCharTo newInstance) {
        if (null == newInstance) {
            logger.error("Error the new CornerLotChar object is null.");
        }
        /* super.insert(newInstance, "PIDB_IC_WAFER"); */
        TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(
                    final TransactionStatus status) {
                int newIcWaferId = SequenceSupport
                        .nextValue(SequenceSupport.SEQ_CORNER_LOT_CHAR);
                SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
                String sql = "insert into PIDB_CORNER_LOT_CHAR "
                        + "(PROD_CODE," + "PROJ_CODE_W_VERSION,"
                        + "DEVICE_OWNER," + "CORNER_LOT_ID,"
                        + "CORNER_LOT_ANALY_STATUS,"
                        + "CORNER_LOT_ANALY_FINISH_DATE," + "CHAR_LOT_ID,"
                        + "CHAR_LOT_ANALY_STATUS,"
                        + "CHAR_LOT_ANALY_FINISH_DATE," + "CHAR_ANALY_SUMMARY,"
                        + "REMARK," + "ASSIGN_TO," + "ASSIGN_EMAIL,"
                        + "CREATED_BY," + "MODIFIED_BY,"
                        + "CORNER_LOT_YIELD_ANALY_SUM)"
                        + " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                logger.debug(sql);
                sjt.update(sql, newInstance.getProdCode(), newInstance
                        .getProjCodeWVersion(), newInstance.getDeviceOwner(),
                        newInstance.getCornerLotId(), newInstance
                                .getCornerLotAnalyStatus(), newInstance
                                .getCornerLotAnalyFinishDate(), newInstance
                                .getCharLotId(), newInstance
                                .getCharLotAnalyStatus(), newInstance
                                .getCharLotAnalyFinishDate(), newInstance
                                .getCharAnalySummary(),
                        newInstance.getRemark(), newInstance.getAssignTo(),
                        newInstance.getAssignEmail(), newInstance
                                .getCreatedBy(), newInstance.getModifiedBy(),
                        newInstance.getCornerLotYieldAnalySum());
            }
        };
        doInTransaction(callback);
    }

    public void updateCornerLotChar(final CornerLotCharTo newInstance) {
        if (newInstance == null) {
            logger.error("Error the parameter is null.");
        }
        TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(
                    final TransactionStatus status) {
                SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
                String sql = "update PIDB_CORNER_LOT_CHAR set "
                        + "PROD_CODE = ?,"
                        + "DEVICE_OWNER = ?,"
                        + "CORNER_LOT_ID = ?,"
                        + "CORNER_LOT_ANALY_STATUS = ?,"
                        + "CORNER_LOT_ANALY_FINISH_DATE = ?,"
                        + "CHAR_LOT_ID = ?,"
                        + "CHAR_LOT_ANALY_STATUS = ?,"
                        + "CHAR_LOT_ANALY_FINISH_DATE = ?,"
                        + "CHAR_ANALY_SUMMARY = ?,"
                        + "REMARK = ?,"
                        + "ASSIGN_TO = ?,"
                        + "ASSIGN_EMAIL = ?,"
                        + "CREATED_BY = ?,"
                        + "MODIFIED_BY = ?,"
                        + "CORNER_LOT_YIELD_ANALY_SUM = ? where PROJ_CODE_W_VERSION = ?";
                sjt.update(sql, newInstance.getProdCode(), newInstance
                        .getDeviceOwner(), newInstance.getCornerLotId(),
                        newInstance.getCornerLotAnalyStatus(), newInstance
                                .getCornerLotAnalyFinishDate(), newInstance
                                .getCharLotId(), newInstance
                                .getCharLotAnalyStatus(), newInstance
                                .getCharLotAnalyFinishDate(), newInstance
                                .getCharAnalySummary(),
                        newInstance.getRemark(), newInstance.getAssignTo(),
                        newInstance.getAssignEmail(), newInstance
                                .getCreatedBy(), newInstance.getModifiedBy(),
                        newInstance.getCornerLotYieldAnalySum(), newInstance
                                .getProjCodeWVersion());
            }
        };
        
        doInTransaction(callback);
    }

    public CornerLotCharTo findByProjCodeWVersion(String version) {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        String sql = "select * from PIDB_CORNER_LOT_CHAR where PROJ_CODE_W_VERSION='"
                + version + "'";
        CornerLotCharTo cornerTo;
        try {
            cornerTo = sjt
                    .queryForObject(sql, new GenericRowMapper<CornerLotCharTo>(
                            CornerLotCharTo.class), new Object[] {});
        } catch (Exception e) {
            // e.printStackTrace();
            return null;
        }
        return cornerTo;
    }

    public List<CornerLotCharTo> getAll() {
        SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
        String sql = "select * from PIDB_CORNER_LOT_CHAR order by PROD_CODE";
        logger.debug(sql);
        List<CornerLotCharTo> result = stj.query(sql,
                new GenericRowMapper<CornerLotCharTo>(CornerLotCharTo.class),
                new Object[] {});
        return result;
    }

    private String generateWhereCause(final CornerLotCharQueryTo queryTo) {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotEmpty(queryTo.getProdCode())) {
            String prodCode = getSmartSearchQueryString("pclc.PROD_CODE",
                    queryTo.getProdCode());
            if (prodCode != null) {
                sb.append(" and (" + prodCode + " )");
            }            
        }
        
        if (StringUtils.isNotEmpty(queryTo.getProjName())) {
            String projName = getSmartSearchQueryString("ppj.PROJ_NAME",
                    queryTo.getProjName());
            if (projName != null) {
                sb.append(" and (" + projName + " )");
            }            
        }
        
        if (StringUtils.isNotEmpty(queryTo.getFab())) {
            sb.append(" and ppj.FAB = " + getSQLString(queryTo.getFab()) + " ");
        }
        
        if (StringUtils.isNotEmpty(queryTo.getProjOption())) {
            sb.append(" and ppjc.PROJ_OPTION = " + getSQLString(queryTo.getProjOption()) + " ");
        }
        
        if (StringUtils.isNotEmpty(queryTo.getProjCodeWVersion())) {
            String projCodeWVersion = getSmartSearchQueryString("pclc.PROJ_CODE_W_VERSION",
                    queryTo.getProjCodeWVersion());
            if (projCodeWVersion != null) {
                sb.append(" and (" + projCodeWVersion + " )");
            }            
        }
        
        return sb.toString();
    }
    
    public List<CornerLotCharQueryTo> queryByQueryPage(
            final CornerLotCharQueryTo queryTo) {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        String sql = "select distinct pclc.PROD_CODE,ppj.PROJ_NAME,ppj.FAB,ppjc.PROJ_OPTION,pclc.PROJ_CODE_W_VERSION from PIDB_CORNER_LOT_CHAR pclc,PIDB_IC_WAFER piw,PIDB_PROJECT ppj,PIDB_PROJECT_CODE ppjc where ppj.PROJ_NAME=ppjc.PROJ_NAME and piw.PROJ_CODE_W_VERSION=pclc.PROJ_CODE_W_VERSION and piw.PROJ_CODE=ppjc.PROJ_CODE ";
        String whereCause = generateWhereCause(queryTo);
        sql += whereCause;
        sql += " order by pclc.PROJ_CODE_W_VERSION";
        
        logger.debug(sql);
        GenericRowMapper<CornerLotCharQueryTo> rm = new GenericRowMapper<CornerLotCharQueryTo>(
                CornerLotCharQueryTo.class);

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
    
    public List<CornerLotCharTo> queryForDomain(
            final CornerLotCharQueryTo queryTo) {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        String sql = "select distinct pclc.* from PIDB_CORNER_LOT_CHAR pclc,PIDB_IC_WAFER piw,PIDB_PROJECT ppj,PIDB_PROJECT_CODE ppjc where ppj.PROJ_NAME=ppjc.PROJ_NAME and piw.PROJ_CODE_W_VERSION=pclc.PROJ_CODE_W_VERSION and piw.PROJ_CODE=ppjc.PROJ_CODE ";
        String whereCause = generateWhereCause(queryTo);
        sql += whereCause;
        sql += " order by pclc.PROJ_CODE_W_VERSION";

        logger.debug(sql);
        GenericRowMapper<CornerLotCharTo> rm = new GenericRowMapper<CornerLotCharTo>(
                CornerLotCharTo.class);

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

//    public void insertToTempTable(final List<String> projCodeList) {
//
//        String tempSql = "insert into PIDB_TEMP_PROJECT_NAME values (?)";
//        BatchPreparedStatementSetter bpss = new BatchPreparedStatementSetter() {
//            public int getBatchSize() {
//                return projCodeList.size();
//            }
//
//            public void setValues(PreparedStatement ps, int i) {
//                try {
//                    ps.setString(1, projCodeList.get(i));
//                } catch (SQLException e) {
//                    logger.error(e);
//                }
//            }
//        };
//        getJdbcTemplate().batchUpdate(tempSql, bpss);
//
//    }

    public int countResult(final CornerLotCharQueryTo queryTo) {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        String sql = "select count(1) from PIDB_CORNER_LOT_CHAR pclc,PIDB_IC_WAFER piw,PIDB_PROJECT ppj,PIDB_PROJECT_CODE ppjc where ppj.PROJ_NAME=ppjc.PROJ_NAME and piw.PROJ_CODE_W_VERSION=pclc.PROJ_CODE_W_VERSION and piw.PROJ_CODE=ppjc.PROJ_CODE ";
        String whereCause = generateWhereCause(queryTo);
        sql += whereCause;
        logger.debug(sql);
        return sjt.queryForInt(sql, new Object[] {});        
    }
}
