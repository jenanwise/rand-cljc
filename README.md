# rand-cljc

Tiny portable explicit-PRNG versions of `clojure.core`'s randomizing functions.


## Installation

```clojure
[rand-cljc "0.1.0"]
```


## Motivation

Clojure and ClojureScript provides five (as of 1.7.0-beta1)
randomizing functions:

* `rand`
* `rand-int`
* `rand-nth`
* `random-sample`
* `shuffle`

However, these all use the system-wide pseudo-random number generator
with no way to provide an explicit PRNG of your own. This makes
writing repeatable code that uses randomization ugly or difficult,
especially when the code needs to run on multiple hosts.

rand-cljc re-implements those randomizing functions, but all with an
additional explicit `rng` as the first argument. The functions are
otherwise exactly the same as their `clojure.core` counterparts.


## Usage

Any PRNG that satisfies the very small `IRandom` protocol can be used
as the first argument to the randomizing functions. For convenience,
you can use the supplied `(rng)` function to create a
platform-appropriate PRNG: `Random` on the JVM and Closure's
`PsuedoRandom` in CLJS.

```clojure
(require '[rand-cljc.core :as rng])
(let [rng (rng/rng)
      coll (range 10)]
    (println (rng/rand rng)) ;; 0.8188531
    (println (rng/rand-int rng 10)) ;; 7
    (println (rng/rand-nth rng coll)) ;; 6
    (println (rng/random-sample rng 0.3 coll)) ;; (3 8 9)
    (println (rng/shuffle rng coll)) ;; [3 5 2 0 1 9 4 7 8 6]
    )
```

`(rng)` can of course also take a seed:

```clojure
(let [seed 19239492
      rng1 (rng/rng seed)
      rng2 (rng/rng seed)
      coll (range 10)]
    (println (= (rng/rand rng1) (rng/rand rng2))) ;; true
    (println (= (rng/shuffle rng1 coll) (rng/shuffle rng2 coll))) ;; true
    )
```


## Requirements

This library uses reader conditionals, so Clojure >= 1.7.0-alpha6 and
ClojureScript >= 0.0-3191 are required.


## Caveats

The provided PRNGs are only as strong as their platform-specific
backings (`java.util.Random` and `goog.testing.PsuedoRandom`),
*neither* of which is cryptographically secure. No attempt at
cryptographic security is promised or even attempted with this
library.

Moreover, neither of the provided PRNGs as values are nicely
serializable or cross-platform. E.g., you cannot take a PRNG on the
JVM, use it for a bit, and then transfer it over the wire to a CLJS
app and use it some more. Such a monstrosity could be built against
the provided `IRandom` protocol, but is not currently included.


## Tests

Currently only minimal tests are included.

To run:

```bash
# Clojure.
# leiningen.test doesn't pick up .cljc yet, so we have to manually
# specify our test namespaces.
lein test rand-cljc.core-test

# ClojureScript in browser.
lein cljsbuild once test
open test.html

# ClojureScript in headless browser.
# Requires phantomjs.
lein cljsbuild test
```


## Related libraries

* [pprng](https://github.com/cemerick/pprng) which provides a protocol
  and suite of functions closer to the `java.util.Random` methods
  (`next-int`, `next-long`, `next-boolean` and so on) and is written
  in cljx.


## License

Copyright © 2015 Jenan Wise

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
