;utility : checkers, factories and calculate method
(defn checkForNull [& args] (every? (fn [arg] (not (nil? arg))) args))
(defn checkValuesOfVector [v] (and (vector? v) (every? number? v)))
(defn checkValuesOfMatrix [m] (and (vector? m) (every? vector? m) (every? true? (mapv checkValuesOfVector m))))

(defn checkTensor [& t]
  (or (every? number? t) (and (every? vector? t) (apply == (mapv count t)) (apply checkTensor (apply concat [] t)))))
(defn checkForSize [& t] (and (apply checkTensor t) (or (every? number? t)
                                                        (and (every? vector? t) (apply == (mapv count t))))))
(defn factoryOfCheckers [f]
  {:pre [(checkForNull f)]
   :post [(checkForNull %)]}
  (fn[& args] (and (apply checkForSize args) (every? f args))))
(def areVectors (factoryOfCheckers checkValuesOfVector))
(def areMatrixes (factoryOfCheckers checkValuesOfMatrix))

(defn calculate [op extraPre]
  {:pre [(checkForNull op extraPre)]
   :post [(checkForNull %)]}
  (letfn [(calc' [& t]
            {:pre [(apply checkForSize t) extraPre]
             :post [(checkForNull %)]}
            (cond
              (every? number? t)
              (apply op t)
              :else
              (apply mapv calc' t)))]
    :return calc'
    ))

(defn operationWithVector [op]
  {:pre [(checkForNull op)]
   :post [(checkForNull %)]}
  (calculate op checkValuesOfVector))
(defn operationWithMatrix [op]
  {:pre [(checkForNull op)]
   :post [(checkForNull %)]}
  (calculate op checkValuesOfMatrix))
(defn operationWithTensor [op]
  {:pre [(checkForNull op)]
   :post [(checkForNull %)]}
  (calculate op true))

;operations
(def v+ (operationWithVector +))
(def v* (operationWithVector *))
(def v- (operationWithVector -))
(def m+ (operationWithMatrix +))
(def m* (operationWithMatrix *))
(def m- (operationWithMatrix -))
(def t+  (operationWithTensor +))
(def t-  (operationWithTensor -))
(def t*  (operationWithTensor *))

(commment ":NOTE: just binary?")
(defn scalar [v1 v2]
  {:pre [(areVectors v1 v2)]
   :post [(checkForNull %)]}
  (apply + (v* v1 v2)))
(defn vect [& v]
  {:pre [(apply areVectors v) (== 3 (count (first v)))]
   :post [(checkForNull %)]}
  (letfn [(det2*2 [a11 a12 a21 a22] (- (* a11 a22) (* a12 a21)))
          (vecMul [a1 a2 a3 b1 b2 b3] [(det2*2 a2 a3 b2 b3) (- (det2*2 a1 a3 b1 b3)) (det2*2 a1 a2 b1 b2)])
          (mult [v1 v2] (apply vecMul (apply conj v1 v2)))]
    (reduce mult v)))

(defn operationWithScalars [f pre]
  {:pre [(checkForNull f pre)]
   :post [(checkForNull %)]}
  (fn [t & args]
    { :pre [(pre t) (every? number? args)]
     :post [(checkForNull %)]}
    (def product (reduce * args))
    (mapv (partial f product) t))
  )
;ANS: fixed long multiply
(def v*s (operationWithScalars * areVectors))
(def m*s (operationWithScalars (fn [s x] (v*s x s)) areMatrixes))

(defn m*v [m v]
  {:pre [(areMatrixes m) (checkValuesOfVector v) (apply == (count v) (mapv count m))]
   :post [(checkForNull %)]}
  (mapv (partial scalar v) m))

(defn transpose [m]
  {:pre [(areMatrixes m)]
   :post [(checkForNull %)]}
  (apply mapv vector m))

(defn compatible [m1 m2] (= (count m2) (count (transpose m1))))
(defn mulTwoMatrix [m1 m2]
  {:pre [(compatible m1 m2)]
   :post [(checkForNull %)]}
  (transpose (mapv (fn [x] (m*v m1 x)) (transpose m2))))
(defn m*m [& m]
  {:pre [apply areMatrixes m]
   :post [(checkForNull %)]}
  (reduce mulTwoMatrix m))
