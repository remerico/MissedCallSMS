package receivers;

import events.EventBus;
import events.IncomingCallEvent;
import events.MissedCallEvent;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.telephony.gsm.SmsManager;
import android.widget.Toast;
import app.Preferences;

public class IncomingCallReceiver extends BroadcastReceiver {
   
     static boolean ring = false;
     static boolean callReceived = false;
     static String callerPhoneNumber = "";
     
     
     public class MissedCallReceivedEvent {
    	 public String number;
     }
     
     
     
     @Override
     public void onReceive(Context mContext, Intent intent){
         
            // Get the current Phone State
           String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
           
           if (state == null) return;

           // If phone state "Ringing"
           if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {           
        	   ring =true;
               
        	   // Get the Caller's Phone Number
               Bundle bundle = intent.getExtras();
               callerPhoneNumber= bundle.getString("incoming_number");
               
               EventBus.getInstance().post(new IncomingCallEvent(callerPhoneNumber));
           }


            // If incoming call is received
           if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
        	   callReceived = true;
           }


            // If phone is Idle
           if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {

        	   // If phone was ringing(ring=true) and not received(callReceived=false) , then it is a missed call

        	   if (ring && !callReceived) {
        		   
        		   EventBus.getInstance().post(new MissedCallEvent(callerPhoneNumber));
        		   
        		   sendSMS(mContext, callerPhoneNumber);
        		   
        		   //Toast.makeText(mContext, "Missed call : "+callerPhoneNumber, Toast.LENGTH_LONG).show();
        	   }
        	   
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




