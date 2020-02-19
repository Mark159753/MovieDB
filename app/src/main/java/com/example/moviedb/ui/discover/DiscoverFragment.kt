package com.example.moviedb.ui.discover

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.lifecycle.Observer
import com.example.moviedb.MovieApp

import com.example.moviedb.R
import com.example.moviedb.di.ViewModelFactoryDI
import com.example.moviedb.model.discover.DiscoverResult
import com.example.moviedb.ui.MainActivity
import com.example.moviedb.ui.discover.adapter.DiscoverListAdapter
import com.example.moviedb.ui.discover.adapter.DiscoverMarginDecoarator
import com.example.moviedb.until.Listening
import com.example.moviedb.until.LocaleHelper
import kotlinx.android.synthetic.main.discover_fragment.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class DiscoverFragment : Fragment(), FilterDialog.FilterDialogListener{

    private lateinit var mAdapter:DiscoverListAdapter
    private var withGenre: ArrayList<Int> = ArrayList<Int>()
    private var withoutGenre: ArrayList<Int> =  ArrayList<Int>()
    private lateinit var dialog:FilterDialog
    private var sortParam:String = "popularity.desc"
    private var dateFrom:String? = null
    private var dateTo:String? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactoryDI
    private lateinit var viewModel: DiscoverViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.discover_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        ((activity!! as MainActivity).application as MovieApp)
            .appComponent
            .getFragmentComponent()
            .create(activity!!)
            .inject(this)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(DiscoverViewModel::class.java)

        dateFrom = savedInstanceState?.getString("saveDateFrom")
        dateTo = savedInstanceState?.getString("saveDateTo")

        initSortSpinner()
        initFilter()
        initDiscoverList()
        safeGenreList(savedInstanceState)
    }


    private fun initSortSpinner(){
        val sortValue = resources.getStringArray(R.array.discover_command_sort)
        discover_sort_spinner.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                sortParam = sortValue[position]
                val res = viewModel.getDiscoveryList(sort_by = sortParam,
                    without_genres = withoutGenre,
                    with_genres = withGenre,
                    primary_release_date_from = dateFrom,
                    primary_release_date_to = dateTo,
                    language = LocaleHelper.getLanguage(activity!!))
                updateDiscoverListData(res)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Log.e("NOTHING","SELECTED")
            }
        }
    }

    private fun initDiscoverList(){
        mAdapter = DiscoverListAdapter()
        discover_list.apply {
            adapter = mAdapter
            setHasFixedSize(true)
            addItemDecoration(DiscoverMarginDecoarator(15,15,15))
        }
    }

    private fun updateDiscoverListData(res:Listening<DiscoverResult>){
        res.pagedList.observe(viewLifecycleOwner, Observer {
            mAdapter.submitList(it)
        })
        res.networkState.observe(viewLifecycleOwner, Observer {
            mAdapter.setNetworkState(it)
        })
    }

    private fun initFilter(){
        dialog = FilterDialog()
        dialog.setListener(this)
        dialog.setChosenGanreList(this.withGenre, this.withoutGenre)
        viewModel.getGenres().observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer
            dialog.setGenderList(it)

            discover_filter_btn.setOnClickListener {
                dialog.setDate(dateFrom, dateTo)
                dialog.show(activity!!.supportFragmentManager, "filter_dialog")
            }
        })
    }

    private fun safeGenreList(savedInstanceState: Bundle?){
        val tempWthGenre = savedInstanceState?.getIntegerArrayList("savedWithGenre")
        val tempWithoutGenre = savedInstanceState?.getIntegerArrayList("saveWithoutGenre")
        tempWthGenre?.let { withGenre.addAll(it) }
        tempWithoutGenre?.let { withoutGenre.addAll(it) }
        if (tempWthGenre != null || tempWithoutGenre != null) {
            dialog.setRestoreGenreList(withGenre, withoutGenre)
        }
    }

        override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putIntegerArrayList("savedWithGenre", withGenre)
        outState.putIntegerArrayList("saveWithoutGenre", withoutGenre)
        outState.putString("saveDateFrom", dateFrom)
        outState.putString("saveDateTo", dateTo)
    }

    override fun makeRequest(
        dateFrom: String?,
        dateTo: String?
    ) {
        this.dateFrom = dateFrom
        this.dateTo = dateTo
        val res = viewModel.getDiscoveryList(sort_by = sortParam,
            with_genres = this.withGenre,
            without_genres = this.withoutGenre,
            primary_release_date_from = dateFrom,
            primary_release_date_to = dateTo,
            language = LocaleHelper.getLanguage(activity!!))
        updateDiscoverListData(res)
    }
}
