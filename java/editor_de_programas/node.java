class Node {
    String line;
    Node next;
    Node prev;

    public Node(String line) {
        this.line = line;
        this.next = this;
        this.prev = this;
    }
}
