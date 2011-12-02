---
layout: post
title: Compojure 0.5.3 on GAE/Jの問題点?自分だけ?
---

# {{page.title}}
<p class="meta">2010-12-22</p>


ローカルでdevサーバを起動してアクセスすると
以下の例外が起きるのはうちだけの問題？？

{% highlight sh %}
java.lang.NoClassDefFoundError: compojure/response/Renderable
{% endhighlight %}

ググっても特にそれらしい情報がなかった。
Renderableだから0.5系全般でそうだったのかな？

で、例外からして単純に defprotocol で生成されるはずの
クラスファイルが参照できてないだけっぽいから、
compojure の project.clj に以下を追加してみて問題なくなったことを確認。

{% highlight clj %}
:aot [compojure.response]
{% endhighlight %}

ちなみに上記を追加して lein compile とかすると classes に
Renderable.class が生成されてることがわかるはず。

毎回修正しなくても良いように :aot を追加修正しただけのを
clojarsにプッシュして作業終了。

[http://clojars.org/org.clojars.liquidz/compojure](http://clojars.org/org.clojars.liquidz/compojure)

という自分のためのメモ


