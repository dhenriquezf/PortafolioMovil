package henriquez.daniel.prueba.app.com.login;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

import henriquez.daniel.prueba.app.com.login.Clases.Perfil;
import henriquez.daniel.prueba.app.com.login.Clases.Usuario;
import henriquez.daniel.prueba.app.com.login.Requests.LoginRequest;
import henriquez.daniel.prueba.app.com.login.Requests.TraerUsuarioRequest;

public class MainActivity extends AppCompatActivity {

    //Declaracion de variables
    EditText etUsuario, etPassword;
    Button btn_login;
    String userName;
    String password;
    Usuario usuario;
    int validaTipoUsr = 0;
    ArrayList<Perfil> perfiles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUsuario = findViewById(R.id.usuario_et);
        etPassword = findViewById(R.id.pass_et);

        btn_login = findViewById(R.id.iniciar_btn);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validaUsuario();
            }
        });
    }

    public void validaUsuario(){
        userName = etUsuario.getText().toString();
        password = etPassword.getText().toString();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                boolean success = Boolean.parseBoolean(response);

                if (success == true){
                    buscaDatosUsuario();
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    //Segun el mensaje nos indica el error.
                    builder.setMessage("error Login")
                            .setNegativeButton("Retry",null)
                            .create().show();
                }
            }
        };

        LoginRequest loginRequest = new LoginRequest(userName,password, responseListener);
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(loginRequest);
    }

    public void buscaDatosUsuario(){
        final ArrayList<String> perf = new ArrayList<>();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                perf.clear();
                perfiles.clear();
                //Creating JsonObject from response String
                try {
                    JSONObject jo = new JSONObject(response);
                    JSONArray ja = jo.getJSONArray("Lista_Perfiles");

                    for (int i = 0; i < ja.length(); i++){
                        JSONObject jsonPerfil = ja.getJSONObject(i);
                        if (jsonPerfil.getString("Tipo").trim().toLowerCase().equals("apoderado") || jsonPerfil.getString("Tipo").trim().toLowerCase().equals("encargado curso")){
                            perfiles.add(new Perfil(jsonPerfil.getInt("Id"),
                                    jsonPerfil.getString("Tipo")));
                            perf.add(jsonPerfil.getString("Tipo").trim());
                            validaTipoUsr = 1;
                        }

                    }

                    usuario = new Usuario(jo.getInt("Rut"),
                                            jo.getString("DigitoV"),
                                            jo.getString("Correo"),
                                            jo.getString("Password"),
                                            jo.getString("Nombre"),
                                            jo.getString("APaterno"),
                                            jo.getString("AMaterno"));


                    if (validaTipoUsr == 1){
                        Intent intent = new Intent(MainActivity.this, SelectPerfil.class);
                        Bundle bEmisor = new Bundle();
                        bEmisor.putSerializable("usuario", usuario);
                        intent.putExtras(bEmisor);
                        intent.putExtra("perfiles",perf);
                        //intent.putExtra();
                        MainActivity.this.startActivity(intent);
                    }else{
                        Toast.makeText(MainActivity.this, "perfil administrativo - acceder al portal web", Toast.LENGTH_LONG).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        TraerUsuarioRequest traerUsuarioRequest = new TraerUsuarioRequest(userName, responseListener);
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(traerUsuarioRequest);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Toast.makeText(MainActivity.this, "Debe iniciar sesión", Toast.LENGTH_LONG).show();
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
