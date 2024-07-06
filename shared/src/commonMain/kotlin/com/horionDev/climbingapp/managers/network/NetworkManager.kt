package com.horionDev.climbingapp.managers.network

import kotlin.native.concurrent.ThreadLocal

class NetworkManager {
    enum class NetworkStatus {
        Available, Unavailable, Losing, Lost
    }

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

        override fun isNetworkAvailable() = networkLoyal.isNetworkAvailable()
//        override fun isNetworkObserved() = networkLoyal.isNetworkObserved()

        internal fun notifyNetworkAvailable() {
            isOnline = true
        }

        internal fun notifyNetworkUnavailable() {
            isOnline = false
        }

        internal val networkLoyal: NetworkLoyal by lazy { NetworkLoyal() }

        private var isOnline: Boolean = false
        private lateinit var networkSource: NetworkSource
//
//        private val onNetworkStateChangesBlocks =
//            NativeAtomicReference(listOf<OnNetworkStateChangesBlock>())
//        private val onNetworkSourcesChangesBlocks =
//            NativeAtomicReference(listOf<OnNetworkSourceChangesBlock>())
//        private val onNetworkAvailableBlocks =
//            NativeAtomicReference(listOf<OnNetworkStateChangesBlock>())
//        private val onNetworkUnavailableBlocks =
//            NativeAtomicReference(listOf<OnNetworkStateChangesBlock>())

    }
}