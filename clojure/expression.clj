;HW11
;copied from kGeorgiy
(defn proto-get [obj key]
  (cond
    (contains? obj key) (obj key)
    (contains? obj :prototype) (proto-get (obj :prototype) key)
    :else nil))
(defn proto-call [this key & args]
  (apply (proto-get this key) this args))
(defn method [key]
  (fn [this & args] (apply proto-call this key args)))
(defn field [key]
  (fn [this] (proto-get this key)))
(defn constructor [ctor prototype]
  (fn [& args] (apply ctor {:prototype prototype} args)))

;convenient functions
(defn div [n m] (double (/ (double n) (double m))))
(def evaluate (method :evaluate))
(def diff (method :diff))
(def toString (method :toString))
(def getArgs (field :args))
(def calc (field :calc))
(def calcDiff (field :calcDiff))
(def sign (field :sign))
(def getValue (field :value))
(def toStringInfix (method :toStringInfix))

		  
;Abstract objects ans factories
(defn toStringCommon [argsb sign argse f]
  (letfn [(mapWithWs [args] (clojure.string/join " " (mapv f args)))
          (getS [b arg e] (if (empty? arg) ""
                                           (str b (mapWithWs arg) e)))]
    (cond (empty? argse)
          (str sign "(" (mapWithWs argsb) ")")
          :else
          (str "(" (getS "" argsb " ") sign (getS " " argse "") ")"))))
(def abstractOperation
  { :diff          #((calcDiff %1) (getArgs %1) (mapv (fn [arg] (diff arg %2)) (getArgs %1)))
   :toString      #(toStringCommon [] (sign %1) (getArgs %1) toString)
   :evaluate      #(apply (calc %1) (mapv (fn [arg] (evaluate arg %2)) (getArgs %1)))
   :toStringInfix #(toStringCommon (vector (first (getArgs %1))) (sign %1) (rest (getArgs %1)) toStringInfix)
   })
(defn createConstOrVar [diff eval toStr]
  (constructor
    (fn [this value] (assoc this :value value))
    {:diff diff :evaluate eval :toString toStr :toStringInfix toStr}))
(defn createOperation [sign calc calcDiff]
  (constructor
    (fn [this & args] (assoc this :args args))
    {:prototype abstractOperation
     :sign      sign :calc calc
     :calcDiff  calcDiff}))

;Constant and Variable
(declare ZERO, ONE)
(def Constant (createConstOrVar
                (fn [this variable] ZERO)
                (fn [this vars] (getValue this))
                (fn [this] (format "%.1f" (getValue this)))))
