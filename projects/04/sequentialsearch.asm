// Searches for an element in an array 
// and returns the address if found.
// Otherwise, returns -1.
// The Search is sequential.

// Interface:
// R0 - return value
// R1 - element so search
// R2 - first address of array 
// R3 - last address of array

// Implementation:
// R0 also holds the current iteration index

@R2     // Init R0 with R2.
D=M     //
@R0     //
M=D     // --

(START-SEARCH)

@R0     // set address
A=M     // --

D=M     // if current element
@R1     // == search key
D=M-D   // return the current
@END    // index.
D;JEQ   // --

@R0     // otherwise increment R0
M=M+1   // --

D=M     // if next index
@R3     // <= last index
D=D-M   //    continue searching
@START-SEARCH
D;JLE   // ------

// Element was not found.
@R0     // return -1.
M=-1    // --

(END)
@END
0;JMP