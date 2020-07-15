%delay
%cortesian tree
%merge of cortesian trees
mg(L, R, T) :-
L = node(KeyL, ValueL, PrL, LL, LR), 
R = node(KeyR, ValueR, PrR, RL, RR),
PrL > PrR, mg(LR, R, Res), T = node(KeyL, ValueL, PrL, LL, Res), !.
mg(L, R, T) :-
L = node(KeyL, ValueL, PrL, LL, LR), 
R = node(KeyR, ValueR, PrR, RL, RR),
PrL =< PrR, mg(L, RL, Res), T = node(KeyR, ValueR, PrR, Res, RR), !.
mg(L, [], L) :- !.
mg([], R, R) :- !.

%split of cortesian trees
sp(node(KY, VL, Pr, L, R), K, Res1, Res2) :- K < KY, 
sp(L, K, P1, P2), 
Res1 = P1, 
Res2 = node(KY, VL, Pr, P2, R).
sp([], _, [], []) :- !.
sp(node(KY, VL, Pr, L, R), K, Res1, Res2) :-
K >= KY,
sp(R, K, P1, P2),
Res1 = node(KY, VL, Pr, L, P1),
Res2 = P2, !.
sp([], _, [], []) :- !.

push_into(CortesianMap, Node, Res) :-
Node = node(KY, _, _, _, _),
splitByKey(CortesianMap, KY, L, M, R),
mg(L, Node, TP),
mg(TP, R, Res), !.
map_build([], []).
map_build([(KY, VL) | TL], Res) :-
map_build(TL, TLS),
map_put(TLS, KY, VL, Res), !.

splitByKey(CortesianMap, KY, L, M, R) :- 
sp(CortesianMap, KY - 1, L, TP),
sp(TP, KY, M, R), !.

findInSubTree(CortesianMap, Res) :-
CortesianMap = node(KY, _, _, R, _),
\+ is_empty(R),
findInSubTree(R, Res), !.

is_empty([]).

findInSubTree(CortesianMap, Res) :-
CortesianMap = node(KY, _, _, R, _),
is_empty(R),
Res is KY, !.

map_put(CortesianMap, KY, VL, Res):-
rand_int(12345678, PR),
C = node(KY, VL, PR, [], []),
push_into(CortesianMap, C, Res), !.

map_get(node(K, V, _, _, _), K, V).
map_get(node(K, V, _, L, R), KY, VL) :-
KY < K,
map_get(L, KY, VL), !.

map_get(node(K, V, _, L, R), KY, VL) :-
KY > K,
map_get(R, KY, VL), !.

map_remove(CortesianMap, KY, Res) :- 
splitByKey(CortesianMap, KY, L, M, R),
mg(L, R, Res), !.

map_ceilingKey(CortesianMap, KY, CeilingKey) :-
splitByKey(CortesianMap, KY - 1, L, M, R),
findInSubTree(R, CeilingKey).