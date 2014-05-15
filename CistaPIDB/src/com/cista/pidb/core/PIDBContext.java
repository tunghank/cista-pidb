package com.cista.pidb.core;

import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.jdbc.core.JdbcTemplate;

import com.cista.pidb.admin.to.UserTo;
import com.cista.pidb.core.mail.MailManager;

/**
 * PIDBContext is a helper class for instance PIDBContext.xml configed classes.
 * @author Matrix
 *
 */
public final class PIDBContext {
    /** Logger. */
    private static final Log log = LogFactory.getLog(PIDBContext.class);

    /** PIDB Context configuration file path and name. */
    private static final String[] PIDB_CONTEXT_CONFIGURATION = new String[] {
            "PIDBContext.xml" };

    /** The Core ApplicationContext of springframework, instanced by initialize() once only. **/
    private static ApplicationContext context;

    /** Default locale for i18n message fetch. */
    private static final Locale DEFAULT_LOCALE = Locale.TAIWAN;

    private static Properties pidbConfig;

    /** The key for UserTo in HttpSession. */
    private static final String LOGIN_USER = "LOGIN_USER";
    
    /** The key for authentication list in HttpSession. */
    private static final String USER_AUTHENTICATION = "USER_AUTHENTICATION";        
    
    /** The key for menu in HttpSession. */
    private static final String USER_MENU = "USER_MENU";      
    
    /** The key for url in HttpSession. */
    private static final String USER_SAVED_URL = "USER_SAVED_URL";          
    
    
    /**
     * Default constructor, it will not instanced directly.
     *
     */
    private PIDBContext() {

    }

    /**
     * BeanFactory initialize internal only.
     *
     */
    private static synchronized void initialize() {
        try {
            if (context == null) {
                //init application context.
                context = new ClassPathXmlApplicationContext(PIDB_CONTEXT_CONFIGURATION);
                
                log.debug("PIDB ApplicationContext "
                        + PIDB_CONTEXT_CONFIGURATION[0] + " loaded success.");
                pidbConfig = (Properties) context.getBean("pidbConfig");
            }
        } catch (Exception e) {
        	log.error("PIDB ApplicationContext "
                    + PIDB_CONTEXT_CONFIGURATION[0] + " load failure.", e);
        }
    }

    /**
     * Return PIDB JDBC DataSource.
     * @return DataSource.
     */
    public static DataSource getPIDBDataSource() {
        initialize();
        return (DataSource) context.getBean("PIDBDataSource");
    }

    public static JdbcTemplate getPIDBJdbcTemplate() {
        initialize();
        return (JdbcTemplate) context.getBean("PIDBJdbcTemplate");
    }
    /**
     * Return a PIDB TransactionTemplate.
     * @return TransactionTemplate.
     */
    public static TransactionTemplate getPIDBTransactionTemplate() {
        initialize();
        return (TransactionTemplate) context.getBean("PIDBTransactionTemplate");
    }

    /**
     * Get a message from resource.
     * @param key resource key.
     * @return message.
     */
    public static String getMessage(final String key) {
        initialize();
        return context.getMessage(key, null, key, DEFAULT_LOCALE);
    }

    public static String getConfig(final String key) {
        initialize();
        return pidbConfig.getProperty(key, null);
    }

    public static UserTo getLoginUser(HttpServletRequest request) {
        return (UserTo) request.getSession().getAttribute(LOGIN_USER);
    }

    public static void setLoginUser(HttpServletRequest request, UserTo user) {
        request.getSession().setAttribute(LOGIN_USER, user);
    }
    
    public static List<String> getUserAuth(HttpServletRequest request) {
        return (List) request.getSession().getAttribute(USER_AUTHENTICATION);
    }

    public static void setUserAuth(HttpServletRequest request, List<String> auth) {
        request.getSession().setAttribute(USER_AUTHENTICATION, auth);
    }        

    public static String getUserMenu(HttpServletRequest request) {
        return (String) request.getSession().getAttribute(USER_MENU);
    }

    public static void setUserMenu(HttpServletRequest request, String menu) {
        request.getSession().setAttribute(USER_MENU, menu);
    }     
    
    public static String getUserSavedUrl(HttpServletRequest request) {
        return (String) request.getSession().getAttribute(USER_SAVED_URL);
    }

    public static void setUserSavedUrl(HttpServletRequest request, String url) {
        request.getSession().setAttribute(USER_SAVED_URL, url);
    }     
    
    public static void removeUserSavedUrl(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_SAVED_URL);
    }         
    /**
     * Return PIDB JDBC DataSource.
     * @return DataSource.
     */
    public static MailManager getPIDBMailManager() {
        initialize();
        return (MailManager) context.getBean("mailManager");
    }    
}
