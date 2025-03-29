import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArvoreBinaria arvore = new ArvoreBinaria();
        String expression = "";
        int option = 0;
        
        do {
            System.out.println("\nMenu de Opções:");
            System.out.println("1. Entrada da expressão aritmética na notação infixa.");
            System.out.println("2. Criação da árvore binária de expressão aritmética.");
            System.out.println("3. Exibição da árvore (pré-ordem, em ordem e pós-ordem).");
            System.out.println("4. Cálculo da expressão.");
            System.out.println("5. Encerramento do programa.");
            System.out.print("Escolha uma opção: ");
            option = scanner.nextInt();
            scanner.nextLine();
            
            switch(option) {
                case 1:
                    System.out.print("Digite a expressão aritmética (notação infixa): ");
                    expression = scanner.nextLine();
                    if(ArvoreBinaria.isValidExpression(expression)) {
                        System.out.println("Expressão válida!");
                    } else {
                        System.out.println("Expressão inválida!");
                        expression = "";
                    }
                    break;
                case 2:
                    if(expression.isEmpty()) {
                        System.out.println("Primeiro insira uma expressão válida (Opção 1).");
                    } else {
                        String postfix = ArvoreBinaria.infixToPostfix(expression);
                        arvore.construirArvore(postfix);
                        System.out.println("Árvore criada com sucesso.");
                    }
                    break;
                case 3:
                    if(arvore.getRoot() == null) {
                        System.out.println("Árvore não criada. Utilize a opção 2.");
                    } else {
                        System.out.print("Pré-ordem: ");
                        arvore.preOrder(arvore.getRoot());
                        System.out.println();
                        System.out.print("Em ordem: ");
                        arvore.inOrder(arvore.getRoot());
                        System.out.println();
                        System.out.print("Pós-ordem: ");
                        arvore.postOrder(arvore.getRoot());
                        System.out.println();
                    }
                    break;
                case 4:
                    if(arvore.getRoot() == null) {
                        System.out.println("Árvore não criada. Utilize a opção 2.");
                    } else {
                        float result = arvore.calcularExpressao();
                        System.out.println("Resultado da expressão: " + result);
                    }
                    break;
                case 5:
                    System.out.println("Encerrando o programa...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } while(option != 5);
        
        scanner.close();
    }
}
