package com.cista.pidb.md.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import com.cista.pidb.core.GenericRowMapper;
import com.cista.pidb.core.PIDBDaoSupport;
import com.cista.pidb.md.to.ZsHdcpKeyQueryTo;
import com.cista.pidb.md.to.ZsHdcpKeyTo;

/**
 * search IcWafer.
 * 
 * @author smilly
 * 
 */
public class ZsHdcpKeyDao extends PIDBDaoSupport {

	public List<ZsHdcpKeyTo> findBySearchPage(
			final ZsHdcpKeyQueryTo findInstance) {
		SimpleJdbcTemplate stj = super.getSimpleJdbcTemplate();
		String sql = "select * from PIDB_HDCP_KEY where 1=1";
		if (findInstance.getReleaseTo() != null
				&& !findInstance.getReleaseTo().equals("")) {
			if (findInstance.getReleaseTo().equals("HX")
					|| findInstance.getReleaseTo().equals("WP")) {
				sql += " AND (RELEASE_TO ='" + findInstance.getReleaseTo() + "'"
						+ " or RELEASE_TO ='ALL')";
			}
		}
		String where = generateWhereCause(findInstance);
		sql = sql + where;
		sql += " order by MATERIAL_NUM";
		logger.debug(sql);

		if (findInstance.getPageNo() > 0) {
			int cursorFrom = (findInstance.getPageNo() - 1)
					* findInstance.getPageSize() + 1;
			int cursorTo = (findInstance.getPageNo())
					* findInstance.getPageSize();
			return stj.query(getPagingSql(sql, cursorFrom, cursorTo),
					new GenericRowMapper<ZsHdcpKeyTo>(ZsHdcpKeyTo.class),
					new Object[] {});
		} else {
			return stj.query(sql, new GenericRowMapper<ZsHdcpKeyTo>(
					ZsHdcpKeyTo.class), new Object[] {});
		}

	}

	public List<ZsHdcpKeyTo> findAll() {
		SimpleJdbcTemplate stj = super.getSimpleJdbcTemplate();
		String sql = "select * from PIDB_HDCP_KEY";
		logger.debug(sql);
		return stj.query(sql, new GenericRowMapper<ZsHdcpKeyTo>(
				ZsHdcpKeyTo.class), new Object[] {});
	}

