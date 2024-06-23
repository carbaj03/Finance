package helper

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.CompletableDeferred

class Deferrable<A> {
  private var result: CompletableDeferred<A> = CompletableDeferred()

  suspend fun await(): A =
    result.await().also { result = CompletableDeferred() }

  fun complete(value: A) {
    if (result.isCompleted) return
    result.complete(value)
  }
}

@Composable
fun <A> rememberDeferrable(): Deferrable<A> =
  remember { Deferrable() }