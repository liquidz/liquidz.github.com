; @layout post
; @title  Mac上のVimを最新にした際のメモ(LuaJIT対応)

(p "Mac上でVimをソースからインストールしたことがなかったので、ソースビルドするついでにLua対応した際のメモです。")

(h2 "参考")
(p "作業する際に参考したページは以下の通りです。")
(ul [
     (link "neocompleteを入れてみた作業ログ - Make 鮫 noise"
           "http://saihoooooooo.hatenablog.com/entry/2013/11/29/152525")
     (link "vimにluajitを対応させてみた作業ログ - Make 鮫 noise"
           "http://saihoooooooo.hatenablog.com/entry/2013/12/02/122005")
     (link "Installed Vim7.4 - mabulog"
           "http://kazuomabuo.hatenablog.jp/entry/2013/08/21/112226")
     ]
  )

(h2 "必要なライブラリをインストール")
##SH
brew install mercurial
brew install lua
SH

(h2 "LuaJITをビルド")
##SH
cd ~/opt/src
git clone http://luajit.org/git/luajit-2.0.git luajit
cd luajit

make
make install
SH

(h2 "Vimをビルド")
##SH
cd ~/opt/src
hg clone https://vim.googlecode.com/hg/ vim
cd vim

./configure --prefix=/usr/local --with-features=huge --enable-multibyte --enable-luainterp --with-luajit --enable-fail-if-missing --with-lua-prefix=/usr/local

make
make install
SH
(ul [ "最後の '--with-lua-prefix' を指定しないとconfigure時に 'configure: error: could not configure lua' が出てしまう" ])

(h2 "余談: Vim 設定")
(h3 "NeoComplete")
(p "NeoComplete の設定をした際に `Another plugin set completefunc! Disabled neocomplete.` というエラーが出たので、.vimrc にて以下を指定して回避。")

##VIM
let g:neocomplete#force_overwrite_completefunc = 1
VIM

(p "参考ページ")
(ul [(link "issue with neocomplete plugin · Issue #283 · tpope/vim-rails" "https://github.com/tpope/vim-rails/issues/283")])

