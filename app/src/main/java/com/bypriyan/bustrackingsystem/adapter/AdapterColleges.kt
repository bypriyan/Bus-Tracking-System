package com.bypriyan.bustrackingsystem.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.bypriyan.bustrackingsystem.R
import com.bypriyan.bustrackingsystem.model.ModelBusDetails
import com.bypriyan.bustrackingsystem.model.ModelColleges
import com.bypriyan.bustrackingsystem.register.SelectActivity
import com.bypriyan.bustrackingsystem.userActivity.UserMapTrackBusActivity
import com.bypriyan.bustrackingsystem.utility.Constants
import com.google.android.material.card.MaterialCardView

class AdapterColleges(private val context: Context, private val collegeList: List<ModelColleges>):
    RecyclerView.Adapter<AdapterColleges.HolderColleges>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderColleges {
        val view = LayoutInflater.from(context).inflate(R.layout.row_colleges, parent, false)
        return HolderColleges(view)
    }

    override fun onBindViewHolder(holder: HolderColleges, position: Int) {
        val modelCollege = collegeList[position]

        var cName = modelCollege.name
        var cImgUrl = modelCollege.imgUrl

        holder.collegeNameTv.text = cName
        holder.collegeImg.load(cImgUrl){
            crossfade(true)
            placeholder(R.drawable.logo)
        }
        holder.collegeCardView.setOnClickListener{
            var intent = Intent(context, SelectActivity::class.java)
            intent.putExtra(Constants.KEY_COLLEGE_NAME, cName)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return collegeList.size
    }

    inner class HolderColleges(itemView:View): RecyclerView.ViewHolder(itemView) {
        val collegeImg: ImageView = itemView.findViewById(R.id.collegeImg)
        val collegeNameTv: TextView = itemView.findViewById(R.id.collegeNameTv)
        val collegeCardView: MaterialCardView = itemView.findViewById(R.id.collegeCardView)
    }
}