package first.endtoend.models;

import java.io.Serializable;

import com.api.sqlitehelper.ClassPersistable;
import com.api.sqlitehelper.FieldPersistable;
import com.api.sqlitehelper.FieldPersistable.Type;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@ClassPersistable
public class TraceDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Expose
	@SerializedName("id")
	@FieldPersistable(value=Type.PRIMARY_KEY, autoIncrement=true)
	private int traceDetailId;
	
	@FieldPersistable(value=Type.FOREIGN_KEY)
	private Trace trace;
	
	@Expose
	@FieldPersistable(value=Type.FOREIGN_KEY)
	private Product product;
	
	@Expose
	@FieldPersistable
	private float quantityRecovered;
	
	public TraceDetail() {
	}
	
	public TraceDetail(Trace trace, Product product, float quantityRecovered) {
		this.trace = trace;
		this.product = product;
		this.quantityRecovered = quantityRecovered;
	}

	/**
	 * @return the traceDetailId
	 */
	public int getTraceDetailId() {
		return traceDetailId;
	}

	/**
	 * @param traceDetailId the traceDetailId to set
	 */
	public void setTraceDetailId(int traceDetailId) {
		this.traceDetailId = traceDetailId;
	}

	/**
	 * @return the trace
	 */
	public Trace getTrace() {
		return trace;
	}

	/**
	 * @param trace the trace to set
	 */
	public void setTrace(Trace trace) {
		this.trace = trace;
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
	 * @return the quantityRecovered
	 */
	public float getQuantityRecovered() {
		return quantityRecovered;
	}

	/**
	 * @param quantityRecovered the quantityRecovered to set
	 */
	public void setQuantityRecovered(float quantityRecovered) {
		this.quantityRecovered = quantityRecovered;
	}
	
	
	
	

}
