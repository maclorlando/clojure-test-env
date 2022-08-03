(ns app.protocols.redis)

(defprotocol Redis
  (exec! [this op args] "carmines")
  (set-value [this k v opts] "sets a redis key")
  (get-value [this k] "retrieves a redis key associated value"))