(def ONE (Constant 1))
(def ZERO (Constant 0))
(def Variable (createConstOrVar
                #(cond (= %2 (getValue %1))
                       ONE
                       :else
                       ZERO)
                (fn [this vars] (vars (getValue this)))
                #(str (getValue %1))))

;Operations
(declare Divide Multiply Subtract Add)
(defn mulAndDivDiffFactory [f]
  (fn [args diffedArgs] (let [part (fn [ar ar2] (Multiply (first ar) (apply Multiply (rest ar2))))
                              firstArgument (part diffedArgs args)
                              secondArgument (part args diffedArgs)
                              sqr (fn [arg] (Multiply arg arg))
                              productOfArgs (sqr (apply Multiply (rest args)))]
                          (f firstArgument secondArgument productOfArgs)
                          )))
(def diffDivision (mulAndDivDiffFactory #(Divide (Subtract %1 %2) %3)))
(def diffMultiply (mulAndDivDiffFactory (fn [f s t] (Add f s))))
(defn linearFunction [op] (fn [args diffedArgs] (apply (force op) diffedArgs)))
(defn linearFunctionsFabric [sign calc f] (createOperation sign calc (linearFunction f)))

(comment ":NOTE: try to do this without delays (now it looks like copy-paste)")
(def Add (linearFunctionsFabric "+" + (delay Add)))
(def Subtract (linearFunctionsFabric "-" - (delay Subtract)))
(def Negate (linearFunctionsFabric "negate" (fn [x] (- x)) (delay Negate)))
(def Multiply (createOperation "*" * diffMultiply))
(def Divide (createOperation "/" div diffDivision))
(def Sum (linearFunctionsFabric "sum" + (delay Sum)))
(def Avg (createOperation "avg" (fn [& args] (div (apply + args) (count args)))
                          (fn [args diffedArgs] (Divide (apply Sum diffedArgs) (Constant (count args))))))

(def Pow (createOperation "**" (fn [a b & args] (Math/pow a b)) (constantly ZERO)))
(def Log (createOperation "//" (fn [a b & args]
                                 (div (Math/log (Math/abs b)) (Math/log (Math/abs a)))) (constantly ZERO)))

(def OPERATIONS
  {
   '-      Subtract
   'negate Negate
   '+      Add
   '*      Multiply
   'sum    Sum
   'avg    Avg
   '/      Divide
   'pow    Pow
   'log    Log
   })
;Parse method is the same as in HW9
(defn parseObject [s]
  (letfn [(inner [arr]
            (apply (get OPERATIONS (first arr)) (mapv parse (rest arr))))
          (parse [arr]
            (if (and (coll? arr) (not (empty? arr)))
              (inner arr)
              (cond
                (number? arr)
                (Constant arr)
                :else
                (Variable (str arr))
                )
              ))]
    (parse (read-string s))))

;HW12
;copied from kGeorgiy
(defn -return [value tail] {:value value :tail tail})
(def -valid? boolean)
(def -value :value)
(def -tail :tail)
(defn _show [result]
  (if (-valid? result) (str "-> " (pr-str (-value result)) " | " (pr-str (apply str (-tail result))))
                       "!"))
(defn tabulate [parser inputs]
  (run! (fn [input] (printf "    %-10s %s\n" (pr-str input) (_show (parser input)))) inputs))
(defn _empty [value] (partial -return value))
(defn _char [p]
  (fn [[c & cs]]
    (if (and c (p c)) (-return c cs))))
(defn _map [f result]
  (if (-valid? result)
    (-return (f (-value result)) (-tail result))))
(defn _combine [f a b]
  (fn [str]
    (let [ar ((force a) str)]
      (if (-valid? ar)
        (_map (partial f (-value ar))
              ((force b) (-tail ar)))))))
(defn _either [a b]
  (fn [str]
    (let [ar ((force a) str)]
      (if (-valid? ar) ar ((force b) str)))))
(defn _parser [p]
  (fn [input]
    (-value ((_combine (fn [v _] v) p (_char #{\u0000})) (str input \u0000)))))
(defn +char [chars] (_char (set chars)))
(defn +char-not [chars] (_char (comp not (set chars))))
(defn +map [f parser] (comp (partial _map f) parser))
(def +parser _parser)
(def +ignore (partial +map (constantly 'ignore)))
(defn iconj [coll value]
  (if (= value 'ignore) coll (conj coll value)))
(defn +seq [& ps]
  (reduce (partial _combine iconj) (_empty []) ps))
(defn +seqf [f & ps] (+map (partial apply f) (apply +seq ps)))
(defn +seqn [n & ps] (apply +seqf (fn [& vs] (nth vs n)) ps))
(defn +or [p & ps]
  (reduce _either p ps))
(defn +opt [p]
  (+or p (_empty nil)))
(defn +star [p]
  (letfn [(rec [] (+or (+seqf cons p (delay (rec))) (_empty ())))] (rec)))
(defn +plus [p] (+seqf cons p (+star p)))
(defn +str [p] (+map (partial apply str) p))
(def *digit (+char "0123456789"))
(def *number (+str (+plus *digit)))
(def *string
  (+seqn 1 (+char "\"") (+str (+star (+char-not "\""))) (+char "\"")))
(def *space (+char " \t\n\r"))
(def *ws (+ignore (+star *space)))
(def *null (+seqf (constantly 'null) (+char "n") (+char "u") (+char "l") (+char "l")))
(def *all-chars (mapv char (range 32 128)))
(apply str *all-chars)
(def *letter (+char (apply str (filter #(Character/isLetter %) *all-chars))))
(def *identifier (+str (+seqf cons *letter (+star (+or *letter *digit)))))
(defn *array [p]
  (+seqn 1 (+char "[") p (+char "]")))
(defn *array [p]
  (+seqn 1 (+char "[") (+opt p) (+char "]")))
(defn *array [p]
  (+seqn 1 (+char "[") (+opt (+seq p (+star (+seqn 1 (+char ",") p)))) (+char "]")))
(defn *array [p]
  (+seqn 1 (+char "[") (+opt (+seqf cons p (+star (+seqn 1 (+char ",") p)))) (+char "]")))
(defn *array [p]
  (+seqn 1 (+char "[") (+opt (+seqf cons *ws p (+star (+seqn 1 *ws (+char ",") *ws p)))) *ws (+char "]")))
(defn *seq [begin p end]
  (+seqn 1 (+char begin) (+opt (+seqf cons *ws p (+star (+seqn 1 *ws (+char ",") *ws p)))) *ws (+char end)))

;My code

;Utilities
(defn rightFold ([f v args]
                 (if (empty? args)
                   v
                   (f v (rightFold f args))))
  ([f args] (rightFold f (first args) (rest args)))
  )
(defn rightPartial [f x] (fn [y] (f y x)))
;for symmetry in names
(def leftFold reduce)

;Primitive Parsers
(defn +string [s] (apply +seq (mapv +char (mapv str s))))
(defn +operationToken [ms s] (+seqf (constantly ms) (+string s)))
(def +negate (+operationToken 'negate "negate"))
(def +pow (+operationToken 'pow "**"))
(def +log (+operationToken 'log "//"))
(def +variable (+seqf Variable *identifier))
(def *doubleNumber (+map read-string (+seqf str (+opt (+char "-")) *number (+opt (+char ".")) (+opt *number))))
(def +number (+seqf (fn [x] (Constant (double x))) *doubleNumber))

;Order of calculation
(defn foldFunction [x y]
  (letfn [(check-func [x] (contains? OPERATIONS (symbol (str x))))
          (makeAction [f x y]  (f (get OPERATIONS (symbol (str x))) y))]
    (cond (check-func y) (makeAction partial y x)
          (check-func x) (makeAction rightPartial x y)
          (fn? x) (x y)
          (fn? y) (y x))))
(defn assocFabric [assoc] (fn [args] (assoc foldFunction (apply concat args))))
(def leftAssoc (assocFabric leftFold))
(def rightAssoc (assocFabric rightFold))

;Map : priorities -> [parser associativity]
(def PRIORITIES
  {
   1 [(+char "+-") leftAssoc]
   2 [(+char "*/") leftAssoc]
   3 [(+or +pow +log) rightAssoc]
   4 [+negate leftAssoc]
   })

;Main parser
(declare priorityParser)
(defn +primitive []
  (+seqn 0 *ws
         (+or
           (+seqn 1 (+char "(") (delay (priorityParser 1)) (+char ")"))
           (+seqf Negate (+ignore +negate) (delay (+primitive)))
           +number
           +variable)
         *ws))
(defn nextPriorityParser [curPriority]
  (if (= curPriority (count PRIORITIES))
    (+primitive)
    (priorityParser (+ curPriority 1))))
(comment ":NOTE: this is not pure parser combinators logic, but looks well")
(defn priorityParser [curPriority]
  (memoize
    (let [nextLevelParser (delay (nextPriorityParser curPriority))
          parseFunc (first (get PRIORITIES curPriority))
          assocFunc (last (get PRIORITIES curPriority))]
      (+map assocFunc (+or
                        (+seqf cons (+seq nextLevelParser)
                               (+star (+seq parseFunc nextLevelParser)))
                        (+seq parseFunc nextLevelParser))))))

;functions for test executing
(def parse (+parser (priorityParser 1)))
(defn parseObjectInfix [str2]
  (parse str2))
