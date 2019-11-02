package edu.uic.ids.model;
import java.io.File;
import java.io.IOException;
import java.awt.Color;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

//import edu.uic.ids.util.ExportFile;
import edu.uic.ids.util.MathUtil;
import edu.uic.ids.util.StringConstants;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.function.LineFunction2D;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.xy.XYDataset;


@ManagedBean(name = StringConstants.ANALYSIS_BEAN_NAME)
@SessionScoped
public class AnalysisBean {

	private int columnCount;
	private int rowCount;
	private String message;
	private String predictorValue;
	private String responseValue;
	private boolean columnRender;
	private boolean renderSchema;
	private boolean buttonDisable;
	private boolean renderMessage;
	private boolean renderReport;
	private boolean renderTabledata;
	private boolean renderCorrelationResult;
	private boolean renderTablename;
	private boolean renderRegressionColumn;
	private boolean renderRegressionButton;
	private boolean renderColumnListbutton;
	private boolean renderRegressionResult;
	private boolean renderNumberOfObservations;
	private boolean renderNumberOfColumns;
	private List<String> numericData;
	private List<String> categoricalData;
	private List<String> columnSelected;
	private List<String> columnsList;
	private List<String> tableList;
	private List<String> columns;
	private List<String> list;
	private XYSeries xySeries;
	private XYSeriesCollection xySeriesVariables;
	private double quartile1;
	private double quartile3;
	private double median1;
	private List<DescriptiveStatsBean> descriptiveStatsBeanList;
	//private Result result;
	private ResultSet resultSet;
	private boolean renderChart;
	private DatabaseBean databaseBean;

	private ResultSetMetaData resultSetMetaData;
	private XYSeriesCollection xySeriesVariable;
	private XYSeriesCollection xyTimeSeriesCollection;
	private XYSeries predictorSeries;
	private XYSeries responseSeries;
	private String errorMessage;
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
	private String chartPath;


	
	public String getChartPath() {
		return chartPath;
	}
	public void setChartPath(String chartPath) {
		this.chartPath = chartPath;
	}
	public AnalysisBean() {
		columnSelected = new ArrayList<String>();
		columnsList = new ArrayList<String>();
		columns = new ArrayList<String>();
		renderTabledata = false;
		descriptiveStatsBeanList = new ArrayList<DescriptiveStatsBean>();
		categoricalData = new ArrayList<String>();
		numericData = new ArrayList<String>();
		buttonDisable = false;
		renderRegressionButton = true;
		renderReport = false;
		tableList = new ArrayList<String>();
		list = new ArrayList<String>();
		xySeries = new XYSeries("Random");
		xySeriesVariable = new XYSeriesCollection();
		renderTablename = false;
		xyTimeSeriesCollection = new XYSeriesCollection();
		predictorSeries = new XYSeries("Predictor");
		responseSeries = new XYSeries("Response");
	}

	@PostConstruct
	public void init() {
		loadDatabaseBeanObject();
	}
	
	public void loadDatabaseBeanObject(){
		Map<String, Object> m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		databaseBean = (DatabaseBean) m.get(StringConstants.DATABASE_BEAN);
	}

	public String getTables() {
		try {
			reset();
			tableList = new ArrayList<String>();
			if(databaseBean==null){
				loadDatabaseBeanObject();
			}
			databaseBean.listTableswithDefaultSchema();
			tableList = databaseBean.getTableList();
			renderTableList();
			return StringConstants.SUCCESS;
		} catch (Exception e) {
			message = e.getMessage();
			renderMessage = true;
			return StringConstants.FAIL;
		}
	}

	public void renderTableList() {
		reset();
		
		if (tableList.isEmpty()) {
			message = "No tables found in the schema.";
			columnRender = false;
			renderRegressionResult = false;
			renderNumberOfColumns = false;
			renderNumberOfObservations = false;
			columnRender = false;
			renderRegressionColumn = false;
			renderTabledata = false;
			renderMessage = true;
			renderTablename = false;
			columnRender = false;
			renderMessage = true;
		} else
			renderTablename = true;
	}

	public String getRegressionColumnNames() {
		reset();
		resetButton();
		if(databaseBean==null){
			loadDatabaseBeanObject();
		}
		if (databaseBean.getTableName().isEmpty()) {
			message = "Please select a table.";
			renderMessage = true;
			return StringConstants.FAIL;
		}
		if (generateRegressionColumns()) {
			return StringConstants.SUCCESS;
		} else {
			renderMessage = true;
			return StringConstants.FAIL;
		}
	}

