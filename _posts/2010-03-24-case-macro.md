---
layout: post
title: case マクロ
---

# {{page.title}}

てきとーに書いた。ハイライトさせたいな

{% highlight clj %}
(defmacro case [base-value & patterns]
 (cons
  'cond
  (fold (fn [[val & more] res]
         (concat res (list (if (= val :else) val `(= ~base-value ~val)) (first more)))) (
    (partition 2 patterns)))))
{% endhighlight %}
