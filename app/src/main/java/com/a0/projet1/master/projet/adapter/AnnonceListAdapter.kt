package com.a0.projet1.master.projet.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.a0.projet1.master.projet.Interface.IItemClickListener
import com.a0.projet1.master.projet.Main2Activity
import com.a0.projet1.master.projet.Model.Intervention
import com.a0.projet1.master.projet.Model.Interventions
import com.a0.projet1.master.projet.R
import com.a0.projet1.master.projet.R.id.btn
import kotlinx.android.synthetic.main.annonce_item.view.*

class AnnonceListAdapter(internal var context: Context,
                         internal var annonceList:MutableList<Intervention>): RecyclerView.Adapter<AnnonceListAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var itemView = LayoutInflater.from(context).inflate(R.layout.annonce_item,parent,false)
        return MyViewHolder(itemView)
    }



    override fun getItemCount(): Int {
        return annonceList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.text_nom.text= annonceList[position].nom
        holder.text_type.text=annonceList[position].type
        holder.text_wilaya.text=annonceList[position].date_depot.toString()


        holder.setItemClickListener(object :IItemClickListener{

            override fun onclick(view: View, position: Int) {
                LocalBroadcastManager.getInstance(context)
                        .sendBroadcast(Intent(Interventions.KEY_ENABLE_HOME).putExtra("nom",annonceList[position].nom))
            }
        })

        holder.btn.setOnClickListener {
            val activity:Main2Activity = context as Main2Activity
           // LocalBroadcastManager.getInstance(context)
            Interventions.remove_annonce(Interventions.findAnnonceByTitre(annonceList[position].nom.toString())!!)
            Toast.makeText(context, "Click!", Toast.LENGTH_SHORT).show();
            activity.ll!!.recycler_view.adapter = activity.ll!!.adapter
            activity.write_file_json()
            // Toast.makeText(Activity(), "Click!", Toast.LENGTH_SHORT).show();

        }

    }
    fun test(view: View){

        Toast.makeText(Activity(), "Click!", Toast.LENGTH_SHORT).show();

    }

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        internal var text_wilaya: TextView
        internal var text_type: TextView
        internal var text_nom: TextView
        internal lateinit var btn : Button
        internal var itemClickListener: IItemClickListener?=null


        fun setItemClickListener(itemClickListener:IItemClickListener)
        {
            this.itemClickListener = itemClickListener
        }
        init {

            text_nom = itemView.findViewById(R.id.nom) as TextView
            text_type = itemView.findViewById(R.id.type) as TextView
            text_wilaya = itemView.findViewById(R.id.date) as TextView
            btn =  itemView.findViewById(R.id.btn) as Button
            itemView.setOnClickListener{view -> itemClickListener!!.onclick(view,adapterPosition)}

        }
    }
}

