import java.io.*;
import java.util.ArrayList;
import java.util.List;

class DLinkedList {
    private Node head;
    private int size;
    private List<String> clipboard;

    public DLinkedList() {
        this.head = null;
        this.size = 0;
        this.clipboard = new ArrayList<>();
    }

    public int getSize(){
        return size;
    }

    public void addLine(String line) {
        Node newNode = new Node(line);
        if (head == null) {
            head = newNode;
        } else {
            Node tail = head.prev;
            tail.next = newNode;
            newNode.prev = tail;
            newNode.next = head;
            head.prev = newNode;
        }
        size++;
    }

    public void insertAfter(int pos, String line) {
        if (pos < 1 || pos > size) {
            System.out.println("Posição inválida!");
            return;
        }
        Node current = getNodeAt(pos);
        Node newNode = new Node(line);
        Node nextNode = current.next;
        current.next = newNode;
        newNode.prev = current;
        newNode.next = nextNode;
        nextNode.prev = newNode;
        size++;
    }

    public void insertBefore(int pos, String line) {
        if (pos < 1 || pos > size) {
            System.out.println("Posição inválida!");
            return;
        }
        Node current = getNodeAt(pos);
        Node newNode = new Node(line);
        Node prevNode = current.prev;
        prevNode.next = newNode;
        newNode.prev = prevNode;
        newNode.next = current;
        current.prev = newNode;
        if (current == head) {
            head = newNode;
        }
        size++;
    }

    public void deleteLine(int pos) {
        if (pos < 1 || pos > size) {
            System.out.println("Posição inválida!");
            return;
        }
        Node current = getNodeAt(pos);
        Node prevNode = current.prev;
        Node nextNode = current.next;
        prevNode.next = nextNode;
        nextNode.prev = prevNode;
        if (current == head) {
            head = nextNode;
        }
        size--;
    }

    public void deleteRange(int start, int end) {
        if (start < 1 || end > size || start > end) {
            System.out.println("Intervalo inválido!");
            return;
        }
        for (int i = start; i <= end; i++) {
            deleteLine(start);
        }
    }

    public void display(int start, int end) {
        if (start < 1 || end > size || start > end) {
            System.out.println("Intervalo inválido!");
            return;
        }
        Node current = getNodeAt(start);
        for (int i = start; i <= end; i++) {
            System.out.println(i + ": " + current.line);
            current = current.next;
        }
    }

    public void displayAll() {
        if (size == 0) {
            System.out.println("Lista vazia!");
            return;
        }
        display(1, size);
    }

    private Node getNodeAt(int pos) {
        Node current = head;
        for (int i = 1; i < pos; i++) {
            current = current.next;
        }
        return current;
    }

