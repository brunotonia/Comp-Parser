package br.com.brunotonia;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Token {

    /* Sugestões da Diomara */
    // filtrar erros
    // gravar palavra do erro e linha para exibir
    // ajustar documentacao
    
    // Variável de Tamanho do Vetor
    // Customizar conforme necessário
    private final Integer tamanho = 6;
    // Variáveis da Classe
    private Integer[] quantidade = new Integer[tamanho];
    private String[] descricao = new String[tamanho];
    private String[] exemplos = new String[tamanho];
    private List<String> erros = new ArrayList<>();

    public Token() {
        // Inicializa variáveis
        for (Integer i = 0; i < tamanho; i++) {
            //this.quantidade.elementAt(0) = ;
            quantidade[i] = 0;
            switch (i) {
                // Adicionar os exemplos da linguagem
                // Trocar e não esquecer de cortar o que não precisamos
                case 0:
                    descricao[i] = "Tipos";
                    //exemplos[i] = "a10 b15 aaa varB teste";
                    exemplos[i] = "mi mf mb ms";  // inteiro float boolean string
                    break;
                case 1:
                    descricao[i] = "Operadores";
                    exemplos[i] = "m+ m* m- m/"; // adicao, multi, sub, divisao
                    break;
                case 2:
                    descricao[i] = "Palavras Reservadas";
                    exemplos[i] = "msera mvaifazendo msair m> <m =m m& ^^"; //maior, menor, igual, and, or
                    break;
                case 3:
                    descricao[i] = "Condições";
                    exemplos[i] = "m> <m =m =\\ m& ^^";
                    break;
                case 4:
                    descricao[i] = "Variaveis";
                    exemplos[i] = "mentr[A-Za-z0-9]";
                    break;
                /*case 5:
                    descricao[i] = "Texto";
                    exemplos[i] = "'Texto 056456'"; // mentr entrada de texto
                    break;
                case 6:
                    descricao[i] = "Escopo";
                    exemplos[i] = "/t";
                    break;
                case 7:
                    descricao[i] = "Palavras Reservadas";
                    exemplos[i] = "¨¨ ¨i ¨o ¨m ¨f ¨r";
                    break;
                case 8:
                    descricao[i] = "Booleano";
                    exemplos[i] = "I O";
                    break;
                case 9:
                    descricao[i] = "Geral";
                    exemplos[i] = "I O";
                    break;
                case 10:*/
                case 5:
                    descricao[i] = "Erro";
                    exemplos[i] = "sertanejo universitário¨'";
                    break;
            }
        }
    }

    public void separarTokens(File arquivo_txt) {
        FileInputStream stream;
        InputStreamReader reader;
        BufferedReader br;
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
                    // Trocar pelas expressões regulares da nossa linguagem
                    if (aux.matches("m[ifbs]")) {
                        //"mi mf mb ms"
                        quantidade[0]++;
                        // adiciona na lista de tokens
                    } else if (aux.matches("m[+-:x=]")) {
                        quantidade[1]++;
                        // adiciona na lista de tokens
                    } else if (aux.matches("##m") || aux.matches("m##") || aux.matches("mentr") || aux.matches("msair")
                            || aux.matches("merro") || aux.matches("!") || aux.matches("msera") || aux.matches("mouentao")
                            || aux.matches("mvaifazendo")) {
                        quantidade[2]++;
                        // adiciona na lista de tokens
                    } else if (aux.matches("m[>&]") || aux.matches("[<=]m") || aux.matches("[^^]") || aux.matches("=//")) {
                        quantidade[3]++;
                        // adiciona na lista de tokens
                    } else if (aux.matches("mentr[A-Za-z0-9]+")) {
                        quantidade[4]++;
                        // adiciona na lista de tokens
                    } else if (aux.matches("[0-9]+") || aux.equals("\n") || aux.equals("\r\n") || aux.equals("\t")) {
                        // ruidos na string
                        // valores
                    } else {
                        //System.out.print(aux);
                        quantidade[5]++;
                        erros.add(aux);
                        // adiciona na lista de tokens
                    }
                }
                // Próxima linha
                linha = br.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Integer[] getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer[] quantidade) {
        this.quantidade = quantidade;
    }

    public String[] getDescricao() {
        return descricao;
    }

    public void setDescricao(String[] descricao) {
        this.descricao = descricao;
    }

    public String[] getExemplos() {
        return exemplos;
    }

    public void setExemplos(String[] exemplos) {
        this.exemplos = exemplos;
    }

    @Override
    public String toString() {
        String resultado = "Quantidade - Token";
        for (Integer i = 0; i < tamanho; i++) {
            resultado += "\n" + quantidade[i] + " - " + descricao[i];
        }
        resultado += ": ";
        for (String aux: erros) {
            resultado += aux + ", ";
        }
        resultado += "\n";
        return resultado;
    }

}