	public ZsHdcpKeyTo findByPrimaryKey(final String _meterialNum) {
		SimpleJdbcTemplate stj = super.getSimpleJdbcTemplate();
		String sql = "select * from PIDB_HDCP_KEY where MATERIAL_NUM = ?";
		logger.debug(sql);
		List<ZsHdcpKeyTo> result = stj.query(sql,
				new GenericRowMapper<ZsHdcpKeyTo>(ZsHdcpKeyTo.class),
				new Object[] { _meterialNum });
		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			logger.warn("User with meterialNum = " + _meterialNum
					+ " is not found.");
			return null;
		}
	}

	/**
	 * create an IcWafer object.
	 * 
	 * @param newInstance
	 */
	public void insertHdcpKey(final ZsHdcpKeyTo newInstance) {
		if (null == newInstance) {
			logger.error("Error the new HDCP KEY object is null.");
		}
		TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
			protected void doInTransactionWithoutResult(
					final TransactionStatus status) {
				/*
				 * int newSdramId = SequenceSupport
				 * .nextValue(SequenceSupport.SEQ_PIDB_SDRAM);
				 */

				SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();

				String sql = "insert into PIDB_HDCP_KEY " + "(MATERIAL_NUM,"
						+ "DESCRIPTION," + "APPLICATION_PRODUCT," + "REMARK,"
						+ "CREATED_BY," + "MODIFIED_BY,"
						+ "VENDOR_CODE, RELEASE_TO)"
						+ " values(?,?,?,?,?,?,?,?)";
				logger.debug(sql);
				sjt.update(sql, newInstance.getMaterialNum(), newInstance
						.getDescription(), newInstance.getApplicationProduct(),
						newInstance.getRemark(), newInstance.getCreatedBy(),
						newInstance.getModifiedBy(), newInstance
								.getVendorCode(), newInstance.getReleaseTo());

			}
		};
		doInTransaction(callback);
	}

	public void deleteByPrimaryKey(final String meterialNum) {
		TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
			public void doInTransactionWithoutResult(
					final TransactionStatus status) {
				SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
				String sql = "delete from PIDB_HDCP_KEY where MATERIAL_NUM =? ";
				logger.debug(sql);
				sjt.update(sql, meterialNum);
			}
		};
		doInTransaction(callback);
	}

	public void updateSdram(final ZsHdcpKeyTo newInstance) {
		if (newInstance == null) {
			logger.error("Error the parameter is null.");
		}
		TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
			protected void doInTransactionWithoutResult(
					final TransactionStatus status) {
				SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
				String sql = "update PIDB_HDCP_KEY set " + "MATERIAL_NUM=?,"
						+ "DESCRIPTION=?," + "APPLICATION_PRODUCT=?,"
						+ "REMARK=?," + "CREATED_BY=?," + "MODIFIED_BY=?,"
						+ "VENDOR_CODE=? , RELEASE_TO=?"
						+ "where MATERIAL_NUM=? ";
				logger.debug(sql);
				sjt.update(sql, newInstance.getMaterialNum(), newInstance
						.getDescription(), newInstance.getApplicationProduct(),
						newInstance.getRemark(), newInstance.getCreatedBy(),
						newInstance.getModifiedBy(), newInstance
								.getVendorCode(), newInstance.getReleaseTo(),
						newInstance.getMaterialNum());
			}
		};
		doInTransaction(callback);
	}

	public int countResult(ZsHdcpKeyQueryTo zsHdcpKeyQueryTo) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select count(*) from PIDB_HDCP_KEY where 1=1";
		if (zsHdcpKeyQueryTo.getReleaseTo() != null
				&& !zsHdcpKeyQueryTo.getReleaseTo().equals("")) {
			if (zsHdcpKeyQueryTo.getReleaseTo().equals("HX")
					|| zsHdcpKeyQueryTo.getReleaseTo().equals("WP")) {
				sql += " AND (RELEASE_TO ='" + zsHdcpKeyQueryTo.getReleaseTo() + "'"
						+ " or RELEASE_TO ='ALL')";
			}
		}
		String where = generateWhereCause(zsHdcpKeyQueryTo);
		sql = sql + where;
		return sjt.queryForInt(sql, new Object[] {});
	}

	private String generateWhereCause(final ZsHdcpKeyQueryTo queryTo) {
		StringBuilder sb = new StringBuilder();
		if (queryTo.getMaterialNum() != null
				&& !queryTo.getMaterialNum().equals("")) {
			String materialNumQueryString = getSmartSearchQueryString(
					"MATERIAL_NUM", queryTo.getMaterialNum());
			if (materialNumQueryString != null) {
				sb.append(" and (" + materialNumQueryString + " )");
			}
		}

		if (queryTo.getVendorCode() != null
				&& !queryTo.getVendorCode().equals("")) {
			String VendorQueryString = getSmartSearchQueryString("VENDOR_CODE",
					queryTo.getVendorCode());
			if (VendorQueryString != null) {
				sb.append(" and (" + VendorQueryString + " )");
			}
		}
		if (queryTo.getApplicationProduct() != null
				&& !queryTo.getApplicationProduct().equals("")) {
			String applicationProduct = getSmartSearchQueryAllLike(
					"APPLICATION_PRODUCT", queryTo.getApplicationProduct());
			if (applicationProduct != null) {
				sb.append("and (" + applicationProduct + ")");
			}
		}

		/*if (queryTo.getReleaseTo() != null
				&& !queryTo.getReleaseTo().equals("")) {
			String releaseTo = getSmartSearchQueryString("RELEASE_TO", queryTo
					.getReleaseTo());
			if (releaseTo != null) {
				sb.append(" and (" + releaseTo + " )");
			}
		}*/
		return sb.toString();
	}

	public List<String> findMaterialNum() {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select distinct MATERIAL_NUM from PIDB_HDCP_KEY order by MATERIAL_NUM";
		logger.debug(sql);
		List<Map<String, Object>> result = stj.queryForList(sql,
				new Object[] {});
		List<String> materialNums = new ArrayList<String>();
		if (result != null && result.size() > 0) {
			for (Map<String, Object> item : result) {
				materialNums.add((String) item.get("MATERIAL_NUM"));
			}
		}
		return materialNums;
	}

	public List<String> findTapeShortName(final String vendorCode) {

		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "Select SHORT_NAME TAPE_VENDOR from WM_SAP_MASTER_VENDOR where ("
				+ super.getSmartSearchQueryString("VENDOR_CODE", vendorCode)
				+ ") order by VENDOR_CODE";
		logger.debug(sql);

		List<Map<String, Object>> result = stj.queryForList(sql,
				new Object[] {});
		List<String> hdcpKey = new ArrayList<String>();

		if (result != null && result.size() > 0) {
			for (Map<String, Object> item : result) {
				hdcpKey.add((String) item.get("TAPE_VENDOR"));
			}
		}
		return hdcpKey;
	}

}
