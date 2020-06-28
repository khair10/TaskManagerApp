package com.khair.taskmanagerapp.data.util

import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.realm.Realm
import io.realm.RealmObject

class SingleTaskObservableOnSubscribe<T: RealmObject>(val mClass: Class<T>, val id: Long) :
    ObservableOnSubscribe<T> {

    override fun subscribe(emitter: ObservableEmitter<T>?) {
        var realm: Realm? = null
        try {
            realm = Realm.getDefaultInstance()
            val result = realm.where(mClass).equalTo("id", id).findFirst()
                ?: throw NoSuchElementException()
            val task = realm.copyFromRealm(result)
            emitter?.onNext(task)
            emitter?.onComplete()
        } catch (e: Exception){
            emitter?.onError(e)
        } finally {
            realm?.close()
        }
    }
}