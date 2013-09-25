package first.endtoend.facades;

import android.content.Context;

import com.api.sqlitehelper.AbstractFacade;

import first.endtoend.models.Aid;

public class AidFacade extends AbstractFacade<Aid> {

	public AidFacade(Context context) {
		super(Aid.class, context);
	}
	
	

}
