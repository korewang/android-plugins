package com.korewang.shuishui.plugins;

import android.content.Context;
import android.telephony.TelephonyManager;

public class GetPhoneInfo {
	private TelephonyManager telephonyManager;  
    /** 
     * Get phone  IMSI
     */  
    private String IMSI;  
    private Context mcontext;  
    public GetPhoneInfo(Context context) {  
    	mcontext=context;  
        telephonyManager = (TelephonyManager) context  
                .getSystemService(Context.TELEPHONY_SERVICE);  
    }  
    /** 
     * Get phone  is  double card
     */
    public boolean getDoubleCard(){
    	
    	return true;
    }
    /** 
     * Get phone  id
     */  
    public String getNativePhoneNumber() {  
        String NativePhoneNumber=null;  
        NativePhoneNumber=telephonyManager.getLine1Number();  
        return NativePhoneNumber;  
    }  
  
    /** 
     * Get phone  serveice 
     */  
    public String getProvidersName() {  
        String ProvidersName = "NameAndAddress";  
        try{  
	        IMSI = telephonyManager.getSubscriberId();  
	        /*
	         *  IMSI font 3 ，460 is China， below 2 00 02 is China mobile  01 is China unicom  03 is China telecom
	         */   
	        if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {  
	            ProvidersName = "中国移动";  
	        } else if (IMSI.startsWith("46001")) {  
	            ProvidersName = "中国联通";  
	        } else if (IMSI.startsWith("46003")) {  
	            ProvidersName = "中国电信";  
	        }  
        }catch(Exception e){  
            e.printStackTrace();  
        }  
        return ProvidersName;  
    }  
      
    public String  getPhoneInfo(){ 
     	TelephonyManager tm = (TelephonyManager)mcontext.getSystemService(Context.TELEPHONY_SERVICE);  
        StringBuilder sb = new StringBuilder();  
        sb.append("\nDeviceId(IMEI) = " + tm.getDeviceId());  
        sb.append("\nDeviceSoftwareVersion = " + tm.getDeviceSoftwareVersion());  
        sb.append("\nLine1Number = " + tm.getLine1Number());  
        sb.append("\nNetworkCountryIso = " + tm.getNetworkCountryIso());  
        sb.append("\nNetworkOperator = " + tm.getNetworkOperator());  
        sb.append("\nNetworkOperatorName = " + tm.getNetworkOperatorName());  
        sb.append("\nNetworkType = " + tm.getNetworkType());  
        sb.append("\nPhoneType = " + tm.getPhoneType());  
        sb.append("\nSimCountryIso = " + tm.getSimCountryIso());  
        sb.append("\nSimOperator = " + tm.getSimOperator());  
        sb.append("\nSimOperatorName = " + tm.getSimOperatorName());  
        sb.append("\nSimSerialNumber = " + tm.getSimSerialNumber());  
        sb.append("\nSimState = " + tm.getSimState());  
        sb.append("\nSubscriberId(IMSI) = " + tm.getSubscriberId());  
        sb.append("\nVoiceMailNumber = " + tm.getVoiceMailNumber());  
        return  sb.toString();  
    }  

}
