package com.khair.taskmanagerapp.data.local

import com.khair.taskmanagerapp.data.dto.TaskNet
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.functions.Function
import io.realm.Realm

class LocalListReader(val mClass: Class<TaskNet> = TaskNet::class.java): Function<Throwable, Flowable<List<TaskNet>>> {

    override fun apply(t: Throwable): Flowable<List<TaskNet>>? {
        var realm: Realm? = null
        val outputItems: List<TaskNet>?
        try {
            realm = Realm.getDefaultInstance()
            val results = realm.where(mClass).findAll()
            outputItems = realm.copyFromRealm(results)
        }catch (t: Throwable){
            return Flowable.error(t)
        }finally {
            realm?.close()
        }
        if(outputItems == null)
            return Flowable.error(NoSuchElementException())
        return Flowable.just(outputItems)
    }
}