package com.cista.pidb.core;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class HttpHelper {
    /** Logger. */
    private static final Log LOGGER = LogFactory.getLog(HttpHelper.class);

    private static final SimpleDateFormat SDF = new SimpleDateFormat(
            "yyyy/MM/dd");

    public static Object pickupForm(Class c, HttpServletRequest request) {
        return pickupForm(c, request, false);
    }

    public static Object pickupForm(Class c, HttpServletRequest request,
            boolean checkParent) {

        try {
            Object instance = c.newInstance();
            Field[] fields = c.getDeclaredFields();
            for (Field f : fields) {
                try {
                    String fieldName = f.getName();
                    Class fieldType = f.getType();
                    String setMethodName = BeanHelper
                            .generateSetMethodNameFromField(fieldName);
                    Method m = c.getDeclaredMethod(setMethodName,
                            new Class[] {fieldType });
                    String parameterValue = request.getParameter(fieldName);
                    Object value = null;
                    if (parameterValue != null) {
                        parameterValue = parameterValue.trim();
                        if (fieldType == int.class
                                || fieldType == Integer.class) {
                            value = Integer.parseInt(parameterValue);
                        } else if (fieldType == String.class) {
                            value = parameterValue;
                        } else if (fieldType == Date.class) {
                            if (!parameterValue.trim().equals("")) {
                                value = SDF.parse(parameterValue);
                            }
                        } else if (fieldType == double.class
                                || fieldType == Double.class) {
                            value = Double.parseDouble(parameterValue);
                        } else if (fieldType == long.class
                                || fieldType == Long.class) {
                            value = Long.parseLong(parameterValue);
                        } else if (fieldType == boolean.class
                                || fieldType == Boolean.class) {
                        	if ("".equals(parameterValue) && fieldType == Boolean.class) {
                        		value = null;
                        	} else if (parameterValue.equalsIgnoreCase("1")
									|| parameterValue.equalsIgnoreCase("Y")
									|| parameterValue.equalsIgnoreCase("T")
									|| parameterValue.equalsIgnoreCase("true")) {
                                value = true;
                            } else {
                                value = false;
                            }
                        }
                        m.invoke(instance, new Object[] {value });
                    }
                } catch (NumberFormatException e) {
                    LOGGER.error(e);
                } catch (SecurityException e) {
                    LOGGER.error(e);
                } catch (IllegalArgumentException e) {
                    LOGGER.error(e);
                } catch (NoSuchMethodException e) {
                    LOGGER.error(e);
                } catch (ParseException e) {
                    LOGGER.error(e);
                } catch (InvocationTargetException e) {
                    LOGGER.error(e);
                }
            }

            if (checkParent) {
                Class supperClass = c.getSuperclass();
                Field[] parentFields = supperClass.getDeclaredFields();
                for (Field f : parentFields) {
                    try {
                        String fieldName = f.getName();
                        Class fieldType = f.getType();
                        String setMethodName = BeanHelper
                                .generateSetMethodNameFromField(fieldName);
                        Method m = supperClass.getDeclaredMethod(setMethodName,
                                new Class[] {fieldType });
                        String parameterValue = request.getParameter(fieldName);
                        Object value = null;
                        if (parameterValue != null) {
                            if (fieldType == int.class
                                    || fieldType == Integer.class) {
                                value = Integer.parseInt(parameterValue);
                            } else if (fieldType == String.class) {
                                value = parameterValue;
                            } else if (fieldType == Date.class) {
                                if (!parameterValue.trim().equals("")) {
                                    value = SDF.parse(parameterValue);
                                }
                            } else if (fieldType == double.class
                                    || fieldType == Double.class) {
                                value = Double.parseDouble(parameterValue);
                            } else if (fieldType == long.class
                                    || fieldType == Long.class) {
                                value = Long.parseLong(parameterValue);
                            } else if (fieldType == boolean.class
                                    || fieldType == Boolean.class) {
                                if (parameterValue.equals("1")
                                        || parameterValue.equals("Y")
                                        || parameterValue.equals("T")) {
                                    value = true;
                                } else {
                                    value = false;
                                }
                            }
                            m.invoke(instance, new Object[] {value });
                        }
                    } catch (NumberFormatException e) {
                        LOGGER.error(e);
                    } catch (SecurityException e) {
                        LOGGER.error(e);
                    } catch (IllegalArgumentException e) {
                        LOGGER.error(e);
                    } catch (NoSuchMethodException e) {
                        LOGGER.error(e);
                    } catch (ParseException e) {
                        LOGGER.error(e);
                    } catch (InvocationTargetException e) {
                        LOGGER.error(e);
                    }
                }
            }
            return instance;
        } catch (InstantiationException e) {
            LOGGER.error(e);
        } catch (IllegalAccessException e) {
            LOGGER.error(e);
        }

        return null;
    }

}
