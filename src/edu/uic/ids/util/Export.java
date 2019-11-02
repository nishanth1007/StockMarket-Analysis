package edu.uic.ids.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import javax.servlet.jsp.jstl.sql.Result;
import javax.servlet.jsp.jstl.sql.ResultSupport;
import java.sql.Statement;

import com.mysql.jdbc.DatabaseMetaData;

import edu.uic.ids.model.DatabaseBean;

@ManagedBean(name = StringConstants.EXPORT)
@SessionScoped
public class Export {

	DatabaseBean databaseBean;

	private String tableSelected;
	private ResultSet resultSet;
	private boolean renderTablename;
	private ResultSetMetaData resultSetMetaData;
	private Result result;
	private boolean renderButton;
	//private List<String> tableList;
	private DatabaseMetaData metaData;
	private String fileLabel;
	private String fileName;
	private long fileSize;
	private String fileContentType;
	private String message;
	private boolean renderMessage;
	private List<String> tableNames;
	private Statement statement;

	public void init() {
		Map<String, Object> m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		databaseBean = (DatabaseBean) m.get(StringConstants.DATABASE_BEAN);

	}

	public void renderTableList() {
		renderMessage = false;
		if (databaseBean.getTableList().isEmpty()) {
			message = "No tables found in the schema.";
			renderMessage = true;
			renderTablename = false;
		} else
			renderTablename = true;
	}

	public String exportCSV() {
		try {
			resetMessage();
			if (databaseBean.getTableList().isEmpty()) {
				message = "No tables found in the schema.";
				renderMessage = true;
				return "FAIL";
			}
			if (databaseBean.getTableName().isEmpty()) {
				message = "Please select table to export data.";
				renderMessage = true;
				return "FAIL";
			} else {
				FacesContext fc = FacesContext.getCurrentInstance();
				ExternalContext ec = fc.getExternalContext();
				FileOutputStream fos = null;
				String path = fc.getExternalContext().getRealPath("/temp");
				File dir = new File(path);
				if (!dir.exists())
					new File(path).mkdirs();
				ec.setResponseCharacterEncoding("UTF-8");
				String fileNameBase = databaseBean.getUserName() + "_" + databaseBean.getTableName() + ".csv";
				String fileName = path + "/" + databaseBean.getUserName() + "_" + fileNameBase;
				File f = new File(fileName);
				resultSet = null;
				String sqlQuery = "select * from " + databaseBean.getDbSchema() + "." + databaseBean.getTableName();
				resultSet = statement.executeQuery(sqlQuery);
				if (resultSet != null) {
					result = ResultSupport.toResult(resultSet);
					Object[][] sData = result.getRowsByIndex();
					String columnNames[] = result.getColumnNames();
					StringBuffer sb = new StringBuffer();
					try {
						fos = new FileOutputStream(fileName);
						for (int i = 0; i < columnNames.length; i++) {
							sb.append(columnNames[i].toString() + ",");
						}
						sb.append("\n");
						fos.write(sb.toString().getBytes());
						for (int i = 0; i < sData.length; i++) {
							sb = new StringBuffer();
							for (int j = 0; j < sData[0].length; j++) {
								if (sData[i][j] == null) {
									String value2 = "0";
									value2 = value2.replaceAll("[^A-Za-z0-9.]", " . ");
									if (value2.isEmpty()) {
										value2 = "0";
									}
									sb.append(value2 + ",");
								} else {
									String value = sData[i][j].toString();
									if (value.contains(",")) {
										int index = value.indexOf(",");
										String newValue = value.substring(0, index - 1);
										value = newValue + value.substring(index + 1, value.length());
									}
									// value=value.replaceAll("[^A-Za-z0-9,.]",
									// " ");
									if (value.isEmpty()) {
										value = "0";
									}
									sb.append(value + ",");
								}
							}
							sb.append("\n");
							fos.write(sb.toString().getBytes());
						}
						fos.flush();
						fos.close();
					} catch (FileNotFoundException e) {
						message = e.getMessage();
						renderMessage = true;
					} catch (IOException io) {
						message = io.getMessage();
						renderMessage = true;
					}
					String mimeType = ec.getMimeType(fileName);
					FileInputStream in = null;
					byte b;
					ec.responseReset();
					ec.setResponseContentType(mimeType);
					ec.setResponseContentLength((int) f.length());
					ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileNameBase + "\"");
					try {
						in = new FileInputStream(f);
						OutputStream output = ec.getResponseOutputStream();
						while (true) {
							b = (byte) in.read();
							if (b < 0)
								break;
							output.write(b);
						}
					} catch (IOException e) {
						message = e.getMessage();
						renderMessage = true;
					} finally {
						try {
							in.close();
						} catch (IOException e) {
							message = e.getMessage();
							renderMessage = true;
						}
					}
					fc.responseComplete();
				} else {
					message = databaseBean.getMessage();
					renderMessage = true;
				}
			}
			return "SUCCESS";
		} catch (Exception e) {
			message = e.getMessage();
			renderMessage = true;
			return "FAIL";
		}
	}

	
	
	
	
	
	
	


	public void resetMessage() {
		renderMessage = false;
	}

	public String getTableSelected() {
		return tableSelected;
	}

	public void setTableSelected(String tableSelected) {
		this.tableSelected = tableSelected;
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

	public void setResultSetMetaData(ResultSetMetaData resultSetMetaData) {
		this.resultSetMetaData = resultSetMetaData;
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public boolean isRenderButton() {
		return renderButton;
	}

	public void setRenderButton(boolean renderButton) {
		this.renderButton = renderButton;
	}

	/*
	 * public List<String> getTableList() { return tableList; }
	 * 
	 * public void setTableList(List<String> tableList) { this.tableList =
	 * tableList; }
	 */
	public DatabaseMetaData getMetaData() {
		return metaData;
	}

	public void setMetaData(DatabaseMetaData metaData) {
		this.metaData = metaData;
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

	/*
	 * public databaseBean getdatabaseBean() { return databaseBean; }
	 * 
	 * public void setdatabaseBean(databaseBean databaseBean) { this.databaseBean =
	 * databaseBean; }
	 */

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileContentType() {
		return fileContentType;
	}

	public void setFileContentType(String fileContentType) {
		this.fileContentType = fileContentType;
	}

	public String getFileLabel() {
		return fileLabel;
	}

	public void setFileLabel(String fileLabel) {
		this.fileLabel = fileLabel;
	}

	public List<String> getTableNames() {
		return tableNames;
	}

	public void setTableNames(List<String> tableNames) {
		this.tableNames = tableNames;
	}

	public Statement getStatement() {
		return statement;
	}

	public void setStatement(Statement statement) {
		this.statement = statement;
	}

}
