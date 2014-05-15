package com.cista.pidb.md.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import com.cista.pidb.core.GenericRowMapper;
import com.cista.pidb.core.PIDBDaoSupport;
import com.cista.pidb.md.to.CspQueryTo;
import com.cista.pidb.md.to.CspTo;

public class CspDao extends PIDBDaoSupport{
	public List<CspTo> query(CspQueryTo queryTo) {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        String sql = "select * from PIDB_CSP where 1=1";
        String sqlWhere = generateWhereCause(queryTo);
        sql = sql + sqlWhere;
        sql += " order by PROJ_NAME,PKG_NAME";
        logger.debug(sql);
        GenericRowMapper<CspTo> rm = new GenericRowMapper<CspTo>(CspTo.class);
        if (queryTo.getPageNo() > 0) {
            int cursorFrom = (queryTo.getPageNo() - 1) * queryTo.getPageSize() + 1;
            int cursorTo = (queryTo.getPageNo()) * queryTo.getPageSize();
            return sjt.query(getPagingSql(sql, cursorFrom, cursorTo), rm, new Object[]{});
        } else {
            return sjt.query(sql, rm, new Object[]{});
        }
    }

     public int countResult(CspQueryTo queryTo) {
            SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
            String sql = "select count(*) from PIDB_CSP where 1=1 ";
            String sqlWhere = generateWhereCause(queryTo);
            sql = sql + sqlWhere;
            logger.debug(sql);
            return sjt.queryForInt(sql, new Object[]{});
        }

