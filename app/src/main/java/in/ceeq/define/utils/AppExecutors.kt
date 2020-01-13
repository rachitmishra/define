package `in`.ceeq.define.utils

import android.os.Handler
import android.os.Looper
import androidx.annotation.NonNull

import java.util.concurrent.Executor
import java.util.concurrent.Executors

class AppExecutors internal constructor(private val diskIO: Executor = Executors.newSingleThreadExecutor(),
                                        private val networkIO: Executor = Executors.newFixedThreadPool(THREAD_COUNT),
                                        private val mainThread: Executor = MainThreadExecutor()) {

    fun diskIO(): Executor {
        return diskIO
    }

    fun networkIO(): Executor {
        return networkIO
    }

    fun mainThread(): Executor {
        return mainThread
    }

    class MainThreadExecutor : Executor {
        private val mainThreadHandler = Handler(Looper.getMainLooper())

        override fun execute(@NonNull command: Runnable) {
            mainThreadHandler.post(command)
        }
    }

    companion object {
        private const val THREAD_COUNT = 3
    }
}

private val diskIO: Executor = Executors.newSingleThreadExecutor()
private val mainThread: Executor = AppExecutors.MainThreadExecutor()

fun io(f: () -> Unit) {
    diskIO.execute(f)
}

fun ui(f: () -> Unit) {
    mainThread.execute(f)
}
