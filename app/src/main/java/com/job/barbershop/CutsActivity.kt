package com.job.barbershop

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.job.barbershop.ChooseLocationActivity.Companion.db
import com.job.barbershop.ChooseLocationActivity.Companion.id
import com.job.barbershop.model.CutService
import kotlinx.android.synthetic.main.activity_cuts.*

class CutsActivity : BaseActivity() {

    private var cutServices = mutableListOf<CutService>()

    companion object {
        fun newIntent(context: Context): Intent =
            Intent(context, CutsActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cuts)

        ex1.setOnClickListener {
            toggleSection(ex1, l1)
        }

        ex2.setOnClickListener {
            toggleSection(ex2, l2)
        }

        ex3.setOnClickListener {
            toggleSection(ex3, l3)
        }

        Log.d(TAG,"==== ${tm.pickedLocation}")
        textView7.text = tm.pickedLocation
    }


    private fun toggleSection(toggleBtn: View, lyt_expand_input: View) {
        val show = toggleArrow(toggleBtn)
        Log.d("#Cuts","$show")
        if (show) {
            lyt_expand_input.visibility = View.VISIBLE
        } else {
            //ViewAnimation.collapse(lyt_expand_input)
            lyt_expand_input.visibility = View.GONE
        }
        //body.invalidate()
    }

    private fun toggleArrow(view: View): Boolean {
        return if (view.rotation == 0f) {
            view.animate().setDuration(200).rotation(180f)
            true
        } else {
            view.animate().setDuration(200).rotation(0f)
            false
        }
    }

    fun toSelectService(v: View) {

        if (cutServices.size > 0 && cutServices.size < 1){
            showSnack("Select at least two services!")
            return
        }

        tm.cutService1 = cutServices[0]
        tm.cutService2 = cutServices[1]

        val service1 = hashMapOf(
            "userId" to id,
            "name" to tm.myDetails?.name,
            "name" to tm.cutService1?.name,
            "price" to tm.cutService1?.price,
            "time" to tm.selectedTime,
            "date" to tm.selectedDate
        )

        val service2 = hashMapOf(
            "userId" to id,
            "userName" to tm.myDetails?.name,
            "name" to tm.cutService2?.name,
            "price" to tm.cutService2?.price,
            "time" to tm.selectedTime,
            "date" to tm.selectedDate
        )

        db.collection("FirstCutService").document(id).set(service1)
        db.collection("SecondCutService").document(id).set(service2)

        val intent = Intent(this, SummaryActivity::class.java)
        intent.putParcelableArrayListExtra("sss", cutServices as ArrayList)
        startActivity(intent)
    }


    fun toLocation(v: View) {
        startActivity(Intent(this, ChooseLocationActivity::class.java))
    }

    fun addClick(v:View){

        val size = cutServices.size
        if (size > 1){
            showSnack("Only allowed to pick two services !")
            return
        }

        when(v.id){
            R.id.gentlemancut -> {
                cutServices.add(CutService(name = "Gentlemen's cut",price = 500,time = "1 Hr"))
                Log.d(TAG, "===> ${v.id} = ${R.id.gentlemancut}")
            }

            R.id.mustache -> {
                cutServices.add(CutService(name = "Beard/ Mustache Trim",price = 350,time = "1 Hr"))
                Log.d(TAG, "===> ${v.id} = ${R.id.gentlemancut}")
            }

            R.id.gentlemantouch -> cutServices.add(CutService(name = "Gentlemen's touch",price = 300,time = "1 Hr"))

            R.id.colourshade -> cutServices.add(CutService(name = "Colour Shading",price = 500,time = "1 Hr"))

            R.id.pedi -> cutServices.add(CutService(name = "Lux Pedicure",price = 700,time = "1 Hr"))

            R.id.mani -> cutServices.add(CutService(name = "Lux Manicure",price = 200,time = "1 Hr"))

        }

        next.text = "CONTINUE - ADDED (${cutServices.size})"
    }

}