	public boolean generateRegressionColumns() {
		try {
			if(databaseBean==null){
				loadDatabaseBeanObject();
			}
			String sqlQuery = "select " + databaseBean.getColumnNamesSelected().toString().replace("[", "").replace("]", "") + " from "
					+ databaseBean.getDbSchema() + "."
					+ databaseBean.getTableName();
			String status = databaseBean.dbExecute(databaseBean.getTableName(), sqlQuery);
			if (status.equalsIgnoreCase("SUCCESS")) {
				resultSet = databaseBean.getResultSet();
			}
			if (resultSet != null) {
				columnsList.clear();
				categoricalData.clear();
				numericData.clear();

				resultSet.last();
				rowCount = resultSet.getRow();
				resultSet.beforeFirst();
				ResultSetMetaData resultSetMetaData = (ResultSetMetaData) resultSet.getMetaData();
				columnCount = resultSetMetaData.getColumnCount();
				for (int i = 1; i <= columnCount; i++) {
					String name = resultSetMetaData.getColumnName(i);
					String datatype = resultSetMetaData.getColumnTypeName(i);
					if (datatype.equalsIgnoreCase("char") || datatype.equalsIgnoreCase("varchar")) {
						categoricalData.add(name);
					} else
						numericData.add(name);
				}
				columnRender = true;
			} else {

				renderNumberOfObservations = false;
				renderNumberOfColumns = false;
				return false;
			}
			return true;
		} catch (Exception e) {
			message = "Error generating Regression Columns";
			renderMessage = true;
			renderNumberOfObservations = false;
			renderNumberOfColumns = false;
			return false;
		}
	}

	public String splitColumns() {
		try {
			reset();
			if (databaseBean.getTableName() != null && columnSelected != null) {
				List<String> columnSeperated = new ArrayList<String>();
				for (int i = 0; i < columnSelected.size(); i++) {
					String data = columnSelected.get(i);
					int index = data.indexOf(" ");
					String column = data.substring(0, index);
					String datatype = data.substring((index + 1), data.length());
					if (datatype.equalsIgnoreCase("CHAR") || datatype.equalsIgnoreCase("VARCHAR")) {
						message = "Categorical variables are not allowed";
						return "FAIL";
					} else {
						columnSeperated.add(column);
					}
				}
				columnSelected = new ArrayList<String>();
				columnSelected = columnSeperated;
				list.clear();
				list = columnSelected;
				columnSeperated = null;
				return StringConstants.SUCCESS;
			} else {
				message = "Please select a table and a column.";
				return StringConstants.FAIL;
			}
		} catch (Exception e) {
			message = e.getMessage();
			renderMessage = true;
			return "FAIL";
		}
	}

	public String generateDescriptiveStatistics() {
		
		reset();
		resetButton();
		loadDatabaseBeanObject();
		//String command = "descriptiveStats";
		/*if (tableList.isEmpty()) {
			message = "No tables found in the schema.";
			renderMessage = true;
			return StringConstants.FAIL;
		}*/
		if (databaseBean.getTableName().isEmpty()) {
			message = "Please select a table and a column.";
			renderMessage = true;
			return StringConstants.FAIL;
		}
		if (databaseBean.getColumnNamesSelected().isEmpty()) {
			message = "Please select a Column";
			renderMessage = true;
			return "STATFAIL";
		} else {
			if (calculateDescriptiveVariables().equals("FAIL")) {
				message="Please select only  numerical variables";
				renderMessage = true;
				databaseBean.setQueryRendered(false);
				return "STATFAIL";
			} else {
			//	ExportFile.appendToFile(command);
			//	ExportFile.appendToFile(System.getProperty("line.separator"));
				return "STATSUCCESS";
			}
		}
	}

