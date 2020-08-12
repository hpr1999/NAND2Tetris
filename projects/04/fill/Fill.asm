// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input.
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel;
// the screen should remain fully black as long as the key is pressed. 
// When no key is pressed, the program clears the screen, i.e. writes
// "white" in every pixel;
// the screen should remain fully clear as long as no key is pressed.

// Put your code here.

(BEGIN)
@KBD    // if keyboard 
D=M;    // inactive clear
@CLEAR  // everything
D;JEQ   // --

@SCREEN    // INIT R0
D=A     // as the
@R0     // KBD-Base address
M=D     // --

(PAINTPIXELBLACK)
@R0     // Load the current address from R0
A=M     // --
D=0     // All 1s
D=!D    // --
M=D     // Set Black

@R0     // Increment the current address
M=M+1   // --
@KBD        // if current address >=   
D=A         //      kbd base address 
@R0         // then continue  
D=D-M       //      painting 
@PAINTPIXELBLACK //      black
D;JGT       // --
@END        //  stop painting
0;JMP       // --

(CLEAR)
@SCREEN // INIT R0
D=A     // as the
@R0     // KBD-Base address
M=D     // --

(PAINTPIXELWHITE)
@R0     // Load the current address from R0
A=M     // --
D=0     // All 0s
M=D     // Set WHITE

@R0     // Increment the current address
M=M+1   // --
@KBD        // if current address >=   
D=A         //      kbd base address 
@R0         // then continue  
D=D-M       //      painting 
@PAINTPIXELWHITE // white
D;JGT       // --
@END        //  stop painting
0;JMP       // --

(END)   // Go back to
@BEGIN  // the beginning
0;JMP   // --