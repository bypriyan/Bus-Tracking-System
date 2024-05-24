package com.bypriyan.bustrackingsystem.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import com.bypriyan.bustrackingsystem.R
import com.bypriyan.bustrackingsystem.model.ModelBusDetails
import com.bypriyan.bustrackingsystem.userActivity.UserMapTrackBusActivity
import com.bypriyan.bustrackingsystem.utility.Constants
import com.google.android.material.card.MaterialCardView

class AdapterBuses(private val context: Context, private val busList: List<ModelBusDetails>):
    RecyclerView.Adapter<AdapterBuses.HolderBuses>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderBuses {
        val view = LayoutInflater.from(context).inflate(R.layout.row_buses, parent, false)
        return HolderBuses(view)    }

    override fun onBindViewHolder(holder: HolderBuses, position: Int) {
        val modelBus = busList[position]

        var latit = modelBus.lat
        var longiti = modelBus.long
        var busNum = modelBus.vehicleNumber
        var name = modelBus.name
        var phoneNumber = modelBus.phoneNumber
        var uid = modelBus.uid

        holder.busNumTv.text = busNum
        holder.driverNameNumTv.text = "$name : $phoneNumber"
        holder.cardBtn.setOnClickListener{
            var intent = Intent(context, UserMapTrackBusActivity::class.java)
            intent.putExtra(Constants.KEY_UID, uid)
            intent.putExtra(Constants.KEY_VEHICLE_NUMBER,busNum)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return busList.size
    }

    inner class HolderBuses(itemView:View): RecyclerView.ViewHolder(itemView) {
        val cardBtn: MaterialCardView = itemView.findViewById(R.id.cardBtn)
        val busNumTv: TextView = itemView.findViewById(R.id.busNumTv)
        val driverNameNumTv: TextView = itemView.findViewById(R.id.driverNameNumTv)
    }
}