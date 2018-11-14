package henriquez.daniel.prueba.app.com.login.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import henriquez.daniel.prueba.app.com.login.R;
import henriquez.daniel.prueba.app.com.login.Requests.AportesRequest;

public class FragmentDetalleAporte extends Fragment {

    String nomCurso;
    String rutApo;
    ListView lvAportes;
    ArrayList<String[]> aportes = new ArrayList<>();
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_fragment_detalle_aporte, container, false);

        setearCampos();
        llenarLista();

        return rootView;
    }

    public void llenarLista(){
        try
        {
            Response.Listener<String> respoListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jo = new JSONObject(response);
                        JSONArray ja = jo.getJSONArray("aporteAlum");

                        aportes.clear();
                        int largo = ja.length();
                        for (int j = 0; j < largo; j++){
                            JSONObject jsonObject = ja.getJSONObject(j);
                            aportes.add(new String[]{String.valueOf(jsonObject.getInt("nro_pago")),
                                                    String.valueOf(jsonObject.getInt("nro_cuenta")),
                                                    String.valueOf(jsonObject.getInt("monto"))});
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    AdaptadorAportes adaptador = new AdaptadorAportes(getActivity().getApplicationContext(), aportes);
                    lvAportes.setAdapter(adaptador);
                }
            };
            AportesRequest aportesRequest = new AportesRequest(rutApo,nomCurso, respoListener);
            RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
            queue.add(aportesRequest);

        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void setearCampos(){
        nomCurso = getArguments().getString("rutAlum");
        rutApo = String.valueOf(getArguments().getInt("rutApoderado"));
        lvAportes = rootView.findViewById(R.id.listaAportes);
    }

    class AdaptadorAportes extends ArrayAdapter<String[]> {

        private Context context;
        private ArrayList<String[]> datos;

        //Constructor, parametros: contexto (actividad donde estamos), array de datos.
        public AdaptadorAportes(Context context, ArrayList<String[]> datos) {
            //llamada al constructor padre. (extends).
            super(context, R.layout.list_item_detalle_aporte, datos);
            this.context = context;
            this.datos = datos;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View item = inflater.inflate(R.layout.list_item_detalle_aporte, null);

            String[] row = datos.get(position);

            TextView lblNroPago = (TextView)item.findViewById(R.id.LblNroPago);
            lblNroPago.setText("Nro. Pago: " + String.valueOf(row[0]));

            TextView lblNroCta = (TextView)item.findViewById(R.id.LblNroCta);
            lblNroCta.setText("Nro. Cuenta: " + String.valueOf(row[1]));

            TextView lblValorPago = (TextView)item.findViewById(R.id.LblValorPago);
            lblValorPago.setText("Monto: " + String.valueOf(row[2]));

            return item;
        }
    }
}
