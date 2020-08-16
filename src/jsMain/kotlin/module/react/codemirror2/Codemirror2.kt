@file:JsModule("react-codemirror2")
@file:JsNonModule

package module.react.codemirror2

import react.RClass
import react.RProps

@JsName("Controlled")
external val codeMirror: RClass<CodeMirrorProps>

external interface CodeMirrorProps : RProps {
    var value: String
    var options: dynamic
    var onBeforeChange: (dynamic, dynamic, String) -> Unit
    var onChange: (dynamic, dynamic, String) -> Unit
}


