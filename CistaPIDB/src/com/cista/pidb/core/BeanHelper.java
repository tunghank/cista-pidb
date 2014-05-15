package com.cista.pidb.core;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.util.HtmlUtils;


public final class BeanHelper {
    /** Logger. */
    private static  final Log LOGGER = LogFactory.getLog(BeanHelper.class);

    public static final NameConverter NAME_CONVERTER = new DefaultNameConverter();

    public static final String HTML_NULL = "";

    /**
     * Generate the set method name from given field name.
     * @param fieldName field name.
     * @return set method name.
     */
    public static String generateSetMethodNameFromField(final String fieldName) {
        String firstLetter = fieldName.substring(0, 1);
        String otherLetter = fieldName.substring(1);
        return "set" + firstLetter.toUpperCase() + otherLetter;
    }

    /**
     * Generate the get method name from given field name.
     * @param fieldName field name.
     * @return get method name.
     */
    public static String generateGetMethodNameFromField(final String fieldName) {
        String firstLetter = fieldName.substring(0, 1);
        String otherLetter = fieldName.substring(1);
        return "get" + firstLetter.toUpperCase() + otherLetter;
    }

    public static Object getPropertyValue(final Object o, final String fieldName) {
        if (o == null) {
            return null;
        }
        try {
            String getMethodName = generateGetMethodNameFromField(fieldName);
            Method m = o.getClass().getDeclaredMethod(getMethodName, new Class[]{});
            
            return m.invoke(o, new Object[]{});
        } catch (SecurityException e) {
            LOGGER.error(e);
        } catch (IllegalArgumentException e) {
            LOGGER.error(e);
        } catch (NoSuchMethodException e) {
            LOGGER.error(e);
        } catch (IllegalAccessException e) {
            LOGGER.error(e);
        } catch (InvocationTargetException e) {
            LOGGER.error(e);
        }
        return null;
    }

    public static Object getPropertyValueByColumn(final Object o, final String columnName) {
        return getPropertyValue(o, NAME_CONVERTER.db2java(columnName));
    }

    public static String getHtmlValueByColumn(final Object o, final String columnName) {
        return getHtmlValueByColumn(o, columnName, null);
    }

    public static String getHtmlValueByColumn(final Object o, final String columnName, final String format) {
        Object value = getPropertyValueByColumn(o, columnName);

        if (value == null) {
            return HTML_NULL;
        }

        if (format == null) {
            return HtmlUtils.htmlEscape(value.toString());
        } else {
            if (value.getClass() == Date.class) {
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                return HtmlUtils.htmlEscape(sdf.format(value));
            } else if (value.getClass() == Number.class) {
                DecimalFormat df = new DecimalFormat(format);
                return HtmlUtils.htmlEscape(df.format(value));
            } else {
                LOGGER.warn("Type " + value.getClass() + " is not support format.");
                return HtmlUtils.htmlEscape(value.toString());
            }
        }
    }

    public static String generateHtmlHiddenField(final Object o) {
        StringBuffer sb = new StringBuffer();
        try {
            Field[] fields = o.getClass().getDeclaredFields();

            for (Field field : fields) {
                String fieldName = field.getName();
                String getMethodName = generateGetMethodNameFromField(fieldName);
                Method m = o.getClass().getDeclaredMethod(getMethodName, new Class[]{});
                Object value = m.invoke(o, new Object[]{});
                sb.append("<input type=\"hidden\" id=\"" + fieldName + "\" name=\"" + fieldName + "\"");
                String valueStr = "";
                if (value == null) {
                    valueStr = "";
                } else if (value.getClass() == Integer.class) {
                    valueStr = ((Integer) value).intValue() + "";
                } else if (value.getClass() == Double.class) {
                    valueStr = ((Double) value).doubleValue() + "";
                } else {
                    valueStr = value.toString();
                }

                sb.append(" value=\"" + HtmlUtils.htmlEscape(valueStr.toString()) + "\">\r\n");
            }

            return sb.toString();
        } catch (SecurityException e) {
            LOGGER.error(e);
        } catch (IllegalArgumentException e) {
            LOGGER.error(e);
        } catch (NoSuchMethodException e) {
            LOGGER.error(e);
        } catch (IllegalAccessException e) {
            LOGGER.error(e);
        } catch (InvocationTargetException e) {
            LOGGER.error(e);
        }

        return "<!-- Generate html hidden field failed.-->";
    }

    public static String generateHtmlHiddenField(final Object o, final boolean checkParent) {
        StringBuffer sb = new StringBuffer();
        try {
            Field[] fields = o.getClass().getDeclaredFields();

            for (Field field : fields) {
                String fieldName = field.getName();
                String getMethodName = generateGetMethodNameFromField(fieldName);
                Method m = o.getClass().getDeclaredMethod(getMethodName, new Class[]{});
                Object value = m.invoke(o, new Object[]{});
                sb.append("<input type=\"hidden\" id=\"" + fieldName + "\" name=\"" + fieldName + "\"");
                String valueStr = "";
                if (value == null) {
                    valueStr = "";
                } else if (value.getClass() == Integer.class) {
                    valueStr = ((Integer) value).intValue() + "";
                } else if (value.getClass() == Double.class) {
                    valueStr = ((Double) value).doubleValue() + "";
                } else {
                    valueStr = value.toString();
                }

                sb.append(" value=\"" + HtmlUtils.htmlEscape(valueStr.toString()) + "\">\r\n");
            }

            if (checkParent) {
                Class supperClass = o.getClass().getSuperclass();
                Field[] parentFields = supperClass.getDeclaredFields();

                for (Field field : parentFields) {
                    String fieldName = field.getName();
                    String getMethodName = generateGetMethodNameFromField(fieldName);
                    Method m = supperClass.getDeclaredMethod(getMethodName, new Class[]{});
                    Object value = m.invoke(o, new Object[]{});
                    sb.append("<input type=\"hidden\" id=\"" + fieldName + "\" name=\"" + fieldName + "\"");
                    String valueStr = "";
                    if (value == null) {
                        valueStr = "";
                    } else if (value.getClass() == Integer.class || value.getClass() == int.class) {
                        valueStr = ((Integer) value).intValue() + "";
                    } else if (value.getClass() == Double.class || value.getClass() == double.class) {
                        valueStr = ((Double) value).doubleValue() + "";
                    } else if (value.getClass() == Boolean.class || value.getClass() == boolean.class) {
                    	valueStr = ((Boolean)value).booleanValue()?"1":"0";
                    } else {
                        valueStr = value.toString();
                    }

                    sb.append(" value=\"" + HtmlUtils.htmlEscape(valueStr.toString()) + "\">\r\n");
                }
            }

            return sb.toString();
        } catch (SecurityException e) {
            LOGGER.error(e);
        } catch (IllegalArgumentException e) {
            LOGGER.error(e);
        } catch (NoSuchMethodException e) {
            LOGGER.error(e);
        } catch (IllegalAccessException e) {
            LOGGER.error(e);
        } catch (InvocationTargetException e) {
            LOGGER.error(e);
        }

        return "<!-- Generate html hidden field failed.-->";
    }

    public static String getQueryCriteriaByColumn(final Object o, final String columnName) {
    	String htmlValue = getHtmlValueByColumn(o, columnName, null);
        return "".equals(htmlValue) ? "All" : htmlValue;
    }

    public static String getQueryCriteriaByColumn(final Object o, final String columnName, final String format) {
    	String htmlValue = getHtmlValueByColumn(o, columnName, format);
        return "".equals(htmlValue) ? "All" : htmlValue;
    }
}
