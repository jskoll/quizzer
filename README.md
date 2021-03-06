# quizzer
Quizzer is a small app that reads in a number of question and randomly selects from 
the provided list to ask the user

The files are read in from a non-standard format and converted into a JSON formatted list of questions
These questions are then saved to a new file called `questions.qson`.  This file is then read to randomly
select questions from.  The import only needs to happen on first run.  As long as the program can find an 
existing `questions.qson` file it will use it.

## Prerequisites for quizzer
This app is written in Kotlin as a command line app and compiles down to a JVM compatible executable.  
The only requirement to run this file is that Java is installed.

If needed you can download the Java install [here](https://java.com)

The `quizzer.zip` file has executables that work on Windows 7/10, MacOS 12.2 (likely to work on older versions but only
tested on 12.2), and Ubuntu 20.04 (likely on any linux based system but only tested on Ubuntu)

## Running  quizzer
Download a release from this [repo](https://github.com/jskoll/quizzer/releases/tag/1.0.0-beta3)

Extact the file (untar or unzip)

In the extracted folder you will find the executable files in `app\bin`.

For Windows the executable is `app.bat`

For MacOS/Linux the executable is `app`

**Note** The app will let the user know if the answer was incorrect or not for 1 second before the next 
question (or final grade) is displayed

### CLI Options (Mac/Linux only)
As with most CLI apps this app provides a help options to view all options provided. 

The files are relative to the location you run the app from.  For example if you extract the file in your 
Downloads folder and attempt to run quizzer from there it will look for the files in `~\Downloads`

Quizzer checks the time limit after each question is answered.  It will time out the quiz after the limit
has passed after a question is answered.  This means if the time limit is 10 minutes, and you leave a question 
up for 11 mins it will allow you to finish that question but timeout afterwards.

```shell
Usage: Quizzer options_list
Options:
    --file, -f -> Question file, will replace current questions with those in the file { String }
    --questionCount, -q -> Number of questions to ask, default: 10 { Int }
    --timeLimit, -tl -> the number of minutes the quiz is allowed to take, default: 10m { Int }
    --help, -h -> Usage info
```
#### Examples

**Import a question file and start a quiz of 10 questions 10 minute time limit**
```shell
app.bat -f q1000.q
```
This uses the default number of questions and time limit

**Run a quiz with previously imported questions (15 questions, 10m timelimit)**
```shell
app.bat -q 15 -tl 10
```
### Windows 
Quizzer includes a bat file in the release zip/tar file found within the extracted `bin` directory.  To run 
double click on `app.bat`.  This will open up a command screen that displays a menu for quizzer.  You can 
navigate this menu using your arrow (up/down) and select your option using the `ENTER` key.

### Acknowledgments
This app has used a couple open source libraries which allow for making a more user-friendly command line
app.
1. [mordant](https://github.com/ajalt/mordant) -- Allowed adding colors to the CLI output 
   1. Apache-2.0 License
2. [kotlin-inquirer](https://github.com/kotlin-inquirer/kotlin-inquirer) -- Allowed a more interactive CLI
   1. Apache-2.0 License
3. [GSON](https://github.com/google/gson) -- for creating/reading JSON
	1.  Apache-2.0 License