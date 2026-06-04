@echo off
setlocal
cd /d "%~dp0"

if exist out rmdir /s /q out
mkdir out

javac -encoding UTF-8 -d out ^
  "src\HealiverseTheme.java" ^
  "src\HealiversePaths.java" ^
  "src\Displayable.java" ^
  "src\Navigable.java" ^
  "src\Quizable.java" ^
  "src\Reward.java" ^
  "src\LeaderboardProvider.java" ^
  "src\UserSession.java" ^
  "src\LearningModule.java" ^
  "src\QuizModule.java" ^
  "src\LoginApplication.java" ^
  "src\GamificationModule.java" ^
  "src\MentalHealthGameApp.java"

if errorlevel 1 (
  echo Compilation failed.
  exit /b 1
)

java -cp "out;." MentalHealthGameApp
endlocal