	public String calculateDescriptiveVariables() {
		try {
			for (int k = 0; k < databaseBean.getColumnNamesSelected().size(); k++) {
				String sqlQuery = "select " + databaseBean.getColumnNamesSelected().get(k) + " from " + databaseBean.getDbSchema() + "."
						+ databaseBean.getTableName();
				String status = databaseBean.dbExecute(databaseBean.getTableName(), sqlQuery);
				if (status.equalsIgnoreCase("Success")) {

					resultSet = databaseBean.getResultSet();
					resultSetMetaData = (ResultSetMetaData) resultSet.getMetaData();
					columnCount = resultSetMetaData.getColumnCount();
					renderNumberOfColumns = true;
					resultSet.last();
					rowCount = resultSet.getRow();
					resultSet.beforeFirst();
					categoricalData.clear();
					numericData.clear();
					renderNumberOfObservations = true;
					String columnName;
					for (int i = 1; i <= columnCount; i++) {
						String name = resultSetMetaData.getColumnName(i);
						String datatype = resultSetMetaData.getColumnTypeName(i);
						if (datatype.equalsIgnoreCase("char") || datatype.equalsIgnoreCase("varchar")) {
							categoricalData.add(name);
						} else
							numericData.add(name);
					}if(numericData.size()>0) {
					for (int j = 1; j < columnCount + 1; j++) {
						List<Double> values = new ArrayList<Double>();
						columnName = resultSetMetaData.getColumnName(j);
						String columnType = resultSetMetaData.getColumnTypeName(j);
						//if(columnType.equalsIgnoreCase("char") || datatype.equalsIgnoreCase("varchar"))))
						resultSet.beforeFirst();
						while (resultSet.next()) {
							switch (columnType.toLowerCase()) {
							case "int":
								values.add((double) resultSet.getInt(columnName));
								break;
							case "smallint":
								values.add((double) resultSet.getInt(columnName));
								break;
							case "float":
								values.add((double) resultSet.getFloat(columnName));
								break;
							case "double":
								values.add((double) resultSet.getDouble(columnName));
								break;
							case "long":
								values.add((double) resultSet.getLong(columnName));
								break;
														
							default:
								values.add((double) resultSet.getDouble(columnName));
								break;
							}
						}
						double[] valuesArray = new double[values.size()];
						for (int i = 0; i < values.size(); i++) {
							valuesArray[i] = (double) values.get(i);
						}

						double minValue = MathUtil.round(StatUtils.min(valuesArray), 100);
						double maxValue = MathUtil.round(StatUtils.max(valuesArray), 100);
						double mean = MathUtil.round(StatUtils.mean(valuesArray), 100);
						double variance = MathUtil.round(StatUtils.variance(valuesArray, mean), 100);
						double std = MathUtil.round(Math.sqrt(variance), 100);
						double median = MathUtil.round(StatUtils.percentile(valuesArray, 50.0), 100);
						double q1 = MathUtil.round(StatUtils.percentile(valuesArray, 25.0), 100);
						double q3 = MathUtil.round(StatUtils.percentile(valuesArray, 75.0), 100);
						double iqr = q3 - q1;
						double range = maxValue - minValue;
						descriptiveStatsBeanList.add(new DescriptiveStatsBean(columnName, minValue, maxValue,
								mean, variance, std, median, q1, q3, iqr, range));
					}}else {
						
						continue;
						
					}
					renderTabledata = true;
				}
			}

			return "SUCCESS";
		} catch (Exception e) {
			message = "Error generating Descriptive Analysis";
			renderMessage = true;
			renderNumberOfObservations = false;
			renderNumberOfColumns = false;
			return "FAIL";
		}
	}

	public String getColumnNames() {
		try {
			reset();
			getTables();
			if (tableList.isEmpty()) {
				message = "No tables found in the Schema";
				renderMessage = true;
				return "FAIL";
			}
			if (databaseBean.getTableName().isEmpty()) {
				message = "Please select a Table";
				renderMessage = true;
				return "FAIL";
			} else {
				columnsList.clear();
				databaseBean.setTableName(databaseBean.getTableName());
				databaseBean.listColumns();
				columnsList = databaseBean.getColumnList();
				columnRender = true;
			}
			return StringConstants.SUCCESS;
		} catch (Exception e) {
			message = e.getMessage();
			renderMessage = true;
			return StringConstants.FAIL;
		}
	}

	public String displayColumnsforRegression() {
		reset();
		resetButton();
		loadDatabaseBeanObject();
		if (databaseBean.getTableName() == null) {
			message = "Please select a table.";
			renderMessage = true;
			renderColumnListbutton = true;
			renderReport = true;
			return "SUCCESS";
		}
		String status = getRegressionColumnNames();
		if (status.equalsIgnoreCase("SUCCESS")) {
			columnRender = false;
			renderRegressionButton = false;
			renderRegressionColumn = true;
			renderColumnListbutton = true;
			renderReport = true;

			return "SUCCESS";
		} else {
			renderMessage = true;
			return "FAIL";
		}
	}
 public String generateRegressionAnalysis() {
	 //String command="regress";
	 String status = generateRegressionReport();
	 
	 if (status=="STATSUCCESS") {
	//	 ExportFile.appendToFile(command+"\t"+responseValue+"\t"+predictorValue);
	//	 ExportFile.appendToFile(System.getProperty("line.separator"));
		 return "STATSUCCESS";
	 }else
		 return "STATFAIL";
	 
 }
	public String generateRegressionReport() {
		reset();
		resetButton();
		message = "";
		getTables();
		if (databaseBean.getTableName() == null) {
			message += " Please select a Table";
			renderMessage = true;
			return StringConstants.FAIL;
		}
		if ((predictorValue == null || predictorValue.equals(""))) {
			message += " Please select a Predictor Variable";
			renderMessage = true;
			return StringConstants.FAIL;
		}
		if ((responseValue == null || responseValue.equals(""))) {
			message += " Please select a Response Variable";
			renderMessage = true;
			return StringConstants.FAIL;
		}

		if ((responseValue.equals("0") && predictorValue.equals("0"))
				|| (responseValue == null && predictorValue == null)
				|| (responseValue.equals("0") && predictorValue == null)
				|| (responseValue == null && predictorValue.equals("0"))) {
			message = "Please select a Predictor and a Response Variable";
			renderMessage = true;
			return StringConstants.FAIL;
		}
		if (calculateRegressionVariables()) {
			return "STATSUCCESS";
		} else
			return "STATFAIL";
	}

