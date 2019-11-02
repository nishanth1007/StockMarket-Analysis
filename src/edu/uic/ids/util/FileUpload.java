package edu.uic.ids.util;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
//import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.apache.myfaces.custom.fileupload.UploadedFile;

import edu.uic.ids.model.DatabaseBean;


@ManagedBean(name = StringConstants.FILE_UPLOAD)
@SessionScoped
public class FileUpload {
	DatabaseBean databaseBean;

	ResultSet rs = null;
	private UploadedFile uploadedFile;
	private String fileLabel;
	private String fileName;
	private long fileSize;
	private String uploadedFileContents;
	private boolean fileImport;
	private boolean fileImportError;
	private String filePath;
	private String tempFileName;
	private String dataContent;
	private FacesContext facesContext;
	private String relativeURL;
	private String fileContentType;
	private String errorMessage;
	private boolean messageRendered;
	private Statement statement;
	private ResultSetMetaData resultSetMetaData;
	String status = "";
	private ArrayList<String> columnsList;
	File fileContents = null;
	private boolean queryRendered;
	int numberOfColumns;
	private ArrayList<String[]> dataList = null;
	private int rowCount=0;
	private int columnCount;
	private boolean columnRender;
	private String chartPath;
	private boolean renderChart;
	private ArrayList<String> categoricalData;
	private ArrayList<String> numericData;
	private boolean renderRegressionResult;
	private double corr;

	private double intercept;
	private double interceptStandardError;
	private double tStatistic;
	private double interceptPValue;
	private double slope;
	private double predictorDF;
	private double residualErrorDF;
	private double totalDF;
	private double regressionSumSquares;
	private double sumSquaredErrors;
	private double totalSumSquares;
	private double meanSquare;
	private double meanSquareError;
	private double fValue;
	private double pValue;
	private double slopeStandardError;
	private double tStatisticPredictor;
	private double pValuePredictor;
	private double standardErrorModel;
	private double rSquare;
	private double rSquareAdjusted;
	private String predictorValue;
	private String responseValue;
	private String regressionEquation;
	private boolean renderCorrelationResult;
	private String descriptiveStatsErrorMessage;
	private boolean descriptiveStatsMessageRendered;
	private boolean renderTabledata;
	private String histogramErrorMessage;
	private boolean histogramMessageRendered;
	private String correlationErrorMessage;
	private boolean correlationMessageRendered;
	private String regressionErrorMessage;
	private boolean regressionMessageRendered;
	private String graphErrorMessage;
	private boolean graphMessageRendered;
	private boolean renderScatter;
	private String scatterErrorMessage;
	private boolean scatterMessageRendered;
	private ArrayList<String> loadColumnsList;
	private ResultSet rs1;
	private String corrResponseValue;
	private String corrPredictorValue;
	private String scatterChartPath;
	private boolean renderScatterResult;
	private String ticker;
	//private String transaction_date;
	private Double open;
	private Double high; 
	private Double low; 
	private Double close;
	//private Double adj_close;
	private Double volume;
	//private Double split_coefficient;
	private Double dividend;
	private String transactionDate;
	private Double adjClose;
	private Double splitCoefficient;
	
//	DatabaseBean db = new DatabaseBean();
	
	
	public void init() {
		Map<String, Object> m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		databaseBean = (DatabaseBean) m.get(StringConstants.DATABASE_BEAN);
	}


