(ns clojure-vm-translator.file-reader
  (:require [clojure.java.io :as io]))

(defn readAll [file]
  (with-open [rdr (io/reader file)]
    (reduce conj [] (line-seq rdr))))

(defn map-file [file map-function wrap-function]
  (with-open [rdr (io/reader file)]
    (wrap-function (map map-function (line-seq rdr)))))
