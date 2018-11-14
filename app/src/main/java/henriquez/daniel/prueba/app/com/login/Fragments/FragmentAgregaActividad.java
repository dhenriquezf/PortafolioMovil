package henriquez.daniel.prueba.app.com.login.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.support.v7.app.AlertDialog;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import henriquez.daniel.prueba.app.com.login.Requests.ActividadApRequest;
import henriquez.daniel.prueba.app.com.login.Clases.Actividad;
import henriquez.daniel.prueba.app.com.login.R;
import henriquez.daniel.prueba.app.com.login.Requests.AgregaActividadRequest;

public class FragmentAgregaActividad extends Fragment {

    //ArrayAdapter<String> comboAdapter;
    ArrayAdapter<Actividad> comboAdapter;
    //ArrayList<String> act = new ArrayList<>();
    ArrayList<Actividad> actividades = new ArrayList<>();
    View rootView;
    //String rutApo;
    Spinner spinner;
    //String nomCurso;
    Button btnNuevo;
    Actividad actividad;
    int idCurso;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_fragment_agrega_actividad, container, false);

        setearCampos();
        llenarSpinner();

        btnNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    Response.Listener<String> respListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            JSONObject jsonResponse;
                            try {
                                jsonResponse = new JSONObject(response);
                                //corresponde a la respusta del webService.
                                boolean success = jsonResponse.getBoolean("success");

                                if (success == true){
                                    Toast.makeText(getActivity(), "Ingreso existoso", Toast.LENGTH_SHORT).show();
                                }else{
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    //Segun el mensaje nos indica el error.
                                    builder.setMessage("error al ingresar actividad")
                                            .setNegativeButton("Retry",null)
                                            .create().show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    AgregaActividadRequest agregaActividadRequest = new AgregaActividadRequest(respListener);
                    RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                    queue.add(agregaActividadRequest);
                }catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!spinner.getItemAtPosition(position).toString().equals("Seleccione")){
                    //nomCurso = spinner.getItemAtPosition(position).toString();
                    actividad = (Actividad) parent.getSelectedItem();
                    btnNuevo.setVisibility(View.VISIBLE);
                }else{
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return rootView;
    }

    public void setearCampos(){
        idCurso = getArguments().getInt("idCurso");
        //rutApo = String.valueOf(getArguments().getInt("rutApoderado"));
        btnNuevo = rootView.findViewById(R.id.btnAgregar);
        spinner = rootView.findViewById(R.id.spActividad);
    }
    public void llenarSpinner(){
        Response.Listener<String> respoListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //JSONObject jo = new JSONObject(response);
                    JSONArray jaActividades = new JSONArray(response);
                    //JSONArray jaContrato = jo.getJSONArray("ArrayOfContrato");

                    for (int i = 0; i < jaActividades.length(); i++){
                        JSONObject jsonActividad = jaActividades.getJSONObject(i);
                        actividades.add(new Actividad(jsonActividad.getInt("Id"),
                                jsonActividad.getString("Nombre").trim(),
                                jsonActividad.getString("Descripcion").trim()));
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
                comboAdapter = new AdaptadorSpinnerAct(getActivity().getApplicationContext(),android.R.layout.simple_spinner_item, actividades);
                spinner.setAdapter(comboAdapter);
            }
        };
        ActividadApRequest traerActividadRequest = new ActividadApRequest(respoListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(traerActividadRequest);
    }

    public class AdaptadorSpinnerAct extends ArrayAdapter<Actividad> {

        private Context context;
        private ArrayList<Actividad> datos;

        //Constructor, parametros: contexto (actividad donde estamos), array de datos.
        public AdaptadorSpinnerAct(Context context, int textViewResourceId,ArrayList<Actividad> datos) {
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
        public Actividad getItem(int position){
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

            label.setText(datos.get(position).getNombre());

            return label;
        }

        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            TextView label = (TextView) super.getDropDownView(position, convertView, parent);
            label.setTextColor(Color.BLACK);
            label.setText(datos.get(position).getNombre());

            return label;
        }
    }

}
