package com.lit.fernbedienung;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{

    ArrayList<Device> exampleList = new ArrayList<>();
    RecyclerView rec;
    CustomAdapter custom;
    FloatingActionButton fab;
    int group;
    int dev = 0;
    String fam = "";
    String name = "";
    EditText groupE;
    EditText nameE;
    EditText famE;
    EditText devE;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        exampleList = readFromSharedPreferences(this);
        if(exampleList == null)
            exampleList = new ArrayList<Device>();
        rec = findViewById(R.id.recycle);
        rec.setLayoutManager(new LinearLayoutManager(this));
        custom = new CustomAdapter(exampleList);
        rec.setAdapter(custom);
        rec.addOnItemTouchListener(new RecyclerTouchListener(this, rec, new ClickListener()
        {
            @Override
            public void onClick(View view, int position)
            {
                Toast.makeText(MainActivity.this,String.valueOf(position),Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onLongClick(View view, int position)
            {
                Toast.makeText(MainActivity.this,String.valueOf(position),Toast.LENGTH_SHORT).show();
                showDeleteD(position);
                custom.notifyDataSetChanged();
                rec.invalidate();
            }
        }));
        fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                fabpressed();
            }
        });
    }

    private void showDeleteD(final int pos)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Löschen?");

        final  View popunt = getLayoutInflater().inflate(R.layout.deletepopup, null);
        builder.setView(popunt);

        builder.setPositiveButton("Löschen", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                exampleList.remove(pos);
                writeToSharedPreferences(MainActivity.this, exampleList);
                custom = new CustomAdapter(exampleList);
                rec.setAdapter(custom);
            }
        });

        builder.setNegativeButton("Abrechen", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void fabpressed()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Neues Gerät");

        final  View poput = getLayoutInflater().inflate(R.layout.newdevpopup, null);
        builder.setView(poput);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                groupE = poput.findViewById(R.id.GeratGruppe);
                nameE = poput.findViewById(R.id.GeratName);
                famE = poput.findViewById(R.id.GeratFamilie);
                devE = poput.findViewById(R.id.GeratNummer);
                System.out.println(nameE);
                group = Integer.parseInt(groupE.getText().toString());
                dev = Integer.parseInt(devE.getText().toString());
                fam = famE.getText().toString().toLowerCase();
                name = nameE.getText().toString();
                addDevice();
            }
        });

        builder.setNegativeButton("Abrechen", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void addDevice()
    {
        Device newDev = new Device(fam.charAt(0),group, dev, name);
        if(newDev.isValid())
        {
            exampleList.add(newDev);
            writeToSharedPreferences(MainActivity.this, exampleList);
            rec.invalidate();
        }
        else
        {
            Toast.makeText(this,"Keine richtige Eingabe",Toast.LENGTH_SHORT).show();
        }

    }

    public boolean writeToSharedPreferences(Context context, ArrayList<Device> arrayList) {

        // String file =  context.getString(R.string.einstellungen_file_key);
        String file = "bacarraylist";
        SharedPreferences mPrefs = context.getSharedPreferences(file, Context.MODE_PRIVATE);

        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(arrayList);
        prefsEditor.putString("bacarraylist", json);
        prefsEditor.commit();

        return true;
    }
    public ArrayList<Device> readFromSharedPreferences(Context context) {

        // String file =  context.getString(R.string.einstellungen_file_key);
        String file = "bacarraylist";
        SharedPreferences mPrefs = context.getSharedPreferences(file, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("bacarraylist", "");
        Type type = new TypeToken<List<Device>>(){}.getType();
        ArrayList<Device> arrayList= gson.fromJson(json, type);
        return arrayList;
    }

    public static interface ClickListener{
        public void onClick(View view,int position);
        public void onLongClick(View view,int position);
    }

    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

        private ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(Context context, final RecyclerView recycleView, final ClickListener clicklistener){

            this.clicklistener=clicklistener;
            gestureDetector=new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child=recycleView.findChildViewUnder(e.getX(),e.getY());
                    if(child!=null && clicklistener!=null){
                        clicklistener.onLongClick(child,recycleView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child=rv.findChildViewUnder(e.getX(),e.getY());
            if(child!=null && clicklistener!=null && gestureDetector.onTouchEvent(e)){
                clicklistener.onClick(child,rv.getChildAdapterPosition(child));
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}