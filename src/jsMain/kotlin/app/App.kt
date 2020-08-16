package app

import kotlinx.browser.document
import module.sqljs.initDb
import module.sqljs.invoke
import react.*
import react.dom.render
import sqlite.SqliteDriver

interface AppState : RState {
    var loading: Boolean
    var db: SqliteDriver?
    var error: Throwable?
}

class App : RComponent<RProps, AppState>() {

    init {
        state.loading = true
    }

    override fun componentDidMount() {
        initDb().then { sqljs ->
            setState {
                db = SqliteDriver(sqljs.Database.invoke())
                loading = false
            }
        }.catch { throwable ->
            setState {
                error = throwable
            }
        }
    }

    override fun RBuilder.render() {
        val error = state.error
        val loading = state.loading
        val db = state.db

        when {
            error != null -> error { this.error = error }
            loading -> loading()
            else -> renderApp { this.db = db!! }
        }
    }
}

fun main() {
    js("require('codemirror/lib/codemirror.css');")
    js("require('codemirror/mode/sql/sql');")
    js("require('styles.css');")
    render(document.getElementById("root")) {
        child(App::class) {}
    }
}