	public boolean calculateRegressionVariables() {
		try {

			responseSeries.clear();
			predictorSeries.clear();
			xySeries.clear();
			xySeriesVariable.removeAllSeries();
			xyTimeSeriesCollection.removeAllSeries();
			String sqlQuery = "select " + predictorValue + ", " + responseValue + " from " + databaseBean.getDbSchema() + "."
					+ databaseBean.getTableName();
			String status = databaseBean.dbExecute(databaseBean.getTableName(), sqlQuery);
			if (status.equalsIgnoreCase("SUCCESS")) {
				resultSet = databaseBean.getResultSet();
				if (resultSet != null) {
					resultSetMetaData = (ResultSetMetaData) resultSet.getMetaData();
					String predictorName = resultSetMetaData.getColumnTypeName(1);
					String responseName = resultSetMetaData.getColumnTypeName(2);
					List<Double> predictorList = new ArrayList<Double>();
					List<Double> responseList = new ArrayList<Double>();
					resultSet.beforeFirst();
					while (resultSet.next()) {
						switch (predictorName.toLowerCase()) {
						case "int":
							predictorList.add((double) resultSet.getInt(1));
							break;
						case "smallint":
							predictorList.add((double) resultSet.getInt(1));
							break;
						case "float":
							predictorList.add((double) resultSet.getFloat(1));
							break;
						case "double":
							predictorList.add((double) resultSet.getDouble(1));
							break;
						case "long":
							predictorList.add((double) resultSet.getLong(1));
							break;
						default:
							predictorList.add((double) resultSet.getDouble(1));
							break;
						}
						switch (responseName.toLowerCase()) {
						case "int":
							responseList.add((double) resultSet.getInt(2));
							break;
						case "smallint":
							responseList.add((double) resultSet.getInt(2));
							break;
						case "float":
							responseList.add((double) resultSet.getFloat(2));
							break;
						case "double":
							responseList.add((double) resultSet.getDouble(2));
							break;
						case "long":
							responseList.add((double) resultSet.getLong(2));
							break;
						default:
							responseList.add((double) resultSet.getDouble(2));
							break;
						}
					}
					double[] predictorArray = new double[predictorList.size()];
					for (int i = 0; i < predictorList.size(); i++) {
						predictorArray[i] = (double) predictorList.get(i);
						predictorSeries.add(i + 1, (double) predictorList.get(i));
					}
					double[] responseArray = new double[responseList.size()];
					for (int i = 0; i < responseList.size(); i++) {
						responseArray[i] = (double) responseList.get(i);
						responseSeries.add(i + 1, (double) responseList.get(i));
					}
					xyTimeSeriesCollection.addSeries(predictorSeries);
					xyTimeSeriesCollection.addSeries(responseSeries);
					SimpleRegression sr = new SimpleRegression();
					if (responseArray.length > predictorArray.length) {
						for (int i = 0; i < predictorArray.length; i++) {
							sr.addData(predictorArray[i], responseArray[i]);
							xySeries.add(predictorArray[i], responseArray[i]);
						}
					} else {
						for (int i = 0; i < responseArray.length; i++) {
							sr.addData(predictorArray[i], responseArray[i]);
							xySeries.add(predictorArray[i], responseArray[i]);
						}
					}
					xySeriesVariable.addSeries(xySeries);
					totalDF = responseArray.length - 1;
					TDistribution tDistribution = new TDistribution(totalDF);
					intercept = sr.getIntercept();
					interceptStandardError = sr.getInterceptStdErr();
					corr=sr.getR();
					tStatistic = 0;
					predictorDF = 1;
					residualErrorDF = totalDF - predictorDF;
					rSquare = sr.getRSquare();
					rSquareAdjusted = rSquare - (1 - rSquare) / (totalDF - predictorDF - 1);
					if (interceptStandardError != 0) {
						tStatistic = (double) intercept / interceptStandardError;
					}
					else
						tStatistic = Double.NaN;
					interceptPValue = (double) 2 * tDistribution.cumulativeProbability(-Math.abs(tStatistic));
					slope = sr.getSlope();
					slopeStandardError = sr.getSlopeStdErr();
					double tStatisticpredict = 0;
					if (slopeStandardError != 0) {
						tStatisticpredict = (double) slope / slopeStandardError;
					}
					pValuePredictor = (double) 2 * tDistribution.cumulativeProbability(-Math.abs(tStatisticpredict));
					standardErrorModel = Math.sqrt(StatUtils.variance(responseArray))
							* (Math.sqrt(1 - rSquareAdjusted));
					regressionSumSquares = sr.getRegressionSumSquares();
					sumSquaredErrors = sr.getSumSquaredErrors();
					totalSumSquares = sr.getTotalSumSquares();
					meanSquare = 0;
					if (predictorDF != 0) {
						meanSquare = regressionSumSquares / predictorDF;
					}
					meanSquareError = 0;
					if (residualErrorDF != 0) {
						meanSquareError = (double) (sumSquaredErrors / residualErrorDF);
					}
					fValue = 0;
					if (meanSquareError != 0) {
						fValue = meanSquare / meanSquareError;
					}
					regressionEquation = responseValue + " = " + intercept + " + (" + slope + ") " + predictorValue;
					FDistribution fDistribution = new FDistribution(predictorDF, residualErrorDF);
					pValue = (double) (1 - fDistribution.cumulativeProbability(fValue));
					renderRegressionResult = true;
					renderNumberOfColumns = true;
					renderNumberOfObservations = true;
					return true;
				} else {
					message = "Result Set Null";
					renderMessage = true;
					return false;
				}
			}

			return false;

		} catch (Exception e) {
			message = "Failure generating Regression";
			renderMessage = true;
			return false;
		}
	}


