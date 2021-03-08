package com.geewaza.code.springboot.webservice;


import javax.jws.WebService;
import javax.xml.ws.Endpoint;

@WebService
public class Function {
    public String transWords(String words){
        StringBuilder res = new StringBuilder();
        for (char c : words.toCharArray()) {
            res.append(c).append(",");
        }
        return res.toString();
    }

    public static void main(String[] args) {
        Endpoint.publish("http://localhost:8089/service/function", new Function());
        System.out.println("Publish Success:listen port 8089");
    }
}
