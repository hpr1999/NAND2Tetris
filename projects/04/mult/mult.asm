// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Mult.asm

// Multiplies R0 and R1 and stores the result in R2.
// (R0, R1, R2 refer to RAM[0], RAM[1], and RAM[2], respectively.)

// Put your code here.

// PSEUDO:
// i = R0;
// a = R1;
// result; (=R2)
// while ( i > 0 ) {
//      result+=a;
//      i--;
// }

@R2     // INIT     
M=0     // --

(BEGIN)

@R0     // if (i == 0) break;
D=M     //
@END    //
D;JEQ   // --

@R1     // result += a;
D=M     //
@R2     //
M=M+D   // --

@R0     // i--; 
M=M-1   // --

@BEGIN  // goto BEGIN
0;JMP   // --
(END)