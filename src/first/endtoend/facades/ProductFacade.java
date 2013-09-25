package first.endtoend.facades;

import android.content.Context;

import com.api.sqlitehelper.AbstractFacade;

import first.endtoend.models.Product;

public class ProductFacade extends AbstractFacade<Product> {

	public ProductFacade(Context context){
		super(Product.class, context);
	}

	
}
