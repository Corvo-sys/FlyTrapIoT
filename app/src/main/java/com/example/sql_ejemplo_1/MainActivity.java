package com.example.sql_ejemplo_1;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //Datos del XML
    EditText input_codigo_sector, input_tipo_trampa, input_nivel_captura, input_observaciones;
    Button boton_enviar;
    ListView lista_trampas;

    //Datos necesarios para ListView
    List<String> datos_trampas = new ArrayList<>();
    List<Integer> lista_id;

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

        //incializar variables
        input_codigo_sector = findViewById(R.id.input_codigo_sector);
        input_tipo_trampa = findViewById(R.id.input_tipo_trampa);
        input_nivel_captura = findViewById(R.id.input_nivel_captura);
        input_observaciones = findViewById(R.id.input_observaciones);
        boton_enviar = findViewById(R.id.boton_enviar);
        lista_trampas = findViewById(R.id.lista_trampas);

        //cargo mis datos desde la base de datos
        cargartrampas();

        boton_enviar.setOnClickListener(v -> {
            crearTrampa();
        });

        lista_trampas.setOnItemClickListener( new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int posicion, long id){
                //al hacer click en un elemento de la lista, buscamos ese elmento en la lista de id y lo enviamos a detalle activity

                Integer id_actual = lista_id.get(posicion);

                Intent intent_detalle = new Intent(MainActivity.this, DetalleActivity.class);
                intent_detalle.putExtra("id_actual", id_actual);
                startActivity(intent_detalle);
            }
        });
    }

    public void cargartrampas()
    {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this);
        SQLiteDatabase db = admin.getReadableDatabase();

        datos_trampas = new ArrayList<>();
        lista_id = new ArrayList<Integer>();

        //CONSULTAR DATOS A LA BASE DE DATOS
        Cursor fila = db.rawQuery("SELECT id,codigo_sector,tipo_trampa,nivel_captura,observaciones FROM trampas", null);

        //validar que tenga por lo menos un dato
        if (fila.moveToFirst())
        {
            //tiene como minimo un dato
            do {
                //obtener los datos de la fila
                int id_actual = fila.getInt(0);
                String codigo = fila.getString(1);
                String tipo = fila.getString(2);
                int captura = fila.getInt(3);
                String observaciones = fila.getString(4);

                String trampa_actual = "# "+id_actual+" sector: "+codigo+" Tipo "+tipo+" captura: "+captura;
                datos_trampas.add(trampa_actual);
                lista_id.add(id_actual);

            } while (fila.moveToNext());
        }
        else
        {
            //si no retorna nada de la base de datos
            datos_trampas.add("No existen esas trampas en la base de datos");
        }

        //adapter
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,datos_trampas );
        lista_trampas.setAdapter(adapter);
    }

    public void crearTrampa()
    {
        String codigoSector = String.valueOf(input_codigo_sector.getText());

        String tipoTrampa = String.valueOf(input_tipo_trampa.getText());

        String nivelCaptura = String.valueOf(input_nivel_captura.getText());

        String observaciones = String.valueOf(input_observaciones.getText());

        if (!codigoSector.isEmpty() && !tipoTrampa.isEmpty() && !nivelCaptura.isEmpty() )
        {
            //si nombre tiene valor y telefono tiene valor entonces agregamos
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this);
            SQLiteDatabase db = admin.getWritableDatabase();

            ContentValues nuevo_dato = new ContentValues();
            nuevo_dato.put("codigo_sector", codigoSector);
            nuevo_dato.put("tipo_trampa", tipoTrampa);
            nuevo_dato.put("nivel_captura", Integer.parseInt(nivelCaptura));
            nuevo_dato.put("observaciones", observaciones);

            db.insert("trampas", null, nuevo_dato);

            //reiniciar los inputs
            input_codigo_sector.setText("");
            input_tipo_trampa.setText("");
            input_nivel_captura.setText("");
            input_observaciones.setText("");
            db.close();

            cargartrampas();

        }
        else
        {
            Toast.makeText(this, "Ingrese los datos de la trampa", Toast.LENGTH_LONG).show();
        }
    }
















}