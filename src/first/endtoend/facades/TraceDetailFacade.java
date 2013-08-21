package first.endtoend.facades;

import android.content.Context;
import first.endtoend.models.TraceDetail;
import first.endtoend.sqliteHelpers.AbstractFacade;

public class TraceDetailFacade extends AbstractFacade<TraceDetail> {

	public TraceDetailFacade(Context context){
		super(TraceDetail.class, context);
	}

}
