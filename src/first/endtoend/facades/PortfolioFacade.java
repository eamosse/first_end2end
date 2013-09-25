package first.endtoend.facades;

import android.content.Context;

import com.api.sqlitehelper.AbstractFacade;

import first.endtoend.models.Portfolio;

public class PortfolioFacade extends AbstractFacade<Portfolio> {

	public PortfolioFacade(Context context){
		super(Portfolio.class, context);
	}
	
}
