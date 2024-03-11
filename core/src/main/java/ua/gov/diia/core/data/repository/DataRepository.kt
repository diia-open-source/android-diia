package ua.gov.diia.core.data.repository

import kotlinx.coroutines.flow.Flow

interface DataRepository<T> {

    val data: Flow<T>

    suspend fun load(): T

    suspend fun clear()
}