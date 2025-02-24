package com.jhonjhon.lanches.classes;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.jhonjhon.lanches.paginas.PedidosActivity;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

public class RecebeNotificacao implements OneSignal.NotificationOpenedHandler {

    // This fires when a notification is opened by tapping on it.
    private Context activityContext;

    public RecebeNotificacao(Context contexto) {
        this.activityContext = contexto;
    }

    @Override
    public void notificationOpened(OSNotificationOpenResult result) {
        OSNotificationAction.ActionType actionType = result.action.type;
        JSONObject data = result.notification.payload.additionalData;
        String customKey;
        boolean Pedido = false;
        Log.i("OSNotificationPayload", "result.notification.payload.toJSONObject().toString(): " + result.notification.payload.toJSONObject().toString());


        if (data != null) {
            JSONObject dataJson = result.notification.payload.toJSONObject();
            try {
                String mensagem = dataJson.getString("body");
                String titulo = dataJson.getString("title");
                //   Log.d("Mensagem", mensagem);
                if (titulo.contains("pedido")) {
                    Pedido = true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            customKey = data.optString("customkey", null);
            if (customKey != null)
                Log.i("OneSignalExample", "customkey set with value: " + customKey);
        }

        if (actionType == OSNotificationAction.ActionType.ActionTaken)
            Log.i("OneSignalExample", "Button pressed with id: " + result.action.actionID);


        if(Pedido) {
            Intent intent = new Intent(activityContext, PedidosActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    activityContext.startActivity(intent);
                }
            }, 3000);
        }

    }
}