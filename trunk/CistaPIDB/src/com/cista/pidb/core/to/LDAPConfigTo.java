package com.cista.pidb.core.to;

/********************************************************************
 * Modify History:
 * Date Time  |  Modifier  |Description
 * 2007/09/09 |  Hank_Tang |加入可以讀取多個AD Function.   
 ********************************************************************/

public class LDAPConfigTo {
	

	private String ldapSite;
	private String ldapUrl;
	private String ldapFactory;
	private String ldapCn;
	private String ldapAuthentication;
	private String ldapUsername;
	private String ldapPassword;

    public String getLdapSite() {
        return ldapSite;
    }

    public void setLdapSite(String ldapSite) {
        this.ldapSite = ldapSite;
    }

    public String getLdapUrl() {
        return ldapUrl;
    }

    public void setLdapUrl(String ldapUrl) {
        this.ldapUrl = ldapUrl;
    }

    public String getLdapFactory() {
        return ldapFactory;
    }

    public void setLdapFactory(String ldapFactory) {
        this.ldapFactory = ldapFactory;
    }
    
    public String getLdapCn() {
        return ldapCn;
    }

    public void setLdapCn(String ldapCn) {
        this.ldapCn = ldapCn;
    }

    public String getLdapAuthentication() {
        return ldapAuthentication;
    }

    public void setLdapAuthentication(String ldapAuthentication) {
        this.ldapAuthentication = ldapAuthentication;
    }
    
    public String getLdapUsername() {
        return ldapUsername;
    }

    public void setLdapUsername(String ldapUsername) {
        this.ldapUsername = ldapUsername;
    }

    public String getLdapPassword() {
        return ldapPassword;
    }

    public void setLdapPassword(String ldapPassword) {
        this.ldapPassword = ldapPassword;
    }
}
