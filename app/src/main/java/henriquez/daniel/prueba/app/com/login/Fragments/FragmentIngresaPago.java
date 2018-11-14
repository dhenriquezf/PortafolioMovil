package henriquez.daniel.prueba.app.com.login.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import henriquez.daniel.prueba.app.com.login.R;
import henriquez.daniel.prueba.app.com.login.Requests.CorreosApoderadosRequest;
import henriquez.daniel.prueba.app.com.login.Requests.IngresarPagoRequest;

public class FragmentIngresaPago extends Fragment {

    View rootView;
    EditText txtMonto;
    EditText txtDeposito;
    EditText txtDescrip;
    EditText txtFecha;
    Button btnPagar;

    String perfil;
    String nomAlum;
    String nomApo;
    String curso;
    int tipoDepo;
    String nroCuenta;
    String depocito;
    String descripcion;
    String fecha;
    String monto;
    String mensaje;
    ArrayList<String> listaEmails = new ArrayList<>();

    Session session = null;
    ProgressDialog pdialog = null;
    String correoEnvia = "lagarto.sparrow@gmail.com";
    String claveCorreo = "cuek12candy2103";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_fragment_ingresa_pago, container, false);

        setearValores();
        activarCampos();

        btnPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generarPago();
            }
        });

        return rootView;
    }

    public void generarPago(){
        try
        {
            if (txtMonto.getText().toString().length() == 0){
                Toast.makeText(getActivity(), "Ingrese el monto", Toast.LENGTH_SHORT).show();
            }else if (txtDeposito.getText().toString().length() == 0){
                Toast.makeText(rootView.getContext(), "Ingrese el nro. de depósito", Toast.LENGTH_SHORT).show();
            }else if (txtDescrip.getText().toString().length() == 0){
                Toast.makeText(getActivity().getApplicationContext(), "Ingrese una descripció", Toast.LENGTH_SHORT).show();
            }else if (txtFecha.getText().toString().length() == 0){
                Toast.makeText(getActivity().getApplicationContext(), "Ingrese la fecha del pago", Toast.LENGTH_SHORT).show();
            }else{

                monto = txtMonto.getText().toString();
                depocito = txtDeposito.getText().toString();
                descripcion = txtDescrip.getText().toString();
                fecha = txtFecha.getText().toString();
                if (perfil.equals("apoderado")){
                    tipoDepo = 1;
                }else{
                    tipoDepo = 2;
                }


                Response.Listener<String> responseListenerSpinner = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonResponse;
                        try {
                            jsonResponse = new JSONObject(response);
                            //corresponde a la respusta del webService.
                            boolean success = jsonResponse.getBoolean("success");

                            if (success == true){
                                //Toast.makeText(getActivity(), "Pago existoso", Toast.LENGTH_SHORT).show();
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                //Segun el mensaje nos indica el error.
                                builder.setMessage("Pago existoso")
                                        .setPositiveButton("Ok",null)
                                        .create().show();

                                txtMonto.setText("");
                                txtDeposito.setText("");
                                txtDescrip.setText("");
                                txtFecha.setText("");

                                traeEmailApoderado();

                            }else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                //Segun el mensaje nos indica el error.
                                builder.setMessage("Error al ingresar pago")
                                        .setNegativeButton("Retry",null)
                                        .create().show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                IngresarPagoRequest ingresarPagoRequest = new IngresarPagoRequest(nroCuenta,monto,depocito,String.valueOf(tipoDepo),fecha,descripcion, responseListenerSpinner);
                RequestQueue queueSpinner = Volley.newRequestQueue(getActivity().getApplicationContext());
                queueSpinner.add(ingresarPagoRequest);
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void enviarCorreo(){

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.user", correoEnvia);
        props.put("mail.password", claveCorreo);

        session = Session.getInstance(props, null);
        try{
            /*
            session = Session.getDefaultInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("lagarto.sparrow@gmail.com", "cuek12candy2103");
                }
            });
            */

            if (session != null){
                InternetAddress[] emails = new InternetAddress[listaEmails.size()];

                if (listaEmails.size() > 0){
                    for (int i = 0; i < listaEmails.size(); i++){
                        emails[i] = new InternetAddress(listaEmails.get(i));
                    }
                }

                mensaje = "Se notifica que el alumno: " + nomAlum + " A realizado un pago de: $" + monto + " Con fecha: " + fecha +
                        " Por medio del Apoderado: " + nomApo + " Al Curso: " + curso;

                StringBuffer msjHTML = new StringBuffer();
                msjHTML.append(mensaje);


                MimeMessage mimeMessage = new MimeMessage(session);
                mimeMessage.setFrom(new InternetAddress("OnTour@gmail.com", "Informe de pagos"));
                mimeMessage.setRecipients(Message.RecipientType.TO,emails);
                mimeMessage.setSubject("Notificación de Abono Alumno");
                Multipart multipart = new MimeMultipart();
                MimeBodyPart mimeBodyPart = new MimeBodyPart();
                mimeBodyPart.setContent(msjHTML.toString(), "text/html");
                multipart.addBodyPart(mimeBodyPart);
                mimeMessage.setContent(multipart);

                Transport transport = session.getTransport("smtp");
                transport.connect(correoEnvia, claveCorreo);
                transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
                transport.close();

                /*
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("lagarto.sparrow@gmail.com"));
                //message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("henriquezdaniel03@gmail.com"));
                message.setRecipients(Message.RecipientType.TO, emails);
                message.setSubject("Notificación de Abono Alumno");
                message.setContent(mensaje, "text/html; charset=utf-8");
                Transport.send(message);
                */
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void traeEmailApoderado(){
        Response.Listener<String> responseListenerSpinner = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    //corresponde a la respusta del webService.
                    JSONObject jo= new JSONObject(response);
                    //extracting json array from response string
                    JSONArray ja = jo.getJSONArray("emailApo");
                    int largo = ja.length();
                    for (int i = 0; i < largo; i++) {
                        JSONObject jsonObject = ja.getJSONObject(i);
                        listaEmails.add(jsonObject.getString("email"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                enviarCorreo();
            }
        };
        CorreosApoderadosRequest correosApoderadosRequest = new CorreosApoderadosRequest(responseListenerSpinner);
        RequestQueue queueSpinner = Volley.newRequestQueue(getActivity().getApplicationContext());
        queueSpinner.add(correosApoderadosRequest);
    }

    public void setearValores(){
        perfil = String.valueOf(getArguments().getString("perfilUsr"));
        nomApo = getArguments().getString("nomApo");
        curso = getArguments().getString("nomCurso");
        nomAlum = getArguments().getString("nomAlum");
        nroCuenta = String.valueOf(getArguments().getString("nroCta"));
        txtMonto = rootView.findViewById(R.id.txtMonto);
        txtDeposito = rootView.findViewById(R.id.txtDeposito);
        txtDescrip = rootView.findViewById(R.id.txtDescripcion);
        txtFecha = rootView.findViewById(R.id.txtFecha);
        btnPagar = rootView.findViewById(R.id.btnIngrePag);
    }

    public void activarCampos(){
        txtMonto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        txtDeposito.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        txtDescrip.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        txtFecha.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
