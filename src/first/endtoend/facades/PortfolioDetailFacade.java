package first.endtoend.facades;

import android.content.Context;

import com.api.sqlitehelper.AbstractFacade;

import first.endtoend.models.PortfolioDetail;

public class PortfolioDetailFacade extends AbstractFacade<PortfolioDetail> {

	public PortfolioDetailFacade(Context context){
		super(PortfolioDetail.class, context);
	}

	
}
