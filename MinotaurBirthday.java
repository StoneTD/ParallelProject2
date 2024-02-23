// Dylan Stone
// Professor Parra
// COP 4520
// Spring 2024



/*
    Cupcake in the Maze:

    If a thread is presented w/ cupcake it can choose to eat or not to eat
    If a thread is presented w/ empty plate it can choose to replace the cupcake or not
        If cupcake is replaced the thread can choose to eat or not before leaving
    We want each thread to see the cupcake at least once
    
    Each thread eats the cupcake on its first time going through the maze
    Every thread replaces the cupcake if it sees an empty plate
    If a thread has already eaten a cupcake it passes on all future cupcakes

    The prompt says "a servant comes to replace the cupcake"
    So everytime the servant replaces the cupcake, "the servant" increments a counter
    When the counter = n, all guests have gone through the labyrinth
 */

 /*
    Viewing the Crystal Vase:

    Make a lock
 
  */

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class PartyGuest extends Thread
{
    // Static variables to be used for a random sleep interval
    // I'll adjust these to fit properly
    private final static int TIMEMIN = 40;
    private final static int TIMEMAX = 60;

    private MinotaurBirthday birthdayParty;  // Reference to the shared party
    private boolean jeetJet;  // It sounds like "Did you eat yet"
    private boolean mazing;  // If the guess is currently in the maze or not

    public boolean getJeetJet()
    {
        return jeetJet;
    }

    public PartyGuest(MinotaurBirthday party)
    {
        birthdayParty = party;
        jeetJet = false;
    }

    @Override
    public void run()
    {
        while (!birthdayParty.isFinished())
        {
            
            mazing = birthdayParty.gateKeeping();
            if (mazing)
            {
                // Gotta spend the time to go through the maze
                try {
                    sleep((int)((Math.random() * (TIMEMAX - TIMEMIN)) + TIMEMIN));
                } catch (InterruptedException e) {
                    System.out.println("A guest has died in the maze, contact your lawyers");
                }

                // They've reached the cupcake
                // If the guest has not eaten yet, they're gonna eat
                jeetJet = birthdayParty.cupcakeCheck(!jeetJet);

                // try {
                //     sleep(5000);
                // } catch (InterruptedException e) {
                //     System.out.println("A guest has died in the maze, contact your lawyers");
                // }

                mazing = false; // They leave
            }
        }
    }
}

class VaseViewingGuest extends Thread
{
    private final static int TIMEMIN = 40;
    private final static int TIMEMAX = 60;

    private MinotaurBirthday birthdayParty;  // The reference we require

    public VaseViewingGuest(MinotaurBirthday party)
    {
        birthdayParty = party;
    }

    @Override
    public void run()
    {
        while (true)
        {
            if (birthdayParty.getSign())
            {
                // Flip the sign to BUSY
                birthdayParty.setSign(false);

                // Spend some time looking at the vase
                try {
                    sleep((int)((Math.random() * (TIMEMAX - TIMEMIN)) + TIMEMIN));
                } catch (InterruptedException e) {
                    System.out.println("A guest has died in the vase room, contact your lawyers");
                }

                // Flip the sign back to AVAILABLE
                birthdayParty.setSign(true);
            }
            else
            {
                // Talk to some other guests while you wait for the vase room to be open
                try {
                    sleep((int)((Math.random() * (TIMEMAX - TIMEMIN)) + TIMEMIN));
                } catch (InterruptedException e) {
                    System.out.println("A guest has died in the kitchen, contact your lawyers");
                }
            }
        }
    }
}

public class MinotaurBirthday extends Thread
{
    // This might be replaced by user input,
    // But if not just go ahead and edit this value directly for testing
    // This is the number of guests btw
    private final static int userInputProbably = 10;  

    // Static variables for the bounds of random time waiting to send the next guest into the maze
    private final static int TIMEMIN = 50;
    private final static int TIMEMAX = 60;

    private int cupcakeCount;  // Number of times "The Servants" have replaced the cupcake
    private int numGuests;  // The number of guests (threads)
    private boolean cupcakeExists;  // If a cupcake is on the plate or not
    private boolean finished;  // Have all the guests been through the maze?

    private Lock cupcakeLock = new ReentrantLock();  // I've used reentrant locks before, I like em
    private Lock mazeLock = new ReentrantLock();  // This one is for entering the maze
    private List<PartyGuest> guestList;  // Gotta have a guest list

    // Vase stuff
    private boolean sign;  // The sign that says AVAILABLE (true) or BUSY (false)
    private List<VaseViewingGuest> vaseGuestList;  // New and shiny guest list for the shiny vase

    public MinotaurBirthday(int n)
    {
        cupcakeCount = 0;
        numGuests = n;
        cupcakeExists = true;
        finished = false;
        guestList = new ArrayList<>();

        sign = true;
    }

    // The Minotaur takes one guest and holds them until he decides they can enter the maze
    public boolean gateKeeping()
    {
        mazeLock.lock();

        try {
            sleep((int)((Math.random() * (TIMEMAX - TIMEMIN)) + TIMEMIN));
        } catch (InterruptedException e) {
            System.out.println("The Minotaur has died, party canceled");
        }        

        mazeLock.unlock();
        return true;
    }

    public boolean isFinished()
    {
        return finished;
    }

    public boolean cupcakeCheck(boolean gonnaEat)
    {
        cupcakeLock.lock();

        if (cupcakeExists)
        {
            if (gonnaEat)
                cupcakeExists = false;
        }
        else 
        {
            // Plate is empty, call the servant
            replaceCupcake();
            if (gonnaEat)
                cupcakeExists = false;
        }

        cupcakeLock.unlock();
        return true;
    }

    // "The Servant"
    public void replaceCupcake()
    {
        cupcakeExists = true;
        cupcakeCount++;

        if (cupcakeCount == numGuests)
        {
            finished = true;
            mazeLock.lock();
            cupcakeLock.lock();
        }
    }

    public boolean getSign()
    {
        return sign;
    }

    public void setSign(boolean real)
    {
        sign = real;
    }

    public static void main(String [] args)
    {
        // It's a party!
        MinotaurBirthday birthdayParty = new MinotaurBirthday(userInputProbably);

        // Make and run the threads
        for (int i = 0; i < userInputProbably; i ++)
        {
            birthdayParty.guestList.add(new PartyGuest(birthdayParty));
        }

        for (int i = 0; i < userInputProbably; i++)
            birthdayParty.guestList.get(i).run();


        while(!birthdayParty.finished)
        {
            // The Minotaur waits for the servant to let him know the guests are finished
        }

        // Testing to see if they've all eaten and therefore have gone through the maze
        // for (int i = 0; i < userInputProbably; i++)
        // {
        //     System.out.println((birthdayParty.guestList.get(i).getJeetJet()) ? 
        //     "Guest " + i + " has eaten" : "Guest " + i + " has not eaten");
        // }

        for (int i = 0; i < userInputProbably; i++)
            birthdayParty.guestList.get(i).interrupt();
        
        // All of the guests have seen the maze, and have had a cupcake
        // Time to move onto the vase exhibit

        // Remaking the guestlist seems pretty innefficient but w/e
        birthdayParty.vaseGuestList = new ArrayList<>();

        for (int i = 0; i < userInputProbably; i++)
            birthdayParty.vaseGuestList.add(new VaseViewingGuest(birthdayParty));
        
        for (int i = 0; i < userInputProbably; i++)
            birthdayParty.vaseGuestList.get(i).run();
        
        // The homework doesn't tell me when to stop so I'll just leave 
        // it to you to CTRL C out of this program when you see fit
    }
}