package com.example.appproject01;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


// implements interface ( event exists in adapter or we difine in separate file normal )
public class MainActivity extends AppCompatActivity implements
        clsRecycleViewAdapter.OnItemClickListener{

            public static int WRITE_EXTERNAL_REQUEST_CODE = 1;

    public static final String MODE_KEY = "mode";
    public static final String ADD_MODE = "add";
    public static final String UPDATE_MODE = "update";
    public static final String BUNDLE_KEY = "carInfo";


    // we use them with dataBase and adapter
            RecyclerView rv;

           clsAccessDataBase dataBase ;
           ArrayList<clsVehicle> cars = new ArrayList<>();
           clsRecycleViewAdapter adapter;
         //  RecyclerView.LayoutManager lm = new GridLayoutManager(this,2);
          RecyclerView.LayoutManager lm = new LinearLayoutManager(this);



    // global main menu items
            MenuItem searchIcon_menuItem;
            MenuItem cheveronIcon_menuItem;
            MenuItem clear_menuItem;
            EditText et_searchForCars;



            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                EdgeToEdge.enable(this);
                setContentView(R.layout.activity_main);
                ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                    Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                    return insets;
                });


                HandleCustomActionBar();

                HandlPermissionIssue();


                HandleRecycleVieGottenFromDataBase();



                // inflate floating button
                FloatingActionButton fab_addCar = findViewById(R.id.main_btn_Add_CarID);

                // Set an onClickListener for the FAB
                fab_addCar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Handle click event

                        OpenAddingCarActivity();

                    }
                });

            }




            public void OpenAddingCarActivity(){

                Intent intent = new Intent(getBaseContext(), add_update.class);

                intent.putExtra(MODE_KEY, ADD_MODE);

                 startActivity(intent);
            }



            public void InitializationOfMenuItems() {
                et_searchForCars.setVisibility(View.INVISIBLE);
                clear_menuItem.setVisible(false);
                cheveronIcon_menuItem.setVisible(false);

                searchIcon_menuItem.setVisible(true);
            }


            public void HandleCustomActionBar() {

                // Set the custom Toolbar as the ActionBar
                Toolbar toolbar = findViewById(R.id.main_toolbar);
                setSupportActionBar(toolbar);
            }

            // Menu Options exists at main appBar layout
            @Override
            public boolean onCreateOptionsMenu(Menu menu) {

                // inflat menu_items from xml to object(view)
                MenuInflater infl = getMenuInflater();

                infl.inflate(R.menu.menu_items_main, menu);

                // global items to use it globaly enabled and disabled
                searchIcon_menuItem = menu.findItem(R.id.menu_main_searchID);
                cheveronIcon_menuItem = menu.findItem(R.id.menu_main_cheronID);
                clear_menuItem = menu.findItem(R.id.menu_main_clearID);


                // edit text item with layout design contains edit text only
                MenuItem item = menu.findItem(R.id.menu_main_editTextLayoutID);

                View v = item.getActionView();

                et_searchForCars = v.findViewById(R.id.editTextLayout_edt_searchForCarsID);


                InitializationOfMenuItems();


                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getBaseContext(), "custom notification", Toast.LENGTH_SHORT).show();
                    }
                });


                return super.onCreateOptionsMenu(menu);
            }


            @Override
            public boolean onOptionsItemSelected(MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.menu_main_cheronID) {

                    // we will do it later

                    return true;

                    // clear item
                } else if (id == R.id.menu_main_clearID) {
                    // Handle the help action
                   InitializationOfMenuItems();

                   GetAllCarsFromDataBase();

                    return true;

                    // search item
                } else if (id == R.id.menu_main_searchID) {

                    et_searchForCars.setVisibility(View.VISIBLE);
                    clear_menuItem.setVisible(true);
                    cheveronIcon_menuItem.setVisible(true);

                    searchIcon_menuItem.setVisible(false);

                    // when text changed we bring it for textWacher below
                    et_searchForCars.addTextChangedListener(textWatcher);

                    return true;

                } else if (id == R.id.menu_main_editTextLayoutID) {

                    // we will do it later


                    return true;

                } else {
                    return false;
                }
            }


            // when activity starting opening calling this method
            @Override
            protected void onStart() {
            super.onStart();
            // Start animations or check for UI updates

                HandleRecycleVieGottenFromDataBase();

            }



           // event when text change do something ( maintain text on textwatcher)
            private TextWatcher textWatcher = new TextWatcher() {
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        String textAtToolBar =et_searchForCars.getText().toString();

       GetCarsLike(textAtToolBar);



    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
};



            public void GetCarsLike(String textAtToolBar){
                dataBase.Open();

                cars = dataBase.GetLikesCars(textAtToolBar);

                dataBase.Close();

                adapter = new clsRecycleViewAdapter(cars,this);


                rv.setHasFixedSize(true);
                rv.setLayoutManager(lm);
                rv.setAdapter(adapter);
            }



            public void GetAllCarsFromDataBase(){

                dataBase = clsAccessDataBase.getInstance(this);


               dataBase.Open();

                //dataBase.DeleteAll();


                cars = dataBase.GetAllCars();

              //  for (clsVehicle car : cars
              //       ) {

              //      Toast.makeText(this,"car image = "+car.getImage(),Toast.LENGTH_LONG).show();
             //   }

               dataBase.Close();

                adapter = new clsRecycleViewAdapter(cars,this);

                // RecyclerView.LayoutManager lm = new LinearLayoutManager(this);

                 rv.setHasFixedSize(true);
                  rv.setLayoutManager(lm);
                  rv.setAdapter(adapter);
            }




           //  fill recycle view from asset dataBase
           public void HandleRecycleVieGottenFromDataBase(){

                 rv= findViewById(R.id.main_recycleViewID);

                 GetAllCarsFromDataBase();

            }



    // event getting from viewholder event in adapterRecycleView when click item
    @Override
    public void onItemClick( int position) {

        Intent intent = new Intent(this, add_update.class);

        Bundle CarData = new Bundle();


        clsVehicle car = cars.get(position);

        //Toast.makeText(getBaseContext(),"car id = "+car.getId()+" IMAGE = "+car.getImage(),Toast.LENGTH_LONG).show();


        // convert and save image at file path
        Uri imageUri = clsImageUtils.convertBase64ToUriAndSaveImageBitmapAtFile(this,car.getImage());


        CarData.putString("model",car.getModel());
        CarData.putString("color",car.getColor());
        CarData.putDouble("DPL",car.getDpl());

        CarData.putString("imageUri",imageUri.toString());

        CarData.putInt("ID",car.getId());
        CarData.putString("description",car.getDescription());




        // Attach the Bundle to the Intent
        intent.putExtra(BUNDLE_KEY,CarData);

        intent.putExtra(MODE_KEY, UPDATE_MODE);


        // Start the activity with the Intent
           startActivity(intent);

    }








