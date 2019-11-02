package edu.uic.ids.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import edu.uic.ids.model.DatabaseBean;
import edu.uic.ids.util.StringConstants;
@SessionScoped
@ManagedBean (name = StringConstants.MAIN)
public class Main {
	private DatabaseBean databaseBean;
	private String message;
	private List<String> schemaList;
	private String userSchema;
	private String schema;
	private boolean renderSchema;
	
	public Main() {
		renderSchema = false;
		schemaList = new ArrayList<String>();
		databaseBean = new DatabaseBean();
	}

	@PostConstruct
	public void init() {
		Map<String, Object> m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		databaseBean = (DatabaseBean) m.get(StringConstants.DATABASE_BEAN);
	}

	public String processLogin() {
//		databaseBean = new DatabaseBean();
		String status = databaseBean.connect();
		if (status.equalsIgnoreCase("SUCCESS")) {
			return "SUCCESS";
		} else
		{
			message = databaseBean.getErrorMessage();
			return "FAIL";
		}
	
	}

	public String processLogout() {
		try {
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			databaseBean.setDbSchema("");
			databaseBean.setTableList(new ArrayList<String>());
			databaseBean.setTableName("");
			databaseBean.setPassword("");
			databaseBean.setUserName("");
			databaseBean.setDbHost("");
			ec.invalidateSession();
			ec.redirect("login.jsp");
			return "LOGOUT";
		} catch (Exception e) {
			message = e.getMessage();
			databaseBean.setMessage(message);
			databaseBean.setMessage(message);
			return StringConstants.FAIL;
		}
	}

	

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<String> getSchemaList() {
		return schemaList;
	}

	public void setSchemaList(List<String> schemaList) {
		this.schemaList = schemaList;
	}

	public String getUserSchema() {
		return userSchema;
	}

	public void setUserSchema(String userSchema) {
		this.userSchema = userSchema;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public boolean isRenderSchema() {
		return renderSchema;
	}

	public void setRenderSchema(boolean renderSchema) {
		this.renderSchema = renderSchema;
	}

	public DatabaseBean getdatabaseBean() {
		return databaseBean;
	}

	public void setdatabaseBean(DatabaseBean databaseBean) {
		this.databaseBean = databaseBean;
	}

}