	public boolean generateRegressionResults() {
		xySeries.clear();
		xySeriesVariable.removeAllSeries();
		if (calculateRegressionVariables()) {
			renderRegressionResult = false;
			renderNumberOfColumns = false;
			renderNumberOfObservations = false;
			return true;
		} else {
			errorMessage = message;
			return false;
		}
	}
	@SuppressWarnings("unused")
	public String loadSelectedHistogram() {
		reset();
		resetButton();
		loadDatabaseBeanObject();
		//String command="hist";
		double[] valuesArray;
		if (databaseBean.getTableName().isEmpty()) {
			message = "Please select a table and a column.";
			renderMessage = true;
			return "STATFAIL";
		}
		if (databaseBean.getColumnNamesSelected().isEmpty()) {
			message = "Please select a Column";
			renderMessage = true;
			return "STATFAIL";
		} else 
		{
			try 
			{
				for (int k = 0; k < databaseBean.getColumnNamesSelected().size(); k++) {
				String sqlQuery = "select " + databaseBean.getColumnNamesSelected().get(k) + " from " + databaseBean.getDbSchema() + "."
						+ databaseBean.getTableName();
				String status = databaseBean.dbExecute(databaseBean.getTableName(), sqlQuery);
				if (status.equalsIgnoreCase("Success")) {
					categoricalData.clear();
					numericData.clear();
					resultSet = databaseBean.getResultSet();
					resultSetMetaData = (ResultSetMetaData) resultSet.getMetaData();
					columnCount = resultSetMetaData.getColumnCount();
					renderNumberOfColumns = true;
					resultSet.last();
					rowCount = resultSet.getRow();
					resultSet.beforeFirst();

					renderNumberOfObservations = true;
					
					String columnName;
					for (int i = 1; i <= columnCount; i++) {
						String name = resultSetMetaData.getColumnName(i);
						String datatype = resultSetMetaData.getColumnTypeName(i);
						if (datatype.equalsIgnoreCase("char") || datatype.equalsIgnoreCase("varchar")) {
							categoricalData.add(name);
						} else
							numericData.add(name);
					}if(categoricalData.size()==0) {
					for (int j = 1; j < columnCount + 1; j++) {
						List<Double> values = new ArrayList<Double>();
						columnName = resultSetMetaData.getColumnName(j);
						String columnType = resultSetMetaData.getColumnTypeName(j);
						resultSet.beforeFirst();
						while (resultSet.next()) {
							switch (columnType.toLowerCase()) {
							case "int":
								values.add((double) resultSet.getInt(columnName));
								break;
							case "smallint":
								values.add((double) resultSet.getInt(columnName));
								break;
							case "float":
								values.add((double) resultSet.getFloat(columnName));
								break;
							case "double":
								values.add((double) resultSet.getDouble(columnName));
								break;
							case "long":
								values.add((double) resultSet.getLong(columnName));
								break;
							default:
								values.add((double) resultSet.getDouble(columnName));
								break;
							}
						}
						Collections.sort(values);
						
						valuesArray = new double[values.size()];
						
						
						
						for (int i = 0; i < values.size(); i++) {
							valuesArray[i] = (double) values.get(i);
						}
						double minValue = valuesArray[0];
						double maxValue = valuesArray[valuesArray.length -1];
						double range=maxValue-minValue;
						double rangeMax=minValue;
						if (range >=50)
						{
							for (int i=1; i<=50;i++) {
								 minValue=rangeMax;
								 rangeMax=range*0.02*i;
								 for (int l = 0; l < values.size(); l++) {
									 	if(values.get(l)>=minValue && values.get(l)<rangeMax)
									 		valuesArray[l]=rangeMax;
									}
							}
								 
							
							}
						
						 HistogramDataset dataset = new HistogramDataset();
					 
					       dataset.addSeries("Histogram",valuesArray,50);
					       String plotTitle = "Histogram"; 
					       String xaxis = columnName;
					       String yaxis = "Frequency"; 
					       PlotOrientation orientation = PlotOrientation.VERTICAL; 
					       boolean show = false; 
					       boolean toolTips = false;
					       boolean urls = false; 
					       JFreeChart chart = ChartFactory.createHistogram( plotTitle, xaxis, yaxis, 
					                dataset, orientation, show, toolTips, urls);
					      
					       try {

					   		FacesContext context = FacesContext.getCurrentInstance();
					   		String path = context.getExternalContext().getRealPath("/temp");
					   		chartPath = "/temp/"+databaseBean.getUserName()+columnName+"_hist"+".png";
					   		String filePath = path+"/"+databaseBean.getUserName()+columnName+"_hist"+".png";
					   		File out = null;
					   		out = new File(filePath);
					   		ChartUtilities.saveChartAsPNG(out, chart, 600, 450);
					   		setRenderChart(true);
					 //  		ExportFile.appendToFile(command+"\t" +columnName);
					//		ExportFile.appendToFile(System.getProperty("line.separator"));
					   		return "STATSUCCESS";
					   	}
					   	catch (IOException e)
					   	{
					   		e.printStackTrace();
					   		return StringConstants.FAIL;
					   	}
					   	catch(Exception e){
					   		e.printStackTrace();
					   		return StringConstants.FAIL;
					   	}
					   	
					}
					}else {
						message="Please select a numerical variable";
						renderMessage = true;
						databaseBean.setQueryRendered(false);
						return StringConstants.FAIL;
					}
				}
					
		 
	
	}
	
			}catch(Exception e) {
				message = "Error generating histogram ";
				renderMessage = true;
				renderNumberOfObservations = false;
				renderNumberOfColumns = false;
				return "FAIL";
			}
	}
		return "STATSUCCESS";

	}
	public String createScatterplotGraph() {
		reset();
		resetButton();
		generateRegressionReport();
		//String command="scatterplot";
		renderRegressionResult=false;
		JFreeChart chart = ChartFactory.createScatterPlot(
				"Scatter Plot, table: "+databaseBean.getTableName(), // chart title
				predictorValue, // x axis label
				responseValue, // y axis label
				getXySeriesVariable(), // data
				PlotOrientation.VERTICAL,
				true, // include legend
				true, // tooltip
				false // URL
				);
		//double regressionParameters[] = Regression.getOLSRegression(getXySeriesVariable(),0); //gives intercept and slope for y=a+bx
		double[] regressionParameters=new double[2];
		regressionParameters[0]=intercept;
		regressionParameters[1]=slope;
		LineFunction2D linefunction2d = new LineFunction2D(
				regressionParameters[0], regressionParameters[1]);
 
		XYDataset dataset = DatasetUtilities.sampleFunction2D(linefunction2d,
				0D,10000000, 600, "Fitted Regression Line");
		XYPlot xyplot = chart.getXYPlot();
		xyplot.setDataset(1, dataset);
		XYLineAndShapeRenderer xylineandshaperenderer = new XYLineAndShapeRenderer(
				true, false);
		xylineandshaperenderer.setSeriesPaint(0, Color.YELLOW);
		xyplot.setRenderer(1, xylineandshaperenderer);
	try {

		FacesContext context = FacesContext.getCurrentInstance();
		String path = context.getExternalContext().getRealPath("/temp");
		chartPath = "/temp/"+databaseBean.getUserName()+"_scatter"+".png";
		String filePath = path+"/"+databaseBean.getUserName()+"_scatter"+".png";
		File out = null;
		out = new File(filePath);
		ChartUtilities.saveChartAsPNG(out, chart, 600, 450);
		setRenderChart(true);
	//	ExportFile.appendToFile(command+"\t"+responseValue+"\t"+predictorValue);
	//	ExportFile.appendToFile(System.getProperty("line.separator"));
		return "STATSUCCESS";
	} 
	catch (IOException e)
	{
		e.printStackTrace();
		return "STATFAIL";
	}
	catch(Exception e){
		e.printStackTrace();
		return "STATFAIL";
	}

	}
public String correlateVariables() {
		
		reset();
		resetButton();
		loadDatabaseBeanObject();
		//String command="corr";
		
		String status=generateRegressionReport();
		if (status == "STATSUCCESS"){
		renderRegressionResult = false;
		renderNumberOfColumns = false;
		renderNumberOfObservations = false;
		
		renderCorrelationResult=true;
	//	ExportFile.appendToFile(command+"\t" +responseValue+"\t"+ predictorValue );
	//	ExportFile.appendToFile(System.getProperty("line.separator"));
		
		return "STATSUCCESS";
		}else {
			
		return "STATFAIL";
}
		
		
	}
	public String resetButton() {
		if(descriptiveStatsBeanList != null)
			descriptiveStatsBeanList.clear();
		columnRender = false;
		renderTabledata = false;
		renderRegressionButton = true;
		renderCorrelationResult=false;
		renderColumnListbutton = false;
		renderRegressionColumn = false;
		renderRegressionResult = false;
		renderNumberOfObservations = false;
		renderNumberOfColumns = false;
		renderReport = false;
		renderMessage = false;
		if(columnSelected != null)
		columnSelected.clear();
		return "SUCCESS";
	}

