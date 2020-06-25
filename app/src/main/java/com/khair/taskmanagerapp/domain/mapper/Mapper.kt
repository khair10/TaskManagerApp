package com.khair.taskmanagerapp.domain.mapper

interface Mapper<From, To> {

    fun map(item: From): To
}