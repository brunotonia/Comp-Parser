package br.com.brunotonia;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Parser {

    private List<String> pilha;
    private Boolean isPossible;

    public Parser() {
        this.pilha = new ArrayList<>();
        this.isPossible = true;
    }

    public void povoaPilha(File arquivo_txt) {
        FileInputStream stream;
        InputStreamReader reader;
        BufferedReader br;
        pilha = new ArrayList<String>();
        pilha.add("$");
        try {
            // Leitura do arquivo de texto!
            stream = new FileInputStream(arquivo_txt);
            reader = new InputStreamReader(stream);
            // Bufferiza o arquivo de texto
            br = new BufferedReader(reader);
            // Carrega a primeira linha
            String linha = br.readLine();
            // Pelo texto todo...
            while (linha != null) {
                String[] tokens = linha.split(" ");
                for (String aux : tokens) {
                    pilha.add(aux);
                }
                linha = br.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Boolean OperadoresLogicos(String token1) {
        if (token1.matches("m[>&]")) {
            pilha.remove(pilha.size() - 1);
            pilha.add(token1);
            return true;
        } else if (token1.matches("[<=]m")) {
            pilha.remove(pilha.size() - 1);
            pilha.add(token1);
            return true;
        } else if (token1.matches("[^^]")) {
            pilha.remove(pilha.size() - 1);
            pilha.add(token1);
            return true;
        } else if (token1.matches("=//")) {
            pilha.remove(pilha.size() - 1);
            pilha.add(token1);
            return true;
        } else {
            return false;
        }
    }

    Boolean OperadoresMatematicos(String token1) {
        if (token1.matches("m[+-:x=]")) {
            pilha.remove(pilha.size() - 1);
            pilha.add(token1);
            return true;
        } else {
            return false;
        }
    }

    Boolean Tipos(List<String> pile, String token1) {
        if (token1.matches("m[ifbs]")) {
            pilha.remove(pilha.size() - 1);
            pilha.add(token1);
            return true;
        } else {
            return false;
        }
    }

    Boolean FuncaoPrincipal(String token1, String token2, String token3) {
        if (token1.matches("m##")) {
            pilha.remove(pilha.size() - 1);
            pilha.add("");
            return true;
        } else {
            return false;
        }

    }

    Boolean Parseador(File arquivo_txt) {
        povoaPilha(arquivo_txt);

        return false;
    }

}
