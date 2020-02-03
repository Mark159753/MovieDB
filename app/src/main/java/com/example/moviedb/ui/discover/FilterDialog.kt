package com.example.moviedb.ui.discover

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviedb.R
import com.example.moviedb.model.genre.Genre
import com.example.moviedb.ui.discover.adapter.expandable.GenreExpandableGroup
import com.example.moviedb.ui.discover.adapter.expandable.MultiCheckGenreAdapter
import com.thoughtbot.expandablecheckrecyclerview.listeners.OnCheckChildClickListener
import com.thoughtbot.expandablecheckrecyclerview.models.CheckedExpandableGroup
import com.thoughtbot.expandablerecyclerview.listeners.GroupExpandCollapseListener
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import java.util.*
import kotlin.collections.ArrayList


class FilterDialog:AppCompatDialogFragment(){

    private var genresList: List<Genre>? = null
    private lateinit var rootView:View
    private lateinit var mAdapter:MultiCheckGenreAdapter
    private var listener:FilterDialogListener? = null
    private val withGenre = ArrayList<Int>()
    private val withoutGenre = ArrayList<Int>()
    private var savedWithGenre:ArrayList<Int>? = null
    private var savedWithoutGenre:ArrayList<Int>? = null
    private var fromYear:String? = null
    private var toYear:String? = null
    private lateinit var inputYearFrom:EditText
    private lateinit var inputYearTo:EditText

    fun setListener(listener:FilterDialogListener){
        this.listener = listener
    }

    fun setChosenGanreList(withGenre: ArrayList<Int>, withoutGenre: ArrayList<Int>){
        savedWithGenre = withGenre
        savedWithoutGenre = withoutGenre
    }

    fun setGenderList(list:List<Genre>){
        this.genresList = list
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        savedInstanceState?.getIntegerArrayList("withGenre")?.let {
            withGenre.addAll(it)
        }
        savedInstanceState?.getIntegerArrayList("withoutGenre")?.let {
            withoutGenre.addAll(it)
        }
        val builder = AlertDialog.Builder(activity, R.style.AlertDialogTheme)

        val inflater = activity!!.layoutInflater
        rootView = inflater.inflate(R.layout.filter_dialog, null)

        builder.setView(rootView)
            .setTitle(resources.getString(R.string.discover_filter))
            .setNegativeButton(resources.getString(R.string.cancel)) { dialog, which ->
                withGenre.clear()
                withoutGenre.clear()
            }
            .setPositiveButton("ok"){dialog, which ->
                savedWithGenre?.clear()
                savedWithoutGenre?.clear()
                savedWithGenre?.addAll(withGenre)
                savedWithoutGenre?.addAll(withoutGenre)
                checkInvalidDate(inputYearFrom.text, inputYearTo.text)
                listener?.makeRequest(fromYear, toYear)
            }
        initExpandableList()
        initYearFilter()
        mAdapter.onRestoreInstanceState(savedInstanceState)
        return builder.create()
    }

    override fun onCancel(dialog: DialogInterface) {
        withGenre.clear()
        withoutGenre.clear()
        super.onCancel(dialog)
    }

