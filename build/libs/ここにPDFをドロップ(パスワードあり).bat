cd %~dp0

@ SET PASSWORD=

@ SET /P PASSWORD="パスワードを入力してください"

java -jar PdfSplitApp.jar %* password=%PASSWORD%

pause
