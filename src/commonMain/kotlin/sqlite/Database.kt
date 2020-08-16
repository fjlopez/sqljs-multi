package sqlite

expect class Database {
    fun exec(sql: String): Array<QueryResults>
    fun export(): Uint8Array
}

expect class Uint8Array

expect interface QueryResults

class SqliteDriver(private val db: Database) {
    fun exec(sql: String): Array<QueryResults> = db.exec(sql)
    fun export(): Uint8Array = db.export()
}