    public void saveToFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            Node current = head;
            for (int i = 0; i < size; i++) {
                writer.write(current.line);
                writer.newLine();
                current = current.next;
            }
            System.out.println("Arquivo '" + filename + "' salvo com sucesso!");
        } catch (IOException e) {
            System.out.println("Erro ao salvar o arquivo: " + e.getMessage());
        }
    }

    public void loadFromFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                addLine(line);
            }
            System.out.println("Arquivo '" + filename + "' lido com sucesso!");
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
        }
    }

    public void mark(int start, int end) {
        if (start < 1 || end > size || start > end) {
            System.out.println("Intervalo inválido!");
            return;
        }
        clipboard.clear();
        Node current = getNodeAt(start);
        for (int i = start; i <= end; i++) {
            clipboard.add(current.line);
            current = current.next;
        }
        System.out.println("Intervalo de linhas " + start + "-" + end + " marcado para cópia ou recorte.");
    }

    public void copy() {
        System.out.println("Texto copiado para a área de transferência.");
    }

    public void cut(int start, int end) {
        mark(start, end);
        deleteRange(start, end);
        System.out.println("Texto recortado para a área de transferência.");
    }

    public void paste(int pos) {
        if (pos < 1 || pos > size) {
            System.out.println("Posição inválida!");
            return;
        }
        Node current = getNodeAt(pos);
        for (String line : clipboard) {
            Node newNode = new Node(line);
            Node nextNode = current.next;
            current.next = newNode;
            newNode.prev = current;
            newNode.next = nextNode;
            nextNode.prev = newNode;
            current = newNode;
            size++;
        }
        System.out.println("Texto colado a partir da linha " + pos + ".");
    }

    public void searchAndReplace(String search, String replace) {
        Node current = head;
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (current.line.contains(search)) {
                current.line = current.line.replace(search, replace);
                count++;
            }
            current = current.next;
        }
        System.out.println("'" + search + "' substituído por '" + replace + "' em " + count + " linhas.");
    }

    public void searchAndReplaceInLine(String search, String replace, int line) {
        if (line < 1 || line > size) {
            System.out.println("Linha inválida!");
            return;
        }
        Node current = getNodeAt(line);
        if (current.line.contains(search)) {
            current.line = current.line.replace(search, replace);
            System.out.println("'" + search + "' substituído por '" + replace + "' na linha " + line + ".");
        } else {
            System.out.println("Nenhuma ocorrência de '" + search + "' na linha " + line + ".");
        }
    }

    public void search(String search) {
        Node current = head;
        boolean found = false;
        for (int i = 0; i < size; i++) {
            if (current.line.contains(search)) {
                System.out.println((i + 1) + ": " + current.line);
                found = true;
            }
            current = current.next;
        }
        if (!found) {
            System.out.println("Nenhuma ocorrência de '" + search + "' encontrada.");
        }
    }

    public void help() {
        System.out.println("Operações permitidas no editor:");
        System.out.println(":e NomeArq.ext - Abrir o arquivo de nome NomeArq.ext e armazenar cada linha em um nó da lista.");
        System.out.println(":w - Salvar a lista no arquivo atualmente aberto.");
        System.out.println(":w NomeArq.ext - Salvar a lista no arquivo de nome NomeArq.ext.");
        System.out.println(":q! - Encerrar o editor. Caso existam modificações não salvas na lista, o programa deve solicitar confirmação se a pessoa usuária do editor deseja salvar as alterações em arquivo antes de encerrar o editor.");
        System.out.println(":v LinIni LinFim - Marcar um texto da lista (para cópia ou recorte – “área de transferência”) da LinIni até LinFim. Deve ser verificado se o intervalo [LinIni, LinFim] é válido.");
        System.out.println(":y - Copiar o texto marcado (ver comando anterior) para uma lista usada como área de transferência.");
        System.out.println(":c - Recortar o texto marcado para a lista de área de transferência.");
        System.out.println(":p LinIniColar - Colar o conteúdo da área de transferência na lista, a partir da linha indicada em LinIniColar. Deve ser verificado se LinIniColar é válido.");
        System.out.println(":s - Exibir em tela o conteúdo completo do código-fonte que consta na lista, de 20 em 20 linhas.");
        System.out.println(":s LinIni LinFim - Exibir na tela o conteúdo do código-fonte que consta na lista, da linha inicial LinIni até a linha final LinFim, de 20 em 20 linhas.");
        System.out.println(":x Lin - Apagar a linha de posição Lin da lista.");
        System.out.println(":xG Lin - Apagar o conteúdo a partir da linha Lin até o final da lista.");
        System.out.println(":XG Lin - Apagar o conteúdo da linha Lin até o início da lista.");
        System.out.println(":/ Elemento - Percorrer a lista, localizar as linhas que contém Elemento e exibir o conteúdo das linhas por completo.");
        System.out.println(":/ Elem ElemTroca - Percorrer a lista e realizar a troca de Elem por ElemTroca em todas as linhas do código-fonte.");
        System.out.println(":/ Elem ElemTroca Linha - Realizar a troca de Elem por ElemTroca na linha Linha do código-fonte.");
        System.out.println(":a PosLin - Permitir a inserção de uma ou mais linhas e inserir na lista depois da posição PosLin. O término da entrada do novo conteúdo é dado por um :a em uma linha vazia.");
        System.out.println(":i PosLin - Permitir a inserção de uma ou mais linhas e inserir na lista antes da posição PosLin. O término da entrada do novo conteúdo é dado por um :i em uma linha vazia.");
        System.out.println(":help - Apresentar na tela todas as operações permitidas no editor.");
    }
}
