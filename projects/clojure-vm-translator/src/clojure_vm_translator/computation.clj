(ns clojure-vm-translator.computation)
(def counter (atom 0))
(defn nextCounterValue [] (swap! counter inc))

(defn binary-calc [operation]
  (conj "@SP"
        "AM=M-1"
        "D=M"
        "A=A-1"
        (case operation
          "add" "M=D+M"
          "sub" "M=M-D"
          "and" "M=D&M"
          "or" "M=D|M"
          "eq" ["D=M-D" (str "@COMP." (nextCounterValue) ".TRUE") "D;JEQ"]
          )
        ))

(defn unary-calc [operation]
  ["@SP"
   "A=M-1"
   (case operation
     "neg" "M=-M"
     "not" "M=!M")])

(defn computation-command [command file-name]
  (case command
    ("add" "sub" "and" "or") (binary-calc command)
    ("neg" "not") (unary-calc command)
    ("eq" "lt" "gt") (throw (NoSuchMethodError. "Not implemented"))))