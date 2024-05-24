package com.appmobiledition.laundryfinder.managers.network

import com.appmobiledition.laundryfinder.managers.network.native.NetworkNativeAtomicReference
import kotlin.native.concurrent.ThreadLocal

class NetworkManager {

    @ThreadLocal
    companion object : BaseNetworkManagerCompanion {
        override fun startNetworkObserver() = networkLoyal.startNetworkObserver()

        override fun stopNetworkObserver() = networkLoyal.stopNetworkObserver()

        override fun onNetworkStateChanges(block: OnNetworkStateChangesBlock): BaseNetworkManagerCompanion {
            TODO("Not yet implemented")
        }

        override fun onNetworkSourceChanges(block: OnNetworkSourceChangesBlock): BaseNetworkManagerCompanion {
            TODO("Not yet implemented")
        }

        override fun onNetworkUnavailable(block: OnNetworkUnavailableBlock): BaseNetworkManagerCompanion {
            TODO("Not yet implemented")
            isOnline = false
        }

        override fun onNetworkAvailable(block: OnNetworkAvailableBlock): BaseNetworkManagerCompanion {
            TODO("Not yet implemented")
        }

        override fun networkIsAvailable() = isOnline

        internal fun notifyNetworkAvailable() {
            isOnline = true
        }

        internal fun notifyNetworkUnavailable() {
            isOnline = false
        }

        internal val networkLoyal: NetworkLoyal by lazy { NetworkLoyal() }

        private var isOnline: Boolean = false
        private lateinit var networkSource: NetworkSource

        private val onNetworkStateChangesBlocks =
            NetworkNativeAtomicReference(listOf<OnNetworkStateChangesBlock>())
        private val onNetworkSourcesChangesBlocks =
            NetworkNativeAtomicReference(listOf<OnNetworkSourceChangesBlock>())
        private val onNetworkAvailableBlocks =
            NetworkNativeAtomicReference(listOf<OnNetworkStateChangesBlock>())
        private val onNetworkUnavailableBlocks =
            NetworkNativeAtomicReference(listOf<OnNetworkStateChangesBlock>())

    }
}