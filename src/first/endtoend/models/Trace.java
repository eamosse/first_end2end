package first.endtoend.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.api.sqlitehelper.ClassPersistable;
import com.api.sqlitehelper.FieldPersistable;
import com.api.sqlitehelper.FieldPersistable.Type;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@ClassPersistable
public class Trace implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@FieldPersistable(value=Type.PRIMARY_KEY, autoIncrement=true)
	private int traceId;

	@Expose
	@FieldPersistable(value=Type.FOREIGN_KEY, nullable=true)
	private Beneficiary beneficiary;

	
	@Expose
	@SerializedName("latitude")
	@FieldPersistable
	private double latitude;

	@Expose
	@SerializedName("longitude")
	@FieldPersistable
	private double longitude; 

	@Expose
	@SerializedName("precision")
	@FieldPersistable
	private float accuracy;

	@Expose
	@SerializedName("dateOp")
	@FieldPersistable
	private Date date;

	@Expose
	@SerializedName("pathPhoto")
	@FieldPersistable(nullable=true)
	private String photo;

	@Expose
	@FieldPersistable
	private int agentId;

	@FieldPersistable
	private int sync;

	@Expose
	private List<TraceDetail> traceDetails;


	public Trace(){
		this.traceDetails = new ArrayList<TraceDetail>();
		this.sync = 0;
	}

	/**
	 * Constructor for operation "Retrieve aid"
	 * @param beneficiarySelected
	 * @param latitude
	 * @param longitude
	 * @param accuracy
	 * @param rationCard
	 * @param date
	 * @param photo
	 * @param agentId
	 */
	public Trace(Beneficiary beneficiary,double latitude, double longitude, float accuracy,
			Date date, String photo, int agentId) {
		this.beneficiary = beneficiary;
		this.latitude = latitude;
		this.longitude = longitude;
		this.accuracy = accuracy;
		this.date = date;
		this.photo = photo;
		this.agentId = agentId;
		this.traceDetails = new ArrayList<TraceDetail>();
		this.sync=0;
	}





	/**
	 * @return the photo
	 */
	public String getPhoto() {
		return photo;
	}

	/**
	 * @param photo the photo to set
	 */
	public void setPhoto(String photo) {
		this.photo = photo;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the accuracy
	 */
	public float getAccuracy() {
		return accuracy;
	}

	/**
	 * @param accuracy the accuracy to set
	 */
	public void setAccuracy(float accuracy) {
		this.accuracy = accuracy;
	}


	public long getAgentId() {
		return agentId;
	}

	public void setAgentId(int agentId) {
		this.agentId = agentId;
	}

	/**
	 * @return the transactionId
	 */
	public int getTraceId() {
		return traceId;
	}

	/**
	 * @param transactionId the transactionId to set
	 */
	public void setTraceId(int traceId) {
		this.traceId = traceId;
	}


	/**
	 * @return the beneficiarySelected
	 */
	public Beneficiary getBeneficiary() {
		return beneficiary;
	}


	/**
	 * @param beneficiarySelected the beneficiarySelected to set
	 */
	public void setBeneficiary(Beneficiary beneficiary) {
		this.beneficiary = beneficiary;
	}


	/**
	 * @return the sync
	 */
	public int getSync() {
		return sync;
	}


	/**
	 * @param sync the sync to set
	 */
	public void setSync(int sync) {
		this.sync = sync;
	}


	/**
	 * @param traceDetails
	 */
	public void setTraceDetails(List<TraceDetail> traceDetails) {
		this.traceDetails = traceDetails;
	}

	/**
	 * @return the details of the trace
	 */
	public List<TraceDetail> getTraceDetails() {
		return traceDetails;
	}

}
