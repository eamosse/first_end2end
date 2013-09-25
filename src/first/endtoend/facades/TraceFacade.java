package first.endtoend.facades;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;

import com.api.sqlitehelper.AbstractFacade;

import first.endtoend.models.Trace;

public class TraceFacade extends AbstractFacade<Trace> {

	public TraceFacade(Context context){
		super(Trace.class, context);
	}
	
	public List<Trace> findAllToSync() throws Exception{
		List<Trace> list = new ArrayList<Trace>();

		//Opening database
		openDataBase();

		Cursor c = dataBase.query(Trace.class.getSimpleName(),null, "sync = "+0,null, null, null, null);
		while (c.moveToNext()){
			Trace trace = convertCursorToEntity(c,Trace.class);
			list.add(trace);
		}

		//close the cursor
		c.close();

		//close database
		closeDataBase();

		return list;
	}

	
	

	
}
