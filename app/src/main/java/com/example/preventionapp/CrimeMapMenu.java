package com.example.preventionapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CrimeMapMenu extends Fragment {

    @Nullable
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.crimemap_menu, container, false);
        Button murder = view.findViewById(R.id.murder);
        Button rape = view.findViewById(R.id.rape);
        Button robbery=view.findViewById(R.id.robbery);
        Button larceny = view.findViewById(R.id.larceny);
        Button violence=view.findViewById(R.id.violence);
        murder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),CrimeMap_Murder.class);
                startActivity(intent);
            }
        });


        robbery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), CrimeMap_Robbery.class);
                startActivity(intent);
            }
        });

        rape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), CrimeMap_Rape.class);
                startActivity(intent);
            }
        });


        larceny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), CrimeMap_Larceny.class);
                startActivity(intent);
            }
        });

        violence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), CrimeMap_Violence.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
