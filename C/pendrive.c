#include <stdio.h>
#include <stdlib.h>
#include <math.h>

#define MAX_FILES 50

void read_input_file(const char *filename, int *num_tests, int test_cases[MAX_FILES][MAX_FILES + 2]) {
    FILE *file = fopen(filename, "r");
    if (!file) {
        printf("Combinação não realizada. Arquivo de entrada não existe.\n");
        printf("Utilizar comando:\n./backup <ARQUIVO_ENTRADA> <ARQUIVO_SAÍDA>\n");
        exit(1);
    }
    
    fscanf(file, "%d", num_tests);
    for (int i = 0; i < *num_tests; i++) {
        fscanf(file, "%d %d", &test_cases[i][0], &test_cases[i][1]);
        for (int j = 0; j < test_cases[i][1]; j++) {
            fscanf(file, "%d", &test_cases[i][j + 2]);
        }
    }
    fclose(file);
}

int find_partition(int *files, int n, int target, int *set_a, int *set_b, int *size_a, int *size_b) {
    int total_sum = 0;
    for (int i = 0; i < n; i++) total_sum += files[i];
    if (total_sum > 2 * target) return 0;
    
    for (int i = 0; i < n; i++) {
        int sum_a = 0, sum_b = 0, idx_a = 0, idx_b = 0;
        for (int j = 0; j < n; j++) {
            if ((sum_a + files[j]) < target) {
                set_a[idx_a++] = files[j];
                sum_a += files[j];
            } else {
                set_b[idx_b++] = files[j];
                sum_b += files[j];
            }
        }
        if (sum_a < target && sum_b < target) {
            *size_a = idx_a;
            *size_b = idx_b;
            return 1;
        }
    }
    return 0;
}

void write_output_file(const char *filename, int num_tests, int results[MAX_FILES][MAX_FILES + 2]) {
    FILE *file = fopen(filename, "w");
    for (int i = 0; i < num_tests; i++) {
        if (results[i][0] == -1) {
            fprintf(file, "%d GB\nImpossível gravar todos os arquivos nos pendrives.\n", results[i][1]);
        } else {
            fprintf(file, "%d GB\nPendrive A (%d GB)\n", results[i][0], results[i][0] / 2);
            for (int j = 2; j < results[i][1] + 2; j++) {
                fprintf(file, "%d GB\n", results[i][j]);
            }
            fprintf(file, "Pendrive B (%d GB)\n", results[i][0] / 2);
            for (int j = results[i][1] + 2; j < results[i][1] + results[i][2] + 2; j++) {
                fprintf(file, "%d GB\n", results[i][j]);
            }
        }
    }
    fclose(file);
}

int main(int argc, char *argv[]) {
    if (argc != 3) {
        printf("Combinação não realizada. Parâmetros incorretos.\n");
        printf("Utilizar comando:\n./backup <ARQUIVO_ENTRADA> <ARQUIVO_SAÍDA>\n");
        return 1;
    }
    
    int num_tests, test_cases[MAX_FILES][MAX_FILES + 2];
    int results[MAX_FILES][MAX_FILES + 2];
    read_input_file(argv[1], &num_tests, test_cases);
    
    for (int i = 0; i < num_tests; i++) {
        int L = test_cases[i][0];
        int T = test_cases[i][1];
        int pendrive_size = L / 2;
        int set_a[MAX_FILES], set_b[MAX_FILES], size_a = 0, size_b = 0;
        
        if (find_partition(&test_cases[i][2], T, pendrive_size, set_a, set_b, &size_a, &size_b)) {
            results[i][0] = L;
            results[i][1] = size_a;
            results[i][2] = size_b;
            for (int j = 0; j < size_a; j++) results[i][j + 2] = set_a[j];
            for (int j = 0; j < size_b; j++) results[i][j + 2 + size_a] = set_b[j];
        } else {
            results[i][0] = -1;
            results[i][1] = L;
        }
    }
    
    write_output_file(argv[2], num_tests, results);
    printf("Combinação realizada no arquivo %s.\n", argv[2]);
    return 0;
}