package henriquez.daniel.prueba.app.com.login.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import java.util.Random;

import henriquez.daniel.prueba.app.com.login.Clases.Alumno;
import henriquez.daniel.prueba.app.com.login.Clases.Cuenta;
import henriquez.daniel.prueba.app.com.login.Clases.Curso;
import henriquez.daniel.prueba.app.com.login.R;
import henriquez.daniel.prueba.app.com.login.Requests.AgregarCuentaRequest;
import henriquez.daniel.prueba.app.com.login.Requests.CuentaRequest;
import henriquez.daniel.prueba.app.com.login.Requests.ExisteCuentaRequest;
import henriquez.daniel.prueba.app.com.login.Requests.TraerAlumnosRequest;
import henriquez.daniel.prueba.app.com.login.Requests.TraerCursosRequest;

public class FragmentPagoAp extends Fragment {

    View rootView;
    String rutApo;
    String nroCta;
    String rutAlum;
    String nomCurso;
    String nomAlum;
    ArrayAdapter<Alumno> comboAdapterAlumnos;
    ArrayAdapter<Curso> comboAdapterCursos;
    ArrayList<Cuenta> cuentas = new ArrayList<>();
    ArrayList<Alumno> alumnos = new ArrayList<>();
    ArrayList<Curso> cursos = new ArrayList<>();
    ArrayList<Curso> datosSpinner = new ArrayList<>();
    TextView lblNroCta;
    TextView lblNomCurso;
    TextView lblTotalReunido;
    TextView lblTotalPagar;
    TextView lblTotalPagado;
    Spinner spinnerAlumnos;
    Spinner spinnerCursos;
    Button btnPagar;
    Button btnAddCta;
    String perfil;

    public FragmentPagoAp.pago implementacion;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        if(context instanceof FragmentPagoAp.pago){
            this.implementacion = (FragmentPagoAp.pago)context;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /** Se instancian los elementos que van a realizar la comunicacion con el
         * otro fragment*/

        btnPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /** Se envia atraves de la interface el mensaje */

                implementacion.pagar(nroCta, nomCurso, nomAlum);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_fragment_pago_ap, container, false);

        setearValores();

        if (perfil.equals("apoderado")){
            spinnerCursos.setVisibility(View.INVISIBLE);
            spinnerCursos.setEnabled(false);
            spinnerAlumnos.setVisibility(View.VISIBLE);
            spinnerAlumnos.setEnabled(true);
            llenarSpinnerAlumno();
        }else{
            spinnerCursos.setVisibility(View.VISIBLE);
            spinnerCursos.setEnabled(true);
            spinnerAlumnos.setVisibility(View.INVISIBLE);
            spinnerAlumnos.setEnabled(false);
            btnAddCta.setVisibility(View.INVISIBLE);
            btnAddCta.setEnabled(false);
            llenarSpinnerCurso();
        }

