package com.project.meetingapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.meetingapp.AESHelper;
import com.project.meetingapp.R;
import com.project.meetingapp.adapters.GridviewAdapter;
import com.project.meetingapp.utilities.Constants;
import com.project.meetingapp.utilities.PreferenceManager;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    String drawerImageUrl, drawerUserName, drawerStatus ;
    private PreferenceManager preferenceManager;
    private GridviewAdapter mAdapter;
    private ArrayList<String> listCountry;
    private ArrayList<Integer> listFlag;

    private GridView gridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //encryption and decryption
        String seedValue = "1234567887654321";
        String strNormalText = "Md Rofiqul Islam";

        SecretKey secretKey = null;
        try {
            secretKey = AESHelper.generateKey(seedValue);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        String strResult= null;
        try {
            strResult = AESHelper.encryptMsg(strNormalText,secretKey);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidParameterSpecException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.d("encryption",strResult);

        SecretKey secretKey2 = null;
        try {
            secretKey2 = AESHelper.generateKey(seedValue);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        String strResult2 = null;
        try {
            strResult2 = AESHelper.decryptMsg(strResult,secretKey2);
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.d("decryption",strResult2);


        //
        preferenceManager = new PreferenceManager(getApplicationContext());

        Toolbar toolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("HMS");

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.header);

        //=======   drawer_header
        View headerview = navigationView.getHeaderView(0);
        ImageView drawerImage = headerview.findViewById(R.id.drawer_image);
        TextView drawerUserTV = (TextView) headerview.findViewById(R.id.drawer_userName);

        Log.d("user","name1: "+drawerUserName+" "+ drawerStatus) ;
        drawerUserTV.setText("Rofiqul Islam");
        Picasso.get().load("https://m.cricbuzz.com/a/img/v1/192x192/i1/c170912/shakib-al-hasan.jpg")
                .placeholder(R.drawable.profile_image)
                .into(drawerImage);
        /*headerview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ContactActivity.class);
                startActivity(intent);
            }
        });*/

        //======  drawer_menu
        navigationView.setNavigationItemSelectedListener(this);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.drawer_open,R.string.drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();


        //Gridvi

        prepareList();

        // prepared arraylist and passed it to the Adapter class
        mAdapter = new GridviewAdapter(this,listCountry, listFlag);

        // Set custom adapter to gridview
        gridView = (GridView) findViewById(R.id.gridView1);
        gridView.setAdapter(mAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(MainActivity.this, mAdapter.g(position), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void prepareList() {
        listCountry = new ArrayList<String>();

        listCountry.add("Doctor List");
        listCountry.add("Call For Appointment");
        listCountry.add("Book an Appointment");
        listCountry.add("View Reports");
        listCountry.add("Online Payment");
        listCountry.add("Telemedicine");
        listCountry.add("Give/View Feedback");
        listCountry.add("Location");

        listFlag = new ArrayList<Integer>();
        listFlag.add(R.drawable.doctors);
        listFlag.add(R.drawable.call);
        listFlag.add(R.drawable.appionment);
        listFlag.add(R.drawable.report);
        listFlag.add(R.drawable.bills);
        listFlag.add(R.drawable.telemedicine);
        listFlag.add(R.drawable.feedback);
        listFlag.add(R.drawable.location);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        closeDrawer();


        switch (item.getItemId()){
            case R.id.dashboard:
                break;
            case R.id.hospital_lm:
                startActivity(new Intent(this, MapsActivity.class));
                break;
            case R.id.meeting:
                startActivity(new Intent(this, TelemeetingActivity.class));
                break;
            case R.id.plot:
                startActivity(new Intent(this, PlotActivity.class));
                break;
            case R.id.logout:
                //FirebaseAuth.getInstance().signOut();
                signOut();
                break;
        }

        return false;
    }

    private void signOut() {
        Toast.makeText(this, "Signing Out...", Toast.LENGTH_SHORT).show();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_USERS).document(
                        preferenceManager.getString(Constants.KEY_USER_ID)
                );
        HashMap<String, Object> updates = new HashMap<>();
        updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
        documentReference.update(updates)
                .addOnSuccessListener(aVoid -> {
                    preferenceManager.clearPreferences();
                    startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Unable to sign out", Toast.LENGTH_SHORT).show());
    }

    private void closeDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START);
    }
    private void openDrawer() {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            closeDrawer();
        }
        super.onBackPressed();
    }


}