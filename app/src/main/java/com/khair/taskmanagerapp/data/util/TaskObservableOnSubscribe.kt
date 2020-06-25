package com.khair.taskmanagerapp.data.util

import android.content.res.AssetManager
import com.google.gson.Gson
import com.google.gson.JsonIOException
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.khair.taskmanagerapp.data.dto.TaskNet
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import java.io.InputStreamReader

class TaskObservableOnSubscribe(val assetFileName: String, val assets: AssetManager) : ObservableOnSubscribe<List<TaskNet>> {

    override fun subscribe(emitter: ObservableEmitter<List<TaskNet>>?) {
        val inputStream = assets.open(assetFileName)
        val reader = JsonReader(InputStreamReader(inputStream, "UTF-8"))
        try {
            val gson = Gson()
            val messages: List<TaskNet> = gson.fromJson(reader, object: TypeToken<List<TaskNet>>(){}.type)
            emitter?.onNext(messages)
            emitter?.onComplete()
        } catch (e: Exception){
            emitter?.onError(e)
        } finally {
            reader.close()
        }
    }
}