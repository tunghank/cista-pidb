package com.cista.pidb.core;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * The base support dao class. All the dao sub-class should extend from
 * BaseDaoSupport.
 * 
 * @author Matrix
 * 
 */
public class BaseDaoSupport extends SimpleJdbcDaoSupport {
	private TransactionTemplate transactionTemplate;

	private static final NameConverter NAME_CONVERTER = new DefaultNameConverter();

	/**
	 * Default constructor.
	 * 
	 */
	public BaseDaoSupport() {
		super();
	}

	/**
	 * Return default TransactionTemplate for transaction management.
	 * 
	 * @return TransactionTemplate
	 */
	protected TransactionTemplate getTransactionTemplate() {
		return transactionTemplate;
	}

	/**
	 * Return default TransactionTemplate for transaction management.
	 * 
	 * @param tt
	 *            TransactionTemplate
	 */
	protected void setTransactionTemplate(final TransactionTemplate tt) {
		transactionTemplate = tt;
	}

	/**
	 * Execute operation in transaction.
	 * 
	 * @param callback
	 *            Something need to do in transaction.
	 */
	public void doInTransaction(final TransactionCallbackWithoutResult callback) {
		transactionTemplate.execute(callback);
	}

	/**
	 * return right string for SQL.
	 * 
	 * @param value
	 *            A string.
	 * @return null or a string quoted by single quote.
	 */
	protected String getSQLString(final String value) {
		if (value == null) {
			return "NULL";
		} else {
			return "'" + toDBString(value) + "'";
		}
	}

