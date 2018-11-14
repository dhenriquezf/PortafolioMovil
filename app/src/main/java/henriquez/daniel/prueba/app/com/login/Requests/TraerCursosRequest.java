package henriquez.daniel.prueba.app.com.login.Requests;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

public class TraerCursosRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL="http://portafolio-duoc.centralus.cloudapp.azure.com:82/api/curso";

    //Constructor.
    public TraerCursosRequest(Response.Listener<String> listener) {
        super(Request.Method.GET, REGISTER_REQUEST_URL, listener, null);
    }
}
