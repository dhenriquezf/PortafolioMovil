package henriquez.daniel.prueba.app.com.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import henriquez.daniel.prueba.app.com.login.Clases.Perfil;
import henriquez.daniel.prueba.app.com.login.Clases.Usuario;

public class SelectPerfil extends AppCompatActivity {

    int rut;
    ArrayAdapter<String> comboAdapter;
    Spinner spPerfiles;
    Button btnIngresar;
    TextView lblNombre;

    Usuario user;
    ArrayList<String> perfiles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_perfil);

        spPerfiles = findViewById(R.id.spinner);
        btnIngresar = findViewById(R.id.button);
        lblNombre = findViewById(R.id.lblNombre);

        Bundle bReceptor = getIntent().getExtras();
        if (bReceptor != null){
            user = (Usuario) bReceptor.getSerializable("usuario");
            perfiles = (bReceptor.getStringArrayList("perfiles"));
            lblNombre.setText(user.nombre);
            llenaSpinner(perfiles);
        }
        //rut = getIntent().getExtras().getInt("rutApoderado");
        //rutApo = String.valueOf(rut);
        //nomApo = getIntent().getExtras().getString("nomApoderado");

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionarPerfil();
            }
        });
    }

    public void llenaSpinner(ArrayList<String> perfiles){
        comboAdapter = new AdaptadorSpinnerPerfiles(SelectPerfil.this,android.R.layout.simple_spinner_dropdown_item, perfiles);
        spPerfiles.setAdapter(comboAdapter);
    }

    public void seleccionarPerfil(){

        String perfil = spPerfiles.getSelectedItem().toString().toLowerCase();
        switch (perfil){
            case "apoderado":
                Intent intent = new Intent(SelectPerfil.this, MenuApoderado.class);
                Bundle bEmisor = new Bundle();
                bEmisor.putSerializable("usuario", user);
                bEmisor.putString("idPerfil",perfil);
                intent.putExtras(bEmisor);
                SelectPerfil.this.startActivity(intent);
                break;
            case "encargado curso":
                Intent intentEncargado = new Intent(SelectPerfil.this, MenuApoderado.class);
                Bundle bEmisorEncargado = new Bundle();
                bEmisorEncargado.putSerializable("usuario", user);
                bEmisorEncargado.putString("idPerfil",perfil);
                intentEncargado.putExtras(bEmisorEncargado);
                SelectPerfil.this.startActivity(intentEncargado);
                /*Intent intentEncargado = new Intent(SelectPerfil.this,MenuEncargado.class);
                SelectPerfil.this.startActivity(intentEncargado);*/
                break;
            default:
                Toast.makeText(SelectPerfil.this, "Problemas con Sistema", Toast.LENGTH_LONG).show();
                break;
        }
    }

    public class AdaptadorSpinnerPerfiles extends ArrayAdapter<String> {

        private Context context;
        private ArrayList<String> datos;

        //Constructor, parametros: contexto (actividad donde estamos), array de datos.
        public AdaptadorSpinnerPerfiles(Context context, int textViewResourceId,ArrayList<String> datos) {
            //llamada al constructor padre. (extends).
            super(context, textViewResourceId, datos);
            this.context = context;
            this.datos = datos;
        }

        @Override
        public int getCount(){
            return datos.size();
        }

        @Override
        public String getItem(int position){
            return datos.get(position);
        }

        @Override
        public long getItemId(int position){
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TextView label = (TextView) super.getView(position, convertView, parent);
            label.setTextColor(Color.WHITE);

            label.setText(datos.get(position));

            return label;
        }

        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            TextView label = (TextView) super.getDropDownView(position, convertView, parent);
            label.setTextColor(Color.BLACK);
            label.setText(datos.get(position));

            return label;
        }
    }
}
