package com.prikshitkumar.foodies.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.prikshitkumar.foodies.R
import com.prikshitkumar.foodies.adapters.FaqsAdapter
import com.prikshitkumar.foodies.adapters.HomeAdapter
import com.prikshitkumar.foodies.model.FaqsModel

class Faqs : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerViewAdapter: FaqsAdapter
    lateinit var progressBarLayout: RelativeLayout
    lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_faqs, container, false)
        initializeTheFields(view)
        val qnaList = arrayListOf<FaqsModel>(
                FaqsModel("Q1: Is this Application compatible with ios Phones?",
                        "Ans: No, this application works only on Android OS."),
                FaqsModel("Q2: Is there any loop hole for login, just want to know?",
                        "Ans: No, you have to register first and after that insert the credentials on login page. You will be logged " +
                                "in if the credentials are correct."),
                FaqsModel("Q3: What the steps you take for optimization of application?",
                        "Ans: There are some advance version of ViewGroups that I use for smooth execution of application. For " +
                                "example, instead of using ListView I am using RecyclerView for better results and because of recycler " +
                                "view, app size is not increased, etc... "),
                FaqsModel("Q4: From which country Developer is?",
                        "Ans: I am from India."),
                FaqsModel("Q5: When I am register myself and insert the pre-register mobile number and new email then also it shows " +
                        "you are already register! Why this prompt shows?",
                        "Ans: Yes, you have to add the credentials that are not in our database for security reasons. Means, Email " +
                                "and Phone Number must be unique."),
                FaqsModel("Q6: When I turn ON the Screen Rotation, then application UI is not rotated! Why?",
                        "Ans: Yes, we blocked the screen rotation feature because of different-different screen sizes. You can use " +
                                "this application only in Portrait mode yet, but after update we will try to add this feature also. Don't worry!"),
                FaqsModel("Q7: I like the feature of adding and removing the data from Favorites. Which Software you used for storing the data?",
                        "Ans: For this, I used SQLite and with the help of SQLite, we can store the data into local storage of Android " +
                                "Devices. SQLite is the database that developers used for adding and removing the data. For proper knowledge, read the " +
                                "documetation.")
        )

        recyclerViewAdapter = FaqsAdapter(
                activity as Context,    // "as" keyword is used for Type-Casting in Kotlin
                qnaList
        )
        progressBarLayout.visibility = View.GONE

        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = layoutManager

        return view
    }

    fun initializeTheFields(view: View) {
        recyclerView = view.findViewById(R.id.id_recyclerViewFAQs)
        layoutManager = LinearLayoutManager(activity as Context)
        progressBarLayout = view.findViewById(R.id.id_progressBarLayoutFAQs)
        progressBar = view.findViewById(R.id.id_progressBarFAQs)
        progressBarLayout.visibility = View.VISIBLE
    }
}