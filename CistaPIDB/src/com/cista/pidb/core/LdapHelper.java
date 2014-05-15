package com.cista.pidb.core;

/********************************************************************
 * Modify History:
 * Date Time  |  Modifier  |Description
 * 2007/09/09 |  Hank_Tang |加入可以讀取多個AD Function.   
 ********************************************************************/


import java.util.Hashtable;
import java.util.List;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cista.pidb.admin.to.UserTo;
import com.cista.pidb.core.dao.LDAPConfigDao;
import com.cista.pidb.core.to.LDAPConfigTo;

public class LdapHelper {
    private static final Log LOGGER = LogFactory.getLog(LdapHelper.class);

    /**
     * Added:Hank_Tang 2007/09/09
     * For:整合各Site AD
     * @param userId
     * @return
     */
    public static UserTo getOUUser(final String userId) {
    	LDAPConfigDao config = new LDAPConfigDao();
    	List ldapList = (List)config.getAll();
    	for (int i=0;i < ldapList.size(); i++){
    		LDAPConfigTo configTo = (LDAPConfigTo)ldapList.get(i);
    		
    		Hashtable<String, Object> env = new Hashtable<String, Object>();
            env.put(Context.PROVIDER_URL, configTo.getLdapUrl());
            env.put(Context.INITIAL_CONTEXT_FACTORY, configTo.getLdapFactory());
            env.put(Context.SECURITY_AUTHENTICATION, configTo.getLdapAuthentication());
            env.put(Context.SECURITY_PRINCIPAL, configTo.getLdapUsername());
            env.put(Context.SECURITY_CREDENTIALS, configTo.getLdapPassword());

    		
            try {
                LdapContext ctx = new InitialLdapContext(env, null);
                
                LOGGER.debug("Login by " + configTo.getLdapUsername()
                        + " successfully.");

                SearchControls searchCtls = new SearchControls();
                searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
                String searchFilter = "(sAMAccountName=" + userId + ")";
                String searchBase = configTo.getLdapCn().toString();

                NamingEnumeration answer = ctx.search(searchBase, searchFilter,
                        searchCtls);
                
                if (answer.hasMoreElements()) {
                    SearchResult sr = (SearchResult) answer.next();
                    Attributes attrs = sr.getAttributes();

                    LOGGER.debug("NEW Find user " + userId + ".");

                    UserTo user = new UserTo();
                    user.setUserId(userId);
                    Attribute a = null;
                    //Get email
                    a = attrs.get("mail");
                    if (a != null) {
                        user.setEmail(a.get().toString());
                    }
                    //Get last name
                    a = attrs.get("sn");
                    if (a != null) {
                        user.setLastName(a.get().toString());
                    }
                    //Get first name
                    a = attrs.get("givenName");
                    if (a != null) {
                        user.setFirstName(a.get().toString());
                    }
                    return user;
                }
                
            } catch (AuthenticationException ae) {
                LOGGER.debug(ae);
                LOGGER.debug("Authentication fail, incorrect username or password.");
               
            } catch (NamingException ne) {
                LOGGER.debug(ne);
                LOGGER.debug("Access to AD server fail.");
                ne.printStackTrace();
                
            }

    	}
    	LOGGER.debug("Can not find user with name " + userId);
    	return null;
    }
    
