#######################################################################################
# Philip Schwartz 																	  #
# ID: 2368864																		  #
# Programming Assignment 1 CSCI 3301												  #
# Fibo.s : Computes the given fibonacci index value for the index specified by user.  #
#######################################################################################


#---------------------------------------------------------------
# data Section, holds the prompt, errormsg, and index
.data

index: .word 	8   #index 8 for testing, Answer = 21 (0, 1, 1, 2, 3, 5, 8, 13, 21 ...)


errorMSG: .asciiz "\nError index below 0"
sumMSG:  .asciiz "\nAnswer: "
indexMSG: .ascii "Calculating Fibonacci Sum at Index: "


#---------------------------------------------------------------
# text Section
.text
.globl 	main
.ent 	main

main:
	li $v0, 4					#syscall (4) to print Index message 
	la $a0, indexMSG
	syscall
	
	lw $v0, index				#load index into register
	add $a0,$v0,$zero 			#move index to $a0
 
 	li $v0, 1					#print current index being looked at
 	syscall

 	bltz $a0, error				#error if index < 0

	jal recursiveFib 			#call recursiveFib
 
#------------After recursively finding fibonacci sum at index, print sum, then terminate program----------- 
 	add $v1, $v0, $zero
 
	li $v0, 4
	la $a0, sumMSG
	syscall
	
	add $a0,$v1,$zero
	li $v0,1  					#print integer system call
	syscall
	li $v0,10 					#terminate, system call
	syscall
#-----------------------------------------------------------------------------------------------------------


#---------------------------recursiveFib, recursively computes fibonacci sum--------------------------------
#a0 = index
#if (index==0) goto return0
#if (index==1) goto return1
#return( recursiveFib(index-1)+recursiveFib(index-2))

recursiveFib:
	addi $sp,$sp,-12 			#save in stack
	sw $ra,0($sp)
	sw $s0,4($sp)
	sw $s1,8($sp)

	add $s0,$a0,$zero			#add index to $s0

	addi $t1,$zero,1			# set $t1 = 1 for comparison to index
	beq $s0,$zero,return0		#if (index == 0) jump to return0
	beq $s0,$t1,return1			#if (index == 1) jump to return1

#-----else-------
	addi $a0,$s0,-1  			#set $a0 to index - 1

	jal recursiveFib			#jump and link back to recursiveFib with $a0 = index-1

	add $s1,$zero,$v0     		#set $s1 = recursiveFib(index-1)

	addi $a0,$s0,-2				#set $a0 to index - 2

	jal recursiveFib            #v0=recursiveFib(index-2)

	add $v0,$v0,$s1       		#v0=recursiveFib(index-2) + $s1
	
	.end
#----------------

 
#--------EXIT------
exitRecursiveFib:
	lw $ra,0($sp)       		#read all registers from stack
	lw $s0,4($sp)
	lw $s1,8($sp)
	addi $sp,$sp,12       		#move stack pointer back
	jr $ra						#jump to return register
 	.end
 	
#------return1 & return0-----------BASE CASES
#if(index == 1) goto return1
#if(index == 0) goto return0
return1:
	li $v0,1
	j exitRecursiveFib
	.end
return0:     
	li $v0,0
	j exitRecursiveFib
	.end
#---------------------------------- 
 
#-----error------------------------ 
error:
	li $v0,4  			#print null terminated string system call (4)
	la $a0,errorMSG  	#load error message into $a0
	syscall 			#print error message
	#terminate
	li $v0, 10			
	syscall
	.end
#----------------------------------
.end