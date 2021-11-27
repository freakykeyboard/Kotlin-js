import react.*
import react.dom.*
import kotlinx.coroutines.*
import kotlinx.html.js.onClickFunction

private val scope = MainScope()

val App = fc<Props> { _ ->
    var taskList by useState(emptyList<TaskItem>())

    useEffectOnce {
        scope.launch {
            taskList = getTaslList()
        }
    }

    h1 {
        +"Aufgabenliste"

    }
    ul {
        taskList.forEach { item ->
            li {
                attrs.onClickFunction={
                    scope.launch {
                        deleteTask(item)
                        taskList=getTaslList()
                    }
                }
                key = item.toString()
                +"${item.text} "
            }
        }
    }
    child(inputComponent){
        attrs.onSubmit={input->
            val forumEntry=TaskItem(input)
            scope.launch {
                addTask(forumEntry)
                taskList=getTaslList()
            }
        }
    }
}