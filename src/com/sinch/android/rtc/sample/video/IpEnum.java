package com.sinch.android.rtc.sample.video;

/**
 * Created by pcandido on 29/09/2016.
 */
public enum IpEnum {
    IP("ip");

    private String ip ;

    public void setCodigo(String text){

        ip = text;

    }





    private IpEnum(String ip){


    }


    public String getCodigo() {
        return ip ;
    }

}