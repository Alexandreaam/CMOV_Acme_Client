package com.acme_industries.acmecaf.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.acme_industries.acmecaf.MainActivityPage
import com.acme_industries.acmecaf.R
import com.acme_industries.acmecaf.RecyclerAdapter
import com.acme_industries.acmecaf.core.Product
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.fragment_home.*
import org.json.JSONArray

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        layoutManager = LinearLayoutManager(context)
        recycler_view.layoutManager = layoutManager

        val url = "http://10.0.2.2:3000/menu"

        val queue = Volley.newRequestQueue(context)
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null ,
                { response ->
                    // Display the first 500 characters of the response string.
                    println("Response is: $response")
                    val products: ArrayList<Product> = ArrayList()

                    val prodlist = response.getJSONArray("Products")
                    println(prodlist)
                    for (it in 0 until prodlist.length()){
                        val prod = prodlist.getJSONObject(it)
                        products.add(Product(prod.get("title") as String,
                                            prod.get("details") as String,
                                            prod.get("price") as String,
                                            prod.get("image") as String))
                    }


                    adapter = RecyclerAdapter(products)
                    recycler_view.adapter = adapter
                },
                { error ->
                    println("That didn't work: $error")
                })

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.home_title)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        return root

    }

}