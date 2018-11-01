package ${packageName};

import com.popalay.cardme.api.ui.state.IntentProcessor
import com.popalay.cardme.api.ui.state.LambdaReducer
import com.popalay.cardme.api.ui.state.Processor
import com.popalay.cardme.api.ui.state.Reducer
import com.popalay.cardme.core.state.BaseMviViewModel
import io.reactivex.rxkotlin.ofType

internal class ${className}ViewModel(
    //TODO: Use cases
) : BaseMviViewModel<${className}ViewState, ${className}Intent>() {

    override val initialState: ${className}ViewState = ${className}ViewState()

    override val processor: Processor<${className}Intent> = IntentProcessor { observable ->
        listOf(
            //TODO: Intents to Actions
        )
    }

    override val reducer: Reducer<${className}ViewState> = LambdaReducer {
        when (this) {
            //TODO: Result to ViewState
            else -> throw IllegalStateException("Can not reduce user for result $this")
        }
    }
}