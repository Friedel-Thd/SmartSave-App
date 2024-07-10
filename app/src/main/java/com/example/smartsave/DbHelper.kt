package com.example.smartsave

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.util.Log
import com.example.smartsave.dataClasses.Einzelumsatz
import com.example.smartsave.dataClasses.Kategorie
import com.example.smartsave.dataClasses.Konto
import com.example.smartsave.dataClasses.Sparziel
import com.example.smartsave.dataClasses.Umsatz
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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
            const val SPARZIEL_ID = "sparzielID"
            const val ZIELKONTO = "zielkonto"
            const val AUSZAHLKONTO = "auszahlkonto"
        }
    }

    private val SQL_CREATE_KONTO_ENTRIES =
        "CREATE TABLE ${SmartSaveContract.KontoEntry.TABLE_NAME} (" +
                "${SmartSaveContract.KontoEntry.KONTONUMMER} LONG PRIMARY KEY," +
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
                "${SmartSaveContract.SparzielZuweisungEntry.SPARZIEL_ID} INTEGER," +
                "${SmartSaveContract.SparzielZuweisungEntry.ZIELKONTO} INTEGER," +
                "${SmartSaveContract.SparzielZuweisungEntry.AUSZAHLKONTO} INTEGER," +
                "FOREIGN KEY(${SmartSaveContract.SparzielZuweisungEntry.SPARZIEL_ID}) REFERENCES ${SmartSaveContract.SparzielEntry.TABLE_NAME}(${SmartSaveContract.SparzielEntry.SPARZIEL_ID})," +
                "FOREIGN KEY(${SmartSaveContract.SparzielZuweisungEntry.ZIELKONTO}) REFERENCES ${SmartSaveContract.KontoEntry.TABLE_NAME}(${SmartSaveContract.KontoEntry.KONTONUMMER})," +
                "FOREIGN KEY(${SmartSaveContract.SparzielZuweisungEntry.AUSZAHLKONTO}) REFERENCES ${SmartSaveContract.KontoEntry.TABLE_NAME}(${SmartSaveContract.KontoEntry.KONTONUMMER}))"

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
                cursor.getLong(cursor.getColumnIndexOrThrow(SmartSaveContract.KontoEntry.KONTONUMMER)),
                cursor.getString(cursor.getColumnIndexOrThrow(SmartSaveContract.KontoEntry.BLZ)),
                cursor.getString(cursor.getColumnIndexOrThrow(SmartSaveContract.KontoEntry.BIC)),
                cursor.getString(cursor.getColumnIndexOrThrow(SmartSaveContract.KontoEntry.IBAN)),
                cursor.getString(cursor.getColumnIndexOrThrow(SmartSaveContract.KontoEntry.BEMERKUNG)),
                cursor.getString(cursor.getColumnIndexOrThrow(SmartSaveContract.KontoEntry.KONTOART))
            )
            konto.umsatzList = loadUmsaetzeForKonto(konto.kontonr)
            konto.kontostand = konto.calcKontostand()
        }
        cursor.close()
        return konto
    }

    fun getKategorienListe(): List<Kategorie> {
        val katList = mutableListOf<Kategorie>()
        val db = readableDatabase
        val query = "SELECT * FROM ${SmartSaveContract.KategorieEntry.TABLE_NAME}"
        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()) {
            val kat = Kategorie(
                cursor.getString(cursor.getColumnIndexOrThrow(SmartSaveContract.KategorieEntry.NAME))
            )
            kat.id = cursor.getInt(cursor.getColumnIndexOrThrow(SmartSaveContract.KategorieEntry.KATEGORIE_ID))
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
    fun insertEinzelumsatz(einzelumsatz: Einzelumsatz): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(SmartSaveContract.EinzelumsatzEntry.BETRAG, einzelumsatz.betrag)
            put(SmartSaveContract.EinzelumsatzEntry.DATUM, einzelumsatz.datum.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
            put(SmartSaveContract.EinzelumsatzEntry.VERWENDUNGSZWECK, einzelumsatz.verwendungsZweck)
        }

        val einzelumsatzId = db.insert(SmartSaveContract.EinzelumsatzEntry.TABLE_NAME, null, values)

        val kategorieZuweisungValues = ContentValues().apply {
            put(SmartSaveContract.KategorieZuweisungEntry.UMSATZ_ID, einzelumsatzId)
            put(SmartSaveContract.KategorieZuweisungEntry.KATEGORIE_ID, einzelumsatz.kategorie.id)
            put(SmartSaveContract.KategorieZuweisungEntry.IS_EINZELUMSATZ, 1)
        }
        db.insert(SmartSaveContract.KategorieZuweisungEntry.TABLE_NAME, null, kategorieZuweisungValues)
        return einzelumsatzId.toInt()
    }

    fun editEinzelumsatz(einzelumsatz: Einzelumsatz) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(SmartSaveContract.EinzelumsatzEntry.BETRAG, einzelumsatz.betrag)
            put(SmartSaveContract.EinzelumsatzEntry.DATUM, einzelumsatz.datum.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
            put(SmartSaveContract.EinzelumsatzEntry.VERWENDUNGSZWECK, einzelumsatz.verwendungsZweck)
        }

        val einzelumsatzId = einzelumsatz.id

        val updateCount = db.update(
            SmartSaveContract.EinzelumsatzEntry.TABLE_NAME,
            values,
            "${SmartSaveContract.EinzelumsatzEntry.EINZELUMSATZ_ID} = ?",
            arrayOf(einzelumsatzId.toString())
        )

        if (updateCount > 0) {
            val kategorieZuweisungValues = ContentValues().apply {
                put(SmartSaveContract.KategorieZuweisungEntry.KATEGORIE_ID, einzelumsatz.kategorie.id)
            }

            val updateKategorieZuweisungCount = db.update(
                SmartSaveContract.KategorieZuweisungEntry.TABLE_NAME,
                kategorieZuweisungValues,
                "${SmartSaveContract.KategorieZuweisungEntry.UMSATZ_ID} = ? AND ${SmartSaveContract.KategorieZuweisungEntry.IS_EINZELUMSATZ} = 1",
                arrayOf(einzelumsatzId.toString())
            )

            if (updateKategorieZuweisungCount == 0) {
                val newKategorieZuweisungValues = ContentValues().apply {
                    put(SmartSaveContract.KategorieZuweisungEntry.UMSATZ_ID, einzelumsatzId)
                    put(SmartSaveContract.KategorieZuweisungEntry.KATEGORIE_ID, einzelumsatz.kategorie.id)
                    put(SmartSaveContract.KategorieZuweisungEntry.IS_EINZELUMSATZ, 1)
                }
                db.insert(SmartSaveContract.KategorieZuweisungEntry.TABLE_NAME, null, newKategorieZuweisungValues)
            }
        }
    }



    private fun loadUmsaetzeForKonto(kontonummer: Long): List<Umsatz> {
        val umsaetze = mutableListOf<Umsatz>()
        val db = readableDatabase

        val query = "SELECT * FROM ${SmartSaveContract.UmsatzEntry.TABLE_NAME} WHERE ${SmartSaveContract.UmsatzEntry.KONTONUMMER} = ?"
        val cursor = db.rawQuery(query, arrayOf(kontonummer.toString()))

        while (cursor.moveToNext()) {
            val umsatz = Umsatz(
                cursor.getString(cursor.getColumnIndexOrThrow(SmartSaveContract.UmsatzEntry.VERWENDUNGSZWECK)),
                cursor.getDouble(cursor.getColumnIndexOrThrow(SmartSaveContract.UmsatzEntry.BETRAG)),
                parseDate(cursor.getString(cursor.getColumnIndexOrThrow(SmartSaveContract.UmsatzEntry.DATUM)))
            )
            umsatz.id = cursor.getInt(cursor.getColumnIndexOrThrow(SmartSaveContract.UmsatzEntry.UMSATZ_ID))

            val kategorieIdQuery = "SELECT ${SmartSaveContract.KategorieZuweisungEntry.KATEGORIE_ID} FROM ${SmartSaveContract.KategorieZuweisungEntry.TABLE_NAME} WHERE ${SmartSaveContract.KategorieZuweisungEntry.UMSATZ_ID} = ?"
            val kategorieIdCursor = db.rawQuery(kategorieIdQuery, arrayOf(umsatz.id.toString()))

            if (kategorieIdCursor.moveToFirst()) {
                val kategorieId = kategorieIdCursor.getInt(kategorieIdCursor.getColumnIndexOrThrow(
                    SmartSaveContract.KategorieZuweisungEntry.KATEGORIE_ID
                ))

                val kategorieQuery = "SELECT * FROM ${SmartSaveContract.KategorieEntry.TABLE_NAME} WHERE ${SmartSaveContract.KategorieEntry.KATEGORIE_ID} = ?"
                val kategorieCursor = db.rawQuery(kategorieQuery, arrayOf(kategorieId.toString()))

                if (kategorieCursor.moveToFirst()) {
                    val kategorie = Kategorie(
                        kategorieCursor.getString(kategorieCursor.getColumnIndexOrThrow(
                            SmartSaveContract.KategorieEntry.NAME
                        ))
                    )
                    kategorie.id = kategorieCursor.getInt(kategorieCursor.getColumnIndexOrThrow(
                        SmartSaveContract.KategorieEntry.KATEGORIE_ID
                    ))
                    umsatz.kategorie = kategorie
                }
                kategorieCursor.close()
            }
            kategorieIdCursor.close()
            umsatz.einzelumsatzListe = loadEinzelumsaetzeForUmsatz(umsatz.id)

            umsaetze.add(umsatz)
        }
        cursor.close()
        return umsaetze
    }

    fun loadEinzelumsaetzeForUmsatz(umsatzID: Int): List<Einzelumsatz> {
        val einzelumsaetze = mutableListOf<Einzelumsatz>()
        val db = readableDatabase

        val query = "SELECT ${SmartSaveContract.EinzelumsatzZuweisungEntry.EINZELUMSATZ_ID} FROM ${SmartSaveContract.EinzelumsatzZuweisungEntry.TABLE_NAME} WHERE ${SmartSaveContract.EinzelumsatzZuweisungEntry.UMSATZ_ID} = ?"
        val cursor = db.rawQuery(query, arrayOf(umsatzID.toString()))

        while (cursor.moveToNext()) {
            val einzelumsatzID = cursor.getInt(cursor.getColumnIndexOrThrow(SmartSaveContract.EinzelumsatzZuweisungEntry.EINZELUMSATZ_ID))

            val einzelumsatzQuery = "SELECT * FROM ${SmartSaveContract.EinzelumsatzEntry.TABLE_NAME} WHERE ${SmartSaveContract.EinzelumsatzEntry.EINZELUMSATZ_ID} = ?"
            val einzelumsatzCursor = db.rawQuery(einzelumsatzQuery, arrayOf(einzelumsatzID.toString()))

            if (einzelumsatzCursor.moveToFirst()) {
                val einzelumsatz = Einzelumsatz(
                    einzelumsatzCursor.getString(einzelumsatzCursor.getColumnIndexOrThrow(
                        SmartSaveContract.EinzelumsatzEntry.VERWENDUNGSZWECK
                    )),
                    einzelumsatzCursor.getDouble(einzelumsatzCursor.getColumnIndexOrThrow(
                        SmartSaveContract.EinzelumsatzEntry.BETRAG
                    )),
                    parseDate(einzelumsatzCursor.getString(einzelumsatzCursor.getColumnIndexOrThrow(
                        SmartSaveContract.EinzelumsatzEntry.DATUM
                    )))
                )
                einzelumsatz.id = einzelumsatzCursor.getInt(einzelumsatzCursor.getColumnIndexOrThrow(
                    SmartSaveContract.EinzelumsatzEntry.EINZELUMSATZ_ID
                ))
                einzelumsatz.hasParentUmsatz = true

                // Kategorie des Einzelumsatzes laden
                val kategorieIdQuery = "SELECT ${SmartSaveContract.KategorieZuweisungEntry.KATEGORIE_ID} FROM ${SmartSaveContract.KategorieZuweisungEntry.TABLE_NAME} WHERE ${SmartSaveContract.KategorieZuweisungEntry.UMSATZ_ID} = ? AND ${SmartSaveContract.KategorieZuweisungEntry.IS_EINZELUMSATZ} = 1"
                val kategorieIdCursor = db.rawQuery(kategorieIdQuery, arrayOf(einzelumsatz.id.toString()))

                if (kategorieIdCursor.moveToFirst()) {
                    val kategorieId = kategorieIdCursor.getInt(kategorieIdCursor.getColumnIndexOrThrow(
                        SmartSaveContract.KategorieZuweisungEntry.KATEGORIE_ID
                    ))

                    val kategorieQuery = "SELECT * FROM ${SmartSaveContract.KategorieEntry.TABLE_NAME} WHERE ${SmartSaveContract.KategorieEntry.KATEGORIE_ID} = ?"
                    val kategorieCursor = db.rawQuery(kategorieQuery, arrayOf(kategorieId.toString()))

                    if (kategorieCursor.moveToFirst()) {
                        val kategorie = Kategorie(
                            kategorieCursor.getString(kategorieCursor.getColumnIndexOrThrow(
                                SmartSaveContract.KategorieEntry.NAME
                            ))
                        )
                        kategorie.id = kategorieCursor.getInt(kategorieCursor.getColumnIndexOrThrow(
                            SmartSaveContract.KategorieEntry.KATEGORIE_ID
                        ))
                        einzelumsatz.kategorie = kategorie
                    }
                    kategorieCursor.close()
                }
                kategorieIdCursor.close()

                einzelumsaetze.add(einzelumsatz)
            }
            einzelumsatzCursor.close()
        }
        cursor.close()
        return einzelumsaetze
    }

    fun getUmsatzByID(umsatzID: Int): Umsatz? {
        val db = readableDatabase
        var umsatz: Umsatz? = null

        val query = "SELECT * FROM ${SmartSaveContract.UmsatzEntry.TABLE_NAME} WHERE ${SmartSaveContract.UmsatzEntry.UMSATZ_ID} = ?"
        val cursor = db.rawQuery(query, arrayOf(umsatzID.toString()))

        if (cursor.moveToFirst()) {
            umsatz = Umsatz(
                cursor.getString(cursor.getColumnIndexOrThrow(SmartSaveContract.UmsatzEntry.VERWENDUNGSZWECK)),
                cursor.getDouble(cursor.getColumnIndexOrThrow(SmartSaveContract.UmsatzEntry.BETRAG)),
                parseDate(cursor.getString(cursor.getColumnIndexOrThrow(SmartSaveContract.UmsatzEntry.DATUM)))
            )
            umsatz.id = cursor.getInt(cursor.getColumnIndexOrThrow(SmartSaveContract.UmsatzEntry.UMSATZ_ID))

            umsatz.einzelumsatzListe = loadEinzelumsaetzeForUmsatz(umsatz.id)

            val kategorieIdQuery = "SELECT ${SmartSaveContract.KategorieZuweisungEntry.KATEGORIE_ID} FROM ${SmartSaveContract.KategorieZuweisungEntry.TABLE_NAME} WHERE ${SmartSaveContract.KategorieZuweisungEntry.UMSATZ_ID} = ?"
            val kategorieIdCursor = db.rawQuery(kategorieIdQuery, arrayOf(umsatz.id.toString()))

            if (kategorieIdCursor.moveToFirst()) {
                val kategorieId = kategorieIdCursor.getInt(kategorieIdCursor.getColumnIndexOrThrow(
                    SmartSaveContract.KategorieZuweisungEntry.KATEGORIE_ID
                ))

                val kategorieQuery = "SELECT * FROM ${SmartSaveContract.KategorieEntry.TABLE_NAME} WHERE ${SmartSaveContract.KategorieEntry.KATEGORIE_ID} = ?"
                val kategorieCursor = db.rawQuery(kategorieQuery, arrayOf(kategorieId.toString()))

                if (kategorieCursor.moveToFirst()) {
                    val kategorie = Kategorie(
                        kategorieCursor.getString(kategorieCursor.getColumnIndexOrThrow(
                            SmartSaveContract.KategorieEntry.NAME
                        ))
                    )
                    kategorie.id = kategorieCursor.getInt(kategorieCursor.getColumnIndexOrThrow(
                        SmartSaveContract.KategorieEntry.KATEGORIE_ID
                    ))
                    umsatz.kategorie = kategorie
                }
                kategorieCursor.close()
            }
            kategorieIdCursor.close()
        }
        cursor.close()
        return umsatz
    }


    fun getKreditKontenListe(): List<Konto> {
        val kreditKontenListe: MutableList<Konto> = mutableListOf()
        val db = readableDatabase
        val query = "SELECT * FROM ${SmartSaveContract.KontoEntry.TABLE_NAME} WHERE ${SmartSaveContract.KontoEntry.KONTOART} = 'Kreditkartenkonto'"
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val konto = Konto(
                cursor.getLong(cursor.getColumnIndexOrThrow(SmartSaveContract.KontoEntry.KONTONUMMER)),
                cursor.getString(cursor.getColumnIndexOrThrow(SmartSaveContract.KontoEntry.BLZ)),
                cursor.getString(cursor.getColumnIndexOrThrow(SmartSaveContract.KontoEntry.BIC)),
                cursor.getString(cursor.getColumnIndexOrThrow(SmartSaveContract.KontoEntry.IBAN)),
                cursor.getString(cursor.getColumnIndexOrThrow(SmartSaveContract.KontoEntry.BEMERKUNG)),
                cursor.getString(cursor.getColumnIndexOrThrow(SmartSaveContract.KontoEntry.KONTOART))
            )
            konto.umsatzList = loadUmsaetzeForKonto(konto.kontonr)
            konto.kontostand = konto.calcKontostand()
            kreditKontenListe.add(konto)
        }

        cursor.close()
        return kreditKontenListe
    }
    fun getBankUndKreditKontenListe(): List<Konto> {
        val bankUndKreditKontenListe: MutableList<Konto> = mutableListOf()
        val db = readableDatabase
        val query = "SELECT * FROM ${SmartSaveContract.KontoEntry.TABLE_NAME} WHERE ${SmartSaveContract.KontoEntry.KONTOART} != 'Sparkonto'"
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val konto = Konto(
                cursor.getLong(cursor.getColumnIndexOrThrow(SmartSaveContract.KontoEntry.KONTONUMMER)),
                cursor.getString(cursor.getColumnIndexOrThrow(SmartSaveContract.KontoEntry.BLZ)),
                cursor.getString(cursor.getColumnIndexOrThrow(SmartSaveContract.KontoEntry.BIC)),
                cursor.getString(cursor.getColumnIndexOrThrow(SmartSaveContract.KontoEntry.IBAN)),
                cursor.getString(cursor.getColumnIndexOrThrow(SmartSaveContract.KontoEntry.BEMERKUNG)),
                cursor.getString(cursor.getColumnIndexOrThrow(SmartSaveContract.KontoEntry.KONTOART))
            )
            konto.umsatzList = loadUmsaetzeForKonto(konto.kontonr)
            konto.kontostand = konto.calcKontostand()
            bankUndKreditKontenListe.add(konto)
        }

        cursor.close()
        return bankUndKreditKontenListe
    }

    fun getSparKontenListe(): List<Konto> {
        val sparKontenListe: MutableList<Konto> = mutableListOf()
        val db = readableDatabase
        val query = "SELECT * FROM ${SmartSaveContract.KontoEntry.TABLE_NAME} WHERE ${SmartSaveContract.KontoEntry.KONTOART} = 'Sparkonto'"
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val konto = Konto(
                cursor.getLong(cursor.getColumnIndexOrThrow(SmartSaveContract.KontoEntry.KONTONUMMER)),
                cursor.getString(cursor.getColumnIndexOrThrow(SmartSaveContract.KontoEntry.BLZ)),
                cursor.getString(cursor.getColumnIndexOrThrow(SmartSaveContract.KontoEntry.BIC)),
                cursor.getString(cursor.getColumnIndexOrThrow(SmartSaveContract.KontoEntry.IBAN)),
                cursor.getString(cursor.getColumnIndexOrThrow(SmartSaveContract.KontoEntry.BEMERKUNG)),
                cursor.getString(cursor.getColumnIndexOrThrow(SmartSaveContract.KontoEntry.KONTOART))
            )
            konto.umsatzList = loadUmsaetzeForKonto(konto.kontonr)
            konto.kontostand = konto.calcKontostand()
            sparKontenListe.add(konto)
        }

        cursor.close()
        return sparKontenListe
    }

    fun getAllKonten(): List<Konto> {
        val kontoListe: MutableList<Konto> = mutableListOf()
        val db = readableDatabase
        val query = "SELECT * FROM ${SmartSaveContract.KontoEntry.TABLE_NAME}"
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val konto = Konto(
                cursor.getLong(cursor.getColumnIndexOrThrow(SmartSaveContract.KontoEntry.KONTONUMMER)),
                cursor.getString(cursor.getColumnIndexOrThrow(SmartSaveContract.KontoEntry.BLZ)),
                cursor.getString(cursor.getColumnIndexOrThrow(SmartSaveContract.KontoEntry.BIC)),
                cursor.getString(cursor.getColumnIndexOrThrow(SmartSaveContract.KontoEntry.IBAN)),
                cursor.getString(cursor.getColumnIndexOrThrow(SmartSaveContract.KontoEntry.BEMERKUNG)),
                cursor.getString(cursor.getColumnIndexOrThrow(SmartSaveContract.KontoEntry.KONTOART))
            )
            konto.umsatzList = loadUmsaetzeForKonto(konto.kontonr)
            konto.kontostand = konto.calcKontostand()
            kontoListe.add(konto)
        }

        cursor.close()
        return kontoListe
    }

    fun getEinzelumsatzListe(): List<Einzelumsatz> {
        val einzelumsatzListe: MutableList<Einzelumsatz> = mutableListOf()
        val db = readableDatabase
        val query = "SELECT * FROM ${SmartSaveContract.EinzelumsatzEntry.TABLE_NAME}"
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val einzelumsatz = Einzelumsatz(
                cursor.getString(cursor.getColumnIndexOrThrow(SmartSaveContract.EinzelumsatzEntry.VERWENDUNGSZWECK)),
                cursor.getDouble(cursor.getColumnIndexOrThrow(SmartSaveContract.EinzelumsatzEntry.BETRAG)),
                parseDate(cursor.getString(cursor.getColumnIndexOrThrow(SmartSaveContract.EinzelumsatzEntry.DATUM)))
            )
            einzelumsatz.id = cursor.getInt(cursor.getColumnIndexOrThrow(SmartSaveContract.EinzelumsatzEntry.EINZELUMSATZ_ID))

            // Überprüfen, ob der Einzelumsatz in der Einzelumsatzzuweisung-Tabelle existiert
            val checkQuery = "SELECT * FROM ${SmartSaveContract.EinzelumsatzZuweisungEntry.TABLE_NAME} WHERE ${SmartSaveContract.EinzelumsatzEntry.EINZELUMSATZ_ID} = ${einzelumsatz.id}"
            val checkCursor = db.rawQuery(checkQuery, null)

            if (checkCursor.moveToFirst()) {
                einzelumsatz.hasParentUmsatz = true
            }

            checkCursor.close()

            // Kategorie für den Einzelumsatz abrufen
            val kategorieQuery = "SELECT ${SmartSaveContract.KategorieEntry.TABLE_NAME}.* FROM ${SmartSaveContract.KategorieEntry.TABLE_NAME} " +
                    "JOIN ${SmartSaveContract.KategorieZuweisungEntry.TABLE_NAME} ON ${SmartSaveContract.KategorieEntry.TABLE_NAME}.${SmartSaveContract.KategorieEntry.KATEGORIE_ID} = ${SmartSaveContract.KategorieZuweisungEntry.TABLE_NAME}.${SmartSaveContract.KategorieZuweisungEntry.KATEGORIE_ID} " +
                    "WHERE ${SmartSaveContract.KategorieZuweisungEntry.UMSATZ_ID} = ${einzelumsatz.id} AND ${SmartSaveContract.KategorieZuweisungEntry.IS_EINZELUMSATZ} = 1"
            val kategorieCursor = db.rawQuery(kategorieQuery, null)

            if (kategorieCursor.moveToFirst()) {
                val kategorie = Kategorie(
                    kategorieCursor.getString(kategorieCursor.getColumnIndexOrThrow(
                        SmartSaveContract.KategorieEntry.NAME
                    ))
                )
                kategorie.id = kategorieCursor.getInt(kategorieCursor.getColumnIndexOrThrow(
                    SmartSaveContract.KategorieEntry.KATEGORIE_ID
                ))
                einzelumsatz.kategorie = kategorie
            }

            kategorieCursor.close()
            einzelumsatzListe.add(einzelumsatz)
        }
        cursor.close()
        return einzelumsatzListe
    }

    fun addEinzelumsatzToUmsatz(umsatzId: Int, einzelumsatz: Einzelumsatz) {
        val db = writableDatabase

        // Füge die Beziehung in der EinzelumsatzZuweisung-Tabelle hinzu
        val zuweisungValues = ContentValues().apply {
            put(SmartSaveContract.EinzelumsatzZuweisungEntry.UMSATZ_ID, umsatzId)
            put(SmartSaveContract.EinzelumsatzZuweisungEntry.EINZELUMSATZ_ID, einzelumsatz.id)
        }


        db.insert(SmartSaveContract.EinzelumsatzZuweisungEntry.TABLE_NAME, null, zuweisungValues)
    }

    fun updateEinzelumsatzZuweisung(einzelumsatz: Einzelumsatz, newUmsatzId: Int) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(SmartSaveContract.EinzelumsatzZuweisungEntry.UMSATZ_ID, newUmsatzId)
        }
        val whereClause = "${SmartSaveContract.EinzelumsatzZuweisungEntry.EINZELUMSATZ_ID} = ?"
        val whereArgs = arrayOf(einzelumsatz.id.toString())
        val count = db.update(SmartSaveContract.EinzelumsatzZuweisungEntry.TABLE_NAME, values, whereClause, whereArgs)

        Log.d("updateEinzelumsatzZuweisung", "Updated $count rows")
    }

    fun removeEinzelumsatzZuweisung(einzelumsatz: Einzelumsatz) {
        val db = writableDatabase
        val whereClause = "${SmartSaveContract.EinzelumsatzZuweisungEntry.EINZELUMSATZ_ID} = ?"
        val whereArgs = arrayOf(einzelumsatz.id.toString())

        val count = db.delete(SmartSaveContract.EinzelumsatzZuweisungEntry.TABLE_NAME, whereClause, whereArgs)

        Log.d("removeEinzelumsatzZuweisung", "Removed $count rows")
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
            val zieldatum = parseDate(cursor.getString(cursor.getColumnIndexOrThrow(
                SmartSaveContract.SparzielEntry.ZIELDATUM
            )))
            val monatsrate = cursor.getDouble(cursor.getColumnIndexOrThrow(SmartSaveContract.SparzielEntry.MONATSRATE))

            val kontenQuery = "SELECT * FROM ${SmartSaveContract.SparzielZuweisungEntry.TABLE_NAME} WHERE ${SmartSaveContract.SparzielZuweisungEntry.SPARZIEL_ID} = $sparzielID"
            val kontenCursor = db.rawQuery(kontenQuery, null)
            var zielkonto: Konto? = null
            var auszahlkonto: Konto? = null

            if (kontenCursor.moveToFirst()) {
                val zielkontoNr = kontenCursor.getLong(kontenCursor.getColumnIndexOrThrow(
                    SmartSaveContract.SparzielZuweisungEntry.ZIELKONTO
                ))
                val auszahlkontoNr = kontenCursor.getLong(kontenCursor.getColumnIndexOrThrow(
                    SmartSaveContract.SparzielZuweisungEntry.AUSZAHLKONTO
                ))

                zielkonto = getKontoByKontonummer(zielkontoNr)
                auszahlkonto = getKontoByKontonummer(auszahlkontoNr)
            }
            kontenCursor.close()

            if (zielkonto != null && auszahlkonto != null) {
                val sparziel = Sparziel(name, betrag, zieldatum, monatsrate, zielkonto, auszahlkonto)
                sparziel.id = sparzielID
                sparzielListe.add(sparziel)
            }
        }

        cursor.close()
        return sparzielListe
    }

    fun getKontoByKontonummer(kontonummer: Long): Konto? {
        val db = readableDatabase
        val query = "SELECT * FROM ${SmartSaveContract.KontoEntry.TABLE_NAME} WHERE ${SmartSaveContract.KontoEntry.KONTONUMMER} = $kontonummer"
        val cursor = db.rawQuery(query, null)
        var konto: Konto? = null

        if (cursor.moveToFirst()) {
            konto = Konto(
                cursor.getLong(cursor.getColumnIndexOrThrow(SmartSaveContract.KontoEntry.KONTONUMMER)),
                cursor.getString(cursor.getColumnIndexOrThrow(SmartSaveContract.KontoEntry.BLZ)),
                cursor.getString(cursor.getColumnIndexOrThrow(SmartSaveContract.KontoEntry.BIC)),
                cursor.getString(cursor.getColumnIndexOrThrow(SmartSaveContract.KontoEntry.IBAN)),
                cursor.getString(cursor.getColumnIndexOrThrow(SmartSaveContract.KontoEntry.BEMERKUNG)),
                cursor.getString(cursor.getColumnIndexOrThrow(SmartSaveContract.KontoEntry.KONTOART))
            )
            konto.kontostand = konto.calcKontostand()
            konto.umsatzList = loadUmsaetzeForKonto(konto.kontonr)
        }

        cursor.close()
        return konto
    }

    fun updateKategorieZuweisung(kategorieId: Int, umsatzId: Int, isEinzelumsatz: Boolean) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(SmartSaveContract.KategorieZuweisungEntry.KATEGORIE_ID, kategorieId)
        }
        val selection = "${SmartSaveContract.KategorieZuweisungEntry.UMSATZ_ID} = ? AND ${SmartSaveContract.KategorieZuweisungEntry.IS_EINZELUMSATZ} = ?"
        val selectionArgs = arrayOf(umsatzId.toString(), if (isEinzelumsatz) "1" else "0")

        Log.d("com.example.smartsave.DbHelper", "Values: $values")
        Log.d("com.example.smartsave.DbHelper", "Selection: $selection")
        Log.d("com.example.smartsave.DbHelper", "SelectionArgs: ${selectionArgs.joinToString()}")

        // Execute the update query
        val count = db.update(
            SmartSaveContract.KategorieZuweisungEntry.TABLE_NAME,
            values,
            selection,
            selectionArgs
        )

        Log.d("com.example.smartsave.DbHelper", "Updated $count rows")
    }

    fun insertSparziel(sparziel: Sparziel) {
        val db = writableDatabase

        val values = ContentValues().apply {
            put(SmartSaveContract.SparzielEntry.NAME, sparziel.name)
            put(SmartSaveContract.SparzielEntry.BETRAG, sparziel.betrag)
            put(SmartSaveContract.SparzielEntry.ZIELDATUM, sparziel.zieldatum.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
            put(SmartSaveContract.SparzielEntry.MONATSRATE, sparziel.monatsrate)
        }
        val sparzielId = db.insert(SmartSaveContract.SparzielEntry.TABLE_NAME, null, values)

        val zuweisungValues = ContentValues().apply {
            put(SmartSaveContract.SparzielZuweisungEntry.SPARZIEL_ID, sparzielId)
            put(SmartSaveContract.SparzielZuweisungEntry.ZIELKONTO, sparziel.zielKonto.kontonr)
            put(SmartSaveContract.SparzielZuweisungEntry.AUSZAHLKONTO, sparziel.auszahlungsKonto.kontonr)
        }
        db.insert(SmartSaveContract.SparzielZuweisungEntry.TABLE_NAME, null, zuweisungValues)
    }

    fun deleteSparziel(sparzielId: Int) {
        val db = writableDatabase
        val selection = "${SmartSaveContract.SparzielEntry.SPARZIEL_ID} = ?"
        val selectionArgs = arrayOf(sparzielId.toString())
        db.delete(SmartSaveContract.SparzielEntry.TABLE_NAME, selection, selectionArgs)
        db.delete(SmartSaveContract.SparzielZuweisungEntry.TABLE_NAME, selection, selectionArgs)
    }

    fun kategorieisAssigned(kategorie: Kategorie): Boolean {
        val kontoListe = this.getAllKonten()
        val einzelumsatzListe = this.getEinzelumsatzListe()
        var isAssigned = false

        for (konto in kontoListe) {
            for (umsatz in konto.umsatzList) {
                if (umsatz.kategorie == kategorie) {
                    isAssigned = true
                }
            }
        }
        for (einzelumsatz in einzelumsatzListe) {
            if(einzelumsatz.kategorie == kategorie) {
                isAssigned = true
            }
        }
        return isAssigned
    }

    fun removeKategorieAssigns(kategorie: Kategorie) {
        val nichtZugeordnetKategorieId = getNichtZugeordnetKategorieId()

        if (nichtZugeordnetKategorieId != null) {
            val db = writableDatabase
            val values = ContentValues().apply {
                put(SmartSaveContract.KategorieZuweisungEntry.KATEGORIE_ID, nichtZugeordnetKategorieId)
            }

            db.update(
                SmartSaveContract.KategorieZuweisungEntry.TABLE_NAME,
                values,
                "${SmartSaveContract.KategorieZuweisungEntry.KATEGORIE_ID} = ?",
                arrayOf(kategorie.id.toString())
            )
        }
    }

    private fun getNichtZugeordnetKategorieId(): Int? {
        val db = readableDatabase
        val query = "SELECT ${SmartSaveContract.KategorieEntry.KATEGORIE_ID} FROM ${SmartSaveContract.KategorieEntry.TABLE_NAME} " +
                "WHERE ${SmartSaveContract.KategorieEntry.NAME} = 'Nicht zugeordnet'"
        val cursor = db.rawQuery(query, null)
        var kategorieId: Int? = null

        if (cursor.moveToFirst()) {
            kategorieId = cursor.getInt(cursor.getColumnIndexOrThrow(SmartSaveContract.KategorieEntry.KATEGORIE_ID))
        }

        cursor.close()
        return kategorieId
    }

    private fun parseDate(datumString: String): LocalDate {
        val dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        return LocalDate.parse(datumString, dateFormat)
    }

    fun clearAllData() {
        val db = writableDatabase
        db.execSQL("DELETE FROM ${SmartSaveContract.KontoEntry.TABLE_NAME}")
        db.execSQL("DELETE FROM ${SmartSaveContract.UmsatzEntry.TABLE_NAME}")
        db.execSQL("DELETE FROM ${SmartSaveContract.EinzelumsatzEntry.TABLE_NAME}")
        db.execSQL("DELETE FROM ${SmartSaveContract.KategorieEntry.TABLE_NAME}")
        db.execSQL("DELETE FROM ${SmartSaveContract.KategorieZuweisungEntry.TABLE_NAME}")
        db.execSQL("DELETE FROM ${SmartSaveContract.EinzelumsatzZuweisungEntry.TABLE_NAME}")
        db.execSQL("DELETE FROM ${SmartSaveContract.SparzielEntry.TABLE_NAME}")
        db.execSQL("DELETE FROM ${SmartSaveContract.SparzielZuweisungEntry.TABLE_NAME}")
    }

    fun insertRandomUmsatzForExistingKonto(konto: Konto) {
        val db = writableDatabase

        val umsaetze = listOf(
            Triple("Testumsatz 1", -100.0, "01/03/2024"),
            Triple("Testumsatz 2", 450.0, "15/01/2024"),
            Triple("Testumsatz 3", -200.0, "28/04/2024")
        )

        umsaetze.forEach { (verwendungszweck, betrag, datum) ->
            val umsatzValues = ContentValues().apply {
                put(SmartSaveContract.UmsatzEntry.KONTONUMMER, konto.kontonr)
                put(SmartSaveContract.UmsatzEntry.DATUM, datum)
                put(SmartSaveContract.UmsatzEntry.VERWENDUNGSZWECK, verwendungszweck)
                put(SmartSaveContract.UmsatzEntry.BETRAG, betrag)
            }

            val umsatzId = db.insert(SmartSaveContract.UmsatzEntry.TABLE_NAME, null, umsatzValues)

            val defaultKategorieID = this.getKategorieIdByName("Nicht zugeordnet")

            // Umsatz der Kategorie "Nicht zugeordnet" zuweisen
            val zuweisungValues = ContentValues().apply {
                put(SmartSaveContract.KategorieZuweisungEntry.KATEGORIE_ID, defaultKategorieID)
                put(SmartSaveContract.KategorieZuweisungEntry.UMSATZ_ID, umsatzId.toInt())
                put(SmartSaveContract.KategorieZuweisungEntry.IS_EINZELUMSATZ, 0)
            }

            // Eintrag in die KategorieZuweisung-Tabelle einfügen
            db.insert(SmartSaveContract.KategorieZuweisungEntry.TABLE_NAME, null, zuweisungValues)

            Log.d("insertRandomUmsatzForExistingKonto", "Inserted random Umsatz for Konto: ${konto.kontonr}")
        }
    }

    fun insertDefaultData() {
        val db = writableDatabase

        val cursor = db.query(
            SmartSaveContract.KategorieEntry.TABLE_NAME,
            arrayOf(SmartSaveContract.KategorieEntry.NAME),
            "${SmartSaveContract.KategorieEntry.NAME} = ?",
            arrayOf("Nicht zugeordnet"),
            null,
            null,
            null
        )

        val exists = cursor.count > 0
        cursor.close()

        if (!exists) {
            val values = ContentValues().apply {
                put(SmartSaveContract.KategorieEntry.NAME, "Nicht zugeordnet")
            }
            db.insert(SmartSaveContract.KategorieEntry.TABLE_NAME, null, values)
        }
    }

    private fun getKategorieIdByName(kategorieName: String): Int {
        val db = readableDatabase

        val projection = arrayOf(SmartSaveContract.KategorieEntry.KATEGORIE_ID)
        val selection = "${SmartSaveContract.KategorieEntry.NAME} = ?"
        val selectionArgs = arrayOf(kategorieName)

        val cursor = db.query(
            SmartSaveContract.KategorieEntry.TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null,
            null
        )

        var kategorieId: Int = -1

        if (cursor.moveToFirst()) {
            kategorieId = cursor.getInt(cursor.getColumnIndexOrThrow(SmartSaveContract.KategorieEntry.KATEGORIE_ID))
        }

        cursor.close()

        return kategorieId
    }


}
