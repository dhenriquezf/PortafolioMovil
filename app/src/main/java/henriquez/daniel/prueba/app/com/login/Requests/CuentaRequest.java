package henriquez.daniel.prueba.app.com.login.Requests;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class CuentaRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL="http://192.168.0.19/WebServiceApp/datosCuenta.php";
    private Map<String,String> params;

    //Constructor.
    public CuentaRequest(String nroCta, String alumRut, Response.Listener<String> listener){
        super(Request.Method.POST,REGISTER_REQUEST_URL,listener,null);
        params = new HashMap<>();
        params.put("nroCuenta", nroCta);
        params.put("alumRut", alumRut);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
