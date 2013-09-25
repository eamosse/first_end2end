package first.endtoend.facades;

import android.content.Context;
import android.database.Cursor;

import com.api.sqlitehelper.AbstractFacade;

import first.endtoend.models.RationCard;

public class RationCardFacade extends AbstractFacade<RationCard> {

	public RationCardFacade(Context context){
		super(RationCard.class, context);
	}
	
	public RationCard findByTagId(String tagId) throws Exception{
		RationCard rc = null;
		//Opening database
				openDataBase();

				Cursor c = dataBase.query(RationCard.class.getSimpleName(),null, 
						"tagId"+" = ?", new String[]{tagId}, null, null, null);
				if(c.moveToNext())
					rc = convertCursorToEntity(c, RationCard.class);

				//close the cursor
				c.close();

				//close database
				closeDataBase();

		
		return rc;
	}

}