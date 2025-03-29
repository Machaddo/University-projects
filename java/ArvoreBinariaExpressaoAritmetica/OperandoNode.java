class OperandoNode extends Node {

    public OperandoNode(String value) {
        super(value);
    }
    
    /**
     * Retorna o valor numérico do operando.
     */
    @Override
    public float visitar() {
        return Float.parseFloat(this.value);
    }
}