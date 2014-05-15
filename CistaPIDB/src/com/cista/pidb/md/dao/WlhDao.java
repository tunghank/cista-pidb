package com.cista.pidb.md.dao;

import java.util.List;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import com.cista.pidb.core.GenericRowMapper;
import com.cista.pidb.core.PIDBDaoSupport;
import com.cista.pidb.md.to.WlhQueryTo;
import com.cista.pidb.md.to.WlhTo;

public class WlhDao extends PIDBDaoSupport{
	
	
	public List<WlhTo> query(WlhQueryTo queryTo) {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        String sql = "Select * From PIDB_WLH where 1=1";
        String sqlWhere = generateWhereCause(queryTo);
        sql = sql + sqlWhere;
        sql += " Order by PROD_NAME";
        logger.debug(sql);
        GenericRowMapper<WlhTo> rm = new GenericRowMapper<WlhTo>(WlhTo.class);
        if (queryTo.getPageNo() > 0) {
            int cursorFrom = (queryTo.getPageNo() - 1) * queryTo.getPageSize() + 1;
            int cursorTo = (queryTo.getPageNo()) * queryTo.getPageSize();
            return sjt.query(getPagingSql(sql, cursorFrom, cursorTo), rm, new Object[]{});
        } else {
            return sjt.query(sql, rm, new Object[]{});
        }
    }

    private String generateWhereCause(final WlhQueryTo queryTo) {
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

    public int countResult(WlhQueryTo queryTo) {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        String sql = "Select count(*) From PIDB_WLH where 1=1 ";
        String sqlWhere = generateWhereCause(queryTo);
        sql = sql + sqlWhere;
        logger.debug(sql);
        return sjt.queryForInt(sql, new Object[]{});
    }

    
    public WlhTo findByPrimaryKey(String _meterialNum) {
		SimpleJdbcTemplate stj = super.getSimpleJdbcTemplate();
		String sql = " SELECT A.* " +
				" FROM PIDB_WLH A " +
				" WHERE 1=1 " +
				" AND A.MATERIAL_NUM = ?";
		logger.debug(sql);
		List<WlhTo> result = stj.query(sql,
				new GenericRowMapper<WlhTo>(WlhTo.class),
				new Object[] { _meterialNum });
		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			logger.warn("User with meterialNum = " + _meterialNum
					+ " is not found.");
			return null;
		}
    }
    
    public void insertWlh(final WlhTo newInstance)  {
        if (null == newInstance) {
            logger.error("Error the new WLH object is null.");
        }
      
        TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(
                    final TransactionStatus status) {
                
                SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();

                String sql = "Insert into PIDB_WLH " +
                	"( MATERIAL_NUM, PART_NUM, PROJ_CODE, PROD_CODE, " +
                	" DESCRIPTION, PROD_NAME, " +
                	" PROD_TYPE, VENDOR_CODE, SERIAL_NUM, VARIANT, RESERVED, " +
                	" APP_CATEGORY, MP_STATUS, PACKING_TYPE, " +
                	" CREATED_BY, REMARK , VENDOR_DEVICE, " +
                	" X_DIM, Y_DIM, Z_DIM, REF_WLO_MATERIAL)"
                + " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                logger.debug(sql);
                
                sjt.update(sql, newInstance.getMaterialNum(),newInstance.getPartNum(),
                		newInstance.getProjCode(),newInstance.getProdCode(),
                		newInstance.getDescription(),
                        newInstance.getProdName(), newInstance.getProdType(),
                        newInstance.getVendorCode(),newInstance.getSerialNum(),
                        newInstance.getVariant(),newInstance.getReserved(),
   
                        newInstance.getAppCategory(),newInstance.getMpStatus(),
                        newInstance.getPackingType(),
                        newInstance.getCreatedBy(), newInstance.getRemark(),
                        newInstance.getVendorDevice(),newInstance.getXDim(),
                        newInstance.getYDim(), newInstance.getZDim(),
                        newInstance.getRefWloMaterial()
                );
                logger.debug(sjt.toString());
            }
        };
        doInTransaction(callback);
    }
    
    public void updateWlh (final WlhTo newInstance) {
        if (newInstance == null) {
            logger.error("Error the parameter is null.");
        }

        TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(
                    final TransactionStatus status) {
                SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();

                String sql = "Update PIDB_WLH Set "
                + "DESCRIPTION = ?,"
                + "APP_CATEGORY = ?,"
                + "MP_STATUS = ?,"
                + "REMARK = ?, " 
                + "VENDOR_DEVICE = ?, " 
                + "X_DIM = ?, " 
                + "Y_DIM = ?, " 
                + "Z_DIM = ?, " 
                + "MODIFIED_BY = ?, " 
                + "UDT = ? " 
                + " Where MATERIAL_NUM = ?";
                logger.debug(sql);
                sjt.update(sql, 
                		newInstance.getDescription(),
                        newInstance.getAppCategory(),
                        newInstance.getMpStatus(),
                        newInstance.getRemark(),
                        newInstance.getVendorDevice(),
                        newInstance.getXDim(),
                        newInstance.getYDim(),
                        newInstance.getZDim(),
                        newInstance.getModifiedBy(),
                        newInstance.getUdt(),
                        newInstance.getMaterialNum()
                );
            }
        };
        doInTransaction(callback);
    }    
}
