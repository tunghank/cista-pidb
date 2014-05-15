package com.cista.pidb.core;


public class BaseQueryTo {
    private int pageNo = 1;
    private int pageSize = Integer.parseInt(PIDBContext.getConfig("query.page.size"));
    private int totalResult;

    public int getPageNo() {
        return pageNo;
    }
    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }
    public int getPageSize() {
        return pageSize;
    }
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    public int getPageTotal() {
        int pageTotal = totalResult / pageSize;
        if (totalResult % pageSize != 0) {
            pageTotal++;
        }
        return pageTotal;
    }

    public int getTotalResult() {
        return totalResult;
    }
    public void setTotalResult(int totalResult) {
        this.totalResult = totalResult;
    }

    public String getPaging() {
        StringBuilder sb = new StringBuilder();
        String summary = PIDBContext.getMessage("query.paging.summary");
        summary = summary.replace("#total", totalResult + "");
        summary = summary.replace("#pagesize", pageSize + "");
        String shows = ((pageNo - 1) * pageSize + 1) + "-";
        shows += (pageNo * pageSize) < totalResult ? (pageNo * pageSize) : totalResult;
        summary = summary.replace("#show", shows);

        sb.append(summary);
        sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
        if (pageNo > 1) {
            sb.append("<input type='button' class='button' onclick='prevPage()' value='"
                    + PIDBContext.getMessage("query.paging.prev") + "'>");
        } else {
            sb.append("<input type='button' class='button' onclick='prevPage()' value='"
                    + PIDBContext.getMessage("query.paging.prev") + "' disabled='true'>");
        }

        sb.append("&nbsp;&nbsp;");

        if (pageNo < getPageTotal()) {
            sb.append("<input type='button' class='button' onclick='nextPage()' value='"
                    + PIDBContext.getMessage("query.paging.next") + "'>");
        } else {
            sb.append("<input type='button' class='button' onclick='nextPage()' value='"
                    + PIDBContext.getMessage("query.paging.next") + "' disabled='true'>");
        }
        return sb.toString();
    }

}
