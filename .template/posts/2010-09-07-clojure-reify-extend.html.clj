; @layout  post
; @title   clojureのreify,extendの挙動

(p "どっちも何かおかしい。それとも可変長引数は使えないのかな？")

#-CLJ
Clojure 1.2.0
user=> (defprotocol Protocol (f [a & b]))
Protocol
user=> (def x (reify Protocol (f [a & b] {'a a '& & 'b b})))
#'user/x
user=> (f x 1)
java.lang.IllegalArgumentException: No single method: f of
interface: user.Protocol found for function: f of protocol: Protocol
(NO_SOURCE_FILE:3)
user=> (f x 1 2)
{a #, & 1, b 2}
user=> (extend String Protocol {:f (fn [a & b] {'a a 'b b})})
nil
user=> (f "hello" 1)
java.lang.IllegalArgumentException: No single method: f of
interface: user.Protocol found for function: f of protocol: Protocol
(NO_SOURCE_FILE:6)
user=> (f "hello" 1 2)
{a "hello", b (1 2)}
CLJ


(p "reifyは以下を見る限り「&」もシンボルとして扱われちゃう。")

(link "http://markmail.org/thread/rtxresashciapdnc"
           "http://markmail.org/thread/rtxresashciapdnc#query:clojure%20%22no%20single%20method%22+page:1+mid:afahohroee6dfsmr+state:results")

(p "extendはそれっぽく動くけど、可変部が2つ以上でないとエラーになっちゃう。")
(p "だからと言って以下のようにしてもダメ。")

#-CLJ
user=> (defprotocol Protocol2 (f [a]) (f [a b]) (f [a b & c]))
Warning: protocol #'user/Protocol2 is overwriting method f of
protocol Protocol
Protocol2
user=> (extend Integer Protocol2 {:f (fn ([a] {'a a}) ([a b] {'a a 'b b}) ([a b & c] {'a a 'b b 'c c}))})
nil
user=> (f 1 2)
java.lang.IllegalArgumentException: No single method: f of interface: user.Protocol2 found for function: f of protocol: Protocol2
(NO_SOURCE_FILE:10)
user=> (f 1 2 3)
java.lang.IllegalArgumentException: No single method: f of interface: user.Protocol2 found for function: f of protocol: Protocol2
(NO_SOURCE_FILE:11)
user=> (f 1 2 3 4)
{a 1, b 2, c (3 4)}
CLJ

(p "うーん、、")


