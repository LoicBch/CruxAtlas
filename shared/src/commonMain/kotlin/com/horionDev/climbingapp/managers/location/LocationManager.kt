package com.horionDev.climbingapp.managers.location

import com.horionDev.climbingapp.managers.location.extension.appending
import com.horionDev.climbingapp.managers.location.extension.removed
import com.horionDev.climbingapp.managers.location.native.NativeAtomicReference
import kotlin.native.concurrent.ThreadLocal

class LocationManager {

    @ThreadLocal
    companion object : BaseLocationManagerCompanion {

        override fun currentLocation(block: OnLocationUpdatedBlock) {
            onLocationUpdatedBlocks.value = onLocationUpdatedBlocks.value.appending(block)
            locationLoyal.getCurrentLocation()
        }

        override fun isLocationEnable() = locationLoyal.isLocationEnabled()

        override fun isLocationObserved() = locationLoyal.isLocationObserved()

        override fun isPermissionAllowed() = locationLoyal.isPermissionAllowed()

        override fun requestPermission() = locationLoyal.requestPermission()

        override fun startLocationUpdating() = locationLoyal.startLocationUpdating()

        override fun stopLocationUpdating() = locationLoyal.stopLocationUpdating()

        override fun onLocationUnavailable(
            target: Any,
            block: OnLocationUnavailableBlock
        ): BaseLocationManagerCompanion {
            onLocationUnavailableBlockMap.value =
                onLocationUnavailableBlockMap.value.appending(target, block)
            return this
        }

        override fun onLocationUpdated(
            target: Any,
            block: OnLocationUpdatedBlock
        ): BaseLocationManagerCompanion {
            onLocationUpdatedBlockMap.value =
                onLocationUpdatedBlockMap.value.appending(target, block)
            return this
        }

        // remplacer le target par une fonction global permettant de detecter la paltform actuel ?
        // On ajoute les block d'execution a chaque call en specifiant la platform
        override fun onPermissionUpdated(
            target: Any,
            block: OnPermissionUpdatedBlock
        ): BaseLocationManagerCompanion {
            onPermissionUpdatedBlockMap.value =
                onPermissionUpdatedBlockMap.value.appending(target, block)
            return this
        }

        override fun removeAllListeners() {
            onLocationUnavailableBlockMap.value = emptyMap()
            onLocationUpdatedBlockMap.value = emptyMap()
            onPermissionUpdatedBlockMap.value = emptyMap()
            onLocationUpdatedBlocks.value = emptyList()
            locationLoyal.removeAllListeners()
        }

        override fun removeListeners(target: Any) {
            removeOnPermissionUpdated(target)
            removeOnLocationUnavailable(target)
            removeOnLocationUpdated(target)
            locationLoyal.removeListeners(target)
        }

        override fun removeOnLocationUnavailable(target: Any) {
            onLocationUnavailableBlockMap.value =
                onLocationUnavailableBlockMap.value.removed(target)
        }

        override fun removeOnLocationUpdated(target: Any) {
            onLocationUpdatedBlockMap.value = onLocationUpdatedBlockMap.value.removed(target)
        }

        override fun removeOnPermissionUpdated(target: Any) {
            onPermissionUpdatedBlockMap.value = onPermissionUpdatedBlockMap.value.removed(target)
        }

        internal val locationLoyal: LocationLoyal by lazy { LocationLoyal() }

        internal fun notifyOnLocationUpdated(data: LocationData) {
            //pas obliger de consommer les blocks ajouter par onLocationUpadted
            ////////////////////////////////////////////////////////////////////
            onLocationUpdatedBlockMap.value.forEach { it.value(data) }
            ////////////////////////////////////////////////////////////////////

            onLocationUpdatedBlocks.value.forEach { it(data) }
            onLocationUpdatedBlocks.value = emptyList()
        }

        internal fun notifyOnLocationUnavailable() {
            onLocationUnavailableBlockMap.value.forEach { it.value() }
        }

        internal fun notifyOnPermissionUpdated(isGranted: Boolean) {
            onPermissionUpdatedBlockMap.value.forEach { it.value(isGranted) }
        }

        private val onLocationUpdatedBlocks =
            NativeAtomicReference(listOf<OnLocationUpdatedBlock>())
        private val onLocationUpdatedBlockMap =
            NativeAtomicReference(mapOf<Any, OnLocationUpdatedBlock>())
        private val onLocationUnavailableBlockMap =
            NativeAtomicReference(mapOf<Any, OnLocationUnavailableBlock>())
        private val onPermissionUpdatedBlockMap =
            NativeAtomicReference(mapOf<Any, OnPermissionUpdatedBlock>())
    }
}