package edu.uic.ids.model;

import java.sql.PreparedStatement;
//import java.security.Timestamp;
import java.sql.Timestamp;
//import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.servlet.jsp.jstl.sql.Result;
import javax.servlet.jsp.jstl.sql.ResultSupport;
//import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.uic.ids.util.StringConstants;
import edu.uic.ids.util.Session;

@ManagedBean(name = StringConstants.DATABASE_BEAN)
@SessionScoped
public class DatabaseBean {
	private String userName;
	private String password;
	private String dbHost;
	private String dbmsType;
	private String dbSchema;
	private String jdbcDriver;
	private String tableName;
	// private String query;
	private String table;
	private final String dbmsPort;
	private String errorMessage;
	private String sqlQuery;

	private String url;
	private Connection connection;
	private String message;
	private Statement statement;
	private DatabaseMetaData databaseMetaData;
	private ResultSet resultSet;

	private ResultSetMetaData resultSetMetaData;

	private Result result;

	private boolean tableListRendered;
	private boolean columnListRendered;
	private boolean messageRendered;
	private boolean queryTypeFlag = false;
	private boolean queryFlag = false;

	boolean queryRendered;
	private ArrayList<String> schemaList = null;
	private ArrayList<String> tableList = null;
	private ArrayList<String> columnList = null;
	private ArrayList<String[]> dataList = null;
	private ArrayList<String> columnNamesSelected;
	@SuppressWarnings("rawtypes")
	private List dataOperationsList = new ArrayList<>();
	@SuppressWarnings("rawtypes")
	private List tableColumns = new ArrayList<>();
	@SuppressWarnings("rawtypes")
	private List userColumns = new ArrayList<>();

	private String ticker; 
	private List<String> tickers;
	private String dateFrom; 
	private String dateTo; 
	private String sort;
	private String userQuery="Enter a query here";
	 

	private static final String[] TABLE_TYPES = { "TABLE" };
	public int numberOfColumns;
	public int numberOfRows;
	private String tableNameValue;
	private boolean renderErrorMessage;
	private boolean renderColumn;
	private boolean renderDataTable;
	private boolean renderTable;
	private int nc;
	private int nr;
	private String executeQuery;
	private String processQuery;
	List<Map<String, Object>> resultData = new ArrayList<Map<String, Object>>();
	private String statemt;

	public DatabaseBean() {
		dbmsPort = "3306";
		queryRendered = false;
		tableName = "";
		dateFrom=""; 
		dateTo=""; 
		ticker="";
		tickers = new ArrayList<String>();
		 
	}

	public String getSqlQuery() {
		return sqlQuery;
	}

	public void setSqlQuery(String sqlQuery) {
		this.sqlQuery = sqlQuery;
	}

	public boolean isQueryRendered() {
		return queryRendered;
	}

	public void setQueryRendered(boolean queryRendered) {
		this.queryRendered = queryRendered;
	}

	public boolean isTableListRendered() {
		return tableListRendered;
	}

	public void setTableListRendered(boolean tableListRendered) {
		this.tableListRendered = tableListRendered;
	}

	public boolean isColumnListRendered() {
		return columnListRendered;
	}

