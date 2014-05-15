package com.cista.pidb.md.dao;

import java.util.List;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import com.cista.pidb.core.GenericRowMapper;
import com.cista.pidb.core.PIDBDaoSupport;
import com.cista.pidb.md.to.MpListEolCustTo;

/**
 * .
 * 
 * @author Hu Meixia
 * 
 */
public class MpListEolCustDao extends PIDBDaoSupport {

	/**
	 * @return List
	 */
	public List<MpListEolCustTo> findByPrimaryKey(final String partNum,
			final String icFgMaterialNum, final String projCodeWVersion,
			final String tapeName, final String pkgCode) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_MP_LIST_EOL where PART_NUM = ? " +
				" and IC_FG_MATERIAL_NUM = ? and PROJ_CODE_W_VERSION = ? "
			+ " and TAPE_NAME = ? and PKG_CODE = ? ";
		GenericRowMapper<MpListEolCustTo> rm = new GenericRowMapper<MpListEolCustTo>(
				MpListEolCustTo.class);
		List<MpListEolCustTo> result = sjt.query(sql, rm, new Object[] { partNum,
				icFgMaterialNum, projCodeWVersion, tapeName, pkgCode });
		if (result != null && result.size() > 0) {
			return result;
		} else {
			return null;
		}
	}
	
	public List<MpListEolCustTo> findByPrimaryKey(final MpListEolCustTo newInstance) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_MP_LIST_EOL where PART_NUM = ? " +
				" and IC_FG_MATERIAL_NUM = ? and PROJ_CODE_W_VERSION = ? "
			+ " and TAPE_NAME = ? and PKG_CODE = ? and EOL_CUST = ?";
		GenericRowMapper<MpListEolCustTo> rm = new GenericRowMapper<MpListEolCustTo>(
				MpListEolCustTo.class);
		List<MpListEolCustTo> result = sjt.query(sql, rm, new Object[] { 
				newInstance.getPartNum(),
				newInstance.getIcFgMaterialNum(), 
				newInstance.getProjCodeWVersion(), 
				newInstance.getTapeName(), 
				newInstance.getPkgCode(),
				newInstance.getEolCust()});
		if (result != null && result.size() > 0) {
			return result;
		} else {
			return null;
		}
	}

	public void deleteEOLCust(final String partNum,
			final String icFgMaterialNum, final String projCodeWVersion,
			final String tapeName, final String pkgCode, final String eolCust) {
		TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
			public void doInTransactionWithoutResult(
					final TransactionStatus status) {
				SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
				String sql = "delete from PIDB_MP_LIST_EOL where PART_NUM = ? " +
				" and IC_FG_MATERIAL_NUM = ? and PROJ_CODE_W_VERSION = ? "
				+ " and TAPE_NAME = ? and PKG_CODE = ? and EOL_CUST = ? ";
				logger.debug(sql);
				sjt.update(sql, partNum, icFgMaterialNum, projCodeWVersion, 
						tapeName, pkgCode, eolCust);
			}
		};
		doInTransaction(callback);
	}

	public void deleteByPrimaryKey(final String partNum,
			final String icFgMaterialNum, final String projCodeWVersion,
			final String tapeName, final String pkgCode) {
		TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
			public void doInTransactionWithoutResult(
					final TransactionStatus status) {
				SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
				String sql = "delete from PIDB_MP_LIST_EOL where PART_NUM = ? " +
				" and IC_FG_MATERIAL_NUM = ? and PROJ_CODE_W_VERSION = ? "
				+ " and TAPE_NAME = ? and PKG_CODE = ? ";
				logger.debug(sql);
				sjt.update(sql, partNum, icFgMaterialNum, projCodeWVersion, 
						tapeName, pkgCode);
			}
		};
		doInTransaction(callback);
	}
	
	public void updateEOLCust(final MpListEolCustTo newInstance) {
		if (newInstance == null) {
			logger.error("Error the parameter is null.");
		}
		TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
			protected void doInTransactionWithoutResult(
					final TransactionStatus status) {
				SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
				String sql = "Update PIDB_MP_LIST_EOL Set REMARK=? " +
						" Where PART_NUM = ? " +
				" and IC_FG_MATERIAL_NUM = ? and PROJ_CODE_W_VERSION = ? "
			+ " and TAPE_NAME = ? and PKG_CODE = ? ";
				logger.debug(sql);
				sjt.update(sql, newInstance.getRemark(), 
						        newInstance.getPartNum(),
						        newInstance.getIcFgMaterialNum(), 
						        newInstance.getProjCodeWVersion(),
						        newInstance.getTapeName(),
						        newInstance.getPkgCode()
						        
							);
			}
		};
		doInTransaction(callback);
	}
}
