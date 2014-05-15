package com.cista.pidb.md.dao;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import com.cista.pidb.core.*;
import com.cista.pidb.md.to.ZsSeqTo;

/**
 * search IcWafer.
 * 
 * @author smilly
 * 
 */
public class ZsSeqDao extends PIDBDaoSupport {

	

	public boolean isSeqNumExist(String seqZs, String seqType) {
		boolean ret = true;
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = " SELECT SEQ_ZS, SEQ_TYPE, SEQ_NUM "
				+ " FROM PIDB_ZS_SEQNUM  " + " WHERE SEQ_ZS =?"
				+ " AND SEQ_TYPE =?" ;
		logger.debug(sql);
		List<Map<String, Object>> result = sjt.queryForList(sql, new Object[] {
				seqZs, seqType });

		if (result != null && result.size() != 1) {
			ret= false;
		}
		return ret;
	}

	/**
	 * create an IcWafer object.
	 * 
	 * @param newInstance
	 */
	public void insertSeq(final ZsSeqTo newInstance) {
		if (newInstance == null) {
			logger.error("Error the parameter is null.");
		}
		TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
			protected void doInTransactionWithoutResult(
					final TransactionStatus status) {
				SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();

				String sql = "insert into PIDB_ZS_SEQNUM " + "(SEQ_ZS,"
						+ "SEQ_TYPE," + "SEQ_NUM)" + " values(?,?,?)";
				logger.debug(sql);
				sjt.update(sql, newInstance.getSeqZs(), newInstance
						.getSeqType(), newInstance.getSeqNum());

			}
		};
		doInTransaction(callback);
	}

	public String getSeqNum(String seqZs, String seqType) {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String SeqNum = "";

		String sql = "select SEQ_NUM from PIDB_ZS_SEQNUM where SEQ_ZS = ? AND SEQ_TYPE = ?";

		logger.debug(sql);
		List<Map<String, Object>> result = stj.queryForList(sql, new Object[] {
				seqZs, seqType });

		if (result != null && result.size() > 0) {
			Map<String, Object> item = result.get(0);
			SeqNum =  item.get("SEQ_NUM").toString();
		}
		return SeqNum;

	}

	public void updateSeqNum(final ZsSeqTo newInstance) {
		if (newInstance == null) {
			logger.error("Error the parameter is null.");
		}
				SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
				String sql = "update PIDB_ZS_SEQNUM set " + "SEQ_NUM=? "
						+ "where SEQ_ZS=? and SEQ_TYPE =?";
				logger.debug(sql);
				sjt.update(sql, newInstance.getSeqNum(),newInstance.getSeqZs()
						,newInstance.getSeqType());
			}
	
	private String generateWhereCause(final ZsSeqTo zsSeqTo) {
		StringBuilder sb = new StringBuilder();
		if (zsSeqTo.getSeqZs() != null && !zsSeqTo.getSeqZs().equals("")) {
			String seqZs = getSmartSearchQueryString("SEQ_ZS", zsSeqTo
					.getSeqZs());
			if (seqZs != null) {
				sb.append(" and (" + seqZs + " )");
			}
		}

		if (zsSeqTo.getSeqType() != null && !zsSeqTo.getSeqType().equals("")) {
			String seqType = getSmartSearchQueryString("SEQ_TYPE", zsSeqTo
					.getSeqType());
			if (seqType != null) {
				sb.append(" and (" + seqType + " )");
			}
		}
		return sb.toString();
	}
}
