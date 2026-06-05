================================================================================
                           HEALIVERSE PROJECT
              A Java Swing Mental Health & Well-Being Desktop App
                    SDG 3: Good Health and Well-Being
                   TMF2954 Java Programming Assignment
                        Group 14, Lecture Group G04/SE
================================================================================

PROJECT OVERVIEW
================================================================================
HEALIVERSE is an educational desktop application built with Java Swing that
promotes mental health awareness and well-being aligned with the United Nations
Sustainable Development Goal (SDG 3): Good Health and Well-Being.

Key Features:
  • Interactive learning module with 10 pages of educational content
  • Quiz module with 20 questions (True/False & Fill-in-the-Blank types)
  • Gamification system with badges, points, stars, and leaderboard
  • User progress tracking with file-based persistence
  • Smartphone-sized UI (390 x 720) desktop display
  • Pastel pixel-art design with friendly, calming aesthetics

TEAM MEMBERS & CONTRIBUTIONS
================================================================================
1. Keweil Anak Bansa
   - LoginApplication (sign-up, login, user management)
   
2. Reselda Anak Robie
   - LearningModule with 10 educational pages
   - Implements: Displayable, Navigable interfaces
   
3. Noor Azuah binti Sawal (105404)
   - QuizModule with 20 questions
   - Implements: Quizable interface
   
4. Zeti Nur Aimar binti Ali
   - GamificationModule (rewards, badges, leaderboard, timer)
   - Implements: Reward, LeaderboardProvider interfaces

PROJECT STRUCTURE
================================================================================
HEALIVERSE/
  ├── src/
  │   ├── LoginApplication.java
  │   ├── MentalHealthGameApp.java
  │   ├── LearningModule.java
  │   ├── QuizModule.java
  │   ├── GamificationModule.java
  │   ├── HealiverseTheme.java
  │   ├── HealiversePaths.java
  │   ├── Displayable.java (interface)
  │   ├── Navigable.java (interface)
  │   ├── Quizable.java (interface)
  │   ├── Reward.java (interface)
  │   └── LeaderboardProvider.java (interface)
  │
  ├── assets/
  │   └── images/
  │       ├── pixel/ (pixel-art game icons)
  │       └── ... (learning content images)
  │
  ├── data/
  │   ├── scores.txt (quiz results, auto-created)
  │   └── learning_progress.txt (page progress, auto-created)
  │
  ├── out/ (compiled .class files)
  │
  ├── README.txt (this file)
  ├── StartHealiverse_G04_SE_G14.bat (Windows launcher)
  └── .gitignore

ASSIGNMENT REQUIREMENTS CHECKLIST
================================================================================

Object-Oriented Design:
  ✓ Minimum 4 classes: 8+ public classes with clear responsibilities
  ✓ Minimum 4 interfaces: 5 interfaces (Displayable, Navigable, Quizable, 
                         Reward, LeaderboardProvider)
  ✓ Each member has ≥1 class and ≥1 interface contribution
  ✓ Inheritance: Custom Swing components extend base classes
  ✓ Polymorphism: Modules implement interfaces with different behaviors
  ✓ Overloading: Multiple constructors (LearningPage, QuizModule)
  ✓ Overriding: All interface methods implemented
  ✓ Exception Handling: InvalidPageException, GamificationDataException

Educational Content:
  ✓ Learning module: 10 pages of comprehensive mental health content
    1. What is Mental Health?
    2. The Mental Health Spectrum
    3. Common Mental Health Conditions
    4. Warning Signs to Watch For
    5. The Brain and Mental Health
    6. Mental Health in Malaysia
    7. Healthy Coping Strategies
    8. Mindfulness and Breathing
    9. When to Seek Professional Help
    10. Resources and Support (Malaysian helplines)

Quiz System:
  ✓ Quiz module: 20 questions total
  ✓ Question types: True/False (10) + Fill-in-the-Blank (10)
  ✓ Each question has options for user interaction
  ✓ Fill-in-the-Blank questions include hints

Score Display & Motivation:
  ✓ Score stored and displayed with percentage
  ✓ Motivational messages based on score:
    80-100%: "Outstanding!"
    60-79%:  "That's good!"
    40-59%:  "Good try!"
    20-39%:  "You can do better!"
    0-19%:   "Don't give up!"

