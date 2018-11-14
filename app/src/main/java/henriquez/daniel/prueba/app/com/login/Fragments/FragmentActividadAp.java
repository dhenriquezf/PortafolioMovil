package henriquez.daniel.prueba.app.com.login.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
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
import henriquez.daniel.prueba.app.com.login.Clases.Alumno;
import henriquez.daniel.prueba.app.com.login.Clases.Curso;
import henriquez.daniel.prueba.app.com.login.R;
import henriquez.daniel.prueba.app.com.login.Requests.TraerAlumnosRequest;
import henriquez.daniel.prueba.app.com.login.Requests.TraerCursosRequest;

//ActividadesApPorCurso.php
public class FragmentActividadAp extends Fragment {

    //ArrayAdapter<String> comboAdapter;
    ArrayAdapter<Curso> comboAdapter;
    ArrayAdapter<Alumno> comboAdapterAlumnos;
    Spinner spCursos;
    ListView lvActividadesAp;
    //String curso;
    String rutApo;
    Button btnAgrega;
    View rootView;
    int idCurso;
    String perfil;
    Curso cursoTemp;

    AdaptadorSpinnerCurso adapterSp;

    ArrayList<Curso> cursos = new ArrayList<>();
    ArrayList<Alumno> alumnos = new ArrayList<>();
    ArrayList<Actividad> actividades = new ArrayList<>();
    ArrayList<Curso> datosSpinner = new ArrayList<>();

    public FragmentActividadAp.nuevaAct implementacion;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        if(context instanceof FragmentContratoAp.comunicaActivity){
            this.implementacion = (FragmentActividadAp.nuevaAct)context;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /** Se instancian los elementos que van a realizar la comunicacion con el
         * otro fragment*/

        btnAgrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /** Se envia atraves de la interface el mensaje */
                implementacion.nueAct(idCurso);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_fragment_actividad_ap, container, false);

        setearCampos();
        llenarSpinner();

        if (perfil.equals("apoderado")){
            btnAgrega.setVisibility(View.INVISIBLE);
        }else{
            btnAgrega.setVisibility(View.VISIBLE);
        }

        try
        {
            spCursos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Curso curso = adapterSp.getItem(position);
                    idCurso = curso.getIdCurso();
                    traerActividades(idCurso);
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return rootView;
    }

