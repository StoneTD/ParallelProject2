# ParallelProject2

### <ins>Problem 1</ins>
In problem 1, the main goal of the code is to juggle the various checks in order to make the guests (threads) run smoothly. <br/>
Each thread must be let in to the maze (a block of code containing a method call) one at a time by the minotaur (main function), <br/>
And each thread must only be able to get to the cupcake at the end of the maze (access an external boolean) one at a time. <br/>

In order to solve these issues I used two reentrant locks and created method calls for a class that would serve as "The Minotaur" titled "MinotaurBirthday". <br/>
These method calls would utilize the reentrant locks to make sure only one thread accessed and edited the code at a time, for the cupcake this was a method called cupcakeCheck which would check if the cupcake existed or not, and depending on if the guest has eaten a cupcake yet or not, if it would be eaten thus removing it. <br/>
Since the Servant would have to replace a cupcake every time someone ate one, a method in The Minotaur class would keep track of the number of times a cupcake has been replaced, and when that number reaches the number of guests, the Servant would notify the Minotaur that every guest has been through the maze (A boolean called "finished" would be switched to true) and all of the guests would stop entering the maze (The threads will be killed). <br/>

This solution works because the locking proceedure verifies that no threads will access data that another thread is currently editing, thereby keeping the data secure and accurate. <br/>

### <ins>Problem 2</ins>
Between the three solutions I believed the second solution was not only the simplest but also the most efficient. <br/> 

A simple sign (external boolean) to let the guests (threads) know that they cannot access the room (I didn't make anything special in the room I just put the thread to sleep for a few milliseconds but you get it) will not cause buildup or require any sort of queueing system. <br/>
In a real world scenario this would allow the guests to enjoy the party while waiting for the room to become available since they won't have to wait in line forever. <br/>
In terms of coding a multithreaded program this is also incredibly simple because all it requires is one simple lock, in this case we used a simple boolean variable. <br/>

The disadvantages of this approach is that the guests might not get a chance to view the vase because someone was camping outside of the room the whole time, or maybe someone left the room only to immediately return to it, you get the idea. <br/>
Not having a queueing system means there will likely be threads that don't get the chance to view the room and that doesn't seem "fair". <br/>
That being said if a guest wanted to see the vase they should've been quicker, not my fault they spent too long getting into overly political conversations for a party setting and lost track of time only to return to the sign to see that it says <span style="color:red">BUSY</span> once again


