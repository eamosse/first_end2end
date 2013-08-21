package first.endtoend.facades;

import android.content.Context;
import first.endtoend.models.Category;
import first.endtoend.sqliteHelpers.AbstractFacade;

public class CategoryFacade extends AbstractFacade<Category> {

	public CategoryFacade(Context context)
			throws Exception {
		super(Category.class, context);
	}

}
