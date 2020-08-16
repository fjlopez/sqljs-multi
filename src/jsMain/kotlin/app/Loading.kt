package app

import react.*
import react.dom.pre

class Loading : RComponent<RProps, RState>() {
    override fun RBuilder.render() {
        pre { +"Loading ..." }
    }
}

fun RBuilder.loading(): ReactElement {
    return child(Loading::class) {}
}