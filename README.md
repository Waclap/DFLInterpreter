# Datapack Filer Language Interpreter

## 概要
データパック(特にfunction)のファイル構成をわかりやすく書くことができる**Datapack Filer Language**のインタプリタです.  
Datapack Filer Languageの解説は[DFL言語解説](docs/dfl_desc/DFL言語解説.md)に記載されています.

## 使用方法
[GitHub](https://github.com/Waclap/DFLInterpreter)から最新版のバイナリ\(zipファイル)をダウンロードして展開し, その中のdfl.exeを実行して使用します.  
`.\dfl.exe [ソースファイル] [生成先フォルダ]` のようにして実行します.  
複数のソースファイルを使う場合は以下のようなPowerShellを使うと便利です.

```powershell
Get-ChildItem "src/" -Filter "*.dfl" -File -Recurse | ForEach-Object {
    .\dfl.exe $_.FullName .\gen\
}
```

PATHを設定すれば `dfl src.dfl .\gen\` のようにして実行できます.

## ライセンス
このプロジェクトは **MIT ライセンス** のもとで公開されています.
詳細は [LICENSE](LICENSE) ファイルをご覧ください.

### サードパーティのライセンス
このプロジェクトは
- [Apache License Version 2.0](licenses/Apache-2.0.txt)のもとで公開されている[Apache Commons JEXL](https://github.com/apache/commons-jexl) \([NOTICEファイル](licenses/NOTICE-JEXL.txt)\)
- [Apache License Version 2.0](licenses/Apache-2.0.txt)のもとで公開されている[Shadow Gradle Plugin](https://github.com/GradleUp/shadow) 

を使用しています.

## 作者
Waclap
- Github: [@Waclap](https://github.com/Waclap)

## サポート
バグ報告等は[Issues](https://github.com/Waclap/DflCompiler/issues)までお願いします.