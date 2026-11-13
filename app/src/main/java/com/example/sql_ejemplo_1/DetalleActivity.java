package com.example.sql_ejemplo_1;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DetalleActivity extends AppCompatActivity {
    public TextView txt_id, txt_codigo_sector, txt_tipo_trampa, txt_nivel_captura, txt_observaciones;
    public Button boton_editar, boton_eliminar;

    public int id_actual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detalle);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txt_id = findViewById(R.id.txt_id);
        txt_codigo_sector = findViewById(R.id.txt_codigo_sector);
        txt_tipo_trampa = findViewById(R.id.txt_tipo_trampa);
        txt_nivel_captura = findViewById(R.id.txt_nivel_captura);
        txt_observaciones = findViewById(R.id.txt_observaciones);

        boton_editar = findViewById(R.id.boton_editar);
        boton_eliminar = findViewById(R.id.boton_eliminar);

        Intent intent_actual = getIntent();
        id_actual = intent_actual.getIntExtra("id_actual", -1);

        cargarTrampas(id_actual);

        boton_eliminar.setOnClickListener(v -> {
            eliminarTrampas(id_actual);
        });

        boton_editar.setOnClickListener(v -> {
            Intent editar_intent = new Intent(DetalleActivity.this, EditarActivity.class);
            editar_intent.putExtra("id_actual", id_actual);
            startActivity(editar_intent);
        });
    }

    public void cargarTrampas(int id) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this);
        SQLiteDatabase db = admin.getReadableDatabase();

        Cursor consulta = db.rawQuery("SELECT id,codigo_sector,tipo_trampa,nivel_captura,observaciones " + "FROM trampas WHERE id=" + id, null);

        if (consulta.moveToFirst()) {
            //existe valor
            txt_id.setText("# " + consulta.getInt(0));
            txt_codigo_sector.setText("Codigo Sector: " + consulta.getString(1));
            txt_tipo_trampa.setText("Tipo Trampa: " + consulta.getString(2));
            txt_nivel_captura.setText("Nivel Captura: " + consulta.getInt(3));
            txt_observaciones.setText("Observaciones: " + consulta.getString(4));
        } else {
            //no existe valor
            txt_id.setText("Registro no encontrado");
            txt_codigo_sector.setText("");
            txt_tipo_trampa.setText("");
            txt_nivel_captura.setText("");
            txt_observaciones.setText("");
        }

        db.close();
        consulta.close();
    }

    public void eliminarTrampas(int id) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this);
        SQLiteDatabase db = admin.getReadableDatabase();

        db.delete("trampas", "id=" + id, null);

        db.close();
        finish();
    }
}