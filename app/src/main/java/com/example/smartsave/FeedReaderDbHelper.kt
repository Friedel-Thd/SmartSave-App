import FeedReaderDbHelper.FeedReaderContract.FeedEntry.BIC
import FeedReaderDbHelper.FeedReaderContract.FeedEntry.BLZ
import FeedReaderDbHelper.FeedReaderContract.FeedEntry.IBAN
import FeedReaderDbHelper.FeedReaderContract.FeedEntry.KONTONUMMER
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.util.Log
import com.example.smartsave.dataClasses.Konto

class FeedReaderDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {


    override fun onCreate(db: SQLiteDatabase) {
        Log.d("FeedReaderDbHelper", "onCreate called")
        db.execSQL(SQL_CREATE_ENTRIES)

    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        Log.d("FeedReaderDbHelper", "onUpgrade called")
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }
    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }
    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "FeedReader.db"
    }
    object FeedReaderContract {
        // Table contents are grouped together in an anonymous object.
        object FeedEntry : BaseColumns {
            const val TABLE_NAME = "entry"
            const val KONTOSTAND = "entry"
            const val KONTONUMMER = "Kontonummer"
            const val BLZ = "BLZ"
            const val BIC = "BIC"
            const val IBAN = "IBAN"
            const val BEMERKUNG = "Bemerkung"
            const val KONTOART = "Kontoart"
        }
    }

    private val SQL_CREATE_ENTRIES =
        "CREATE TABLE ${FeedReaderContract.FeedEntry.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${FeedReaderContract.FeedEntry.KONTOSTAND} TEXT," +
                "${FeedReaderContract.FeedEntry.KONTONUMMER} TEXT," +
                "${FeedReaderContract.FeedEntry.BLZ} TEXT," +
                "${FeedReaderContract.FeedEntry.BIC} TEXT," +
                "${FeedReaderContract.FeedEntry.IBAN} TEXT," +
                "${FeedReaderContract.FeedEntry.BEMERKUNG} TEXT," +
                "${FeedReaderContract.FeedEntry.KONTOART} TEXT)"

    private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${FeedReaderContract.FeedEntry.TABLE_NAME}"

    fun insertKonto(konto: Konto){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(FeedReaderContract.FeedEntry.KONTONUMMER, konto.kontotnr)
            put(FeedReaderContract.FeedEntry.BLZ, konto.BLZ)
            put(FeedReaderContract.FeedEntry.BIC, konto.BIC)
            put(FeedReaderContract.FeedEntry.IBAN, konto.IBAN)
            put(FeedReaderContract.FeedEntry.BEMERKUNG, konto.bemerkung)
            put(FeedReaderContract.FeedEntry.KONTOART,konto.Kontoart)
        }
        db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values)

    }
    fun getBankkonto() : Konto {
        val konto : Konto
        val db = readableDatabase
        val query = "SELECT * FROM ${FeedReaderContract.FeedEntry.TABLE_NAME} WHERE ${FeedReaderContract.FeedEntry.KONTOART} = 'Bankkonto'"
        val cursor = db.rawQuery(query, null)
        while(cursor.moveToNext()){
            val kontonummer = cursor.getInt(cursor.getColumnIndexOrThrow(KONTONUMMER))
            val BLZ = cursor.getInt(cursor.getColumnIndexOrThrow(BLZ))
            val BIC = cursor.getInt(cursor.getColumnIndexOrThrow(BIC))
            val IBAN = cursor.getString(cursor.getColumnIndexOrThrow(IBAN))
            val blz =
            val blz
        }


    }

}
