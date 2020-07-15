erat(CURN, MAXN) :- 
T is CURN * CURN,
T > MAXN, !.
%%%%%%%%%%%%%%%%%%5
erat(CURN, MAXN) :- 
T is CURN * CURN,
T =< MAXN,
prime_cycle(CURN, T, MAXN, CURN),
T2 is CURN + 1,
erat(T2, MAXN), !.
%%%%%%%%%%%%%%%%%%%%%%
prime_cycle(CURN, T, MAXN, STEP) :-
(prime(CURN),
prime_cycle(T, MAXN, STEP)); true.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
prime_cycle(N, MAXN, STEP) :- N > MAXN.

prime_cycle(N, MAXN, STEP) :-
N =< MAXN,
assert(composite_table(N)),
NN is N + STEP,
prime_cycle(NN, MAXN, STEP).
%%%%%%%%%%%%%%%%%%%%%%%%%%
% :NOTE: not necessary to calculate primes up to N, sqrt(N) is enough
init(N) :- erat(2, N), !.
prime(N) :- \+( 1 is N),
\+ composite_table(N).
composite(N) :- composite_table(N).


less_in_list(F, [H | T]) :- F =< H.
less_in_list(F, []).


prime_divisors2(R, [H | T]) :-
less_in_list(H, T),
prime(H),
prime_divisors2(R1, T),
R is R1 * H.
prime_divisors2(1, []).

prime_divisors(1, []) :- !.
prime_divisors(X, D, [X]) :-
T is D * D,
X < T, !.
prime_divisors(X, D, [D | T]) :- 
0 is mod(X, D),
T2 is div(X, D),
!,
prime_divisors(T2, D, T).

prime_divisors(X, D, L) :-
 \+ (0 is mod (X, D)),
!,
T is D + 1,
prime_divisors(X, T, L).

prime_divisors(X, L) :- 
number(X),
!,
X > 1,
prime_divisors(X, 2, L).

prime_divisors(N, R) :- 
prime_divisors2(N,  R).

prime_palindrome(N, K) :-
prime(N),
buildK(N, K, R),
reverse(R, R2),
equals(R2, R).

buildK(N, K, []) :-
0 is N, !.
buildK(N, K, [H | TA]) :-
T is mod(N, K),
H is T,
NN is div(N, K),
buildK(NN, K, TA).
equals([], []) :- !.
equals([H1 | T1], [H2 | T2]) :-
H1 is H2,
equals(T1, T2).