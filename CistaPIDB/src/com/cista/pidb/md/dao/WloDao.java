package com.cista.pidb.md.dao;

import java.util.List;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import com.cista.pidb.core.GenericRowMapper;
import com.cista.pidb.core.PIDBDaoSupport;
import com.cista.pidb.md.to.WloQueryTo;
import com.cista.pidb.md.to.WloTo;

public class WloDao extends PIDBDaoSupport{
	
	
	public List<WloTo> query(WloQueryTo queryTo) {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        String sql = "Select * From PIDB_WLO where 1=1";
        String sqlWhere = generateWhereCause(queryTo);
        sql = sql + sqlWhere;
        sql += " Order by PROD_NAME";
        logger.debug(sql);
        GenericRowMapper<WloTo> rm = new GenericRowMapper<WloTo>(WloTo.class);
        if (queryTo.getPageNo() > 0) {
            int cursorFrom = (queryTo.getPageNo() - 1) * queryTo.getPageSize() + 1;
            int cursorTo = (queryTo.getPageNo()) * queryTo.getPageSize();
            return sjt.query(getPagingSql(sql, cursorFrom, cursorTo), rm, new Object[]{});
        } else {
            return sjt.query(sql, rm, new Object[]{});
        }
    }

    private String generateWhereCause(final WloQueryTo queryTo) {
        StringBuilder sb = new StringBuilder();
        if (queryTo.getMaterialNum() != null && !queryTo.getMaterialNum().equals("")) {
            String materialNumQueryString = getSmartSearchQueryString("MATERIAL_NUM", queryTo.getMaterialNum());
            if (materialNumQueryString != null) {
                sb.append(" and (" + materialNumQueryString + " )");
            }
        }

        if (queryTo.getProdType() != null && !queryTo.getProdType().equals("")) {
            String prodTypeQueryString = getSmartSearchQueryString("PROD_TYPE", queryTo.getProdType());
            if (prodTypeQueryString != null) {
                sb.append(" and (" + prodTypeQueryString + " )");
            }
        }
        
        if (queryTo.getVendorCode() != null && !queryTo.getVendorCode().equals("")) {
            String vendorCodeQueryString = getSmartSearchQueryString("VENDOR_CODE", queryTo.getVendorCode());
            if (vendorCodeQueryString != null) {
                sb.append(" and (" + vendorCodeQueryString + " )");
            }
        }
        
        if (queryTo.getProdName() != null && !queryTo.getProdName().equals("")) {
            String prodNameQueryString = getSmartSearchQueryString("PROD_NAME", queryTo.getProdName());
            if (prodNameQueryString != null) {
                sb.append(" and (" + prodNameQueryString + " )");
            }
        }
        return sb.toString();
    }

