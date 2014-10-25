package com.codepath.snyteam7.crossroads.fragments;

import com.codepath.snyteam7.crossroads.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HowtoPage3Fragment extends Fragment {
    // The argument key for the page number this fragment represents.
   public static final String ARG_PAGE = "page";
    // The fragment's page number, which is set to the argument value for {@link #ARG_PAGE}.
   private int mPageNumber;

   // Factory method for this fragment class. Constructs a new fragment for the given page number.
   public static HowtoPage3Fragment create(int pageNumber) {
       HowtoPage3Fragment fragment = new HowtoPage3Fragment();
       Bundle args = new Bundle();
       args.putInt(ARG_PAGE, pageNumber);
       fragment.setArguments(args);
       return fragment;
   }

   public HowtoPage3Fragment() {
   }

   @Override
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       mPageNumber = getArguments().getInt(ARG_PAGE);
   }

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
           Bundle savedInstanceState) {
       // Inflate the layout
       ViewGroup rootView = (ViewGroup) inflater
               .inflate(R.layout.fragment_howto3, container, false);

       // Set the title view to show the page number.
       //((TextView) rootView.findViewById(android.R.id.text1)).setText("Step" + mPageNumber + 1);

       return rootView;
   }

    // Returns the page number represented by this fragment object.
   public int getPageNumber() {
       return mPageNumber;
   }

}
