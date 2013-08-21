package first.endtoend.facades;

import android.content.Context;
import first.endtoend.models.PortfolioDetail;
import first.endtoend.sqliteHelpers.AbstractFacade;

public class PortfolioDetailFacade extends AbstractFacade<PortfolioDetail> {

	public PortfolioDetailFacade(Context context){
		super(PortfolioDetail.class, context);
	}

	
}
