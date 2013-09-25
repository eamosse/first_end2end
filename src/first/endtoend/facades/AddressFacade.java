package first.endtoend.facades;

import com.api.sqlitehelper.AbstractFacade;

import android.content.Context;

import first.endtoend.models.Address;

public class AddressFacade extends AbstractFacade<Address> {

	public AddressFacade(Context context) {
		super(Address.class, context);
	}

	
}
