package com.example.loginregister;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.loginregister.adapter.GenericShoesAdapter;
import com.example.loginregister.model.GenericShoes;
import com.example.loginregister.repository.GenericShoesRepository;
import com.example.loginregister.service.GenericShoesService;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    GenericShoesService genericShoesService;
    ArrayList<GenericShoes> listGenericShoes;
    ListView listViewGenericShoes;
    GenericShoesAdapter genericShoesAdapter;
    SearchView searchView;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    //ActivityMainBinding binding;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        genericShoesService = (GenericShoesService) GenericShoesRepository.getService();

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);


        // Initialize the SearchView
        searchView = rootView.findViewById(R.id.search_view);
        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();
        view();
        searchView = getActivity().findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Call the API to search for GenericShoes with the given name
                Call<GenericShoes[]> call = genericShoesService.getAllGenericShoesByName(query);
                call.enqueue(new Callback<GenericShoes[]>() {
                    @Override
                    public void onResponse(Call<GenericShoes[]> call, Response<GenericShoes[]> response) {

                        if (response.isSuccessful()) {
                            GenericShoes[] genericShoes = response.body();
                            if (genericShoes == null || genericShoes.length == 0) {
                                setGenericShoesAdapter(genericShoes);
                                Toast.makeText(getActivity(), "No record", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            setGenericShoesAdapter(genericShoes);
                            Toast.makeText(getActivity(), "Get latest data successfully", Toast.LENGTH_SHORT).show();
                            // Update the list with the new data
                        } else {
                            Toast.makeText(getActivity(), "Eror in reponse.isSuccessful()", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<GenericShoes[]> call, Throwable t) {
                        // Handle the error
                    }
                });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Do nothing
                return false;
            }
        });

    }

    private void view() {
        try {
            Call<GenericShoes[]> call = genericShoesService.getAllGenericShoes(); //call service
            call.enqueue(new Callback<GenericShoes[]>() {
                @Override
                public void onResponse(Call<GenericShoes[]> call, Response<GenericShoes[]> response) {
                    GenericShoes[] genericShoes = response.body();
                    if (genericShoes == null || genericShoes.length == 0) {
                        setGenericShoesAdapter(genericShoes);
                        Toast.makeText(getActivity(), "No record", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    setGenericShoesAdapter(genericShoes);
                    Toast.makeText(getActivity(), "Get latest data successfully", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<GenericShoes[]> call, Throwable t) {
                }
            });
        } catch (Exception e) {
            Log.d("Error on view :", e.getMessage());
        }
    }






    //    private void view() {
//        try {
//            Call<Category[]> call = categoryService.getAllCategories(); //call service
//            call.enqueue(new Callback<Category[]>() {
//                @Override
//                public void onResponse(Call<Category[]> call, Response<Category[]> response) {
//                    Category[] categories = response.body();
//                    if (categories == null || categories.length == 0) {
//                        setCategoryAdapter(categories);
//                        Toast.makeText(getActivity(), "No record", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                    setCategoryAdapter(categories);
//                    Toast.makeText(getActivity(), "Get latest data successfully", Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void onFailure(Call<Category[]> call, Throwable t) {
//                }
//            });
//        } catch (Exception e) {
//            Log.d("Error on view :", e.getMessage());
//        }
//    }

    private void setGenericShoesAdapter(GenericShoes[] genericShoes) {
        listGenericShoes = new ArrayList<>();
        Collections.addAll(listGenericShoes, genericShoes);
        listViewGenericShoes = getView().findViewById(R.id.listTrainee); // Update to use getView() instead of findViewById()
        genericShoesAdapter = new GenericShoesAdapter(getContext(), listViewGenericShoes.getId(), listGenericShoes); // Update to use getContext() instead of MainActivity.this
        listViewGenericShoes.setAdapter((ListAdapter) genericShoesAdapter);
    }


}