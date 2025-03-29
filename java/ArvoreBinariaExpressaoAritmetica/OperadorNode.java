class OperadorNode extends Node {

    public OperadorNode(String value) {
        super(value);
    }
    
    /**
     * Retorna o resultado da operação indicada pelo operador.
     * Supõe que os nós esquerdo e direito já foram criados.
     */
    @Override
    public float visitar() {
        float leftVal = (left != null) ? left.visitar() : 0;
        float rightVal = (right != null) ? right.visitar() : 0;
        switch (this.value) {
            case "+":
                return leftVal + rightVal;
            case "-":
                return leftVal - rightVal;
            case "*":
                return leftVal * rightVal;
            case "/":
                if(rightVal == 0) {
                    System.out.println("Erro: Divisão por zero.");
                    return Float.NaN;
                }
                return leftVal / rightVal;
            default:
                return Float.NaN;
        }
    }
}