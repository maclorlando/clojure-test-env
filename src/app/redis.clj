(ns app.redis
  (:require [com.stuartsierra.component :as component]
            [taoensso.carmine :as car]
            [app.protocols.redis :as r]))

(def commands-translation-map {:exp car/expire})

(defrecord RedisClient [opts config]
  component/Lifecycle
  (start [component]
    (let [opts {:pool {} :spec config}]
      (try
        (car/wcar opts (car/ping))
        (catch clojure.lang.ExceptionInfo e
          (println ::redis-component :ping-failed)
          (throw (ex-info "Redis connection exception" {} e))))
      (assoc component :opts opts)))
  (stop [component]
    (dissoc component :opts))

  r/Redis
  (exec! [component op args]
    (car/wcar (:opts component) (apply op args)))

  (set-value [component k av opts]
    (let [additional-commands
          (reduce-kv (fn [acc ak av]
                       (when (ak commands-translation-map)
                         (conj acc [(ak commands-translation-map) [k av]])))
                     []
                     opts)]
      (car/wcar (:opts component)
                (if (seq additional-commands)
                  (into [(car/set k av)]
                        (map (fn [cmd] (apply (first cmd) (second cmd)))
                             additional-commands))
                  (car/set k av)))))
  (get-value [component k]
    (car/wcar (:opts component) (car/get k))))