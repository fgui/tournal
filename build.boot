(set-env!
 :source-paths #{"src/cljs"}
 :resource-paths #{"html"}

 :dependencies '[[org.clojure/clojure "1.9.0-alpha17"]         ;; add CLJ
                 [org.clojure/clojurescript "1.9.671"]
                 [adzerk/boot-cljs "2.0.0" :scope "test"]
                 [pandeiro/boot-http "0.8.3" :scope "test"]
                 [adzerk/boot-reload "0.5.1" :scope "test"]
                 [adzerk/boot-cljs-repl "0.3.3" :scope "test"]
                 [org.clojure/tools.nrepl "0.2.12" :scope "test"]
                 [com.cemerick/piggieback "0.2.1" :scope "test"]
                 [weasel                  "0.7.0" :scope "test"]
                 [reagent "0.7.0"]
                 [re-frame "0.9.4"]
                 [day8.re-frame/http-fx "0.1.3"]
                 [org.clojure/tools.nrepl "0.2.12" :scope "test"]
                 [com.cemerick/piggieback "0.2.1" :scope "test"]
                 [figwheel-sidecar "0.5.7" :scope "test"]
                 ])

(require '[adzerk.boot-cljs :refer [cljs]]
         '[pandeiro.boot-http :refer [serve]]
         '[adzerk.boot-reload :refer [reload]]
         '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]])

                                        

(deftask run []
  (comp
   (serve)
   (watch)
   (cljs-repl)
   (reload)
   (cljs)))


(deftask development []
  (task-options! cljs
                 {:optimizations :none
                  :source-map true
                  :compiler-options {:asset-path "js/main.out"}}
                 reload {:on-jsload 'tournal.core/mount-root})
  identity)

(deftask dev
  "Simple alias to run application in development mode"
  []
  (comp
   (development)
   (run)
   (target :dir #{"target/dev"})))


(deftask production []
  (task-options! cljs {:optimizations :advanced
                       :compiler-options {:asset-path "js/main.out"}})
  identity)

(deftask prod
  "Simple alias to run application in production mode"
  []
  (comp
   (production)
   (cljs)
   (target :dir #{"target/prod"})))



