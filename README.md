# Java_Hairdresser_Concurrency
### Project Overview
This Java project models a hairdresser salon system with concurrent client scheduling, addressing shared resource management challenges. The system implements a thread-safe queue with a fixed capacity, adhering to the FIFO (First-In-First-Out) principle. When the queue reaches maximum capacity, new clients are rejected.
The solution employs multiple scheduler threads, each assigned to a hairdresser chair, which continuously poll the queue for clients. Upon availability of resources (a free chair and a matching hairdresser), a client is served. After service completion, resources are released, allowing the next client in the queue to be processed.

**Key Features**:
- Thread-safe queue with a fixed maximum size to prevent overloading.
- Dynamic scheduler threads managing client assignments based on resource availability.
- Resource synchronization ensuring no race conditions occur when accessing chairs or hairdressers.
- Efficient client handling, where idle chairs immediately process the next available client.

**Technical Implementation**:
- Concurrency Control: Uses synchronized blocks to manage shared resources.
- Thread Management: Each hairdresser chair operates in a separate thread, continuously checking for clients.
- Graceful Rejection: Clients are denied entry if the queue is full, simulating real-world capacity constraints.
- This project serves as a practical demonstration of multi-threading, synchronization, and resource management in Java, applicable to real-world scheduling systems.

### How to run?
1. ```git clone https://github.com/KrysztofN/Java_Hairdresser_Concurrency.git```
2. ```cd Java_Hairdresser_Concurrency```
3. Go to the project root
4. *If you want to build yourself (ensure you have maven installed) Build: ```mvn clean package```
5. Linu) Run:```java -jar target/Test-1.0-SNAPSHOT.jar```
   <br>
   Windows Run: ```jawa -jar target/Test-1.0-SNAPSHOT.jar```

### Demo
https://github.com/user-attachments/assets/ea9eb967-c92f-4193-be8e-4b137bdafcb3