    public void traerActividades(int idCurso){
        Response.Listener<String> respoListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                actividades.clear();
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

                AdaptadorActividadesCurso adaptador = new AdaptadorActividadesCurso(getActivity().getApplicationContext(), actividades);
                lvActividadesAp.setAdapter(adaptador);
            }
        };
        ActividadApRequest traerActividadRequest = new ActividadApRequest(respoListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(traerActividadRequest);
    }

    public void setearCampos(){
        rutApo = String.valueOf(getArguments().getInt("rutUsr"));
        perfil = String.valueOf(getArguments().getString("perfilUsr"));
        spCursos = rootView.findViewById(R.id.spSelecCurso);
        lvActividadesAp = rootView.findViewById(R.id.listaActividadesAp);
        btnAgrega = rootView.findViewById(R.id.btnAgrega);
    }

    public void llenarSpinner(){
        datosSpinner.clear();
        traerCursos();
    }

    public void traerCursos(){
        Response.Listener<String> respoCursos = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                cursos.clear();
                try {
                    JSONArray jaCursos = new JSONArray(response);

                    for (int i = 0; i < jaCursos.length(); i++){
                        JSONObject jsonCurso = jaCursos.getJSONObject(i);
                        cursos.add(new Curso(jsonCurso.getInt("Id"),
                                jsonCurso.getString("Nombre"),
                                jsonCurso.getInt("Id"),
                                jsonCurso.getInt("TotalReunido")));
                    }

                    traerAlumnos();
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        };
        TraerCursosRequest traerCursosRequest = new TraerCursosRequest(respoCursos);
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(traerCursosRequest);
    }

    public void traerAlumnos(){
        try{
            Response.Listener<String> respoListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    alumnos.clear();
                    try {
                        JSONArray jaAlumnos = new JSONArray(response);

                        for (int i = 0; i < jaAlumnos.length(); i++){
                            JSONObject jsonAlumno = jaAlumnos.getJSONObject(i);
                            JSONObject jsonApoderado = jsonAlumno.getJSONObject("Apoderado");
                            JSONObject jsonUsuario = jsonApoderado.getJSONObject("Usuario");
                            JSONObject jsonCurso = jsonAlumno.getJSONObject("Curso");
                            JSONObject jsonColegio = jsonCurso.getJSONObject("Colegio");
                            if (jsonUsuario.getInt("Rut") == Integer.parseInt(rutApo)){
                                alumnos.add(new Alumno(jsonAlumno.getInt("Rut"),
                                        (jsonAlumno.getString("Nombre").trim() + " " + jsonAlumno.getString("APaterno").trim() + " " +jsonAlumno.getString("AMaterno").trim()),
                                        jsonCurso.getInt("Id"),
                                        jsonUsuario.getString("Rut").trim()));
                            }
                        }
                        for (int i = 0; i < alumnos.size(); i++){
                            for (int j = 0; j < cursos.size(); j++){
                                if (alumnos.get(i).getCurso() == cursos.get(j).getIdCurso()){
                                    Curso curso = cursos.get(j);
                                    if (!datosSpinner.contains(curso)){
                                        datosSpinner.add(cursos.get(j));
                                    }
                                }
                            }
                        }
                        adapterSp = new AdaptadorSpinnerCurso(getActivity().getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, datosSpinner);
                        spCursos.setAdapter(adapterSp);
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            };
            TraerAlumnosRequest traerAlumnosRequest = new TraerAlumnosRequest(respoListener);
            RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
            queue.add(traerAlumnosRequest);
        }catch (Exception e){
            Toast.makeText(getActivity().getApplicationContext(), "Problemas con el sistema", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    public interface nuevaAct{
        void nueAct (int cursoId);
    }

    public class AdaptadorSpinnerCurso extends ArrayAdapter<Curso> {

        private Context context;
        private ArrayList<Curso> datos;

        //Constructor, parametros: contexto (actividad donde estamos), array de datos.
        public AdaptadorSpinnerCurso(Context context, int textViewResourceId,ArrayList<Curso> datos) {
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
        public Curso getItem(int position){
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

    class AdaptadorActividadesCurso extends ArrayAdapter<Actividad> {

        private Context context;
        private ArrayList<Actividad> datos;

        //Constructor, parametros: contexto (actividad donde estamos), array de datos.
        public AdaptadorActividadesCurso(Context context, ArrayList<Actividad> datos) {
            //llamada al constructor padre. (extends).
            super(context, R.layout.listitem_actividadescurso, datos);
            this.context = context;
            this.datos = datos;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View item = inflater.inflate(R.layout.listitem_actividadescurso, null);

            //TextView lblTitulo = (TextView)item.findViewById(R.id.LblCurso);
            //lblTitulo.setText("Curso: " + datos.get(position).getNombreCurso());

            TextView lblCodigo = (TextView)item.findViewById(R.id.LblId);
            lblCodigo.setText("Cod: " + datos.get(position).getNombre());

            TextView lblActividad = (TextView)item.findViewById(R.id.LblActividad);
            lblActividad.setText("Actividad: " + datos.get(position).getNombre());

            TextView lblDescripcion = (TextView)item.findViewById(R.id.LblDescripcion);
            lblDescripcion.setText("Descripcion: " + datos.get(position).getNombre());

            //TextView lblAlumno = (TextView)item.findViewById(R.id.LblAlumno);
            //lblAlumno.setText("Alumno: " + datos.get(position).getNombreAlumno());

            TextView lblProrrateo = (TextView)item.findViewById(R.id.LblProrrateo);
            lblProrrateo.setText("Prorrateo: " + String.valueOf(0));

            TextView lblRecaudo = (TextView)item.findViewById(R.id.LblRecaudo);
            lblRecaudo.setText("Recaudado total: " + String.valueOf(0));

            //btnAgrega.setVisibility(View.VISIBLE);

            return item;
        }
    }
}
