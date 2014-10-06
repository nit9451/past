##################################################################
# Philip Schwartz 												 #
# ID: 2368864													 #
# Programming Assignment 1 CSCI 3301							 #
# Sqrt.s - Calculates Sqrt of number X by doing a binary search. #
# Result returning register is $f12								 #
##################################################################

.data
x: 	.float 123.0
y: 	.float 0.0

one: .float 1.0
zero: .float 0.0
two: .float 2.0
precision: .float 0.000000001

counter: .word 0

intro:  .asciiz "Calculates the SQRT of the Input for X in .data section!"
inputMSG: .asciiz "Input: "
newLine: .asciiz "\n"
resultMSG: .asciiz "Result: "
errorMSG: .asciiz "ERROR: Input for x < 0 not allowed."
.text

main:
	#Initialization
	l.s $f0, zero		# zero
	l.s $f1, one		# one = 1.0
	l.s $f2, two		# two = 2.0
	l.s $f3, x			# x
	l.s $f4, y			# y = result register
	l.s $f18, precision
	lw $t1, counter #maintains the count of how many times it has been looped and quits after 100 iterations
	
	li $t0, 0	#Set t0 for index of stack
	add.s  $f11, $f3, $f0   #set z to x + 0
	addi $sp, $sp, 4
	
	#Print intro, with inputMSG + input X
	li $v0, 4
	la $a0, intro
	syscall
	li $v0, 4
	la $a0, newLine
	syscall
	li $v0, 4
	la $a0, inputMSG
	syscall
	li $v0, 2
	l.s $f12, x
	syscall
	li $v0, 4
	la $a0, newLine
	syscall
	
#-------Algorithm Here---------


	#Branch for error if x < 0
	c.eq.s  $f3, $f0
	bc1t	zeroError
	c.lt.s  $f3, $f0
	bc1t	zeroError
	#-------------------------

loop:
	sub.s 	 $f16, $f11, $f4 #$f16 = z - y
	
	c.eq.s  $f16, $f0		#if difference is equal to zero then answer found
	bc1t loop3
	c.lt.s  $f16, $f18		#if difference is less than precision, end
	bc1t loop3
	add.s $f13, $f11, $f4    # m = y + z
	div.s  $f13, $f13, $f2 	 # divide m/2
	
	#li $v0, 2
	#mov.s $f12, $f13
	#syscall
	#Print a newline 
	#la $a0, newLine
	#li $v0, 4
	#syscall
	
	s.s		$f13, ($sp)		# add midpoint to stack
	addi    $sp, $sp, 4		#increment stack by 4
	mul.s   $f6, $f13, $f13 #m * m = $f6
	sub.s   $f7, $f6, $f3  	#subtract m^2 - x
	
	#branch if m^2 - x = 0, then the answer was found
	c.eq.s  $f7, $f0
	bc1t	loop3
	
	beq $t1, 100, loop3	#quit the program after 100 iterations
	
	
	#branch if m^2 - x < 0, then jump to loop2
	c.lt.s  $f7, $f0
	bc1t	loop2
	
	#continue to loop1 if m^2 > 0
	
loop1:
	mov.s $f11, $f13	#set z to m (new upper limit)
	addi $t1, $t1, 1     #increment counter
	j loop
	
	
loop2:
	mov.s $f4, $f13	#set y to m (new lower limit)
	addi $t1, $t1, 1     #increment counter
	j loop
	

#----------Output Result--------------	
loop3: 
	la $a0, resultMSG
	li $v0, 4
	syscall
	mov.s $f12, $f13  #print result in $f12
	li $v0, 2
	syscall
	
	li $v0, 10
	syscall 		   #exit
	 
	
	
#------------Print Error MSG for x < 0-------------	
zeroError:
	#Print a newline 
	la $a0, newLine
	li $v0, 4
	syscall
	#Print an Error Here
	la $a0, errorMSG
	li $v0, 4
	syscall
	
	li 	$v0, 10
	syscall		 #exit
	
.end






