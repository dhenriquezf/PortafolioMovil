package henriquez.daniel.prueba.app.com.login.Requests;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL="http://portafolio-duoc.centralus.cloudapp.azure.com:82/api/usuario/existe";
    private Map<String,String> params;

    //Constructor.
    public LoginRequest(String username, String pass, Response.Listener<String> listener){
        super(Request.Method.POST, REGISTER_REQUEST_URL, listener,null);
        params = new HashMap<>();
        params.put("Rut",username);
        params.put("Password",pass);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
