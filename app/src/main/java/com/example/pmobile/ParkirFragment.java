package com.example.pmobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ParkirFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ParkirFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    // TODO: Rename and change types of parameters
    private String nama;
    private String harga;
    private String id;

    public ParkirFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param id Parameter 1.
     * @param nama Parameter 2.
     * @param harga  Parameter 3.
     * @return A new instance of fragment ParkirFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ParkirFragment newInstance(String id, String nama,String harga) {
        ParkirFragment fragment = new ParkirFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, id);
        args.putString(ARG_PARAM2, nama);
        args.putString(ARG_PARAM3,harga);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // mengisi id, nama dan harga apabila argument tidak kosong
        if (getArguments() != null) {
            id = getArguments().getString(ARG_PARAM1);
            nama = getArguments().getString(ARG_PARAM2);
            harga = getArguments().getString(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_parkir, container, false);
        Button toInfo = view.findViewById(R.id.buttonInfoParkir);

        // intent untuk menuju ke InforamasiParkirActivity beserta mengirimkan id
        toInfo.setOnClickListener(v -> {
            Intent A_info = new Intent(getActivity().getApplicationContext(),InformasiParkirActivity.class);
            A_info.putExtra("park_id",this.id);
            startActivity(A_info);
        });

        // menampilkan nama  dan harga parkir
        TextView namaParkir = view.findViewById(R.id.tampilListParkir);
        TextView hargaParkir = view.findViewById(R.id.tampilHargaParkir);
        namaParkir.setText(this.nama);
        hargaParkir.setText("Rp "+this.harga+"-/jam");

        return view;
    }
}