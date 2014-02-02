ServerInterconnect
==================
A simple non-ssl encryption solution for sensitive data on a mesh-like network. Support will soon be added for: whitelist and blacklist of IP adresses, specific target packet types, and double encryption.

How it works
==================
1. Client probes the network for a server to connect to
2. When it finds a server, it connects to the available socket
3. The server acknowledges this connection by establishing a new EvaluationThread to determine whether or not the client is indeed a legimate client that has the right credentials
4. To test this, the server sends a RequestAuthenticationPacket to the client
5. The client immediately sends an AuthenticationPacket containing its credentials
6. The encryption key, padding and first analyzed, and, if deemed fit for decryption, are then decrypted and compared to the credentials in the server's config.xml file. If the credentials match, the socket is then passed to a new ServerThread which begins the communication between the two verified commincators (All packets that either the server/client have tried to send to each other thus far have been queued up, and are now sent)

Packet Sending/Recieving
==================
1. Client generates a packet, and a packetheader (provides information about the packet) and encyptes them both using the IV bytes and SecretKey provided in the .xml file
2. The packet is then sent to the server, where the padding and SecretKey are checked, and if deemed fit for decryption, it is decrypted. The PacketHeader and the Packet are then checked against each other to ensure packet integrity. Then it is sent to all the clients that are specified in the recipients list, and if the list is null, it is sent to all clients connected
3. A similar process occurs when clients recieve a packet, and a PacketRecievedEvent is called for use by the implementor
