package br.com.brunotonia;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Parser {

    private final String numberRegEx = "(([0-9]*[.][0-9]+)|([0-9]+))";
    private final String varRegEx = "(mentr)[A-Za-z0-9]*";
    private final String mathSymbolRegEx = "[+-=*:]";
    private final String blockRegEx = "[\n\t]";
    private final String logicalRegEx = "m([>][=<]|[=][<=]|[><])";
    private final String specialRegEx = "[(),]";
    private final String systemCallRegEx = "[m][m(Da)|m(Mostra)]|[m]";
    private final String methodRegEx = "[m][fm]";
    private final String textRegEx = "'[A-Za-z0-9-_ +\\-\\*\\\\:^\\n]*''";

    private List<String> pilha;
    private List<String> tokens;
    
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
                    System.out.println("\t" + aux);
                    //tokens.add(aux);
                }
                linha = br.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Boolean Executar() {
        pilha = new ArrayList<>();
        Boolean ehPossivel = true;
        for (int i = 0; i < tokens.size(); i++) {
            // Seria mid uma substring?
            if (tokens.get(i).substring(0, 1).equals("/*")) {
                tokens.remove(i);
                i--;
            }
        }
        tokens.add("$");
        pilha.add("$");
        pilha.add("Funcao");
        while (ehPossivel == true && tokens.size() > 0) {
            if (pilha.get(pilha.size() - 1).equals("$")) {
                if (tokens.get(0).equals("$")) {
                    pilha.remove(pilha.size() - 1);
                    tokens.remove(0);
                    ehPossivel = true;
                } else {
                    ehPossivel = false;
                }
            } else if (pilha.get(pilha.size() - 1).equals("Funcao")) {
                ehPossivel = Funcao(tokens.get(0));
            } else if (pilha.get(pilha.size() - 1).equals("Funcao_Principal")) {
                ehPossivel = FuncaoPrincipal(tokens.get(0));
            } else if (pilha.get(pilha.size() - 1).equals("Funcao_Normal")) {
                if (tokens.size() > 2) {
                    ehPossivel = FuncaoNormal(tokens.get(0), tokens.get(1), tokens.get(2));
                } else {
                    ehPossivel = false;
                }
            } else if (pilha.get(pilha.size() - 1).equals("Var_Funcao")) {
                ehPossivel = var_funcao(tokens.get(0));
            } else if (pilha.get(pilha.size() - 1).equals("Tab")) {
                if (tokens.size() > 1) {
                    ehPossivel = tab(tokens.get(0), tokens.get(1));
                } else {
                    ehPossivel = false;
                }
            } else if (pilha.get(pilha.size() - 1).equals("Eme")) {
                if (tokens.size() > 2) {
                    ehPossivel = trema(tokens.get(0), tokens.get(1), tokens.get(2));
                } else {
                    ehPossivel = false;
                }
            } else if (pilha.get(pilha.size() - 1).equals("Opc")) {
                ehPossivel = opc(tokens.get(0));
            } else if (pilha.get(pilha.size() - 1).equals("Tipo")) {
                ehPossivel = tipo(tokens.get(0));
            } else if (pilha.get(pilha.size() - 1).equals("Op_mat")) {
                ehPossivel = op_mat(tokens.get(0));
            } else if (pilha.get(pilha.size() - 1).equals("Op_log")) {
                ehPossivel = op_log(tokens.get(0));
            //} else if (Utils::regexComparator(methodRegEx, pilha.get(pilha.size() -1))) {
            } else if (pilha.get(pilha.size() - 1).matches(methodRegEx)) {
                if (tokens.get(0).equals(pilha.get(pilha.size() - 1))) {
                    pilha.remove(pilha.size() - 1);
                    tokens.remove(0);
                    ehPossivel = true;
                } else {
                    ehPossivel = false;
                }
            //} else if (Utils::regexComparator(systemCallRegEx, pilha.get(pilha.size() - 1))) {
            } else if (pilha.get(pilha.size() - 1).matches(systemCallRegEx)) {
                if (tokens.get(0).equals(pilha.get(pilha.size() - 1))) {
                    pilha.remove(pilha.size() - 1);
                    tokens.remove(0);
                    ehPossivel = true;
                } else {
                    ehPossivel = false;
                }
                //} else if (Utils::regexComparator(mathSymbolRegEx, pilha.get(pilha.size() - 1))) {
            } else if (pilha.get(pilha.size() - 1).matches(mathSymbolRegEx)) {
                if (tokens.get(0).equals(pilha.get(pilha.size() - 1))) {
                    pilha.remove(pilha.size() - 1);
                    tokens.remove(0);
                    ehPossivel = true;
                } else {
                    ehPossivel = false;
                }
                //} else if (Utils::regexComparator(logicalRegEx, pilha.get(pilha.size() - 1))) {
            } else if (pilha.get(pilha.size() - 1).matches(logicalRegEx)) {
                if (tokens.get(0).equals(pilha.get(pilha.size() - 1))) {
                    pilha.remove(pilha.size() - 1);
                    tokens.remove(0);
                    ehPossivel = true;
                } else {
                    ehPossivel = false;
                }
                //} else if (Utils::regexComparator(blockRegEx, pilha.get(pilha.size() - 1))) {
            } else if (pilha.get(pilha.size() - 1).matches(blockRegEx)) {
                if (tokens.get(0).equals(pilha.get(pilha.size() - 1))) {
                    pilha.remove(pilha.size() - 1);
                    tokens.remove(0);
                    ehPossivel = true;
                } else {
                    ehPossivel = false;
                }
                //} else if (Utils::regexComparator(specialRegEx, pilha.get(pilha.size() - 1))) {
            } else if (pilha.get(pilha.size() - 1).matches(specialRegEx)) {
                if (tokens.get(0).equals(pilha.get(pilha.size() - 1))) {
                    pilha.remove(pilha.size() - 1);
                    tokens.remove(0);
                    ehPossivel = true;
                } else {
                    ehPossivel = false;
                }
            } else if (pilha.get(pilha.size() - 1).equals("var")) {
                //if (Utils::regexComparator(textRegEx, tokens.get(0) )) {
                if (tokens.get(0).matches(textRegEx)) {
                    pilha.remove(pilha.size() - 1);
                    tokens.remove(0);
                    ehPossivel = true;
                } else {
                    ehPossivel = false;
                }
            } else if (pilha.get(pilha.size() - 1).equals("numero")) {
                //if (Utils::regexComparator(numberRegEx, tokens.get(0))) {
                if (tokens.get(0).matches(numberRegEx)) {
                    pilha.remove(pilha.size() - 1);
                    tokens.remove(0);
                    ehPossivel = true;
                } else {
                    ehPossivel = false;
                }
            } else if (pilha.get(pilha.size() - 1).equals("texto")) {
                //if (Utils::regexComparator(textRegEx, tokens.get(0))) {
                if (tokens.get(0).matches(textRegEx)) {
                    pilha.remove(pilha.size() - 1);
                    tokens.remove(0);
                    ehPossivel = true;
                } else {
                    ehPossivel = false;
                }
            } else {
                ehPossivel = false;
            }
        }
        return ehPossivel;
    }

    private Boolean Funcao(String token) {
        if (token.equals("m##")) {
            pilha.remove(pilha.size() - 1);
            pilha.add("Funcao_Principal");
            return true;
        } else if (token.equals("##m")) {
            pilha.add("Funcao_Normal");
            return true;
        } else {
            return false;
        }
    }

    private Boolean FuncaoPrincipal(String token) {
        if (token.equals("m##")) {
            pilha.remove(pilha.size() - 1);
            pilha.add("Tab");
            pilha.add("/n");
            pilha.add("m");
            pilha.add("m##");
            return true;
        } else {
            return false;
        }
    }

    private Boolean FuncaoNormal(String tokenA, String tokenB, String tokenC) {
        if (tokenA.equals("##m")) {
            //if (Utils::regexComparator (varRegEx, tokenB) &&  tokenC == "m") {
            if (tokenB.matches(varRegEx) && tokenC.equals("m")) {
                pilha.remove(pilha.size() - 1);
                pilha.add("Tab");
                pilha.add("/n");
                pilha.add("m");
                pilha.add("Funcao_Nome");
                pilha.add("##m");
                return true;
                //} else if (Utils::regexComparator (varRegEx, tokenB) && tokenC == "(" ) {
            } else if (tokenB.matches(varRegEx) && tokenC.equals("(")) {
                pilha.remove(pilha.size() - 1);
                pilha.add("Tab");
                pilha.add("/n");
                pilha.add("m");
                pilha.add(")");
                pilha.add("Var_Funcao");
                pilha.add("var");
                pilha.add("(");
                pilha.add("Funcao_Nome");
                pilha.add("##m");
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private Boolean var_funcao(String token) {
        if (token.equals(",")) {
            pilha.remove(pilha.size() - 1);
            pilha.add("Var_Funcao");
            pilha.add("mentr");
            pilha.add(",");
        } else {
            pilha.remove(pilha.size() - 1);
        }
        return true;
    }

    private Boolean tab(String tokenA, String tokenB) {
        if (tokenA.equals("\t")) {
            if (tokenB.equals("\t")) {
                pilha.remove(pilha.size() - 1);
                pilha.add("Tab");
                pilha.add("\t");
            //} else if (Utils::regexComparator(systemCallRegEx, tokenB)) {
            } else if (tokenB.matches(systemCallRegEx)) {
                pilha.remove(pilha.size() - 1);
                pilha.add("Eme");
                pilha.add("\t");
            //} else if (Utils::regexComparator(logicalRegEx, tokenB)) {
            } else if (tokenB.matches(logicalRegEx)) {
                pilha.remove(pilha.size() - 1);
                pilha.add("Opc");
                pilha.add("Op_Log");
                pilha.add("\t");
            } else {
                pilha.remove(pilha.size() - 1);
            }
        } else {
            pilha.remove(pilha.size() - 1);
        }
        return true;
    }

    private Boolean trema(String tokenA, String tokenB, String tokenC) {
        if (tokenA.equals("m")) {
            //if (Utils::regexComparator(varRegEx + "|" + numberRegEx + "|" + "[(]" + "|" + textRegEx,tokenB) && tokenC == "\n") {
            if (tokenB.matches(varRegEx + "|" + numberRegEx + "|" + "[(]" + "|" + textRegEx) && tokenC.equals("\n")) {
                pilha.remove(pilha.size() - 1);
                pilha.add("Tab");
                pilha.add("/n");
                pilha.add("Opc");
                pilha.add("m");
                return true;
            //} else if (Utils::regexComparator (mathSymbolRegEx, tokenB) &&  Utils::regexComparator(varRegEx + "|" + numberRegEx + "|" + "[(]" + "|" + textRegEx,tokenC)) {
            } else if (tokenB.matches(mathSymbolRegEx) && tokenC.matches(varRegEx + "|" + numberRegEx + "|" + "[(]" + "|" + textRegEx)) {
                pilha.remove(pilha.size() - 1);
                pilha.add("Tab");
                pilha.add("/n");
                pilha.add("m");
                pilha.add("Opc");
                pilha.add("Opc_log");
                pilha.add("m");
                return true;
            } else {
                return false;
            }
        } else if (tokenA.equals("mDa")) {
            pilha.remove(pilha.size() - 1);
            pilha.add("Tab");
            pilha.add("/n");
            pilha.add("mentr");
            pilha.add("mDa");
            return true;
        } else if (tokenA.equals("mMostra")) {
            pilha.remove(pilha.size() - 1);
            pilha.add("Tab");
            pilha.add("/n");
            pilha.add("Opc");
            pilha.add("mMostra");
            return true;
        } else if (tokenA.equals("mm")) {
            pilha.remove(pilha.size() - 1);
            pilha.add("Tab");
            pilha.add("/n");
            pilha.add("m");
            pilha.add("Opc");
            pilha.add("Opc_log");
            pilha.add("##m");
            return true;
        } else {
            return false;
        }
    }

    private Boolean opc(String tokenA) {
        //if(Utils::regexComparator(varRegEx + "|" + numberRegEx + "|" + "[(]" + "|" + textRegEx,tokenA)) {
        if (tokenA.matches(varRegEx + "|" + numberRegEx + "|" + "[(]" + "|" + textRegEx)) {
            pilha.remove(pilha.size() - 1);
            pilha.add("Opc");
            pilha.add("Tipo");
        //} else if (Utils::regexComparator(mathSymbolRegEx, tokenA)) {
        } else if (tokenA.matches(mathSymbolRegEx)) {
            pilha.remove(pilha.size() - 1);
            pilha.add("Opc");
            pilha.add("Op_mat");
        // } else if (Utils::regexComparator(logicalRegEx, tokenA)) {
        } else if (tokenA.matches(logicalRegEx)) {
            pilha.remove(pilha.size() - 1);
            pilha.add("Opc");
            pilha.add("Op_log");
        } else {
            pilha.remove(pilha.size() - 1);
        }
        return true;
    }
    
    private Boolean tipo(String tokenA) {
        //if(Utils::regexComparator(varRegEx,tokenA)) {
        if(tokenA.matches(varRegEx)) {
            pilha.remove(pilha.size() - 1);
            pilha.add("mentr");
            return true;
        //} else if(Utils::regexComparator(numberRegEx ,tokenA)) {
        } else if(tokenA.matches(numberRegEx)) {
            pilha.remove(pilha.size() - 1);
            pilha.add("numero");
            return true;
        //} else if(Utils::regexComparator("[(]",tokenA)) {
        } else if(tokenA.matches("[(]")) {
            pilha.remove(pilha.size() - 1);
            pilha.add(")");
            pilha.add("Opc");
            pilha.add("(");
            return true;
        //} else if(Utils::regexComparator(textRegEx,tokenA)) {
        } else if(tokenA.matches(textRegEx)) {
            pilha.remove(pilha.size() - 1);
            pilha.add("texto");
            return true;
        }
        else {
            return false;
        }
    }

    private Boolean op_mat(String tokenA) {
        if(tokenA.equals("m+")) {
            pilha.remove(pilha.size() - 1);
            pilha.add("m+");
            return true;
        } else if(tokenA.equals("m-")) {
            pilha.remove(pilha.size() - 1);
            pilha.add("m-");
            return true;
        } else if(tokenA.equals("m*")) {
            pilha.remove(pilha.size() - 1);
            pilha.add("m*");
            return true;
        } else if(tokenA.equals("m:")) {
            pilha.remove(pilha.size() - 1);
            pilha.add("m:");
            return true;
        } else {
            return false;
        }
    }

    private Boolean op_log(String tokenA) {
        if(tokenA.equals("m>")) {
            pilha.remove(pilha.size() - 1);
            pilha.add("m>");
            return true;
        } else if(tokenA.equals("m<")) {
            pilha.remove(pilha.size() - 1);
            pilha.add("m<");
            return true;
        } else if(tokenA.equals("m=")) {
            pilha.remove(pilha.size() - 1);
            pilha.add("m=");
            return true;
        } else if(tokenA.equals("m>=")) {
            pilha.remove(pilha.size() - 1);
            pilha.add("m>=");
            return true;
        } else if(tokenA.equals("m<=")) {
            pilha.remove(pilha.size() - 1);
            pilha.add("m<=");
            return true;
        } else if(tokenA.equals("m==")) {
            pilha.remove(pilha.size() - 1);
            pilha.add("m==");
            return true;
        } else if(tokenA.equals("m><")) {
            pilha.remove(pilha.size() - 1);
            pilha.add("m><");
            return true;
        /*} else if(tokenA.equals("m=")) {
            pilha.remove(pilha.size() - 1);
            pilha.add("m=");
            return true;*/
        } else {
        return false;
        }
    }

}
