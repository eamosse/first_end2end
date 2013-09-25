package first.endtoend.facades;

import android.content.Context;

import com.api.sqlitehelper.AbstractFacade;

import first.endtoend.models.Category;

public class CategoryFacade extends AbstractFacade<Category> {

	public CategoryFacade(Context context) {
		super(Category.class, context);
	}

}
