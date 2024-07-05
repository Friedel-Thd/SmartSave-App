import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.util.Log
import com.example.smartsave.dataClasses.Kategorie
import com.example.smartsave.dataClasses.Konto
import com.example.smartsave.dataClasses.Sparziel
import com.example.smartsave.dataClasses.Umsatz
import java.text.SimpleDateFormat
import java.util.Date

class DbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {


    override fun onCreate(db: SQLiteDatabase) {
        Log.d("FeedReaderDbHelper", "onCreate called")
        db.execSQL(SQL_CREATE_KONTO_ENTRIES)
        db.execSQL(SQL_CREATE_UMSATZ_ENTRIES)
        db.execSQL(SQL_CREATE_EINZELUMSATZ_ENTRIES)
        db.execSQL(SQL_CREATE_KATEGORIE_ENTRIES)
        db.execSQL(SQL_CREATE_KATEGORIEZUWEISUNG_ENTRIES)
        db.execSQL(SQL_CREATE_EINZELUMSATZZUWEISUNG_ENTRIES)
        db.execSQL(SQL_CREATE_SPARZIEL_ENTRIES)
        db.execSQL(SQL_CREATE_SPARZIELZUWEISUNG_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.d("SmatSaveDbHelper", "onUpgrade called")
        db.execSQL(SQL_DELETE_KONTO_ENTRIES)
        db.execSQL(SQL_DELETE_UMSATZ_ENTRIES)
        db.execSQL(SQL_DELETE_EINZELUMSATZ_ENTRIES)
        db.execSQL(SQL_DELETE_KATEGORIE_ENTRIES)
        db.execSQL(SQL_DELETE_KATEGORIEZUWEISUNG_ENTRIES)
        db.execSQL(SQL_DELETE_EINZELUMSATZZUWEISUNG_ENTRIES)
        db.execSQL(SQL_DELETE_SPARZIEL_ENTRIES)
        db.execSQL(SQL_DELETE_SPARZIELZUWEISUNG_ENTRIES)
        onCreate(db)
    }
    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "SmartSave.db"
    }

    object SmartSaveContract {
        object KontoEntry : BaseColumns {
            const val TABLE_NAME = "konto"
            const val KONTONUMMER = "kontonr"
            const val BLZ = "blz"
            const val BIC = "bic"
            const val IBAN = "iban"
            const val BEMERKUNG = "bemerkung"
            const val KONTOART = "kontoart"
        }

        object UmsatzEntry : BaseColumns {
            const val TABLE_NAME = "umsatz"
            const val UMSATZ_ID = "umsatzID"
            const val KONTONUMMER = "kontonr"
            const val DATUM = "datum"
            const val VERWENDUNGSZWECK = "verwendungszweck"
            const val BETRAG = "betrag"
        }

        object EinzelumsatzEntry : BaseColumns {
            const val TABLE_NAME = "einzelumsatz"
            const val EINZELUMSATZ_ID = "einzelumsatzID"
            const val VERWENDUNGSZWECK = "verwendungszweck"
            const val BETRAG = "betrag"
            const val DATUM = "datum"
        }

        object KategorieEntry : BaseColumns {
            const val TABLE_NAME = "kategorie"
            const val KATEGORIE_ID = "kategorieID"
            const val NAME = "name"
        }

        object KategorieZuweisungEntry : BaseColumns {
            const val TABLE_NAME = "kategoriezuweisung"
            const val KATEGORIE_ID = "kategorieID"
            const val UMSATZ_ID = "umsatzID"
            const val IS_EINZELUMSATZ = "isEinzelumsatz"
        }

        object EinzelumsatzZuweisungEntry : BaseColumns {
            const val TABLE_NAME = "einzelumsatzzuweisung"
            const val UMSATZ_ID = "umsatzID"
            const val EINZELUMSATZ_ID = "einzelumsatzID"
        }

        object SparzielEntry : BaseColumns {
            const val TABLE_NAME = "sparziel"
            const val SPARZIEL_ID = "sparzielID"
            const val NAME = "name"
            const val BETRAG = "betrag"
            const val ZIELDATUM = "zieldatum"
            const val MONATSRATE = "monatsrate"
        }

        object SparzielZuweisungEntry : BaseColumns {
            const val TABLE_NAME = "sparzielzuweisung"
            const val KONTONUMMER = "kontonr"
            const val ZIELKONTO = "zielkonto"
            const val AUSZAHLKONTO = "auszahlkonto"
        }
    }

