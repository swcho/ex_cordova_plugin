package com.moduscreate.plugin;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import android.content.Context;
import android.widget.Toast;

import java.io.IOException; 
import java.net.InetAddress; 
import java.net.SocketException; 
import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

public class ModusEcho extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        if ("echo".equals(action)) {
            echo(args.getString(0), callbackContext);
            return true;
        }

        if ("ntp".equals(action)) {
            final CallbackContext cb = callbackContext;
            cordova.getThreadPool().execute(new Runnable() {
                public void run() {
                    NTPUDPClient client = new NTPUDPClient();
                    client.setDefaultTimeout(10000);
                    try {
                        client.open();
                    } catch (final SocketException se) {
                        se.printStackTrace();
                        cb.error("socket exception " + se.toString());
                    }
                    try {
                        TimeInfo info = client.getTime(InetAddress.getByName("time.bora.net"));
                        // TimeInfo info = client.getTime(InetAddress.getByName("203.254.163.74"));
                        info.computeDetails();
                        Long offsetValue = info.getOffset();
                        int receiverTimeDelta = (offsetValue == null) ? 0 : offsetValue.intValue();
                        cb.success(receiverTimeDelta);
                    } catch (final IOException ioe) {
                        ioe.printStackTrace();
                        cb.error("io exception " + ioe.toString());
                    }
                    client.close();
                }
            });
            return true;
        }

        return false;
    }

    private void echo(String msg, CallbackContext callbackContext) {
        if (msg == null || msg.length() == 0) {
            callbackContext.error("Empty message!");
        } else {
            Toast.makeText(webView.getContext(), msg, Toast.LENGTH_LONG).show();
            callbackContext.success(msg);
        }
    }

    // https://www.programcreek.com/java-api-examples/index.php?source_dir=TKSmartKitchen-master/android/src/de/tud/kitchen/android/NTPTimeReceiver.java
    private void ntp(CallbackContext cb) {
        NTPUDPClient client = new NTPUDPClient();
        client.setDefaultTimeout(10000);
        try {
            client.open();
        } catch (final SocketException se) {
            se.printStackTrace();
            cb.error("socket exception " + se.toString());
        }
        try {
            // TimeInfo info = client.getTime(InetAddress.getByName("time.bora.net"));
            TimeInfo info = client.getTime(InetAddress.getByName("203.254.163.74"));
            info.computeDetails();
            Long offsetValue = info.getOffset();
            int receiverTimeDelta = (offsetValue == null) ? 0 : offsetValue.intValue();
            cb.success(receiverTimeDelta);
        } catch (final IOException ioe) {
            ioe.printStackTrace();
            cb.error("io exception " + ioe.toString());
        }
        client.close();
    }
}
