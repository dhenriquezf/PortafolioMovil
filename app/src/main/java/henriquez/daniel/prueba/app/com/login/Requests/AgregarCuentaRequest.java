package henriquez.daniel.prueba.app.com.login.Requests;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class AgregarCuentaRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL="http://192.168.0.19/WebServiceApp/agregaCuenta.php";
    private Map<String,String> params;

    //Constructor.
    public AgregarCuentaRequest(String nroCuenta,String rutAlum,Response.Listener<String> listener){
        super(Request.Method.POST,REGISTER_REQUEST_URL,listener,null);
        params = new HashMap<>();
        params.put("nroCta", nroCuenta);
        params.put("rutAlum", rutAlum);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
