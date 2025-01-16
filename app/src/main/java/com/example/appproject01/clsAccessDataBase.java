package com.example.appproject01;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class clsAccessDataBase {

    private SQLiteDatabase dataBase; // to open my dataBase

    // i can use open helper instead of that because it is parent class and two have getWritable
    // and get readable database
    private clsAssetDataBase carsDataBase; // to get instance from asset database
    // private SQLiteOpenHelper openHelper;

    private Context context;

    private static clsAccessDataBase dataBaseAccessInstance;// instance from this database access


    private clsAccessDataBase(Context context){

        // this points to database assets (can just read but not write on it)
        // i can use this : this.openHelper=new clsDataBaseFromAssetFromSQLSrver(context);
        this.carsDataBase = new clsAssetDataBase(context);

        this.context = context;
    }

    public static clsAccessDataBase getInstance(Context context){
        if(dataBaseAccessInstance==null){
            dataBaseAccessInstance =  new clsAccessDataBase(context);

        }
        return dataBaseAccessInstance;

    }


    public void Open(){

        this.dataBase = this.carsDataBase.getWritableDatabase();
    }


    public void Close(){
        if(this.dataBase!=null){
            this.dataBase.close();
        }
    }



    /*
    // called one time only when create object from class
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE Cars (Id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Model TEXT,Color TEXT,DistancePerLitter REAL)");
    }

    // called when DB_version is changed
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Cars");

        onCreate(sqLiteDatabase);

    }
    */


    public boolean Insert(clsVehicle car){

        // like bundle to put  inserted values in columns at database
        ContentValues values = new ContentValues();
        values.put("Model",car.getModel());
        values.put("Color",car.getColor());
        values.put("DistancePerLitter",car.getDpl());
        values.put("description",car.getDescription());

        values.put("image",car.getImage());


        // insert values to table
        long insertedID= dataBase.insert("Cars",null,values);

        Toast.makeText(context,"car is inserted = "+insertedID,Toast.LENGTH_LONG).show();

        return insertedID!=-1;
    }

    public boolean Update(clsVehicle car){
        // points to database
        // SQLiteDatabase db = getWritableDatabase();

        // like bundle to put  inserted values in columns at database
        ContentValues values = new ContentValues();
        values.put("Model",car.getModel());
        values.put("Color",car.getColor());
        values.put("DistancePerLitter",car.getDpl());
        values.put("description",car.getDescription());

        values.put("image",car.getImage());

        // insert values to table
        String[] Args = new String[]{car.getId()+""};
        int rowsAffected= dataBase.update("Cars",values,"id=?",Args);

        return rowsAffected>0;
    }

    public long GetCarsCount(){
        // points to database
        //  SQLiteDatabase db = getReadableDatabase();
        // get all entries in table Cars
        return DatabaseUtils.queryNumEntries(dataBase,"Cars");
    }


    public boolean Delete(clsVehicle car){
        // points to database
        //    SQLiteDatabase db = getWritableDatabase();


        // all this operations cruid can use direct query on db.exec( put query)

        // insert values to table
        String[] Args = new String[]{car.getId()+""};
        int rowsAffected= dataBase.delete("Cars","id=?",new String[] {String.valueOf(car.getId())});

        return rowsAffected>0;
    }


    public void DeleteAll(){
        // points to database
        //    SQLiteDatabase db = getWritableDatabase();


        // all this operations cruid can use direct query on db.exec( put query)
        dataBase.execSQL("DELETE from Cars");

        Toast.makeText(context,"all is deleted",Toast.LENGTH_LONG).show();

    }



    public ArrayList<clsVehicle> GetAllCars(){

        ArrayList<clsVehicle> cars = new ArrayList<>();

        // SQLiteDatabase db = this.carsDataBase.getReadableDatabase();

        Cursor cursor = dataBase.rawQuery("SELECT * FROM Cars",null);

        // cursore poins to -1 than to 0 rows
        while(cursor.moveToNext()){

            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String Model = cursor.getString(cursor.getColumnIndex("model"));
            String color = cursor.getString(cursor.getColumnIndex("color"));
            Double dpl = cursor.getDouble(cursor.getColumnIndex("distancePerLitter"));

            String image=cursor.getString(cursor.getColumnIndex("image"));

           // Toast.makeText(context,"image name from database : "+image,Toast.LENGTH_LONG).show();



            String description=cursor.getString(cursor.getColumnIndex("description"));



            clsVehicle car = new clsVehicle(id,Model,color,dpl,image,description);

            cars.add(car);
        }

        cursor.close();

      //  Toast.makeText(context,"get all cars",Toast.LENGTH_LONG).show();


        return cars;

    }

    public ArrayList<clsVehicle> GetLikesCars(String text) {

        ArrayList<clsVehicle> cars = new ArrayList<>();

        // SQLiteDatabase db = this.carsDataBase.getReadableDatabase();


        // Use parameterized query to avoid SQL injection
        Cursor cursor = dataBase.rawQuery("SELECT * FROM Cars WHERE model LIKE ?", new String[]{ text + "%" });


        // cursore poins to -1 than to 0 rows
        while (cursor.moveToNext()) {

            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String Model = cursor.getString(cursor.getColumnIndex("model"));
            String color = cursor.getString(cursor.getColumnIndex("color"));
            Double dpl = cursor.getDouble(cursor.getColumnIndex("distancePerLitter"));

            String image = cursor.getString(cursor.getColumnIndex("image"));

          //  Toast.makeText(context, "image name from database : " + imageName, Toast.LENGTH_LONG).show();

            String description = cursor.getString(cursor.getColumnIndex("description"));


            clsVehicle car = new clsVehicle(id, Model, color, dpl, image, description);

            cars.add(car);
        }

        cursor.close();

        //  Toast.makeText(context,"get all cars",Toast.LENGTH_LONG).show();


        return cars;
    }


    public clsVehicle FindCarByID(int id){

        clsVehicle car =null;

        // SQLiteDatabase db = this.carsDataBase.getReadableDatabase();

        Cursor cursor = dataBase.rawQuery("SELECT * FROM Cars WHERE id = ?", new String[]{ String.valueOf(id) });

        // cursore poins to -1 than to 0 rows
        while(cursor.moveToNext()){

            String Model = cursor.getString(cursor.getColumnIndex("model"));
            String color = cursor.getString(cursor.getColumnIndex("color"));
            Double dpl = cursor.getDouble(cursor.getColumnIndex("distancePerLitter"));

            String image=cursor.getString(cursor.getColumnIndex("image"));

            //Toast.makeText(context,"image name from database : "+imageName,Toast.LENGTH_LONG).show();




            String description=cursor.getString(cursor.getColumnIndex("description"));



             car = new clsVehicle(id,Model,color,dpl,image,description);

        }

        cursor.close();

        //  Toast.makeText(context,"get all cars",Toast.LENGTH_LONG).show();


        return car;

    }



}


