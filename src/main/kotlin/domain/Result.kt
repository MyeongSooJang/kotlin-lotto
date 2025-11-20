package domain

sealed interface Result<T> {
    data class Success<T>(val value: T) : Result<T>
    data class Failure<T>(val error: String) : Result<T>
}