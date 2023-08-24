#include <ctype.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <unistd.h>

char *palavraSorteada;

int gerar_numero_aleatorio() {
  srand(time(NULL));
  int nmrAleatorio = (rand() % 6621 + 1);

  return nmrAleatorio;
}

char *sortear_palavra(FILE *sem_acentos, int nmrAleatorio, char *termo) {
  int contador = 0;
  while (!feof(sem_acentos)) {
    if (NULL != fgets(termo, 10, sem_acentos)) {
      if (contador < nmrAleatorio) {
        contador++;

        if (contador == nmrAleatorio) {
          if (strlen(termo) == 6) {
            break;
          } else {
            nmrAleatorio++;
          }
        }
      }
    }
  }

  return termo;
}

bool verificar_se_usuario_acertou_palavra(char *palavraUsuario) {
  char copia_Sorteada[5];

  int certo = 0;
  for (int r = 0; r < 5; r++) {
    copia_Sorteada[r] = palavraSorteada[r];   
  }

  for (int r = 0; r < 5; r++) {
    if(copia_Sorteada[r] != palavraUsuario[r]){
      certo++;
    }
  }

  if (certo == 0) {
    return true;
  } 
  else {
    printf("\n*-----------*\n|           |\n| ");
    
    for (int r = 0; r < 5; r++) {
      printf("%c ", palavraUsuario[r]);
    }
  
    printf("|\n|");

    int x = 0;
    for (int i = 0; i < 5; i++) {
      for (int j = 5; j >= 0; j--) {
        if (palavraUsuario[i] == copia_Sorteada[j]) {
          if (palavraUsuario[i] == palavraSorteada[i]) {
            printf(" *");
            x++;
            copia_Sorteada[i] = '*';
            break;
          } else {
            printf(" !");
            x++;
            copia_Sorteada[j] = '*';
            break;
          }
        }
      }
      if (x == 0) {
        printf(" x");
      }
      x = 0;
    }
    printf(" |\n|           |\n*-----------*");
    return false;
  }
}

bool verificar_se_palavra_existe(FILE *sem_acentos, char *palavraUsuario) {
  FILE *sem_acentos2;
  sem_acentos2 = fopen("sem_acentos.txt", "r");

  int contador = 0;
  char palavra[10];

  while (fgets(palavra, sizeof(palavra), sem_acentos2) != NULL) {
    if (strstr(palavra, palavraUsuario) != NULL) {
      return true;
    }
  }

  return false;
}

bool validar_palavra_digitada(FILE *sem_acentos, char *palavraUsuario) {
  if (strlen(palavraUsuario) != 5)
    return false;

  return verificar_se_palavra_existe(sem_acentos, palavraUsuario);
}

void jogador_acertou_palavra(int tentativas, float tempo) {
  FILE *arquivo;
  char nome[20];

  printf("\n\nParabéns! Você acertou!\n");
  printf("\nDigite seu nome: ");
  scanf("%s", nome);

  arquivo = fopen("scores.txt", "w");

  if (arquivo == NULL) {
    printf("Ocorreu um erro ao gravar os dados no arquivo. :( \n)");
  } else {
    fprintf(arquivo, "Nome do jogador: %s\n", nome);
    fprintf(arquivo, "Palavra sorteada: %s\n", palavraSorteada);
    fprintf(arquivo, "Número de tentativas: %d\n", tentativas);
    fprintf(arquivo, "Tempo total até acertar: %f\n", tempo);
  }

  fclose(arquivo);

  printf("\nScore salvo com sucesso!");
}

void exibir_palavra_sorteada() {
  printf("\nVocê excedeu o número de tentativas :( \n");
  printf("A palavra sorteada era: %s", palavraSorteada);
  return;
}

void iniciar_jogo(FILE *sem_acentos) {
  double tempo = 0.0;
  clock_t begin = clock();

  printf("\nWordle\n");

  int tentativas = 1;
  while (tentativas <= 6) {
    printf("\nDigite uma palavra com cinco letras: ");
    char palavraUsuario[10];
    scanf("%s", palavraUsuario);

    bool palavraValida = validar_palavra_digitada(sem_acentos, palavraUsuario);

    if (palavraValida) {
      bool acertou = verificar_se_usuario_acertou_palavra(palavraUsuario);

      if (acertou) {
        clock_t end = clock();

        tempo += (double)(end - begin) / CLOCKS_PER_SEC;

        jogador_acertou_palavra(tentativas, tempo);

        break;
      } else {
        printf("\n\n------ Número de tentativas: %d restantes\n",
               6 - tentativas);
        tentativas++;
      }
    } else {
      printf("\nPalavra inválida.\n");
    }
  }

  if (tentativas > 6)
    exibir_palavra_sorteada();
}

bool verificar_se_usuario_deseja_encerrar_wordle() {
  printf("\n\nDigite 's' para sortear uma nova palavra. Caso contrário digite "
         "'n' para encerrar o jogo (s/n): ");
  char resp[1];
  scanf("%s", resp);

  if (strcmp(resp, "s") == 0)
    return true;

  return false;
}

void programa_finalizado() {
  printf("\n\nFinalizando wordle...\n\n");

  sleep(2);

  printf("Até mais!\n\n\n");

  sleep(2);
}

int main() {
  bool executar = true;
  char termo[10];
  FILE *sem_acentos;

  while (executar) {
    sem_acentos = fopen("sem_acentos.txt", "r");

    int nmrAleatorio = gerar_numero_aleatorio();

    palavraSorteada = sortear_palavra(sem_acentos, nmrAleatorio, termo);

    iniciar_jogo(sem_acentos);

    fclose(sem_acentos);

    executar = verificar_se_usuario_deseja_encerrar_wordle();
  }

  programa_finalizado();

  return 0;
}
