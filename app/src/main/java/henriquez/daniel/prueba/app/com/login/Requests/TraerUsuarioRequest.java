package henriquez.daniel.prueba.app.com.login.Requests;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class TraerUsuarioRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL="http://portafolio-duoc.centralus.cloudapp.azure.com:82/api/usuario/por-rut/";

    //Constructor.
    public TraerUsuarioRequest(String usrRut, Response.Listener<String> listener){
        super(Request.Method.GET, REGISTER_REQUEST_URL + usrRut, listener,null);
    }

}
