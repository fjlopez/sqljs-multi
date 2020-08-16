package app

import kotlinx.browser.document
import kotlinx.html.dom.create
import kotlinx.html.js.a
import kotlinx.html.js.onClickFunction
import org.w3c.dom.events.Event
import org.w3c.dom.url.URL
import org.w3c.files.Blob
import org.w3c.files.BlobPropertyBag
import react.*
import react.dom.button
import react.dom.div
import react.dom.h1
import module.react.codemirror2.codeMirror
import sqlite.SqliteDriver
import sqlite.QueryResults

class RenderApp : RComponent<RenderAppProps, RenderAppState>() {

    override fun componentDidMount() {
        setState {
            sql = """
                    DROP TABLE IF EXISTS employees;
                    CREATE TABLE employees( id          integer,  name    text,
                                            designation text,     manager integer,
                                            hired_on    date,     salary  integer,
                                            commission  float,    dept    integer);

                    INSERT INTO employees VALUES (1,'JOHNSON','ADMIN',6,'1990-12-17',18000,NULL,4);
                    INSERT INTO employees VALUES (2,'HARDING','MANAGER',9,'1998-02-02',52000,300,3);
                    INSERT INTO employees VALUES (3,'TAFT','SALES I',2,'1996-01-02',25000,500,3);
                    INSERT INTO employees VALUES (4,'HOOVER','SALES I',2,'1990-04-02',27000,NULL,3);
                    INSERT INTO employees VALUES (5,'LINCOLN','TECH',6,'1994-06-23',22500,1400,4);
                    INSERT INTO employees VALUES (6,'GARFIELD','MANAGER',9,'1993-05-01',54000,NULL,4);
                    INSERT INTO employees VALUES (7,'POLK','TECH',6,'1997-09-22',25000,NULL,4);
                    INSERT INTO employees VALUES (8,'GRANT','ENGINEER',10,'1997-03-30',32000,NULL,2);
                    INSERT INTO employees VALUES (9,'JACKSON','CEO',NULL,'1990-01-01',75000,NULL,4);
                    INSERT INTO employees VALUES (10,'FILLMORE','MANAGER',9,'1994-08-09',56000,NULL,2);
                    INSERT INTO employees VALUES (11,'ADAMS','ENGINEER',10,'1996-03-15',34000,NULL,2);
                    INSERT INTO employees VALUES (12,'WASHINGTON','ADMIN',6,'1998-04-16',18000,NULL,4);
                    INSERT INTO employees VALUES (13,'MONROE','ENGINEER',10,'2000-12-03',30000,NULL,2);
                    INSERT INTO employees VALUES (14,'ROOSEVELT','CPA',9,'1995-10-12',35000,NULL,1);

                    SELECT designation,COUNT(*) AS nbr, (AVG(salary)) AS avg_salary FROM employees GROUP BY designation ORDER BY avg_salary DESC;
                    SELECT name,hired_on FROM employees ORDER BY hired_on;                    
                """.trimIndent()
        }
    }

    private fun exec(sql: String) {
        runCatching {
            props.db.exec(sql)
        }.onSuccess {
            setState {
                results = it
                error = null
            }
        }.onFailure {
            setState {
                error = it
            }
        }
    }

    private fun executeSql(@Suppress("UNUSED_PARAMETER") event: Event) {
        state.sql?.let { exec(it) }
    }

    override fun RBuilder.render() {
        val results = state.results
        val error = state.error
        div("App") {
            h1 {
                +"Kotlin-React SQL interpreter"
            }
            codeMirror {
                attrs {
                    value = (state.sql ?: "")
                    options = js {
                        mode = "text/x-mysql"
                        viewportMargin = js("Infinity")
                        indentWithTabs = true
                        smartIndent = true
                        lineNumbers = true
                        matchBrackets = true
                        autofocus = true
                    }
                    onBeforeChange = { _, _, value -> setState { sql = value } }
                }
            }
            button(classes = "button") {
                +"Execute"
                attrs {
                    onClickFunction = ::executeSql
                }
            }
            button(classes = "button") {
                +"Export"
                attrs {
                    onClickFunction = {
                        val export = props.db.export()
                        val blob = Blob(arrayOf(export), BlobPropertyBag(type = "octet/stream"))
                        val url = URL.createObjectURL(blob)
                        val link = document.create.a()
                        link.style.display = "none"
                        link.href = url
                        link.download = "sqlite.db"
                        link.click()
                        URL.revokeObjectURL(url)
                    }
                }
            }
            when {
                error != null -> error { this.error = error }
                results != null -> results { this.results = results }
            }
        }
    }
}

interface RenderAppState : RState {
    var sql: String?
    var error: Throwable?
    var results: Array<QueryResults>?
}

interface RenderAppProps : RProps {
    var db: SqliteDriver
}

fun RBuilder.renderApp(handler: RenderAppProps.() -> Unit): ReactElement {
    return child(RenderApp::class) {
        attrs(handler)
    }
}

fun js(builder: dynamic.() -> Unit): dynamic {
    val obj = Any().asDynamic()
    builder.invoke(obj)
    return obj
}