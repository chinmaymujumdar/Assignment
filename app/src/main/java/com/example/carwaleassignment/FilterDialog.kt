package com.example.carwaleassignment

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import com.example.carwaleassignment.viewmodel.CasesViewModel
import java.lang.Exception


class FilterDialog {

    companion object {
        fun showAlertDialog(context: Context,viewModel: CasesViewModel?,type: Type) {
            val builder = androidx.appcompat.app.AlertDialog.Builder(context)
            val view = LayoutInflater.from(context).inflate(R.layout.filter_dialog_layout, null)
            builder.setView(view)
            val editText=view.findViewById<EditText>(R.id.comparison_value)
            val radioGroup=view.findViewById<RadioGroup>(R.id.radio_group)
            builder.setPositiveButton("Ok", object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    try {
                        if (editText.text.isEmpty()) {
                            Toast.makeText(context, "Value cannot be empty", Toast.LENGTH_SHORT).show()
                        } else {
                            val selctedId = radioGroup.checkedRadioButtonId
                            if (selctedId == R.id.less) {
                                viewModel?.addLessToMap(type, editText.text.toString().toInt())
                            } else {
                                viewModel?.addGreaterToMap(type, editText.text.toString().toInt())
                            }
                        }
                    }catch (e:Exception){
                        Toast.makeText(context, "Some error occured", Toast.LENGTH_SHORT).show()
                    }
                }

            })

            builder.setNegativeButton("Cancel", object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {

                }

            })
            val dialog = builder.create()
            dialog.show()
        }
    }

}