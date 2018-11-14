package henriquez.daniel.prueba.app.com.login.Requests;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class IngresarPagoRequest extends StringRequest{
    private static final String REGISTER_REQUEST_URL="http://192.168.0.19/WebServiceApp/ingresarPago.php";
    private Map<String,String> params;

    //Constructor.
    public IngresarPagoRequest(String nroCta, String mtoPago, String nroDep, String tipDep, String fecha, String descriptDep, Response.Listener<String> listener){
        super(Request.Method.POST,REGISTER_REQUEST_URL,listener,null);
        params = new HashMap<>();
        params.put("nroCta", nroCta);
        params.put("mtoPago", mtoPago);
        params.put("nroDep", nroDep);
        params.put("tipDep", tipDep);
        params.put("fecha", fecha);
        params.put("descriptDep", descriptDep);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