    private val SQL_CREATE_KONTO_ENTRIES =
        "CREATE TABLE ${SmartSaveContract.KontoEntry.TABLE_NAME} (" +
                "${SmartSaveContract.KontoEntry.KONTONUMMER} INTEGER PRIMARY KEY," +
                "${SmartSaveContract.KontoEntry.BLZ} TEXT," +
                "${SmartSaveContract.KontoEntry.BIC} TEXT," +
                "${SmartSaveContract.KontoEntry.IBAN} TEXT," +
                "${SmartSaveContract.KontoEntry.BEMERKUNG} TEXT," +
                "${SmartSaveContract.KontoEntry.KONTOART} TEXT)"

    private val SQL_CREATE_UMSATZ_ENTRIES =
        "CREATE TABLE ${SmartSaveContract.UmsatzEntry.TABLE_NAME} (" +
                "${SmartSaveContract.UmsatzEntry.UMSATZ_ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${SmartSaveContract.UmsatzEntry.KONTONUMMER} INTEGER," +
                "${SmartSaveContract.UmsatzEntry.DATUM} TEXT," +
                "${SmartSaveContract.UmsatzEntry.VERWENDUNGSZWECK} TEXT," +
                "${SmartSaveContract.UmsatzEntry.BETRAG} REAL," +
                "FOREIGN KEY(${SmartSaveContract.UmsatzEntry.KONTONUMMER}) REFERENCES ${SmartSaveContract.KontoEntry.TABLE_NAME}(${SmartSaveContract.KontoEntry.KONTONUMMER}))"

    private val SQL_CREATE_EINZELUMSATZ_ENTRIES =
        "CREATE TABLE ${SmartSaveContract.EinzelumsatzEntry.TABLE_NAME} (" +
                "${SmartSaveContract.EinzelumsatzEntry.EINZELUMSATZ_ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${SmartSaveContract.EinzelumsatzEntry.VERWENDUNGSZWECK} TEXT," +
                "${SmartSaveContract.EinzelumsatzEntry.BETRAG} REAL," +
                "${SmartSaveContract.EinzelumsatzEntry.DATUM} TEXT)"

    private val SQL_CREATE_KATEGORIE_ENTRIES =
        "CREATE TABLE ${SmartSaveContract.KategorieEntry.TABLE_NAME} (" +
                "${SmartSaveContract.KategorieEntry.KATEGORIE_ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${SmartSaveContract.KategorieEntry.NAME} TEXT)"

    private val SQL_CREATE_KATEGORIEZUWEISUNG_ENTRIES =
        "CREATE TABLE ${SmartSaveContract.KategorieZuweisungEntry.TABLE_NAME} (" +
                "${SmartSaveContract.KategorieZuweisungEntry.KATEGORIE_ID} INTEGER," +
                "${SmartSaveContract.KategorieZuweisungEntry.UMSATZ_ID} INTEGER," +
                "${SmartSaveContract.KategorieZuweisungEntry.IS_EINZELUMSATZ} INTEGER," +
                "FOREIGN KEY(${SmartSaveContract.KategorieZuweisungEntry.KATEGORIE_ID}) REFERENCES ${SmartSaveContract.KategorieEntry.TABLE_NAME}(${SmartSaveContract.KategorieEntry.KATEGORIE_ID}))"

    private val SQL_CREATE_EINZELUMSATZZUWEISUNG_ENTRIES =
        "CREATE TABLE ${SmartSaveContract.EinzelumsatzZuweisungEntry.TABLE_NAME} (" +
                "${SmartSaveContract.EinzelumsatzZuweisungEntry.UMSATZ_ID} INTEGER," +
                "${SmartSaveContract.EinzelumsatzZuweisungEntry.EINZELUMSATZ_ID} INTEGER," +
                "FOREIGN KEY(${SmartSaveContract.EinzelumsatzZuweisungEntry.UMSATZ_ID}) REFERENCES ${SmartSaveContract.UmsatzEntry.TABLE_NAME}(${SmartSaveContract.UmsatzEntry.UMSATZ_ID})," +
                "FOREIGN KEY(${SmartSaveContract.EinzelumsatzZuweisungEntry.EINZELUMSATZ_ID}) REFERENCES ${SmartSaveContract.EinzelumsatzEntry.TABLE_NAME}(${SmartSaveContract.EinzelumsatzEntry.EINZELUMSATZ_ID}))"

