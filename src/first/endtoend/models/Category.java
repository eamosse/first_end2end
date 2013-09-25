package first.endtoend.models;

import java.io.Serializable;

import com.api.sqlitehelper.ClassPersistable;
import com.api.sqlitehelper.FieldPersistable;
import com.api.sqlitehelper.FieldPersistable.Type;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@ClassPersistable
public class Category implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Expose (serialize=false)
	@SerializedName("id")
	@FieldPersistable(value=Type.PRIMARY_KEY)
	private int categoryId;
	
	@Expose (serialize=false)
	@SerializedName("name")
	@FieldPersistable
	private String categoryName;
	
	
	public Category() {
	}
	
	public Category(int categoryId, String categoryName) {
		this.categoryId = categoryId;
		this.categoryName = categoryName;
	}

	/**
	 * @return the categoryId
	 */
	public int getCategoryId() {
		return categoryId;
	}

	/**
	 * @param categoryId the categoryId to set
	 */
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	/**
	 * @return the categoryName
	 */
	public String getCategoryName() {
		return categoryName;
	}

	/**
	 * @param categoryName the categoryName to set
	 */
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

}
