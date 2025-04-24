# Java_Hairdresser_Concurrency
Java Hairdresser Concurrency project solving shared resource problems 

Given problem solves hairdresser like system. Clients fill the que in FIFO style (with given size), if the queue is full it won't accept any more clients. Next for N number of chairs we invoke the same number of scheduler threads. Each one of them checks for the client in the queue and tries to serve him according to the actual state of resources (if available then he will be served, if not it waits until reasources are available). After the client is served the resources are freed and the nest customer is served.

### How to run?
1. Download the repository 
2. Go to the project root
3. Build: ```mvn clean package```
4. Run: ```java -jar target/Test-1.0-SNAPSHOT.jar```

### Demo
https://github.com/user-attachments/assets/ea9eb967-c92f-4193-be8e-4b137bdafcb3

