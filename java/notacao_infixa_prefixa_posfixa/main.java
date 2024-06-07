/* 
    Referências: 
        - https://panda.ime.usp.br/algoritmos/static/algoritmos/08-posfixa.html
        - https://codereview.stackexchange.com/questions/35750/postfix-evaluation-using-a-stack
        - https://ankurm.com/java-program-to-evaluate-postfix-expressions/
*/

import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String expressaoInfixa = "";
        String expressaoPosfixa = "";
        char[] variaveis;
        double[] valores;
        
        int opcao = 0;
        while (opcao != 5) {
            System.out.println("\n\nMenu de Opções:");
            System.out.println("1. Entrada da expressão aritmética na notação infixa");
            System.out.println("2. Entrada dos valores numéricos associados às variáveis");
            System.out.println("3. Conversão da expressão para notação posfixa");
            System.out.println("4. Avaliação da expressão");
            System.out.println("5. Encerramento do programa");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    System.out.print("Digite a expressão aritmética na notação infixa: ");
                    expressaoInfixa = scanner.nextLine();
                    if (validarExpressao(expressaoInfixa)) {
                        System.out.println("Expressão válida.");
                    } else {
                        System.out.println("Expressão inválida.");
                    }
                    break;
                case 2:
                    if (!expressaoInfixa.isEmpty()) {
                        variaveis = extrairVariaveis(expressaoInfixa);
                        valores = new double[variaveis.length];
                        if (valores.length > 0)
                            System.out.print("Valores numéricos associados às variáveis: ");
                        for (int i = 0; i < variaveis.length; i++) {
                            System.out.print(variaveis[i]);
                        }
                    }else {
                        System.out.println("Por favor, insira a expressão primeiro.");
                    }
                    break;
                case 3:
                    if (!expressaoInfixa.isEmpty()) {
                        expressaoPosfixa = converterParaPosfixa(expressaoInfixa);
                        System.out.println("Expressão posfixa: " + expressaoPosfixa);
                    } else {
                        System.out.println("Por favor, insira a expressão primeiro.");
                    }
                    break;
                case 4:
                    if (!expressaoPosfixa.isEmpty()) {
                        variaveis = extrairVariaveis(expressaoPosfixa);
                        valores = new double[variaveis.length];
                        if (valores != null && valores.length > 0) {
                            double resultado = calcularExpressaoPosfixa(expressaoPosfixa);
                            System.out.println("Resultado da expressão: " + resultado);
                        } else {
                            System.out.println("Por favor, insira os valores das variáveis primeiro.");
                        }
                    } else {
                        System.out.println("Por favor, insira a expressão primeiro.");
                    }
                    break;
                case 5:
                    System.out.println("Encerrando o programa...");
                    break;
                default:
                    System.out.println("Opção inválida.");
                    break;
        }
    }

        scanner.close();
    }

    public static boolean validarExpressao(String expressao) {
        Pilha pilhaParenteses = new Pilha(expressao.length());
        
        for (int i = 0; i < expressao.length(); i++) {
            char c = expressao.charAt(i);
            if (c == '(') {
                pilhaParenteses.push(c);
            } else if (c == ')') {
                if (pilhaParenteses.IsEmpty()) {
                    return false;
                } else {
                    pilhaParenteses.pop();
                }
            } else if (!Character.isLetterOrDigit(c) && c != '+' && c != '-' && c != '*' && c != '/' && c != '^') {
                return false;
            }
        }

        return pilhaParenteses.IsEmpty();
    }

    public static char[] extrairVariaveis(String expressao) {
        StringBuilder variaveisBuilder = new StringBuilder();
        
        for (int i = 0; i < expressao.length(); i++) {
            char c = expressao.charAt(i);
            if (Character.isDigit(c)) {
                variaveisBuilder.append(c);
            }
        }

        return variaveisBuilder.toString().toCharArray();
    }

    public static String converterParaPosfixa(String expressao) {
        StringBuilder posfixa = new StringBuilder();
        Pilha pilha = new Pilha(expressao.length());
        
        for (int i = 0; i < expressao.length(); i++) {
            char c = expressao.charAt(i);
            
            if (Character.isLetterOrDigit(c)) {
                posfixa.append(c);
            } else if (c == '(') {
                pilha.push(c);
            } else if (c == ')') {
                while (!pilha.IsEmpty() && pilha.top() != '(') {
                    posfixa.append(pilha.pop());
                }
                pilha.pop();
            } else {
                int operador = obterOperador(c);
                while (!pilha.IsEmpty() && obterOperador(pilha.top()) >= operador) {
                    posfixa.append(pilha.pop());
                }
                pilha.push(c);
            }
        }
        
        while (!pilha.IsEmpty()) {
            posfixa.append(pilha.pop());
        }
        
        return posfixa.toString();
    }

    private static int obterOperador(char operador) {
        switch (operador) {
            case '^':
                return 3;
            case '*':
            case '/':
                return 2;
            case '+':
            case '-':
                return 1;
            default:
                return 0;
        }
    }

    public static double calcularExpressaoPosfixa(String expressao) {
        Pilha pilha = new Pilha(expressao.length());

        for (int i = 0; i < expressao.length(); i++) {
            char caracter = expressao.charAt(i);

            if (Character.isDigit(caracter)) {
                double operando = Character.getNumericValue(caracter);
                pilha.push((char)operando);
            } else {
                int operando2 = pilha.pop();
                int operando1 = pilha.pop();

                switch (caracter) {
                    case '+':
                        double soma = operando1 + operando2;
                        pilha.push((char)soma);
                        break;
                    case '-':
                        char subtracao = (char)(operando1 - operando2);
                        pilha.push(subtracao);
                        break;
                    case '*':
                        char produto = (char)((double)operando1 * operando2);
                        pilha.push(produto);
                        break;
                    case '/':
                        char divisao = (char)(operando1 / operando2);
                        pilha.push(divisao);
                        break;
                    default:
                        System.out.print("Operador inválido: " + caracter);
                }
            }
        }

        return pilha.pop();
    }
}
