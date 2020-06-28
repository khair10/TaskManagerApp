package com.khair.taskmanagerapp.data.util

import android.content.res.AssetManager
import com.google.gson.Gson
import com.google.gson.JsonIOException
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.khair.taskmanagerapp.data.dto.TaskNet
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.realm.Realm
import io.realm.RealmObject
import java.io.InputStreamReader

class TasksObservableOnSubscribe<T: RealmObject>(val mClass: Class<T>) : ObservableOnSubscribe<List<T>> {

    override fun subscribe(emitter: ObservableEmitter<List<T>>?) {
        var realm: Realm? = null
        try {
            realm = Realm.getDefaultInstance()
            val results = realm.where(mClass).findAll()
            val tasks = realm.copyFromRealm(results)
            emitter?.onNext(tasks)
            emitter?.onComplete()
        } catch (e: Exception){
            emitter?.onError(e)
        } finally {
            realm?.close()
        }
    }
}