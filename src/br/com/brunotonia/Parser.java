package br.com.brunotonia;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Parser {

    private final String numberRegEx = "(([0-9]*[.][0-9]+)|([0-9]+))";
    private final String varRegEx = "[a-z][a-zA-Z0-9]*";
    private final String mathSymbolRegEx = "[+-*/]";
    private final String blockRegEx = "[\n\t]";
    private final String logicalRegEx = "[<][=]|[=][>]|[><|=-!]";
    private final String specialRegEx = "[(),]";
    private final String systemCallRegEx = "[#][#io]|[#]";
    private final String methodRegEx = "[#][fm]";
    private final String textRegEx = "'[^\n]*'";

    private List<String> pilha;
    private List<String> tokens;

    private String log = "";

    public void CarregarTokens(File file) {
        FileInputStream stream;
        InputStreamReader reader;
        BufferedReader br;
        tokens = new ArrayList<>();
        try {
            stream = new FileInputStream(file);
            reader = new InputStreamReader(stream);
            br = new BufferedReader(reader);
            String linha = br.readLine();
            while (linha != null) {
                String[] tks = linha.split(" ");
                for (String aux : tks) {
                    if (aux.startsWith("\t")) {
                        tokens.add("\t");
                        tokens.add(aux.substring(1));
                    } else if (aux.endsWith(",")) {
                        tokens.add(aux.substring(0, aux.length() - 1));
                        tokens.add(",");
                    } else if (aux.endsWith("\n")) {
                        tokens.add(aux.substring(0, aux.length() - 1));
                        tokens.add("\n");
                    } else {
                        tokens.add(aux);
                    }
                }
                linha = br.readLine();
                tokens.add("\n");
            }
            tokens.remove(tokens.size() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ImprimirTokens() {
        for (String aux : tokens) {
            if (aux.equals("\t")) {
                System.out.println("\t/t");
            } else if (aux.equals("\n")) {
                System.out.println("\t/n");
            } else {
                System.out.println("\t" + aux);
            }
        }
    }

    public String getLog() {
        return log;
    }

    public Boolean Executar() {
        pilha = new ArrayList<>();
        Boolean isPossible = true;
        /*for (int i = 0; i < tokens.size(); i++) {
            if (tokens.get(i).substring(0, 2) == "/*") {
                tokens.remove(i);
                i--;
            }
        }*/
        tokens.add("CIFRAO");
        pilha.add("CIFRAO");
        System.out.println("pilha.add: CIFRAO");
        pilha.add("Funcao");
        System.out.println("pilha.add: Funcao");
        while (isPossible == true && tokens.size() > 0) {
            if (pilha.get(pilha.size() - 1) == "CIFRAO") {
                if (tokens.get(0) == "CIFRAO") {
                    log += " CIFRAO = CIFRAO\n";
                    System.out.println("pilha.remove: " + pilha.get(pilha.size() - 1));
                    pilha.remove(pilha.size() - 1);
                    tokens.remove(0);
                    isPossible = true;
                    break;
                } else {
                    isPossible = false;
                }
            } else if (pilha.get(pilha.size() - 1).equals("Funcao")) {
                log += pilha.get(pilha.size() - 1) + " ->";
                System.out.println("chamando metodo: funcao (" + tokens.get(0) + ")");
                isPossible = funcao(tokens.get(0));
            } else if (pilha.get(pilha.size() - 1) == "Funcao_Principal") {
                log += pilha.get(pilha.size() - 1) + " ->";
                System.out.println("chamando metodo: funcao_principal (" + tokens.get(0) + ")");
                isPossible = funcao_principal(tokens.get(0));
            } else if (pilha.get(pilha.size() - 1) == "Funcao_Normal") {
                log += pilha.get(pilha.size() - 1) + " ->";
                if (tokens.size() > 2) {
                    System.out.println("chamando metodo: funcao_normal (" + tokens.get(0) + ", " + tokens.get(1) + ", " + tokens.get(2) + ")");
                    isPossible = funcao_normal(tokens.get(0), tokens.get(1), tokens.get(2));
                } else {
                    isPossible = false;
                }
            } else if (pilha.get(pilha.size() - 1) == "Var_Funcao") {
                log += pilha.get(pilha.size() - 1) + " ->";
                System.out.println("chamando metodo: var_funcao (" + tokens.get(0) + ")");
                isPossible = var_funcao(tokens.get(0));
            } else if (pilha.get(pilha.size() - 1) == "Tab") {
                if (tokens.size() > 1) {
                    log += pilha.get(pilha.size() - 1) + " ->";
                    System.out.println("chamando metodo: tab (" + tokens.get(0) + ", " + tokens.get(1) + ")");
                    isPossible = tab(tokens.get(0), tokens.get(1));
                } else {
                    log += pilha.get(pilha.size() - 1) + " ->\t\t&";
                    System.out.println("pilha.remove: " + pilha.get(pilha.size() - 1));
                    pilha.remove(pilha.size() - 1);
                }
            } else if (pilha.get(pilha.size() - 1) == "Trema") {
                log += pilha.get(pilha.size() - 1) + " ->";
                if (tokens.size() > 2) {
                    System.out.println("chamando metodo: trema (" + tokens.get(0) + ", " + tokens.get(1) + ", " + tokens.get(2) + ")");
                    isPossible = trema(tokens.get(0), tokens.get(1), tokens.get(2));
                } else if (tokens.get(0) == "#") {
                    log += "\t\tTab\n"
                            + "\t\t/n\n"
                            + "\t\t#\n";
                    System.out.println("pilha.remove: " + pilha.get(pilha.size() - 1));
                    pilha.remove(pilha.size() - 1);
                    System.out.println("pilha.add: Tab");
                    pilha.add("Tab");
                    System.out.println("pilha.add: /n");
                    pilha.add("\n");
                    System.out.println("pilha.add: #");
                    pilha.add("#");
                    isPossible = true;
                } else {
                    isPossible = false;
                }
            } else if (pilha.get(pilha.size() - 1) == "Opc") {
                log += pilha.get(pilha.size() - 1) + " ->";
                System.out.println("chamando metodo: opc (" + tokens.get(0) + ")");
                isPossible = opc(tokens.get(0));
            } else if (pilha.get(pilha.size() - 1) == "Vir_Opc") {
                log += pilha.get(pilha.size() - 1) + " ->";
                System.out.println("chamando metodo: vir_opc (" + tokens.get(0) + ")");
                isPossible = vir_opc(tokens.get(0));
            } else if (pilha.get(pilha.size() - 1) == "Tipo") {
                log += pilha.get(pilha.size() - 1) + " ->";
                System.out.println("chamando metodo: tipo (" + tokens.get(0) + ")");
                isPossible = tipo(tokens.get(0));
            } else if (pilha.get(pilha.size() - 1) == "Op_mat") {
                log += pilha.get(pilha.size() - 1) + " ->";
                System.out.println("chamando metodo: op_mat (" + tokens.get(0) + ")");
                isPossible = op_mat(tokens.get(0));
            } else if (pilha.get(pilha.size() - 1) == "Op_log") {
                System.out.println("chamando metodo: op_log (" + tokens.get(0) + ")");
                log += pilha.get(pilha.size() - 1) + " ->";
                isPossible = op_log(tokens.get(0));
            } else if (pilha.get(pilha.size() - 1).matches(methodRegEx)) {
                if (tokens.get(0) == pilha.get(pilha.size() - 1)) {
                    log += " " + tokens.get(0) + " = " + pilha.get(pilha.size() - 1) + "\n";
                    System.out.println("pilha.remove: " + pilha.get(pilha.size() - 1));
                    pilha.remove(pilha.size() - 1);
                    tokens.remove(0);
                    isPossible = true;
                } else {
                    isPossible = false;
                }
            } else if (pilha.get(pilha.size() - 1).matches(systemCallRegEx)) {
                if (tokens.get(0) == pilha.get(pilha.size() - 1)) {
                    log += " " + tokens.get(0) + " = " + pilha.get(pilha.size() - 1) + "\n";
                    System.out.println("pilha.remove: " + pilha.get(pilha.size() - 1));
                    pilha.remove(pilha.size() - 1);
                    tokens.remove(0);
                    isPossible = true;
                } else {
                    isPossible = false;
                }
            } else if (pilha.get(pilha.size() - 1).matches(specialRegEx)) {
                if (tokens.get(0) == pilha.get(pilha.size() - 1)) {
                    log += " " + tokens.get(0) + " = " + pilha.get(pilha.size() - 1) + "\n";
                    System.out.println("pilha.remove: " + pilha.get(pilha.size() - 1));
                    pilha.remove(pilha.size() - 1);
                    tokens.remove(0);
                    isPossible = true;
                } else {
                    isPossible = false;
                }
            } else if (pilha.get(pilha.size() - 1).matches(mathSymbolRegEx)) {
                if (tokens.get(0) == pilha.get(pilha.size() - 1)) {
                    log += " " + tokens.get(0) + " = " + pilha.get(pilha.size() - 1) + "\n";
                    System.out.println("pilha.remove: " + pilha.get(pilha.size() - 1));
                    pilha.remove(pilha.size() - 1);
                    tokens.remove(0);
                    isPossible = true;
                } else {
                    isPossible = false;
                }
            } else if (pilha.get(pilha.size() - 1).matches(logicalRegEx)) {
                if (tokens.get(0) == pilha.get(pilha.size() - 1)) {
                    log += " " + tokens.get(0) + " = " + pilha.get(pilha.size() - 1) + "\n";
                    System.out.println("pilha.remove: " + pilha.get(pilha.size() - 1));
                    pilha.remove(pilha.size() - 1);
                    tokens.remove(0);
                    isPossible = true;
                } else {
                    isPossible = false;
                }
            } else if (pilha.get(pilha.size() - 1).matches(blockRegEx)) {
                if (tokens.get(0) == pilha.get(pilha.size() - 1)) {
                    if (pilha.get(pilha.size() - 1) == "\n") {
                        log += "/n" + " = " + "/n\n";
                    } else {
                        log += "/t" + " = " + "/t\n";
                    }
                    System.out.println("pilha.remove: " + pilha.get(pilha.size() - 1));
                    pilha.remove(pilha.size() - 1);
                    tokens.remove(0);
                    isPossible = true;
                } else {
                    if (pilha.get(pilha.size() - 1) == "\n") {
                        log += "/n" + " ->" + "\t\t&\n";
                    } else {
                        log += "/t" + " ->" + "\t\t&\n";
                    }
                    System.out.println("pilha.remove: " + pilha.get(pilha.size() - 1));
                    pilha.remove(pilha.size() - 1);
                    isPossible = true;
                }
            } else if (pilha.get(pilha.size() - 1) == "var" || pilha.get(pilha.size() - 1) == "funcao_nome") {
                if (tokens.get(0).matches(varRegEx)) {
                    log += " " + tokens.get(0) + " = " + pilha.get(pilha.size() - 1) + "\n";
                    System.out.println("pilha.remove: " + pilha.get(pilha.size() - 1));
                    pilha.remove(pilha.size() - 1);
                    tokens.remove(0);
                    isPossible = true;
                } else {
                    isPossible = false;
                }
            } else if (pilha.get(pilha.size() - 1) == "num") {
                if (tokens.get(0).matches(numberRegEx)) {
                    log += " " + tokens.get(0) + " = " + pilha.get(pilha.size() - 1) + "\n";
                    System.out.println("pilha.remove: " + pilha.get(pilha.size() - 1));
                    pilha.remove(pilha.size() - 1);
                    tokens.remove(0);
                    isPossible = true;
                } else {
                    isPossible = false;
                }
            } else if (pilha.get(pilha.size() - 1) == "texto") {
                if (tokens.get(0).matches(textRegEx)) {
                    log += " " + tokens.get(0) + " = " + pilha.get(pilha.size() - 1) + "\n";
                    System.out.println("pilha.remove: " + pilha.get(pilha.size() - 1));
                    pilha.remove(pilha.size() - 1);
                    tokens.remove(0);
                    isPossible = true;
                } else {
                    isPossible = false;
                }
            } else {
                isPossible = false;
            }
        }
        return isPossible;
    }

    private Boolean funcao(String token) {
        System.out.println("\tMétodo funcao");
        if (token.equals("#m")) {
            log += "\t\tFuncao_Principal\n";
            System.out.println("\t\tpilha.remove: " + pilha.get(pilha.size() - 1));
            pilha.remove(pilha.size() - 1);
            System.out.println("\t\tpilha.add: Funcao_Principal");
            pilha.add("Funcao_Principal");
            System.out.println("\tFim do Método funcao");
            return true;
        } else if (token.equals("#f")) {
            log += "\t\tFuncao_Normal\n";
            System.out.println("\t\tpilha.remove: " + pilha.get(pilha.size() - 1));
            pilha.remove(pilha.size() - 1);
            System.out.println("\t\tpilha.add: Funcao_Normal");
            pilha.add("Funcao_Normal");
            System.out.println("\tFim do Método funcao");
            return true;
        } else {
            log += "\t\t?\n";
            System.out.println("\tFim do Método funcao");
            return false;
        }
    }

    private Boolean funcao_principal(String token) {
        System.out.println("\tMétodo funcao");
        if (token.equals("#m")) {
            log += "\tTab\n"
                    + "\t\t/n\n"
                    + "\t\t#\n"
                    + "\t\t#m\n";
            System.out.println("\t\tpilha.remove: " + pilha.get(pilha.size() - 1));
            pilha.remove(pilha.size() - 1);
            System.out.println("\t\tpilha.add: Tab");
            pilha.add("Tab");
            System.out.println("\t\tpilha.add: /n");
            pilha.add("\n");
            System.out.println("\t\tpilha.add: #");
            pilha.add("#");
            System.out.println("\t\tpilha.add: #m,");
            pilha.add("#m");
            System.out.println("\tFim do Método Funcao_Principal");
            return true;
        } else {
            log += "?\n";
            System.out.println("\tFim do Método Funcao_Principal");
            return false;
        }
    }

    private Boolean funcao_normal(String tokenA, String tokenB, String tokenC) {
        if (tokenA == "#f") {
            if (tokenB.matches(varRegEx) && tokenC == "#") {
                log += "\tTab\n"
                        + "\t\t/n\n"
                        + "\t\t#\n"
                        + "\t\tfuncao_nome\n"
                        + "\t\t#f\n";
                System.out.println("pilha.remove: " + pilha.get(pilha.size() - 1));
                pilha.remove(pilha.size() - 1);
                System.out.println("pilha.add: Tab");
                pilha.add("Tab");
                System.out.println("pilha.add: /n");
                pilha.add("\n");
                System.out.println("pilha.add: #");
                pilha.add("#");
                System.out.println("pilha.add: funcao_nome");
                pilha.add("funcao_nome");
                System.out.println("pilha.add: #f");
                pilha.add("#f");
                return true;
            } else if (tokenB.matches(varRegEx) && tokenC == "(") {
                log += "\tTab\n"
                        + "\t\t/n\n"
                        + "\t\t#\n"
                        + "\t\t)\n"
                        + "\t\tVar_Funcao\n"
                        + "\t\tvar\n"
                        + "\t\t(\n"
                        + "\t\tfuncao_nome\n"
                        + "\t\t#f\n";
                System.out.println("pilha.remove: " + pilha.get(pilha.size() - 1));
                pilha.remove(pilha.size() - 1);
                System.out.println("pilha.add: Tab");
                pilha.add("Tab");
                System.out.println("pilha.add: /n");
                pilha.add("\n");
                System.out.println("pilha.add: #");
                pilha.add("#");
                System.out.println("pilha.add: )");
                pilha.add(")");
                System.out.println("pilha.add: Var_Funcao");
                pilha.add("Var_Funcao");
                System.out.println("pilha.add: var");
                pilha.add("var");
                System.out.println("pilha.add: (");
                pilha.add("(");
                System.out.println("pilha.add: funcao_nome");
                pilha.add("funcao_nome");
                System.out.println("pilha.add: #f");
                pilha.add("#f");
                return true;
            } else {
                log += "\t\t?\n";
            }

            return false;
        } else {
            log += "\t\t?\n";
            return false;
        }
    }

    private Boolean var_funcao(String token) {
        if (token.equals(",")) {
            log += "\t\tVar_Funcao\n"
                    + "\t\tvar\n"
                    + "\t\t,\n";
            System.out.println("pilha.remove: " + pilha.get(pilha.size() - 1));
            pilha.remove(pilha.size() - 1);
            System.out.println("pilha.add: Var_Funcao");
            pilha.add("Var_Funcao");
            System.out.println("pilha.add: var");
            pilha.add("var");
            System.out.println("pilha.add: ,");
            pilha.add(",");
        } else {
            log += "\t\t&\n";
            pilha.remove(pilha.size() - 1);         
        }
        return true;
    }

    private Boolean tab(String tokenA, String tokenB) {
        if (tokenA == "\t") {
            if (tokenB == "\t") {
                log += "\t\tTab\n"
                        + "\t\t/t\n";
                System.out.println("pilha.remove: " + pilha.get(pilha.size() - 1));
                pilha.remove(pilha.size() - 1);
                System.out.println("pilha.add: Tab");
                pilha.add("Tab");
                System.out.println("pilha.add: /t");
                pilha.add("\t");
            } else if (tokenB.matches(systemCallRegEx)) {
                log += "\t\tTrema\n"
                        + "\t\t/t\n";
                System.out.println("pilha.remove: " + pilha.get(pilha.size() - 1));
                pilha.remove(pilha.size() - 1);
                System.out.println("pilha.add: Trema");
                pilha.add("Trema");
                System.out.println("pilha.add: /t");
                pilha.add("\t");
            } else if (tokenB.matches(logicalRegEx)) {
                log += "\t\tTab\n"
                        + "\t\t/n\n"
                        + "\t\tTrema\n"
                        + "\t\tOpc\n"
                        + "\t\tOp_log\n"
                        + "\t\t/t\n";
                System.out.println("pilha.remove: " + pilha.get(pilha.size() - 1));
                pilha.remove(pilha.size() - 1);
                System.out.println("pilha.add: Tab");
                pilha.add("Tab");
                System.out.println("pilha.add: /n");
                pilha.add("\n");
                System.out.println("pilha.add: Trema");
                pilha.add("Trema");
                System.out.println("pilha.add: Opc");
                pilha.add("Opc");
                System.out.println("pilha.add: Op_log");
                pilha.add("Op_log");
                System.out.println("pilha.add: /t");
                pilha.add("\t");

            } else {
                log += "\t\t&\n";
                System.out.println("pilha.remove: " + pilha.get(pilha.size() - 1));
                pilha.remove(pilha.size() - 1);
            }
        } else {
            log += "\t\t&\n";
            System.out.println("pilha.remove: " + pilha.get(pilha.size() - 1));
            pilha.remove(pilha.size() - 1);
        }
        return true;
    }

    private Boolean trema(String tokenA, String tokenB, String tokenC) {
        if (tokenA == "#") {
            String regxA = varRegEx + "|" + numberRegEx + "|" + "[(]" + "|" + textRegEx;
            String regxB = varRegEx + "|" + numberRegEx + "|" + "[(]" + "|" + textRegEx;
            if (tokenB.matches(varRegEx)) {
                log += "\t\tTab\n"
                        + "\t\t/n\n"
                        + "\t\tVir_Opc\n"
                        + "\t\tvar\n"
                        + "\t\t#\n";
                System.out.println("pilha.remove: " + pilha.get(pilha.size() - 1));
                pilha.remove(pilha.size() - 1);
                System.out.println("pilha.add: Tab");
                pilha.add("Tab");
                System.out.println("pilha.add: /n");
                pilha.add("\n");
                pilha.add("Vir_Opc");
                System.out.println("pilha.add: Vir_Opc");
                pilha.add("var");
                System.out.println("pilha.add: var");
                pilha.add("#");
                System.out.println("pilha.add: #");
                return true;
            } else if (tokenB == "\n") {
                log += "\t\tTab\n"
                        + "\t\t/n\n"
                        + "\t\t#\n";
                System.out.println("pilha.remove: " + pilha.get(pilha.size() - 1));
                pilha.remove(pilha.size() - 1);
                System.out.println("pilha.add: Tab");
                pilha.add("Tab");
                System.out.println("pilha.add: /n");
                pilha.add("\n");
                System.out.println("pilha.add: #");
                pilha.add("#");
                return true;
            } else if (tokenB.matches(regxA)) {
                log += "\t\tTab\n"
                        + "\t\t/n\n"
                        + "\t\tOpc\n"
                        + "\t\t#\n";
                System.out.println("pilha.remove: " + pilha.get(pilha.size() - 1));
                pilha.remove(pilha.size() - 1);
                System.out.println("pilha.add: Tab");
                pilha.add("Tab");
                System.out.println("pilha.add: /n");
                pilha.add("\n");
                System.out.println("pilha.add: Opc");
                pilha.add("Opc");
                System.out.println("pilha.add: #");
                pilha.add("#");
                return true;
            } else if (tokenB.matches(logicalRegEx) && tokenC.matches(regxB)) {
                log += "\t\tTab\n"
                        + "\t\t/n\n"
                        + "\t\t#\n"
                        + "\t\tOpc\n"
                        + "\t\tOp_log\n"
                        + "\t\t#\n";
                System.out.println("pilha.remove: " + pilha.get(pilha.size() - 1));
                pilha.remove(pilha.size() - 1);
                System.out.println("pilha.add: Tab");
                pilha.add("Tab");
                System.out.println("pilha.add: /n");
                pilha.add("\n");
                System.out.println("pilha.add: #");
                pilha.add("#");
                System.out.println("pilha.add: Opc");
                pilha.add("Opc");
                System.out.println("pilha.add: Op_log");
                pilha.add("Op_log");
                System.out.println("pilha.add: #");
                pilha.add("#");
                System.out.println("pilha.add: #");
                return true;
            } else {
                log += "\t\t?\n";
                return false;
            }
        } else if (tokenA == "#i") {
            log += "\t\tTab\n"
                    + "\t\t/n\n"
                    + "\t\tvar\n"
                    + "\t\t#i\n";
            System.out.println("pilha.remove: " + pilha.get(pilha.size() - 1));
            pilha.remove(pilha.size() - 1);
            System.out.println("pilha.add: Tab");
            pilha.add("Tab");
            System.out.println("pilha.add: /n");
            pilha.add("\n");
            System.out.println("pilha.add: var");
            pilha.add("var");
            System.out.println("pilha.add: #i");
            pilha.add("#i");
            return true;
        } else if (tokenA == "#o") {
            log += "\t\tTab\n"
                    + "\t\t/n\n"
                    + "\t\tOpc\n"
                    + "\t\t#o\n";
            System.out.println("pilha.remove: " + pilha.get(pilha.size() - 1));
            pilha.remove(pilha.size() - 1);
            System.out.println("pilha.add: Tab");
            pilha.add("Tab");
            System.out.println("pilha.add: /n");
            pilha.add("\n");
            System.out.println("pilha.add: Opc");
            pilha.add("Opc");
            System.out.println("pilha.add: #o");
            pilha.add("#o");
            return true;
        } else if (tokenA == "##") {
            log += "\t\tTab\n"
                    + "\t\t/n\n"
                    + "\t\t#\n"
                    + "\t\tOpc\n"
                    + "\t\tOp_log\n"
                    + "\t\t##\n";
            System.out.println("pilha.remove: " + pilha.get(pilha.size() - 1));
            pilha.remove(pilha.size() - 1);
            System.out.println("pilha.add: Tab");
            pilha.add("Tab");
            System.out.println("pilha.add: /n");
            pilha.add("\n");
            System.out.println("pilha.add: #");
            pilha.add("#");
            System.out.println("pilha.add: Opc");
            pilha.add("Opc");
            System.out.println("pilha.add: Opc_log");
            pilha.add("Op_log");
            System.out.println("pilha.add: ##");
            pilha.add("##");
            return true;
        } else {
            log += "\t\t?\n";
            return false;
        }
    }

    private Boolean opc(String tokenA) {
        if (tokenA.matches(varRegEx + "|" + numberRegEx + "|" + "[(]" + "|" + textRegEx) && tokenA != ",") {
            log += "\t\tOpc\n"
                    + "\t\tTipo\n";
            System.out.println("pilha.remove: " + pilha.get(pilha.size() - 1));
            pilha.remove(pilha.size() - 1);
            pilha.add("Opc");
            System.out.println("pilha.add: Opc");
            pilha.add("Tipo");
            System.out.println("pilha.add: Tipo");
        } else if (tokenA.matches(mathSymbolRegEx) && tokenA != ",") {
            log += "\t\tOpc\n"
                    + "\t\tOp_mat\n";
            System.out.println("pilha.remove: " + pilha.get(pilha.size() - 1));
            pilha.remove(pilha.size() - 1);
            System.out.println("pilha.add: Opc");
            pilha.add("Opc");
            System.out.println("pilha.add: Opc_mat");
            pilha.add("Op_mat");
        } else if (tokenA.matches(logicalRegEx) && tokenA != ",") {
            log += "\t\tOpc\n"
                    + "\t\tOp_log\n";
            System.out.println("pilha.remove: " + pilha.get(pilha.size() - 1));
            pilha.remove(pilha.size() - 1);
            System.out.println("pilha.add: Opc");
            pilha.add("Opc");
            System.out.println("pilha.add: Opc_log");
            pilha.add("Op_log");
        } else {
            log += "\t\t&\n";
            System.out.println("pilha.remove: " + pilha.get(pilha.size() - 1));
            pilha.remove(pilha.size() - 1);
        }
        return true;
    }

    private Boolean tipo(String tokenA) {
        if (tokenA.matches(varRegEx)) {
            log += "\t\tvar\n";
            System.out.println("pilha.remove: " + pilha.get(pilha.size() - 1));
            pilha.remove(pilha.size() - 1);
            System.out.println("pilha.add: var");
            pilha.add("var");
            return true;
        } else if (tokenA.matches(numberRegEx)) {
            log += "\t\tnum\n";
            System.out.println("pilha.remove: " + pilha.get(pilha.size() - 1));
            pilha.remove(pilha.size() - 1);
            System.out.println("pilha.add: num");
            pilha.add("num");
            return true;
        } else if (tokenA.matches("[(]")) {
            log += "\t\t)\n"
                    + "\t\tOpc\n"
                    + "\t\t(\n";
            System.out.println("pilha.remove: " + pilha.get(pilha.size() - 1));
            pilha.remove(pilha.size() - 1);
            System.out.println("pilha.add: )");
            pilha.add(")");
            System.out.println("pilha.add: Opc");
            pilha.add("Opc");
            System.out.println("pilha.add: (");
            pilha.add("(");
            return true;
        } else if (tokenA.matches(textRegEx)) {
            log += "\t\ttexto\n";
            System.out.println("pilha.remove: " + pilha.get(pilha.size() - 1));
            pilha.remove(pilha.size() - 1);
            System.out.println("pilha.add: texto");
            pilha.add("texto");
            return true;
        } else {
            log += "\t\t?\n";
            return false;
        }
    }

    private Boolean op_mat(String tokenA) {
        if (tokenA == "+") {
            log += "\t\t+\n";
            System.out.println("pilha.remove: " + pilha.get(pilha.size() - 1));
            pilha.remove(pilha.size() - 1);
            System.out.println("pilha.add: +");
            pilha.add("+");
            return true;
        } else if (tokenA == "-") {
            log += "\t\t-\n";
            System.out.println("pilha.remove: " + pilha.get(pilha.size() - 1));
            pilha.remove(pilha.size() - 1);
            System.out.println("pilha.add: -");
            pilha.add("-");
            return true;
        } else if (tokenA == "*") {
            log += "\t\t*\n";
            System.out.println("pilha.remove: " + pilha.get(pilha.size() - 1));
            pilha.remove(pilha.size() - 1);
            System.out.println("pilha.add: *");
            pilha.add("*");
            return true;
        } else if (tokenA == "/") {
            log += "\t\t//\n";
            System.out.println("pilha.remove: " + pilha.get(pilha.size() - 1));
            pilha.remove(pilha.size() - 1);
            System.out.println("pilha.add: /");
            pilha.add("/");
            return true;
        } else {
            log += "\t\t?\n";
            return false;
        }
    }

    private Boolean op_log(String tokenA) {
        if (tokenA == ">") {
            log += "\t\t>\n";
            System.out.println("pilha.remove: " + pilha.get(pilha.size() - 1));
            pilha.remove(pilha.size() - 1);
            System.out.println("pilha.add: >");
            pilha.add(">");
            return true;
        } else if (tokenA == "<") {
            log += "\t\t<\n";
            System.out.println("pilha.remove: " + pilha.get(pilha.size() - 1));
            pilha.remove(pilha.size() - 1);
            System.out.println("pilha.add: <");
            pilha.add("<");
            return true;
        } else if (tokenA == "=") {
            log += "\t\t=\n";
            System.out.println("pilha.remove: " + pilha.get(pilha.size() - 1));
            pilha.remove(pilha.size() - 1);
            System.out.println("pilha.add: =");
            pilha.add("=");
            return true;
        } else if (tokenA == "=>") {
            log += "\t\t=>\n";
            System.out.println("pilha.remove: " + pilha.get(pilha.size() - 1));
            pilha.remove(pilha.size() - 1);
            System.out.println("pilha.add: =>");
            pilha.add("=>");
            return true;
        } else if (tokenA == "<=") {
            log += "\t\t<=\n";
            System.out.println("pilha.remove: " + pilha.get(pilha.size() - 1));
            pilha.remove(pilha.size() - 1);
            System.out.println("pilha.add: <=");
            pilha.add("<=");
            return true;
        } else if (tokenA == "-") {
            log += "\t\t-\n";
            System.out.println("pilha.remove: " + pilha.get(pilha.size() - 1));
            pilha.remove(pilha.size() - 1);
            System.out.println("pilha.add: -");
            pilha.add("-");
            return true;
        } else if (tokenA == "|") {
            log += "\t\t|\n";
            System.out.println("pilha.remove: " + pilha.get(pilha.size() - 1));
            pilha.remove(pilha.size() - 1);
            System.out.println("pilha.add: |");
            pilha.add("|");
            return true;
        } else if (tokenA == "!") {
            log += "\t\t!\n";
            System.out.println("pilha.remove: " + pilha.get(pilha.size() - 1));
            pilha.remove(pilha.size() - 1);
            System.out.println("pilha.add: !");
            pilha.add("!");
            return true;
        } else {
            log += "\t\t?\n";
            return false;
        }
    }

    private Boolean vir_opc(String token) {
        String regEx = varRegEx + "|"
                + numberRegEx + "|"
                + textRegEx + "|"
                + mathSymbolRegEx + "|"
                + logicalRegEx + "|"
                + specialRegEx;
        if (token == ",") {
            log += "\t\tVir_Opc\n"
                    + "\t\t,\n";
            System.out.println("pilha.remove: " + pilha.get(pilha.size() - 1));
            pilha.remove(pilha.size() - 1);
            System.out.println("pilha.add: Vir_Opc");
            pilha.add("Vir_Opc");
            System.out.println("pilha.add: ,");
            pilha.add(",");
        } else if (token.matches(varRegEx)) {
            log += "\t\tVir_Opc\n"
                    + "\t\tvar\n";
            System.out.println("pilha.remove: " + pilha.get(pilha.size() - 1));
            pilha.remove(pilha.size() - 1);
            System.out.println("pilha.add: Vir_Opc");
            pilha.add("Vir_Opc");
            System.out.println("pilha.add: var");
            pilha.add("var");
        } else if (token.matches(regEx)) {
            log += "\t\tVir_Opc\n"
                    + "\t\tOpc\n";
            System.out.println("pilha.remove: " + pilha.get(pilha.size() - 1));
            pilha.remove(pilha.size() - 1);
            System.out.println("pilha.add: Vir_Opc");
            pilha.add("Vir_Opc");
            System.out.println("pilha.add: Opc");
            pilha.add("Opc");
        } else {
            log += "\t\t&\n";
            System.out.println("pilha.remove: " + pilha.get(pilha.size() - 1));
            pilha.remove(pilha.size() - 1);
        }
        return true;
    }

}
