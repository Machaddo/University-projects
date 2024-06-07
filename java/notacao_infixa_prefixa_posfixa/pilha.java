public class Pilha {
    private char[] elementos;
    private int topo;
    private int capacidade;

    public Pilha(int tamanho) {
        capacidade = tamanho;
        elementos = new char[capacidade];
        topo = -1;
    }

    public void push(char elemento) {
        if (topo == capacidade - 1) {
            System.out.println("Erro: Pilha cheia");
        } else {
            topo++;
            elementos[topo] = elemento;
        }
    }

    public char pop() {
        if (topo == -1) {
            System.out.println("Erro: Pilha vazia");
            return '\0';
        } else {
            char elemento = elementos[topo];
            topo--;
            return elemento;
        }
    }

    public char top() {
        if (topo == -1) {
            System.out.println("Erro: Pilha vazia");
            return '\0';
        } else {
            return elementos[topo];
        }
    }

    public boolean IsEmpty() {
        return (topo == -1);
    }
}
