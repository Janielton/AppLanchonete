package com.jhonjhon.lanches.classes;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.jhonjhon.lanches.paginas.CarrinhoActivity;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class EnviarNotificacao extends AsyncTask<String, Void, Boolean> {
    private Context activityContext;
    private ProgressDialog dialog;
    PreferencesUsuario preferencesUsuario;

    public EnviarNotificacao() {
    }

    public EnviarNotificacao(Context activityContext)
    {
        this.activityContext = activityContext;
        preferencesUsuario = new PreferencesUsuario(activityContext);
    }

    @Override
    protected void onPreExecute() {
        if(activityContext != null){
            dialog = new ProgressDialog(activityContext);
            dialog.setMessage("Por favor aguarde...");
            dialog.setTitle("Enviando pedido");
         //   dialog.show();
        }
    }

    protected Boolean doInBackground(String... valor) {
        HttpURLConnection httpCon = null;
        String idPlayer = valor[0];
        if(idPlayer.contains(",")){
            idPlayer = idPlayer.replace(",","\",\"");
            idPlayer = idPlayer.replaceAll("[\\r\\n\\s]+","");
        }
        String imagem = valor[1];
        String titulo = valor[2];
        String mensagem = valor[3];

        try {
            String jsonResponse;

            URL url = new URL("https://onesignal.com/api/v1/notifications");
            httpCon = (HttpURLConnection)url.openConnection();
            httpCon.setUseCaches(false);
            httpCon.setDoOutput(true);
            httpCon.setDoInput(true);

            httpCon.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            httpCon.setRequestProperty("Authorization", "Basic ZmE3NDljNTktYzRlMS00MGYxLWIyN2YtNGY4N2VhZjA3Mjg2");
            httpCon.setRequestMethod("POST");

            String strJsonBody = "{"
                        + "\"app_id\": \"bce20c5d-04aa-4d5a-82cf-471b60b1c767\","
                        + "\"include_player_ids\": [\""+idPlayer+"\"],"
                //        + "\"big_picture\":\""+imagem+"\","
                        + "\"large_icon\": \"icone_app_pedido\","
                        + "\"headings\": {\"en\": \""+titulo+"\"},"
                        + "\"data\": {\"pedido\": \"app\"},"
                        + "\"contents\": {\"en\": \""+mensagem+"\"}"
                        + "}";

            System.out.println("strJsonBody:\n" + strJsonBody);

            byte[] sendBytes = strJsonBody.getBytes("UTF-8");
            httpCon.setFixedLengthStreamingMode(sendBytes.length);

            OutputStream outputStream = httpCon.getOutputStream();
            outputStream.write(sendBytes);

            int httpResponse = httpCon.getResponseCode();
            System.out.println("httpResponse: " + httpResponse);

            if (  httpResponse >= HttpURLConnection.HTTP_OK
                    && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                Scanner scanner = new Scanner(httpCon.getInputStream(), "UTF-8");
                jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                System.out.println("jsonResponse:\n" + jsonResponse);
                scanner.close();
            }
            else {
                Scanner scanner = new Scanner(httpCon.getErrorStream(), "UTF-8");
                jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                System.out.println("jsonResponseErro:\n" + jsonResponse);
                scanner.close();
                return false;
            }
            return true;

        } catch(Throwable t) {
            t.printStackTrace();
            return false;
        }finally {
            if (httpCon != null) {
                httpCon.disconnect();
            }
        }


    }

    @Override
    protected void onPostExecute(Boolean result) {

        ((CarrinhoActivity)activityContext).SalvarPedido();
//        if(activityContext != null){
//            if (dialog.isShowing()) {
//                dialog.dismiss();
//            }
//        }
    }

    private void MandarTodos(){
        try {
            String jsonResponse;

            URL url = new URL("https://onesignal.com/api/v1/notifications");
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setUseCaches(false);
            con.setDoOutput(true);
            con.setDoInput(true);

            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Authorization", "Basic ZmE3NDljNTktYzRlMS00MGYxLWIyN2YtNGY4N2VhZjA3Mjg2");
            con.setRequestMethod("POST");

            String strJsonBody = "{"
                    +   "\"app_id\": \"f92ef08d-67f1-4e03-b887-3c5375f67bb2\","
                    +   "\"included_segments\": [\"All\"],"
                    +   "\"data\": {\"foo\": \"bar\"},"
                    +   "\"contents\": {\"pt\": \"Mensagem\"}"
                    + "}";


            System.out.println("strJsonBody:\n" + strJsonBody);

            byte[] sendBytes = strJsonBody.getBytes("UTF-8");
            con.setFixedLengthStreamingMode(sendBytes.length);

            OutputStream outputStream = con.getOutputStream();
            outputStream.write(sendBytes);

            int httpResponse = con.getResponseCode();
            System.out.println("httpResponse: " + httpResponse);

            if (  httpResponse >= HttpURLConnection.HTTP_OK
                    && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                scanner.close();
            }
            else {
                Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                scanner.close();
            }
            System.out.println("jsonResponse:\n" + jsonResponse);

        } catch(Throwable t) {
            t.printStackTrace();
        }
    }
}
