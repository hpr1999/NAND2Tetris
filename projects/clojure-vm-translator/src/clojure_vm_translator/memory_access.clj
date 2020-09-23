(ns clojure-vm-translator.memory-access
  (:require [clojure.string :as str]))

(defn segment-memory-access [memory-segment offset mode]
  (let [base [(str "@" offset)
              "D=A"
              (case memory-segment
                "local" "@LCL"
                "this" "@THIS"
                "that" "@THAT"
                "argument" "@ARG")]]
    (case mode "push" (conj base "A=A+D" "D=M") "pop" (conj base "D=A+D"))))

(defn save-in-d [mode] (case mode "push" "D=M" "pop" "D=A"))

(defn memory-access [segment offset file-name mode]
  (case segment
    ("local" "this" "that" "argument") (segment-memory-access segment offset mode)
    "constant" [(str "@" offset) (save-in-d mode)]
    "temp" [(str "@" (+ 5 offset)) (save-in-d mode)]
    "pointer" [(case offset 0 "@THIS" 1 "@THAT") (save-in-d mode)]
    "static" [(str "@" file-name "." offset) (save-in-d mode)]
    ))

(defn stack-push [segment offset file-name]
  (conj
    (memory-access segment offset file-name "push")
    "@SP"
    "M=M+1"
    "A=M-1"
    "M=D"))

(defn stack-pop [segment offset file-name]
  (conj
    (memory-access segment offset file-name "pop")
    "@13"
    "M=D"
    "@SP"
    "AM=M-1"
    "D=M"
    "@13"
    "A=M"
    "M=D"
    )
  )

(defn memory-command [command file-name]
  (let [command-parts (str/split command #" ")
        command-part (nth command-parts 0)
        segment-part (nth command-parts 1)
        offset-part (nth command-parts 2)]
    (case command-part
      "push" (stack-push segment-part (Integer/parseInt offset-part) file-name)
      "pop" (stack-pop segment-part (Integer/parseInt offset-part) file-name))
    ))