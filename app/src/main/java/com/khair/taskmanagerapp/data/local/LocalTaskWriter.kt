package com.khair.taskmanagerapp.data.local

import com.khair.taskmanagerapp.data.dto.TaskNet
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.functions.Function
import io.realm.Realm

class LocalTaskWriter(val mClass: Class<TaskNet> = TaskNet::class.java): Function<TaskNet, Flowable<TaskNet>> {
    override fun apply(element: TaskNet?): Flowable<TaskNet> {
        var realm: Realm? = null
        if(element == null)
            return Flowable.error(NoSuchElementException())
        try {
            realm = Realm.getDefaultInstance()
            realm.executeTransaction {
                val temp: TaskNet? = it.where(mClass).equalTo("id", element.id).findFirst()
                if (temp == null)
                    it.copyToRealm(element)
            }
        } catch (t: Throwable){
            return Flowable.error<TaskNet>(t)
        } finally {
            realm?.close()
        }
        return Flowable.just(element)
    }
}