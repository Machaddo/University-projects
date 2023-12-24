import os
import time
from operator import itemgetter

# Variáveis Globais
candidatos = []
presidentes = []
governadores = []
prefeitos = []
eleitores = []
votosEmBranco = [["Prefeito", 0],["Governador", 0],["Presidente", 0]]
votosNulos = [["Prefeito", 0],["Governador", 0],["Presidente", 0]]
votosTT = 0

# Helpers
newLine = "\n"
tabMenu = "-" * 20
tabHead = ">" * 20
tabBody = " " * 21

# Mensagens
errorMessage = "Atenção!"

menu = ["1. Cadastrar Candidatos", "2. Cadastrar Eleitores", "3. Votar", "4. Apurar Resultados", "5. Relatório e Estatísticas", "6. Encerrar"]

def menuPrincipal():
    os.system('cls' if os.name == 'nt' else 'clear')

    print(newLine + tabMenu + " MENU - SIMULADOR DO SISTEMA DE VOTAÇÃO " + tabMenu + newLine)

    for item in menu:
        print(" " * 30 + item)

    opMenu = int(input(newLine + " " * 30 + "Digite uma das opções: "))

    while opMenu < 1 or opMenu > 6:
        opMenu = int(input(newLine + " " * 30 + "Opção inválida. Digite uma das opções do menu: "))

    os.system('cls' if os.name == 'nt' else 'clear')

    if opMenu == 1:
        cadastrarCandidatos()
    elif opMenu == 2:
        cadastrarEleitores()
    elif opMenu == 3:
        votacao()
    elif opMenu == 4:
        apuracao()
    elif opMenu == 5:
        relatorios_e_estatisticas()
    else:
        encerrar_aplicacao()

def cadastrarCandidatos():
    print(newLine + tabHead + " Cadastrar candidatos")

    resp = "s"
    while resp == "s":
        nome = input(newLine + tabBody + "Nome: ")
        numero = int(input(tabBody + "Número: "))
        partido = input(tabBody + "Partido: ").lower()
        cargo = input(tabBody + "Cargo: ").lower()

        cadValido = validar_cadastro_candidato(numero, partido, cargo)

        if cadValido:
            gravar_candidato(nome, numero, partido, cargo)
            resp = input(newLine + tabBody + "Deseja cadastrar outro candidato ? (s/n): ")
        else:
            print(tabBody + "Tente novamente: ")
    
    menuPrincipal()
            
def cadastrarEleitores():
    print(newLine + tabHead + " Cadastrar Eleitores")

    resp = "s"
    while resp == "s":
        nome = input(newLine + tabBody + "Nome: ")
        cpf = int(input(tabBody + "CPF: "))

        cadValido = validar_cadastro_eleitor(cpf)

        if cadValido:
            gravar_eleitor(nome, cpf)
            resp = input(newLine + tabBody + "Deseja cadastrar outro candidato ? (s/n): ")
        else:
            print(tabBody + "Tente novamente: ")

    menuPrincipal()

def votacao():
    global candidatos
    global eleitores

    print(newLine + tabHead + " Votação")

    if len(candidatos) < 1:
        print(newLine + tabBody + errorMessage + " É ncessário cadastrar ao menos um candidato para realizar a votação.")
        return cadastrarCandidatos()
    elif len(eleitores) < 1:
        print(newLine + tabBody + errorMessage + " É ncessário cadastrar ao menos um eleitor para realizar a votação.")
        return cadastrarEleitores()

    cpf = int(input(newLine + tabBody + "Digite o CPF do eleitor: "))

    cpfValido = validar_cpf_eleitor_votacao(cpf)

    while not cpfValido:
        cpf = int(input(newLine + tabBody + "O CPF informado é inválido. Tente novamente: "))
        cpfValido = validar_cpf_eleitor_votacao(cpf)

    os.system('cls' if os.name == 'nt' else 'clear')

    print(newLine + tabBody + " Obrigado pelo seu voto!")
    time.sleep(2)

    menuPrincipal()

def apuracao():
    global prefeitos
    global governadores
    global presidentes

    print(newLine + tabHead + " Apuração")

    if len(prefeitos) < 1 or len(governadores) < 1 or len(presidentes) < 1:
        print(newLine + tabBody + errorMessage + "É necessário cadastrar ao menos um candidato por cargo para realizar a apuração dos votos.")
        time.sleep(2)
        menuPrincipal()

    cont = 0
    while cont < 3:
        if cont == 0:
            realizar_apuraracao_votos(prefeitos, "Prefeito")
        elif cont == 1:
            realizar_apuraracao_votos(governadores, "Governador")
        else:
            realizar_apuraracao_votos(presidentes, "Presidente")
        
        cont = cont + 1
    
    resp = "n"
    while resp != "s":
        resp = input(newLine + tabBody + "Deseja voltar ao menu principal ? (se sim digite -> s): ").lower()

        if resp != "s":
            print(newLine + tabBody + "Desculpe, não entendi.")
    
    menuPrincipal()

