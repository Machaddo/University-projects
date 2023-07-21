#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <stdbool.h>

#define palavra_max 1000
#define palavra_tam 100

int busca_binaria(char dicionario[palavra_max][palavra_tam], int tam, char *palavra) {
  bool repetir = true;
  int inicio = 0, meio, fim = tam - 1;
  
  while (repetir) {
    meio = (inicio + fim)/2;
    int comparar = strcasecmp(dicionario[meio], palavra);
    if(comparar == 0) return meio; 
    else if(comparar < 0) inicio = meio + 1;
    else fim = meio - 1;
    
    repetir = (inicio <= fim);
  }
  
  return -1;
}

void inserir_palavra(char dicionario[palavra_max][palavra_tam], int *tam, char *palavra) {
    int busca = busca_binaria(dicionario, *tam, palavra);
    if (busca == -1) {
        int insertIndex;
        for (insertIndex = 0; insertIndex < *tam; insertIndex++) {
            if (strcasecmp(dicionario[insertIndex], palavra) > 0)
                break;
        }
        for (int i = *tam; i > insertIndex; i--)
            strcpy(dicionario[i], dicionario[i - 1]);
        strcpy(dicionario[insertIndex], palavra);
        (*tam)++;
    }
}

int main() {
    char dicionario[palavra_max][palavra_tam];
    int tam = 0;

    FILE *arquivo = fopen("file.txt", "r");
    if (arquivo == NULL) {
        printf("Erro ao abrir o arquivo.\n");
        return 1;
    }

    char palavra[palavra_tam];
    while (fscanf(arquivo, "%s", palavra) == 1) {
        for (int i = 0; palavra[i]; i++)
            palavra[i] = tolower(palavra[i]);

        inserir_palavra(dicionario, &tam, palavra);
    }

    fclose(arquivo);

    for (int i = 0; i < tam; i++)
        printf("%s\n", dicionario[i]);

    printf("\ntotal de palavras diferentes no dicionario = %d\n", tam);

    return 0;
}
