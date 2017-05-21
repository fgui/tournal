(ns tournal.runner
    (:require [doo.runner :refer-macros [doo-tests]]
              [tournal.core-test]))

(doo-tests 'tournal.core-test)
