package first.endtoend.models;

import java.io.Serializable;
import java.util.Date;

import com.api.sqlitehelper.ClassPersistable;
import com.api.sqlitehelper.FieldPersistable;
import com.api.sqlitehelper.FieldPersistable.Type;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@ClassPersistable
public class Aid implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Expose
	@SerializedName("id")
	@FieldPersistable(value=Type.PRIMARY_KEY)
	private int aidId;
	
	@Expose
	@SerializedName("aidStartDate")
	@FieldPersistable
	private Date startDate;
	
	@Expose
	@SerializedName("aidEndDate")
	@FieldPersistable
	private Date endDate;
	
	@Expose
	@FieldPersistable(value=Type.FOREIGN_KEY)
	@SerializedName("aidCategory")
	private Category category;
	
	@Expose
	@FieldPersistable
	private int status;
	
	public Aid() {
	}

	/**
	 * constructor with params
	 * @param aidId
	 * @param startDate
	 * @param endDate
	 * @param product
	 * @param category
	 * @param status
	 */
	public Aid(int aidId, Date startDate, Date endDate, Category category, int status) {
		this.aidId = aidId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.category = category;
		this.status = status;
	}


	/**
	 * @return the aidId
	 */
	public int getAidId() {
		return aidId;
	}


	/**
	 * @param aidId the aidId to set
	 */
	public void setAidId(int aidId) {
		this.aidId = aidId;
	}


	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}


	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}


	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}
	
	


	/**
	 * @return the category
	 */
	public Category getCategory() {
		return category;
	}


	/**
	 * @param category the category to set
	 */
	public void setCategory(Category category) {
		this.category = category;
	}


	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	
	
}
