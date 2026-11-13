package com.example.sql_ejemplo_1;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EditarActivity extends AppCompatActivity {
    EditText input_codigo_sector, input_tipo_trampa, input_nivel_capturas, input_observaciones;
    Button boton_enviar;

    public int id_actual ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_editar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        input_codigo_sector = findViewById(R.id.input_codigo_sector);
        input_tipo_trampa = findViewById(R.id.input_tipo_trampas2);
        input_nivel_capturas = findViewById(R.id.input_nivel_captura2);
        input_observaciones = findViewById(R.id.input_observaciones2);
        boton_enviar = findViewById(R.id.boton_enviar2);

        Intent intent_actual = getIntent();
        id_actual = intent_actual.getIntExtra("id_actual", -1);

        cargarTrampas(id_actual);

        boton_enviar.setOnClickListener(v -> {
            actualizartrampas(id_actual);
        });
    }

    public void cargarTrampas(int id)
    {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this);
        SQLiteDatabase db = admin.getReadableDatabase();

        Cursor consulta = db.rawQuery("SELECT id,codigo_sector,tipo_trampa,nivel_captura,observaciones " + "FROM trampas WHERE id="+id, null);

        if(consulta.moveToFirst())
        {
            //existe valor
            input_codigo_sector.setText(consulta.getString(1));

            input_tipo_trampa.setText(consulta.getString(2));

            input_nivel_capturas.setText(String.valueOf(consulta.getInt(3)));

            input_observaciones.setText(
                    consulta.getString(4));
        }
        else
        {
            //no existe valor
        }

        db.close();
        consulta.close();
    }

    public void actualizartrampas(int id)
    {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this);
        SQLiteDatabase db = admin.getWritableDatabase();

        String codigoSector = input_codigo_sector.getText().toString();

        String tipoTrampa = input_tipo_trampa.getText().toString();

        String nivelCaptura = input_nivel_capturas.getText().toString();

        String observaciones = input_observaciones.getText().toString();

        ContentValues valores = new ContentValues();
        valores.put("codigo_sector", codigoSector);
        valores.put("tipo_trampa", tipoTrampa);
        valores.put("nivel_captura", Integer.parseInt(nivelCaptura));
        valores.put("observaciones", observaciones);

        db.update("trampas", valores, "id="+id, null);

        db.close();
        finish();
    }
}