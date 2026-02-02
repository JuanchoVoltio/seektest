Welcome!

This lib allows you to use and implement a message sender for different channels.

1. Register all the senders for different channels using an instance of SenderRegistry. Define which one are default channels.
2. Create a Message object
3. Invoke the send() method for the desired channel(s).

You can create your own implementations for senders using the interface IMessageSender, or an implementation for a specific channel and/or
provider.

1. Define a new IMessageSender class
2. Create a new Channel Provider for the new sender
    a) For a new implementation of an already existing channel's provider extend the corresponding abstract class.
    b) For a new provider of a new Channel, you must define also the default base class for your provider