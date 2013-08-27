package first.endtoend.facades;

import android.content.Context;
import first.endtoend.models.Family;
import first.endtoend.sqliteHelpers.AbstractFacade;

public class FamilyFacade extends AbstractFacade<Family> {

	public FamilyFacade(Context context){
		super(Family.class, context);
	}
	
	

}