// this codes all about permissions to read diff resource files

    public void HandlPermissionIssue(){
        if (CheckPermission()) {

            Toast.makeText(getBaseContext(), "permissions success", Toast.LENGTH_LONG).show();
        } else {
            RequestPermissions();
        }
    }

    public boolean CheckPermission() {

        // permissions
        // ContextCompat in level of app , ActivityCompat in level of activity only

        // For Android 11 (API level 30) and higher, check if "All Files Access" is granted
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

            // is permission already agree or not in android versions more than 11
            return Environment.isExternalStorageManager();

        } else {

            // permissions for lower than android 11
            int readCheck = ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE);
            int writeCheck = ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE);

            // is permission already agree or not in android versions less than 11

            return writeCheck == PackageManager.PERMISSION_GRANTED;


        }
    }


    public void RequestPermissions() {

    }




    // This method handles the result of the permission request for less than 11 android versions
    public void OnRequestPermissionsResultCallback(int requestCode, @NonNull String[] permissions,
                                                   @NonNull int[] grantResults) {
        if (requestCode == WRITE_EXTERNAL_REQUEST_CODE) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted, do the task that requires the permission
                //  performActionRequiringPermission();
                Toast.makeText(this, "permissions is ok on android versions less than 11", Toast.LENGTH_LONG).show();
            } else {
                // Permission denied, you can show a message to the user explaining the importance of the permission
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    // we use it to check permissions more higher than 11 android
    // Register the launcher to handle the result from the settings activity
    private ActivityResultLauncher<Intent> manageAllFilesAccessLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                // Check the result when the settings activity is finished
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (Environment.isExternalStorageManager()) {
                        // Permission was granted
                        Toast.makeText(this, "All Files Access granted! in versions android 11 or more", Toast.LENGTH_LONG).show();
                    } else {
                        // Permission was not granted
                        Toast.makeText(this, "All Files Access not granted.", Toast.LENGTH_LONG).show();
                    }
                }
            });
}



