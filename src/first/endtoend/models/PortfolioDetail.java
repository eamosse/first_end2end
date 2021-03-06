package first.endtoend.models;

import java.io.Serializable;

import com.api.sqlitehelper.ClassPersistable;
import com.api.sqlitehelper.FieldPersistable;
import com.api.sqlitehelper.FieldPersistable.Type;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@ClassPersistable
public class PortfolioDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Expose
	@SerializedName("id")
	@FieldPersistable(value=Type.PRIMARY_KEY)
	private int portfolioDetailId;
	
	@Expose (serialize=false)
	@SerializedName("product")
	@FieldPersistable(value=Type.FOREIGN_KEY)
	private Product product;
		
	@Expose (serialize=false)
	@SerializedName("aid")
	@FieldPersistable(value=Type.FOREIGN_KEY)
	private Aid aid;
		
	@Expose (serialize=false)
	@SerializedName("quantity")
	@FieldPersistable
	private float quantity;
	
	@Expose (serialize=false)
	@SerializedName("reduction")
	@FieldPersistable
	private int reduction;
	
	@Expose (deserialize=false)
	private float quantityWished; 
	
	@FieldPersistable(value=Type.FOREIGN_KEY)
	private Portfolio portfolio;
	
	
	public PortfolioDetail(int portfolioDetailId, Product product, Aid aid,
			float quantity, int reduction, Portfolio portfolio) {
		this.portfolioDetailId = portfolioDetailId;
		this.product = product;
		this.aid = aid;
		this.quantity = quantity;
		this.reduction = reduction;
		this.portfolio = portfolio;
	}

	public PortfolioDetail() {
	}



	/**
	 * @return the portfolioDetailId
	 */
	public int getPortfolioDetailId() {
		return portfolioDetailId;
	}

	/**
	 * @param portfolioDetailId the portfolioDetailId to set
	 */
	public void setPortfolioDetailId(int portfolioDetailId) {
		this.portfolioDetailId = portfolioDetailId;
	}

	/**
	 * @return the quantity
	 */
	public float getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(float quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return the reduction
	 */
	public int getReduction() {
		return reduction;
	}

	/**
	 * @param reduction the reduction to set
	 */
	public void setReduction(int reduction) {
		this.reduction = reduction;
	}

	/**
	 * @return the product
	 */
	public Product getProduct() {
		return product;
	}

	/**
	 * @param product the product to set
	 */
	public void setProduct(Product product) {
		this.product = product;
	}

	/**
	 * @return the aid
	 */
	public Aid getAid() {
		return aid;
	}

	/**
	 * @param aid the aid to set
	 */
	public void setAid(Aid aid) {
		this.aid = aid;
	}

	/**
	 * @return the quantityWished
	 */
	public float getQuantityWished() {
		return quantityWished;
	}

	/**
	 * @param quantityWished the quantityWished to set
	 */
	public void setQuantityWished(float quantityWished) {
		this.quantityWished = quantityWished;
	}

	
	/**
	 * @return the portfolio
	 */
	public Portfolio getPortfolio() {
		return portfolio;
	}

	/**
	 * @param portfolio the portfolio to set
	 */
	public void setPortfolio(Portfolio portfolio) {
		this.portfolio = portfolio;
	}

}