    private val SQL_CREATE_SPARZIEL_ENTRIES =
        "CREATE TABLE ${SmartSaveContract.SparzielEntry.TABLE_NAME} (" +
                "${SmartSaveContract.SparzielEntry.SPARZIEL_ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${SmartSaveContract.SparzielEntry.NAME} TEXT," +
                "${SmartSaveContract.SparzielEntry.BETRAG} REAL," +
                "${SmartSaveContract.SparzielEntry.ZIELDATUM} TEXT," +
                "${SmartSaveContract.SparzielEntry.MONATSRATE} REAL)"

    private val SQL_CREATE_SPARZIELZUWEISUNG_ENTRIES =
        "CREATE TABLE ${SmartSaveContract.SparzielZuweisungEntry.TABLE_NAME} (" +
                "${SmartSaveContract.SparzielZuweisungEntry.KONTONUMMER} INTEGER," +
                "${SmartSaveContract.SparzielZuweisungEntry.ZIELKONTO} INTEGER," +
                "${SmartSaveContract.SparzielZuweisungEntry.AUSZAHLKONTO} INTEGER," +
                "FOREIGN KEY(${SmartSaveContract.SparzielZuweisungEntry.KONTONUMMER}) REFERENCES ${SmartSaveContract.KontoEntry.TABLE_NAME}(${SmartSaveContract.KontoEntry.KONTONUMMER}))"

    private val SQL_DELETE_KONTO_ENTRIES = "DROP TABLE IF EXISTS ${SmartSaveContract.KontoEntry.TABLE_NAME}"
    private val SQL_DELETE_UMSATZ_ENTRIES = "DROP TABLE IF EXISTS ${SmartSaveContract.UmsatzEntry.TABLE_NAME}"
    private val SQL_DELETE_EINZELUMSATZ_ENTRIES = "DROP TABLE IF EXISTS ${SmartSaveContract.EinzelumsatzEntry.TABLE_NAME}"
    private val SQL_DELETE_KATEGORIE_ENTRIES = "DROP TABLE IF EXISTS ${SmartSaveContract.KategorieEntry.TABLE_NAME}"
    private val SQL_DELETE_KATEGORIEZUWEISUNG_ENTRIES = "DROP TABLE IF EXISTS ${SmartSaveContract.KategorieZuweisungEntry.TABLE_NAME}"
    private val SQL_DELETE_EINZELUMSATZZUWEISUNG_ENTRIES = "DROP TABLE IF EXISTS ${SmartSaveContract.EinzelumsatzZuweisungEntry.TABLE_NAME}"
    private val SQL_DELETE_SPARZIEL_ENTRIES = "DROP TABLE IF EXISTS ${SmartSaveContract.SparzielEntry.TABLE_NAME}"
    private val SQL_DELETE_SPARZIELZUWEISUNG_ENTRIES = "DROP TABLE IF EXISTS ${SmartSaveContract.SparzielZuweisungEntry.TABLE_NAME}"

