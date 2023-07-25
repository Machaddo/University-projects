#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

bool ganhou(char m[3][3], char jogador) {
  int i, j;

  for (i = 0; i < 3; i++) {
    if (m[i][0] == jogador && m[i][1] == jogador && m[i][2] == jogador)
      return true;
  }

  for (j = 0; j < 3; j++) {
    if (m[0][j] == jogador && m[1][j] == jogador && m[2][j] == jogador)
      return true;
  }

  if (m[0][0] == jogador && m[1][1] == jogador && m[2][2] == jogador)
    return true;

  if (m[0][2] == jogador && m[1][1] == jogador && m[2][0] == jogador)
    return true;

  return false;
}

bool empatou(char m[3][3]) {
  int i, j;
  for (i = 0; i < 3; i++) {
    for (j = 0; j < 3; j++) {
      if (m[i][j] == '.')
        return false;
    }
  }
  return true;
}

int analisar_jogo(char m[3][3]) {
  if (ganhou(m, 'X')) return 1;
  else if (ganhou(m, 'O')) return 2;
  else if (empatou(m)) return 3;
  
  return 4;
}

bool jogada_valida(char m[3][3], char jogador, int i, int j) {
  if (i < 0 || i > 2) return false;
  
  if (j < 0 || j > 2) return false;
  
  if (m[i][j] != '.') return false;
  
  return true;
}

void fazer_minha_jogada(char m[3][3]) {
  int i, j;

  // tento ganhar em uma jogada
  for (i = 0; i < 3; i++) {
    for (j = 0; j < 3; j++) {
      if (m[i][j] == '.') {
        m[i][j] = 'X';
        if (analisar_jogo(m) == 1)
          return;
        m[i][j] = '.';
      }
    }
  }

  // tento impedir o adversário de ganhar
  for (i = 0; i < 3; i++) {
    for (j = 0; j < 3; j++) {
      if (m[i][j] == '.') {
        m[i][j] = 'O';
        if (analisar_jogo(m) == 2) {
          m[i][j] = 'X';
          return;
        }
        m[i][j] = '.';
      }
    }
  }

  // Busco realizar jogadas nos pontos extremos da matrix
  for (i = 0; i < 3; i++) {
    for (j = 0; j < 3; j++) {
      if (m[i][j] == '.') {
        if ((i == 0 && j == 0) || (i == 0 && j == 2)) {
          m[i][j] = 'X';
          return;
        } else if ((i == 2 && j == 0) || (i == 2 && j == 2)) {
          m[i][j] = 'X';
          return;
        }
      }
    }
  }

  // Preencho próximo campo vazio
  for (i = 0; i < 3; i++) {
    for (j = 0; j < 3; j++) {
      if (m[i][j] == '.') {
        m[i][j] = 'X';
        return;
      }
    }
  }
}

void imprimir(char m[3][3]) {
  int i;
  printf("\n");
  printf("    1 2 3\n");
  printf("    v v v\n\n");
  for (i = 0; i < 3; i++) {
    printf("%d > %c|%c|%c\n", i + 1, m[i][0], m[i][1], m[i][2]);
    if (i < 2)
      printf("    -----\n");
  }
  printf("\n");
}

void iniciar(char m[3][3]) {
  int i, j;
  for (i = 0; i < 3; i++) {
    for (j = 0; j < 3; j++) {
      m[i][j] = '.';
    }
  }
}

void exibir_resultado(int status){
  if (status == 1)
    printf("\n# Jogador X ganhou!\n\n");
  else if (status == 2)
    printf("\n# Jogador O ganhou!\n\n");
  else
    printf("\n# Empatou!\n\n");
}

bool mais_uma_rodada(){
  char resp;
  printf("Mais uma rodada ? (s/n): ");
  scanf(" %c", &resp);

  int value = strcmp(&resp, "s");

  if (value == 0) {
    system("clear");
    return true;
  }

  return false;
}

int main() {
  char velha[3][3] = {{'X', '.', 'O'}, {'X', 'O', '.'}, {'X', 'X', 'O'}};
  char vez = 'X';
  int status = 1;
  bool terminou = false;

  srand(time(NULL));
  if (rand() % 2 == 0) vez = 'O';

  iniciar(velha);
  imprimir(velha);

  while (!terminou) {
    if (vez == 'X') {
      printf("\n# Minha vez!\n");
      sleep(3);
      fazer_minha_jogada(velha);
    } else {
      int linha, coluna;
      bool valida = false;
      while (!valida) {
        printf("\n# É sua vez!\n");
        sleep(1);
        printf("# Linha da jogada: ");
        scanf("%d", &linha);
        printf("# Coluna da jogada: ");
        scanf("%d", &coluna);
        valida = jogada_valida(velha, vez, linha - 1, coluna - 1);
        if (!valida) {
          printf("# Jogada inválida!\n");
        }
      }
      velha[linha - 1][coluna - 1] = vez;
    }

    imprimir(velha);

    status = analisar_jogo(velha);
    terminou = (status != 4);

    if (vez == 'X') vez = 'O';
    else vez = 'X';
  }

  exibir_resultado(status);

  if (mais_uma_rodada()) return main();

  printf("\nAté mais!");
  
  return 0;
}
