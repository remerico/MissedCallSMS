package receivers;

import java.lang.reflect.Method;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.telephony.gsm.SmsManager;
import android.widget.Toast;
import app.Preferences;

import com.android.internal.telephony.ITelephony;

import events.EventBus;
import events.IncomingCallEvent;
import events.MissedCallEvent;


public class IncomingCallReceiver extends BroadcastReceiver {
   
     static boolean ring = false;
     static boolean callReceived = false;
     static String callerPhoneNumber = "";
     
     
     public class MissedCallReceivedEvent {
    	 public String number;
     }
     
     
     
     @Override
     public void onReceive(Context context, Intent intent) {

    	 // Get the current Phone State
    	 String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
       
    	 if (state == null) return;

    	 // If phone state "Ringing"
    	 if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {           
    		 ring = true;
           
        	 // Get the Caller's Phone Number
        	 Bundle bundle = intent.getExtras();
        	 callerPhoneNumber = bundle.getString("incoming_number");
           
        	 EventBus.getInstance().post(new IncomingCallEvent(callerPhoneNumber));
        	 
        	 endCall(context);
        	 
        	 sendSMS(context, callerPhoneNumber);
        	 
    	 }


    	 // If incoming call is received
    	 if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
    		 callReceived = true;
    	 }


    	 // If phone is Idle
    	 if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {

    	   // Handle missed call
    	   if (ring && !callReceived) {
    		   EventBus.getInstance().post(new MissedCallEvent(callerPhoneNumber));
    		   //sendSMS(context, callerPhoneNumber);
    	   }
    	   
    	   ring = false;
    	   callReceived = false;
        	   
         }
           
     }
     
     
     
     @SuppressWarnings("unchecked")
	private void endCall(Context context) {
    	 
    	 TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    	 
    	 try {
        	 
        	 Class c = Class.forName(telephony.getClass().getName());
             Method m = c.getDeclaredMethod("getITelephony");
             m.setAccessible(true);
        	 ITelephony telephonyService = (ITelephony) m.invoke(telephony);
        	 
        	 telephonyService = (ITelephony) m.invoke(telephony);
             telephonyService.endCall();
        	 
    	 }
    	 catch (Exception e) {
    		 Toast.makeText(context, "Could not end call: " + e.getMessage(), Toast.LENGTH_LONG).show();
    		 e.printStackTrace();
    	 }
    	 
     }
     
     
     @SuppressWarnings("deprecation")
	private void sendSMS(Context context, String number) {
    	 
    	 String message = Preferences.getResponseMessage(context);
    	 
    	 Toast.makeText(context, "Sending SMS to : " + number, Toast.LENGTH_LONG).show();
    	 
		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage(number, null, message, null, null);
    	 
     }
       
}




