package com.cista.pidb.md.dao;

import java.util.List;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import com.cista.pidb.core.GenericRowMapper;
import com.cista.pidb.core.PIDBDaoSupport;
import com.cista.pidb.md.to.MpListCustomerMapTo;

public class MpListCustomerMapDao extends PIDBDaoSupport {
	/**
	 * @return List
	 */
	public List<MpListCustomerMapTo> findByPrimaryKey(final String partNum,
			final String icFgMaterialNum, final String projCodeWVersion,
			final String tapeName, final String pkgCode, final String process) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_MP_LIST_MAP_CUSTOMER where PART_NUM = ? "
				+ " and IC_FG_MATERIAL_NUM = ? and PROJ_CODE_W_VERSION = ? "
				+ " and TAPE_NAME = ? and PKG_CODE = ? and PROCESS = ?";
		GenericRowMapper<MpListCustomerMapTo> rm = new GenericRowMapper<MpListCustomerMapTo>(
				MpListCustomerMapTo.class);
		List<MpListCustomerMapTo> result = sjt.query(sql, rm, new Object[] {
				partNum, icFgMaterialNum, projCodeWVersion, tapeName, pkgCode,
				process });
		if (result != null && result.size() > 0) {
			return result;
		} else {
			return null;
		}
	}

	public void deleteCustomer(final String partNum,
			final String icFgMaterialNum, final String projCodeWVersion,
			final String tapeName, final String pkgCode, final String vendor,
			final String process, final String cust) {
		TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
			public void doInTransactionWithoutResult(
					final TransactionStatus status) {
				SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
				String sql = "delete from PIDB_MP_LIST_MAP_CUSTOMER where PART_NUM = ? "
						+ " and IC_FG_MATERIAL_NUM = ? and PROJ_CODE_W_VERSION = ? "
						+ " and TAPE_NAME = ? and PKG_CODE = ?  and PROCESS = ? and VENDOR = ? and CUST=?";
				logger.debug(sql);
				sjt.update(sql, partNum, icFgMaterialNum, projCodeWVersion,
						tapeName, pkgCode, vendor, process,cust);
			}
		};
		doInTransaction(callback);
	}

	public void deleteByPrimaryKey(final String partNum,
			final String icFgMaterialNum, final String projCodeWVersion,
			final String tapeName, final String pkgCode, final String process) {
		TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
			public void doInTransactionWithoutResult(
					final TransactionStatus status) {
				SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
				String sql = "delete from PIDB_MP_LIST_MAP_CUSTOMER where PART_NUM = ? "
						+ " and IC_FG_MATERIAL_NUM = ? and PROJ_CODE_W_VERSION = ? "
						+ " and TAPE_NAME = ? and PKG_CODE = ? and PROCESS = ?";
				logger.debug(sql);
				sjt.update(sql, partNum, icFgMaterialNum, projCodeWVersion,
						tapeName, pkgCode, process);
			}
		};
		doInTransaction(callback);
	}

}
