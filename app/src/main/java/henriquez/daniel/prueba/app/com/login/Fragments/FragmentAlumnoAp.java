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

import henriquez.daniel.prueba.app.com.login.Clases.Alumno;
import henriquez.daniel.prueba.app.com.login.R;
import henriquez.daniel.prueba.app.com.login.Requests.TraerAlumnosRequest;

public class FragmentAlumnoAp extends Fragment {

    String rutApo;
    ArrayAdapter<Alumno> comboAdapter;
    TextView lblRut;
    TextView lblNombre;
    TextView lblAporte;
    TextView lblSaldo;
    TextView lblTotal;
    View rootView;
    Spinner spinner;
    int rut;
    String perfil;
    Button btnAportes;
    String rutAlum;

    ArrayList<Alumno> alumnos = new ArrayList<>();

    public FragmentAlumnoAp.comunicaAct implementacion;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        if(context instanceof FragmentAlumnoAp.comunicaAct){
            this.implementacion = (FragmentAlumnoAp.comunicaAct)context;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /** Se instancian los elementos que van a realizar la comunicacion con el
         * otro fragment*/

        btnAportes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /** Se envia atraves de la interface el mensaje */
                implementacion.comunicaAct(rutAlum);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_fragment_alumno_ap, container, false);

        setearCampos();
        llenarSpinner(perfil);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!spinner.getItemAtPosition(position).toString().equals("Seleccione")){
                    Alumno alum = (Alumno) parent.getSelectedItem();
                    traerEstadoCta(alum);
                }else{
                    ocultarCampos();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return rootView;
    }

    public void llenarSpinner(final String perfil){
        Response.Listener<String> respoListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                alumnos.clear();
                try {
                    //JSONObject jo = new JSONObject(response);
                    JSONArray jaAlumnos = new JSONArray(response);
                    //JSONArray jaAlumnos = jo.getJSONArray("ArrayOfAlumno");

                    if (perfil.equals("apoderado")){
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
                                comboAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, alumnos);
                                spinner.setAdapter(comboAdapter);
                            }
                        }
                    }else{
                        int idCurso = 0;
                        for (int i = 0; i < jaAlumnos.length(); i++){
                            JSONObject jsonAlumno = jaAlumnos.getJSONObject(i);
                            JSONObject jsonApoderado = jsonAlumno.getJSONObject("Apoderado");
                            JSONObject jsonUsuario = jsonApoderado.getJSONObject("Usuario");
                            JSONObject jsonCurso = jsonAlumno.getJSONObject("Curso");
                            if (jsonUsuario.getInt("Rut") == Integer.parseInt(rutApo)){
                                idCurso = jsonCurso.getInt("Id");
                                break;
                            }
                        }
                        if (idCurso != 0){
                            for (int i = 0; i < jaAlumnos.length(); i++){
                                JSONObject jsonAlumno = jaAlumnos.getJSONObject(i);
                                JSONObject jsonApoderado = jsonAlumno.getJSONObject("Apoderado");
                                JSONObject jsonUsuario = jsonApoderado.getJSONObject("Usuario");
                                JSONObject jsonCurso = jsonAlumno.getJSONObject("Curso");
                                if (jsonCurso.getInt("Id") == idCurso){
                                    alumnos.add(new Alumno(jsonAlumno.getInt("Rut"),
                                            (jsonAlumno.getString("Nombre").trim() + " " + jsonAlumno.getString("APaterno").trim() + " " +jsonAlumno.getString("AMaterno").trim()),
                                            jsonCurso.getInt("Id"),
                                            jsonUsuario.getString("Rut").trim()));
                                }
                            }
                        }
                    }
                    comboAdapter = new AdaptadorSpinnerAlumno(getActivity().getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, alumnos);
                    spinner.setAdapter(comboAdapter);
                }catch (JSONException e){
                    Toast.makeText(getActivity().getApplicationContext(), "Error al traer datos", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        };
        TraerAlumnosRequest traerAlumnosRequest = new TraerAlumnosRequest(respoListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(traerAlumnosRequest);
    }

    public void traerEstadoCta(Alumno alum){
        llenarCampos(alum);
    }

    private void llenarCampos(Alumno alum){
        try{
            lblRut.setText("Rut: " + String.valueOf(alum.getId()));
            lblNombre.setText("Nombre: " + alum.getNombreCompleto());
            lblAporte.setText("Aporte: $" + String.valueOf(0));
            lblSaldo.setText("Saldo restante: $" + String.valueOf(0));
            lblTotal.setText("Total pagar: $" + String.valueOf(0));
            rutAlum = String.valueOf(alum.getId());

            mostrarCampos();
        }catch(Exception e)
        {
            Toast.makeText(getActivity().getApplicationContext(), "Error al mostrar datos", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void setearCampos(){
        rut = getArguments().getInt("rutUsr");
        rutApo = String.valueOf(rut);
        perfil = String.valueOf(getArguments().getString("perfilUsr"));
        lblRut = rootView.findViewById(R.id.lblRutAlumno);
        lblNombre = rootView.findViewById(R.id.lblNombreAlumno);
        lblAporte = rootView.findViewById(R.id.lblAporteAlumno);
        lblSaldo = rootView.findViewById(R.id.lblFaltanteAlumno);
        lblTotal = rootView.findViewById(R.id.lblTotalPagarAlumno);
        btnAportes = rootView.findViewById(R.id.btnAporte);
        spinner = rootView.findViewById(R.id.spinner);
        ocultarCampos();
    }

    public void ocultarCampos(){
        lblRut.setVisibility(View.INVISIBLE);
        lblNombre.setVisibility(View.INVISIBLE);
        lblAporte.setVisibility(View.INVISIBLE);
        lblSaldo.setVisibility(View.INVISIBLE);
        lblTotal.setVisibility(View.INVISIBLE);
        btnAportes.setVisibility(View.INVISIBLE);
    }

    public void mostrarCampos(){
        lblRut.setVisibility(View.VISIBLE);
        lblNombre.setVisibility(View.VISIBLE);
        lblAporte.setVisibility(View.VISIBLE);
        lblSaldo.setVisibility(View.VISIBLE);
        lblTotal.setVisibility(View.VISIBLE);
        btnAportes.setVisibility(View.VISIBLE);
    }

    public interface comunicaAct{
        void comunicaAct (String rutAlum);
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
}
