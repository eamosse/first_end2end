package first.endtoend.models;

import java.io.Serializable;

import com.api.sqlitehelper.ClassPersistable;
import com.api.sqlitehelper.FieldPersistable;
import com.api.sqlitehelper.FieldPersistable.Type;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@ClassPersistable
public class RationCard implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@FieldPersistable(value=Type.PRIMARY_KEY)
	@Expose (serialize=false)
	@SerializedName("id")
	private int rationCardId;
	
	@FieldPersistable
	@Expose
	@SerializedName("tagId")
	private String tagId;
	
	@Expose
	@FieldPersistable(value=Type.FOREIGN_KEY)
	private Family family;
		

	public RationCard() {
	}

	public RationCard(int rationCardId,String tagId, Family family) {
		this.rationCardId = rationCardId;
		this.tagId = tagId;
		this.family = family;
	}

	/**
	 * @return the rationCardId
	 */
	public int getRationCardId() {
		return rationCardId;
	}

	/**
	 * @param rationCardId the rationCardId to set
	 */
	public void setRationCardId(int rationCardId) {
		this.rationCardId = rationCardId;
	}

	

	/**
	 * @return the tagId
	 */
	public String getTagId() {
		return tagId;
	}

	/**
	 * @param tagId the tagId to set
	 */
	public void setTagId(String tagId) {
		this.tagId = tagId;
	}

	/**
	 * @return the family
	 */
	public Family getFamily() {
		return family;
	}

	/**
	 * @param family the family to set
	 */
	public void setFamily(Family family) {
		this.family = family;
	}
	
	
	
	
	

}