    public int countResult(WloQueryTo queryTo) {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        String sql = "Select count(*) From PIDB_WLO where 1=1 ";
        String sqlWhere = generateWhereCause(queryTo);
        sql = sql + sqlWhere;
        logger.debug(sql);
        return sjt.queryForInt(sql, new Object[]{});
    }

    
    public WloTo findByPrimaryKey(String _meterialNum) {
		SimpleJdbcTemplate stj = super.getSimpleJdbcTemplate();
		String sql = " SELECT A.MATERIAL_NUM, A.PART_NUM, PROJ_CODE, PROD_CODE, " +
				" A.DESCRIPTION, " +
				" A.PROD_NAME, A.PROD_TYPE, A.VENDOR_CODE, A.SERIAL_NUM, " +
				" A.RESERVED, A.LENS, A.PERSPECTIVE, " +
				" A.FNO, A.IR_COATING, A.EMI, A.THICKNESS_OF_GLUE, A.BARREL_TYPE, " +
				" A.CREATED_BY, A.APP_CATEGORY, A.MP_STATUS, " +
				" A.MODIFIED_BY, A.REMARK, " +
				" A.CDT, A.UDT, A.VENDOR_DEVICE, A.VARIANT, A.PACKING_TYPE  " +
				" FROM PIDB_WLO A " +
				" WHERE 1=1 " +
				" AND A.MATERIAL_NUM = ?";
		logger.debug(sql);
		List<WloTo> result = stj.query(sql,
				new GenericRowMapper<WloTo>(WloTo.class),
				new Object[] { _meterialNum });
		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			logger.warn("User with meterialNum = " + _meterialNum
					+ " is not found.");
			return null;
		}
    }
    
    public void insertWlo(final WloTo newInstance)  {
        if (null == newInstance) {
            logger.error("Error the new WLO object is null.");
        }
      
        TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(
                    final TransactionStatus status) {
                
                SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();

                String sql = "Insert into PIDB_WLO " +
                	"( MATERIAL_NUM, PART_NUM, PROJ_CODE, PROD_CODE, " +
                	" DESCRIPTION, PROD_NAME, " +
                	" PROD_TYPE, VENDOR_CODE, SERIAL_NUM, RESERVED, " +
                	" LENS, " +
                	" PERSPECTIVE, FNO, IR_COATING, EMI, THICKNESS_OF_GLUE, " +
                	" BARREL_TYPE, APP_CATEGORY, MP_STATUS," +
                	" CREATED_BY, REMARK, VENDOR_DEVICE, VARIANT, PACKING_TYPE  )"
                + " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                logger.debug(sql);
                
                sjt.update(sql, newInstance.getMaterialNum(),newInstance.getPartNum(),
                		newInstance.getProjCode(),newInstance.getProdCode(),
                		newInstance.getDescription(),
                        newInstance.getProdName(), newInstance.getProdType(),
                        newInstance.getVendorCode(),newInstance.getSerialNum(),
                        newInstance.getReserved(),
                        newInstance.getLens(), newInstance.getPerspective(),
                        newInstance.getFno(), newInstance.getIrCoating(),
                        newInstance.getEmi(), newInstance.getThicknessOfGlue(),
                        newInstance.getBarrelType(), 
                        newInstance.getAppCategory(),newInstance.getMpStatus(),
                        newInstance.getCreatedBy(), newInstance.getRemark(),
                        newInstance.getVendorDevice(),newInstance.getVariant(),
                        newInstance.getPackingType()
                );
                logger.debug(sjt.toString());
            }
        };
        doInTransaction(callback);
    }
    
    public void updateWlo (final WloTo newInstance) {
        if (newInstance == null) {
            logger.error("Error the parameter is null.");
        }

        TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(
                    final TransactionStatus status) {
                SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();

                String sql = "Update PIDB_WLO Set "
                + "DESCRIPTION = ?,"
                + "LENS = ?,"
                + "PERSPECTIVE = ?,"
                + "FNO = ?,"
                + "IR_COATING = ?,"
                + "EMI = ?,"
                + "THICKNESS_OF_GLUE = ?,"
                + "BARREL_TYPE = ?,"
                + "APP_CATEGORY = ?,"
                + "MP_STATUS = ?,"
                + "MODIFIED_BY = ?, " 
                + "REMARK = ?, " 
                + "UDT = ?, " 
                + "VENDOR_DEVICE = ? " 
                + " Where MATERIAL_NUM = ?";
                logger.debug(sql);
                sjt.update(sql, 
                		newInstance.getDescription(),
                		newInstance.getLens(),
                        newInstance.getPerspective(),
                        newInstance.getFno(),
                        newInstance.getIrCoating(),
                        newInstance.getEmi(),
                        newInstance.getThicknessOfGlue(),
                        newInstance.getBarrelType(),
                        newInstance.getAppCategory(),
                        newInstance.getMpStatus(),
                        newInstance.getModifiedBy(),
                        newInstance.getRemark(),
                        newInstance.getUdt(),
                        newInstance.getVendorDevice(),
                        newInstance.getMaterialNum()
                );
            }
        };
        doInTransaction(callback);
    }    
}
