package edu.upi.cs.yudiwbs.uas_template;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;

import java.util.ArrayList;

import edu.upi.cs.yudiwbs.uas_template.databinding.FragmentSatuBinding;


public class FragmentSatu extends Fragment {

    private FragmentSatuBinding binding;
    private SharedPreferences preferences;
    private SensorManager sm;
    private Sensor senAccel;


    ArrayList<Hasil> alHasil =new ArrayList<>();
    AdapterHasil adapter;
    RecyclerView.LayoutManager lm;

    public FragmentSatu() {
        // Required empty public constructor
    }

    public static FragmentSatu newInstance(String param1, String param2) {
        FragmentSatu fragment = new FragmentSatu();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        preferences = getActivity().getSharedPreferences(
                "edu.upi.cs.yudiwbs.uas_template", Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSatuBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        adapter = new AdapterHasil(alHasil);
        binding.rvHasilFrag1.setAdapter(adapter);

        lm = new LinearLayoutManager(getActivity());
        binding.rvHasilFrag1.setLayoutManager(lm);

        binding.rvHasilFrag1.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

        sm = (SensorManager)    getActivity().getSystemService(getActivity().getApplicationContext().SENSOR_SERVICE);
        senAccel = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (senAccel != null){
            Log.d("debugyudi","Sukses!");
        }
        else {
            Log.d("debugyudi","Tidak ada sensor");
        }

        binding.buttonFrag1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("debugyudi","onclick");
                ApiIpfy.get("/?format=json", null, new JsonHttpResponseHandler() {


                    @Override
                    //hati2 success jsonobjek atau jsonarray
                    public void onSuccess(int statusCode,
                                          cz.msebera.android.httpclient.Header[] headers,
                                          org.json.JSONObject response) {
                        Log.d("debugyudi","onSuccess jsonobjek");

                        String rate1 ="";

                        try {
                            rate1 = (String) response.get("ip");

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("debugyudi", "msg error" +":" +e.getMessage());
                        }
                        alHasil.add(new Hasil(rate1));
                        adapter.notifyDataSetChanged();
                        Log.d("debugyudi", "rate" +":" +rate1);
                    }
                    @Override
                    public  void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String err, Throwable throwable)  {
                        Log.e("debugyudi", "error " + ":" + statusCode +":"+ err);
                    }
                });

            }
        });

        return view;

    }


}



