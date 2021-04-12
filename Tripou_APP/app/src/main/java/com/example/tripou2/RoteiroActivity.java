package com.example.tripou2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

public class RoteiroActivity extends AppCompatActivity {

    static int NEW_ITEM_REQUEST = 1;
    static int PLAY_SERVICES_RESOLUTION_REQUEST = 2;
    FusedLocationProviderClient fusedLocationProviderClient;
    Location myLocation;

    MyAdapterRoteiro myAdapterRoteiro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roteiro);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        RoteiroViewModel roteiroViewModel = new ViewModelProvider(this).get(RoteiroViewModel.class);

        Button btnCriarRoteiro = findViewById(R.id.btnCriarRoteiro);
        btnCriarRoteiro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ActivityCompat.checkSelfPermission(RoteiroActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(RoteiroActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(RoteiroActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            Spinner spTempoDisp = findViewById(R.id.spTempoDisp);
                            String tempodisp = spTempoDisp.getSelectedItem().toString();
                            roteiroViewModel.loadRoteiro(String.valueOf(location.getLatitude()),String.valueOf(location.getLongitude()), tempodisp);
                        }
                    }
                });
            }
        });

        RecyclerView rvRoteiro = findViewById(R.id.rvRoteiro);
        rvRoteiro.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvRoteiro.setLayoutManager(layoutManager);

        LiveData<List<PontoTuristico>> roteiro = roteiroViewModel.getRoteiro();
        roteiro.observe(RoteiroActivity.this, new Observer<List<PontoTuristico>>() {

            @Override
            public void onChanged(List<PontoTuristico> roteiro) {
                MyAdapterRoteiro adapterRoteiro = new MyAdapterRoteiro(RoteiroActivity.this, roteiro);
                rvRoteiro.setAdapter(adapterRoteiro);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }

    void checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if(resultCode != ConnectionResult.SUCCESS) {
            if(apiAvailability.isUserResolvableError(resultCode)) {
                Dialog errorDialog = apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST);
                errorDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        Toast.makeText(RoteiroActivity.this, "O seu celular não tem suporte ao Google Play Services", Toast.LENGTH_SHORT);
                        Button btnCriarRoteiro = findViewById(R.id.btnCriarRoteiro);
                        btnCriarRoteiro.setEnabled(false);
                    }
                });
            }
            else {
                Toast.makeText(RoteiroActivity.this, "O seu celular não tem suporte ao Google Play Services", Toast.LENGTH_SHORT);
                Button btnCriarRoteiro = findViewById(R.id.btnCriarRoteiro);
                btnCriarRoteiro.setEnabled(false);
            }
        }
    }
}