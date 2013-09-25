package first.endtoend.facades;

import android.content.Context;

import com.api.sqlitehelper.AbstractFacade;

import first.endtoend.models.Family;

public class FamilyFacade extends AbstractFacade<Family> {

	public FamilyFacade(Context context){
		super(Family.class, context);
	}
	
	

}
