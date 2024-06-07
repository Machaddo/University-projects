import java.util.Scanner;

class Main {
    private static DLinkedList list = new DLinkedList();
    private static String currentFile = null;
    private static boolean isModified = false;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String command = "";

        while (!command.equals(":q!")){
            System.out.print(":");
            command = scanner.nextLine().trim();
            processCommand(command);
        }

        scanner.close();
    }

    private static void processCommand(String command) {
        if (command.startsWith(":e ")) {
            currentFile = command.substring(3).trim();
            list = new DLinkedList();
            list.loadFromFile(currentFile);
        } else if (command.equals(":w")) {
            if (currentFile != null) {
                list.saveToFile(currentFile);
                isModified = false;
            } else {
                System.out.println("Nenhum arquivo aberto!");
            }
        } else if (command.startsWith(":w ")) {
            String filename = command.substring(3).trim();
            list.saveToFile(filename);
            currentFile = filename;
            isModified = false;
        } else if (command.equals(":q!")) {
            if (isModified) {
                System.out.print("Existem modificações não salvas. Deseja salvar? (s/n): ");
                Scanner scanner = new Scanner(System.in);
                String response = scanner.nextLine().trim().toLowerCase();
                if (response.equals("s")) {
                    if (currentFile != null) {
                        list.saveToFile(currentFile);
                    } else {
                        System.out.print("Nome do arquivo: ");
                        currentFile = scanner.nextLine().trim();
                        list.saveToFile(currentFile);
                    }
                }
            }
            System.out.println("Encerrando o editor.");
        } else if (command.startsWith(":v ")) {
            String[] parts = command.substring(3).trim().split(" ");
            int start = Integer.parseInt(parts[0]);
            int end = Integer.parseInt(parts[1]);
            list.mark(start, end);
        } else if (command.equals(":y")) {
            list.copy();
        } else if (command.equals(":c")) {
            System.out.println("Comando incompleto: :c [início] [fim]");
        } else if (command.startsWith(":c ")) {
            String[] parts = command.substring(3).trim().split(" ");
            int start = Integer.parseInt(parts[0]);
            int end = Integer.parseInt(parts[1]);
            list.cut(start, end);
            isModified = true;
        } else if (command.startsWith(":p ")) {
            int pos = Integer.parseInt(command.substring(3).trim());
            list.paste(pos);
            isModified = true;
        } else if (command.equals(":s")) {
            list.displayAll();
        } else if (command.startsWith(":s ")) {
            String[] parts = command.substring(3).trim().split(" ");
            int start = Integer.parseInt(parts[0]);
            int end = Integer.parseInt(parts[1]);
            list.display(start, end);
        } else if (command.startsWith(":x ")) {
            int line = Integer.parseInt(command.substring(3).trim());
            list.deleteLine(line);
            isModified = true;
        } else if (command.startsWith(":xG ")) {
            int line = Integer.parseInt(command.substring(4).trim());
            list.deleteRange(line, list.getSize());
            isModified = true;
        } else if (command.startsWith(":XG ")) {
            int line = Integer.parseInt(command.substring(4).trim());
            list.deleteRange(1, line);
            isModified = true;
        } else if (command.startsWith(":/ ")) {
            String[] parts = command.substring(3).trim().split(" ");
            if (parts.length == 1) {
                list.search(parts[0]);
            } else if (parts.length == 2) {
                list.searchAndReplace(parts[0], parts[1]);
                isModified = true;
            } else if (parts.length == 3) {
                int line = Integer.parseInt(parts[2]);
                list.searchAndReplaceInLine(parts[0], parts[1], line);
                isModified = true;
            }
        } else if (command.startsWith(":a ")) {
            int pos = Integer.parseInt(command.substring(3).trim());
            System.out.println("Inserindo linhas após a linha " + pos + ". Digite :a para terminar.");
            insertLinesAfter(pos);
            isModified = true;
        } else if (command.startsWith(":i ")) {
            int pos = Integer.parseInt(command.substring(3).trim());
            System.out.println("Inserindo linhas antes da linha " + pos + ". Digite :i para terminar.");
            insertLinesBefore(pos);
            isModified = true;
        } else if (command.equals(":help")) {
            list.help();
        } else {
            System.out.println("Comando desconhecido. Digite :help para ver a lista de comandos.");
        }
    }

    private static void insertLinesAfter(int pos) {
        Scanner scanner = new Scanner(System.in);
        String line;
        do {
            line = scanner.nextLine();
            if (!line.equals(":a")) {
                list.insertAfter(pos++, line);
            }
        } while (!line.equals(":a"));
    }

    private static void insertLinesBefore(int pos) {
        Scanner scanner = new Scanner(System.in);
        String line;
        do {
            line = scanner.nextLine();
            if (!line.equals(":i")) {
                list.insertBefore(pos++, line);
            }
        } while (!line.equals(":i"));
    }
}
