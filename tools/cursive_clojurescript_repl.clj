;;;; Copyright © 2015-2017 José Pablo Fernández Silva

;; https://github.com/bhauman/lein-figwheel/wiki/Running-figwheel-in-a-Cursive-Clojure-REPL

(use 'figwheel-sidecar.repl-api)
(start-figwheel!) ;; <-- fetches configuration
(cljs-repl)