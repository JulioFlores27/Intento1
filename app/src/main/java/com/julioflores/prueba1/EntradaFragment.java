package com.julioflores.prueba1;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
    Button bu1, scanner;
    AsyncHttpClient cliente;
    String usuario;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_entrada, container, false);
        mp1 = (EditText) v.findViewById(R.id.matpri1);
        nl1 = (EditText) v.findViewById(R.id.lote1);
        c1 = (EditText) v.findViewById(R.id.cantidad1);
        u1 = (EditText) v.findViewById(R.id.ubicacion1);
        bu1 = (Button) v.findViewById(R.id.b1);
        scanner = (Button) v.findViewById(R.id.boton_scanner);
        cliente = new AsyncHttpClient();

        //usuario = "Javier Belausteguigoitia";
        //usuario = "Danya López";
        //usuario = "Tablet";
        //usuario = "Edgar Cruz";
        usuario = "Juan Antonio Muñoz";
        //usuario = "Edgar Gallardo";

        bu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mp1.getText().toString().isEmpty() || nl1.getText().toString().isEmpty() || c1.getText().toString().isEmpty() || u1.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), "Favor de ingresar datos",Toast.LENGTH_SHORT).show();
                }else if (mp1.getText().toString().length() <5 || mp1.getText().toString().length() > 25){
                    Toast.makeText(getActivity(), "Revisar MP",Toast.LENGTH_SHORT).show();
                }else if (nl1.getText().toString().length() <5 || nl1.getText().toString().length() > 10){
                    Toast.makeText(getActivity(), "Revisar lote",Toast.LENGTH_SHORT).show();
                }
                if ((nl1.getText().toString().length() == 10 || nl1.getText().toString().length() == 5) && (mp1.getText().toString().length() ==5 || mp1.getText().toString().length() >21 ) && (u1.getText().toString().matches(".-.-.")||u1.getText().toString().matches("..-.-.")||u1.getText().toString().matches(".-.-..")||u1.getText().toString().matches("..-.-.."))){
                    ObtenerAlmacenes();
                    mp1.setText("");
                    nl1.setText("");
                    c1.setText("");
                    u1.setText("");
                } else {
                    Toast.makeText(getActivity(), "Revisar ubicación",Toast.LENGTH_SHORT).show();
                }
            }
        });

        scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder mibuild = new AlertDialog.Builder(getActivity());
                final View mview = getLayoutInflater().inflate(R.layout.dialogo_escaneo, null);
                final EditText codigo = (EditText) mview.findViewById(R.id.codigo_escaneado);
                mibuild.setTitle("Escanea el codigo");
                mibuild.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String escaneo = codigo.getText().toString();
                        if (escaneo.length() > 23) {
                            String[] partes_codigo = codigo.getText().toString().split("-");
                            String mp_codigo = partes_codigo[0];
                            String lote_codigo = partes_codigo[3].trim();

                            mp1.setText(mp_codigo);
                            nl1.setText(lote_codigo);
                            c1.requestFocus();
                        }
                        else {
                            Toast.makeText(getActivity(),"Error en el código",Toast.LENGTH_LONG).show();
                        }
                    }
                });
                mibuild.setView(mview);
                AlertDialog dialogo = mibuild.create();

                dialogo.show();
            }
        });
        return v;
    }
    private void ObtenerAlmacenes(){
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
                "&Persona=" + usuario.replaceAll(" ", "%20") +
                "&Observaciones=" + obs.replaceAll(" ", "%20") +
                "&FechayHora=" + dias.replaceAll(" ", "%20");
        Toast.makeText(getActivity(), "Sus datos se han guardado",Toast.LENGTH_SHORT).show();
        cliente.post(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) { if(statusCode == 200){ } }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) { }
        });
    }
}