# Datapack Filer Language
## 概要
Datapack Filer Language(以降DFL)は「ファイルの生成」「ファイルへの書き込み」などの動作を命令として表現する言語です.  
特に, 条件分岐や再帰処理によってファイル構造が複雑になる際に便利です.  
Windowsなら以下のようなPowerShellを作ると便利です.
<details>
<summary> PowerShell例 </summary>

```powershell
Get-ChildItem "src/" -Filter "*.dfl" -File -Recurse | ForEach-Object {
    .\dfl\dfl.exe $_.FullName .\gen\
}
```
</details>

### コード例
<details>
<summary> コード例を表示 </summary>

```
* function example
setType waclap function mcfunction
open load
    / scoreboard objectives add dfl_example dummy
    > execute as @a at @s run |
    call dfl/recurse_test
close

set maxRecurseCount = 20
open dfl/recurse_test
    / scoreboard players set @a[distance=..10] dfl_example 0
    > execute as @a[distance=..10] at @s run |
    open dfl/recurse ** {type:"flame"}
        / execute if score @s dfl_example matches %{maxRecurseCount}.. run return
        / scoreboard players add @s dfl_example 1
        / particle $(type) ~ ~ ~ 0.1 0.1 0.1 0.5 10
        recurse {type:"angry_villager"}
    close
    call otherlib:hello
close

***
 tags from here
***

setType waclap tags json
open function/load
    ///
    {
      "values": [
        "waclap:load"
      ]
    }
    ///
close
```
</details>

### 実行結果
<div align="left">
    <img src="img/sample_code_result.png" alt="Sample Code Result">
</div>
<details>
<summary> data/waclap/function/load.mcfunction </summary>

```
scoreboard objectives add dfl_example dummy
execute as @a at @s run function waclap:dfl/recurse_test

```
</details>
<details>
<summary> data/waclap/function/dfl/recurse.mcfunction </summary>

```
execute if score @s dfl_example matches 20.. run return
scoreboard players add @s dfl_example 1
particle $(type) ~ ~ ~ 0.1 0.1 0.1 0.5 10
function waclap:dfl/recurse {type:"angry_villager"}

```
</details>
<details>
<summary> data/waclap/function/dfl/recurse_test.mcfunction </summary>

```
scoreboard players set @a[distance=..10] dfl_example 0
execute as @a[distance=..10] at @s run function waclap:dfl/recurse {type:"flame"}
function otherlib:hello

```
</details>
<details>
<summary> data/waclap/tags/function/load.json </summary>

```
{
  "values": [
    "waclap:load"
  ]
}

```
</details>

※ 上記の例では同ファイル内でfunctionとtagsを管理していますが, かえってソースコードが見にくくなるのでソースファイルを分割することをおすすめします.  
 DFLはjsonファイルには使わずfunctionにのみ使うべきです. また, 実際はload関数とrecurse系関数などの機能単位でソースファイルを分けるべきです.


## 文法
### 命令
- 基本構成: \[命令タイプ] \[オペランド]  
\(例) / summon creeper ⇒ '/': 命令タイプ, 'summon creeper': オペランド  
- 命令はすべて1行単位で書き, 上から下の順で実行される.
- 行頭のインデントは無視される.

### リソースロケーション
'waclap:math/add' のように表されるfunctionファイルやtagファイルのid.  
'waclap:math/add' の内, 'waclap' を名前空間\(namespace), 'math/add' をパス\(path)と呼ぶ.  
名前空間が指定されていない場合は, 名前空間にカレントタイプのデフォルト名前空間を当てはめる.

### タイプ設定
タイプ設定は'デフォルト名前空間', 'データタイプ', 'ファイル拡張子'の3つで構成される.  
デフォルト名前空間=waclap, データタイプ=function, ファイル拡張子=mcfunction の場合, 'math/add' のリソースロケーションにあたるファイルは 'data/waclap/function/math/add.mcfunction' となる.  
デフォルト名前空間とデータタイプには半角小文字英数字とアンダーバー'\_'を使用でき, ファイル拡張子には半角小文字英数字とアンダーバー'_', ピリオド'.'を使うことができる.  
デフォルトのタイプ設定は 'minecraft', 'function', 'mcfunction' となっている. タイプ設定の変更にはsetType命令を使用する.

