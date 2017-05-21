(ns tournal.core-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [tournal.core :as core]))

(deftest fake-test
  (testing "fake description"
    (is (= 1 2))))