    fun insertKonto(konto: Konto) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(SmartSaveContract.KontoEntry.KONTONUMMER, konto.kontonr)
            put(SmartSaveContract.KontoEntry.BLZ, konto.blz)
            put(SmartSaveContract.KontoEntry.BIC, konto.bic)
            put(SmartSaveContract.KontoEntry.IBAN, konto.iban)
            put(SmartSaveContract.KontoEntry.BEMERKUNG, konto.bemerkung)
            put(SmartSaveContract.KontoEntry.KONTOART, konto.kontoart)
        }
        db.insert(SmartSaveContract.KontoEntry.TABLE_NAME, null, values)
    }

    fun getBankkonto(): Konto? {
        var konto: Konto? = null
        val db = readableDatabase
        val query = "SELECT * FROM ${SmartSaveContract.KontoEntry.TABLE_NAME} WHERE ${SmartSaveContract.KontoEntry.KONTOART} = 'Bankkonto'"
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            konto = Konto(
                cursor.getInt(cursor.getColumnIndexOrThrow(SmartSaveContract.KontoEntry.KONTONUMMER)),
                cursor.getString(cursor.getColumnIndexOrThrow(SmartSaveContract.KontoEntry.BLZ)),
                cursor.getString(cursor.getColumnIndexOrThrow(SmartSaveContract.KontoEntry.BIC)),
                cursor.getString(cursor.getColumnIndexOrThrow(SmartSaveContract.KontoEntry.IBAN)),
                cursor.getString(cursor.getColumnIndexOrThrow(SmartSaveContract.KontoEntry.BEMERKUNG)),
                cursor.getString(cursor.getColumnIndexOrThrow(SmartSaveContract.KontoEntry.KONTOART))
            )
            konto.umsatzLst = loadUmsaetzeForKonto(konto.kontonr)
        }
        cursor.close()
        return konto
    }

    fun getKategorienListe(): List<Kategorie> {
        val katList = mutableListOf<Kategorie>()
        val db = readableDatabase
        val query = "SELECT * FROM ${SmartSaveContract.KategorieEntry.TABLE_NAME}"
        val cursor = db.rawQuery(query, null)
        while (cursor.moveToFirst()) {
            val kat = Kategorie(
                cursor.getString(cursor.getColumnIndexOrThrow(SmartSaveContract.KategorieEntry.NAME))
            )
            katList.add(kat)
        }
        cursor.close()
        return katList
    }

    fun insertKategorie(kat: Kategorie){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(SmartSaveContract.KategorieEntry.NAME, kat.name)
        }
        db.insert(SmartSaveContract.KategorieEntry.TABLE_NAME, null, values)
    }
    fun removeKategorie(kat: Kategorie) {
        val db = writableDatabase
        db.delete(
            SmartSaveContract.KategorieEntry.TABLE_NAME,
            "${SmartSaveContract.KategorieEntry.NAME} = ?",
            arrayOf(kat.name)
        )
    }


    private fun loadUmsaetzeForKonto(kontonummer: Int): List<Umsatz> {
        val umsaetze = mutableListOf<Umsatz>()
        val db = readableDatabase
        val query = "SELECT * FROM ${SmartSaveContract.UmsatzEntry.TABLE_NAME} WHERE ${SmartSaveContract.UmsatzEntry.KONTONUMMER} = $kontonummer"
        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()) {
            val umsatz = Umsatz(
                cursor.getString(cursor.getColumnIndexOrThrow(SmartSaveContract.UmsatzEntry.VERWENDUNGSZWECK)),
                cursor.getDouble(cursor.getColumnIndexOrThrow(SmartSaveContract.UmsatzEntry.BETRAG)),
                parseDate(cursor.getString(cursor.getColumnIndexOrThrow(SmartSaveContract.UmsatzEntry.DATUM)))
            )
            umsatz.setId(cursor.getInt(cursor.getColumnIndexOrThrow(SmartSaveContract.UmsatzEntry.UMSATZ_ID)))
            umsaetze.add(umsatz)
            //TODO Unterumsätze holen
        }
        cursor.close()
        return umsaetze
    }

    fun getKreditKontenListe(): List<Konto> {
        val kreditKontenListe: MutableList<Konto> = mutableListOf()
        val db = readableDatabase
        val query = "SELECT * FROM ${SmartSaveContract.KontoEntry.TABLE_NAME} WHERE ${SmartSaveContract.KontoEntry.KONTOART} = 'Kreditkartenkonto'"
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val konto = Konto(
                cursor.getInt(cursor.getColumnIndexOrThrow(SmartSaveContract.KontoEntry.KONTONUMMER)),
                cursor.getString(cursor.getColumnIndexOrThrow(SmartSaveContract.KontoEntry.BLZ)),
                cursor.getString(cursor.getColumnIndexOrThrow(SmartSaveContract.KontoEntry.BIC)),
                cursor.getString(cursor.getColumnIndexOrThrow(SmartSaveContract.KontoEntry.IBAN)),
                cursor.getString(cursor.getColumnIndexOrThrow(SmartSaveContract.KontoEntry.BEMERKUNG)),
                cursor.getString(cursor.getColumnIndexOrThrow(SmartSaveContract.KontoEntry.KONTOART))
            )
            konto.umsatzLst = loadUmsaetzeForKonto(konto.kontonr)
            kreditKontenListe.add(konto)
        }

        cursor.close()
        return kreditKontenListe
    }

    fun getAllKonten(): List<Konto> {
        val kontoListe: MutableList<Konto> = mutableListOf()
        val db = readableDatabase
        val query = "SELECT * FROM ${SmartSaveContract.KontoEntry.TABLE_NAME}"
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val konto = Konto(
                cursor.getInt(cursor.getColumnIndexOrThrow(SmartSaveContract.KontoEntry.KONTONUMMER)),
                cursor.getString(cursor.getColumnIndexOrThrow(SmartSaveContract.KontoEntry.BLZ)),
                cursor.getString(cursor.getColumnIndexOrThrow(SmartSaveContract.KontoEntry.BIC)),
                cursor.getString(cursor.getColumnIndexOrThrow(SmartSaveContract.KontoEntry.IBAN)),
                cursor.getString(cursor.getColumnIndexOrThrow(SmartSaveContract.KontoEntry.BEMERKUNG)),
                cursor.getString(cursor.getColumnIndexOrThrow(SmartSaveContract.KontoEntry.KONTOART))
            )
            konto.umsatzLst = loadUmsaetzeForKonto(konto.kontonr)
            kontoListe.add(konto)
        }

        cursor.close()
        return kontoListe
    }

    fun getSparzielListe(): List<Sparziel> {
        val sparzielListe: MutableList<Sparziel> = mutableListOf()
        val db = readableDatabase
        val query = "SELECT * FROM ${SmartSaveContract.SparzielEntry.TABLE_NAME}"
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val sparzielID = cursor.getInt(cursor.getColumnIndexOrThrow(SmartSaveContract.SparzielEntry.SPARZIEL_ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(SmartSaveContract.SparzielEntry.NAME))
            val betrag = cursor.getDouble(cursor.getColumnIndexOrThrow(SmartSaveContract.SparzielEntry.BETRAG))
            val zieldatum = parseDate(cursor.getString(cursor.getColumnIndexOrThrow(SmartSaveContract.SparzielEntry.ZIELDATUM)))
            val monatsrate = cursor.getDouble(cursor.getColumnIndexOrThrow(SmartSaveContract.SparzielEntry.MONATSRATE))

            val kontenQuery = "SELECT * FROM ${SmartSaveContract.SparzielZuweisungEntry.TABLE_NAME} WHERE ${SmartSaveContract.SparzielZuweisungEntry.KONTONUMMER} = $sparzielID"
            val kontenCursor = db.rawQuery(kontenQuery, null)
            var zielkonto: Konto? = null
            var auszahlkonto: Konto? = null

            if (kontenCursor.moveToFirst()) {
                val zielkontoNr = kontenCursor.getInt(kontenCursor.getColumnIndexOrThrow(SmartSaveContract.SparzielZuweisungEntry.ZIELKONTO))
                val auszahlkontoNr = kontenCursor.getInt(kontenCursor.getColumnIndexOrThrow(SmartSaveContract.SparzielZuweisungEntry.AUSZAHLKONTO))

                zielkonto = getKontoByKontonummer(zielkontoNr)
                auszahlkonto = getKontoByKontonummer(auszahlkontoNr)
            }
            kontenCursor.close()

            if (zielkonto != null && auszahlkonto != null) {
                val sparziel = Sparziel(name, betrag, zieldatum, monatsrate, zielkonto, auszahlkonto)
                sparzielListe.add(sparziel)
            }
        }

        cursor.close()
        return sparzielListe
    }

    private fun getKontoByKontonummer(kontonummer: Int): Konto? {
        val db = readableDatabase
        val query = "SELECT * FROM ${SmartSaveContract.KontoEntry.TABLE_NAME} WHERE ${SmartSaveContract.KontoEntry.KONTONUMMER} = $kontonummer"
        val cursor = db.rawQuery(query, null)
        var konto: Konto? = null

        if (cursor.moveToFirst()) {
            konto = Konto(
                cursor.getInt(cursor.getColumnIndexOrThrow(SmartSaveContract.KontoEntry.KONTONUMMER)),
                cursor.getString(cursor.getColumnIndexOrThrow(SmartSaveContract.KontoEntry.BLZ)),
                cursor.getString(cursor.getColumnIndexOrThrow(SmartSaveContract.KontoEntry.BIC)),
                cursor.getString(cursor.getColumnIndexOrThrow(SmartSaveContract.KontoEntry.IBAN)),
                cursor.getString(cursor.getColumnIndexOrThrow(SmartSaveContract.KontoEntry.BEMERKUNG)),
                cursor.getString(cursor.getColumnIndexOrThrow(SmartSaveContract.KontoEntry.KONTOART))
            )
            konto.umsatzLst = loadUmsaetzeForKonto(konto.kontonr)
        }

        cursor.close()
        return konto
    }

    fun parseDate(datumString: String): Date {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        return dateFormat.parse(datumString) ?: Date() // Falls parsen fehlschlägt, gib ein Standarddatum zurück
    }

}
