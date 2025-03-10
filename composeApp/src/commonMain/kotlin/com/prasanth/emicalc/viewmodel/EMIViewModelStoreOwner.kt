package com.prasanth.emicalc.viewmodel

import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner

class EMIViewModelStoreOwner(override val viewModelStore: ViewModelStore = ViewModelStore()) :ViewModelStoreOwner {

    companion object INSTANCE

}