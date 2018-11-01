package ${packageName};

import org.koin.androidx.scope.ext.android.bindScope
import org.koin.androidx.scope.ext.android.getOrCreateScope
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.loadKoinModules

private object ${className}Module {

    fun get() = module("${className}Module", override = true) {
        viewModel { ${className}ViewModel(/*params*/) }
        scope(scopeId) {  }
    }
}

private const val scopeId = "${className}Feature"

internal fun ${className}Fragment.loadModule() {
    loadKoinModules(${className}Module.get())
    bindScope(getOrCreateScope(scopeId))
}