	/**
	 * This method is used to replace the "'" string to "''".
	 * 
	 * @param str
	 *            the String to format
	 * @return The proper String.
	 */
	private String toDBString(final String str) {
		if (str == null) {
			return "";
		}
		StringBuffer dbStr = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c == '\'') {
				dbStr.append("''");
			} else {
				dbStr.append(c);
			}
		}
		return dbStr.toString().trim();
	}

	/**
	 * return right string for SQL.
	 * 
	 * @param value
	 *            A string.
	 * @return null or a string quoted by single quote.
	 */
	protected String getLikeSQLString(final String value) {
		if (value == null) {
			return "NULL";
		} else {
			return "'" + toDBString(value) + "%'";
		}
	}

	/**
	 * return right string for SQL.
	 * 
	 * @param value
	 *            A string.
	 * @return null or a string quoted by single quote.
	 */
	protected String getLikeSQL(final String value) {
		if (value == null) {
			return "NULL";
		} else {
			return "'%" + toDBString(value) + "%'";
		}
	}

	/**
	 * This method is used to replace the "'" string to "''".
	 * 
	 * @param str
	 *            the String to format
	 * @return The proper String.
	 * @deprecated
	 */
	@SuppressWarnings("unused")
	private static String toLikeDBString(final String str) {
		if (str == null) {
			return "";
		}
		StringBuffer dbStr = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			switch (c) {
			case '\'':
				dbStr.append("''");
				break;
			case '%':
				dbStr.append("[%]");
				break;
			case '[':
				dbStr.append("[[]");
				break;
			case '_':
				dbStr.append("[_]");
				break;
			default:
				dbStr.append(c);
			}
		}
		return dbStr.toString();
	}

	protected String getSQLDateString(final Date d) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return getSQLDateString(sdf.format(d));
	}

	protected String getSQLDateString(final String str) {
		return "to_date('" + str + "', 'YYYY-MM-DD HH24:MI:SS')";
	}

	protected String getSQLDateString(final String str, final String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			return getSQLDateString(sdf.parse(str));
		} catch (ParseException e) {
			logger.error("Parse date " + str + " fail.", e);
		}
		return null;
	}

	public void insert(Object newInstance, String tableName) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		Field[] fs = newInstance.getClass().getDeclaredFields();
		String sql = new String("insert into " + tableName + " ");
		StringBuilder sqlCol = new StringBuilder();
		StringBuilder sqlVal = new StringBuilder();
		Object[] val = new Object[fs.length];

		for (int i = 0; i < fs.length; i++) {
			Field element = fs[i];
			String beanName = element.getName();
			Object o = BeanHelper.getPropertyValue(newInstance, beanName);
			String columnName = NAME_CONVERTER.java2db(beanName);

			sqlCol.append(",").append(columnName);
			sqlVal.append(",?");
			val[i] = o;
		}

		if (fs.length > 0) {
			sql = sql + "(" + sqlCol.substring(1) + ") values ("
					+ sqlVal.substring(1) + ")";
		}
		logger.debug(sql);
		sjt.update(sql, val);
	}

	public void update(Object newInstance, String tableName,
			Map<String, Object> key) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		Field[] fs = newInstance.getClass().getDeclaredFields();

		String sql = "update " + tableName + " set ";
		StringBuilder sqlCol = new StringBuilder();
		StringBuilder sqlCrt = new StringBuilder();
		Object[] val = new Object[fs.length + key.size()];

		for (int i = 0; i < fs.length; i++) {
			Field element = fs[i];
			String beanName = element.getName();
			Object o = BeanHelper.getPropertyValue(newInstance, beanName);
			String columnName = NAME_CONVERTER.java2db(beanName);

			sqlCol.append(",").append(columnName).append("=").append("?");
			val[i] = o;
		}
		if (fs.length > 0) {
			sql = sql + sqlCol.substring(1) + " where ";
		}

		int j = 0;
		for (String o : key.keySet()) {
			sqlCrt.append(" and ").append(o).append("=? ");
			val[fs.length + j] = key.get(o);
			j++;
		}
		if (key.size() > 0) {
			sql = sql + sqlCrt.substring(5);
		}

		logger.debug(sql);

		sjt.update(sql, val);
	}

	protected String getPagingSql(String orginalSql, int cursorFrom,
			int cursorTo) {
		if (cursorTo < cursorFrom) {
			return orginalSql;
		} else {
			// Hank Bug Fix 2007/11/02
			/*
			 * System.out.println("SQL " + "SELECT * FROM (SELECT ROWNUM
			 * ROW_NUM, a.* FROM (" + orginalSql + ") a order by ROW_NUM) WHERE
			 * ROW_NUM >= " + cursorFrom + " and ROW_NUM <= " + cursorTo );
			 */

			return "SELECT * FROM (SELECT ROWNUM ROW_NUM, a.* FROM ("
					+ orginalSql + ") a order by ROW_NUM) WHERE ROW_NUM >= "
					+ cursorFrom + " and ROW_NUM <= " + cursorTo;

		}
	}
	
	protected String getSmartSearchQueryAllLike(String columnName, String criteria) {
		StringBuilder sb = new StringBuilder();
		if (criteria != null && !criteria.equals("")) {
			String[] subCriteria = criteria.split(",");
			String subSql = "";
			for (String subCri : subCriteria) {
				sb.append(" or " + columnName + " like "
								+ getLikeSQL(subCri));
			}
			sb.append(subSql);
		}
		if (sb.length() > 0) {
			return sb.toString().substring(4);
		} else {
			return null;
		}
	}
	
	
	
	

	protected String getSmartSearchQueryLike(String columnName, String criteria) {
		StringBuilder sb = new StringBuilder();
		if (criteria != null && !criteria.equals("")) {
			String[] subCriteria = criteria.split(",");
			String subSql = "";
			for (String subCri : subCriteria) {
				sb.append(" or " + columnName + " like "
								+ getLikeSQLString(subCri));
			}
			sb.append(subSql);
		}
		if (sb.length() > 0) {
			return sb.toString().substring(4);
		} else {
			return null;
		}
	}

	protected String getSmartSearchQueryString(String columnName,
			String criteria) {
		StringBuilder sb = new StringBuilder();
		if (criteria != null && !criteria.equals("")) {
			String[] subCriteria = criteria.split(",");
			String subSql = "";
			for (String subCri : subCriteria) {
				subCri = subCri.trim();
				if (subCri.equals("*")) {
					// Find a "*", the criteria will query ALL.
					return null;
				}

				boolean like = false;
				if (subCri.startsWith("*")) {
					like = true;
					subCri = "%" + subCri.substring(1);
				}
				if (subCri.endsWith("*")) {
					like = true;
					subCri = subCri.substring(0, subCri.length() - 1) + "%";
				}

				if (like) {
					sb.append(" or " + columnName + " like "
							+ getSQLString(subCri) );
				} else {
					sb.append(" or " + columnName + " = "
							+ getSQLString(subCri));
				}
			}
			sb.append(subSql);
		}
		if (sb.length() > 0) {
			return sb.toString().substring(4);
		} else {
			return null;
		}
	}

	/**
	 * build judge empty SQL.
	 * 
	 * @param value
	 *            columnName string
	 * @return String
	 */
	protected String getAssertEmptyString(final String columnName) {
		return columnName != null && !columnName.equals("") ? columnName
				+ " is not null and length(trim(" + columnName
				+ ")) is not null " : " 1=1 ";
	}
}
