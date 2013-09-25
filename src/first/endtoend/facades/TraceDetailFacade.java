package first.endtoend.facades;

import android.content.Context;

import com.api.sqlitehelper.AbstractFacade;

import first.endtoend.models.TraceDetail;

public class TraceDetailFacade extends AbstractFacade<TraceDetail> {

	public TraceDetailFacade(Context context){
		super(TraceDetail.class, context);
	}

}