	public void reset() {
		renderMessage = false;
		renderTabledata = false;
		renderRegressionResult = false;
		renderNumberOfObservations = false;
		renderNumberOfColumns = false;
		renderChart=false;
	}

	public List<String> getColumnSelected() {
		return columnSelected;
	}

	public void setColumnSelected(List<String> columnSelected) {
		this.columnSelected = columnSelected;
	}

	public List<String> getColumnsList() {
		return columnsList;
	}

	public void setColumnsList(List<String> columnsList) {
		this.columnsList = columnsList;
	}

	public boolean isColumnRender() {
		return columnRender;
	}

	public void setColumnRender(boolean columnRender) {
		this.columnRender = columnRender;
	}

	public ResultSet getResultSet() {
		return resultSet;
	}

	public void setResultSet(ResultSet resultSet) {
		this.resultSet = resultSet;
	}

	public boolean isRenderTablename() {
		return renderTablename;
	}

	public void setRenderTablename(boolean renderTablename) {
		this.renderTablename = renderTablename;
	}

	public ResultSetMetaData getResultSetMetaData() {
		return resultSetMetaData;
	}

	public List<DescriptiveStatsBean> getDescriptiveStatsBeanList() {
		return descriptiveStatsBeanList;
	}

	public void setDescriptiveStatsBeanList(List<DescriptiveStatsBean> descriptiveStatsBeanList) {
		this.descriptiveStatsBeanList = descriptiveStatsBeanList;
	}

