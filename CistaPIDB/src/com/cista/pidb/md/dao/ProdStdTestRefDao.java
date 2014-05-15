package com.cista.pidb.md.dao;

import java.util.List;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.cista.pidb.core.GenericRowMapper;
import com.cista.pidb.core.PIDBDaoSupport;
import com.cista.pidb.md.to.ProdStdTestRefQueryTo;
import com.cista.pidb.md.to.ProdStdTestRefTo;
/**
 * .
 * @author Hu Meixia
 *
 */
public class ProdStdTestRefDao extends PIDBDaoSupport {
    /**
     * .
     * @param projCode String
     * @return List
     */
    public List<ProdStdTestRefTo> findByProjCode(final String projCode) {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        String sql = "select * from PIDB_PROD_STD_TEST_REF ppstr, PIDB_PROJECT pp left join PIDB_PROJECT_CODE ppc"
             + " on pp.PROJ_NAME=ppc.PROJ_NAME where ppc.PROJ_CODE=? and pp.PROD_FAMILY=ppstr.PRODUCT_FAMILY"
             + " and pp.PROD_LINE=ppstr.PRODUCT_LINE";

        GenericRowMapper<ProdStdTestRefTo> rm = new GenericRowMapper<ProdStdTestRefTo>(
                ProdStdTestRefTo.class);

        return sjt.query(sql, rm, new Object[] {projCode});
    }

    /**
     * .
     * @param testReferenceId String
     * @return ProdStdTestRefTo
     */
    public ProdStdTestRefTo findByTestReferenceId(final String testReferenceId) {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        String sql = "select * from PIDB_PROD_STD_TEST_REF where TEST_REFERENCE_ID = ?";
        GenericRowMapper<ProdStdTestRefTo> rm = new GenericRowMapper<ProdStdTestRefTo>(ProdStdTestRefTo.class);
        List<ProdStdTestRefTo> result = sjt.query(sql, rm, new Object[]{testReferenceId});
        if (result != null && result.size() > 0) {
            return result.get(0);
        } else {
            return null;
        }
    }

    /**
     * .
     * @param productLine String
     * @param productFamily String
     * @return Object
     */
    public Object findMaxVar(final String productLine, final String productFamily) {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        String sql = "select max(VARIANT) from PIDB_PROD_STD_TEST_REF where PRODUCT_LINE = ? and PRODUCT_FAMILY = ?";
        logger.debug(sql);
        Object obj = sjt.queryForObject(sql, Object.class, new Object[]{productLine, productFamily});
        return obj;
    }

    /**
     * .
     * @param queryTo ProdStdTestRefQueryTo
     * @return int
     */
    public int countResult(final ProdStdTestRefQueryTo queryTo) {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        String sql = "";
        sql = "select count(*) from PIDB_PROD_STD_TEST_REF where 1 = 1 ";
        String whereCause = generateWhereCause(queryTo);
        sql += whereCause;
        return sjt.queryForInt(sql, new Object[]{});
    }

    /**
     * .
     * @param queryTo ProdStdTestRefQueryTo
     * @return List
     */
    public List<ProdStdTestRefTo> query(final ProdStdTestRefQueryTo queryTo) {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        String sql = "select distinct TEST_REFERENCE_ID,"
        + "VARIANT,"
        + "PRODUCT_LINE,"
        + "PRODUCT_FAMILY,"
        + "CP_INDEX_TIME_E,"
        + "CP_CPU_TIME_E,"
        + "CP_TESTER_E,"
        + "CP_TESTER_SPEC_E,"
        + "FT_INDEX_TIME_E,"
        + "FT_CPU_TIME_E,"
        + "FT_TESTER_E,"
        + "FT_TESTER_SPEC_E,"
        + "NOTE,"
        + "ASSIGN_TO,"
        + "ASSIGN_EMAIL,"
        + "CREATED_BY,"
        + "MODIFIED_BY"
        + " from PIDB_PROD_STD_TEST_REF where 1 = 1 ";
//        }
        String whereCause = generateWhereCause(queryTo);
        sql += whereCause;

        GenericRowMapper<ProdStdTestRefTo> rm = new GenericRowMapper<ProdStdTestRefTo>(ProdStdTestRefTo.class);
        if (queryTo.getPageNo() > 0) {
            int cursorFrom = (queryTo.getPageNo() - 1) * queryTo.getPageSize() + 1;
            int cursorTo = (queryTo.getPageNo()) * queryTo.getPageSize();
            return sjt.query(getPagingSql(sql, cursorFrom, cursorTo), rm, new Object[]{});
        } else {
            return sjt.query(sql, rm, new Object[]{});
        }
    }

    /**
     * .
     * @param queryTo ProdStdTestRefQueryTo
     * @return String
     */
    private String generateWhereCause(final ProdStdTestRefQueryTo queryTo) {
        StringBuilder sb = new StringBuilder();
        if (queryTo.getProductLine() != null && !queryTo.getProductLine().equals("")) {
            String queryString = getSmartSearchQueryString("PRODUCT_LINE", queryTo.getProductLine());
            if (queryString != null) {
                sb.append(" and (" + queryString + " )");
            }
        }
        if (queryTo.getProductFamily() != null && !queryTo.getProductFamily().equals("")) {
            String queryString = getSmartSearchQueryString("PRODUCT_FAMILY", queryTo.getProductFamily());
            if (queryString != null) {
                sb.append(" and (" + queryString + " )");
            }
        }

        return sb.toString();
    }
}
