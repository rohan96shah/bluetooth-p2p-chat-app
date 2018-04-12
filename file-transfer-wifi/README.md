# Automate the process of sending files using Wifi and Bluetooth Connection from Laptop ‘A’ to Laptop ‘B’ and vice versa.

### Description
<ol>
  <li>Download bluecove-2.1.2.jar file.</li>
  <li>Open Netbeans</li>
  <li>Add the jar (zipped the file to the project’s run time and compile time</li>.
  <li>Create a new Java project called “Bluetooth”. After creating the project, the package name should be “bluetooth” and there should be    one class file called “Start.java”.</li>
  <li>Create four new java classes inside the same package - “SampleSPPClient.java”, “SampleSPPServer.java”, “FileTransferClient.java”, and   “FileTransferServer.java”.</li>
  <li>Copy the code from our files and paste it into the respective files created. For example, code from Start.java will be pasted into      class Start (which was created in step 2).</li>
  <li>Perform steps 1-4 on Laptop ‘B’.</li>
  <li>Turn on the bluetooth for Laptop ‘A’ and Laptop ‘B’ and pair the devices (i.e. make sure the two laptops are paired before performing   step 7).</li>
  <li>Change the IP address in the “SampleSPPClient.java” and “SampleSPPServer.java” by changing the value of “My_IP” variable before         running the program.</li>
  <li>Run the file ‘Start.java’ on both the laptops.</li>
  <li>After connection has been established, enter “download “ filename to download the file desired. Also, you can enter “index” to get a    list of files in the directory.</li>
</ol>

Assumptions: We have implemented Random Number generator in the Start.java file that will generate Boolean values, that is 0 and 1. We are assuming that it will generate an equal number of 0’s and 1’s over a period of time. 

