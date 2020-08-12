// R1: Erste Fib-Zahl
// R2: Zweite Fib-Zahl
// R3: Dritte Fib-Zahl
// ...
// R0: Addresse der aktuellen Fib-Zahl


// ---INIT---
@R1     // R1=1
M=1     // --
@R2     // R2=1
M=1     // --
D=A     // R0=2
@R0     //
M=D     // --
// ---END INIT---

(BEGINNEXTVALUE)
@R0     // LETZTEN WERT LADEN
A=M
D=M
A=A-1   // D = fib(n)+fib(n-1)
D=D+M   // --
A=A+1   // Speichern in M[n+1]
A=A+1   //
M=D     // --
@R0     // ZÄHLER ERHÖHEN
M=M+1   // --
@BEGINNEXTVALUE
0;JMP   // NÄCHSTER WERT