	public void setResultSetMetaData(ResultSetMetaData resultSetMetaData) {
		this.resultSetMetaData = resultSetMetaData;
	}

	/*public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}*/

	public List<String> getTableList() {
		return tableList;
	}

	public void setTableList(List<String> tableList) {
		this.tableList = tableList;
	}

	public boolean isRenderSchema() {
		return renderSchema;
	}

	public void setRenderSchema(boolean renderSchema) {
		this.renderSchema = renderSchema;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isRenderMessage() {
		return renderMessage;
	}

	public void setRenderMessage(boolean renderMessage) {
		this.renderMessage = renderMessage;
	}

	public List<String> getColumns() {
		return columns;
	}

	public void setColumns(List<String> columns) {
		this.columns = columns;
	}

	public int getColumnCount() {
		return columnCount;
	}

	public void setColumnCount(int columnCount) {
		this.columnCount = columnCount;
	}

	public int getRowCount() {
		return rowCount;
	}

	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

	public boolean isRenderTabledata() {
		return renderTabledata;
	}

	public void setRenderTabledata(boolean renderTabledata) {
		this.renderTabledata = renderTabledata;
	}

	public boolean isRenderRegressionColumn() {
		return renderRegressionColumn;
	}

	public void setRenderRegressionColumn(boolean renderRegressionColumn) {
		this.renderRegressionColumn = renderRegressionColumn;
	}

	public boolean isRenderColumnListbutton() {
		return renderColumnListbutton;
	}

	public void setRenderColumnListbutton(boolean renderColumnListbutton) {
		this.renderColumnListbutton = renderColumnListbutton;
	}

	public boolean isRenderRegressionButton() {
		return renderRegressionButton;
	}

	public void setRenderRegressionButton(boolean renderRegressionButton) {
		this.renderRegressionButton = renderRegressionButton;
	}

	public boolean isButtonDisable() {
		return buttonDisable;
	}

	public void setButtonDisable(boolean buttonDisable) {
		this.buttonDisable = buttonDisable;
	}

	public List<String> getCategoricalData() {
		return categoricalData;
	}

	public void setCategoricalData(List<String> categoricalData) {
		this.categoricalData = categoricalData;
	}

	public List<String> getNumericData() {
		return numericData;
	}

	public void setNumericData(List<String> numericData) {
		this.numericData = numericData;
	}

	public String getPredictorValue() {
		return predictorValue;
	}

	public void setPredictorValue(String predictorValue) {
		this.predictorValue = predictorValue;
	}

	public String getResponseValue() {
		return responseValue;
	}

	public void setResponseValue(String responseValue) {
		this.responseValue = responseValue;
	}

	public boolean isRenderReport() {
		return renderReport;
	}

	public void setRenderReport(boolean renderReport) {
		this.renderReport = renderReport;
	}

	public boolean isRenderRegressionResult() {
		return renderRegressionResult;
	}

	public void setRenderRegressionResult(boolean renderRegressionResult) {
		this.renderRegressionResult = renderRegressionResult;
	}

	public boolean isRenderNumberOfObservations() {
		return renderNumberOfObservations;
	}

	public void setRenderNumberOfObservations(boolean renderNumberOfObservations) {
		this.renderNumberOfObservations = renderNumberOfObservations;
	}

	public boolean isRenderNumberOfColumns() {
		return renderNumberOfColumns;
	}

	public void setRenderNumberOfColumns(boolean renderNumberOfColumns) {
		this.renderNumberOfColumns = renderNumberOfColumns;
	}

	public double getMedian1() {
		return median1;
	}

	public void setMedian1(double median1) {
		this.median1 = median1;
	}

	public double getQuartile1() {
		return quartile1;
	}

	public void setQuartile1(double quartile1) {
		this.quartile1 = quartile1;
	}

	public double getQuartile3() {
		return quartile3;
	}

	public void setQuartile3(double quartile3) {
		this.quartile3 = quartile3;
	}

	public XYSeriesCollection getXySeriesVariable() {
		return xySeriesVariable;
	}

	public void setXySeriesVariable(XYSeriesCollection xySeriesVariable) {
		this.xySeriesVariable = xySeriesVariable;
	}

	public List<String> getList() {
		return list;
	}

	public void setList(List<String> list) {
		this.list = list;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public XYSeriesCollection getXyTimeSeriesCollection() {
		return xyTimeSeriesCollection;
	}

	public void setXyTimeSeriesCollection(XYSeriesCollection xyTimeSeriesCollection) {
		this.xyTimeSeriesCollection = xyTimeSeriesCollection;
	}

	public XYSeriesCollection getXySeriesVariables() {
		return xySeriesVariables;
	}

	public void setXySeriesVariables(XYSeriesCollection xySeriesVariables) {
		this.xySeriesVariables = xySeriesVariables;
	}

	private String regressionEquation;

	public XYSeries getPredictorSeries() {
		return predictorSeries;
	}

	public void setPredictorSeries(XYSeries predictorSeries) {
		this.predictorSeries = predictorSeries;
	}

	public XYSeries getResponseSeries() {
		return responseSeries;
	}

	public void setResponseSeries(XYSeries responseSeries) {
		this.responseSeries = responseSeries;
	}

	public String getRegressionEquation() {
		return regressionEquation;
	}

	public void setRegressionEquation(String regressionEquation) {
		this.regressionEquation = regressionEquation;
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
	public boolean isRenderChart() {
		return renderChart;
	}
	public void setRenderChart(boolean renderChart) {
		this.renderChart = renderChart;
	}
	public double getCorr() {
		return corr;
	}
	public void setCorr(double corr) {
		this.corr = corr;
	}
	public boolean isRenderCorrelationResult() {
		return renderCorrelationResult;
	}
	public void setRenderCorrelationResult(boolean renderCorrelationResult) {
		this.renderCorrelationResult = renderCorrelationResult;
	}

}
