(ns clojure-vm-translator.core
  (:require [clojure-vm-translator.file-reader :refer :all]
            [clojure-vm-translator.memory-access :refer :all]))

(doseq [x (map-file "C:\\Development\\Uni\\nand2tetris\\projects\\clojure-vm-translator\\resources\\example.vm"
          #(memory-command % "foo") doall) y x] (println y))