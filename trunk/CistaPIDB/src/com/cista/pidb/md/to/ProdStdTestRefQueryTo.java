package com.cista.pidb.md.to;

import com.cista.pidb.core.BaseQueryTo;

public class ProdStdTestRefQueryTo extends BaseQueryTo {
    private String productLine;

    private String productFamily;

    public String getProductFamily() {
        return productFamily;
    }

    public void setProductFamily(String productFamily) {
        this.productFamily = productFamily;
    }

    public String getProductLine() {
        return productLine;
    }

    public void setProductLine(String productLine) {
        this.productLine = productLine;
    }
}
