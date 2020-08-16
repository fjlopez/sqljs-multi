package app

import react.*
import react.dom.*
import sqlite.QueryResults

interface ResultsProps : RProps {
    var results: Array<QueryResults>
}

class Results : RComponent<ResultsProps, RState>() {
    override fun RBuilder.render() {
        pre {
            table {
                if (props.results.isEmpty()) emptyResults() else props.results.forEach { result -> render(result) }
            }
        }
    }

    private fun RBuilder.render(result: QueryResults) {
        thead { tr { result.columns.forEach { columnName -> td { +columnName } } } }
        tbody { result.values.forEach { row -> tr { row.forEach { value -> td { +value.toString() } } } } }
    }

    private fun RBuilder.emptyResults() {
        thead { tr { td { +"No results" } } }
    }
}

fun RBuilder.results(handler: ResultsProps.() -> Unit): ReactElement {
    return child(Results::class) {
        attrs(handler)
    }
}