.data
    menu: .asciiz "\n\n SISTEMA DE CONTROLE DE ESTOQUE \n1. Inserir um novo item no estoque\n2. Excluir um item do estoque\n3. Buscar um item pelo código\n4. Imprimir os produtos em estoque (código e quantidade)\n5. Sair\nDigite sua opção: "
    invl_opc_msg: .asciiz "\nOpcao NAO disponivel! Digite novamente\n\n"

    msg_cod: .asciiz "\nDigite o codigo do produto: "
    msg_qtd: .asciiz "Digite a quantidade do produto: "
    msg_prod_found: .asciiz "\nProduto encontrado. Quantidade em estoque: "
    msg_prod_not_found: .asciiz "\nProduto não encontrado no estoque\n"
    sair_msg: .asciiz "\nPrograma encerrado.."
    virgula: .asciiz "   |   "
    espaco: .asciiz "   "
    pular_linha: .asciiz "\n"
    repeticoes: .asciiz "\nRepeticoes: "
    cod_qtd: .asciiz "\nCodigo x Quantidade\n"
    msg_deletar: .asciiz "\nDigite o codigo do produto a ser deletado: "
    estoque_0: .asciiz "\nEstoque vazio!\n"
    item_deletado: .asciiz "\nItem deletado com sucesso!\n"
    
.text
    	# Inicializacao do ponteiro para o primeiro num da lista
    	li $t0, 0
    	la $s0, ($t0) # head

	li $t5, 0		# REGISTRADOR CONTADOR
	
    	j main
    	
insert:
	# Alocando espaco na heap para o primeiro num da lista
   	li $a0, 12  # 12 bytes para armazenar um ponteiro e dois valores inteiros
    	li $v0, 9   # Codigo do servico do sistema para alocacao na heap
    	syscall
   	move $s1, $v0  # Armazenando o endereco do num alocado na heap em $s1
   	
   	li $v0, 4 # IMPRIMIR NA TELA A MENSAGEM "CODIGO_P"
	la $a0, msg_cod 
	syscall
    
    	li $v0, 5  # LER A ENTRADA (INTEIRO) DO USUARIO E FAZER A COPIA
	syscall
	move $t3, $v0
	
	sw $t3, ($s1) # Armazenando o valor no endereco do no. alocado
	
	li $v0, 4 # IMPRIMIR NA TELA A MENSAGEM "QTD_P"
	la $a0, msg_qtd 
	syscall
    
    	li $v0, 5 # LER A ENTRADA (INTEIRO) DO USUARIO E FAZER A COPIA
	syscall
	move $t3, $v0
	
	sw $t3, 4($s1)  # Armazenando o valor no endereco do no. alocado
	sw $t0, 8($s1)  # O ponteiro proximo do no. nulo
	bge $t5, 1, mais_elementos
	
	move $s0, $s1
	j ultimo_insert
	
	mais_elementos:
		sw $s1, 8($s2)
		li $t2, 0
		move $s1, $s0 # Inicializando nó para primeiro valor informado
		
		loop_insert:
		beq $t2, $t5, ultimo_insert # EXECUTA QTD DE ELEMENTOS
		lw $s1, 8($s1)
		addi $t2, $t2, 1
    		j loop_insert
		
		ultimo_insert:
		move $s2, $s1
		addi $t5, $t5, 1
		jr $ra

delete:
	li $t2, 0 # REGISTRADOR "i"
	
	# Imprime mensagem para inserir o código do produto	
	li $v0, 4
    	la $a0, msg_deletar
    	syscall

	# Lê número digitado
    	li $v0, 5
    	syscall
    	move $t3, $v0
    	
    	move $s3, $s0 	# Inicializando nó para primeiro valor informado
    	move $s4, $zero  # $s4 é um ponteiro para o nó anterior

    	loop_delete:
    	addi $t2, $t2, 1	# Incrementa contador
    	beq $s3, $zero, not_found_delete  # Se o nó for igual a zero, significa que o produto não foi encontrado
    	lw $t4, 0($s3) 	# Carrega o código do produto do nó atual

    	# Se o código do produto corresponder ao código fornecido pelo usuário, pula para found_delete
    	beq $t4, $t3, found_delete

    	# Senão, continue percorrendo a lista
    	move $s4, $s3	
    	lw $s3, 8($s3)  	# Avanca para o próximo nó
  
    	j loop_delete

    	found_delete:
    	beq $t5, 1, zerar_head
    	beq $t2, 1, primeiro_elemento_delete # Deleta primeiro elemento
    	beq $t2, $t5, ultimo_elemento_delete # Deleta último elemento
    	
    	# DELETAR ELEMENTOS DO MEIO
    	lw $s3, 8($s3) # Avanca para o próximo nó
    	sw $s3, 8($s4) # FORNECE O ENDERECO PARA O ANTERIOR
    	j fim_delete

    	zerar_head:
    	move $s0, $t0 # head	
    	j fim_delete
    	
    	primeiro_elemento_delete:
    	lw $s3, 8($s3)	# LER O ENDEREÇO QUE ESTA APONTANDO
    	move $s0, $s3	# PASSAR O ENDEREÇO PARA O HEAD
    	j fim_delete
    	
    	ultimo_elemento_delete:
    	sw $t0, 8($s1)	# PASSAR O ENDEREÇO DO PENULTIMO SENDO NULO
    	    	    	    	
    	fim_delete:
    	subi $t5, $t5, 1
    	li $v0, 4 
    	la $a0, item_deletado # Imprime mensagem informando que produto foi deletado
    	syscall 
    	jr $ra
    	
    	not_found_delete:
    	li $v0, 4 
    	la $a0, msg_prod_not_found  # Imprime mensagem informando que produto não foi encontrado
    	syscall 
    	jr $ra

