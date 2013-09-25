package first.endtoend.dal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import com.api.sqlitehelper.*;

import first.endtoend.models.Address;
import first.endtoend.models.Aid;
import first.endtoend.models.Beneficiary;
import first.endtoend.models.Category;
import first.endtoend.models.Family;
import first.endtoend.models.Portfolio;
import first.endtoend.models.PortfolioDetail;
import first.endtoend.models.Product;
import first.endtoend.models.RationCard;
import first.endtoend.models.Trace;
import first.endtoend.models.TraceDetail;

public class EndToEndSqliteHelper extends SQliteHelper {
	String dbName = "endtoendDB";
	int version = 1;
	public EndToEndSqliteHelper(Context context, CursorFactory factory) {
		super(context, factory);
		Utils.dbName = dbName; 
		Utils.dbVersion =version;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(createTable(Category.class));
		db.execSQL(createTable(Product.class));
		db.execSQL(createTable(Aid.class));
		db.execSQL(createTable(Family.class));
		db.execSQL(createTable(Portfolio.class));
		db.execSQL(createTable(PortfolioDetail.class));
		db.execSQL(createTable(Beneficiary.class));
		db.execSQL(createTable(Address.class));
		db.execSQL(createTable(RationCard.class));
		db.execSQL(createTable(Trace.class));
		db.execSQL(createTable(TraceDetail.class));

	}

}
