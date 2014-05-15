package com.cista.pidb.md.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DownloadAction;

import com.cista.pidb.core.BeanHelper;
import com.cista.pidb.core.DefaultNameConverter;
import com.cista.pidb.core.NameConverter;

public class ReportDownloadAction extends DownloadAction {
    /** Logger. */
    private Log logger = LogFactory.getLog(getClass());

    private static final NameConverter NAME_CONVERTER = new DefaultNameConverter();

    private static final SimpleDateFormat SDF = new SimpleDateFormat(
            "yyyy/MM/dd HH:mm:ss");

    private static final String COLUMN_SPLITER = ",";

    @Override
    protected StreamInfo getStreamInfo(ActionMapping arg0, ActionForm arg1,
            HttpServletRequest arg2, HttpServletResponse arg3) throws Exception {
        arg3.setContentType("application/vnd.ms-excel");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        WritableWorkbook workbook = Workbook.createWorkbook(bos);
        String title = (String) arg2.getAttribute("reportTitle");
        List content = (List) arg2.getAttribute("reportContent");
        String column = (String) arg2.getAttribute("reportColumn");
        arg3.setHeader("Content-disposition", "attachment; filename=" + title
                + ".xls");
        // createExcel(workbook, title, content);
        createExcel(workbook, title, content, column);
        workbook.write();
        workbook.close();
        return new ByteArrayStreamInfo("application/vnd.ms-excel", bos
                .toByteArray());
    }

    private void createExcel(WritableWorkbook workbook, String title,
            List content) {
        WritableSheet wws = workbook.createSheet(title, 0);
        // Create title
        WritableFont arial16font = new WritableFont(WritableFont.ARIAL, 16,
                WritableFont.BOLD, false);
        WritableCellFormat arial16format = new WritableCellFormat(arial16font);
        WritableCell titleCell = new jxl.write.Label(0, 0, title, arial16format);
        try {
            wws.addCell(titleCell);
        } catch (RowsExceededException e) {
            logger.warn(e);
        } catch (WriteException e) {
            logger.warn(e);
        }

        // Create content
        if (content == null || content.size() == 0) {
            return;
        }
        Field[] fields = content.get(0).getClass().getDeclaredFields();
        // Create column names

        WritableFont arial12font = new WritableFont(WritableFont.ARIAL, 10,
                WritableFont.BOLD, false);
        WritableCellFormat arial12format = new WritableCellFormat(arial12font);
        for (int i = 0; i < fields.length; i++) {
            WritableCell wc = new jxl.write.Label(i, 2, NAME_CONVERTER
                    .java2db(fields[i].getName()), arial12format);
            try {
                wws.addCell(wc);
            } catch (RowsExceededException e) {
                logger.warn(e);
            } catch (WriteException e) {
                logger.warn(e);
            }
        }
        for (int i = 0; i < content.size(); i++) {
            Object o = content.get(i);
            for (int j = 0; j < fields.length; j++) {
                Object value = BeanHelper.getPropertyValue(o, fields[j]
                        .getName());
                WritableCell wc = null;
                if (value != null) {
                    if (value instanceof Number) {
                        wc = new jxl.write.Number(j, i + 3, ((Number) value)
                                .doubleValue());
                    } else if (value instanceof Date) {
                        if (value != null) {
                            wc = new jxl.write.Label(j, i + 3, SDF
                                    .format(value));
                        }
                        // wc = new jxl.write.DateTime(j, i + 3, (Date) value);
                    } else {
                        wc = new jxl.write.Label(j, i + 3, value.toString());
                    }
                    try {
                        if (wc != null) {
                            wws.addCell(wc);
                        }
                    } catch (RowsExceededException e) {
                        logger.warn(e);
                    } catch (WriteException e) {
                        logger.warn(e);
                    }
                }
            }
        }
    }

    protected class ByteArrayStreamInfo implements StreamInfo {

        protected String contentType;

        protected byte[] bytes;

        public ByteArrayStreamInfo(String contentType, byte[] bytes) {
            this.contentType = contentType;
            this.bytes = bytes;
        }

        public String getContentType() {
            return contentType;
        }

        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(bytes);
        }
    }

    private void createExcel(WritableWorkbook workbook, String title,
            List content, String column) {
        WritableSheet wws = workbook.createSheet(title, 0);
        // Create title
        WritableFont arial16font = new WritableFont(WritableFont.ARIAL, 16,
                WritableFont.BOLD, false);
        WritableCellFormat arial16format = new WritableCellFormat(arial16font);
        WritableCell titleCell = new jxl.write.Label(0, 0, title, arial16format);
        try {
            wws.addCell(titleCell);
        } catch (RowsExceededException e) {
            logger.warn(e);
        } catch (WriteException e) {
            logger.warn(e);
        }

        // Create content
        if (column == null || column.indexOf(COLUMN_SPLITER) < 0) {
            return;
        }
        String[] columns = column.split(COLUMN_SPLITER);

        if (content == null || content.size() == 0) {
            return;
        }

        // Create column names

        WritableFont arial12font = new WritableFont(WritableFont.ARIAL, 10,
                WritableFont.BOLD, false);
        WritableCellFormat arial12format = new WritableCellFormat(arial12font);

        for (int i = 0; i < columns.length; i++) {
            WritableCell wc = new jxl.write.Label(i, 2, columns[i],
                    arial12format);
            try {
                wws.addCell(wc);
            } catch (RowsExceededException e) {
                logger.warn(e);
            } catch (WriteException e) {
                logger.warn(e);
            }
        }

        for (int i = 0; i < content.size(); i++) {
            Object o = content.get(i);
            for (int j = 0; j < columns.length; j++) {
                Object value = BeanHelper.getPropertyValueByColumn(o,
                        columns[j]);
                WritableCell wc = null;
                if (value != null) {
                    if (value instanceof Number) {
                        wc = new jxl.write.Number(j, i + 3, ((Number) value)
                                .doubleValue());
                    } else if (value instanceof Date) {
                        if (value != null) {
                            wc = new jxl.write.Label(j, i + 3, SDF
                                    .format(value));
                        }
                        // wc = new jxl.write.DateTime(j, i + 3, (Date) value);
                    } else {
                        wc = new jxl.write.Label(j, i + 3, value.toString());
                    }
                    try {
                        if (wc != null) {
                            wws.addCell(wc);
                        }
                    } catch (RowsExceededException e) {
                        logger.warn(e);
                    } catch (WriteException e) {
                        logger.warn(e);
                    }
                }
            }
        }

    }
}
