package com.julioflores.prueba1;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import cz.msebera.android.httpclient.Header;

public class EntradaFragment extends Fragment {
    View v;
    EditText mp1, nl1, c1, u1;
    Button bu1;
    AsyncHttpClient cliente;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_entrada, container, false);
        mp1 = (EditText) v.findViewById(R.id.matpri1);
        nl1 = (EditText) v.findViewById(R.id.lote1);
        c1 = (EditText) v.findViewById(R.id.cantidad1);
        u1 = (EditText) v.findViewById(R.id.ubicacion1);
        bu1 = (Button) v.findViewById(R.id.b1);
        cliente = new AsyncHttpClient();
        bu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObtenerAlmacenes();
                mp1.setText("");
                nl1.setText("");
                c1.setText("");
                u1.setText("");
            }
        });
        return v;
    }
    private void ObtenerAlmacenes(){
//        if(){
//
//        }
        String obs = " ";
        Date fechahora = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        String dias = dateFormat.format(fechahora);
        String currentString = u1.getText().toString();
        String[] separated = currentString.split("-");
        String a = separated[0];
        String b = separated[1];
        String c = separated[2];
        String url = "https://appsionmovil.000webhostapp.com/AlmacenMP_insertar.php?Rack="+ a.replaceAll(" ", "%20") +
                "&Fila="+ b.replaceAll(" ", "%20") +
                "&Columna="+ c.replaceAll(" ", "%20") +
                "&MateriaPrima="+ mp1.getText().toString().replaceAll(" ", "%20") +
                "&LoteMP="+ nl1.getText().toString().replaceAll(" ", "%20") +
                "&Cantidad="+ c1.getText().toString().replaceAll(" ", "%20") +
                "&Persona=" + obs.replaceAll(" ", "%20") +
                "&Observaciones=" + obs.replaceAll(" ", "%20") +
                "&FechayHora=" + dias.replaceAll(" ", "%20");
        Toast.makeText(getActivity(), "Sus datos se han guardado",Toast.LENGTH_SHORT).show();
        cliente.post(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode == 200){
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            }
        });
    }
}