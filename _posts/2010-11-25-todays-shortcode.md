---
layout: post
title: 今日のショートコード
---

# {{page.title}}
<p class="meta">2010-11-25</p>


フィボナッチ数列のN番目

[http://golf.shinh.org/p.rb?Fibonacci+Number](http://golf.shinh.org/p.rb?Fibonacci+Number)

{% highlight clj %}
(#(if(&lt; 0%2)(recur%3(dec%2)(+%%3))(pr%))0(read)1)
{% endhighlight %}

全然特別なことしてないな。。
一応vim-posterousのテストも兼ねた投稿