def relatorios_e_estatisticas():
    global eleitores
    global prefeitos
    global governadores
    global presidentes
    global candidatos
    global votosTT

    print(newLine + tabHead + " Relatório e Estatísticas")

    # Eleitores que votaram
    print(newLine + tabBody + "Eleitores que realizaram a votação:" + newLine)
    for i in range(len(eleitores)):
        if eleitores[i][2] == True:
            print(tabBody, i+1, "- " + eleitores[i][0])

    # Total de eleitores é igual ao total de votos 
    cont = 0
    for i in range(len(eleitores)):
        if eleitores[i][2] == True:
            cont = cont + 1
    
    if cont == votosTT:
        print(newLine + newLine + tabBody + "Auditoria:")
        print(tabBody + "Total de votos:", votosTT, " " * 3, "Total de eleitores que votaram:", cont)

    # Partido que elegeu mais politicos e o partido que elegeu menos politicos
    partidos = []

    for i in range(len(prefeitos)):
        if len(partidos) > 0:
            for j in range(len(partidos)):
                if prefeitos[i][2] == partidos[j][0]:
                    partidos[j][1] += 1
                    break
            partidos.append([prefeitos[i][2], prefeitos[i][4]])
        else:
            partidos.append([prefeitos[i][2], prefeitos[i][4]])

    for i in range(len(governadores)):
        for j in range(len(partidos)):
            if governadores[i][2] == partidos[j][0]:
                partidos[j][1] += 1
                break
        partidos.append([governadores[i][2], governadores[i][4]])

    for i in range(len(presidentes)):
        for j in range(len(partidos)):
            if presidentes[i][2] == partidos[j][0]:
                partidos[j][1] += 1
                break
        partidos.append([presidentes[i][2], presidentes[i][4]])

    print(partidos)

    menuPrincipal()

def encerrar_aplicacao():
    print(newLine + tabHead + " Finalizando programa...")
    time.sleep(2)
    print(newLine + tabBody + "Programa finalizado com sucesso. Até mais!")
    time.sleep(3)

#region Funções de validação
def validar_cadastro_candidato(numero, partido, cargo):
    global candidatos

    cargos = ["presidente", "governador", "prefeito"]
    if not cargos.__contains__(cargo):
        os.system('cls' if os.name == 'nt' else 'clear')

        print(newLine + tabBody + errorMessage + " O cargo informado não existe." + newLine)

        return False

    if len(candidatos) > 0:
        for i in range(len(candidatos)):
            
            if (candidatos[i][1] == numero and candidatos[i][2] != partido) or (candidatos[i][1] != numero and candidatos[i][2] == partido):
                os.system('cls' if os.name == 'nt' else 'clear')

                print(newLine + tabBody + errorMessage + " O número e o partido informado não coincidem." + newLine)

                return False
                
            elif candidatos[i][1] == numero and candidatos[i][3] == cargo:
                os.system('cls' if os.name == 'nt' else 'clear')

                print(newLine + tabBody + errorMessage + " Já existe um candidato deste partido concorrendo ao cargo de " + cargo + "." + newLine)

                return False

    return True

def validar_cadastro_eleitor(cpf):
    global eleitores

    if len(str(cpf)) != 11:
        print(newLine + tabBody + errorMessage + " Informe um CPF válido." + newLine)
        return False

    if len(eleitores) > 0:
        for i in range(len(eleitores)):
            if eleitores[i][1] == cpf:
                os.system('cls' if os.name == 'nt' else 'clear')

                print(newLine + tabBody + errorMessage + " O CPF informado já está cadastrado." + newLine)

                return False
    
    return True

def validar_cpf_eleitor_votacao(cpf):
    global eleitores

    votoValido = False

    for i in range(len(eleitores)):
        if cpf == eleitores[i][1] and eleitores[i][2] == False:
            votoValido = validar_votacao()

            if votoValido:
                gravar_voto_eleitor(cpf, votoValido)

            break

    return votoValido

def validar_votacao():
    global prefeitos
    global governadores
    global presidentes

    cargos = ["Prefeito","Governador","Presidente"]

    for i in range(len(cargos)):
        os.system('cls' if os.name == 'nt' else 'clear')
        print(newLine + tabHead + " Voto para " + cargos[i])
        print(newLine + tabBody + "Para votar em branco digite > 0" + newLine + tabBody + "Para votar nulo digite > -1")

        votoFeito = False
        while not votoFeito:
            numeroCand = int(input(newLine + tabBody + "Insira o número do candidato: "))
            
            while numeroCand <= 0 and numeroCand != -1 and numeroCand != 0:
                numeroCand = int(input(newLine + tabBody + "O número inserido é inválido, digite novamente: "))
            
            if numeroCand > 0:
                if cargos[i] == "Prefeito":
                    votoFeito = gravar_voto(prefeitos, numeroCand)
                elif cargos[i] == "Governador":
                    votoFeito = gravar_voto(governadores, numeroCand)
                else:
                    votoFeito = gravar_voto(presidentes, numeroCand)
            else:
                if numeroCand == 0:
                    votoFeito = gravar_voto_em_branco(cargos[i])
                else:
                    votoFeito = gravar_voto_nulo(cargos[i])

            if not votoFeito:
                print(newLine + tabBody + "Tente novamente:")
        
    return votoFeito