	public String processFileImport() {
		init();
//		databaseBean = new DatabaseBean();
		String asq =databaseBean.connect();
		
		System.out.println(asq);
		if (uploadedFile == null) {
			return "FAIL";
		}
		errorMessage = "";
		uploadedFileContents = null;
		facesContext = FacesContext.getCurrentInstance();
		filePath = facesContext.getExternalContext().getRealPath("/") + String.valueOf("\\") ;
		filePath = filePath.substring(0, filePath.length()-1) + "temp";
		
		new File(filePath).mkdir();
		System.out.print(filePath );
		//filePath = System.getProperty("user.dir");
		FileOutputStream fos = null;
		int n = 0;
		fileImport = false;
		fileImportError = true;
		System.out.println("517");
		//rowCount=0;
		try {
//			databaseBean.createTable();
			fileName = uploadedFile.getName();
			fileSize = uploadedFile.getSize();
			String value1[] = fileName.split("\\\\");
			int l = value1.length -1;
			setFileContentType(uploadedFile.getContentType());
			tempFileName = filePath + "/" + value1[l];
			fileContents = new File(tempFileName);
			fos = new FileOutputStream(fileContents);
			fos.write(uploadedFile.getBytes());
			fos.close();
			Scanner s = new Scanner(fileContents);
			dataContent="";
			rowCount=0;
	/*	if(s.hasNext()) {
				String headers = s.nextLine();
				dataContent += headers;
				String colName[] = headers.split(","); 
				List<String> colList = new ArrayList<>();
				for(String col: colName) {
					colList.add(col.trim());
					System.out.println("This is CSCO_2");	
				}			
			}  */
			int indexOf = fileName.lastIndexOf((char)92);
			String filePathList = fileName.substring(indexOf+1); 
			System.out.println(filePathList.split("\\."));
			String fileName1 = filePathList.split("\\.")[0];
			
			if(fileName1.equals("CSCO_1")||fileName1.equals("CSCO_2")||fileName1.equals("CSCO_5")||fileName1.equals("ORCL_1")||fileName1.equals("ORCL_2")||fileName1.equals("ORCL_5")||fileName1.equals("GOOG_5"))
			if(s.hasNext()) {
				dataContent += s.nextLine() + "\n";
			}
			
			String input;			
			while(s.hasNext())
			{
				String sline = s.nextLine() ;
				dataContent += sline + "\n";
				rowCount++;
				input = sline;
				String [] result = input.split(",");	
				System.out.println(fileName + " ids517");
				if(fileName1.equals("CSCO_2")||fileName1.equals("CSCO_4")||fileName1.equals("ORCL_2")||fileName1.equals("ORCL_4"))
				{
				ticker = fileLabel;
			    transactionDate = result[1];
			    open = Double.parseDouble(result[2]);
			    high = Double.parseDouble(result[3]);
			    low = Double.parseDouble(result[4]);
			    close = Double.parseDouble(result[5]);
			    adjClose = Double.parseDouble(result[6]);
			    volume = Double.parseDouble(result[7]);
			    dividend = Double.parseDouble(result[8]);
			    splitCoefficient = Double.parseDouble(result[9]);
			    } else if(fileName1.equals("CSCO_1")||fileName1.equals("CSCO_3")||fileName1.equals("ORCL_1")||fileName1.equals("ORCL_3"))
				{
			    ticker = fileLabel;
			    transactionDate = result[0];
			    open = Double.parseDouble(result[1]);
			    high = Double.parseDouble(result[2]);
			    low = Double.parseDouble(result[3]);
			    close = Double.parseDouble(result[4]);
			    adjClose = Double.parseDouble(result[5]);
			    volume = Double.parseDouble(result[6]);
			    dividend = Double.parseDouble(result[7]);
			    splitCoefficient = Double.parseDouble(result[8]); }
			    else if(fileName1.equals("CSCO_5")||fileName1.equals("ORCL_5")||fileName1.equals("GOOG_5"))
				{
					ticker = fileLabel;
				    transactionDate = result[0];
				    open = Double.parseDouble(result[1]);
				    high = Double.parseDouble(result[2]);
				    low = Double.parseDouble(result[3]);
				    close = Double.parseDouble(result[4]);
				    adjClose = Double.parseDouble(result[5]);
				    volume = Double.parseDouble(result[6]);
				    dividend = 0.0;
				    splitCoefficient = 0.0;
				}
			    databaseBean.insertDB(ticker, transactionDate, open, high, low, close, adjClose, volume, dividend,splitCoefficient);
				System.out.println("Inserted into DB");	  
			} 
			
		
			
				 
			s.close();
			System.out.println(n);
			fileImport = true;

		} catch (Exception e) {
			e.printStackTrace();
			return "FAIL";
		}
		errorMessage = "File Upload Successful";
		messageRendered = true;
		return "SUCCESS";
	}

	public boolean isRenderChart() {
		return renderChart;
	}

	public void setRenderChart(boolean renderChart) {
		this.renderChart = renderChart;
	}

	public ArrayList<String> getCategoricalData() {
		return categoricalData;
	}

	public void setCategoricalData(ArrayList<String> categoricalData) {
		this.categoricalData = categoricalData;
	}

	public ArrayList<String> getNumericData() {
		return numericData;
	}

	public void setNumericData(ArrayList<String> numericData) {
		this.numericData = numericData;
	}

	public String resetButton() {

		messageRendered = false;
		return "SUCCESS";
	}

	public UploadedFile getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	public String getFileLabel() {
		return fileLabel;
	}