Gamification Elements:
  ✓ Points: Coins earned from quiz completion
  ✓ Stars: Based on quiz performance
  ✓ Badges: 8 achievement badges (learn, quiz, combo achievements)
  ✓ Timer: 2-minute Calm Timer breathing quest
  ✓ Leaderboard: Top scores from all users
  ✓ Progress Tracker: Overall completion percentage

Data Persistence:
  ✓ Scores NOT hardcoded
  ✓ File-based storage: text files (NO external database)
    - scores.txt: Quiz attempts and results
    - learning_progress.txt: Module page tracking
  ✓ User progress saved automatically
  ✓ Supports multiple user profiles

UI/UX Design:
  ✓ Desktop application (Windows/Mac/Linux)
  ✓ Smartphone-sized display (390 x 720 pixels)
  ✓ Pastel pixel-art theme:
    - Colors: Pink (#FFC1D6), Lavender (#CDBBFF), Mint (#B7F4D1),
              Baby Blue (#BDE5FF), Soft Yellow (#FFE6A6)
  ✓ Large readable text (12-32pt depending on element)
  ✓ Big buttons for easy interaction
  ✓ Spacious, non-crowded layout
  ✓ Clear navigation flow

Technical Requirements:
  ✓ Java Swing only (NO external libraries)
  ✓ Command-line compilable: javac -encoding UTF-8 -d out src/*.java
  ✓ Command-line runnable: java -cp out LoginApplication
  ✓ UTF-8 encoding for international character support
  ✓ No hardcoded data (all dynamic and file-based)

HOW TO COMPILE & RUN
================================================================================

OPTION 1: Windows Batch File (Easiest)
  Double-click: StartHealiverse_G04_SE_G14.bat
  (Ensures data/ folder exists and launches the app)

OPTION 2: Manual Command-Line Compilation

  Step 1: Open Terminal/Command Prompt
    Navigate to the project root directory (where src/ and data/ folders are)

  Step 2: Compile
    javac -encoding UTF-8 -d out src/*.java

    Output: Compiled .class files will appear in out/ folder
    (No error messages should appear if successful)

  Step 3: Run
    java -cp out LoginApplication

    The Healiverse login screen will appear.

OPTION 3: Run Directly (Skip Login)
  java -cp out MentalHealthGameApp guest
  
  This opens the app directly as "guest" user (skips login screen).

DEFAULT TEST ACCOUNT
================================================================================
Username: admin
Password: password123

(Or create a new account using the Sign Up feature)

NAVIGATION FLOW
================================================================================

Login Screen
  ├─ Sign Up: Create new account with validation
  ├─ Login: Enter credentials (admin/password123 for testing)
  └─ Continue as Guest: Use as guest user

Home Dashboard
  ├─ Shows HEALIVERSE branding & SDG 3 logo
  ├─ 6 feature cards accessible from menu:
  │  ├─ Learn (10 pages of content)
  │  ├─ Quiz (20 questions)
  │  ├─ Achievements (badge progress)
  │  ├─ Progress (overall completion)
  │  ├─ Calm Time (2-min timer)
  │  └─ Leaderboard (top scores)
  └─ Bottom navigation bar (5 icons)

Learning Module
  ├─ Displays 10 pages sequentially
  ├─ Shows page number and progress bar
  ├─ Auto-saves progress to learning_progress.txt
  ├─ Navigation: Back/Next buttons
  ├─ Last page shows "Quiz >" button
  └─ Can resume from saved page on next login

Quiz Module
  ├─ 20 questions (True/False and Fill-in-Blank)
  ├─ Question counter and progress bar
  ├─ Immediate feedback (Correct/Incorrect)
  ├─ Shows correct answer if wrong
  ├─ Results screen shows:
  │  ├─ Final score (e.g., 16/20)
  │  ├─ Percentage (80%)
  │  ├─ Star rating (1-3 stars)
  │  ├─ Motivational message
  │  └─ Options to retry or return home
  └─ Score saved to scores.txt automatically

Gamification (Rewards)
  ├─ Player Status Panel:
  │  ├─ Username and level
  │  ├─ EXP bar toward next level
  │  └─ Resource counts (hearts, stars, coins, badges)
  │
  ├─ Collectibles:
  │  └─ 12 special thematic icons
  │
  ├─ Badges Section:
  │  ├─ 8 unlockable achievement badges
  │  ├─ Shows locked/unlocked status
  │  └─ Featured badge highlight
  │
  ├─ Progress Tracker:
  │  ├─ Overall completion percentage
  │  ├─ Milestone checklist
  │  └─ Learning + Quiz + Badge progress
  │
  ├─ Calm Timer Quest:
  │  ├─ 2-minute breathing timer
  │  ├─ Start and Reset buttons
  │  └─ Progress visual
  │
  └─ Leaderboard:
     ├─ Top 10 user scores
     ├─ Shows username, score, percentage
     └─ Sortable by rank

Profile Screen
  ├─ Player information display
  ├─ Feature demonstration notes
  └─ Buttons: Back to Home, Log Out, Exit App

DATA STORAGE
================================================================================

Scores File: data/scores.txt
  Format: username,score,total,percentage,timestamp
  Example:
    admin,16,20,80,2024-06-15 14:30:00
    guest,14,20,70,2024-06-15 14:45:00
  
  Auto-created if not present.
  Updated after each quiz completion.
  Used for leaderboard rankings.

Learning Progress File: data/learning_progress.txt
  Format: username,currentPage,totalPages,timestamp,completed
  Example:
    admin,5,10,2024-06-15 13:20:00,false
    guest,10,10,2024-06-15 13:50:00,true
  
  Auto-created if not present.
  Updated on every page transition.
  Allows users to resume from saved page.

Notes:
  - Both files auto-create if missing
  - Both use plain text format (easy to inspect)
  - No hardcoding of data
  - Each user has independent progress

DESIGN THEME
================================================================================

Color Palette (Pastel Pixel Art):
  Primary:
    • Pastel Pink (#FFC1D6) - Buttons, accents, warm elements
    • Mint Green (#B7F4D1) - Success states, positive actions
    • Lavender (#CDBBFF) - Secondary elements, calming accents
    • Baby Blue (#BDE5FF) - Information elements, cool accents
    • Soft Yellow (#FFE6A6) - Highlights, rewards, warmth

  Neutral:
    • Cream/Off-White (#FFF9F2) - Main background
    • Dark Purple (#3A2756) - Primary text, dark elements
    • Muted Purple (#6452A6) - Secondary text, subtle elements

Typography:
  • Title Font: Monospaced Bold (24-32pt) for headings
  • Button Font: SansSerif Bold (14-18pt) for interactive elements
  • Body Font: SansSerif Plain (12-14pt) for content text

Design Principles:
  ✓ Cute but academic appearance
  ✓ Pastel colors reduce eye strain
  ✓ Large, readable text for accessibility
  ✓ Spacious layout prevents overcrowding
  ✓ Consistent color usage throughout
  ✓ Mobile-style layout adapted for desktop
  ✓ Clear visual hierarchy
  ✓ Soft, friendly, calming aesthetic

TROUBLESHOOTING
================================================================================

Compilation Error: "cannot find symbol"
  Solution:
    1. Verify all Java files are in src/ folder
    2. Ensure UTF-8 encoding flag: javac -encoding UTF-8 ...
    3. Check file names match class names
    4. Run: javac -encoding UTF-8 -d out src/*.java

Runtime Error: "ClassNotFoundException: LoginApplication"
  Solution:
    1. Verify out/ folder contains .class files
    2. Use correct command: java -cp out LoginApplication
    3. Ensure current directory is project root
    4. Recompile if .class files are missing

Images Not Loading
  Solution:
    1. Place images in assets/images/ folder
    2. Place pixel art in assets/images/pixel/ folder
    3. Images are optional - app shows text if missing
    4. Check file names match references in code

Data Not Saving
  Solution:
    1. Verify data/ folder exists in project root
    2. Check folder is writable (permissions)
    3. Folder auto-creates if missing
    4. Check for error messages in terminal

UI Layout Issues
  Solution:
    1. App is optimized for 390x720 display
    2. Some screen DPI settings may cause slight misalignment
    3. Try resizing or maximizing the window
    4. Layout is tested on standard Java Swing L&F

PRESENTATION TIPS
================================================================================

When demonstrating for grading:

1. LOGIN & SIGN-UP (30 seconds)
   "Let me show the login system..."
   - Sign up with new account (show validation)
   - Login with admin/password123
   - Point out guest option

2. LEARNING MODULE (1-2 minutes)
   "Here's our 10-page learning module..."
   - Browse 3-4 different pages
   - Show images and color-coded headers
   - Point out progress bar and page counter
   - Show how progress auto-saves
   - Click "Quiz >" on last page

3. QUIZ MODULE (2-3 minutes)
   "The quiz has 20 questions with 2 types..."
   - Answer a True/False question
   - Show immediate feedback
   - Answer a Fill-in-the-Blank question (show hint)
   - Complete several more questions
   - Show results with motivational message
   - Point out score saved to file

4. GAMIFICATION (2-3 minutes)
   "Gamification includes multiple reward systems..."
   - Show player status and level
   - Explain points, stars, badges
   - Highlight unlocked badges
   - Show progress percentage
   - Demo Calm Timer (start and reset)
   - Show leaderboard with multiple users

5. TECHNICAL HIGHLIGHTS (1 minute)
   "Our technical implementation includes..."
   - 5 interfaces for module contracts
   - 8+ classes with clear responsibilities
   - File-based data persistence
   - Theme system for consistent styling
   - Exception handling for robustness

6. CODE TOUR (Optional)
   "Looking at the code structure..."
   - Show src/ folder organization
   - Highlight interface implementations
   - Point out file I/O in GamificationModule
   - Show HealiverseTheme centralizing colors

CODE QUALITY NOTES
================================================================================

✓ OOP Principles:
  - Abstraction: Theme system, module interfaces
  - Inheritance: Custom Swing components
  - Polymorphism: Interface implementations
  - Overloading: Multiple constructors
  - Overriding: Interface method implementations

✓ Code Organization:
  - Separate classes for each module
  - HealiverseTheme centralizes styling
  - HealiversePaths handles file access
  - Clear method naming and documentation

✓ Comments:
  - Creator/tester comments preserved throughout
  - Method documentation for clarity
  - Group member responsibilities documented
  - Design decisions explained

✓ No External Dependencies:
  - Pure Java Swing only
  - No Maven, Gradle, or external libraries
  - Simple, self-contained project
  - Easy to compile and run anywhere

✓ Student-Friendly:
  - Beginner-level code style
  - Clear variable naming
  - Logical code structure
  - Well-documented without over-engineering

ADDITIONAL RESOURCES
================================================================================

Mental Health Resources (In-App):
  - 10 pages of educational content
  - Malaysian-specific statistics
  - Crisis helpline numbers for Malaysia
  - Evidence-based coping strategies
  - Links to online resources

Learning Outcomes:
  Students understand:
  - What mental health is and its importance
  - The spectrum from thriving to crisis
  - Common conditions (anxiety, depression, stress)
  - Warning signs and early intervention
  - Neurotransmitters and brain chemistry
  - Cultural context in Malaysia
  - Coping strategies (sleep, exercise, journaling)
  - Mindfulness and breathing techniques
  - When to seek professional help
  - Available support resources

QUICK REFERENCE
================================================================================

Compilation:
  javac -encoding UTF-8 -d out src/*.java

Running:
  java -cp out LoginApplication

Test Account:
  admin / password123

Default Guest:
  Continue as Guest (no credentials needed)

Main Entry Points:
  - LoginApplication.main() - Standard entry
  - MentalHealthGameApp.main() - App entry (requires login in normal flow)

Key Classes:
  - LoginApplication: Authentication system
  - MentalHealthGameApp: Main dashboard and navigation
  - LearningModule: 10-page educational content
  - QuizModule: 20 questions with scoring
  - GamificationModule: Rewards and achievements

Key Interfaces:
  - Displayable: Content display contract
  - Navigable: Multi-page navigation
  - Quizable: Quiz contract
  - Reward: Gamification rewards
  - LeaderboardProvider: Score data loading

Data Files:
  - data/scores.txt: Quiz results
  - data/learning_progress.txt: Page progress

================================================================================
Project Version: 1.0 (Final)
Last Updated: June 2026
Assignment: TMF2954 Java Programming
Group: 14 (G04/SE)
Status: Ready for Submission ✓
================================================================================

