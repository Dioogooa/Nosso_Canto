@echo off
chcp 65001 > nul
echo ==========================================
echo    CORRIGINDO BOM DE TODOS OS ARQUIVOS
echo ==========================================

echo Corrigindo arquivos Java...

REM Corrigir todos os arquivos .java
for /r src %%f in (*.java) do (
    echo Corrigindo: %%~nxf
    powershell -Command "$content = Get-Content -Path '%%f' -Raw -Encoding UTF8; $content = $content -replace '^\uFEFF',''; Set-Content -Path '%%f' -Value $content -Encoding UTF8"
)

echo.
echo ✅ TODOS os arquivos foram corrigidos!
echo.
echo AGORA tente compilar novamente!
pause