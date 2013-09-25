package first.endtoend.facades;

import android.content.Context;

import com.api.sqlitehelper.AbstractFacade;

import first.endtoend.models.Beneficiary;

public class BeneficiaryFacade extends AbstractFacade<Beneficiary> {

	public BeneficiaryFacade(Context context){
		super(Beneficiary.class, context);
	}

	
}
