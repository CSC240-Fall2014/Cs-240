

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DaMuzikHelper extends SQLiteOpenHelper{
    // table name
	public static final String TABLE_NAME = "departments";

	// columns in the table
	public static final String _ID = "_id";
	public static final String NAME = "name";
	public static final String ARTIST = "artist";
	public static final String PATHWAY = "pathWay";

	// database version and name
	private static final int DATABASE_VERSION = 2;
	private static final String DATABASE_NAME = "departmentInformation.db";
	
	public DaMuzikHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + _ID + 
				" INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME + 
		" TEXT, " + ARTIST + " TEXT, " + PATHWAY + " TEXT);");
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}
}
