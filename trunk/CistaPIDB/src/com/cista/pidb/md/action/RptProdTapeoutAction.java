/*
 * 2010.03.01/FCG1 @Jere Huang - Initial Version.
 * 2010.04.21/FCG2 @Jere Huang - 區分NTO, Option NTO.
 */
package com.cista.pidb.md.action;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.cista.pidb.code.dao.SapMasterProductFamilyDao;
import com.cista.pidb.core.HttpHelper;
import com.cista.pidb.md.dao.RptProdTapeoutDao;
import com.cista.pidb.md.to.RptProdTapeoutPeriodQueryTo;
import com.cista.pidb.md.to.RptProdTapeoutRevisionListTo;
import com.cista.pidb.md.to.RptProdTapeoutRevisionQueryTo;
import com.cista.pidb.md.to.RptTapeoutFamilyLineTo;

public class RptProdTapeoutAction extends DispatchAction
{
	String forward;
	protected final Log logger = LogFactory.getLog(getClass());
	
	public ActionForward pre(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) 
	{
		String forward = "success";
		return mapping.findForward(forward);
	}
	
	public ActionForward query(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response)
	{
		forward = "result";
		String       				  sColumnStruct = "";
		String                        sColumnReader = "";
		String                        sColumnAttr = "";
		String    					  sQueryTapeOutYearFrom = "";
		String    					  sQueryTapeOutMonthFrom = "";
		String    					  sQueryTapeOutYearTo = "";
		String    					  sQueryTapeOutMonthTo = "";
		String						  sQueryProdLine = "";
		String						  sQueryProdFamilyCode = "";
		String						  sQueryProdFamilyName = "";
		String						  sTableHeaderNM = "IC Family"; //"TO_Project";
		String						  sSummaryRow = "Summary";
		String						  sSummaryCol = "Total";
		StringBuffer 				  sbColumn;
		StringBuffer 				  sbColReader;
		DateFormat  				  df;
		StringBuffer                  sbGridData;
		ArrayList<String>   		  columnArrayList;
		Hashtable<Integer,Integer>    summaryHt;
		SapMasterProductFamilyDao     sapProdFamilyDao;
		RptProdTapeoutDao 			  rptTapeoutDao;
		SapMasterProductFamilyDao     prodFamilyDao;
		RptProdTapeoutRevisionQueryTo revisionQueryTo;
		List<RptTapeoutFamilyLineTo>  prodFamilyLineList;
		List<RptProdTapeoutRevisionListTo> revisionList;
		
		//0. init variable
		summaryHt= new Hashtable<Integer,Integer>();
		sapProdFamilyDao = new SapMasterProductFamilyDao();
		prodFamilyDao = new SapMasterProductFamilyDao();
		
		RptProdTapeoutPeriodQueryTo queryTo = (RptProdTapeoutPeriodQueryTo) HttpHelper.pickupForm(RptProdTapeoutPeriodQueryTo.class, request, true);
		revisionQueryTo = (RptProdTapeoutRevisionQueryTo) HttpHelper.pickupForm(RptProdTapeoutRevisionQueryTo.class, request, true);
		sQueryTapeOutYearFrom = revisionQueryTo.getTapeOutYearFrom();
		sQueryTapeOutMonthFrom = revisionQueryTo.getTapeOutMonthFrom();
		sQueryTapeOutYearTo = revisionQueryTo.getTapeOutYearTo();
		sQueryTapeOutMonthTo= revisionQueryTo.getTapeOutMonthTo();
		sQueryProdLine = revisionQueryTo.getProdLine();
		//轉換成數字
		sQueryProdFamilyName = queryTo.getProdFamily();
		if(sQueryProdFamilyName != null)
		{
			sQueryProdFamilyCode = prodFamilyDao.findFamilyCodeByDesc(sQueryProdFamilyName);	
            queryTo.setProdFamily(sQueryProdFamilyCode);
		}
		
		//1. 取得所有欄位資料, 計算時間序列, 列出所有的日期區間, -1=不同   0=相同
		columnArrayList = new ArrayList<String>();
		Calendar calDateFrom = Calendar.getInstance();
		calDateFrom.set(Calendar.YEAR, Integer.parseInt(queryTo.getTapeOutYearFrom()) );
		calDateFrom.set(Calendar.MONTH, Integer.parseInt(queryTo.getTapeOutMonthFrom())-1 );
		
		Calendar calDatetTo = Calendar.getInstance();
		calDatetTo.set(Calendar.YEAR, Integer.parseInt(queryTo.getTapeOutYearTo()) );
		calDatetTo.set(Calendar.MONTH, Integer.parseInt(queryTo.getTapeOutMonthTo()) );
		
		int iDeffent = calDateFrom.compareTo(calDatetTo);
		df = new SimpleDateFormat( "yyyy/MM "); 
		columnArrayList.add(sTableHeaderNM);
		do
		{
			columnArrayList.add(df.format(calDateFrom.getTime()) ); //表頭欄位資料
			
			calDateFrom.add(Calendar.MONTH, 1);
			iDeffent = calDateFrom.compareTo(calDatetTo);
		}while(iDeffent==-1);
		//column summary
		columnArrayList.add(sSummaryRow);
		
		//1.1 column struct
		sbColumn = new StringBuffer();
		sbColReader = new StringBuffer();
		int j=0;
		for(String colObj:columnArrayList)
		{
			String sColName = colObj;
			//日期 + NTO or 日期 + Revision
			//for(int i=0;i<2;i++)
			for(int i=0;i<3;i++)
			{
				if(i==0)
				{
					sColumnAttr = "NTO";
				}
				else if(i==1)
				{
					//FCG2
					sColumnAttr = "Option NTO"; 
				}
				else
				{
					sColumnAttr = "Revision";
				}
				//標題, 最後summary  skip
				if(j==0 || j==columnArrayList.size()-1)
				{
					//FCG2
					//i=2; 
					i=3;
				}
				else
				{
					sColName = colObj + "_" + sColumnAttr;
				}
				//grid column
				sbColumn.append("{header: '");
				sbColumn.append(sColName.trim());
				sbColumn.append("', id: '");
				sbColumn.append(sColName.replaceAll(" ",""));
				sbColumn.append("', dataIndex: '");
				sbColumn.append(sColName.replaceAll(" ",""));
				if(sTableHeaderNM.equals(sColName.trim()) || sTableHeaderNM.equals(sColName.trim()) )
				{
					sbColumn.append("', width: 130, sortable: true}  ");
				}
				else
				{
					//sbColumn.append("', width: 90, sortable: true}  ");
					//隔行變色
					int k = (j+1)%2;
					if(k==0)
					{
						sbColumn.append("', width: 90, sortable: true, renderer: columnColor}  ");
					}
					else
					{
						sbColumn.append("', width: 90, sortable: true}  ");
					}
				}
				sbColumn.append(",");			
				//proxy reader
				sbColReader.append("{name:'");
				sbColReader.append(sColName.replaceAll(" ",""));
				if(sTableHeaderNM.equals(sColName.trim()) || sTableHeaderNM.equals(sColName.trim()) )
				{
					sbColReader.append("'} ");
				}
				else
				{
					sbColReader.append("' ,type: 'int'} ");
				}
				sbColReader.append(",");
			}
			++j;
		}
		if(columnArrayList.size()>0)
		{
			sColumnStruct = sbColumn.substring(0, sbColumn.length()-1);
			sColumnReader = sbColReader.substring(0, sbColReader.length()-1);
		}	
		//2. 取得左邊欄位資料
		rptTapeoutDao = new RptProdTapeoutDao();
		prodFamilyLineList = rptTapeoutDao.queryProdFamilyLineByPeriod(queryTo);
		
		//多一行total
		RptTapeoutFamilyLineTo familyTo = new RptTapeoutFamilyLineTo();
		familyTo.setProdFamily(sSummaryCol);
		//familyTo.setProdLine(" ");
		familyTo.setProdLine(sSummaryCol);
		//有資料才做loop
		if(prodFamilyLineList.size()>0)
		{
			prodFamilyLineList.add(familyTo);
		}
		sbGridData = new StringBuffer();
		//2.1 列出左邊欄位的資料 [101, OA] [102,OA] [103,AV]
		for(RptTapeoutFamilyLineTo familyLineObj:prodFamilyLineList)
		{
			//3. 依據family, line 取得期間內所有資料 [2009/01, Function] [2009/01,NTO] [2009/01,Panel-ESD]
			//init revisionQueryTo
			revisionQueryTo.setProdFamily("");
			revisionQueryTo.setProdLine("");
			
			//grid data 左邊column
			sbGridData.append("['");
			sbGridData.append(familyLineObj.getProdLine() );
			//最後一行判斷
			if(!sSummaryCol.equals(familyLineObj.getProdLine() ))
			{
				sbGridData.append("-");
			}
			sbGridData.append(sapProdFamilyDao.findDescByProdFamily(familyLineObj.getProdFamily()) );
			sbGridData.append("',");
			
			revisionQueryTo.setProdFamily(familyLineObj.getProdFamily());
			revisionQueryTo.setProdLine(familyLineObj.getProdLine());
			revisionList = rptTapeoutDao.queryRevision(revisionQueryTo);		
			//計算summary用
			int colCount = 0;
			int iRowSum = 0;
			for(String colObj :columnArrayList)
			{
				int iNTOCount = 0;
				int iRevisionCount = 0;				
				int iOptionNTOCount = 0; //FCG2
				++colCount;
				colObj = colObj.trim();
				
				//標頭skip
				if(sTableHeaderNM.equals(colObj) )
				{
					continue;
				}
				//4. 比對資料並放到 grid data
				for(RptProdTapeoutRevisionListTo revisionObj:revisionList)
				{
					String sDataYear= revisionObj.getYearMonth();
					sDataYear = transParameterNull(sDataYear).trim();
					//4.1 判斷資料與表頭是否相同
					if(colObj.equals(sDataYear) )
					{						
						String sRevisionItem = transParameterNull(revisionObj.getRevisionItem());
						//計算NTO, Revision次數  , -1=沒找到, >0 = 有找到
						//FCG2
						if(sRevisionItem.indexOf("Option NTO")>=0)
						{
							++iOptionNTOCount;
						}
						else if(sRevisionItem.indexOf("NTO")>=0)
						{
							++iNTOCount;
						}
						else
						{
							++iRevisionCount;
						}
					}					
				}//end for(RptProdTapeoutRevisionListTo revisionObj:revisionList) 				
				//4.2 column summary data
				if(sSummaryCol.equals(familyLineObj.getProdFamily()) )
				{
					iNTOCount = summaryHt.get(colCount);
					iRevisionCount = summaryHt.get(colCount+500);					
					iOptionNTOCount = summaryHt.get(colCount+1000); //FCG2
				}
				else
				{
					//4.2.1 先取值, 若有則加總, 無則新增
					if(summaryHt.get(colCount)==null)
					{
						summaryHt.put(colCount, iNTOCount);
						summaryHt.put(colCount+500, iRevisionCount);
						summaryHt.put(colCount+1000, iOptionNTOCount);//FCG2
					}
					else
					{
						int iN = summaryHt.get(colCount) + iNTOCount;
						int iR = summaryHt.get(colCount+500) + iRevisionCount;
						int iON = summaryHt.get(colCount+1000) + iOptionNTOCount; //FCG2
						
						summaryHt.put(colCount, iN);
						summaryHt.put(colCount+500, iR);
						summaryHt.put(colCount+1000, iON);
					}
				}			
				//4.3 將NTO, Revision數字放到grid data中
				if(sSummaryRow.equals(colObj))
				{
					sbGridData.append("'");
					sbGridData.append(iRowSum);
					sbGridData.append("',");
				}
				else
				{
					sbGridData.append("'");
					sbGridData.append(iNTOCount);
					sbGridData.append("','");
					//FCG2
					sbGridData.append(iOptionNTOCount);
					sbGridData.append("','");
					sbGridData.append(iRevisionCount);
					sbGridData.append("',");
					//row summary FCG2
					iRowSum = iRowSum + iNTOCount + iRevisionCount + iOptionNTOCount;
				}				
			}		
			//去,   此為一筆資料
			sbGridData.setLength(sbGridData.length()-1);
			sbGridData.append("],");
		} //end for(RptTapeoutFamilyLineTo familyLineObj:prodFamilyLineList)	
		//去,  完整資料
		if(prodFamilyLineList.size()>1)
		{
			sbGridData.setLength(sbGridData.length()-1);
		}	
		//選單資料
		request.setAttribute("reqYearFrom", sQueryTapeOutYearFrom);
		request.setAttribute("reqMonthFrom", sQueryTapeOutMonthFrom);
		request.setAttribute("reqYearTo", sQueryTapeOutYearTo);
		request.setAttribute("reqMonthTo", sQueryTapeOutMonthTo);
		request.setAttribute("reqProdFamily", sQueryProdFamilyName);
		request.setAttribute("reqProdLine", sQueryProdLine);
		
		//回傳資料
		request.setAttribute("gridData", sbGridData.toString());
		request.setAttribute("gridColumn", sColumnStruct);
		request.setAttribute("gridColumnReader", sColumnReader);
		
		return mapping.findForward(forward);
	}
	
	
	public String transParameterNull(String inputParameter)
    {
        int zeroLength = 0;
        String outParameter ="";
        boolean isParameterNull = (inputParameter == null ||
                                   inputParameter.length() == zeroLength);
        if(isParameterNull)
        {
        	outParameter = " ";
        }
        else
        {
        	outParameter = inputParameter;
        }
        return outParameter;
    }	
}


