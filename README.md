#Scheduler
This is a simple program in java that constructs time schedules based on certain parameters(rooms, instructors, courses etc). It utilizes the Beam Search algorithm and was made for the A.I. course in my univercity.

##Input/Output
The input of the program consists of four files:<br/>
-[courses.xml](Scheduler/blob/master/courses.xml) that has the ids and the names of all the courses that must be included in the time schedule, their semester, instructor's id as well as the size of the room that is required for the course.<br/>
-[rooms.xml](Scheduler/blob/master/rooms.xml) that has the ids of all the available rooms, their names as well as their size (medium, large).<br/>
-[slots.xml](Scheduler/blob/master/slots.xml) that has the time slots (2*60 minutes each from Monday till Friday) for when each room is available.<br/>
-[instructors.xml](Scheduler/blob/master/instructors.xml) that has the instructors' ids, names as well as the days, time slots in which they are not available for teaching.
(the names of the files are not important).<br/>
The calculated schedule can be saved as a .txt, .xml and .html file.

##Rules/Limitations
1)Two courses must not be taught in the same timeslot (Day/Hour) and in the same Room.<br/>
2)Two courses of the same instructor cannot be taught in the same timeslot.<br/>
3)Each course must be taught in two timeslots per week (2 * (2*60 minutes) ).<br/>
4)Two courses of the same semester cannot be taught in the same timeslot.<br/>
5)Each course must be taught in rooms that are of adequate space/size.<br/>
6)Each course must be taught in two different days per week.<br/>
7)The courses cannot be taught in Days/Hours in which their instructors are not available.<br/>

##License
Licensed under the [GNU General Public License, version 3 (GPLv3)](http://www.gnu.org/licenses/gpl.txt)<br/>
Redistributions of files must retain the above copyright notice.