     private String generateWhereCause(final CspQueryTo queryTo) {
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

    public List<CspTo> findAll() {
        SimpleJdbcTemplate stj =  super.getSimpleJdbcTemplate();
        String sql = "select * from PIDB_TRADITIONAL_PKG";
       // logger.debug(sql);
        return stj.query(sql, new GenericRowMapper<CspTo>(CspTo.class),
                new Object[] {});
   }

    public void insertCsp(final CspTo newInstance)  {
            if (null == newInstance) {
                logger.error("Error the new CSP object is null.");
            }
           /* super.insert(newInstance, "PIDB_IC_WAFER");*/
            TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
                protected void doInTransactionWithoutResult(
                        final TransactionStatus status) {
                    //int newIcWaferId = SequenceSupport.nextValue(SequenceSupport.SEQ_TRAD_PKG);

                    SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();

                    String sql = "insert into PIDB_CSP "
                    + "(PROJ_NAME,"
                    + "PKG_NAME,"
                    + "PKG_CODE,"
                    + "CSP_TYPE,"
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
                            newInstance.getCspType(), newInstance.getPinCount(),
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

    public void updateCsp (final CspTo newInstance) {
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

                String sql = "update PIDB_CSP set "
                + "PROJ_NAME = ?,"
                + "PKG_CODE = ?,"
                + "CSP_TYPE = ?,"
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
                + " where PKG_NAME = ?";
                logger.debug(sql);
                sjt.update(sql, newInstance.getProjName(),
                         newInstance.getPkgCode(),
                        newInstance.getCspType(), newInstance.getPinCount(),
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

    public CspTo findByPkgName(String name) {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        String sql = "select * from PIDB_Csp where PKG_NAME='" + name + "'";
        CspTo pkgTo;
        try {
            pkgTo = sjt.queryForObject(sql, new GenericRowMapper<CspTo>(CspTo.class),
               new Object[] {});
        } catch (Exception e) {
            //e.printStackTrace();
            return null;
        }
        return pkgTo;
    }

    public List<String> findPkgName() {
        SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
        String sql = "select distinct PKG_NAME from PIDB_CSP order by PKG_NAME";
        logger.debug(sql);
        List<Map<String, Object>> result = stj.queryForList(sql, new Object[] {});
        List<String> pkgNames = new ArrayList<String>();
        if (result != null && result.size() > 0) {
            for (Map<String, Object> item : result) {
                pkgNames.add((String) item.get("PKG_NAME"));
            }
        }
        return pkgNames;
    }

    public List<String> findPkgCode() {
        SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
        String sql = "select distinct PKG_CODE from PIDB_CSP order by PKG_CODE";
        logger.debug(sql);
        List<Map<String, Object>> result = stj.queryForList(sql, new Object[] {});
        List<String> pkgCodes = new ArrayList<String>();
        if (result != null && result.size() > 0) {
            for (Map<String, Object> item : result) {
                pkgCodes.add((String) item.get("PKG_CODE"));
            }
        }
        return pkgCodes;
    }
    
    public List<String> findPkgCodeByPkgCode(final String pkgCode) {
        SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
        String sql = "select distinct PKG_CODE from PIDB_CSP where (" + super.getSmartSearchQueryString("PKG_CODE" , pkgCode) + ") order by PKG_CODE";
        logger.debug(sql);
        List<Map<String, Object>> result = stj.queryForList(sql, new Object[] {});
        List<String> pkgCodes = new ArrayList<String>();
        if (result != null && result.size() > 0) {
            for (Map<String, Object> item : result) {
                pkgCodes.add((String) item.get("PKG_CODE"));
            }
        }
        return pkgCodes;
    }

    public List<String> findDistAllProdCode() {
        SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
        String sql = "select distinct PROD_CODE from PIDB_CSP order by PROD_CODE";
        logger.debug(sql);
        List<Map<String, Object>> result = stj.queryForList(sql, new Object[] {});
        List<String> prodCodes = new ArrayList<String>();
        if (result != null && result.size() > 0) {
            for (Map<String, Object> item : result) {
               prodCodes.add((String) item.get("PROD_CODE"));
            }
        }
        return prodCodes;
    }

    public List<Map<String, Object>> findByProdCode(String prodCode) {
        SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
        String sql = "select Distinct PKG_CODE as PKG_CODE from PIDB_CSP where "
        + getAssertEmptyString("PKG_CODE");
        logger.debug(sql);
        return stj.queryForList(sql, new Object[] {});
    }

    public List<String> findPkgTypeByPkgCode(final String pkgCode) {
        SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
        String sql = "";
        List<Map<String, Object>> result;
        if (pkgCode != null && pkgCode.length() > 0) {
            sql = "select distinct TRAD_PKG_TYPE from PIDB_CSP where PKG_CODE = ? order by TRAD_PKG_TYPE";
            logger.debug(sql);
            result = stj.queryForList(sql, new Object[] {pkgCode});
        } else {
            sql = "select distinct TRAD_PKG_TYPE from PIDB_CSP order by TRAD_PKG_TYPE";
            logger.debug(sql);
            result = stj.queryForList(sql, new Object[] {});
        }
        List<String> pkgTypes = new ArrayList<String>();
        if (result != null && result.size() > 0) {
            for (Map<String, Object> item : result) {
                pkgTypes.add((String) item.get("CSP_TYPE"));
            }
        }
        return pkgTypes;
    }
   
    public List<CspTo> findByPkgCode(String pkgCode) {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        
        String sql = "select * from PIDB_CSP where PKG_CODE = ?";

        return sjt.query(sql, new GenericRowMapper<CspTo>(CspTo.class),
                new Object[] {pkgCode});
    }  
    
    public List<Map<String, Object>> findByProjCode(String projCode) {
        SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
        String sql = "select Distinct ptk.PKG_CODE as PKG_CODE from PIDB_CSP ptk left join PIDB_PROJECT_CODE ppc on ptk.PROJ_NAME = ppc.PROJ_NAME where "
        + getAssertEmptyString("ptk.PKG_CODE") + " and ppc.PROJ_CODE = ? order by ptk.PKG_CODE";
        logger.debug(sql);
        return stj.queryForList(sql, new Object[] {projCode});
    }    
    
    public List<CspTo> findByProjNameAndPkgCode(String projName, String pkgCode) {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        
        String sql = "select * from PIDB_CSP where PROJ_NAME = ? and PKG_CODE = ?";

        return sjt.query(sql, new GenericRowMapper<CspTo>(CspTo.class),
                new Object[] {projName,pkgCode});
    }
    
    public List<CspTo> findByPkgNameList(String pkgName) {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        
        String sql = "select * from PIDB_CSP where PKG_NAME = ?";

        return sjt.query(sql, new GenericRowMapper<CspTo>(CspTo.class),
                new Object[] {pkgName});
    }

}
