package com.cista.pidb.md.to;

import com.cista.pidb.core.BaseQueryTo;

public class BumpMaskQueryTo extends BaseQueryTo {

    private String projCode;
    
    private String projName;

    private String fab;

    private String projOption;

    private String maskName;

    private String fabDescr;

    public String getFabDescr() {
        return fabDescr;
    }

    public void setFabDescr(String fabDescr) {
        this.fabDescr = fabDescr;
    }

    public String getFab() {
        return fab;
    }

    public void setFab(String fab) {
        this.fab = fab;
    }

    public String getMaskName() {
        return maskName;
    }

    public void setMaskName(String maskName) {
        this.maskName = maskName;
    }

    public String getProjOption() {
        return projOption;
    }

    public void setProjOption(String _projOption) {
        this.projOption = _projOption;
    }
    public String getProjName() {
        return projName;
    }

    public void setProjName(String projName) {
        this.projName = projName;
    }

    public String getProjCode() {
        return projCode;
    }

    public void setProjCode(String projCode) {
        this.projCode = projCode;
    }
}
