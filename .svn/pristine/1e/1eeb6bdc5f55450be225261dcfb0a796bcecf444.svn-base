package first.endtoend.models;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import first.endtoend.sqliteHelpers.ClassPersistable;
import first.endtoend.sqliteHelpers.FieldPersistable;
import first.endtoend.sqliteHelpers.FieldPersistable.Type;

@ClassPersistable
public class Portfolio implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Expose
	@SerializedName("id")
	@FieldPersistable(value=Type.PRIMARY_KEY)
	private int portfolioId;
	
	@Expose(serialize=false)
	private ArrayList<PortfolioDetail> details;	
	//List of product to up to date on the server
	@Expose(deserialize=false)
	@SerializedName("details")
	private ArrayList<PortfolioDetail> productsSelected;
	
	@FieldPersistable(value=Type.FOREIGN_KEY)
	private Family family;


	public Portfolio(int portfolioId, Family family) {
		this.portfolioId = portfolioId;
		this.family = family;
	}

	public Portfolio() {
	}

	/**
	 * @return the portfolioId
	 */
	public int getPortfolioId() {
		return portfolioId;
	}
	/**
	 * @param portfolioId the portfolioId to set
	 */
	public void setPortfolioId(int portfolioId) {
		this.portfolioId = portfolioId;
	}

	/**
	 * @return the details
	 */
	public ArrayList<PortfolioDetail> getDetails() {
		return details;
	}

	/**
	 * @param details the details to set
	 */
	public void setDetails(ArrayList<PortfolioDetail> details) {
		this.details = details;
	}

	/**
	 * @return the productsSelected
	 */
	public ArrayList<PortfolioDetail> getProductsSelected() {
		return productsSelected;
	}

	/**
	 * @param productsSelected the productsSelected to set
	 */
	public void setProductsSelected(ArrayList<PortfolioDetail> productsSelected) {
		this.productsSelected = productsSelected;
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
