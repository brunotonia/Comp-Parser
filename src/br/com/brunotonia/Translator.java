package br.com.brunotonia;

public class Translator {
    
    public String Tradutor (String token) {
        String resultado = "";
        if (token.matches("[0-9]+")) {
            resultado = token;
        }
        if (token.startsWith("mentr")) {
            resultado = token.substring(5);
        }
        
        
        return resultado;
    }
    
}
