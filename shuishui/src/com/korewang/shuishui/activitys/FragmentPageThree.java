package com.korewang.shuishui.activitys;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.korewang.shuishui.R;
import com.korewang.shuishui.plugins.GetPhoneInfo;

/*
 * import android.support.v4.app.Fragment;
 * 而不是import android.Fragment;
 * 
 * */
public class FragmentPageThree extends Fragment{
	private View rootView;
	private TextView mPhoneInfo;
	private Context context;
	private EditText et ;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.fragment_three, null);
			initView();
		}
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		return rootView;
	}
	public void initView(){
		mPhoneInfo = (TextView)rootView.findViewById(R.id.myPhoneInfo);
		GetPhoneInfo siminfo = new GetPhoneInfo(getActivity());  
        String sims = "getProvidersName:"+siminfo.getProvidersName()+
	        				"\r\ngetNativePhoneNumber:"+siminfo.getNativePhoneNumber()+
	        				"\r\ngetPhoneInfo:"+siminfo.getPhoneInfo();
        mPhoneInfo.setText(sims);
        
        
        //获取wifi服务  
        WifiManager wifiManager = (WifiManager)getActivity().getSystemService(Context.WIFI_SERVICE);  
        //判断wifi是否开启  
        if (!wifiManager.isWifiEnabled()) {  
    	    wifiManager.setWifiEnabled(true);    
	    }  
	    WifiInfo wifiInfo = wifiManager.getConnectionInfo();       
	    int ipAddress = wifiInfo.getIpAddress();   
	    String ip = intToIp(ipAddress);   
	    et = (EditText)rootView.findViewById(R.id.EditTextwifi);  
	    et.setText(ip);  
	    //et.setText(getLocalIpAddress());
    	
	}
	    
	public String getLocalIpAddress()  
    {  
        try  
        {  
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();)  
            {  
               NetworkInterface intf = en.nextElement();  
               for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)  
               {  
                   InetAddress inetAddress = enumIpAddr.nextElement();  
                   if (!inetAddress.isLoopbackAddress())  
                   {  
                       return inetAddress.getHostAddress().toString();  
                   }  
               }  
           }  
        }  
        catch (SocketException ex)  
        {  
            Log.e("WifiPreference IpAddress", ex.toString());  
        }  
        return null;  
    }  
	private String intToIp(int i) {       
	     
	      return (i & 0xFF ) + "." +       
	    ((i >> 8 ) & 0xFF) + "." +       
	    ((i >> 16 ) & 0xFF) + "." +       
	    ( i >> 24 & 0xFF) ;  
	 }   
}
