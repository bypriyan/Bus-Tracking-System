package com.bypriyan.bustrackingsystem.model

data class ModelBusDetails(val lat:String, val long:String, val name:String, val phoneNumber:String, val uid:String, val vehicleNumber:String){

    constructor():this("","","","","","")
}
