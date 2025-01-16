package com.example.appproject01;

import android.app.Activity;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.concurrent.Executor;

public class add_update extends AppCompatActivity {

    // views
    EditText et_model;
    EditText et_color;
    EditText et_description;
    EditText et_dpl;
    ImageView iv_image;

    // menu items
    MenuItem deleteIcon_menuItem;
    MenuItem checkIcon_menuItem;
    MenuItem editIcon_menuItem;

    ArrayList<clsVehicle> cars = new ArrayList<>();

    clsAccessDataBase dataBase ;


    clsVehicle carToUpdate=null;

    Intent intent=null;

    String mode;

    Uri imageUri=null;

    private static final int GALLERY_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_update);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        HandleToolBar();


        InflateViews();

         intent = getIntent();
         mode = intent.getStringExtra(MainActivity.MODE_KEY);

       // Toast.makeText(this,"string image = "+intent.getBundleExtra(MainActivity.BUNDLE_KEY).getString("image"),Toast.LENGTH_LONG).show();

        //Toast.makeText(this,"mode = "+mode,Toast.LENGTH_LONG).show();


        if (MainActivity.ADD_MODE.equals(mode)) {
            // Handle Add Mode
            EnableViews();

        } else if( MainActivity.UPDATE_MODE.equals(mode)) {
            // Handle Update Mode

            FillDataByCarInfoGettingFromMainActivity();
            DisableViews();


        }





        // im here where i click on image then open gallerie
        iv_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openGallery();
            }
        });


    }





      public void InflateViews(){
        et_dpl = findViewById(R.id.Add_Update_ev_dplID);
         et_description= findViewById(R.id.Add_Update_ev_descriptionID);
          et_color = findViewById(R.id.Add_Update_ev_colorID);
          et_model = findViewById(R.id.Add_Update_ev_modelID);
          iv_image = findViewById(R.id.Add_Update_iv_ImageID);

      }



      public void DisableViews(){
          et_model.setEnabled(false);
          et_color.setEnabled(false);
          et_description.setEnabled(false);
          et_dpl.setEnabled(false);
      }

      public void EnableViews(){
        et_model.setEnabled(true);
        et_color.setEnabled(true);
        et_description.setEnabled(true);
        et_dpl.setEnabled(true);

      }




      public void FillDataByCarInfoGettingFromMainActivity() {
          Bundle carData = intent.getBundleExtra(MainActivity.BUNDLE_KEY);

          if (carData != null) {
              String model = carData.getString("model");
              String color = carData.getString("color");
              double dpl = carData.getDouble("DPL");


              // Retrieve the Uri string and convert back to Uri
              String imageUriString = carData.getString("imageUri");



              int id = carData.getInt("ID");
              String description = carData.getString("description");

             // Toast.makeText(this,"image  = "+image,Toast.LENGTH_LONG).show();

              // fill data
              et_color.setText(color);
              et_description.setText(description);
              et_dpl.setText(dpl + "");
              et_model.setText(model);


              if (imageUriString != null) {
                  Uri URIimage = Uri.parse(imageUriString);

                  // set global image
                  imageUri = URIimage;

                  iv_image.setImageURI(URIimage);
              }


                 // Toast.makeText(this,"image uri = "+imageUri,Toast.LENGTH_LONG).show();



              else iv_image.setImageResource(R.drawable.woman);


              // fill global car comming from updated
              carToUpdate= new clsVehicle(id,model,color,dpl,imageUriString,description);

          }
      }



    public void HandleToolBar(){
        // Find the toolbar by its ID and set it as the action bar
        Toolbar toolBar = findViewById(R.id.Add_Update_tb_TolbarID);
        setSupportActionBar(toolBar);  // Set the Toolbar as the ActionBar
    }


    // Menu Options exists at add_update appBar layout
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // inflat menu_items from xml to object(view)
        MenuInflater infl = getMenuInflater();

        infl.inflate(R.menu.menu_items_add_update,menu);

        // global items to use it globaly enabled and disabled
        deleteIcon_menuItem = menu.findItem(R.id.menu_add_update_deleteID);
        checkIcon_menuItem = menu.findItem(R.id.menu_add_update_checkID);
        editIcon_menuItem = menu.findItem(R.id.menu_add_update_EditID);



       if( MainActivity.UPDATE_MODE.equals(mode)) {
          // Toast.makeText(this,"mode at items = "+mode,Toast.LENGTH_LONG).show();

            editIcon_menuItem.setVisible(true);
            deleteIcon_menuItem.setVisible(true);
            checkIcon_menuItem.setVisible(false);
        }

        else{
            editIcon_menuItem.setVisible(false);
            deleteIcon_menuItem.setVisible(false);
            checkIcon_menuItem.setVisible(true);
        }


        return super.onCreateOptionsMenu(menu);
    }


    public boolean DeleteCar()
    {
        dataBase = clsAccessDataBase.getInstance(this);
        dataBase.Open();

        if(carToUpdate!=null && dataBase.Delete(carToUpdate)){
            dataBase.Close();

            return true;
        }


        return false;

    }

    public boolean UpdateCar()
    {
        dataBase = clsAccessDataBase.getInstance(this);
        dataBase.Open();

        if(carToUpdate!=null && dataBase.Update(carToUpdate)){
            dataBase.Close();

            return true;
        }


        return false;

    }

    public boolean InsertCar(){
        dataBase = clsAccessDataBase.getInstance(this);
        dataBase.Open();

        if(carToUpdate!=null && dataBase.Insert(carToUpdate)){
            dataBase.Close();

            return true;
        }

        return false;
    }


    public void FillCarInfo(){

        if(MainActivity.ADD_MODE.equals(mode)){
            carToUpdate = new clsVehicle();
        }

        carToUpdate.setColor(et_color.getText().toString());
        carToUpdate.setDescription(et_description.getText().toString());
        carToUpdate.setModel(et_model.getText().toString());

        carToUpdate.setDpl(Double.parseDouble(et_dpl.getText().toString()));

        if(imageUri!=null){

            String imageBased64 = clsImageUtils.saveImageUriAsString(imageUri,this);

            carToUpdate.setImage(imageBased64);
        }

        else carToUpdate.setImage(null);

        imageUri=null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_add_update_checkID) {


            // im here
            FillCarInfo();

            Toast.makeText(this, "check clicked", Toast.LENGTH_SHORT).show();


            // update
            if( MainActivity.UPDATE_MODE.equals(mode)) {
                if (UpdateCar()) {
                    Toast.makeText(getBaseContext(), " car with id = " + id + " Is Updated succssfully", Toast.LENGTH_LONG).show();
                    finish();

                } else {
                    Toast.makeText(getBaseContext(), " Update fail", Toast.LENGTH_LONG).show();

                }
            }
            // add car

            else if (MainActivity.ADD_MODE.equals(mode)) {
                if (InsertCar()) {
                    Toast.makeText(getBaseContext(), " car with id = " + id + " Is Inserted succssfully", Toast.LENGTH_LONG).show();
                    finish();

                } else {
                    Toast.makeText(getBaseContext(), " Insert fail", Toast.LENGTH_LONG).show();

                }
            }




            return true;


        } else if (id == R.id.menu_add_update_deleteID) {
           // Toast.makeText(this, "update clicked", Toast.LENGTH_SHORT).show();


            if(DeleteCar()){
                Toast.makeText(getBaseContext()," car with id = "+id+ " Is Deleted succssfully",Toast.LENGTH_LONG).show();
                finish();
            }
             else{
                Toast.makeText(getBaseContext()," Deleted fail",Toast.LENGTH_LONG).show();

            }

             
            return true;

        }

        else if (id == R.id.menu_add_update_EditID) {
           // Toast.makeText(this, "edit clicked", Toast.LENGTH_SHORT).show();

            EnableViews();
            editIcon_menuItem.setVisible(false);
            deleteIcon_menuItem.setVisible(false);
            checkIcon_menuItem.setVisible(true);

            return true;

        }

        else {
            return false;
        }
    }




    // open gallerie code

    // Declare the launcher for starting an activity and receiving the result
    private final ActivityResultLauncher<Intent> galleryLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        // Check if the result is OK and has data
                        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                            Uri selectedImageUri = result.getData().getData();
                            if (selectedImageUri != null) {
                                imageUri = selectedImageUri;

                              //  Bitmap bitmapImage = clsImageUtils.ConvertUriImageToBitmap(selectedImageUri, this);
                                iv_image.setImageURI(imageUri);  // Display the image in an ImageView


                                // save image in file to can deal with it ( file.paths)
                                String imageBased64 = clsImageUtils.saveImageUriAsString(imageUri,this);
                                Uri savedImage = clsImageUtils.convertBase64ToUriAndSaveImageBitmapAtFile(this,imageBased64);

                            }
                        }
                    }
            );



    // Method to open the gallery with an Intent
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");  // Filter for images only
        galleryLauncher.launch(intent);  // Launch the gallery with the launcher
    }





}