Monologue
=========

![Monologue logo](assets/logo.png)

Monologue is a console-based social networking application.
The application is an exercise to demonstrate coding style, and is not intended for production use.

Limitations
-----------

 * The application doesn't work over the network, and all the users must share the same terminal.
 * Users are created on the fly from their first posts, and no pre-defined list of users exists.
 * The messages are in memory only and lost every time app is stopped.
 
How to run
----------

You need a [Java JDK 8 or newer][jdk-link] and [Maven][maven-link] to run the app.

* To package: `mvn package`

* To run: `cd target && java -jar monologue.jar`


Usage
-----

Monologue supports 4 commands : **posting**, **reading**, **following** and **wall**

## Posting

You can publish a message to a user personal timeline, by providing your username:

    <username> -> <message>

*Example: Alice can publish message to her personal timeline, and Bob to his personal timeline*


    > Alice -> I love the weather today
    > Bob -> Damn! We lost!
    > Bob -> Good game though.


## Reading

You can read the personal timeline of any user:

    <user name>

*Example: You can view Alice timeline and then Bob timeline*


    > Alice
    I love the weather today (5 minutes ago)
    > Bob
    Good game though. (1 minute ago)
    Damn! We lost! (2 minutes ago)


## Following

You can follow another user with the following command:

    <user name> follows <another user>

Following will aggregate the user timeline with yours in your wall (see _Wall_ section for more info),

*Example:  Charlie's timeline will change depending on who he follows*


    > Charlie -> I'm in New York today! Anyone want to have a coffee?
    > Charlie follows Alice
    > Charlie wall
    Charlie - I'm in New York today! Anyone want to have a coffee? (2 seconds ago)
    Alice - I love the weather today (5 minutes ago)
    > Charlie follows Bob
    > Charlie wall
    Charlie - I'm in New York today! Anyone wants to have a coffee? (15 seconds ago)
    Bob - Good game though. (1 minute ago)
    Bob - Damn! We lost! (2 minutes ago)
    Alice - I love the weather today (5 minutes ago)


## Wall

You can display a user wall (aggregation of his timeline and the ones he/she follows):

    <user name> wall


    > Charlie wall
    Charlie - I'm in New York today! Anyone wants to have a coffee? (15 seconds ago)
    Bob - Good game though. (1 minute ago)
    Bob - Damn! We lost! (2 minutes ago)
    Alice - I love the weather today (5 minutes ago)


[jdk-link]: http://www.oracle.com/technetwork/java/javase/downloads/
[maven-link]: https://maven.apache.org/