    public static UserTo getUser(final String userId) {
        Hashtable<String, Object> env = new Hashtable<String, Object>();
        env.put(Context.PROVIDER_URL, PIDBContext.getConfig("ldap.url"));
        env.put(Context.INITIAL_CONTEXT_FACTORY, PIDBContext
                .getConfig("ldap.factory"));
        env.put(Context.SECURITY_AUTHENTICATION, PIDBContext
                .getConfig("ldap.authentication"));
        env.put(Context.SECURITY_PRINCIPAL, "CN="
                + PIDBContext.getConfig("ldap.administrator.username").toString() + ", "
                + PIDBContext.getConfig("ldap.cn").toString());
        env.put(Context.SECURITY_CREDENTIALS, PIDBContext
                .getConfig("ldap.administrator.password"));

		
        try {
            LdapContext ctx = new InitialLdapContext(env, null);
            LOGGER.debug("Login by "
                    + PIDBContext.getConfig("ldap.administrator.username")
                    + " successfully.");

            SearchControls searchCtls = new SearchControls();
            searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            String searchFilter = "(sAMAccountName=" + userId + ")";
            String searchBase = PIDBContext.getConfig("ldap.cn").toString();

            NamingEnumeration answer = ctx.search(searchBase, searchFilter,
                    searchCtls);
            if (answer.hasMoreElements()) {
                SearchResult sr = (SearchResult) answer.next();
                Attributes attrs = sr.getAttributes();

                LOGGER.debug("Find user " + userId
                        + ".");

                UserTo user = new UserTo();
                user.setUserId(userId);
                Attribute a = null;
                //Get email
                a = attrs.get("mail");
                if (a != null) {
                    user.setEmail(a.get().toString());
                }
                //Get last name
                a = attrs.get("sn");
                if (a != null) {
                    user.setLastName(a.get().toString());
                }
                //Get first name
                a = attrs.get("givenName");
                if (a != null) {
                    user.setFirstName(a.get().toString());
                }
                return user;
            } else {
                LOGGER.debug("Can not find user with name " + userId);
                return null;
            }

        } catch (AuthenticationException ae) {
            LOGGER.debug(ae);
            LOGGER.debug("Authentication fail, incorrect username or password.");
        } catch (NamingException ne) {
            LOGGER.debug(ne);
            LOGGER.debug("Access to AD server fail.");
        }
        return null;
    }

    public static boolean checkOUUser(final String userId, final String password) {
    	LDAPConfigDao config = new LDAPConfigDao();
    	List ldapList = (List)config.getAll();
    	for (int i=0;i < ldapList.size(); i++){
    		LDAPConfigTo configTo = (LDAPConfigTo)ldapList.get(i);
    		
    		Hashtable<String, Object> env = new Hashtable<String, Object>();
            env.put(Context.PROVIDER_URL, configTo.getLdapUrl());
            env.put(Context.INITIAL_CONTEXT_FACTORY, configTo.getLdapFactory());
            env.put(Context.SECURITY_AUTHENTICATION, configTo.getLdapAuthentication());
            //env.put(Context.SECURITY_PRINCIPAL, configTo.getLdapUsername());
            env.put(Context.SECURITY_PRINCIPAL, getOUUserCN(userId) );
            env.put(Context.SECURITY_CREDENTIALS, password);

	        try {
	            LdapContext ctx = new InitialLdapContext(env, null);
	            LOGGER.debug("Login by " + userId + " successfully.");
	            return true;
	        } catch (AuthenticationException ae) {
	            LOGGER.debug(ae);
	            LOGGER.debug("Authentication fail, incorrect username or password.");
	        } catch (NamingException ne) {
	            LOGGER.debug(ne);
	            LOGGER.debug("Access to AD server fail.");
	        }
    	}
        return false;
    }
    
    
    public static boolean checkUser(final String userId, final String password) {
        Hashtable<String, Object> env = new Hashtable<String, Object>();
        env.put(Context.PROVIDER_URL, PIDBContext.getConfig("ldap.url"));
        env.put(Context.INITIAL_CONTEXT_FACTORY, PIDBContext
                .getConfig("ldap.factory"));
        env.put(Context.SECURITY_AUTHENTICATION, PIDBContext
                .getConfig("ldap.authentication"));
        env.put(Context.SECURITY_PRINCIPAL, "CN="
                + getUserCN(userId) + ", "
                + PIDBContext.getConfig("ldap.cn").toString());
        env.put(Context.SECURITY_CREDENTIALS, password);
        try {
            LdapContext ctx = new InitialLdapContext(env, null);
            LOGGER.debug("Login by "
                    + userId
                    + " successfully.");
            return true;
        } catch (AuthenticationException ae) {
            LOGGER.debug(ae);
            LOGGER.debug("Authentication fail, incorrect username or password.");
        } catch (NamingException ne) {
            LOGGER.debug(ne);
            LOGGER.debug("Access to AD server fail.");
        }
        return false;
    }

