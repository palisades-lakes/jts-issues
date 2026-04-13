@echo off
:: palisades.lakes (at) gmail (dot) com
:: 2026-04-13

::set XMX=-Xms29g -Xmx29g -Xmn11g
::set XMX=-Xms12g -Xmx12g -Xmn5g 
set XMX= 

set OPENS=--add-opens java.base/java.lang=ALL-UNNAMED
set CP=-cp lib/*

set JAVA="%JAVA_HOME%\bin\java"

set CMD=%JAVA% -ea -dsa %XMX% %OPENS% %CP% %*
::echo %CMD%
%CMD%
