package com.khair.taskmanagerapp.data.local

import com.khair.taskmanagerapp.data.dto.TaskNet
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.functions.Function
import io.realm.Realm
import io.realm.RealmObject

class SingleLocalWriter<T: RealmObject>(val mClass: Class<T>): Function<T, Completable> {

    override fun apply(t: T): Completable {
        var realm: Realm? = null
        try {
            realm = Realm.getDefaultInstance()
            realm.executeTransaction {
                val maxValue: Number? = it.where(mClass).max("id")
                val pk = if (maxValue != null) maxValue.toLong() + 1 else 0.toLong()
                val taskNet: T? =
                    it.where(mClass).equalTo("dateStart", (t as TaskNet).dateStart)
                        .equalTo("dateFinish", (t as TaskNet).dateFinish)
                        .findFirst()
                if (taskNet != null)
                    throw AlreadyHaveTaskException()
                (t as TaskNet).id = pk
                it.insert(t)
            }
        } catch (e: AlreadyHaveTaskException){
            return Completable.error(e)
        } catch (e: Exception) {
            return Completable.error(e)
        } finally {
            realm?.close()
        }
        return Completable.complete()
    }
}