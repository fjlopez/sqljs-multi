package app

import react.*
import react.dom.pre

interface ErrorProps : RProps {
    var error: Throwable
}

class Error : RComponent<ErrorProps, RState>() {
    override fun RBuilder.render() {
        pre("error") {
            +props.error.toString()
        }
    }
}

fun RBuilder.error(handler: ErrorProps.() -> Unit): ReactElement {
    return child(Error::class) {
        attrs(handler)
    }
}