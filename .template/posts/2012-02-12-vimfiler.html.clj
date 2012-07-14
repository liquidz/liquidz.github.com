; @layout post
; @title  VimFiler上で「送る」機能を実装するプラグイン作ったった

(warn "2012-02-12 @ShougoMatsu さんにTwitterで指摘していただいたところを修正・追記しています。")

(p "メインのファイラをVimFilerに移行中です。")

(p "Ubuntuではシェルのみで操作していたのですが、
   会社のWindows環境ではずっと" (link "DF" "http://homepage1.nifty.com/bee/df/") "を愛用していて、
   それと同じような環境をUbuntuでも使いたいと思っていました。")

(p "そこでVimmerとしてはVimFilerだろ！ということでいろいろと設定している中で
   VimFilerにエクスプローラの「送る」ライクな機能を提供するプラグインを作ってしまったので紹介です。")

(h2 "sendto plugin for vimfiler")

(dl {:github (link "https://github.com/liquidz/vimfiler-sendto")})

(ps "もともとVimFiler上で \"!\" コマンドを使えば外部コマンドでファイルを実行できるのですが、
    毎回入力するのが面倒だったのと unite.vim のインターフェイスを使って選択できれば
    より便利なんじゃないかということで書いてみた次第です。
    なおVimScriptはほどんど初心者に近いので書き方でおかしなところがあればご指摘ください。。"

    "設定方法などはGitHub上のREADMEを参照してください。")

(h2 ".vimrc for vimfiler")

(p "以下、おまけですが .vimrc に書いているVimFiler向けの設定です。")

(p "基本的にはDFのキーマップに合わせて変更しているのと"
   [:s "Ubuntuではシステム側の関連付けがVimFiler上では使えなかったので
       xdg-open を明示的に関連付けています。
       (mpg, wmv, rm あたりが開けないのは困りますよね！"])

(warn
 "Ubuntuでの関連付けが使えない件はEnter(VimFilerの関連付け)で開けなかっただけで
 x(システムの関連付け)であれば開けました。勘違いスミマセン")

#-VIM
let g:vimfiler_safe_mode_by_default = 0
let g:vimfiler_as_default_explorer = 1
let g:vimfiler_sort_type = "Time"
let g:vimfiler_sendto = {
\   'unzip' : 'unzip'
\ , 'Inkscape ベクターグラフィックエディタ' : 'inkspace'
\ , 'GIMP 画像エディタ' : 'gimp'
\ , 'gedit' : 'gedit'
\ }

nnoremap <Leader><leader> :VimFiler<CR>
aug VimFilerKeyMapping
    au!
    autocmd FileType vimfiler call s:vimfiler_local()

    function! s:vimfiler_local()
        " キーマップのカスタマイズ
        nmap <buffer> <C-r> <Plug>(vimfiler_rename_file)
        nmap <buffer> a <Plug>(vimfiler_toggle_mark_all_lines)
        nmap <buffer> m <Plug>(vimfiler_set_current_mask)
        nmap <buffer> M <Plug>(vimfiler_move_file)
        nmap <buffer> D <Plug>(vimfiler_make_directory)
        nmap <buffer> h <Plug>(vimfiler_smart_h)zz
        nmap <buffer> F <Plug>(vimfiler_new_file)
        " sendto呼び出し
        nnoremap <buffer> <Leader>s :Unite sendto<CR>

        " 関連付け
        if has('unix')
            call vimfiler#set_execute_file('sh', 'sh')
            " 2/12 不要になったのでコメントアウト
            "for ext in ["html", "htm", "pdf", "jpg", "gif", "png", "svg", "zip", "lzh", "mp3", "mpg", "wmv", "rm", "flv"]
            "    call vimfiler#set_execute_file(ext, 'xdg-open')
            "endfor
        endif

        " Unite bookmark連携
        nnoremap <buffer> z <C-u>:Unite bookmark<CR>
        nnoremap <buffer> A <C-u>:UniteBookmarkAdd<CR>
        " Unite bookmarkのアクションをVimFilerに
        call unite#custom_default_action('source/bookmark/directory' , 'vimfiler')
        " incremental search
        nnoremap <buffer> / /^\s*\(\|-\\|\|+\\|+\\|-\) \zs
    endfunction
aug END
VIM

(p "以上です。")


(warn "追記")
(ps "ちなみに vimfiler#set_execute_file は拡張子の指定にコンマ区切りが使えるそうです。
    (現状、コンマの前後にスペースは入れられないようです)"

    "なので上記のように for で回す必要はありませんでした。")

#-VIM
call vimfiler#set_execute_file("jpg,png", 'xdg-open')
VIM

(ps "また sendto に似た機能で g:vimfiler_execute_file_list というのがすでにありました。"

    "以下のように指定するとjpgファイルを開こうとした場合に
    Uniteのインターフェイスで xdg-open, gimp のどちらで開くかを指定できます。")

#-VIM
let g:vimfiler_execute_file_list = {
\   'jpg' : ['xdg-open', 'gimp']
\ }
VIM

(p "sendtoでは必要な時だけ指定するようにできることと、
   ワイルドカードの追加などで差別化していければと思います。")