        btnAddCta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarCuenta();
                existeCuenta(Integer.parseInt(rutAlum));
            }
        });

        spinnerAlumnos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!spinnerAlumnos.getItemAtPosition(position).toString().equals("Seleccione")){
                    Alumno alum = (Alumno) parent.getSelectedItem();
                    rutAlum = String.valueOf(alum.getId());
                    nomAlum = alum.getNombreCompleto();
                    existeCuenta(alum.getId());
                }else{
                    ocultarCampos();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerCursos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!spinnerCursos.getItemAtPosition(position).toString().equals("Seleccione")){
                    Curso curso = (Curso) parent.getSelectedItem();
                    int idCurso = curso.getIdCurso();
                    nomAlum = curso.getNombre();
                    llenarDatos("", String.valueOf(idCurso));
                    //existeCuenta(idCurso);
                }else{
                    ocultarCampos();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //llenarSpinner();

        return rootView;
    }

    public void setearValores(){
        rutApo = String.valueOf(getArguments().getInt("rutUsr"));
        perfil = String.valueOf(getArguments().getString("perfilUsr"));
        lblNroCta = rootView.findViewById(R.id.lblNroCta);
        lblNomCurso = rootView.findViewById(R.id.lblNomCurso);
        lblTotalReunido = rootView.findViewById(R.id.lblTotalReunido);
        lblTotalPagar = rootView.findViewById(R.id.lblTotalPagar);
        lblTotalPagado = rootView.findViewById(R.id.lblTotalPagado);
        btnPagar = rootView.findViewById(R.id.btnPagar);
        btnAddCta = rootView.findViewById(R.id.btnAddCta);
        spinnerAlumnos = rootView.findViewById(R.id.spSelecAlumno);
        spinnerCursos = rootView.findViewById(R.id.spSelecCurso);
        ocultarCampos();
    }

    public void ocultarCampos(){
        lblNroCta.setVisibility(View.INVISIBLE);
        lblNomCurso.setVisibility(View.INVISIBLE);
        lblTotalReunido.setVisibility(View.INVISIBLE);
        lblTotalPagar.setVisibility(View.INVISIBLE);
        lblTotalPagado.setVisibility(View.INVISIBLE);
        btnPagar.setVisibility(View.INVISIBLE);
        btnAddCta.setVisibility(View.INVISIBLE);
    }

    public void agregarCuenta(){
        Random random = new Random();

        String nroCuenta =  String.valueOf(random.nextInt(999999999));

        Response.Listener<String> responseListenerSpinner = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonResponse;
                try {
                    jsonResponse = new JSONObject(response);
                    //corresponde a la respusta del webService.
                    boolean success = jsonResponse.getBoolean("success");

                    if (success == true){
                        Toast.makeText(getActivity().getApplicationContext(), "Cuenta generada con exito", Toast.LENGTH_SHORT).show();
                        llenarSpinnerAlumno();
                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        //Segun el mensaje nos indica el error.
                        builder.setMessage("Error con generación")
                                .setNegativeButton("Retry",null)
                                .create().show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity().getApplicationContext(), "Problemas al crear cuenta", Toast.LENGTH_SHORT).show();
                }
            }
        };

        AgregarCuentaRequest agregarCuentaRequest = new AgregarCuentaRequest(nroCuenta,rutAlum, responseListenerSpinner);
        RequestQueue queueSpinner = Volley.newRequestQueue(getActivity().getApplicationContext());
        queueSpinner.add(agregarCuentaRequest);
    }

    public void llenarSpinnerCurso(){
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
                    comboAdapterCursos = new AdaptadorSpinnerCurso(getActivity().getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, datosSpinner);
                    spinnerCursos.setAdapter(comboAdapterCursos);
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        };
        TraerAlumnosRequest traerAlumnosRequest = new TraerAlumnosRequest(respoListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(traerAlumnosRequest);
    }

    /*public void llenarSpinnerCurso(){
        Response.Listener<String> respoListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                cursos.clear();
                try {
                    //JSONObject jo = new JSONObject(response);
                    JSONArray jaAlumnos = new JSONArray(response);
                    //JSONArray jaAlumnos = jo.getJSONArray("ArrayOfAlumno");

                    for (int i = 0; i < jaAlumnos.length(); i++){
                        JSONObject jsonAlumno = jaAlumnos.getJSONObject(i);
                        JSONObject jsonApoderado = jsonAlumno.getJSONObject("Apoderado");
                        JSONObject jsonUsuario = jsonApoderado.getJSONObject("Usuario");
                        JSONObject jsonCurso = jsonAlumno.getJSONObject("Curso");
                        JSONObject jsonColegio = jsonCurso.getJSONObject("Colegio");
                        if (jsonUsuario.getInt("Rut") == Integer.parseInt(rutApo)){
                            cursos.add(new Curso(jsonCurso.getInt("Id"),
                                    jsonCurso.getString("Nombre").trim(),
                                    jsonColegio.getInt("Id"),
                                    jsonCurso.getInt("TotalReunido")));

                            comboAdapterCursos = new AdaptadorSpinnerCurso(getActivity().getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, cursos);
                            spinnerCursos.setAdapter(comboAdapterCursos);
                        }
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        };
        TraerAlumnosRequest traerAlumnosRequest = new TraerAlumnosRequest(respoListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(traerAlumnosRequest);
    }*/

    public void llenarSpinnerAlumno(){
        Response.Listener<String> respoListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                alumnos.clear();
                try {
                    //JSONObject jo = new JSONObject(response);
                    JSONArray jaAlumnos = new JSONArray(response);
                    //JSONArray jaAlumnos = jo.getJSONArray("ArrayOfAlumno");

                    for (int i = 0; i < jaAlumnos.length(); i++){
                        JSONObject jsonAlumno = jaAlumnos.getJSONObject(i);
                        JSONObject jsonApoderado = jsonAlumno.getJSONObject("Apoderado");
                        JSONObject jsonUsuario = jsonApoderado.getJSONObject("Usuario");
                        JSONObject jsonCurso = jsonAlumno.getJSONObject("Curso");
                        if (jsonUsuario.getInt("Rut") == Integer.parseInt(rutApo)){
                            alumnos.add(new Alumno(jsonAlumno.getInt("Rut"),
                                    (jsonAlumno.getString("Nombre").trim() + " " + jsonAlumno.getString("APaterno").trim() + " " +jsonAlumno.getString("AMaterno").trim()),
                                    jsonCurso.getInt("Id"),
                                    jsonUsuario.getString("Rut").trim()));


                            comboAdapterAlumnos = new AdaptadorSpinnerAlumno(getActivity().getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, alumnos);
                            spinnerAlumnos.setAdapter(comboAdapterAlumnos);
                        }
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        };
        TraerAlumnosRequest traerAlumnosRequest = new TraerAlumnosRequest(respoListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(traerAlumnosRequest);
    }

    public void existeCuenta(int alumRut){
        final String rutAlum = String.valueOf(alumRut);

        try
        {
            //Llena Spinner.
            Response.Listener<String> responseListenerSpinner = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //ArrayList<String> datos = new ArrayList<>();
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        //corresponde a la respusta del webService.
                        JSONObject jo= new JSONObject(response);
                        //extracting json array from response string
                        JSONArray ja = jo.getJSONArray("cuentas");

                        int largo = ja.length();
                        for (int i = 0; i < largo; i++) {
                            JSONObject jsonObject = ja.getJSONObject(i);
                            if (jsonObject.getString("nro_cuenta").equals("null")){
                                Toast.makeText(getActivity().getApplicationContext(), "Alumno sin cuenta... Registre cuenta", Toast.LENGTH_SHORT).show();
                                lblNroCta.setVisibility(View.INVISIBLE);
                                lblNomCurso.setVisibility(View.INVISIBLE);
                                lblTotalReunido.setVisibility(View.INVISIBLE);
                                lblTotalPagar.setVisibility(View.INVISIBLE);
                                lblTotalPagado.setVisibility(View.INVISIBLE);
                                btnPagar.setVisibility(View.INVISIBLE);
                                btnAddCta.setVisibility(View.VISIBLE);
                            }else{
                                llenarDatos(jsonObject.getString("nro_cuenta"), rutAlum);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "Problemas al traer datos", Toast.LENGTH_SHORT).show();
                    }
                }
            };
            ExisteCuentaRequest existeCuentaRequest = new ExisteCuentaRequest(rutAlum, responseListenerSpinner);
            RequestQueue queueSpinner = Volley.newRequestQueue(getActivity().getApplicationContext());
            queueSpinner.add(existeCuentaRequest);

        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void llenarDatos(final String nroCuenta, String alumRut){
        try
        {
            if (perfil.equals("apoderado")){
                //Llena campos.
                Response.Listener<String> responseListenerSpinner = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ArrayList<String> datos = new ArrayList<>();
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            //corresponde a la respusta del webService.
                            JSONObject jo= new JSONObject(response);
                            //extracting json array from response string
                            JSONArray ja = jo.getJSONArray("cuentas");

                            int largo = ja.length();
                            datos.add("Seleccione");
                            for (int i = 0; i < largo; i++) {
                                JSONObject jsonObject = ja.getJSONObject(i);
                                cuentas.add(new Cuenta(jsonObject.getInt("nro_cuenta"),
                                        jsonObject.getInt("total_reunido"),
                                        jsonObject.getInt("monto_a_pagar"),
                                        jsonObject.getInt("total_pagado"),
                                        jsonObject.getString("curso")));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try{
                            for(int i = 0; i < cuentas.size();i++){
                                //int nroCuenta = cuentas.get(i).getNroCuenta();
                                if (cuentas.get(i).getNroCuenta() == Integer.parseInt(nroCuenta)){
                                    lblNroCta.setText("Nro. cuenta: " + String.valueOf(cuentas.get(i).getNroCuenta()));
                                    lblNroCta.setVisibility(TextView.VISIBLE);

                                    lblNomCurso.setText("Curso: " + cuentas.get(i).getCurso());
                                    lblNomCurso.setVisibility(View.VISIBLE);

                                    lblTotalReunido.setText("Total reunido: " + String.valueOf(cuentas.get(i).getTotalReunido()));
                                    lblTotalReunido.setVisibility(View.VISIBLE);

                                    lblTotalPagar.setText("Monto a pagar: " + String.valueOf(cuentas.get(i).getMontoPagar()));
                                    lblTotalPagar.setVisibility(View.VISIBLE);

                                    lblTotalPagado.setText("Total pagado: " + String.valueOf(cuentas.get(i).getTotalPagado()));
                                    lblTotalPagado.setVisibility(View.VISIBLE);

                                    btnPagar.setVisibility(View.VISIBLE);
                                    btnAddCta.setVisibility(View.INVISIBLE);
                                    nroCta = String.valueOf(cuentas.get(i).getNroCuenta());
                                    nomCurso = String.valueOf(cuentas.get(i).getCurso());
                                    break;
                                }
                            }
                        }catch(Exception e)
                        {
                            Toast.makeText(getActivity().getApplicationContext(), "Error al traer datos", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                };

                CuentaRequest cuentaRequest = new CuentaRequest(nroCuenta, alumRut, responseListenerSpinner);
                RequestQueue queueSpinner = Volley.newRequestQueue(getActivity().getApplicationContext());
                queueSpinner.add(cuentaRequest);
            }else{
                for(int i = 0; i < cursos.size();i++){
                    if (cursos.get(i).getIdCurso() == Integer.parseInt(alumRut)){
                        lblNroCta.setText("Nro. cuenta: " + String.valueOf(000000000));
                        lblNroCta.setVisibility(TextView.VISIBLE);

                        lblNomCurso.setText("Curso: " + cursos.get(i).getNombre());
                        lblNomCurso.setVisibility(View.VISIBLE);

                        lblTotalReunido.setText("Total reunido: $" + String.valueOf(00000));
                        lblTotalReunido.setVisibility(View.VISIBLE);

                        lblTotalPagar.setText("Monto a pagar: $" + String.valueOf(00000));
                        lblTotalPagar.setVisibility(View.VISIBLE);

                        lblTotalPagado.setText("Total pagado: $" + String.valueOf(00000));
                        lblTotalPagado.setVisibility(View.VISIBLE);

                        btnPagar.setVisibility(View.VISIBLE);
                        btnAddCta.setVisibility(View.INVISIBLE);
                        nroCta = String.valueOf(000000000);
                        nomCurso = String.valueOf(cursos.get(i).getNombre());
                        break;
                    }
                }
            }


        }catch(Exception e)
        {
            e.printStackTrace();
        }
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

    public class AdaptadorSpinnerAlumno extends ArrayAdapter<Alumno> {

        private Context context;
        private ArrayList<Alumno> datos;

        //Constructor, parametros: contexto (actividad donde estamos), array de datos.
        public AdaptadorSpinnerAlumno(Context context, int textViewResourceId,ArrayList<Alumno> datos) {
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
        public Alumno getItem(int position){
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

            label.setText(datos.get(position).getNombreCompleto());

            return label;
        }

        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            TextView label = (TextView) super.getDropDownView(position, convertView, parent);
            label.setTextColor(Color.BLACK);
            label.setText(datos.get(position).getNombreCompleto());

            return label;
        }
    }

    public interface pago{
        void pagar (String nroCta, String nomCurso, String nomAlum);
    }
}
