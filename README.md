# task_management
Task management system and bugtracking

This is a project for bugtracking and managing tasks.
All tasks can be matched to one project.
Every task has a status and priority for fetching. For a comfortable managment all statuses and priorites can be colored (especial parameters in the Administrator profile).
When a task goes into the Completed status the "Complete date" field will be authomatically changed to the date and time where status was changed.

The main working process is in the task view form (user push the "View" button in the task's list). In the task's view form user can write comments and read comments from other users. Also the Assignee, Status and Priority can be changed.

Administrator (user with ADMIN role) can more possibility than user without ADMIN role.
Administrator can edit Catalogs: Statuses, Priorities and manage Users.
In statuses and priorities ADMIN can set the color. If Status/Priority has a color a fiels "Status"/"Priority" will be colored in a task's list and task view form.

User can work with tasks that were assigned to this user or if user is in the list of users of this task. Users of task can be set by author of the task or by the admin (user with ADMIN role) in the "Task edit mode".

By default project use **h2 in-mem database**. So before deploying an application you need change database parameters in the **application.properties** file. Also there is a TaskManagementRunner.java file. This file contains initialization code with demo data. Certainly it must be removed if you are going to use this application in industrial mode.
The application allows sending email messages for new users after registration process. For this reason you have to set email parameters in the **application.properties** file and uncomment block of code in the RegistrationController file.