## 命令一覧
<details>
<summary> 目次 </summary>

- 書き込み系命令  
[/ 命令](#line_op)  
[> 命令](#append_op)  
[/// 命令](#write_mode_op)  
[call 命令](#call_op)  
[recurse 命令](#recurse_op)
- ファイル系命令  
[open 命令](#open_op)  
[close 命令](#close_op)  
[setType 命令](#set_type_op)  
- 変数系命令  
[set 命令](#set_op)  
[def 命令](#def_op)  
[include 命令](#include_op)  
- コメント系命令  
[* 命令](#comment_op)  
[*** 命令](#comment_mode_op)
</details>

<a id="line_op"></a>
### /命令
- 機能  
オペランドの内容をファイルに書き込み, 改行する.  
%{var_name}と書くことで変数を展開した内容を書き込むことができる. 
%%{var_name}だと内容は展開されず, '%{var_name}' が書き込まれる.  
行末の'|'は書き込まれないので, 行末の空白を見やすくすることができる.  
- 使用例  
`/ summon %{mobType}`  
⇒ mobTypeに 'zombie' が設定されていれば, `summon zombie` が書き込まれる.

<a id="append_op"></a>
### \> 命令
- 機能  
/ 命令 とほぼ同じ機能だが, 書き込み後は改行されない.  
- 使用例
```
> execute as @a\[tag=%{tag}] at @s run |  
/ summon creeper
```
⇒ tagに 'participants' が設定されていれば, `execute as @a[tag=participants] at @s run summon creeper` が書き込まれる.

<a id="write_mode_op"></a>
### /// 命令
- 機能  
/// 命令で囲まれている行をすべて改行付きで書き込む. /や>のように%{var_name}でフォーマットできる. 　
行末の|もそのまま書き込まれる
- 使用例
```
///
summon tnt ~ ~ ~ |
kill @s
///
```
⇒
```
summon tnt ~ ~ ~ |
kill @s
```
が書き込まれる.

<a id="call_op"></a>
### call 命令
- 機能  
指定されたリソースロケーションのファイルを呼び出すfunctionコマンドを書き込む.  
リソースロケーションの後に書かれている文字列は呼び出しと続けて書き込まれる. そこでは%{var_name}としてフォーマットもできる.
書き込み後は改行する.
- 使用例
```
> execute as @a at @s run |
call waclap:particles/magic {strength:10}
```
⇒ `execute as @a at @s run function waclap:particles/magic {strength:10}` が書き込まれる.

<a id="recurse_op"></a>
### recurse 命令
- 機能  
現在開いているファイルを呼び出すfunctionコマンドを書き込む.  
オペランドに書かれている文字列は呼び出しと続けて書き込まれる. そこでは%{var_name}としてフォーマットもできる.
書き込み後は改行する.
- 使用例
```
> $
recurse {index:$(i)}
```
⇒ 'waclap:rec_test' を開いている場合は `$function waclap:rec_test {index:$(i)}` が書き込まれる.

<a id="open_op"></a>
### open 命令
- 機能  
指定されたリソースロケーションのファイルを開く.  
リソースロケーションの後に ' **' を書くことでそのファイルを開くfunctionコマンドを書き込んでからファイルを開くようになる.  
その後に書かれた文字列はfunctionコマンドの後に続けて書き込まれる. 書き込み後は改行する.
- 使用例  
`open waclap:math/add`  
⇒ 'waclap:math/add' にあたるファイルを作成し, 開く.  
`open waclap:math/sub ** {x: 2, y: 3}`  
⇒ `function waclap:math/sub {x: 2, y: 3}`と書き込んだ後, 'waclap:math/sub' にあたるファイルを作成し, 開く.

<a id="close_op"></a>
### close 命令
- 機能  
現在開いているファイルを閉じ, それ以前に開いていたファイルを開く.
- 使用例  
```
open waclap:entry
    / summon tnt
    open waclap:particles/flame **
        / particle flame ~ ~ ~ 0 0 0 0.1 10
    close
    / summon lightning_bolt
close
```
⇒
waclap:entryには
```
summon tnt
function waclap:particles/flame
summon lightning_bolt
```
waclap:particles/flameには
`particle flame ~ ~ ~ 0 0 0 0.1 10`
が書き込まれる.

<a id="set_type_op"></a>
### setType 命令
- 機能  
  タイプ設定を更新する. オペランドは 'デフォルト名前空間', 'データタイプ', 'ファイル拡張子' の順でスペース区切りで記載する.  
- 使用例
```
setType minecraft tags json
open function/load
    ///
    {
      "values": [
        "waclap:load"
      ]
    }
    ///
close
```
⇒
'data/minecraft/tags/function/load.json' に
```json
{
  "values": [
    "waclap:load"
  ]
}
```
と書き込まれる.

<a id="set_op"></a>
### set 命令
- 機能  
変数に値を代入する.  
`set [変数名] = [値]` とすることで\[変数名]の変数に\[値]を代入できる.  
値の解説は[値](#値)に記載されています.
- 使用例
```
set tag = "participants"
/ kill @a[tag=%{tag}]
```
⇒ `kill @a[tag=participants]`と書き込まれる.

<a id="def_op"></a>
### def 命令
- 機能  
`def [マクロ名](引数列) = [式]` とすることでマクロを定義する.  
マクロ名と引数は変数名と同じ命名規則で, 引数列は`[引数1], [引数2]`のようにして書く.  
式には[Apache Commons JEXL](https://commons.apache.org/proper/commons-jexl/reference/syntax.html)を使用し, その中では指定した引数を参照することができる.
- 使用例
```
def add(arg1, arg2) = arg1 + arg2
set name = add("player", 2)
/ kill @a[name="%{name}"]
```
⇒ `kill @a[name="player2"]`と書き込まれる.

<a id="include_op"></a>
### include 命令
- 機能  
`include [ファイルパス]` 指定されたファイルを実行し, その際のマクロ定義や変数の状態, タイプ設定を引き継いでもとの行に戻る.  
書き込みやファイル作成の命令を書くのはおすすめしない.  
- 使用例  

config.dfl: 
```
def add(a, b) = a + b
set author = "Waclap"
```
src.dfl: 
```
include config.dfl

open a
    set sum = add(2, 3)
    / sum: %{sum}
    / %{author}
close
```
⇒ 'a' にあたるファイルに
```
5
Waclap
```
と書き込まれる.

<a id="comment_op"></a>
### \* 命令
- 機能  
何も実行しない. コメントを書く際に使用する.
- 使用例
`* これはメモです`   
⇒ 実行時にはスキップされるので, 実行結果には影響を与えない.

<a id="comment_mode_op"></a>
### *** 命令
- 機能  
***命令で閉じられている部分を複数行のコメントとして解釈する. 
- 使用例
```
***
これはコメントの1行目.
2行目
***
```
⇒ 実行時にはスキップされるので, 実行結果には影響を与えない.

## 値
### リテラル
- 文字列リテラル  
 文字列データ.  
 "によって囲まれている部分を文字列リテラルとして解釈する.  
 文字列内で, 文字としての"を使用したいなら, \"と書く必要がある.  
 `"hello \"Steve\""` は `hello "Steve"`として解釈される.  
- 数値リテラル  
 数値データ. 10進整数, 16進整数, 10進実数を使用できる.  
 10進整数: 0-9までの文字 \(例: `103`, `02`)  
 16進整数: 0xの後に0-9, A-Fの文字 \(例: `0x2A`, `0x3ad`)  
 10進実数: 10進整数を.で区切る \(例: `30.1`, `10.`)  
### 参照値
- 変数  
 1文字目は半角アルファベット, それ以降は半角英数字と'_'が使える.  
 \(例: `var_0`, `POINT_SCORE`)  
 その変数名に格納されている値を参照できる. 一度も値が格納されていない値を参照するとコンパイルエラーとなる.  
- マクロ
 マクロ名は変数と同じ命名規則.  
 `[マクロ名]([引数])` として値を取得できる. マクロが存在しない場合や引数の数が一致しない場合はコンパイルエラーとなる.  
 引数は, `[値1], [値2]` のようにして書く.  
 例:
  - `add(2, 3)`
  - `add("a", add(3, length))`
