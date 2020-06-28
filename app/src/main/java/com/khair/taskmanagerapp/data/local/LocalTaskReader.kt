package com.khair.taskmanagerapp.data.local

import com.khair.taskmanagerapp.data.dto.TaskNet
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.functions.Function
import io.realm.Realm
import org.reactivestreams.Publisher

class LocalTaskReader(val id: Long, val mClass: Class<TaskNet> = TaskNet::class.java): Function<Throwable, Flowable<TaskNet>> {

    override fun apply(t: Throwable): Flowable<TaskNet> {
        var realm: Realm? = null
        var outputItem: TaskNet? = null
        try {
            realm = Realm.getDefaultInstance()
            val result = realm.where(mClass).equalTo("id", id).findFirst() ?: throw NoSuchElementException()
            outputItem = realm.copyFromRealm(result)
        }catch (t: Throwable){
            return Flowable.error(t)
        }finally {
            realm?.close()
        }
        if(outputItem == null)
            return Flowable.error(NoSuchElementException())
        return Flowable.just(outputItem)
    }

}
