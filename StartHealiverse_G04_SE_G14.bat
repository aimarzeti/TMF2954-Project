@echo off
setlocal
cd /d "%~dp0"

if not exist out mkdir out

javac -encoding UTF-8 -d out ^
  "source code\learning module\LearningModule.java" ^
  "source code\quiz\QuizModule.java" ^
  "source code\user login\LoginApplication.java" ^
  "source code\gamification\Reward.java" ^
  "source code\gamification\GamificationModule.java" ^
  "source code\gamification\MentalHealthGameApp.java"

if errorlevel 1 (
  echo Compilation failed.
  exit /b 1
)

java -cp "out;." LoginApplication
endlocal
