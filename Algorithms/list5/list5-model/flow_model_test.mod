param n, integer, >= 2; /*no. nodes*/

set V, default {0..n-1}; /*set of nodes*/

set E, within V cross V; /*set of arcs*/

param a{(i,j) in E}, > 0; /*a(i,j) = capacity of (i,j) */

param s, symbolic, in V, default 0; /*setup source node*/

param t, symbolic, in V, default  n-1; /*setup sink node */

var x{(i,j) in E}, >= 0, <= a[i,j]; /* x[i,j] is elementary flow through arc (i,j) */

var flow, >= 0; /* total flow from s to t */

s.t. node{i in V}:
    sum{(j,i) in E} x[j,i] + (if i = s then flow) = sum{(i,j) in E} x[i,j] + (if i = t then flow);
    /* summary flow into node i through srcs = summary flow from node i through srcs */

maximize obj: flow;

solve;
    printf {1..56} "="; printf "\n";
    printf "Maximum flow from node %s to node %s is %g\n\n", s, t, flow;
 /* printf "Starting node   Ending node   Arc capacity    Flow in arc\n";
    printf "-------------   -----------   ------------    -----------\n";
    printf {(i,j) in E: x[i,j] != 0}: "%13s   %11s   %12g   %11g\n", i, j, a[i,j], x[i,j];
    printf {1..56} "="; printf "\n"; */

data;

param n := 32;

 param : E: a := 
   0 1 3
   0 2 22
   0 4 21
   0 8 16
   0 16 6
   1 3 13
   1 5 9
   1 9 1
   1 17 8
   2 3 4
   2 6 14
   2 10 11
   2 18 1
   3 7 1
   3 11 2
   3 19 6
   4 5 2
   4 6 3
   4 12 11
   4 20 8
   5 7 4
   5 13 6
   5 21 6
   6 7 5
   6 14 4
   6 22 5
   7 15 6
   7 23 14
   8 9 16
   8 10 10
   8 12 5
   8 24 7
   9 11 8
   9 13 7
   9 25 5
   10 11 2
   10 14 2
   10 26 4
   11 15 7
   11 27 15
   12 13 5
   12 14 5
   12 28 2
   13 15 16
   13 29 7
   14 15 16
   14 30 14
   15 31 12
   16 17 15
   16 18 5
   16 20 12
   16 24 4
   17 19 4
   17 21 6
   17 25 2
   18 19 4
   18 22 6
   18 26 4
   19 23 2
   19 27 2
   20 21 6
   20 22 3
   20 28 5
   21 23 13
   21 29 15
   22 23 14
   22 30 1
   23 31 19
   24 25 6
   24 26 1
   24 28 5
   25 27 10
   25 29 9
   26 27 11
   26 30 13
   27 31 8
   28 29 6
   28 30 2
   29 31 15
   30 31 11;