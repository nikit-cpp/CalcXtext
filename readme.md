# I. Importing
[stackoverflow.com](http://stackoverflow.com/questions/13073605/how-do-i-make-egit-respect-multi-project-git-repository "stackoverflow.com")
##
0. git clone into this folder
1. File, Import...
2. Git, Projects from Git, Next>
3. Local, Next>
4. Add..., Browse , Finish
5. Select repo from 4., Next>
6. Import existing projects, Next>
7. Check the checkboxes beside the projects you want and click Finish

# II. Compiling & Launching
1. run launchMeBefore.bat for create folders existing 'xtend-gen' and 'src-gen'
2. ignore errors create mwe2 workflow for grammar (launch .mwe2)
3. reset the workspace
4. enlarge MaxPermSize: add VM argument '-XX:MaxPermSize=512m'