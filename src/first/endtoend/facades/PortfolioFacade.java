package first.endtoend.facades;

import android.content.Context;
import first.endtoend.models.Portfolio;
import first.endtoend.sqliteHelpers.AbstractFacade;

public class PortfolioFacade extends AbstractFacade<Portfolio> {

	public PortfolioFacade(Context context){
		super(Portfolio.class, context);
	}
	
}
