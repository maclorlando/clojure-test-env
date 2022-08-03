(ns domain.fixtures.redis 
  (:require [clj-test-containers.core :as tc]))

(def ^:dynamic *instance*)

(defn start-redis! [f] 
  (try 
    (println "starting redis")
    (let [redis (do (println :starting-redis {})
                    (tc/start! (tc/create {:image-name "redis:7.0.2"
                                           :exposed-ports [6379]})))]
      (println redis)
      (binding [*instance* redis]
        (f))
      (println :stopping-redis {})
      (tc/stop! redis))
    (catch Exception e
      (println e)
      {})
    ))

(defn get-local-port []
  (when-not (bound? #'*instance*)
    (throw (ex-info "redis not yet initialized" {})))
  (->> *instance*
       :mapped-ports
       (map val)
       first))