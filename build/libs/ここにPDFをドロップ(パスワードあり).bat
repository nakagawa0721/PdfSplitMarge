cd %~dp0

@ SET PASSWORD=

@ SET /P PASSWORD="�p�X���[�h����͂��Ă�������"

java -jar PdfSplitApp.jar %* password=%PASSWORD%

pause