    /**
     * Find user's CN by sAMAccountName.
     * @param userId sAMAccountName
     * @return CN
     */
    public static String getUserCN(final String userId) {
        Hashtable<String, Object> env = new Hashtable<String, Object>();
        env.put(Context.PROVIDER_URL, PIDBContext.getConfig("ldap.url"));
        env.put(Context.INITIAL_CONTEXT_FACTORY, PIDBContext
                .getConfig("ldap.factory"));
        env.put(Context.SECURITY_AUTHENTICATION, PIDBContext
                .getConfig("ldap.authentication"));
        env.put(Context.SECURITY_PRINCIPAL, "CN="
                + PIDBContext.getConfig("ldap.administrator.username").toString() + ", "
                + PIDBContext.getConfig("ldap.cn").toString());
        env.put(Context.SECURITY_CREDENTIALS, PIDBContext
                .getConfig("ldap.administrator.password"));
        try {
            LdapContext ctx = new InitialLdapContext(env, null);
            LOGGER.debug("Login by "
                    + PIDBContext.getConfig("ldap.administrator.username")
                    + " successfully.");

            SearchControls searchCtls = new SearchControls();
            searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            String searchFilter = "(sAMAccountName=" + userId + ")";
            String searchBase = PIDBContext.getConfig("ldap.cn").toString();

            NamingEnumeration answer = ctx.search(searchBase, searchFilter,
                    searchCtls);
            if (answer.hasMoreElements()) {
                SearchResult sr = (SearchResult) answer.next();
                Attributes attrs = sr.getAttributes();

                LOGGER.debug("Find user " + userId
                        + ".");

                Attribute a = null;
                //Get last name
                a = attrs.get("cn");
                if (a != null) {
                    return a.get().toString();
                }
                return null;
            } else {
                LOGGER.debug("Can not find user with name " + userId);
                return null;
            }

        } catch (AuthenticationException ae) {
            LOGGER.debug(ae);
            LOGGER.debug("Authentication fail, incorrect username or password.");
        } catch (NamingException ne) {
            LOGGER.debug(ne);
            LOGGER.debug("Access to AD server fail.");
        }
        return null;
    }

    /**
     * Find user's CN by sAMAccountName.
     * @param userId sAMAccountName
     * @return CN
     */
    public static String getOUUserCN(final String userId) {
    	LDAPConfigDao config = new LDAPConfigDao();
    	List ldapList = (List)config.getAll();
    	for (int i=0;i < ldapList.size(); i++){
    		LDAPConfigTo configTo = (LDAPConfigTo)ldapList.get(i);
    		
    		Hashtable<String, Object> env = new Hashtable<String, Object>();
            env.put(Context.PROVIDER_URL, configTo.getLdapUrl());
            env.put(Context.INITIAL_CONTEXT_FACTORY, configTo.getLdapFactory());
            env.put(Context.SECURITY_AUTHENTICATION, configTo.getLdapAuthentication());
            env.put(Context.SECURITY_PRINCIPAL, configTo.getLdapUsername());
            env.put(Context.SECURITY_CREDENTIALS, configTo.getLdapPassword());
	        try {
	            LdapContext ctx = new InitialLdapContext(env, null);
	            LOGGER.debug("Login by " + configTo.getLdapUsername()
	                    + " successfully.");
	
	            SearchControls searchCtls = new SearchControls();
	            searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
	            String searchFilter = "(sAMAccountName=" + userId + ")";
	            String searchBase = configTo.getLdapCn().toString();
	
	            NamingEnumeration answer = ctx.search(searchBase, searchFilter,
	                    searchCtls);
	            if (answer.hasMoreElements()) {
	                SearchResult sr = (SearchResult) answer.next();
	                Attributes attrs = sr.getAttributes();
	
	                LOGGER.debug("Find user " + userId + ".");
	
	                Attribute a = null;
	                
	                //Get distinguishedName
	                a = attrs.get("distinguishedName");
	                if (a != null) {
	                	return a.get().toString();
	                }
	            } 
	
	        } catch (AuthenticationException ae) {
	            LOGGER.debug(ae);
	            LOGGER.debug("Authentication fail, incorrect username or password.");
	        } catch (NamingException ne) {
	            LOGGER.debug(ne);
	            LOGGER.debug("Access to AD server fail.");
	        }
    	}
    	LOGGER.debug("Can not find user with name " + userId);
        return null;
    }
}
