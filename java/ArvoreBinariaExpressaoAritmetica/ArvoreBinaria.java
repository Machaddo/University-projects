import java.util.Stack;

class ArvoreBinaria {
    
    private Node root;

    public ArvoreBinaria() {
        this.root = null;
    }
    
    public Node getRoot() {
        return root;
    }
    
    // Método para criar a árvore a partir da expressão em notação pós-fixa
    public void construirArvore(String postfix) {
        Stack<Node> stack = new Stack<>();
        String[] tokens = postfix.split("\\s+");
        
        for (String token : tokens) {
            if (token.isEmpty())
                continue;
            if (isOperator(token.charAt(0)) && token.length() == 1) {
                // Cria nó operador
                OperadorNode operador = new OperadorNode(token);
                operador.right = stack.pop();
                operador.left = stack.pop();
                stack.push(operador);
            } else {
                // Cria nó operando
                stack.push(new OperandoNode(token));
            }
        }
        root = stack.pop();
    }
    
    // Verifica se o caractere é um operador permitido
    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }
    
    // Métodos de percurso:
    public void preOrder(Node node) {
        if (node != null) {
            System.out.print(node.value + " ");
            preOrder(node.left);
            preOrder(node.right);
        }
    }
    
    public void inOrder(Node node) {
        if (node != null) {
            inOrder(node.left);
            System.out.print(node.value + " ");
            inOrder(node.right);
        }
    }
    
    public void postOrder(Node node) {
        if (node != null) {
            postOrder(node.left);
            postOrder(node.right);
            System.out.print(node.value + " ");
        }
    }
    
    // Percurso para calcular a expressão utilizando o método visitar()
    public float calcularExpressao() {
        if (root == null) return Float.NaN;
        return root.visitar();
    }
    
    // Método utilitário para converter uma expressão infixa em pós-fixa usando o algoritmo Shunting-yard
    public static String infixToPostfix(String infix) {
        StringBuilder postfix = new StringBuilder();
        Stack<Character> stack = new Stack<>();
        infix = infix.replaceAll("\\s+", "");
        
        for (int i = 0; i < infix.length(); i++) {
            char c = infix.charAt(i);
            if (Character.isDigit(c) || c == '.') {
                while (i < infix.length() && (Character.isDigit(infix.charAt(i)) || infix.charAt(i) == '.')) {
                    postfix.append(infix.charAt(i));
                    i++;
                }
                postfix.append(" ");
                i--; // Ajusta o índice
            } else if (c == '(') {
                stack.push(c);
            } else if (c == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    postfix.append(stack.pop()).append(" ");
                }
                if (!stack.isEmpty() && stack.peek() == '(') {
                    stack.pop();
                }
            } else if (isOperatorStatic(c)) {
                while (!stack.isEmpty() && isOperatorStatic(stack.peek()) &&
                        (precedence(c) <= precedence(stack.peek()))) {
                    postfix.append(stack.pop()).append(" ");
                }
                stack.push(c);
            }
        }
        while (!stack.isEmpty()) {
            postfix.append(stack.pop()).append(" ");
        }
        return postfix.toString().trim();
    }
    
    // Método auxiliar estático para verificar operadores
    private static boolean isOperatorStatic(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }
    
    // Retorna a precedência do operador
    private static int precedence(char c) {
        switch (c) {
            case '*': 
            case '/':
                return 2;
            case '+': 
            case '-':
                return 1;
        }
        return -1;
    }
    
    // Validação sintática básica da expressão infixa
    public static boolean isValidExpression(String expr) {
        if (expr == null) return false;
        expr = expr.replaceAll("\\s+", "");
        if (expr.isEmpty()) return false;
        
        // Verifica caracteres válidos
        for (char c : expr.toCharArray()) {
            if (!Character.isDigit(c) && c != '.' && c != '+' && c != '-' &&
                c != '*' && c != '/' && c != '(' && c != ')') {
                return false;
            }
        }
        
        // Verifica balanceamento de parênteses
        Stack<Character> parenStack = new Stack<>();
        for (char c : expr.toCharArray()){
            if(c == '(') {
                parenStack.push(c);
            } else if(c == ')'){
                if(parenStack.isEmpty()){
                    return false;
                }
                parenStack.pop();
            }
        }
        if(!parenStack.isEmpty()) return false;
        
        // Validação sintática simples: alternância entre operandos e operadores
        boolean expectingOperand = true;
        int i = 0;
        while(i < expr.length()){
            char c = expr.charAt(i);
            if (expectingOperand) {
                if(c == '(') {
                    i++;
                } else if (Character.isDigit(c) || c == '.') {
                    boolean dotFound = (c == '.');
                    i++;
                    while(i < expr.length() && (Character.isDigit(expr.charAt(i)) || expr.charAt(i) == '.')){
                        if(expr.charAt(i) == '.'){
                            if(dotFound) return false;
                            dotFound = true;
                        }
                        i++;
                    }
                    expectingOperand = false;
                } else {
                    return false;
                }
            } else {
                if (i < expr.length() && (expr.charAt(i)=='+' || expr.charAt(i)=='-' ||
                                            expr.charAt(i)=='*' || expr.charAt(i)=='/')) {
                    expectingOperand = true;
                    i++;
                } else if (i < expr.length() && expr.charAt(i)==')'){
                    i++;
                } else {
                    return false;
                }
            }
        }
        return !expectingOperand;
    }
}