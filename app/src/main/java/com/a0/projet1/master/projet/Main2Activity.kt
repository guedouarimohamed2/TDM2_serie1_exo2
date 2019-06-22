package com.a0.projet1.master.projet

import android.app.DatePickerDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.a0.projet1.master.projet.Model.Intervention
import com.a0.projet1.master.projet.Model.Interventions
import com.a0.projet1.master.projet.R.id.date
import com.a0.projet1.master.projet.adapter.AnnonceListAdapter
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.app_bar_main2.*
import kotlinx.android.synthetic.main.fragment_ajouter_form.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class Main2Activity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
////////////////////////////////////////////////////////////////////////////////////////////////////////////
private val showDetail = object : BroadcastReceiver(){
    override fun onReceive(p0: Context?, intent: Intent?) {
        if (intent!!.action!!.toString() == Interventions.KEY_ENABLE_HOME)
        {
   //         supportActionBar!!.setDisplayHomeAsUpEnabled(true)
   //         supportActionBar!!.setDisplayShowHomeEnabled(true)
            //replace fragment
            val detailFragment = InterventionDetaille.getInstance()
            val num = intent.getStringExtra("nom")
            val bundle = Bundle()
            bundle.putString("nom",num)
            detailFragment.arguments = bundle

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment,detailFragment)
            fragmentTransaction.addToBackStack("detail")
            fragmentTransaction.commit()
        }
    }
}



var arr = arrayListOf<String>()
    val manager = supportFragmentManager
    var images: MutableList<Uri>? = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out)

        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    1)
        }


        setSupportActionBar(toolbar)



        fab.setOnClickListener { view ->
            loadFrag2(AjouterForm())
            images!!.clear()
        }

        //////////////////////////////////////////////////////////////
        toolbar.setTitle("Annonces plombiers")
        setSupportActionBar(toolbar)
        loadFrag1(ll!!)
     //   Annonces.initial()
        intial_file_json()
        read_file_json()

/////////////////////////////////////////////////////////

if(btn_date !=null) {
    btn_date.setOnClickListener {

    }
}

        ////////////////////////////////////////////////////


        LocalBroadcastManager.getInstance(this)
                .registerReceiver(showDetail, IntentFilter(Interventions.KEY_ENABLE_HOME))

        ////////////////////////////////////////////////////////

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

    }
    fun write_file_json(){
        var jsn = JSONArray()
        for(a in Interventions.ans){
            jsn.put(JSONObject().put("num",a.num).put("nom",a.nom).put("type",a.type).put("date",a.date_depot))
        }
        var output:Writer
        var fileName = "file"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        if(!storageDir.exists()){
            storageDir.mkdir()
        }
        var file = File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),"file.json")
        output = BufferedWriter(FileWriter(file)as Writer)
        output.write(jsn.toString())
        output.close()
    }


    fun intial_file_json(){
        var jsn = JSONArray()
        for(a in Interventions.ans){
            jsn.put(JSONObject().put("num",a.num).put("nom",a.nom).put("type",a.type).put("date",a.date_depot))
        }
        jsn.put(JSONObject().put("num","1").put("nom","mohamed").put("type","type2").put("date","2019-06-25"))
        jsn.put(JSONObject().put("num","2").put("nom","hamid").put("type","type3").put("date","2019-07-05"))
        jsn.put(JSONObject().put("num","3").put("nom","sofiane").put("type","type4").put("date","2019-06-29"))
        jsn.put(JSONObject().put("num","4").put("nom","yacine").put("type","type2").put("date","2019-09-15"))
        jsn.put(JSONObject().put("num","5").put("nom","mokranr").put("type","type5").put("date","2019-08-12"))
        jsn.put(JSONObject().put("num","6").put("nom","kadour").put("type","type2").put("date","2019-07-21"))

        var output:Writer
        var fileName = "file"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        if(!storageDir.exists()){
            storageDir.mkdir()
        }
        var file = File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),"file.json")
        output = BufferedWriter(FileWriter(file))
        output.write(jsn.toString())
        output.close()
    }

    fun read_file_json(){
        val stream = FileInputStream(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString()+"/file.json")
        val Text = stream.bufferedReader().use(BufferedReader::readText)
        var jsonarr = JSONArray(Text)
        for(i in 0..jsonarr.length()-1){
            var jsonobj = jsonarr.getJSONObject(i)
            var a:Intervention=Intervention(jsonobj.getString("num").toInt(),jsonobj.getString("nom").toString(), jsonobj.getString("type").toString(),jsonobj.getString("date").toString())
            Interventions.add_annonce(a)
        }
    }
    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main2, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }
    var ll:ListIntervention? = ListIntervention()
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                loadFrag1(ll!!)
            }
            R.id.nav_gallery -> {
                loadFrag2(AjouterForm())
                images!!.clear()
            }
            R.id.tri_date_depot -> {
                var sortedList = Interventions.ans.sortedByDescending {it.date_depot}
                Interventions.ans.clear()
                for (obj in sortedList) {
                    Interventions.ans.add(obj)
                }
                ll!!.adapter = AnnonceListAdapter(ll!!.activity!!,Interventions.ans)
                ll!!.recycler_view.adapter = ll!!.adapter
            }
            R.id.rech_date ->{

                ll!!.last_suggest.clear()
                for (a in Interventions.ans)
                    ll!!.last_suggest.add(a.date_depot!!)

                ll!!.search_bar.visibility = View.VISIBLE
                ll!!.search_bar.lastSuggestions = ll!!.last_suggest
                ll!!.search_bar.setHint("Entrer Nom")
            }


        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
    internal lateinit var btn: Button

    fun test(){
        ll!!.recycler_view.adapter = ll!!.adapter
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    fun show_list_annonce(view : View){
        loadFrag1(ListIntervention())
    }

    fun show_ajouter_form(view : View){
        loadFrag2(AjouterForm())
        images!!.clear()
    }

    private fun loadFrag1(f1:ListIntervention){

        val ft = manager.beginTransaction()
        ft.replace(R.id.fragment,f1)


        ft.addToBackStack(null)
        ft.commit()
    }

    private fun loadFrag2(f2:AjouterForm){

        val ft = manager.beginTransaction()
        ft.replace(R.id.fragment,f2)
        ft.addToBackStack(null)
        ft.commit()
    }

    companion object {
        private val IMAGE_PICK_CODE = 1000
        private val REQUEST_PICK_PHOTO = 1
    }


    val c=Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONDAY)
    val day = c.get(Calendar.DAY_OF_MONTH)
    fun pick_date(view: View){
        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
            var formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            val d = mDay.toString()+"-"+mMonth.toString()+"-"+mYear.toString()
            var date = LocalDate.of(mYear,mMonth, mDay)


            datee.text =date.toString()

        }, year, month, day)
        dpd.show()

        datee.text =date.toString()
    }


    fun ajouter(view: View){
        val nom: EditText =findViewById(R.id.input_nom)
        val type: EditText =findViewById(R.id.input_type)
        val date: TextView =findViewById(R.id.datee)





        val t =  System.currentTimeMillis();
        Log.i("TAG", "SERIAL: " + t);
        var a1 = Intervention(Interventions.nb_intervention!! + 1,nom.text.toString(),type.text.toString(),datee.text.toString())
        Interventions.ans.add(a1)
        loadFrag1(ll!!)
        nom.setText("")
        type.setText("")
      //  date.setText("")
        write_file_json()
    }

}
