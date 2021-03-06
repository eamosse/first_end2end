package first.endtoend.models;

import java.io.Serializable;

import com.api.sqlitehelper.ClassPersistable;
import com.api.sqlitehelper.FieldPersistable;
import com.api.sqlitehelper.FieldPersistable.Type;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@ClassPersistable
public class Product implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Expose
	@SerializedName("id")
	@FieldPersistable(value=Type.PRIMARY_KEY)
	private int productId;
	
	@Expose (serialize=false)
	@SerializedName("productName")
	@FieldPersistable
	private String name;
	
	@Expose (serialize=false)
	@SerializedName("productUnity")
	@FieldPersistable
	private String unity;
	
	@Expose (serialize=false)
	@SerializedName("productIconURL")
	@FieldPersistable
	private String iconeURL;
	
	@Expose(serialize=false)
	@SerializedName("productDescription")
	@FieldPersistable
	private String description;
	
	@Expose (serialize=false)
	@SerializedName("productPrice")
	@FieldPersistable
	private float price;
	

	public Product(int productId, String name, String unity, String iconeURL,
			String description, float price) {
		super();
		this.productId = productId;
		this.name = name;
		this.unity = unity;
		this.iconeURL = iconeURL;
		this.description = description;
		this.price = price;
	}


	public Product() {
	}


	/**
	 * @return the productId
	 */
	public int getProductId() {
		return productId;
	}
	/**
	 * @param productId the productId to set
	 */
	public void setProductId(int productId) {
		this.productId = productId;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the unity
	 */
	public String getUnity() {
		return unity;
	}
	/**
	 * @param unity the unity to set
	 */
	public void setUnity(String unity) {
		this.unity = unity;
	}
	/**
	 * @return the iconeURL
	 */
	public String getIconeURL() {
		return iconeURL;
	}
	/**
	 * @param iconeURL the iconeURL to set
	 */
	public void setIconeURL(String iconeURL) {
		this.iconeURL = iconeURL;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the price
	 */
	public float getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(float price) {
		this.price = price;
	}
	
}