	public void setColumnListRendered(boolean columnListRendered) {
		this.columnListRendered = columnListRendered;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getDbmsPort() {
		return dbmsPort;
	}

	public ArrayList<String[]> getDataList() {
		return dataList;
	}

	public void setDataList(ArrayList<String[]> dataList) {
		this.dataList = dataList;
	}

	public ArrayList<String> getColumnNamesSelected() {
		return columnNamesSelected;
	}

	public void setColumnNamesSelected(ArrayList<String> columnNamesSelected) {
		this.columnNamesSelected = columnNamesSelected;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Statement getStatement() {
		return statement;
	}

	public void setStatement(Statement statement) {
		this.statement = statement;
	}

	public DatabaseMetaData getDatabaseMetaData() {
		return databaseMetaData;
	}

	public void setDatabaseMetaData(DatabaseMetaData databaseMetaData) {
		this.databaseMetaData = databaseMetaData;
	}

	public ResultSetMetaData getResultSetMetaData() {
		return resultSetMetaData;
	}

	public void setResultSetMetaData(ResultSetMetaData resultSetMetaData) {
		this.resultSetMetaData = resultSetMetaData;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public int getNumberOfColumns() {
		return numberOfColumns;
	}

	public void setNumberOfColumns(int numberOfColumns) {
		this.numberOfColumns = numberOfColumns;
	}

	public static String[] getTableTypes() {
		return TABLE_TYPES;
	}

	public void setTableList(ArrayList<String> tableList) {
		this.tableList = tableList;
	}

	public void setColumnList(ArrayList<String> columnList) {
		this.columnList = columnList;
	}

	public ResultSet getResultSet() {
		return resultSet;
	}

	public void setResultSet(ResultSet resultSet) {
		this.resultSet = resultSet;
	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}

	public String getDbHost() {
		return dbHost;
	}

	public String getDbmsType() {
		return dbmsType;
	}

	public String getDbSchema() {
		return dbSchema;
	}

	public String getJdbcDriver() {
		return jdbcDriver;
	}

	public String getUrl() {
		return url;
	}

	public ArrayList<String> getTableList() {
		return tableList;
	}

	public ArrayList<String> getColumnList() {
		return columnList;
	}

	public ArrayList<String> getSchemaList() {
		return schemaList;
	}

	public void setSchemaList(ArrayList<String> schemaList) {
		this.schemaList = schemaList;
	}

	public void setUserName(String newUserName) {
		userName = newUserName;
	}

	public void setPassword(String newPassword) {
		password = newPassword;
	}

	public void setDbHost(String newDbHost) {
		dbHost = newDbHost;
	}

	public void setDbmsType(String newDbmsType) {
		dbmsType = newDbmsType;
	}

	public void setDbSchema(String newDbSchema) {
		dbSchema = newDbSchema;
	}

	public void setJdbcDriver(String newJdbcDriver) {
		jdbcDriver = newJdbcDriver;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isMessageRendered() {
		return messageRendered;
	}

	public void setMessageRendered(boolean messageRendered) {
		this.messageRendered = messageRendered;
	}

	public void insertDB(String ticker, String transactionDate, Double open, Double high, Double low, Double close,
			Double adjClose, Double volume, Double splitCoefficient, Double dividend) {
		sqlQuery = "INSERT INTO GS305_STOCKTRANSACTION (TICKER,TRANSACTIONDATE,OPEN,HIGH,LOW,CLOSE,ADJ_CLOSE,VOLUME,SPLIT_COEFFICIENT,DIVIDEND) VALUES (?,?,?,?,?,?,?,?,?,?);";

		setTable(null);
		try {
			PreparedStatement stmt = connection.prepareStatement(sqlQuery);
			stmt.setString(1, ticker);
			stmt.setString(2, transactionDate);
			stmt.setString(3, Double.toString(open));
			stmt.setString(4, Double.toString(high));
			stmt.setString(5, Double.toString(low));
			stmt.setString(6, Double.toString(close));
			stmt.setString(7, Double.toString(adjClose));
			stmt.setString(8, Double.toString(volume));
			stmt.setString(9, Double.toString(dividend));
			stmt.setString(10, Double.toString(splitCoefficient));

			stmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public String droptable() {
		queryRendered = false;

		sqlQuery = "DROP TABLE " + tableName + " ;";

		setTable(null);
		resetButton();
		try {
			PreparedStatement stmt = connection.prepareStatement(sqlQuery);
			stmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return StringConstants.SUCCESS;
	}

	public String createTable() {
		sqlQuery = "CREATE TABLE GS305_STOCKTRANSACTION ( id int(11) NOT NULL AUTO_INCREMENT," + "TICKER VARCHAR(45),"
				+ "TRANSACTIONDATE VARCHAR(45)," + "OPEN DOUBLE," + "HIGH DOUBLE," + "LOW DOUBLE," + "CLOSE DOUBLE,"
				+ "ADJ_CLOSE DOUBLE," + "VOLUME DOUBLE," + "SPLIT_COEFFICIENT DOUBLE," + "DIVIDEND DOUBLE,"
				+ "dailyReturn DOUBLE," + "PRIMARY KEY(id))";
		setTable(null);
		try {
			PreparedStatement stmt = connection.prepareStatement(sqlQuery);
			stmt.executeUpdate();
			// InsertintoDB()
		} catch (SQLException e) {
			e.printStackTrace();
		}
		resetButton();
		return StringConstants.SUCCESS;
	}

	public String connect() {
		errorMessage = "";
		if (userName == null || userName.trim().equals("") || password == null || password.trim().equals("")) {
			errorMessage = "*Fill Mandatory fields";
			return StringConstants.FAIL;
		}
		switch (dbmsType) {
		case StringConstants.MY_SQL:
			jdbcDriver = StringConstants.MYSQL_DRIVER;
			url = StringConstants.MYSQL_URL + dbHost + ":" + dbmsPort + "/" + dbSchema;
			break;
		case StringConstants.DB2:
			jdbcDriver = StringConstants.DB2_DRIVER;
			url = StringConstants.DB2_URL + dbHost + ":" + dbmsPort + "/" + dbSchema;
			break;
		case StringConstants.ORACLE:
			jdbcDriver = StringConstants.ORACLE_DRIVER;
			url = StringConstants.ORACLE_URL + dbHost + ":" + dbmsPort + "/" + dbSchema;
			break;
		}
		try {
			Class.forName(jdbcDriver);
			connection = DriverManager.getConnection(url, userName, password);
			System.out.print(connection.toString());
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			databaseMetaData = connection.getMetaData();
			return StringConstants.SUCCESS;
		} catch (ClassNotFoundException ce) {
			errorMessage = "Database: " + dbmsType + " not supported.";
			return StringConstants.FAIL;

		} catch (SQLException se) {
			if (se.getSQLState().equals(StringConstants.INVALID_CREDENTIALS)) {
				errorMessage = "Either username or password does not match";
			} else if (se.getSQLState().equals(StringConstants.UNKNOWN_SCHEMA)) {
				errorMessage = "Schema does not exists";
			} else if (se.getSQLState().equals(StringConstants.APPLICATION_TIMEOUT)) {
				errorMessage = "Application Timeout \n or check host and port";
			} else {
				errorMessage = "SQL Exception occurred!\n" + "Error Code: " + se.getErrorCode() + "\n" + "SQL State: "
						+ se.getSQLState() + "\n" + "Message :" + se.getMessage() + "\n\n";
			}
			return StringConstants.FAIL;
		} catch (Exception e) {
			errorMessage = "Exception occurred: " + e.getMessage();
			return StringConstants.FAIL;
		}

	}

	public String dbExecute(String tableName, String query) {
		String status = "";
		queryRendered = false;
		try {
			if (connection == null) {
				connect();
			}
			statement = connection.createStatement();
			if (query.toLowerCase().contains("select")) {
				resultSet = statement.executeQuery(query);
				resultSetMetaData = resultSet.getMetaData();
				generateResult();
				numberOfColumns = resultSetMetaData.getColumnCount();
				if (resultSet != null) {
					resultSet.last();
					nr = resultSet.getRow();
					dataList = new ArrayList<String[]>(nr);
					resultSet.beforeFirst();

					int numOfCols = resultSet.getMetaData().getColumnCount();
					String[] dataRow = new String[numOfCols];
					while (resultSet.next()) {
						for (int i = 1, j = 0; i < numOfCols + 1; i++, j++) {
							if (resultSet.getString(i) != null)
								dataRow[j] = resultSet.getString(i).toString();
						}
						dataList.add(dataRow);
					}
				}
				
				status = "SUCCESS";
				queryRendered = true;
			}
			
		} catch (SQLException se) {
			message = se.getMessage();
			messageRendered = true;
			status = "FAIL";
			se.printStackTrace();
		} catch (Exception e) {
			message = e.getMessage();
			messageRendered = true;
			status = "FAIL";
			e.printStackTrace();
		}
		return status;

	}


	

	public String listTableswithDefaultSchema() {
		/*String command = " tableList";
		
		 * ExportFile.appendToFile(command);
		 * ExportFile.appendToFile(System.getProperty("line.separator"));
		 */
		String status = "";
		tableList = null;
		columnList = null;
		sqlQuery = "";
//		message = "";
		queryRendered = false;
		try {
			if (connection == null) {
				connect();
			}
			databaseMetaData = connection.getMetaData();
			resultSet = databaseMetaData.getTables(null, dbSchema, null, TABLE_TYPES);
			if (resultSet != null) {
				resultSet.last();
				int noOfRows = resultSet.getRow();
				tableList = new ArrayList<String>(noOfRows);
				resultSet.beforeFirst();
				String eachTableName = "";
				while (resultSet.next()) {
					eachTableName = resultSet.getString("TABLE_NAME");
					if (!dbmsType.equalsIgnoreCase("oracle") || eachTableName.length() < 4)
						tableList.add(eachTableName);
					else if (!eachTableName.substring(0, 4).equalsIgnoreCase("BIN$"))
						tableList.add(eachTableName);
				}
			}
			tableListRendered = true;
			columnListRendered = false;
			status = StringConstants.SUCCESS;
		} catch (SQLException e) {
			tableListRendered = false;
			status = StringConstants.FAIL;
		}
		return status;
	}

	public String listColumns() {
		String status = "";
		columnList = null;
		sqlQuery = "";
		queryRendered = false;
		
		message = "";
		if (tableName == null || tableName.equals("")) {
			message = "Please select a Table";
			messageRendered = true;
			return StringConstants.FAIL;
		}
		try {
			if (connection == null) {
				connect();
			}

			/* String command = "columnList";
			 * ExportFile.appendToFile("use"+"\t"+tableName);
			 * ExportFile.appendToFile(System.getProperty("line.separator"));
			 * ExportFile.appendToFile(command);
			 * ExportFile.appendToFile(System.getProperty("line.separator"));
			 */
			databaseMetaData = connection.getMetaData();
			resultSet = databaseMetaData.getColumns(null, dbSchema, tableName, null);
			String columnName = "";
			if (resultSet != null) {
				resultSet.last();
				int noOfRows = resultSet.getRow();
				columnList = new ArrayList<String>(noOfRows);
				resultSet.beforeFirst();
				while (resultSet.next()) {
					columnName = resultSet.getString("COLUMN_NAME");
					columnList.add(columnName);
				}
			}
			columnListRendered = true;
			status = StringConstants.SUCCESS;
		} catch (SQLException se) {
			columnListRendered = false;
			status = StringConstants.FAIL;
		}
		return status;
	}

	public String loadTableContents() {
		queryRendered = false;
		try {
			if (null != tableName && !"".equals(tableName)) {
				listColumns();
				columnNamesSelected = columnList;
				loadSelectedColumns();
				sqlQuery = "select * from " + dbSchema + "." + tableName + " ;";

				return StringConstants.SUCCESS;
			} else {
				message = "Kindly load and select a table.";
				messageRendered = true;
				return StringConstants.FAIL;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			queryRendered = false;
			return StringConstants.FAIL;
		}
	}

	public String loadSelectedColumns() {
		queryRendered = false;
		
		if (tableName == null || tableName.equals("")) {
			message = "Kindly load and select a table.";
			messageRendered = true;
			return StringConstants.FAIL;
		}
		if (null != columnNamesSelected && !columnNamesSelected.isEmpty() && columnListRendered) {
			sqlQuery = "select " + columnNamesSelected.toString().replace("[", "").replace("]", "") + " from "
					+ dbSchema + "." + tableName + " ;";
			dbExecute(tableName, sqlQuery);
			generateResult();
			queryRendered = true;
			/* String command = "select";
			 * String selectCommand = command + "\t"
					+ columnNamesSelected.toString().replace("[", "").replace("]", "").replace(",", "\t");
			
			 * ExportFile.appendToFile(selectCommand.trim() );
			 * ExportFile.appendToFile(System.getProperty("line.separator"));
			 */
			return StringConstants.SUCCESS;
		} else {
			message = "Kindly load and select column(s).";
			messageRendered = true;
			return StringConstants.FAIL;
		}
	}

	public void generateResult() {
		if (resultSet != null) {
			result = ResultSupport.toResult(resultSet);
		}
	}

	public String computeReturnMethod() {
		System.out.println("Entered");
		setRenderErrorMessage(false);
		setRenderColumn(true);
		setRenderDataTable(false);

		executeQuery = null;
		nc = 0;
		nr = 0;

		processQuery = "SELECT * FROM GS305_STOCKTRANSACTION;";

		if (processQuery.length() != 0) {
			setRenderErrorMessage(false);
			setRenderDataTable(false);
			setRenderColumn(true);

			executeQuery = null;

			nc = 0;
			nr = 0;

			if (!dataOperationsList.isEmpty()) {
				dataOperationsList.clear();
			}

			if (!tableColumns.isEmpty()) {
				tableColumns.clear();
			}
			try {
				dataOperationsList = executeComputeReturn(processQuery);
				int lastindex = dataOperationsList.size() - 1;

				for (int i = 0; i < dataOperationsList.size(); i++) {

					if (i < lastindex) {
						// Code for ticker
						String t1 = dataOperationsList.get(i).toString();
						String[] result = t1.split(",");
						String ticker1 = result[1].replaceAll("}", "");
						String[] result1 = ticker1.split("=");
						String ticker = result1[1].replaceAll("}", "");
						// Code for transaction date
						String tDate1 = result[2].replaceAll("}", "");
						String[] result2 = tDate1.split("=");
						String transactionDate = result2[1].replaceAll("}", "");
						// Code for close1
						String c1 = result[7].replaceAll("}", "");
						String[] result3 = c1.split("=");
						String Day1close = result3[1].replaceAll("}", "");
						// Code for close2
						String c2 = dataOperationsList.get(i + 1).toString();
						String[] result4 = c2.split(",");
						String close2 = result4[7].replaceAll("}", "");
						String[] result5 = close2.split("=");
						String Day2close = result5[1].replaceAll("}", "");

						Double dailyReturn = Math.log(Double.parseDouble(Day2close) / Double.parseDouble(Day1close));
						UpdateDB(ticker, transactionDate, dailyReturn);

					} else {
						// Code for ticker
						String t6 = dataOperationsList.get(lastindex).toString();
						String[] result6 = t6.split(",");
						String ticker2 = result6[1].replaceAll("}", "");
						String[] result7 = ticker2.split("=");
						String ticker = result7[1].replaceAll("}", "");
						// Code for transaction date
						String tDate2 = result6[2].replaceAll("}", "");
						String[] result8 = tDate2.split("=");
						String transactionDate = result8[1].replaceAll("}", "");
						// Code for close2
					//	String c3 = dataOperationsList.get(lastindex).toString();
					//	String[] result9 = c3.split(",");
					//	String close3 = result9[7].replaceAll("}", "");
					//	String[] result10 = close3.split("=");
					//	String Day2close = result10[1].replaceAll("}", "");
						Double dailyReturn = 0.0;
						UpdateDB(ticker, transactionDate, dailyReturn);
					}
				}

			} catch (SQLException e) {
				if (null != errorMessage) {
					setErrorMessage(null);
				}
				setErrorMessage(e.getMessage());
				setRenderErrorMessage(true);
				setRenderTable(false);
			}

		}
		return StringConstants.SUCCESS;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List executeComputeReturn(String processQuery) throws SQLException {
		setQueryTypeFlag(false);
		resultData.clear();
		userColumns.clear();

		char lastChar = processQuery.charAt(processQuery.length() - 1);
		if (lastChar != ';') {

			sqlQuery = processQuery + ";";
		} else {
			sqlQuery = processQuery;
		}

		PreparedStatement statemt = connection.prepareStatement(sqlQuery);

		System.out.println(statemt);
		setStatemt(statemt.toString());

		System.out.println("After setting Statement" + statemt);

		if (sqlQuery.toUpperCase().contains("SELECT")) {

			setQueryTypeFlag(true);

			resultSet = statemt.executeQuery();

			setQueryFlag(true);

			ResultSetMetaData metaData = resultSet.getMetaData();
			int columnCount = metaData.getColumnCount();

			for (int i = 1; i <= columnCount; i++) {
				String name = metaData.getColumnName(i);
				userColumns.add(name);
			}

			while (resultSet.next()) {
				Map<String, Object> columns = new LinkedHashMap<String, Object>();

				for (int i = 1; i <= columnCount; i++) {

					columns.put(metaData.getColumnLabel(i), resultSet.getObject(i));
				}

				resultData.add(columns);
				// break;
			}
			resultSet.beforeFirst();

			return resultData;

		} else {
			statemt.executeUpdate();
			setQueryTypeFlag(false);
			return resultData;

		}

	}

	public void UpdateDB(String ticker, String transactionDate, Double dailyReturn) {
		sqlQuery = "UPDATE GS305_STOCKTRANSACTION SET dailyReturn = ? WHERE ticker = ? AND transactionDate = ?;";

		setTable(null);
		try {
			PreparedStatement stmt = connection.prepareStatement(sqlQuery);
			stmt.setString(1, Double.toString(dailyReturn));
			stmt.setString(2, ticker);
			stmt.setString(3, transactionDate);

			stmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String getTableNameValue() {
		return tableNameValue;
	}

	public void setTableNameValue(String tableNameValue) {
		this.tableNameValue = tableNameValue;
	}

	public boolean isRenderErrorMessage() {
		return renderErrorMessage;
	}

	public void setRenderErrorMessage(boolean renderErrorMessage) {
		this.renderErrorMessage = renderErrorMessage;
	}

	public boolean isRenderColumn() {
		return renderColumn;
	}

	public void setRenderColumn(boolean renderColumn) {
		this.renderColumn = renderColumn;
	}

	public boolean isRenderDataTable() {
		return renderDataTable;
	}

	public void setRenderDataTable(boolean renderDataTable) {
		this.renderDataTable = renderDataTable;
	}

	public int getNc() {
		return nc;
	}

	public void setNc(int nc) {
		this.nc = nc;
	}

	public int getNr() {
		return nr;
	}

	public void setNr(int nr) {
		this.nr = nr;
	}

	public String getExecuteQuery() {
		return executeQuery;
	}

	public void setExecuteQuery(String executeQuery) {
		this.executeQuery = executeQuery;
	}

	public String getProcessQuery() {
		return processQuery;
	}

	public void setProcessQuery(String processQuery) {
		this.processQuery = processQuery;
	}

	public boolean isRenderTable() {
		return renderTable;
	}

	public void setRenderTable(boolean renderTable) {
		this.renderTable = renderTable;
	}
	@SuppressWarnings("rawtypes")
	public List getDataOperationsList() {
		return dataOperationsList;
	}
	@SuppressWarnings("rawtypes")
	public void setDataOperationsList(List dataOperationsList) {
		this.dataOperationsList = dataOperationsList;
	}
	@SuppressWarnings("rawtypes")
	public List getTableColumns() {
		return tableColumns;
	}
	@SuppressWarnings("rawtypes")
	public void setTableColumns(List tableColumns) {
		this.tableColumns = tableColumns;
	}

	public boolean isQueryTypeFlag() {
		return queryTypeFlag;
	}

	public void setQueryTypeFlag(boolean queryTypeFlag) {
		this.queryTypeFlag = queryTypeFlag;
	}

	public List<Map<String, Object>> getResultData() {
		return resultData;
	}

	public void setResultData(List<Map<String, Object>> resultData) {
		this.resultData = resultData;
	}
	@SuppressWarnings("rawtypes")
	public List getUserColumns() {
		return userColumns;
	}
	@SuppressWarnings("rawtypes")
	public void setUserColumns(List userColumns) {
		this.userColumns = userColumns;
	}

	public boolean isQueryFlag() {
		return queryFlag;
	}

	public void setQueryFlag(boolean queryFlag) {
		this.queryFlag = queryFlag;
	}

	public String getStatemt() {
		return statemt;
	}

	public void setStatemt(String statemt) {
		this.statemt = statemt;
	}
	
	public String getTicker() {
		  return ticker; 
	}
	  
	public void setTicker(String ticker) { 
		  this.ticker = ticker; 
	}
	  
	public String getDateFrom() { 
		  return dateFrom; 
	}
	  
	public void setDateFrom(String dateFrom) {
		  this.dateFrom = dateFrom; 
	}
	  
	public String getDateTo() {
		  return dateTo; 
	}
	  
	public void setDateTo(String dateTo) { 
		  this.dateTo = dateTo; 
	}
	  
	  public List<String> getTickers() {
		  return tickers; 
		  }
	  
	  public void setTickers(List<String> tickers) { 
		  this.tickers = tickers; 
		  }
	  
	  public String getSort() { 
		  return sort; 
		  }
	  
	  public void setSort(String sort) { 
		  this.sort = sort; 
		  } 
	  public String getUserQuery() { 
		  return userQuery; 
	}
	  
	  public void setUserQuery(String userQuery) { 
		  this.userQuery = userQuery; 
		  }

		/* Method for fetching table data */
		public ResultSet fetchTableData(String sqlQuery)
		{
			try {
				resultSet = statement.executeQuery(sqlQuery);
				return resultSet;
			} catch (SQLException se) {
				message = "SQL Exception has occured while fetching data from the tables, find the error details below"+ "\n" + "Error Code: " + se.getErrorCode() + "\n" +
						"SQL State: " + se.getSQLState() + "\n" +
						"Message :" + se.getMessage();
				return resultSet = null;
			} catch (Exception e) {e.printStackTrace();
				message = "The application encountered an exception: " + e.getMessage();
				return resultSet = null;
			}
		}
	
		public String distinctTickers() {
			 try {	
				String sqlQuery;
				sqlQuery = "select distinct ticker from "+dbSchema+"." + tableName;
				
				resultSet = statement.executeQuery(sqlQuery);
				
				tickers.clear();
				
				
				String ticknames;
				if (resultSet != null) {
					while(resultSet.next()) {
						ticknames= resultSet.getString("ticker");
						tickers.add(ticknames);
					}
					
				} else {
					message = "No tickers available";
					messageRendered = true;
				}

			 }
			 catch (Exception err) {
					err.printStackTrace();
					message = "An exception has occured. The details of the error are given below." + "\n" + err.getMessage();
					messageRendered = true;
					return "FAIL";
				}
			
			return "SUCCESS";
		}
		
		
	public String filterTicker() {
			try {
				String query="";
				if(ticker!="") {
					if(dateFrom=="" || dateTo=="") {
						query= "select * from "+dbSchema+"."+tableName+" where Ticker like '%"+ticker+"%';";
						
						sqlQuery = query;
						resultSet=statement.executeQuery(query);
						buildMetaData();
						renderTable = true;
					}
					else {
						query= "select * from "+dbSchema+"."+tableName+" where Ticker like '%"+ticker+"%' AND TransactionDate BETWEEN '" + dateFrom + "' AND '" + dateTo +"';";
						
						sqlQuery = query;
						resultSet=statement.executeQuery(query);
						buildMetaData();
						renderTable = true;
					}
				}
				else {
					query = "select * from "+dbSchema+"." + tableName+";";
					
					resultSet = statement.executeQuery(query);
					sqlQuery = query;
					if (resultSet != null) {
						buildMetaData();
						renderTable = true;
						return "SUCCESS";
					} 
					else {
						//message = databaseBean.getMessage();
						messageRendered = true;
						return "FAIL";
					}
				}
			}
			catch (Exception err) {
				err.printStackTrace();
				//message = databaseBean.getMessage();
				messageRendered = true;
				return "Error!";
			}
			
			return "SUCCESS";
		}
		
		public String sortDate() {
			try {
				String query="";
				if(ticker=="") {
					if(dateFrom=="" || dateTo=="") {
						
						query= "select * from "+dbSchema+". "+tableName+" ORDER BY TransactionDate "+sort+";";
						userQuery = query;
						resultSet=statement.executeQuery(query);
						buildMetaData();
						renderTable = true;
					}
					else {
						
						query= "select * from "+dbSchema+"."+tableName+" where TransactionDate BETWEEN '" + dateFrom + "' AND '" + dateTo +"'"+ "ORDER BY TransactionDate "+sort+";";
						userQuery = query;
						resultSet=statement.executeQuery(query);
						buildMetaData();
						renderTable = true;	
					}
				}
				else {
					if(dateFrom =="" || dateTo=="") {
						
						query= "select * from "+dbSchema+"."+tableName+" where Ticker like '%"+ticker+"%' ORDER BY TransactionDate "+sort+";";
						userQuery = query;
						resultSet=statement.executeQuery(query);
						buildMetaData();
						renderTable = true;
					}
					else {
						
						query= "select * from "+dbSchema+"."+tableName+" where Ticker like '%"+ticker+"%' AND TransactionDate BETWEEN '" + dateFrom + "' AND '" + dateTo +"'"+ "ORDER BY TransactionDate "+sort+";";
						userQuery = query;
						resultSet=statement.executeQuery(query);
						buildMetaData();
						renderTable = true;	
					}
				}
			}
			catch (Exception err) {
				err.printStackTrace();
				//message = databaseBean.getMessage();
				messageRendered = true;
				return "Error!";
			}
			return "SUCCESS";
		}
		
		public String filterDate() {
			try {
				String query="";
				if(ticker=="") {
					
					query = "Select * from "+dbSchema+"."+tableName+" where TransactionDate BETWEEN '" + dateFrom + "' AND '" + dateTo +"';";
					sqlQuery = query;
					resultSet = statement.executeQuery(query);
					buildMetaData();
					renderTable = true;
				}
				else {
					
					query = "Select * from "+dbSchema+"."+tableName+" where ticker like '%" + ticker + "%' AND TransactionDate BETWEEN '" + dateFrom + "' AND '" + dateTo +"';";
					sqlQuery = query;
					resultSet = statement.executeQuery(query);
					buildMetaData();
					renderTable = true;	
				
				}
				
			}
			catch (Exception err) {
				err.printStackTrace();
				//message = databaseBean.getMessage();
				messageRendered = true;
				return "Error!";
			}
			
			return "SUCCESS";
					
		}
	
	  
	 

private void buildMetaData() {
		try {
			if (resultSet != null) {
				columnNamesSelected = new ArrayList<String>();
				resultSetMetaData = resultSet.getMetaData();
				result = ResultSupport.toResult(resultSet);
				numberOfColumns = resultSetMetaData.getColumnCount();
				nr = result.getRowCount();
				String columnNameList[] = result.getColumnNames();
				for (int i = 0; i < numberOfColumns; i++) {
					columnNamesSelected.add(columnNameList[i]);
				}
				renderTable = true;
			} else {
				// message = dbAccess.getMessage();
				messageRendered = true;
			}
		} catch (Exception err) {
			err.printStackTrace();
			message = err.getMessage();
			messageRendered = true;
		}
	} 

	public String resetButton() {
		tableListRendered = false;
		columnListRendered = false;
		messageRendered = false;
		queryRendered = false;
		if (tableList != null)
			tableList.clear();
		if (columnList != null)
			columnList.clear();
		dateFrom="";
		dateTo=""; 
		return "SUCCESS";
	}
	
	/*public String login() {
		try {
			
		sqlQuery = "CREATE TABLE if not exists gs305_usage(sessionID varchar(45), sessionOpen DATETIME, sessionClose DATETIME, clientIP varchar(45), primary key(sessionID))";
		PreparedStatement stmt = null;
		stmt = connection.prepareStatement(sqlQuery);
		stmt.executeUpdate();
		stmt = null;
		sqlQuery = null;

		Class.forName(jdbcDriver);
		connection = DriverManager.getConnection(url, getUserName(),getPassword());
		statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		databaseMetaData = connection.getMetaData();
		
		HttpSession session = Session.getSession();
		session.setAttribute("username", getUserName());
		session.getServletContext();
		
		System.out.println(InetAddress.getLocalHost().getHostAddress());
		
		PreparedStatement pstmt = null;
		try {
			sqlQuery = "CREATE TABLE if not exists gs305_usage(sessionID varchar(45), sessionOpen DATETIME, sessionClose DATETIME, clientIP varchar(45), primary key(sessionID))";
			pstmt = connection.prepareStatement(sqlQuery);
			pstmt.executeUpdate();
			pstmt.close();
			pstmt = null;
			sqlQuery = null;
			
			sqlQuery = "INSERT INTO gs305_usage(sessionID, sessionOpen, clientIP) VALUES(?,?,?) "
					+ " ON DUPLICATE KEY UPDATE sessionOpen=VALUES(sessionOpen)";
			pstmt = connection.prepareStatement(sqlQuery);
			pstmt.setString(1, session.getId());
			pstmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			pstmt.setString(3, InetAddress.getLocalHost().getHostAddress());
			
			pstmt.executeUpdate();
			sqlQuery = null;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(pstmt != null) {
				pstmt.close();
				pstmt = null;
			}
			if(sqlQuery != null) {
				sqlQuery = null;
			}
		}

			return "SUCCESS";
		
	} */
	
	
	public String logout() throws SQLException {
		System.out.println("Logout...!!!");
		HttpSession session = Session.getSession();
		
		PreparedStatement pstmt = null;
		try {
			sqlQuery = "UPDATE gs305_usage set sessionClose = ? WHERE sessionID = ?";
			pstmt = connection.prepareStatement(sqlQuery);
			pstmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
			pstmt.setString(2, session.getId());
			
			pstmt.executeUpdate();
			sqlQuery = null;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(pstmt != null) {
				pstmt.close();
				pstmt = null;
			}
			if(sqlQuery != null) {
				sqlQuery = null;
			}
		}
		session.invalidate();
		
		return "LOGOUT";
	}
}