def validar_voto_confirmado():
    resp = input(newLine + tabBody + "Deseja confirmar o voto ? (s/n): ").lower()

    if resp == "s":
        return True
    else:
        return False
#endregion

#region Funções de gravação
def gravar_candidato(nome, numero, partido, cargo):
    global presidentes
    global governadores
    global prefeitos
    global candidatos

    candidatos.append([nome, numero, partido, cargo])

    if cargo == "presidente":
        presidentes.append([nome, numero, partido, cargo, 0, 0])
    elif cargo == "governador":
        governadores.append([nome, numero, partido, cargo, 0, 0])
    else:
        prefeitos.append([nome, numero, partido, cargo, 0, 0])

def gravar_eleitor(nome, cpf):
    global eleitores
    eleitores.append([nome, cpf, False])

def gravar_voto(cargo, numeroCand):
    for i in range(len(cargo)):
        if numeroCand == cargo[i][1]: 
            print(newLine + tabBody + "Nome: " + cargo[i][0])
            print(tabBody + "Partido: " + cargo[i][2])

            votoConfirmado = validar_voto_confirmado()

            if votoConfirmado:
                cont = cargo[i][4] + 1
                cargo[i][4] = cont
            
            return votoConfirmado

    print(newLine + tabBody + "Não foi possível encontrar nenhum candidato com o número informado.")

    return False

def gravar_voto_em_branco(cargo):
    global votosEmBranco 

    print(newLine + tabBody + "Atenção! O voto inserido é em branco. Se deseja prosseguir, por favor confirme abaixo.")

    votoConfirmado = validar_voto_confirmado()

    if votoConfirmado:
        for i in range(len(votosEmBranco)):
            if cargo == votosEmBranco[i][0]:
                cont = votosEmBranco[i][1] + 1
                votosEmBranco[i][1] = cont
                break

    return votoConfirmado

def gravar_voto_nulo(cargo):
    global votosNulos 

    print(newLine + tabBody + "Atenção! O voto inserido é em branco. Se deseja prosseguir, por favor confirme abaixo.")

    votoConfirmado = validar_voto_confirmado()
    
    if votoConfirmado:
        for i in range(len(votosNulos)):
            if cargo == votosNulos[i][0]:
                cont = votosNulos[i][1] + 1
                votosNulos[i][1] = cont
                break

    return votoConfirmado

def gravar_voto_eleitor(cpf, votoFeito):
    global eleitores
    
    for i in range(len(eleitores)):
        if cpf == eleitores[i][1]:
            eleitores[i][2] = votoFeito
#endregion

def realizar_apuraracao_votos(lista, cargo):
    global votosEmBranco
    global votosNulos
    global votosTT

    votoEmBrancoTT = 0
    votoNuloTT = 0
    votosValidosTT = 0

    for i in range(len(votosEmBranco)):
        if votosEmBranco[i][0] == cargo and votosEmBranco[i][1] > 0:
            votoEmBrancoTT += votosEmBranco[i][1]
            break

    for i in range(len(votosNulos)):
        if votosNulos[i][0] == cargo and votosNulos[i][1] > 0:
            votoNuloTT += votosNulos[i][1]
            break

    for i in range(len(lista)):
        votosValidosTT += lista[i][4]

    votosTT = votoEmBrancoTT + votoNuloTT + votosValidosTT

    lista = sorted(lista, key=itemgetter(4), reverse=True)

    for i in range(len(lista)):
        votosCand = lista[i][4]
        votosValidosPorc = (votosCand * 100) / votosTT
        lista[i][5] = votosValidosPorc

    exibir_apuracao(cargo, lista, votosTT, votosValidosTT, votoEmBrancoTT, votoNuloTT)

def exibir_apuracao(cargo, lista, votosTT, votosValidosTT, votoEmBrancoTT, votoNuloTT):
    print(newLine + newLine + newLine + tabBody + "Apuração resultado " + cargo + ":")

    print(newLine + tabBody + "Nome\t\tPartido\t\tTotal de votos\t\tVotos válidos %")

    print(tabBody + "-" * 74)

    for i in range(len(lista)):
        print(tabBody + "%s\t\t%s\t\t%2.f\t\t\t%2.f" % (lista[i][0], lista[i][2], lista[i][4], lista[i][5]))

    print(tabBody + "-" * 74)

    print(tabBody + "Total de votos: ", votosTT)

    votosValidosTTPorc = (votosValidosTT * 100) / votosTT
    print(tabBody + "Total de votos válidos: ", votosValidosTT, " | %2.f" % (votosValidosTTPorc) + " %")
    
    votosEmBrancoPorc = (votoEmBrancoTT * 100) / votosTT
    print(tabBody + "Total de votos em branco: ", votoEmBrancoTT, " | %2.f" % (votosEmBrancoPorc) + " %")

    votosNuloPorc = (votoNuloTT * 100) / votosTT
    print(tabBody + "Total de votos nulos: ", votoNuloTT, " | %2.f" % (votosNuloPorc) + " %")

menuPrincipal()
