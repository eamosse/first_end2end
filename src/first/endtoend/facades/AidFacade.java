package first.endtoend.facades;

import android.content.Context;
import first.endtoend.models.Aid;
import first.endtoend.sqliteHelpers.AbstractFacade;

public class AidFacade extends AbstractFacade<Aid> {

	public AidFacade(Context context)
			throws Exception {
		super(Aid.class, context);
	}
	
	

}
