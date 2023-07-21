'''
Sistemas Coordenadas 2D
'''

import math
import os

pontoProx = 0
pontoDistante = 0
distPontoProx = 0
distPontoDistante = 0

def verificarDistanciaEntreOsPontos(distancia, x2, y2):
    global pontoProx
    global pontoDistante
    global distPontoProx
    global distPontoDistante
    
    if distPontoDistante == 0 and distPontoProx == 0:
        distPontoDistante = distancia
        distPontoProx = distancia
        pontoProx = x2, y2
        pontoDistante = x2, y2
        
    elif distancia > distPontoDistante:
        distPontoDistante = distancia
        pontoDistante = x2, y2
        
    elif distancia < distPontoProx:
        distPontoProx = distancia
        pontoProx = x2, y2

def calculaDistancia(x1, y1, x2, y2):
    a = x2 - x1
    b = y2 - y1
    distancia = math.sqrt(math.pow(a, 2) + math.pow(b, 2))
    
    verificarDistanciaEntreOsPontos(distancia, x2, y2)

def sistemasCoordenadas():
    eixoX = int(input("\nDigite a abcissa X: "))
    eixoY = int(input("Digite a ordenada Y: "))
    
    qtdPontos = int(input("\nInforme a quantidade de pontos que deseja visualizar: "))
    
    while qtdPontos <= 0:
        qtdPontos = int(input("\nQuantidade inválida. Digite um número maior que zero: "))
    
    for i in range(qtdPontos):
        pontoX = int(input("\nDigite o ponto X: "))
        pontoY = int(input("Digite o ponto Y: "))
    
        if pontoX > eixoX and pontoY > eixoY:
            print("Ponto (", pontoX, ",", pontoY,")", " está no 1º quadrante")
        elif pontoX < eixoX and pontoY > eixoY:
            print("Ponto (", pontoX, ",", pontoY,")", " está no 2º quadrante")
        elif pontoX < eixoX and pontoY < eixoY:
            print("Ponto (", pontoX, ",", pontoY,")", " está no 3º quadrante")
        elif pontoX > eixoX and pontoY < eixoY:
            print("Ponto (", pontoX, ",", pontoY,")", " está no 4º quadrante")
        else:
            print("Ponto (", pontoX, ",", pontoY, ")", " está no eixo coordenado.")
        
        calculaDistancia(eixoX, eixoY, pontoX, pontoY)
    
    print("\nPonto",pontoProx, "eh o mais próximo, distancia = %.2f" % distPontoProx)
    print("Ponto",pontoDistante, "eh o mais distante, distancia = %.2f" % distPontoDistante)
    
print("Programa iniciado...")    

n = 1
while n == 1:
    sistemasCoordenadas()
    n = int(input("\nDeseja visualizar outros valores? Se sim digite 1 | Se não digite 0: "))
    
    if n == 1:
        distPontoProx = 0
        distPontoDistante = 0

        os.system('cls' if os.name == 'nt' else 'clear')

print("\nPrograma finalizado, até mais!")
input()
