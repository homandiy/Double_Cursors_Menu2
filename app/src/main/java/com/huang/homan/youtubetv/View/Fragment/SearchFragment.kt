package com.huang.homan.youtubetv.View.Fragment

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.huang.homan.youtubetv.R
import kotlinx.android.synthetic.main.fragment_search.*


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [SearchFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment() {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ltag("onCreate()")
        // Inflate the layout for this fragment
        if (arguments != null) {
            mParam1 = arguments!!.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        ltag("onCreateView()")

        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ltag("onViewCreated()")

        pageContent.onFocusChangeListener = OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                pageContent.setTextColor(Color.BLACK)
            } else {
                pageContent.setTextColor(Color.MAGENTA)
            }
        }

        if (mParam1 != null) {
            ltag("mParam1: $mParam1")
            pageContent.setTextColor(Color.RED)

            val info = "$mParam1 Page: Left/Right to next menu option...\n"+
                    "Data: waiting... "
            pageContent.text = info
        }
    }

    companion object {

        /* Log tag and shortcut */
        private val TAG = "MYLOG " + SearchFragment::class.java.simpleName
        fun ltag(message: String) { Log.i(TAG, message) }

        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "search for"

        /**
         * @param searchString
         * @return A new instance of fragment SearchFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(searchString: String): SearchFragment {
            ltag("newInstance()")

            val fragment = SearchFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, searchString)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
