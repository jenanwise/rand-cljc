(ns rand-cljc.core-test
  #?(:cljs (:require-macros [cljs.core.async.macros :refer [go]]))
  (:require #?(:clj [clojure.test :refer [is testing deftest]]
               :cljs [cljs.test :refer-macros [is testing deftest]])
            #? (:clj [clojure.core.async :refer [go timeout chan >! <! put!]]
                :cljs [cljs.core.async :refer [timeout chan >! <! put!]])
            [clojure.set :refer [subset?]]
            [rand-cljc.core :as rng]))

;; ## Basic usage tests
;; Simple little tests that just verify each of the core functions
;; don't blow up and return something sane-looking.

(deftest test-rand
  (let [rng (rng/rng)]
    (is (#?(:clj float? :cljs number?) (rng/rand rng)))
    (is (#?(:clj float? :cljs number?) (rng/rand rng 100)))))

(deftest test-rand-int
  (let [rng (rng/rng)]
    (is (integer? (rng/rand-int rng 100)))))

(deftest test-rand-nth
  (let [rng (rng/rng)
        coll [:a :b :c :d :e :f :g]]
    (is (get (set coll) (rng/rand-nth rng coll)))))

(deftest test-random-sample
  (let [rng (rng/rng)
        coll [:a :b :c :d :e :f :g]]
    (is (subset? (set (rng/random-sample rng 0.3 coll))
                 (set coll)))))

(deftest test-shuffle
  (let [rng (rng/rng)
        coll [:a :b :c :d :e :f :g]]
    (is (= (set (rng/shuffle rng coll))
           (set coll)))))


;; ## Repeatability
;; Using the same seed for multiple rngs should allow us to create
;; reproducible results with the randomizing functions.

(deftest test-repeatability
  (let [seed #?(:clj (System/currentTimeMillis)
                :cljs (.now js/Date))
        rng1 (rng/rng seed)
        rng2 (rng/rng seed)
        coll [:a :b :c :d :e :f :g]]
    (testing (str "randomizing functions should be repeatable with seed " seed)
      (is (= (rng/rand rng1)
             (rng/rand rng2)))
      (is (= (rng/rand-int rng1 100)
             (rng/rand-int rng2 100)))
      (is (= (rng/rand-nth rng1 coll)
             (rng/rand-nth rng2 coll)))
      (is (= (rng/random-sample rng1 0.3 coll)
             (rng/random-sample rng2 0.3 coll)))
      (is (= (rng/shuffle rng1 coll)
             (rng/shuffle rng2 coll))))))
