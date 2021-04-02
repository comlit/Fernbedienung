package com.lit.fernbedienung;

//import net.dongliu.requests.Requests;

import com.github.kevinsawicki.http.HttpRequest;

public class Sender
{
    public static void senden(final char fam, final int group, final int device, final char onoff)
    {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    String ip = "http://192.168.178.44/";
                    String url = ip + fam + group + device + onoff;
                    int resp = HttpRequest.get(url).code();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}