search: 
	# Imprime mensagem para inserir o código do produto	
	li $v0, 4
    	la $a0, msg_cod
    	syscall

	# Lê número digitado
    	li $v0, 5
    	syscall
    	move $t3, $v0
    	
    	move $s3, $s0 # Inicializando nó para primeiro valor informado

    	loop_search:
    	beq $s3, $zero, not_found_search # Se o nó for igual a zero, significa que o produto não foi encontrado
    	lw $t4, 0($s3) # Carrega o código do produto do nó atual

    	# Se o código do produto corresponder ao código fornecido pelo usuário, imprima a quantidade
    	beq $t4, $t3, found_search

    	# Senão, continue percorrendo a lista
    	lw $s3, 8($s3)  # Avance para o próximo nó
    	j loop_search

    	found_search:
    	li $v0, 4 
    	la $a0, msg_prod_found # Imprime mensagem informando que produto foi encontrado
    	syscall 
    	li $v0, 1 # Imprime quantidade do produto em estoque
    	lw $a0, 4($s3) # Carregue a quantidade em estoque do nó atual
    	syscall
    	jr $ra
    	
    	not_found_search:
    	li $v0, 4 
    	la $a0, msg_prod_not_found # Imprime mensagem informando que produto não foi encontrado
    	syscall 
    	jr $ra

print:
	beq $t5, $zero, estoque_zero
	
	li $t2, 0		# REGISTRADOR "i"
	
	move $s3, $s0
	
	li $v0, 4		# IMPRIMIR NA TELA CODIGO E QUANTIDADE
	la $a0, cod_qtd
	syscall
	 
	loop4:  
	beq $t2, $t5, fim  # Se i >= tamanho, sair
	
	li $v0, 4 # IMPRIMIR NA TELA ESPACO
	la $a0, espaco 
	syscall
		
	# Imprimindo a sequencia
    	li $v0, 1
    	lw $a0, ($s3)
    	syscall
    	
    	li $v0, 4 # IMPRIMIR NA TELA VIRGULA E ESPACO
	la $a0, virgula 
	syscall
	
	li $v0, 1
    	lw $a0, 4($s3)
    	syscall
    	
    	li $v0, 4 # IMPRIMIR NA TELA VIRGULA E ESPACO
	la $a0, pular_linha 
	syscall
	   
	lw $s3, 8($s3) 
	addi $t2, $t2, 1
	
	j loop4
	
	estoque_zero:
	li $v0, 4		# IMPRIMIR NA TELA ESTOQUE ZERO
	la $a0, estoque_0
	syscall
	
	fim:
	jr $ra

invalid_option:
    	li $v0, 4
    	la $a0, invl_opc_msg
    	syscall
    	
main:
    	li $v0, 4
    	la $a0, menu
	syscall

    	li $v0, 5
   	syscall
   	move $t1, $v0

    	blt $t1, 1, invalid_option
    	bgt $t1, 5, invalid_option

	# SE A ENTRADA FOR 1, PULA PARA O INSERT
	beq $t1, 1, op1
	
	# SE A ENTRADA FOR 2, PULA PARA O DELETE	
	beq $t1, 2, op2
	
	# SE A ENTRADA FOR 3, PULA PARA O SEARCH	
	beq $t1, 3, op3
	
	# SE A ENTRADA FOR 4, PULA PARA O PRIMEIRO
	beq $t1, 4, op4
	
	# SE A ENTRADA FOR 5, ENCERRE O PROGRAMA
	beq $t1, 5, op5
	
	op1:
		jal insert # Inserir um novo item no estoque
		j main # Exibe menu
		
	op2:
		jal delete # Inserir um novo item no estoque
		j main # Exibe menu
		
	op3:
		jal search # Inserir um novo item no estoque
		j main # Exibe menu
		
	op4:
		jal print # Imprime os produtos em estoque (código e quantidade)
		j main # Exibe menu
		
	op5:
		j exit
	
exit:
	li $v0, 4
	la $a0, sair_msg
	syscall

    	li $v0, 10
    	syscall
