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
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

import henriquez.daniel.prueba.app.com.login.Clases.Alumno;
import henriquez.daniel.prueba.app.com.login.Clases.Contrato;
import henriquez.daniel.prueba.app.com.login.Clases.Curso;
import henriquez.daniel.prueba.app.com.login.Clases.Destino;
import henriquez.daniel.prueba.app.com.login.Clases.Seguro;
import henriquez.daniel.prueba.app.com.login.Clases.Servicio;
import henriquez.daniel.prueba.app.com.login.R;
import henriquez.daniel.prueba.app.com.login.Reportes.TemplatePDF;
import henriquez.daniel.prueba.app.com.login.Requests.TraerAlumnosRequest;
import henriquez.daniel.prueba.app.com.login.Requests.TraerContratoCursoRequest;
import henriquez.daniel.prueba.app.com.login.Requests.TraerCursosRequest;

public class FragmentContratoAp extends Fragment {

    ArrayAdapter<Curso> comboAdapterCursos;
    ArrayList<Contrato> contratos = new ArrayList<>();
    ArrayList<Alumno> alumnosCurso = new ArrayList<>();
    ArrayList<Destino> destinos = new ArrayList<>();
    ArrayList<Alumno> alumnos = new ArrayList<>();
    ArrayList<Curso> cursos = new ArrayList<>();
    ArrayList<Servicio> servicios = new ArrayList<>();
    ArrayList<Seguro> seguros = new ArrayList<>();
    ArrayList<Curso> datosSpinner = new ArrayList<>();
    TextView lblCurso;
    TextView lblDescripcion;
    TextView lblFechaViaje;
    TextView lblValorTotal;
    TextView lblDestinosValor;
    TextView lblServicioValor;
    TextView lblSeguroValor;
    View rootView;
    String rutApo;
    String perfil;
    String nomEncargado;
    Spinner spinnerAlumnos;
    Spinner spinnerCursos;
    Button btnPdf;
    Button btnServicios;
    Button btnPoliza;
    Button btnDestinos;
    Curso curso;
    Contrato contrato;
    Destino destino;
    int idCurso;
    int valorServicios;
    int valorSeguros;
    int valorDestinos;
    int rutEncargado;

    private TemplatePDF templatePDF;
    private String fechaText;
    private String cursoText;
    private String descripText;
    private String valorText;
    private String cantServText;
    private String cantSeguText;
    private String valorSeguroText;
    private String valorServicioText;
    private String valorDestinoText;