	public void setFileLabel(String fileLabel) {
		this.fileLabel = fileLabel;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public long getFileSize() {
		return fileSize;
	}

	public String getRelativeURL() {
		return relativeURL;
	}

	public String getFileContentType() {
		return fileContentType;
	}

	public void setFileContentType(String fileContentType) {
		this.fileContentType = fileContentType;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public String getUploadedFileContents() {
		return uploadedFileContents;
	}

	public void setUploadedFileContents(String uploadedFileContents) {
		this.uploadedFileContents = uploadedFileContents;
	}

	public boolean isFileImport() {
		return fileImport;
	}

	public void setFileImport(boolean fileImport) {
		this.fileImport = fileImport;
	}

	public void setRelativeURL(String relativeURL) {
		this.relativeURL = relativeURL;
	}

	public boolean isFileImportError() {
		return fileImportError;
	}

	public void setFileImportError(boolean fileImportError) {
		this.fileImportError = fileImportError;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getTempFileName() {
		return tempFileName;
	}

	public void setTempFileName(String tempFileName) {
		this.tempFileName = tempFileName;
	}

	public FacesContext getFacesContext() {
		return facesContext;
	}

	public void setFacesContext(FacesContext facesContext) {
		this.facesContext = facesContext;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public boolean isMessageRendered() {
		return messageRendered;
	}

	public void setMessageRendered(boolean messageRendered) {
		this.messageRendered = messageRendered;
	}

	public boolean isQueryRendered() {
		return queryRendered;
	}

	public void setQueryRendered(boolean queryRendered) {
		this.queryRendered = queryRendered;
	}
	
	public String getDataContent() {
		return dataContent;
		}

	public void setDataContent(String dataContent) {
		this.dataContent = dataContent;
		}

	public ArrayList<String> getColumnsList() {
		return columnsList;
	}

	public void setColumnsList(ArrayList<String> columnsList) {
		this.columnsList = columnsList;
	}

	public ResultSetMetaData getResultSetMetaData() {
		return resultSetMetaData;
	}

	public void setResultSetMetaData(ResultSetMetaData resultSetMetaData) {
		this.resultSetMetaData = resultSetMetaData;
	}

	public ResultSet getRs() {
		return rs;
	}

	public void setRs(ResultSet rs) {
		this.rs = rs;
	}

	public int getRowCount() {
		return rowCount;
	}

	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

	public ArrayList<String[]> getDataList() {
		return dataList;
	}

	public void setDataList(ArrayList<String[]> dataList) {
		this.dataList = dataList;
	}

	public boolean isColumnRender() {
		return columnRender;
	}

	public void setColumnRender(boolean columnRender) {
		this.columnRender = columnRender;
	}

	public int getColumnCount() {
		return columnCount;
	}

	public void setColumnCount(int columnCount) {
		this.columnCount = columnCount;
	}

	public String getChartPath() {
		return chartPath;
	}

	public void setChartPath(String chartPath) {
		this.chartPath = chartPath;
	}

	public boolean isRenderRegressionResult() {
		return renderRegressionResult;
	}

	public void setRenderRegressionResult(boolean renderRegressionResult) {
		this.renderRegressionResult = renderRegressionResult;
	}

	public double getCorr() {
		return corr;
	}

	public void setCorr(double corr) {
		this.corr = corr;
	}

	public double getIntercept() {
		return intercept;
	}

	public void setIntercept(double intercept) {
		this.intercept = intercept;
	}

	public double getInterceptStandardError() {
		return interceptStandardError;
	}

	public void setInterceptStandardError(double interceptStandardError) {
		this.interceptStandardError = interceptStandardError;
	}

	public double gettStatistic() {
		return tStatistic;
	}

	public void settStatistic(double tStatistic) {
		this.tStatistic = tStatistic;
	}

	public double getInterceptPValue() {
		return interceptPValue;
	}

	public void setInterceptPValue(double interceptPValue) {
		this.interceptPValue = interceptPValue;
	}

	public double getSlope() {
		return slope;
	}

	public void setSlope(double slope) {
		this.slope = slope;
	}

	public double getPredictorDF() {
		return predictorDF;
	}

	public void setPredictorDF(double predictorDF) {
		this.predictorDF = predictorDF;
	}

	public double getResidualErrorDF() {
		return residualErrorDF;
	}

	public void setResidualErrorDF(double residualErrorDF) {
		this.residualErrorDF = residualErrorDF;
	}

	public double getTotalDF() {
		return totalDF;
	}

	public void setTotalDF(double totalDF) {
		this.totalDF = totalDF;
	}

	public double getRegressionSumSquares() {
		return regressionSumSquares;
	}

	public void setRegressionSumSquares(double regressionSumSquares) {
		this.regressionSumSquares = regressionSumSquares;
	}

	public double getSumSquaredErrors() {
		return sumSquaredErrors;
	}

	public void setSumSquaredErrors(double sumSquaredErrors) {
		this.sumSquaredErrors = sumSquaredErrors;
	}

	public double getTotalSumSquares() {
		return totalSumSquares;
	}

	public void setTotalSumSquares(double totalSumSquares) {
		this.totalSumSquares = totalSumSquares;
	}

	public double getMeanSquare() {
		return meanSquare;
	}

	public void setMeanSquare(double meanSquare) {
		this.meanSquare = meanSquare;
	}

	public double getMeanSquareError() {
		return meanSquareError;
	}

	public void setMeanSquareError(double meanSquareError) {
		this.meanSquareError = meanSquareError;
	}

	public double getfValue() {
		return fValue;
	}

	public void setfValue(double fValue) {
		this.fValue = fValue;
	}

	public double getpValue() {
		return pValue;
	}

	public void setpValue(double pValue) {
		this.pValue = pValue;
	}

	public double getSlopeStandardError() {
		return slopeStandardError;
	}

	public void setSlopeStandardError(double slopeStandardError) {
		this.slopeStandardError = slopeStandardError;
	}

	public double gettStatisticPredictor() {
		return tStatisticPredictor;
	}

	public void settStatisticPredictor(double tStatisticPredictor) {
		this.tStatisticPredictor = tStatisticPredictor;
	}

	public double getpValuePredictor() {
		return pValuePredictor;
	}

	public void setpValuePredictor(double pValuePredictor) {
		this.pValuePredictor = pValuePredictor;
	}

	public double getStandardErrorModel() {
		return standardErrorModel;
	}

	public void setStandardErrorModel(double standardErrorModel) {
		this.standardErrorModel = standardErrorModel;
	}

	public double getrSquare() {
		return rSquare;
	}

	public void setrSquare(double rSquare) {
		this.rSquare = rSquare;
	}

	public double getrSquareAdjusted() {
		return rSquareAdjusted;
	}

	public void setrSquareAdjusted(double rSquareAdjusted) {
		this.rSquareAdjusted = rSquareAdjusted;
	}

	public String getPredictorValue() {
		return predictorValue;
	}

	public void setPredictorValue(String predictorValue) {
		this.predictorValue = predictorValue;
	}

	public String getRegressionEquation() {
		return regressionEquation;
	}

	public void setRegressionEquation(String regressionEquation) {
		this.regressionEquation = regressionEquation;
	}

	public boolean isRenderCorrelationResult() {
		return renderCorrelationResult;
	}

	public void setRenderCorrelationResult(boolean renderCorrelationResult) {
		this.renderCorrelationResult = renderCorrelationResult;
	}

	public String getResponseValue() {
		return responseValue;
	}

	public void setResponseValue(String responseValue) {
		this.responseValue = responseValue;
	}

	public String getDescriptiveStatsErrorMessage() {
		return descriptiveStatsErrorMessage;
	}

	public void setDescriptiveStatsErrorMessage(String descriptiveStatsErrorMessage) {
		this.descriptiveStatsErrorMessage = descriptiveStatsErrorMessage;
	}

	public boolean isDescriptiveStatsMessageRendered() {
		return descriptiveStatsMessageRendered;
	}

	public void setDescriptiveStatsMessageRendered(boolean descriptiveStatsMessageRendered) {
		this.descriptiveStatsMessageRendered = descriptiveStatsMessageRendered;
	}


	public boolean isRenderTabledata() {
		return renderTabledata;
	}

	public void setRenderTabledata(boolean renderTabledata) {
		this.renderTabledata = renderTabledata;
	}

	public String getHistogramErrorMessage() {
		return histogramErrorMessage;
	}

	public void setHistogramErrorMessage(String histogramErrorMessage) {
		this.histogramErrorMessage = histogramErrorMessage;
	}

	public boolean isHistogramMessageRendered() {
		return histogramMessageRendered;
	}

	public void setHistogramMessageRendered(boolean histogramMessageRendered) {
		this.histogramMessageRendered = histogramMessageRendered;
	}

	public String getCorrelationErrorMessage() {
		return correlationErrorMessage;
	}

	public void setCorrelationErrorMessage(String correlationErrorMessage) {
		this.correlationErrorMessage = correlationErrorMessage;
	}

	public boolean isCorrelationMessageRendered() {
		return correlationMessageRendered;
	}

	public void setCorrelationMessageRendered(boolean correlationMessageRendered) {
		this.correlationMessageRendered = correlationMessageRendered;
	}

	public String getRegressionErrorMessage() {
		return regressionErrorMessage;
	}

	public void setRegressionErrorMessage(String regressionErrorMessage) {
		this.regressionErrorMessage = regressionErrorMessage;
	}

	public boolean isRegressionMessageRendered() {
		return regressionMessageRendered;
	}

	public void setRegressionMessageRendered(boolean regressionMessageRendered) {
		this.regressionMessageRendered = regressionMessageRendered;
	}

	public String getGraphErrorMessage() {
		return graphErrorMessage;
	}

	public void setGraphErrorMessage(String graphErrorMessage) {
		this.graphErrorMessage = graphErrorMessage;
	}

	public boolean isGraphMessageRendered() {
		return graphMessageRendered;
	}

	public void setGraphMessageRendered(boolean graphMessageRendered) {
		this.graphMessageRendered = graphMessageRendered;
	}

	public boolean isRenderScatter() {
		return renderScatter;
	}

	public void setRenderScatter(boolean renderScatter) {
		this.renderScatter = renderScatter;
	}

	public String getScatterErrorMessage() {
		return scatterErrorMessage;
	}

	public void setScatterErrorMessage(String scatterErrorMessage) {
		this.scatterErrorMessage = scatterErrorMessage;
	}

	public boolean isScatterMessageRendered() {
		return scatterMessageRendered;
	}

	public void setScatterMessageRendered(boolean scatterMessageRendered) {
		this.scatterMessageRendered = scatterMessageRendered;
	}

	public ArrayList<String> getLoadColumnsList() {
		return loadColumnsList;
	}

	public void setLoadColumnsList(ArrayList<String> loadColumnsList) {
		this.loadColumnsList = loadColumnsList;
	}

	public ResultSet getRs1() {
		return rs1;
	}

	public void setRs1(ResultSet rs1) {
		this.rs1 = rs1;
	}

	public String getCorrResponseValue() {
		return corrResponseValue;
	}

	public void setCorrResponseValue(String corrResponseValue) {
		this.corrResponseValue = corrResponseValue;
	}

	public String getCorrPredictorValue() {
		return corrPredictorValue;
	}

	public void setCorrPredictorValue(String corrPredictorValue) {
		this.corrPredictorValue = corrPredictorValue;
	}

	public String getScatterChartPath() {
		return scatterChartPath;
	}

	public void setScatterChartPath(String scatterChartPath) {
		this.scatterChartPath = scatterChartPath;
	}

	public boolean isRenderScatterResult() {
		return renderScatterResult;
	}

	public void setRenderScatterResult(boolean renderScatterResult) {
		this.renderScatterResult = renderScatterResult;
	}

	public String getTicker() {
		return ticker;
	}

	public void setTicker(String ticker) {
		this.ticker = ticker;
	}

	public Double getOpen() {
		return open;
	}

	public void setOpen(Double open) {
		this.open = open;
	}

	public Double getHigh() {
		return high;
	}

	public void setHigh(Double high) {
		this.high = high;
	}

	public Double getLow() {
		return low;
	}

	public void setLow(Double low) {
		this.low = low;
	}

	public Double getClose() {
		return close;
	}

	public void setClose(Double close) {
		this.close = close;
	}

	public Double getVolume() {
		return volume;
	}

	public void setVolume(Double volume) {
		this.volume = volume;
	}

	public Double getDividend() {
		return dividend;
	}

	public void setDividend(Double dividend) {
		this.dividend = dividend;
	}

	public String getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}

	public Double getAdjClose() {
		return adjClose;
	}

	public void setAdjClose(Double adjClose) {
		this.adjClose = adjClose;
	}

	public Double getSplitCoefficient() {
		return splitCoefficient;
	}

	public void setSplitCoefficient(Double splitCoefficient) {
		this.splitCoefficient = splitCoefficient;
	}

	public Statement getStatement() {
		return statement;
	}

	public void setStatement(Statement statement) {
		this.statement = statement;
	}
}