    private fun initExpandableList(){
        val expandableListDetail = ArrayList<GenreExpandableGroup>().apply {
            add(GenreExpandableGroup(resources.getString(R.string.with_genre), genresList ?: emptyList()))
            add(GenreExpandableGroup(resources.getString(R.string.without_genre), genresList ?: emptyList()))
        }
        val genresListRecycler = rootView.findViewById<RecyclerView>(R.id.genres_list)
        mAdapter = MultiCheckGenreAdapter(expandableListDetail)

        showSaveSelectedGenres()

        genresListRecycler.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
        }
        mAdapter.setChildClickListener(object :OnCheckChildClickListener{
            override fun onCheckChildCLick(
                v: View?,
                checked: Boolean,
                group: CheckedExpandableGroup?,
                childIndex: Int
            ) {
                when(group!!.title){
                    resources.getString(R.string.with_genre) -> {
                        if (checked) withGenre.add(genresList!![childIndex].id) else
                            withGenre.remove(genresList!![childIndex].id)
                    }
                    resources.getString(R.string.without_genre) -> {
                        if (checked) withoutGenre.add(genresList!![childIndex].id) else
                            withoutGenre.remove(genresList!![childIndex].id)
                    }
                }

            }
        })
        mAdapter.setOnGroupExpandCollapseListener(object :GroupExpandCollapseListener{
            override fun onGroupCollapsed(group: ExpandableGroup<*>?) {
                val param = genresListRecycler.layoutParams
                param.height = ViewGroup.LayoutParams.WRAP_CONTENT
                genresListRecycler.layoutParams = param
            }

            override fun onGroupExpanded(group: ExpandableGroup<*>?) {
                val param = genresListRecycler.layoutParams
                param.height = ViewGroup.LayoutParams.WRAP_CONTENT
                genresListRecycler.layoutParams = param
            }
        })
    }

    fun setRestoreGenreList(withGenre: ArrayList<Int>?, withoutGenre: ArrayList<Int>?){
        withGenre?.let { this.savedWithGenre = withGenre; this.withGenre.addAll(it) }
        withoutGenre?.let { this.savedWithoutGenre = withoutGenre; this.withoutGenre.addAll(it) }
    }

    private fun showSaveSelectedGenres(){
        if (withGenre.isNotEmpty()){
            for (i in withGenre) {
                genresList?.let {
                    for (genre in it.indices){
                        if (it[genre].id == i) mAdapter.checkChild(true, 0, genre)
                    }
                }
            }
        }
        if (withoutGenre.isNotEmpty()){
            for (i in withoutGenre) {
                genresList?.let {
                    for (genre in it.indices){
                        if (it[genre].id == i) mAdapter.checkChild(true, 1, genre)
                    }
                }
            }
        }
    }

    private fun initYearFilter(){
        inputYearFrom = rootView.findViewById<EditText>(R.id.text_input_from)
        inputYearTo = rootView.findViewById<EditText>(R.id.text_input_to)

        fromYear?.let { inputYearFrom.setText(it.subSequence(0, 4)) }
        toYear?.let { inputYearTo.setText(it.subSequence(0, 4)) }

        inputYearTo.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_ENTER){
                val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(activity!!.window.decorView.rootView.windowToken, 0)
                inputYearTo.isFocusable = false
                inputYearTo.isFocusableInTouchMode = true
                return@setOnKeyListener true
            }else
                return@setOnKeyListener false
        }

        inputYearFrom.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_ENTER){
                val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(activity!!.window.decorView.rootView.windowToken, 0)
                inputYearFrom.isFocusable = false
                inputYearFrom.isFocusableInTouchMode = true
                return@setOnKeyListener true
            }else
                return@setOnKeyListener false
        }
    }

    fun setDate(from:String?, to:String?){
        fromYear = from
        toYear = to
    }

    @SuppressLint("SimpleDateFormat")
    private fun checkInvalidDate(yearFromText:Editable, yearToText:Editable){
        val yearFrom = if (yearFromText.toString() != "") yearFromText.toString().toInt() else null
        val yearTo = if (yearToText.toString() != "") yearToText.toString().toInt() else null
        var from = 0
        var to = 0
        yearFrom?.let {
            from = if (it in 1900..9999) it else 1900
            fromYear = if (from <= to || to == 0) "$from-01-01" else "$to-01-01"
        }
        yearTo?.let {
            to = if (it in 1900..9999) it  else Calendar.getInstance().get(Calendar.YEAR)
            toYear = if (to >= from) "$to-12-31" else "$from-12-31"
        }

    }

    interface FilterDialogListener{
        fun makeRequest(dateFrom:String? = null, dateTo:String? = null)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putIntegerArrayList("withGenre", withGenre)
        outState.putIntegerArrayList("withoutGenre", withoutGenre)
        mAdapter.onSaveInstanceState(outState)
    }

}