# Translating in `Nand2Tetris`



## Languages

We want this Translator to use one Codebase to intelligently translate between the four main languages used in `Nand2Tetris`. Those are outlined in the following.



### 

### Hack Machine Code



### Hack Assembler



### Hack VM Language



### Jack



## Design



##

### Goals for an Implementation

I wish to make the implementation theoretically extensible and object oriented instead of relying heavily on Regex Pattern Matching or the like to distinguish concepts. Concepts should be able to be represented as Classes which capsulate the Translation Logic. 



### Design Problems to get right

##



#### In General

1. Who decides which Object (Assembly-Instruction, VM-Operation or Line-of-Jack) is created from a String?
2. How do those Objects get created and validated? What kind of Object can polymorphically  decide the validity of Objects? (Factories?) 





####

#### Assembler



#### VM-Translator

1. The representation of Boolean Values must be chosen and consistent between `AND`, `OR`, `LT`, `GT`, `EQ`
2. `LT`,`GT` and `EQ` will need `JMP` -Instructions. That means that they will need access to some kind of unique jump labels. That will require the management of current line numbers or unique symbol names.
3. 



#### Jack-Compiler







## 