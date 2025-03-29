class Node {
    protected String value;
    protected Node left;
    protected Node right;
    
    public Node(String value) {
        this.value = value;
        this.left = null;
        this.right = null;
    }
    
    /**
     * Método a ser chamado quando o nó é visitado.
     * Na classe base, retorna Float.NaN.
     */
    public float visitar() {
        return Float.NaN;
    }
}