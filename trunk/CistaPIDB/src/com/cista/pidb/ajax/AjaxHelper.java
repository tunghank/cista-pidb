package com.cista.pidb.ajax;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.cista.pidb.core.BeanHelper;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public final class AjaxHelper {
    /** Logger. */
    private static  final Log LOGGER = LogFactory.getLog(AjaxHelper.class);

    private static Element bean2XmlElement(Object o) {
        if (o == null) {
            return null;
        }

        Element e = DocumentHelper.createElement(o.getClass().getSimpleName());
        Class c = o.getClass();
        Field[] fields = c.getDeclaredFields();
        for (Field f : fields) {
            String fieldName = f.getName();
            String getMethodName = BeanHelper.generateGetMethodNameFromField(fieldName);
            Method m = null;
            try {
                m = c.getDeclaredMethod(getMethodName, new Class[]{});

                if (m != null) {
                    Object value = m.invoke(o);
                    if (value != null) {
                        //add by fumingjie 2007/01/17, for formating date
                        if (value instanceof Date) {
                            SimpleDateFormat formater = new SimpleDateFormat("yyyy/MM/dd");
                            String dateStr = formater.format(value);
                            e.addElement(fieldName).addCDATA(dateStr);
                            continue;
                        }

                        if (value instanceof List) {
                            Element list = e.addElement(fieldName);
                            List valueList = (List) value;
                            for (Object subObject : valueList) {
                                list.add(bean2XmlElement(subObject));
                            }
                        } else {
                            e.addElement(fieldName).addCDATA(value.toString());
                        }
                    }
                }
            } catch (InvocationTargetException ex) {
                LOGGER.warn("Bean2xml fail.", ex);
            } catch (NoSuchMethodException ex) {
                LOGGER.warn("Bean2xml fail.", ex);
            } catch (IllegalAccessException ex) {
                LOGGER.warn("Bean2xml fail.", ex);
            }
        }
        return e;
    }



    private static String xml2string(Element e) {
        if (e == null) {
            return null;
        }

        StringWriter sw = new StringWriter();
        XMLWriter output = new XMLWriter(sw, OutputFormat.createPrettyPrint());
        Document doc = DocumentHelper.createDocument();
        doc.add(e);
        try {
            output.write(doc);
        } catch (IOException e1) {
            LOGGER.error(e1);
        }
        return sw.toString();
    }

    public static String bean2XmlString(Object o) {
        return xml2string(bean2XmlElement(o));
    }

    public static String bean2String(Object o) {
    	

        return ReflectionToStringBuilder.toString(o, ToStringStyle.MULTI_LINE_STYLE);
    }
    
    public static String list2XmlString(String listName, List list) {
        StringWriter sw = new StringWriter();
        XMLWriter output = new XMLWriter(sw, OutputFormat.createPrettyPrint());
        Document doc = DocumentHelper.createDocument();
        Element root = doc.addElement(listName);
        for (Object o : list) {
            root.add(bean2XmlElement(o));
        }

        try {
            output.write(doc);
        } catch (IOException e1) {
            LOGGER.error(e1);
        }
        return sw.toString();
    }
}
