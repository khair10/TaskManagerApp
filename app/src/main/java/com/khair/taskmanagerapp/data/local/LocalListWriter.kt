package com.khair.taskmanagerapp.data.local

import com.khair.taskmanagerapp.data.dto.TaskNet
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.functions.Function
import io.realm.Realm
import io.realm.RealmObject

class LocalListWriter(val mClass: Class<TaskNet> = TaskNet::class.java): Function<List<TaskNet>, Flowable<List<TaskNet>>> {

    override fun apply(t: List<TaskNet>?): Flowable<List<TaskNet>> {
        var realm: Realm? = null
        if (t == null)
            return Flowable.error(NoSuchElementException())
        try {
            realm = Realm.getDefaultInstance()
            for (item in t){
                val temp = realm.where(TaskNet::class.java).equalTo("id", item.id).findFirst()
                if (temp == null){
                    realm.beginTransaction()
                    realm.copyToRealm(item)
                    realm.commitTransaction()
                }
            }
        } catch (t: Throwable){
            return Flowable.error(t)
        } finally {
            realm?.close()
        }
        return Flowable.just(t)
    }
}