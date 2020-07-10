package com.example.carwaleassignment.model

import com.example.carwaleassignment.Type
import java.util.*

data class FilterModel(var operator:Type,var type: Type,var value:Int) {

    override fun equals(other: Any?): Boolean{
        if (this === other) return true
        if (other?.javaClass != javaClass) return false
        other as FilterModel
        if (!(type==other.type)){
            return false
        }
        return true
    }
}