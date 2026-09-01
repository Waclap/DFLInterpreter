Get-ChildItem "src/" -Filter "*.dfl" -File -Recurse | ForEach-Object {
    .\dfl\dfl.exe $_.FullName .\gen\
}