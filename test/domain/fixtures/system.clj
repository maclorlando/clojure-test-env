(ns domain.fixtures.system
  (:require [com.stuartsierra.component :as component]
            [domain.fixtures.redis :as r]
            [app.redis :as redis]))

(def ^:dynamic *system*)


(defn start-system! [f]
  (println "starting system")
  (try
    (let [updated-config {:redis {:uri (str "redis://localhost:" (r/get-local-port))}}
          system-map (component/system-map
                      :redis (redis/map->RedisClient {:config (-> updated-config :redis)}))
          system (component/start-system system-map)]
      (binding [*system* system]
        (f))
      (component/stop-system system))
    (catch Exception e
      (println e)
      {})))