    public comunicaActivity implementacion;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        if(context instanceof comunicaActivity){
            this.implementacion = (comunicaActivity)context;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /** Se instancian los elementos que van a realizar la comunicacion con el
         * otro fragment*/

        btnServicios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /** Se envia atraves de la interface el mensaje */
                implementacion.comunicaServicios(idCurso);
            }
        });

        btnDestinos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /** Se envia atraves de la interface el mensaje */
                implementacion.comunicaDestinos(idCurso);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_fragment_contrato_ap, container, false);

        setearValores();

        btnPoliza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generarPoliza();
            }
        });

        btnPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generarReporte();
            }
        });

        spinnerCursos.setVisibility(View.VISIBLE);
        spinnerCursos.setEnabled(true);
        spinnerAlumnos.setVisibility(View.INVISIBLE);
        spinnerAlumnos.setEnabled(false);
        llenarSpinner();

        spinnerCursos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Curso elementoSpinner = (Curso) spinnerCursos.getItemAtPosition(position);
                String textSpinner = elementoSpinner.getNombre();
                if (!textSpinner.equalsIgnoreCase("Seleccione")){
                    curso = (Curso) parent.getSelectedItem();
                    traerAlumnosCursos(curso);
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

    public void traerAlumnosCursos(final Curso curso){
        Response.Listener<String> respoListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                alumnosCurso.clear();
                try {
                    JSONArray jaAlumnos = new JSONArray(response);

                    for (int i = 0; i < jaAlumnos.length(); i++){
                        JSONObject jsonAlumno = jaAlumnos.getJSONObject(i);
                        JSONObject jsonApoderado = jsonAlumno.getJSONObject("Apoderado");
                        JSONObject jsonUsuario = jsonApoderado.getJSONObject("Usuario");
                        JSONObject jsonCurso = jsonAlumno.getJSONObject("Curso");
                        if (jsonCurso.getInt("Id") == curso.getIdCurso()){
                            alumnosCurso.add(new Alumno(jsonAlumno.getInt("Rut"),
                                    (jsonAlumno.getString("Nombre").trim() + " " + jsonAlumno.getString("APaterno").trim() + " " +jsonAlumno.getString("AMaterno").trim()),
                                    jsonCurso.getInt("Id"),
                                    jsonUsuario.getString("Rut").trim()));
                        }
                    }

                    traerContrato(curso);
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        };
        TraerAlumnosRequest traerAlumnosRequest = new TraerAlumnosRequest(respoListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(traerAlumnosRequest);
    }

    public void traerAlumnos(){
        Response.Listener<String> respoListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                alumnos.clear();
                //datosSpinner.add(new Curso(0,"Seleccione",0,0));
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
                    comboAdapterCursos = new AdaptadorSpinnerCurso(getActivity().getApplicationContext(),android.R.layout.simple_spinner_item, datosSpinner);
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

    public void setearValores(){
        rutApo = String.valueOf(getArguments().getInt("rutUsr"));
        perfil = String.valueOf(getArguments().getString("perfilUsr"));
        btnPoliza = rootView.findViewById(R.id.btnPoliza);
        btnServicios = rootView.findViewById(R.id.btnServicios);
        btnPdf = rootView.findViewById(R.id.btnPdf);
        btnDestinos = rootView.findViewById(R.id.btnDestinos);
        spinnerAlumnos = rootView.findViewById(R.id.spinnerAlumnos);
        spinnerCursos = rootView.findViewById(R.id.spinnerCursos);
        lblCurso = rootView.findViewById(R.id.lblCurso);
        lblDescripcion = rootView.findViewById(R.id.lblDescripcion);
        lblFechaViaje = rootView.findViewById(R.id.lblFechaViaje);
        lblValorTotal = rootView.findViewById(R.id.lblValorTotal);
        lblDestinosValor = rootView.findViewById(R.id.lblDestinosValor);
        lblServicioValor = rootView.findViewById(R.id.lblServicioValor);
        lblSeguroValor = rootView.findViewById(R.id.lblSeguroValor);
        ocultarCampos();
    }

    public void ocultarCampos(){
        lblCurso.setVisibility(View.INVISIBLE);
        lblDescripcion.setVisibility(View.INVISIBLE);
        lblFechaViaje.setVisibility(View.INVISIBLE);
        lblValorTotal.setVisibility(View.INVISIBLE);
        lblDestinosValor.setVisibility(View.INVISIBLE);
        lblServicioValor.setVisibility(View.INVISIBLE);
        lblSeguroValor.setVisibility(View.INVISIBLE);
        btnPdf.setVisibility(View.INVISIBLE);
        btnServicios.setVisibility(View.INVISIBLE);
        btnPoliza.setVisibility(View.INVISIBLE);
        btnDestinos.setVisibility(View.INVISIBLE);
    }

    public void traerContrato(final Curso curso){
        Response.Listener<String> respoListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                SimpleDateFormat d = new SimpleDateFormat("dd-MM-yy");
                Date date;
                servicios.clear();
                seguros.clear();
                destinos.clear();
                contrato = null;
                try {
                    JSONArray jaContrato = new JSONArray(response);

                    for (int i = 0; i < jaContrato.length(); i++){
                        JSONObject jsonContrato = jaContrato.getJSONObject(i);
                        JSONObject jsonCurso = jsonContrato.getJSONObject("Curso");

                        if (jsonCurso.getInt("Id") == curso.getIdCurso()){
                            JSONArray jaServiciosLista = jsonContrato.getJSONArray("ListaServiciosAsociados");
                            for (int j = 0; j < jaServiciosLista.length(); j++){
                                JSONObject joServicioAsoci = jaServiciosLista.getJSONObject(j);
                                JSONObject joServicio = joServicioAsoci.getJSONObject("Servicio");
                                servicios.add(new Servicio(joServicio.getInt("Id"),
                                        joServicio.getString("Descripcion").trim(),
                                        joServicio.getString("Nombre").trim(),
                                        joServicio.getInt("Valor")));
                            }
                            JSONArray jaSegurosLista = jsonContrato.getJSONArray("ListaSeguroAsociados");
                            for (int j = 0; j < jaSegurosLista.length(); j++){
                                JSONObject joSeguroAsoci = jaSegurosLista.getJSONObject(j);
                                JSONObject joSeguro = joSeguroAsoci.getJSONObject("Seguro");
                                seguros.add(new Seguro(joSeguro.getInt("Id"),
                                        joSeguro.getString("Nombre").trim(),
                                        joSeguro.getString("Descripcion").trim(),
                                        joSeguro.getInt("Dias_Cobertura"),
                                        joSeguroAsoci.getInt("Valor"),
                                        joSeguroAsoci.getInt("Tipo_Seguro")));
                            }
                            JSONArray jaDestinosLista = jsonContrato.getJSONArray("ListaDestinosAsociados");
                            for (int j = 0; j < jaDestinosLista.length(); j++){
                                JSONObject joDestinoAsoci = jaDestinosLista.getJSONObject(j);
                                JSONObject joDestino = joDestinoAsoci.getJSONObject("Destino");
                                destinos.add(new Destino(joDestino.getInt("Id"),
                                        joDestino.getString("Nombre").trim(),
                                        joDestino.getInt("Valor")));
                            }
                            int cantDestinos = destinos.size();
                            int cantServicios = servicios.size();
                            int cantSerguros = seguros.size();

                            date = d.parse(jsonContrato.getString("Fecha_Viaje").trim());
                            String fecha = d.format(date);

                            contrato = new Contrato(jsonContrato.getInt("Id"),
                                    jsonContrato.getString("Descripcion").trim(),
                                    jsonContrato.getString("Nombre").trim(),
                                    fecha,
                                    jsonContrato.getInt("Valor"),
                                    jsonCurso.getInt("Id"),
                                    cantServicios,
                                    cantSerguros,
                                    cantDestinos);
                            llenarCampos(contrato, curso);
                            break;
                        }else {
                            if (contrato == null){
                                Toast.makeText(getActivity().getApplicationContext(), "Curso sin contrato... Contacte al Ejecutivo", Toast.LENGTH_SHORT).show();
                            }
                            ocultarCampos();
                        }
                    }
                }catch (JSONException e){
                    Toast.makeText(getActivity().getApplicationContext(), "Error al traer datos", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                } catch (ParseException e) {
                    Toast.makeText(getActivity().getApplicationContext(), "Error al traer datos", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        };
        TraerContratoCursoRequest traerContratoCursoRequest = new TraerContratoCursoRequest(respoListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(traerContratoCursoRequest);
    }

    public void llenarCampos(Contrato contrato, Curso curso){
        try{
            valorDestinos = 0;
            valorServicios = 0;
            valorSeguros = 0;

            lblCurso.setText("Curso: " + curso.getNombre());
            lblCurso.setVisibility(TextView.VISIBLE);

            lblDescripcion.setText("Descripción: " + contrato.getDescripcion());
            lblDescripcion.setVisibility(View.VISIBLE);

            lblFechaViaje.setText("Fecha viaje: " + contrato.getFecha());
            lblFechaViaje.setVisibility(View.VISIBLE);

            lblValorTotal.setText("Valor: $" + String.valueOf(contrato.getTotalViaje()));
            lblValorTotal.setVisibility(View.VISIBLE);

            for (int i = 0; i < destinos.size(); i++){
                valorDestinos = valorDestinos + destinos.get(i).getValor();
            }
            //Destino destino = destinos.get(0);
            //valorDestinos = destino.getValor();

            lblDestinosValor.setText("Valor destinos: $" + String.valueOf(valorDestinos));
            lblDestinosValor.setVisibility(View.VISIBLE);

            for (int i = 0; i < servicios.size(); i++){
                valorServicios = valorServicios + servicios.get(i).getValor();
            }
            //Servicio servicio = servicios.get(0);
            //valorServicios = servicio.getValor();

            lblServicioValor.setText("Valor servicios: $" + String.valueOf(valorServicios));
            lblServicioValor.setVisibility(View.VISIBLE);

            for (int i = 0; i < seguros.size(); i++){
                valorSeguros = valorSeguros + seguros.get(i).getValor();
            }
            //Seguro seguro = seguros.get(0);
            //valorSeguros = seguro.getValor();

            lblSeguroValor.setText("Valor seguros: $" + String.valueOf(valorSeguros));
            lblSeguroValor.setVisibility(View.VISIBLE);

            btnPdf.setVisibility(View.VISIBLE);
            btnServicios.setVisibility(View.VISIBLE);
            btnPoliza.setVisibility(View.VISIBLE);
            btnDestinos.setVisibility(View.VISIBLE);

            idCurso = curso.getIdCurso();
        }catch(Exception e)
        {
            Toast.makeText(getActivity().getApplicationContext(), "Error al traer datos", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    //trae los datos en pantalla para el reporte.
    private void datosPoliza(){
        cursoText = String.valueOf(lblCurso.getText());
        descripText = String.valueOf(lblDescripcion.getText());
        fechaText = String.valueOf(lblFechaViaje.getText());
        valorText = String.valueOf(lblValorTotal.getText());
    }

    //crea t completa reporte.
    private void generarPoliza(){
        SimpleDateFormat d = new SimpleDateFormat("dd-MM-yy");
        Date date = new Date();
        String fechaActual = d.format(date);;

        datosPoliza();
        //Crear reporte.
        String[] header = {"CÓDIGO","NOMBRE","DESCRIPCIÓN","COBERTURA","VALOR ($)", "TIPO"};
        templatePDF = new TemplatePDF(getActivity());
        templatePDF.openDocument("SegurosPDF");
        templatePDF.addMetaData("Poliza","Poliza","OnTour");
        templatePDF.addTitles("POLIZA DE SEGURO DE ASISTENCIA A PERSONAS EN VIAJE", "________________________________________________", fechaActual);
        templatePDF.addParagraph("ARTICULO I:   OBJETO DEL SEGURO");
        templatePDF.addParagraph(
                "Mediante este seguro, la compañía se obliga a pagar las indemnizaciones y a efectuar las prestaciones que esta póliza contempla " +
                        "en favor de las personas aseguradas, respecto de una o varias contingencias que puedan sufrir durante un viaje y que se describen en este contrato de seguro.");
        templatePDF.addParagraph("ARTICULO II:  PERSONAS ASEGURADAS");
        templatePDF.addParagraph("Para los efectos de esta póliza, se entiende que quedan protegidas por la cobertura del seguro las siguientes personas:");
        templatePDF.addParagraph("a)    La persona que aparezca como asegurado titular según las Condiciones Particulares de la póliza.");
        templatePDF.addParagraph("Cada vez que en esta póliza se use la expresión \"asegurado\", se entiende que ella incluye a todas las personas mencionadas en este artículo.");
        templatePDF.addParagraph("ARTICULO III: VIGENCIA Y AMBITO TERRITORIAL DE LA COBERTURA");
        templatePDF.addParagraph("El tiempo de vigencia se establece en las Condiciones Particulares de esta póliza.");
        templatePDF.addParagraph(
                "El derecho a las indemnizaciones y prestaciones contempladas en esta póliza se refiere a contingencias que ocurran a una distancia superior a " +
                        "20 kilómetros contados desde el domicilio del asegurado que figura en las Condiciones Particulares de la Póliza.");
        templatePDF.addParagraph(
                "El ámbito territorial de las coberturas se extiende a todo el mundo, cualquiera que sea el medio de transporte utilizado, " +
                        "siempre que la permanencia del asegurado fuera de su residencia habitual con motivo del viaje, no sea superior a 60 días.");
        templatePDF.addParagraph("ARTICULO IV:   LISTA DE SEGUROS");
        templatePDF.createTable(header,getPolizas());
        templatePDF.closeDocument();

        //visualiza en la aplicacion
        templatePDF.viewPDF();
        //visualiza en app externa.
        //templatePDF.viewPDF(getActivity());
    }

    //busca datos para los servicios.
    private ArrayList<String[]> getPolizas(){
        ArrayList<String[]> rows = new ArrayList<>();
        ArrayList<Seguro> segurosReport;

        for(int i = 0; i < seguros.size();i++){
            String nombre = seguros.get(i).getNombre();
            int cantSeguros = seguros.size();
            segurosReport = (ArrayList<Seguro>) seguros.clone();
            for (int j = 0; j < cantSeguros; j++ ){
                rows.add(new String[]{String.valueOf(segurosReport.get(j).getId()),
                        segurosReport.get(j).getNombre(),
                        segurosReport.get(j).getDescripcion(),
                        String.valueOf(segurosReport.get(j).getCobertura()),
                        String.valueOf(segurosReport.get(j).getValor()),
                        String.valueOf(segurosReport.get(j).getTipo())});
            }
        }
        return rows;
    }


    //trae los datos en pantalla para el reporte.
    private void datosReporte(){
        cursoText = String.valueOf(lblCurso.getText());
        descripText = String.valueOf(lblDescripcion.getText());
        fechaText = String.valueOf(lblFechaViaje.getText());
        valorText = String.valueOf(lblValorTotal.getText());
        valorSeguroText = String.valueOf(lblSeguroValor.getText());
        valorServicioText = String.valueOf(lblServicioValor.getText());
        valorDestinoText = String.valueOf(lblDestinosValor.getText());
    }

    //crea t completa reporte.
    private void generarReporte(){
        SimpleDateFormat d = new SimpleDateFormat("dd-MM-yy");
        Date date = new Date();
        String fechaActual = "";
        String fechaInicio = "";
        String fechaFin = "";
        try {
            fechaActual = d.format(date);
            fechaInicio = contrato.getFecha();
            Calendar ahoraCal = Calendar.getInstance();
            Date date2 = d.parse(fechaInicio);
            ahoraCal.setTime(date2);
            ahoraCal.add(Calendar.DATE, 7);
            fechaFin = d.format(ahoraCal.getTime());
            fechaInicio = d.format(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String listaAlumnos = "";
        for (int i = 0; i < alumnosCurso.size(); i++){
            listaAlumnos = listaAlumnos + ", " + alumnosCurso.get(i).getNombreCompleto();
        }



        datosReporte();
        //Crear reporte.
        String[] headerServicio = {"CÓDIGO","NOMBRE","DESCRIPCIÓN","VALOR ($)"};
        String[] headerDestinos = {"CÓDIGO","NOMBRE","VALOR ($)"};
        String[] headerSeguro = {"CÓDIGO","NOMBRE","DESCRIPCIÓN","COBERTURA","VALOR ($)", "TIPO"};
        templatePDF = new TemplatePDF(getActivity());
        templatePDF.openDocument("ContratoPDF");
        templatePDF.addMetaData("Contrato","Contrato","OnTour");
        templatePDF.addTitles("Contrato de servicios", "On Tour", fechaActual);
        templatePDF.addParagraph("CONTRATO DE PRESTACION DE SERVICIOS TURISTICOS   Nº " + contrato.getId());
        templatePDF.addParagraph("Fecha: " + fechaActual);
        templatePDF.addParagraph("REUNIDOS");
        templatePDF.addParagraph(
                "De una parte "+ curso.getNombre().trim() +" con DNI: "+ String.valueOf(curso.getIdCurso()) +" en adelante CLIENTE, que actúa en nombre propio y también en representación de " +
                        "" + listaAlumnos + " todos ellos miembros del mismo grupo de viaje. " +
                        "Y de la otra parte, la entidad mercantil OnTour, que gira comercialmente bajo la marca registrada  OnTour, en adelante AGENCIA," +
                        " que actúa en nombre propio y en calidad de organizadora del viaje objeto de este contrato." +
                        "Se reconocen mutuamente plena capacidad para suscribir el presente CONTRATO DE VIAJE, de conformidad con lo que dispone la ley de las Agencias de Viajes y formalizan los siguientes.");
        templatePDF.addParagraph("PACTO");
        templatePDF.addParagraph("I. Duración del viaje");
        templatePDF.addParagraph("Fecha de inicio: "+ fechaInicio);
        templatePDF.addParagraph("Fecha de finalización: "+ fechaFin);
        templatePDF.addParagraph("II. Identificación del viaje");
        templatePDF.addParagraph("Este contrato tiene por objeto el viaje llamado "+ contrato.getNombre() +" y la descripción del mismo figura mas adelante. En este se especifica detalladamente todo el viaje (Destinos, " +
                ", servicios incluidos, seguros incluidos).");
        templatePDF.addParagraph("III. Precio del viaje");
        templatePDF.addParagraph("Total, impuestos incluidos: $" + String.valueOf(contrato.getTotalViaje()));
        templatePDF.addParagraph("Precio de seguro(s): $" + valorSeguros);
        templatePDF.addParagraph("Gastos de anulación: En el supuesto de que el cliente anule el viaje antes de la salida la agencia organizadora tendrá derecho al cobro de las siguientes cantidades: ");
        templatePDF.addParagraph("•Gastos de gestión.");
        templatePDF.addParagraph("•Gastos de anulación debidamente justificados.");
        templatePDF.addParagraph("•Penalización consistente en: un 5% si se produce con más de 10 días antes de la salida del viaje y menos de 15, un 15% entre 10 y 3 días antes y un 25% en las 48hs. anteriores. Si el \" +\n" +
                "\"cliente no se presentara a la salida los gastos de anulación serían del 100%.");
        templatePDF.addParagraph("El cliente manifiesta que ha sido informado de la situación y requisitos del país/países objeto de su viaje de acuerdo con la información publicada en la página web del Ministerio de " +
                "Asuntos Exteriores www.mae.es y que conoce, por tanto, las características y posibles riesgos de toda índole del país/países de destino.");
        templatePDF.addParagraph("Reclamaciones: El consumidor está obligado a comunicar a la mayor brevedad posible al prestador del que se trate, cualquier incumplimiento en la ejecución de los servicios de forma " +
                        "fehaciente, así como a ponerlo en conocimiento de la agencia a la mayor brevedad posible. Las acciones de reclamación prescriben por el transcurso de dos años.");
        templatePDF.addParagraph("IV. Destinos incluidos");
        templatePDF.createTable(headerDestinos,getDestinos());
        templatePDF.addParagraph("V. Servicios incluidos");
        templatePDF.createTable(headerServicio,getServicios());
        templatePDF.addParagraph("VI. Seguros incluidos");
        templatePDF.createTable(headerSeguro,getPolizas());
        templatePDF.addParagraph("La no aceptación de lo anterior supondrá que no se podrán efectuar  los servicios esperados por parte de la agencia.");
        templatePDF.addParagraph("El abajo firmante manifiesta su capacidad para firmar en nombre de los restantes contratantes del viaje.");
        templatePDF.addParagraph("En prueba de conformidad, firman el presente contrato por duplicado:");
        templatePDF.addParagraph(" ");
        templatePDF.addParagraph(" ");
        templatePDF.addParagraph(" ");
        templatePDF.addParagraph("          EL CLIENTE                                                                          LA AGENCIA");
        templatePDF.closeDocument();

        //visualiza en la aplicacion
        templatePDF.viewPDF();
        //visualiza en app externa.
        //templatePDF.viewPDF(getActivity());
    }

    //busca datos para los servicios.
    private ArrayList<String[]> getServicios(){
        ArrayList<String[]> rows = new ArrayList<>();
        ArrayList<Servicio> serviciosReport;

        for(int i = 0; i < servicios.size();i++){
            int cantServicios = servicios.size();
            serviciosReport = (ArrayList<Servicio>) servicios.clone();
            for (int j = 0; j < cantServicios; j++ ){
                rows.add(new String[]{String.valueOf(serviciosReport.get(j).getId()),
                        serviciosReport.get(j).getNombre(),
                        serviciosReport.get(j).getDescripcion(),
                        String.valueOf(serviciosReport.get(j).getValor())});
            }
        }
        //rows.add(new String[]{"1","Pedro","Lopez"});
        return rows;
    }

    //busca datos para los servicios.
    private ArrayList<String[]> getDestinos(){
        ArrayList<String[]> rows = new ArrayList<>();
        ArrayList<Destino> destinosReport;

        for(int i = 0; i < destinos.size();i++){
            int cantDestinos = destinos.size();
            destinosReport = (ArrayList<Destino>) destinos.clone();
            for (int j = 0; j < cantDestinos; j++ ){
                rows.add(new String[]{String.valueOf(destinosReport.get(j).getId()),
                        destinosReport.get(j).getNombre(),
                        String.valueOf(destinosReport.get(j).getValor())});
            }
        }
        //rows.add(new String[]{"1","Pedro","Lopez"});
        return rows;
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

    public interface comunicaActivity{
        void comunicaServicios (int cursoId);
        void comunicaDestinos (int cursoId);
    }

    /*
    public interface comunicaActivity{
        void comunica (String alumnoNombre);
    }
    */
}

