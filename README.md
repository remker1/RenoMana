# Description
The Reno Application helps small renovation company managers to keep track of their projects, its due date and set a schedule for an accepted renovation request. They could also keep track of inventory items that are available in their office, and see a list of employees where they would be able to see who is assigned to what project, if any. On the customer side, customers are able to fill and send out a renovation request and leave reviews or feedback through the app about their experience(s).

# Prerequisites
Before installing the RenoMana Application, ensure you have the following prerequisites:

- Java: Required to run the application. Make sure you have Java installed on your system. [Download Java](https://www.java.com/en/download/)
- Docker Desktop: Used for running the application in a container. [Download Docker Desktop](https://www.docker.com/products/docker-desktop/)

# Installation Steps
(For Installation Guide with Pictures/Visulation, click [this](https://docs.google.com/document/d/1w0FADX0_oJc1_JlC1kkNfNMcAL2cwQGmA8ypfJn4GJs/edit?usp=sharing.))

**Step 1: Docker Desktop Installation**

-  If you haven't already, install Docker Desktop from the provided link.
- To run Docker Desktop:
    - Open Docker Desktop application
    - Click Sign In: This will direct you to your browser where you will be able to click Sign up button instead if you do not have an account. 
    - After creating your account, log in and you should get a pop out that would redirect you back to the desktop app. 
        - If not, you can find Docker Desktop at your tab bar or launchpad.
- Leave Docker Desktop running in the background.


**Step 2: Downloading Project Zip (jar and libraries)**
- Download our project zip (with libraries) here: https://drive.google.com/file/d/1LtPtDJ28vrCRRlF3Brn5xJS5aohEqXG8/view?usp=sharing
- Once downloaded, place it where you can easily find and access it. In this case, we recommend your Desktop/Homepage screen. 
- Unzip the project.
- Navigate to the location where the unzipped file is stored but do not open it yet.

Step 3: Running the Application
- In the folder where your unzipped file is, select the `RenoManaApplication Folder` but do not open it yet. Make sure it is only highlighted. 
- Type `command + I` which will open a smaller folder with `RenoManaApplication Folder` informations.
    - Double click the content from the `General: -> Where` 
    - Do `command + C`
- Open your terminal
    - Mac (two ways):
        - Launchpad → Search Bar → Terminal
        - `Command` + `Space bar` → Spotlight Search Bar → Terminal
- In the terminal, type `cd (command+V)/RenoManaApplication` 
- Type `bash runApp.sh` to run the app.

**Step 4: Running the website**
- In Docker Desktop, in the flask_server row, click the hyperlink `5001:5001` to get to the website.
- Once you click the hyperlink, you will be redirected to our customer site. 

**Step 5: Exiting Out**
- In your terminal where you did `bash runApp.sh,` type `control + c`
- Type `bash exitApp.sh`

# Video Demos
**Installation SetUp**

**Features Demo**

# Troubleshooting 
- Port Issues: Most common issue that can pop out is when port 5001 is already running when we are trying to run Docker. To troubleshoot:
    - Navigate back to the RenoManaApplication Folder
    - Click back_end_team
    - Click docker-compose.yml with any text editor you have
    - Change `ports: 5001:5001` left side 5001 to 5002 or 5003 or anything.
    - Save changes.
    - Do Step 2-4 again.

- Java Version Problems: Check that you have the correct version of Java installed by running `java -version` in your command line.


