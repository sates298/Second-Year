param n, integer, >= 2; /*no. nodes*/

set V, default {0..n-1}; /*set of nodes*/

set E, within V cross V; /*set of arcs*/

param a{(i,j) in E}, > 0; /*a(i,j) = capacity of (i,j) */

param s, symbolic, in V, default 0; /*setup source node*/

param t, symbolic, in V, default  n-1; /*setup sink node */

var x{(i,j) in E}, >= 0, <= a[i,j]; /* x[i,j] is elementary flow through aśrc (i,j) */

var matches, >= 0; /* total flow from s to t */

s.t. node{i in V}:
    sum{(j,i) in E} x[j,i] + (if i = s then matches) = sum{(i,j) in E} x[i,j] + (if i = t then matches);
    /* summary flow into node i through srcs = summary flow from node i through srcs */

maximize obj: matches;

solve;
    printf {1..56} "="; printf "\n";
    printf "Maximum no matches: %s\n", matches;
 /* printf "Starting node   Ending node   Arc capacity    Flow in arc\n";
    printf "-------------   -----------   ------------    -----------\n";
    printf {(i,j) in E: x[i,j] != 0}: "%13s   %11s   %12g   %11g\n", i, j, a[i,j], x[i,j]; */
    printf {1..56} "="; printf "\n";

data;
