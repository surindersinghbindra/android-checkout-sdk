package com.caribeanroyal.ecommercesample.core.domain.usecase

import kotlinx.coroutines.flow.Flow

interface UseCase<in Params, out Result> {
    operator fun invoke(params: Params): Flow<Result>
}

// Result wrapper
sealed class DomainResult<out T> {
    data class Success<out T>(val data: T) : DomainResult<T>()
    data class Error(val exception: Throwable) : DomainResult<Nothing>()
    object Loading : DomainResult<Nothing>()
}
