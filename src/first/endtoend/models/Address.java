package first.endtoend.models;

import java.io.Serializable;

import com.api.sqlitehelper.ClassPersistable;
import com.api.sqlitehelper.FieldPersistable;
import com.api.sqlitehelper.FieldPersistable.Type;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@ClassPersistable
public class Address implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Expose(serialize=false)
	@SerializedName("id")
	@FieldPersistable(value=Type.PRIMARY_KEY)
	private int addressId;
	
	@Expose(serialize=false)
	@SerializedName("numStreet")
	@FieldPersistable
	private String street;
	
	
	@Expose(serialize=false)
	@SerializedName("zipCode")
	@FieldPersistable
	private String zipCode;
	
	@Expose(serialize=false)
	@SerializedName("localityName")
	@FieldPersistable
	private String locality;
	
	@FieldPersistable(value=Type.FOREIGN_KEY)
	private Family family;
	
	public Address(String street, String zipCode,String locality,Family family) {
		this.street = street;
		this.zipCode = zipCode;
		this.locality = locality;
		this.family = family;
	}

	public Address() {
	}

	/**
	 * @return the addressId
	 */
	public int getAddressId() {
		return addressId;
	}


	/**
	 * @param addressId the addressId to set
	 */
	public void setAddressId(int addressId) {
		this.addressId = addressId;
	}

	/**
	 * @return the street
	 */
	public String getStreet() {
		return street;
	}


	/**
	 * @param street the street to set
	 */
	public void setStreet(String street) {
		this.street = street;
	}


	/**
	 * @return the zipCode
	 */
	public String getZipCode() {
		return zipCode;
	}


	/**
	 * @param zipCode the zipCode to set
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
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

	/**
	 * @return the locality
	 */
	public String getLocality() {
		return locality;
	}


	/**
	 * @param locality the locality to set
	 */
	public void setLocality(String locality) {
		this.locality = locality;
	}	
	
	
	public String toString(){
		return (this.street + " "+this.getZipCode() +" " + this.locality).replaceAll("\\s+", " ");
	}
	
	
}
