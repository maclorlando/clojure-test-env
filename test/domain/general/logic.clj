(ns test.domain.general.logic
  (:require [clojure.test :refer [are deftest is testing use-fixtures]]
            [domain.fixtures.redis :as redis]
            [domain.fixtures.system :as system]
            [app.protocols.redis :as r]
            ))

 ;;providing fixtures for system and redis to wrap tests:
 (use-fixtures :once redis/start-redis! system/start-system!)

(deftest logic_test
  (testing "a simple logic test..." 
    (let [redis-client (-> system/*system* :redis)]
      (r/set-value redis-client "given-key" "given-value" {})
      (println redis-client)
      (println (r/get-value redis-client "given-key"))
      (println (r/get-value redis-client "x")))
    (is (= 1 1))))